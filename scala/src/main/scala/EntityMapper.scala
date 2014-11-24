import reactivemongo.bson.BSONDocument

import scala.reflect.ClassTag

/**
 * Created by rodrigo on 31/08/14.
 */
class EntityMapper[E](aTypeToMap: ClassTag[E]) {

  def toObject(document: BSONDocument): AnyRef = new Object()

  def toDocument(anObject: AnyRef) = BSONDocument.empty
}
