package org.smp;

import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smp.model.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import org.smm.*;

public class PaginationTest {

    private static final String USER_COLL = "User";
    private static MongoMapper mongoMapper = createMapper();
    private DB smpDb;

    @Before
    public void before() throws Exception {
        smpDb = new Mongo().getDB("simple-mongo-mapper");
        smpDb.getCollection(USER_COLL).drop();
    }

    @Test
    public void createPaginationFor20UsersAndPageSize10() {
        create20Users();

        Pagination<User> pagination = mongoMapper.createPagination(USER_COLL, 10, smpDb);

        Assert.assertEquals(10, pagination.getPage().size());
        Assert.assertEquals(2, pagination.getAmountOfPages());
        Assert.assertEquals(1, pagination.getCurrentPageNumber());
    }

    @Test
    public void createPaginationFor20UsersAndPageSize10AndGetThe2ndPage() {
        create20Users();

        Pagination<User> pagination = mongoMapper.createPagination(USER_COLL, 10, smpDb);

        pagination.moveToNextPage();

        List<User> page = pagination.getPage();
        Assert.assertEquals(10, page.size());
        Assert.assertEquals(false, page.get(0).isAdmin());
        Assert.assertEquals(false, page.get(9).isAdmin());
        Assert.assertEquals(2, pagination.getAmountOfPages());
        Assert.assertEquals(2, pagination.getCurrentPageNumber());
    }

    @Test
    public void createPaginationFor20UsersAndPageSize7AndGetThe2ndPage() {
        create20Users();

        Pagination<User> pagination = mongoMapper.createPagination(USER_COLL, 7, smpDb);

        pagination.moveToNextPage();

        List<User> page = pagination.getPage();
        Assert.assertEquals(7, page.size());
        Assert.assertEquals(true, page.get(0).isAdmin());
        Assert.assertEquals(false, page.get(6).isAdmin());
        Assert.assertEquals(3, pagination.getAmountOfPages());
        Assert.assertEquals(2, pagination.getCurrentPageNumber());
    }

    @Test
    public void createPaginationFor20UsersAndPageSize7AndGetThe3rdPage() {
        create20Users();

        Pagination<User> pagination = mongoMapper.createPagination(USER_COLL, 7, smpDb);

        pagination.moveToNextPage();
        pagination.moveToNextPage();

        List<User> page = pagination.getPage();
        Assert.assertEquals("Page Size", 6, page.size());
        Assert.assertEquals("First isAdmin User in the Page", false, page.get(0).isAdmin());
        Assert.assertEquals("Last isAdmin User in the Page", false, page.get(5).isAdmin());
        Assert.assertEquals("Current Amount Of Pages", 3, pagination.getAmountOfPages());
        Assert.assertEquals("Current Page Number", 3, pagination.getCurrentPageNumber());
    }

    @Test
    public void createPaginationFor20UsersAndPageSize3AndGoDirectlyToThe4thPage() {
        create20Users();

        Pagination<User> pagination = mongoMapper.createPagination(USER_COLL, 3, smpDb);

        pagination.goToPage(4);

        List<User> page = pagination.getPage();
        Assert.assertEquals("Page Size", 3, page.size());
        Assert.assertEquals("First isAdmin User in the Page", true, page.get(0).isAdmin());
        Assert.assertEquals("Last isAdmin User in the Page", false, page.get(2).isAdmin());
        Assert.assertEquals("Current Amount Of Pages", 7, pagination.getAmountOfPages());
        Assert.assertEquals("Current Page Number", 4, pagination.getCurrentPageNumber());
    }

    @Test
    public void createPaginationFor20UsersAndPageSize3AndFilterByIsAdminAndGoDirectlyToThe3rdPage() {
        create20Users();

        Pagination<User> pagination = mongoMapper.createPagination(USER_COLL, 3, new BasicDBObject(
                "isAdmin", true), smpDb);

        pagination.goToPage(3);

        List<User> page = pagination.getPage();
        Assert.assertEquals("Page Size", 3, page.size());
        Assert.assertEquals("First isAdmin User in the Page", true, page.get(0).isAdmin());
        Assert.assertEquals("Last isAdmin User in the Page", true, page.get(2).isAdmin());
        Assert.assertEquals("Current Amount Of Pages", 4, pagination.getAmountOfPages());
        Assert.assertEquals("Current Page Number", 3, pagination.getCurrentPageNumber());
    }

    private static MongoMapper createMapper() {
        SimpleMongoMapper mapper = new SimpleMongoMapper();

        SimpleEntityMapper<User> entityMapper = new SimpleEntityMapper<>(User.class);
        entityMapper.addBinder(new SimpleFieldBinder<>(User.class, "name", true, String.class));
        entityMapper.addBinder(new SimpleFieldBinder<>(User.class, "isAdmin", true,
                boolean.class));

        mapper.addEntityMapper(USER_COLL, entityMapper);

        return mapper;
    }

    private void create20Users() {

        Random random = new Random();
        for (int iii = 0; iii < 10; iii++) {
            mongoMapper.save(USER_COLL, new User("User-Admin-" + random.nextInt(), true), smpDb);
        }

        for (int iii = 0; iii < 10; iii++) {
            mongoMapper.save(USER_COLL, new User("User-" + random.nextInt(), false), smpDb);
        }

    }

}
