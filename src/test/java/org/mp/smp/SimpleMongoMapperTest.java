package org.mp.smp;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class SimpleMongoMapperTest {

    protected final DB smpDb;

    public SimpleMongoMapperTest() {
	try {
	    smpDb = new Mongo().getDB("simple-mongo-mapper");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
