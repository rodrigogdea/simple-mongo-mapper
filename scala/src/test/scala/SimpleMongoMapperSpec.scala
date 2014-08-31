import java.util.concurrent.TimeUnit

import akka.actor.FSM.Failure
import org.specs2.mutable.Specification
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.LastError
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

/**
 * Created by rodrigo.gdea@gmail.com on 28/08/14.
 */
class SimpleMongoMapperSpec extends Specification {
  "The 'Hello world' string" should {
    "contain 11 characters" in {
      val driver: MongoDriver = new MongoDriver
      val connection: MongoConnection = driver.connection(List("localhost"))
      val db: DefaultDB = connection("simple-mongo-mapper")
      val collection: BSONCollection = db("test")
      val insert: Future[LastError] = collection.insert(BSONDocument("firstName" -> "Test"))
      insert.onComplete( x => println(s"${x}\n"))

      Await.ready(insert, Duration(2, TimeUnit.SECONDS))

      "Hello world" must have size (11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }
}
