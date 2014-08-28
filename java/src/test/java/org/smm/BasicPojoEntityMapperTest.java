package org.smm;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.smm.model.SomePojo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

public class BasicPojoEntityMapperTest {

	private DB db;
	private MongoMapper mongoMapper;

	@Before
	public void before() {
		db = Mockito.mock(DB.class);
		mongoMapper = Mockito.mock(MongoMapper.class);
	}

	@Test
	public void toDbObject() {
		BasicPojoEntityMapper<SomePojo> mapper = new BasicPojoEntityMapper<>(SomePojo.class);
		SomePojo entity = new SomePojo("Test");
		entity.setAge(23);
		DBObject dbObject = mapper.toDBObject(entity, db, mongoMapper);

		assertEquals("Test", dbObject.get("name"));
		assertEquals(23, dbObject.get("age"));
	}

	@Test
	public void toEntity() {
		BasicPojoEntityMapper<SomePojo> mapper = new BasicPojoEntityMapper<>(SomePojo.class);
		DBObject dbObject = new BasicDBObject("name", "Pepe");
		dbObject.put("age", 32);

		SomePojo somePojo = mapper.toEntity(new MappingDbObject(dbObject), db, mongoMapper);

		assertEquals(new Integer(32), somePojo.getAge());
	}

}
