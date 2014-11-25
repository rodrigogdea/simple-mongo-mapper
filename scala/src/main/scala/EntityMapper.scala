import java.lang.reflect.{Method, Field}

import reactivemongo.bson.Producer.NameOptionValueProducer
import reactivemongo.bson._

import scala.reflect.ClassTag

/**
 * Created by rodrigo on 31/08/14.
 */
class EntityMapper[E](aTypeToMap: ClassTag[E]) {

  private implicit val runtimeClass: Class[_] = aTypeToMap.runtimeClass
  private val mappedFields: Map[String, MappedField[E]] =
    runtimeClass.getDeclaredFields.foldLeft(Map[String, MappedField[E]]()) { (map, field) =>
      map + (field.getName -> new MappedField(field))
    }

  runtimeClass.getDeclaredFields foreach (f => println())

  def toObject(document: BSONDocument): E = {
    new Object().asInstanceOf[E]
  }

  def toDocument(anObject: E): BSONDocument = {

    val bsonValues = mappedFields.foldLeft(Seq[(String, BSONValue)]()) { (seq, tuple) =>
      seq.+:(tuple._1 -> tuple._2(anObject))
    }

    BSONDocument(bsonValues)
  }
}

class MappedField[E](field: Field) {

  def apply(anEntity: E)(implicit runtimeClass: Class[_]): BSONValue = {
    val method: Method = runtimeClass.getMethod(field.getName)
    val value: Any = method.invoke(anEntity)

    value match {
      case v: Int => BSONInteger(value.asInstanceOf[Int])
      case v: Double => BSONDouble(value.asInstanceOf[Double])
      case v: String => BSONString(value.asInstanceOf[String])
      case _ => BSONNull
    }
  }
}