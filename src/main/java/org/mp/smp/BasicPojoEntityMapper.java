package org.mp.smp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import com.mongodb.DB;
import com.mongodb.DBObject;

public class BasicPojoEntityMapper<E> implements EntityMapper<E> {

	private Class<E> typeToMap;
	private Collection<SimpleFieldBinder<E>> binders = new ArrayList<SimpleFieldBinder<E>>();

	public BasicPojoEntityMapper(final Class<E> typeToMap) {
		this.typeToMap = typeToMap;
		mapType();
	}

	private void mapType() {
		Field[] fields = typeToMap.getDeclaredFields();
		for (Field field : fields) {
			binders.add(new SimpleFieldBinder<E>(typeToMap, field.getName()));
		}
	}

	@Override
	public E toEntity(final MappingDbObject entityDbo, final DB db, final MongoMapper mongoMapper) {
		try {
			E instance = typeToMap.newInstance();

			for (SimpleFieldBinder<E> simpleFieldBinder : binders) {
				simpleFieldBinder.bindDbobjectToEntity(entityDbo, instance, db, mongoMapper);
			}

			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public DBObject toDBObject(final E entity, final DB db, final MongoMapper mongoMapper) {
		MappingDbObject mappingDbObject = new MappingDbObject();

		for (SimpleFieldBinder<E> simpleFieldBinder : binders) {
			simpleFieldBinder.bindEntityToDbObject(entity, mappingDbObject, db, mongoMapper);
		}

		return mappingDbObject;
	}

	@Override
	public String getName() {
		return typeToMap.getSimpleName();
	}

	@Override
	public Class<E> getMappedType() {
		return typeToMap;
	}

}
