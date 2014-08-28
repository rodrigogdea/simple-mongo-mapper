package org.smm;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class SimpleMongoMapperTest {

    protected final DB smpDb;

    public SimpleMongoMapperTest() {
	try {
	    smpDb = new MongoClient().getDB("simple-mongo-mapper");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
