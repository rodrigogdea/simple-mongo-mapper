import java.util.concurrent.TimeUnit

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.concurrent.{Await, Future}


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

  val mappedCollection = new MappedCollection(aCollectionName, anEntityMapper)

  "When add a new User to the collection" should {
    val user: User = User("Test", 28, Address("St", 23))

    val add: Future[Either[ObjectRef, OperationFailed]] = mappedCollection.add(user)

    val result: Either[ObjectRef, OperationFailed] = Await.result(add, oneSec)

    "toDocument method must be called on EntityMapper" in {
      there was one(anEntityMapper).toDocument(any[User])
    }

    "successful operation" in {
      result.isLeft
    }

    "contains one element" in {
      Await.result( mappedCollection.count(), oneSec) == 1
    }
  }

}
