import reactivemongo.bson.BSONDocument

import scala.reflect.runtime.universe._

/**
 * Created by rodrigo on 31/08/14.
 */
class EntityMapper(aTypeToMap: Type) {

  def toObject(document: BSONDocument): AnyRef = new Object()

  def toDocument(anObject: AnyRef) = BSONDocument.empty
}
