package org.smm;

import java.lang.reflect.Constructor;

import com.mongodb.DB;

public class DefaultInstanceFactory<E> implements InstanceFactory<E> {

	private Class<E> aType;

	public DefaultInstanceFactory(Class<E> aType) {
		this.aType = aType;
	}

	@Override
	public E create(Class<?>[] paramTypes, Object[] arguments, DB db, MongoMapper mongoMapper) {
		try {
			Constructor<E> declaredConstructor = aType.getDeclaredConstructor(paramTypes);
			return declaredConstructor.newInstance(arguments);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
