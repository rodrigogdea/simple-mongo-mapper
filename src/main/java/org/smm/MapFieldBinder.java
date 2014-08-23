package org.smm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

public class MapFieldBinder<E> extends SimpleFieldBinder<E> {

	private MapFieldConverter mapFieldConverter;

	public MapFieldBinder(Class<E> entityClass, String propertyName,
			MapFieldConverter mapFieldConverter) {
		super(entityClass, propertyName);
		this.mapFieldConverter = mapFieldConverter;
	}

	@Override
	public void bindEntityToDbObject(Object entity, MappingDbObject entityDbo, DB db,
			MongoMapper mongoMapper) {
		DBObject mapDbObject = new BasicDBObject();
		Map<Object, Object> map = (Map<Object, Object>) this.getFieldValue(entity);

		if (map != null) {
			for (Map.Entry<Object, Object> mapEntry : map.entrySet()) {
				addMapEntryToMapDbObject(mapEntry, mapDbObject, db, mongoMapper);
			}
			entityDbo.put(this.getFieldName(), mapDbObject);
		}
	}

	@Override
	public void bindDbobjectToEntity(MappingDbObject entityDbo, Object entity, DB db,
			MongoMapper mongoMapper) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		DBObject mapDbObject = (DBObject) entityDbo.get(this.getFieldName());

		if (mapDbObject != null) {
			Set<Map.Entry<Object, Object>> entrySet = mapDbObject.toMap().entrySet();

			for (Map.Entry<Object, Object> entry : entrySet) {
				addDbObjectEntryToMap(entry, map, db, mongoMapper);
			}
			this.setFieldValue(entity, map);
		}
	}

	protected void addDbObjectEntryToMap(Map.Entry<Object, Object> dbObjectEntry,
			Map<Object, Object> map, DB db, MongoMapper mongoMapper) {
		mapFieldConverter.addDbObjectEntryToMap(dbObjectEntry, map, db, mongoMapper);
	}

	protected void addMapEntryToMapDbObject(Map.Entry<Object, Object> mapEntry,
			DBObject mapDbObject, DB db, MongoMapper mongoMapper) {
		mapFieldConverter.addMapEntryToMapDbObject(mapEntry, mapDbObject, db, mongoMapper);
	}

}
