package org.mp.smp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SimpleMongoMapper implements MongoMapper {

	private RegExpMap<EntityMapper<?>> entityMappersByColl = new RegExpMap<EntityMapper<?>>();
	private Map<String, EntityMapper<?>> entityMappersByName = new HashMap<String, EntityMapper<?>>();
	private Map<Class<?>, EntityMapper<?>> entityMappersByType = new HashMap<Class<?>, EntityMapper<?>>();

	/**
	 * Add An {@link EntityMapper} associated with a collection.
	 * 
	 * @param collectionName
	 * @param entityMapper
	 */
	public void addEntityMapper(String collectionName, EntityMapper<?> entityMapper) {
		this.entityMappersByColl.put(Pattern.compile("^" + collectionName + "$"), entityMapper);
		this.addEntityMapper(entityMapper);
	}

	/**
	 * Add An {@link EntityMapper} associated with a collection given as
	 * {@link Pattern}.
	 * 
	 * @param collectionName
	 * @param entityMapper
	 */
	public void addEntityMapper(Pattern collectionNameAsPattern, EntityMapper<?> entityMapper) {
		this.entityMappersByColl.put(collectionNameAsPattern, entityMapper);
		this.addEntityMapper(entityMapper);
	}

	/**
	 * Add An {@link EntityMapper} that does not associated with any collection.
	 * 
	 * @param entityMapper
	 */
	public void addEntityMapper(EntityMapper<?> entityMapper) {
		this.entityMappersByType.put(entityMapper.getMappedType(), entityMapper);
		this.entityMappersByName.put(entityMapper.getName(), entityMapper);
	}

	@Override
	public <E> E findOne(String collectionName, DBObject filter, DB db) throws ObjectNotFoundException {

		LocalCache localCache = LocalCache.get();
		EntityKey entityKey = new EntityKey(collectionName, filter);
		Object found = localCache.find(entityKey);

		if (found != null) {
			return (E) found;
		}

		EntityMapper<E> entityMapper = findEntityMapperByColl(collectionName);

		DBCollection entities = db.getCollection(collectionName);
		DBObject objectFound = entities.findOne(filter);
		assertObjectFound(objectFound, collectionName, filter);
		MappingDbObject entityDbo = new MappingDbObject(objectFound);
		E entity = entityMapper.toEntity(entityDbo, db, this);

		localCache.put(entityKey, entity);

		return entity;
	}

	@Override
	public void save(String collectionName, Object entity, DB db) {
		EntityMapper<Object> entityMapper = findEntityMapperByColl(collectionName);

		DBCollection entities = db.getCollection(collectionName);
		DBObject dbObject = entityMapper.toDBObject(entity, db, this);
		entities.save(dbObject);
		LocalCache.get().invalidate();
	}

	@Override
	public void update(String collectionName, Object entity, DBObject criteria, DB db,
			boolean insertIfNotExists) {
		EntityMapper<Object> entityMapper = findEntityMapperByColl(collectionName);

		DBCollection entities = db.getCollection(collectionName);
		DBObject dbObject = entityMapper.toDBObject(entity, db, this);
		entities.update(criteria, dbObject, insertIfNotExists, false);
		LocalCache.get().invalidate();
	}

	@Override
	public <E> List<E> findAll(String collectionName, int skip, int limit, DB db) {
		EntityMapper<E> entityMapper = findEntityMapperByColl(collectionName);

		DBCollection entitiesColl = db.getCollection(collectionName);
		DBCursor cursor = entitiesColl.find().skip(skip).limit(limit);

		List<E> entities = new ArrayList<E>();
		dbCursoToList(entities, cursor, entityMapper, db);
		return entities;
	}

	@Override
	public <E> List<E> find(String collectionName, DBObject filter, DB db) {
		return find(collectionName, filter, 0, 0, db);
	}

	@Override
	public <E> List<E> find(String collectionName, DBObject filter, int skip, int batchSize, DB db) {
		EntityMapper<E> entityMapper = this.findEntityMapperByColl(collectionName);

		DBCollection entitiesColl = db.getCollection(collectionName);
		DBCursor dbCursor = entitiesColl.find(filter).skip(skip).limit(batchSize);

		List<E> entities = new ArrayList<E>();
		dbCursoToList(entities, dbCursor, entityMapper, db);

		return entities;
	}

	@Override
	public <E> Pagination<E> createPagination(String collectionName, int pageSize, DB db) {
		return createPagination(collectionName, pageSize, new BasicDBObject(), db);
	}

	@Override
	public <E> Pagination<E> createPagination(String collectionName, int pageSize, DBObject filter,
			DB db) {
		return new SimplePagination<E>(collectionName, filter, pageSize, this, db);
	}

	@Override
	public String getEntityMapperNameFor(Object entity) {
		return findEntityMapperByType(entity.getClass()).getName();
	}

	@Override
	public DBObject ToDbObject(Object entity, DB db) {
		EntityMapper<Object> entityMapper = findEntityMapperByType(entity.getClass());
		DBObject dbObject = entityMapper.toDBObject(entity, db, this);
		return dbObject;
	}

	@Override
	public Object ToEntity(String entityMapperName, MappingDbObject entityDbo, DB db) {
		EntityMapper<Object> entityMapper = findEntityMapperByName(entityMapperName);
		Object entity = entityMapper.toEntity(entityDbo, db, this);
		return entity;
	}

	private <E> void dbCursoToList(List<E> entities, DBCursor dbCursor,
			EntityMapper<E> entityMapper, DB db) {
		for (DBObject dbObject : dbCursor) {
			E entity = entityMapper.toEntity(new MappingDbObject(dbObject), db, this);
			entities.add(entity);
		}
	}

	private void assertObjectFound(DBObject objectFound, String collectionName, DBObject filter) throws ObjectNotFoundException{
		if (objectFound == null) {
			throw new ObjectNotFoundException(String.format("Object not found in collection '%s' with this filter: %s", collectionName, filter));
		}
	}

	@SuppressWarnings("unchecked")
	private <E> EntityMapper<E> findEntityMapperByColl(String collectionName) {
		EntityMapper<?> entityMapper = this.entityMappersByColl.get(collectionName);
		if (entityMapper == null) {
			throw new IllegalArgumentException("Does not exist any EntityMapper for collection "
					+ collectionName);
		}
		return (EntityMapper<E>) entityMapper;
	}

	@SuppressWarnings("unchecked")
	private <E> EntityMapper<E> findEntityMapperByName(String entityMapperName) {
		EntityMapper<?> entityMapper = this.entityMappersByName.get(entityMapperName);
		if (entityMapper == null) {
			throw new IllegalArgumentException("Does not exist any EntityMapper with this name: "
					+ entityMapperName);
		}
		return (EntityMapper<E>) entityMapper;
	}

	@SuppressWarnings("unchecked")
	private <E> EntityMapper<E> findEntityMapperByType(Class<?> mappedType) {
		EntityMapper<?> entityMapper = this.entityMappersByType.get(mappedType);
		if (entityMapper == null) {
			throw new IllegalArgumentException("Does not exist any EntityMapper for the class: "
					+ mappedType.getName());
		}
		return (EntityMapper<E>) entityMapper;
	}

}
