import java.lang.reflect.{Constructor, Field, Method}

import reactivemongo.bson._

/**
 * Created by rodrigo on 31/08/14.
 */

object EntityMapper {

  def apply[E](aTypeToMap: Class[E]): EntityMapper[E] = {
    EntityMapper[E](aTypeToMap, extracMappedFields(aTypeToMap))
  }

  def extracMappedFields[E](aTypeToMap: Class[E]): Seq[MappedField[E]] = {
    val constructor: Constructor[_] = aTypeToMap.getConstructors.apply(0)
    val length: Int = constructor.getParameterTypes.length

    aTypeToMap.getDeclaredFields.foldLeft(Seq[MappedField[E]]()) { (seq, field) =>
      if (seq.size < length) seq.:+(MappedField(field)) else seq
    }
  }

}

case class EntityMapper[E](runtimeClass: Class[E], mappedFields: Seq[MappedField[E]]) {
  val constructor: Constructor[_] = runtimeClass.getConstructors.apply(0)

  def toObject(document: BSONDocument): E = {
    val params = mappedFields.foldLeft(Seq[Object]())((seq, mappedField) => seq :+ mappedField(document).asInstanceOf[Object])
    constructor.newInstance(params: _*).asInstanceOf[E]
  }

  def toDocument(anObject: E): BSONDocument = {
    val bsonValues = mappedFields.foldLeft(Seq[(String, BSONValue)]()) { (seq, mappedField) =>
      seq.:+(mappedField.name -> mappedField(anObject, runtimeClass))
    }
    BSONDocument(bsonValues)
  }
}


case class MappedField[E](field: Field) {
  val name = field.getName

  def apply(document: BSONDocument): Any = {
    field.getType match {
      case c if c == classOf[Int] => document.getAs[Int](name).getOrElse()
      case c if c == classOf[String] => document.getAs[String](name).getOrElse()
      case c if c == classOf[Double] => document.getAs[Double](name).getOrElse()
      case _ => None
    }
  }

  def apply(anEntity: E, runtimeClass: Class[E]): BSONValue = {
    val method: Method = runtimeClass.getMethod(name)
    val value: Any = method.invoke(anEntity)
    value match {
      case v: Int => BSONInteger(method.invoke(anEntity).asInstanceOf[Int])
      case v: Double => BSONDouble(method.invoke(anEntity).asInstanceOf[Double])
      case v: String => BSONString(method.invoke(anEntity).asInstanceOf[String])
      case _ => BSONNull
    }
  }
}