import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext
import scala.reflect.runtime.universe._


/**
 * Created by rodrigo on 31/08/14.
 */
class MongoMapper(implicit ec: ExecutionContext) {

  val collections: Map[String, MappedCollection] = Map()

  def entity[E: TypeTag](): Unit = entity[E](null)

  def entity[E: TypeTag](collection: String = null) = {

    val typeOfE: Type = typeOf[E]
    val entityMapper = new EntityMapper(typeOfE)

    val name = Option(collection) match {
      case Some(name) => name
      case None => typeOfE.toString
    }
    collections.put(name, new MappedCollection(name, entityMapper))

  }

  def apply(collection: String): MappedCollection = {
    collections(collection)
  }
}
