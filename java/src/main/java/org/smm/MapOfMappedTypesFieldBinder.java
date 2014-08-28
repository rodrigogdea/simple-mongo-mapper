package org.smm;

import java.util.Map;

import com.mongodb.DB;
import com.mongodb.DBObject;

public class MapOfMappedTypesFieldBinder<E> extends MapFieldBinder<E> {

	private static final String DISCRIMINATOR_TYPE = "_type";

	public MapOfMappedTypesFieldBinder(Class<E> entityClass, String propertyName) {
		super(entityClass, propertyName, null);
	}

	@Override
	protected void addDbObjectEntryToMap(Map.Entry<Object, Object> entry, Map<Object, Object> map,
			DB db, MongoMapper mongoMapper) {

		DBObject dbObject = (DBObject) entry.getValue();
		String entityMapperName = (String) dbObject.get(DISCRIMINATOR_TYPE);
		Object subEntity = mongoMapper
				.ToEntity(entityMapperName, new MappingDbObject(dbObject), db);
		map.put(entry.getKey(), subEntity);
	}

	@Override
	protected void addMapEntryToMapDbObject(Map.Entry<Object, Object> mapEntry,
			DBObject mapDbObject, DB db, MongoMapper mongoMapper) {
		DBObject aDbObject = mongoMapper.ToDbObject(mapEntry.getValue(), db);
		aDbObject.put(DISCRIMINATOR_TYPE, mongoMapper.getEntityMapperNameFor(mapEntry.getValue()));
		mapDbObject.put(mapEntry.getKey().toString(), aDbObject);
	}

}
