import java.lang.reflect.{Method, Field}

import reactivemongo.bson.Producer.NameOptionValueProducer
import reactivemongo.bson.{BSONValue, BSONDocument, BSONString}

import scala.reflect.ClassTag

/**
 * Created by rodrigo on 31/08/14.
 */
class EntityMapper[E](aTypeToMap: ClassTag[E]) {

  private implicit val runtimeClass: Class[_] = aTypeToMap.runtimeClass
  private val mappedFields: Map[String, MappedField] =
    runtimeClass.getDeclaredFields.foldLeft(Map[String, MappedField]()) { (map, field) =>
      map + (field.getName -> new MappedField(field))
    }

  runtimeClass.getDeclaredFields foreach (f => println())

  def toObject(document: BSONDocument): AnyRef = new Object()

  def toDocument(anObject: AnyRef): BSONDocument = {

    val bsonValues = mappedFields.foldLeft(Seq[(String, BSONValue)]()) { (seq, tuple) =>
      seq.+:(tuple._1 -> tuple._2(anObject))
    }

    BSONDocument(bsonValues)
  }
}

class MappedField(field: Field) {

  def apply(anEntity: AnyRef)(implicit runtimeClass: Class[_]): BSONValue = {
    val method: Method = runtimeClass.getMethod(field.getName)
    BSONString(method.invoke(anEntity).asInstanceOf[String])
  }
}