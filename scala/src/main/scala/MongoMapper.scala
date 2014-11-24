import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._


/**
 * Created by rodrigo on 31/08/14.
 */
class MongoMapper(implicit ec: ExecutionContext) {

  val collections: Map[String, MappedCollection[_]] = Map()

  def entity[E: ClassTag](): Unit = entity[E](null)

  def entity[E: ClassTag](collection: String = null) = {

    val classTag: ClassTag[E] = implicitly[ClassTag[E]]
    val entityMapper = new EntityMapper(classTag)

    val name = Option(collection) match {
      case Some(name) => name
      case None => classTag.runtimeClass.getSimpleName
    }
    collections.put(name, new MappedCollection(name, entityMapper))

  }

  def apply(collection: String): MappedCollection[_] = {
    collections(collection)
  }
}
