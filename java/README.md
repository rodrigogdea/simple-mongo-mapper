Simple Mongo Mapper
===================

Simple Mongo Mapper is just another MongoDB Mapper for java that let to map entities in a declarative and  non-intrusive way.


Quick start
-----------

``` java

// Creating and EntityMapper for a User class
SimpleEntityMapper<User> userEntityMapper = new SimpleEntityMapper<>(User.class);

// Binding fields "name" and "age" and also the constructor, eg: new User("John", 32); 
userEntityMapper.addBinder(new SimpleFieldBinder<>(User.class, "name", true, String.class));
userEntityMapper.addBinder(new SimpleFieldBinder<>(User.class, "age", true, Integer.class));

SimpleMongoMapper mongoMapper = new SimpleMongoMapper();
mongoMapper.addEntityMapper("users", userEntityMapper);

DB db = new MongoClient().getDB("test");

User user = new User("John", 32);

mongoMapper.save("users", user, db);


```
