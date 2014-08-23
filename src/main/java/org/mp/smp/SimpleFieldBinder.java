package org.mp.smp;

import java.lang.reflect.Field;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBObject;

public class SimpleFieldBinder<E> {

	private Field field;
	private Class<?> ctorType;
	private boolean isConstructorParam;
	private FieldValueConverter fieldValueConverter;
	private String dbFieldName;

	public SimpleFieldBinder(Class<E> entityClass, String fieldName, String dbFieldName,
			FieldValueConverter aFieldValueConverter, boolean isConstructorParam, Class<?> ctorType) {
		this.dbFieldName = dbFieldName;
		this.field = findField(entityClass, fieldName);
		this.fieldValueConverter = aFieldValueConverter;
		this.isConstructorParam = isConstructorParam;
		this.ctorType = ctorType;
	}

	public SimpleFieldBinder(Class<E> entityClass, String fieldName,
			FieldValueConverter aFieldValueConverter, boolean isConstructorParam, Class<?> ctorType) {
		this(entityClass, fieldName, fieldName, aFieldValueConverter, isConstructorParam, ctorType);
	}

	public SimpleFieldBinder(Class<E> entityClass, String fieldName) {
		this(entityClass, fieldName, fieldName, new NullValueConverter(), false, null);
	}

	public SimpleFieldBinder(Class<E> entityClass, String fieldName, String dbFieldName) {
		this(entityClass, fieldName, dbFieldName, new NullValueConverter(), false, null);
	}

	public SimpleFieldBinder(Class<E> entityClass, String fieldName, boolean isConstructorParam,
			Class<?> ctorType) {
		this(entityClass, fieldName, fieldName, new NullValueConverter(), isConstructorParam,
				ctorType);
	}

	public SimpleFieldBinder(Class<E> entityClass, String fieldName, String dbFieldName,
			boolean isConstructorParam, Class<?> ctorType) {
		this(entityClass, fieldName, dbFieldName, new NullValueConverter(), isConstructorParam,
				ctorType);
	}

	public void bindDbobjectToEntity(MappingDbObject entityDbo, Object entity, DB db,
			MongoMapper mongoMapper) {
		if (!this.isConstructorParam()) {
			Object value = getValueFromDbObject(entityDbo);
			setFieldValue(entity, value);
		}
	}

	public void bindEntityToDbObject(Object entity, MappingDbObject entityDbo, DB db,
			MongoMapper mongoMapper) {
		entityDbo.put(dbFieldName, fieldValueConverter.convertToDb(getFieldValue(entity)));
	}

	public void addToParams(MappingDbObject entityDbo, List<Object> constructorParams, DB db,
			MongoMapper mongoMapper) {
		if (this.isConstructorParam()) {
			Object value = getValueFromDbObject(entityDbo);
			constructorParams.add(value);
		}
	}

	protected Object getValueFromDbObject(DBObject entityDbo) {
		return fieldValueConverter.convertToEntity(entityDbo.get(dbFieldName));
	}

	protected String getFieldName() {
		return field.getName();
	}

	protected void setFieldValue(Object entity, Object value) {
		try {
			this.field.set(entity, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Object getFieldValue(Object entity) {
		try {
			return this.field.get(entity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected boolean isConstructorParam() {
		return isConstructorParam;
	}

	protected Field findField(Class<?> entityClass, String fieldName) {
		try {
			Field field = entityClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			Class<?> superclass = entityClass.getSuperclass();
			if (superclass != null) {
				return this.findField(superclass, fieldName);
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	public void addToParamTypes(List<Class<?>> paramTypes) {
		if (paramTypes != null && isConstructorParam) {
			paramTypes.add(ctorType);
		}
	}

}
