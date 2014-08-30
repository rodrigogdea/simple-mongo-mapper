simple-mongo-mapper
===================

Is a non-intrusive mongodb mapper for java & scala



# 1st Case

```scala
case class User(name: String, age: Int)

MongoMapper.register("Users", Entity[User])

val user = new User("John Coltrane", 28)

// implicit DB  
MongoMapper.for("Users").add(user)
```

```json

{ _id: 1234565, name: "John Coltrane", age: 28 }
```


# 2nd Case

```scala
case class Address(street: String)
case class User(name: String, age: Int, address: Address)

MongoMapper.register("Users", Entity[User])

val user = User("John Coltrane", 28, Address("Banfield"))

// implicit DB  
MongoMapper.for("Users").add(user)
```

```json
 { _id: 1234565, name: "John Coltrane", age: 28, address: { street: "Banfield" } }
```
