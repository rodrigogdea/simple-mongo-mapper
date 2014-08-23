package org.mp.smp;

import org.bson.types.ObjectId;

import com.mongodb.DB;

public class ObjectIdFieldBinder<E> extends SimpleFieldBinder<E> {

	private static final String ID_PROPERTY_NAME = "_id";

	public ObjectIdFieldBinder(Class<E> entityClass, String propertyName) {
		super(entityClass, propertyName);
	}

	@Override
	public void bindEntityToDbObject(final Object entity, MappingDbObject entityDbo, DB db,
			MongoMapper mongoMapper) {

		String id = (String) this.getFieldValue(entity);

		if (!(id == null || id == "")) {
			entityDbo.put(ID_PROPERTY_NAME, new ObjectId(id));
		} else {
			entityDbo.addPropertyListener(ID_PROPERTY_NAME, new PropertyListener() {

				@Override
				public void onSetValue(String propertyName, Object value) {
					ObjectId objectId = (ObjectId) value;
					setFieldValue(entity, objectId.toString());
				}
			});
		}

	}

	@Override
	public void bindDbobjectToEntity(MappingDbObject entityDbo, Object entity, DB db,
			MongoMapper mongoMapper) {
		ObjectId objectId = (ObjectId) entityDbo.get(ID_PROPERTY_NAME);
		if (objectId != null) {
			this.setFieldValue(entity, objectId.toString());
		}
	}

}
