import java.util.concurrent.TimeUnit

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.Count

import scala.concurrent.Await
import scala.concurrent.duration.{FiniteDuration, Duration}


class MappedCollectionSpec extends Specification with Mockito {
  val oneSec: FiniteDuration = Duration(1, TimeUnit.SECONDS)

  val aCollectionName: String = "myCollection"
  val driver: MongoDriver = new MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost"))

  implicit val db: DB = connection("simple-mongo-mapper")
  val anEntityMapper = mock[EntityMapper]
  anEntityMapper.toDocument(any[User]) returns BSONDocument.empty

  val bsonCollection: BSONCollection =  db(aCollectionName)
  Await.ready(bsonCollection.remove(BSONDocument.empty), oneSec)

  "When add a new User to the collection" should {


    val mappedCollection = new MappedCollection(aCollectionName, anEntityMapper)
    mappedCollection.add(User("Test", 28, Address("St", 23)))

    "contains the new User" in {
      //TODO Verify that the new User was inserted
      there was one(anEntityMapper).toDocument(any[User])

      Await.result(db.command(Count(aCollectionName)).map( count => count == 1), oneSec)
    }
  }

}
