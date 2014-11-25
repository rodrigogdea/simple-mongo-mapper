import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.duration._
import scala.concurrent._


class MappedCollectionSpec extends Specification with Mockito with NoTimeConversions {
  val oneSec = 1.seconds

  val aCollectionName: String = "myCollection"
  val driver: MongoDriver = new MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost"))

  implicit val db: DB = connection("simple-mongo-mapper")

  val user: User = User("Test", 28, Address("St", 23))

  val anEntityMapper = mock[EntityMapper[User]]
  anEntityMapper.toDocument(any[User]) returns BSONDocument.empty
  anEntityMapper.toObject(any[BSONDocument]) returns user

  val bsonCollection: BSONCollection =  db(aCollectionName)
  val mappedCollection = new MappedCollection(aCollectionName, anEntityMapper)

  "When add a new User to the collection" should {
    Await.ready(bsonCollection.remove(BSONDocument.empty), oneSec)
    val add = mappedCollection.add(user)
    val result = Await.result(add, oneSec)

    "toDocument method must be called on EntityMapper" in {
      there was one(anEntityMapper).toDocument(any[User])
    }

    "successful operation" in {
      result.isLeft
    }

    "collection must contains the inserted doc" in {
      val get = mappedCollection.get(result.left.get.id)

      Await.result(get, oneSec).isLeft
    }

    "contains one element" in {
      Await.result( mappedCollection.count(), oneSec) == 1
    }
  }

}
