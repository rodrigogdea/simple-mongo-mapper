import java.util.concurrent.TimeUnit

import org.specs2.mutable.Specification
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.LastError
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success


import java.lang.reflect._

/**
 * Created by rodrigo.gdea@gmail.com on 28/08/14.
 */

case class Address(street: String, number: Int)

case class User(name: String, age: Int, address: Address)

class SimpleMongoMapperSpec extends Specification {
  //  val driver: MongoDriver = new MongoDriver
  //  val connection: MongoConnection = driver.connection(List("localhost"))
  //  val db: DefaultDB = connection("simple-mongo-mapper")
  //  val collection: BSONCollection = db("test")

  "When register an Entity in MongoMapper" should {
    val mongoMapper: MongoMapper = new MongoMapper()
    mongoMapper.entity[User]()
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
