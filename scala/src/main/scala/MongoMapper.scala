import scala.collection.mutable.Map
import scala.reflect.runtime.universe._


/**
 * Created by rodrigo on 31/08/14.
 */
class MongoMapper {

  val collections: Map[String, MappedCollection] = Map()

  def map[E: TypeTag](collection: String) = {
    val entityMapper = new EntityMapper(typeOf[E])
    collections.put(collection, new MappedCollection(entityMapper))
  }


  def apply(collection: String): MappedCollection = {
      collections(collection)
  }
}
