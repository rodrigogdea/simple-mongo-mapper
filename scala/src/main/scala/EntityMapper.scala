import reactivemongo.bson.BSONDocument

import scala.reflect.runtime.universe._

/**
 * Created by rodrigo on 31/08/14.
 */
class EntityMapper(aTypeToMap: Type) {
  def toDocument(anObject: AnyRef) = BSONDocument.empty
}
