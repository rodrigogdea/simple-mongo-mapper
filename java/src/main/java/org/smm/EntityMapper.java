package org.smm;

import com.mongodb.DB;
import com.mongodb.DBObject;

public interface EntityMapper<E> {

	E toEntity(MappingDbObject entityDbo, DB db, MongoMapper mongoMapper);

	DBObject toDBObject(E entity, DB db, MongoMapper mongoMapper);

	String getName();

	Class<E> getMappedType();

}