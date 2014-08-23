package org.mp.smp;

import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBObject;

public interface MongoMapper {

	/**
	 * Try to find one Object that match with the specified filter.
	 * @param collectionName
	 * @param filter
	 * @param db
	 * @return
	 * @throws ObjectNotFoundException
	 */
	<E> E findOne(String collectionName, DBObject filter, DB db) throws ObjectNotFoundException;

	void save(String collectionName, Object entity, DB db);

	void update(String collectionName, Object entity, DBObject criteria, DB db,
			boolean insertIfNotExists);

	<E> List<E> findAll(String collectionName, int skip, int limit, DB db);

	<E> List<E> find(String collectionName, DBObject filter, DB db);

	<E> List<E> find(String collectionName, DBObject filter, int skip, int batchSize, DB db);

	<E> Pagination<E> createPagination(String collectionName, int pageSize, DB db);

	<E> Pagination<E> createPagination(String collectionName, int pageSize, DBObject filter, DB db);

	Object ToEntity(String entityMapperName, MappingDbObject entityDbo, DB db);

	DBObject ToDbObject(Object entity, DB db);

	String getEntityMapperNameFor(Object entity);

}