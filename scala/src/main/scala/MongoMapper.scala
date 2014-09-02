import scala.collection.mutable.Map
import scala.reflect.runtime.universe._


/**
 * Created by rodrigo on 31/08/14.
 */
class MongoMapper {

  val collections: Map[String, MappedCollection] = Map()

  def entity[E: TypeTag](): Unit = entity[E](null)

  def entity[E: TypeTag](collection: String = null) = {

    val typeOfE: Type = typeOf[E]
    val entityMapper = new EntityMapper(typeOfE)
    val mappedCollection: MappedCollection = new MappedCollection(entityMapper)

    val name = Option(collection) match {
      case Some(name) => name
      case None => typeOfE.toString
    }
    collections.put(name, mappedCollection)

  }

  def apply(collection: String): MappedCollection = {
    collections(collection)
  }
}
