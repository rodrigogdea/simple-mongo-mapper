import reactivemongo.api.DB
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONDocument}
import reactivemongo.core.commands.{Count, LastError}

import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by rodrigo on 31/08/14.
 */

case class OperationFailed(lastError: String)

case class ObjectRef(id: String, anObject: AnyRef)


class MappedCollection(val collectionName: String, val mapper: EntityMapper)(implicit ec: ExecutionContext) {


  def count()(implicit db: DB): Future[Int] = db.command(Count(collectionName))

  /**
   * Add a new Object to the Collection
   * @param anObject The new Object
   * @param db The database used to add the new Object to the Collection
   * @return if add operation do not fail, the future will return the Object Reference to the new Object
   */
  def add(anObject: AnyRef)(implicit db: DB): Future[Either[ObjectRef, OperationFailed]] = {
    val collection: BSONCollection = db(collectionName)

    val toDocument: BSONDocument = mapper.toDocument(anObject)
    val objectID: BSONObjectID = BSONObjectID.generate

    collection.insert(toDocument.add("_id" -> objectID)) map (lastError =>
      if (lastError.ok)
        Left(ObjectRef(objectID.stringify, anObject))
      else
        Right(OperationFailed(lastError.errMsg.get))
      )
  }


  def get(id: String)(implicit db: DB): Future[Either[ObjectRef, OperationFailed]] = {
    val collection: BSONCollection = db(collectionName)

    collection.find(BSONDocument("_id" -> BSONObjectID(id))).one[BSONDocument].map {
        case Some(doc) => Left(ObjectRef(id, mapper.toObject(doc)))
        case None => Right(OperationFailed("Document not found"))
    }
  }

}
