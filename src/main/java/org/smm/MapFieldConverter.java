package org.smm;

import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.DB;
import com.mongodb.DBObject;

public interface MapFieldConverter {

	public void addDbObjectEntryToMap(Entry<Object, Object> dbObjectEntry, Map<Object, Object> map,
			DB db, MongoMapper mongoMapper);

	public void addMapEntryToMapDbObject(Entry<Object, Object> mapEntry, DBObject mapDbObject,
			DB db, MongoMapper mongoMapper);

}
