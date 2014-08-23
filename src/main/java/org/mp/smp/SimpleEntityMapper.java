package org.mp.smp;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBObject;

public class SimpleEntityMapper<E> implements EntityMapper<E> {

	private List<SimpleFieldBinder<E>> binders;
	private Class<E> typeToMap;
	private InstanceFactory<E> instanceFactory;

	public SimpleEntityMapper(Class<E> aTypeToMap) {
		this(aTypeToMap, new DefaultInstanceFactory<E>(aTypeToMap));
	}

	public SimpleEntityMapper(Class<E> aTypeToMap, InstanceFactory<E> anInstanceFactory) {
		this.typeToMap = aTypeToMap;
		instanceFactory = anInstanceFactory;
		binders = new ArrayList<SimpleFieldBinder<E>>();
	}

	public E toEntity(MappingDbObject entityDbo, DB db, MongoMapper mongoMapper) {
		E entity = instantiateEntity(entityDbo, db, mongoMapper);

		for (SimpleFieldBinder<E> binder : this.binders) {
			binder.bindDbobjectToEntity(entityDbo, entity, db, mongoMapper);
		}

		return entity;
	}

	public DBObject toDBObject(E entity, DB db, MongoMapper mongoMapper) {
		MappingDbObject entityDbo = new MappingDbObject();
		for (SimpleFieldBinder<E> binder : this.binders) {
			binder.bindEntityToDbObject(entity, entityDbo, db, mongoMapper);
		}

		return entityDbo;
	}

	private E instantiateEntity(MappingDbObject entityDbo, DB db, MongoMapper mongoMapper) {
		List<Class<?>> paramTypes = new ArrayList<Class<?>>();
		List<Object> constructorParams = new ArrayList<Object>();

		for (SimpleFieldBinder<E> binder : this.binders) {
			binder.addToParams(entityDbo, constructorParams, db, mongoMapper);
			binder.addToParamTypes(paramTypes);
		}

		return instanceFactory.create(paramTypes.toArray(new Class<?>[] {}),
				constructorParams.toArray(), db, mongoMapper);
	}

	@Override
	public String getName() {
		return this.typeToMap.getSimpleName();
	}

	@Override
	public Class<E> getMappedType() {
		return this.typeToMap;
	}

	public void addBinder(SimpleFieldBinder<E> fieldBinder) {
		this.binders.add(fieldBinder);
	}

}
