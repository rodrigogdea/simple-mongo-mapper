package org.mp.smp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBObject;

public class ListOfMappedEntityFieldBinder<E> extends SimpleFieldBinder<E> {

	private Class<?> mappedEntity;
	private String propertyName;

	public ListOfMappedEntityFieldBinder(final Class<E> entityClass, final String propertyName,
			final Class<?> mappedEntity) {
		super(entityClass, propertyName);
		this.propertyName = propertyName;
		this.mappedEntity = mappedEntity;
	}

	@Override
	public void bindDbobjectToEntity(final MappingDbObject entityDbo, final Object entity, final DB db,
			final MongoMapper mongoMapper) {
		List<?> jsonArray = (List<?>) getValueFromDbObject(entityDbo);
		if (jsonArray != null) {
			LinkedList<Object> listToBind = new LinkedList<>();
			for (Object object : jsonArray) {
				listToBind.add(mongoMapper.ToEntity(mappedEntity.getSimpleName(),
						new MappingDbObject((DBObject) object), db));
			}
			setFieldValue(entity, listToBind);
		}
	}

	@Override
	public void bindEntityToDbObject(final Object entity, final MappingDbObject entityDbo, final DB db,
			final MongoMapper mongoMapper) {
		Collection<?> listValue = (Collection<?>) getFieldValue(entity);
		if (listValue != null) {
			BasicDBList basicDBList = new BasicDBList();
			for (Object object : listValue) {
				basicDBList.add(mongoMapper.ToDbObject(object, db));
			}
			entityDbo.put(propertyName, basicDBList);
		}
	}
}
