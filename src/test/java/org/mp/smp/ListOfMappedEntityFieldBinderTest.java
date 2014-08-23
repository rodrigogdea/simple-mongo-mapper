package org.mp.smp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBCollection;

public class ListOfMappedEntityFieldBinderTest extends SimpleMongoMapperTest {

	private static final String PARENT_COLL = "ParentColl";

	@Before
	public void setUp() throws Exception {
		DBCollection chessBoardCollection = smpDb.getCollection(PARENT_COLL);
		chessBoardCollection.drop();
	}

	@Test
	public void testSaveAParentChildObject() {

		SimpleEntityMapper<Parent> parentEntityMapper = new SimpleEntityMapper<>(Parent.class);
		parentEntityMapper.addBinder(new ListOfMappedEntityFieldBinder<>(Parent.class, "children", Child.class));

	}

	public class Parent {
		private List<Child> children = new ArrayList<>();

		public List<Child> getChildreen() {
			return children;
		}

		public void setChildreen(final List<Child> childreen) {
			children = childreen;
		}
	}

	public class Child {
		private String name;

		public String getName() {
			return name;
		}

		public Child(final String name) {
			this.name = name;
		}
	}

}
