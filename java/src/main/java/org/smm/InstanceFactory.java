package org.smm;

import com.mongodb.DB;

public interface InstanceFactory<E> {

	E create(Class<?>[] paramTypes, Object[] arguments, DB db, MongoMapper mongoMapper);

}
