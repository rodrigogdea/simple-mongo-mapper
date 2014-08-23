package org.smm;

import com.mongodb.DBObject;

public class EntityKey {

	private String collectionName;
	private DBObject filter;
	private int thisHash;

	public EntityKey(String collectionName, DBObject filter) {
		this.collectionName = collectionName;
		this.filter = filter;
		this.thisHash = this.collectionName.hashCode() + filter.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		EntityKey anEntityKey = (EntityKey) obj;

		return collectionName.equals(anEntityKey.collectionName)
				&& filter.equals(anEntityKey.filter);
	}

	@Override
	public int hashCode() {
		return this.thisHash;
	}
}