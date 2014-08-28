package org.smm;

import java.lang.reflect.Field;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

public class ObjectReferenceFieldBinder<E> extends FlatObjectFieldBinder<E> {

	private static final String KEY_VALUE_PROPERTY = "_KeyValue";
	private String keyFieldName;
	private String foreignCollection;
	private String keyDbFieldName;

	public ObjectReferenceFieldBinder(Class<E> entityClass, String fieldName,
			String foreignCollection, String keyFieldName, String keyDbFieldName,
			boolean isConstructorParam, Class<?> ctorType) {
		super(entityClass, fieldName, isConstructorParam, ctorType);
		this.keyFieldName = keyFieldName;
		this.foreignCollection = foreignCollection;
		this.keyDbFieldName = keyDbFieldName;
	}

	public ObjectReferenceFieldBinder(Class<E> entityClass, String fieldName,
			String foreignCollection, String keyFieldName, boolean isConstructorParam,
			Class<?> ctorType) {
		this(entityClass, fieldName, foreignCollection, keyFieldName, keyFieldName,
				isConstructorParam, ctorType);
	}

	@Override
	protected DBObject entityValueToDbObject(Object entity, DB db, MongoMapper mongoMapper) {
		DBObject dbValue = new BasicDBObject();
		dbValue.put(KEY_VALUE_PROPERTY, this.getKeyValue(entity, db, mongoMapper));
		return dbValue;
	}

	@Override
	protected Object dbObjectValueToEntity(MappingDbObject value, DB db, MongoMapper mongoMapper) {
		Object key = value.get(KEY_VALUE_PROPERTY);

		return mongoMapper.findOne(this.foreignCollection, new BasicDBObject(keyDbFieldName, key),
				db);
	}

	private Object getKeyValue(Object entity, DB db, MongoMapper mongoMapper) {

		Field keyField = this.findField(entity.getClass(), this.keyFieldName);

		try {
			return keyField.get(entity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
