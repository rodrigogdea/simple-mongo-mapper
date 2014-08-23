package org.smm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MappingDbObject extends BasicDBObject {
	private static final long serialVersionUID = 1L;
	
	private DBObject dbObject;

	private Map<String, PropertyListener> propertyListeners = new HashMap<String, PropertyListener>();

	public MappingDbObject() {
		this(new BasicDBObject());
	}
	
	public MappingDbObject(DBObject dbObject) {
		super();
		assertNotNull(dbObject);
		this.dbObject = dbObject;
	}

	private void assertNotNull(DBObject dbObject) {
		if(dbObject == null){
			throw new IllegalArgumentException("dbObject is null.");
		}
	}

	public boolean containsField(String fieldName) {
		return dbObject.containsField(fieldName);
	}

	public Object get(String arg0) {
		return dbObject.get(arg0);
	}

	public boolean isPartialObject() {
		return dbObject.isPartialObject();
	}

	public Set<String> keySet() {
		return dbObject.keySet();
	}

	public void markAsPartialObject() {
		dbObject.markAsPartialObject();
	}

	public Object put(String propertyName, Object value) {
		Object object = dbObject.put(propertyName, value);
		
		PropertyListener listener = this.propertyListeners.get(propertyName);
		
		if(listener != null){
			listener.onSetValue(propertyName, value);
		}
		
		return object;
	}

	public void putAll(DBObject arg0) {
		dbObject.putAll(arg0);
	}

	public void putAll(Map arg0) {
		dbObject.putAll(arg0);
	}

	public Object removeField(String arg0) {
		return dbObject.removeField(arg0);
	}

	public Map toMap() {
		return dbObject.toMap();
	}

	@Deprecated
	public boolean containsKey(String arg0) {
		return dbObject.containsKey(arg0);
	}

	public void addPropertyListener(String propertyName, PropertyListener propertyListener) {
		this.propertyListeners.put(propertyName, propertyListener);
	}
	
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return this.dbObject.toMap().entrySet();
	}
}

