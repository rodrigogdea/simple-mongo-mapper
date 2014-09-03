import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument


class MappedCollectionSpec extends Specification with Mockito {
  val driver: MongoDriver = new MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost"))
  implicit val db: DB = connection("simple-mongo-mapper")

  val anEntityMapper = mock[EntityMapper]
  anEntityMapper.toDocument(any[User]) returns BSONDocument.empty

  "When add a new User to the collection" should {

    val mappedCollection = new MappedCollection("myCollection", anEntityMapper)

    mappedCollection.add(User("Test", 28, Address("St", 23)))

    "contains the new User" in {
      //TODO Verify that the new User was inserted
      there was one(anEntityMapper).toDocument(any[User])
    }
  }

}
