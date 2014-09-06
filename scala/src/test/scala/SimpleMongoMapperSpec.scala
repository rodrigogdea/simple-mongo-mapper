import org.specs2.mutable.Specification

/**
 * Created by rodrigo.gdea@gmail.com on 28/08/14.
 */

case class Address(street: String, number: Int)

case class User(name: String, age: Int, address: Address)

class SimpleMongoMapperSpec extends Specification {

  "When register an Entity in MongoMapper" should {
    val mongoMapper: MongoMapper = new MongoMapper()
    mongoMapper.entity[User]
    "contain a MappedCollection for a collection with the same name as Entity" in {
      mongoMapper("User") must not be null
    }
  }

  "When register an Entity in MongoMapper to a Collection" should {
    val mongoMapper: MongoMapper = new MongoMapper()
    mongoMapper.entity[User]("Users")
    "contain a MappedCollection for the Entity" in {
      mongoMapper("Users") must not be null
    }
  }
}
