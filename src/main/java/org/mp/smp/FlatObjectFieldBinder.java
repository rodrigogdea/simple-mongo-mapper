package org.mp.smp;

import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBObject;

public class FlatObjectFieldBinder<E> extends SimpleFieldBinder<E> {

	protected static final String OBJECT_TYPE = "_type";

	public FlatObjectFieldBinder(Class<E> entityClass, String fieldName) {
		super(entityClass, fieldName);
	}

	public FlatObjectFieldBinder(Class<E> entityClass, String fieldName,
			boolean isConstructorParam, Class<?> ctorType) {
		super(entityClass, fieldName, null, new NullValueConverter(), isConstructorParam, ctorType);
	}

	@Override
	public void bindEntityToDbObject(Object entity, MappingDbObject entityDbo, DB db,
			MongoMapper mongoMapper) {
		Object value = this.getFieldValue(entity);

		if (value != null) {
			DBObject dbValue = entityValueToDbObject(value, db, mongoMapper);
			entityDbo.put(this.getFieldName(), dbValue);
		} else {
			entityDbo.put(this.getFieldName(), null);
		}
	}

	@Override
	public void bindDbobjectToEntity(MappingDbObject entityDbo, Object entity, DB db,
			MongoMapper mongoMapper) {
		if (!this.isConstructorParam()) {
			DBObject value = (DBObject) entityDbo.get(getFieldName());
			if (value != null) {
				Object subEntity = dbObjectValueToEntity(new MappingDbObject(value), db,
						mongoMapper);
				this.setFieldValue(entity, subEntity);
			}
		}
	}

	@Override
	public void addToParams(MappingDbObject entityDbo, List<Object> constructorParams, DB db,
			MongoMapper mongoMapper) {
		if (this.isConstructorParam()) {
			DBObject value = (DBObject) entityDbo.get(getFieldName());
			if (value != null) {
				Object subEntity = dbObjectValueToEntity(new MappingDbObject(value), db,
						mongoMapper);
				constructorParams.add(subEntity);
			}
		}
	}

	protected Object dbObjectValueToEntity(MappingDbObject value, DB db, MongoMapper mongoMapper) {
		String typeName = (String) value.get(OBJECT_TYPE);
		Object subEntity = mongoMapper.ToEntity(typeName, value, db);
		return subEntity;
	}

	protected DBObject entityValueToDbObject(Object value, DB db, MongoMapper mongoMapper) {
		DBObject dbValue = mongoMapper.ToDbObject(value, db);
		dbValue.put(OBJECT_TYPE, mongoMapper.getEntityMapperNameFor(value));
		return dbValue;
	}

}
