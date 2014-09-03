import reactivemongo.api.DB
import reactivemongo.api.collections.default.BSONCollection

import scala.concurrent.ExecutionContext

/**
 * Created by rodrigo on 31/08/14.
 */
class MappedCollection(val collectionName: String, val mapper: EntityMapper)(implicit ec: ExecutionContext) {


  def add(anObject: AnyRef)(implicit db: DB) = {
    val collection: BSONCollection = db(collectionName)
    collection.save(mapper.toDocument(anObject))
  }


}
