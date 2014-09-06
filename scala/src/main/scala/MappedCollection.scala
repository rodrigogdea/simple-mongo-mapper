import reactivemongo.api.DB
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONDocument}
import reactivemongo.core.commands.{Count, LastError}

import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by rodrigo on 31/08/14.
 */

case class OperationFailed(lastError: LastError)

case class ObjectRef(id: String, anObject: AnyRef)


class MappedCollection(val collectionName: String, val mapper: EntityMapper)(implicit ec: ExecutionContext) {


  def count()(implicit db: DB): Future[Int] = db.command(Count(collectionName))

  def add(anObject: AnyRef)(implicit db: DB): Future[Either[ObjectRef, OperationFailed]] = {
    val collection: BSONCollection = db(collectionName)

    val toDocument: BSONDocument = mapper.toDocument(anObject)
    val objectID: BSONObjectID = BSONObjectID.generate

    collection.insert(toDocument.add("_id" -> objectID)) map (lastError =>
      if (lastError.ok)
        Left(ObjectRef(objectID.stringify, anObject))
      else
        Right(OperationFailed(lastError))
      )
  }


}
