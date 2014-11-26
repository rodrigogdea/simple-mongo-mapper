import java.lang.reflect.{Constructor, Method, Field}

import reactivemongo.bson.Producer.NameOptionValueProducer
import reactivemongo.bson._

import scala.reflect.ClassTag

/**
 * Created by rodrigo on 31/08/14.
 */

object EntityMapper {

  def extracMappedFields[E](aTypeToMap: Class[E]): Seq[MappedField[E]] = {
    val constructor: Constructor[_] = aTypeToMap.getConstructors.apply(0)
    val length: Int = constructor.getParameterTypes.length

    aTypeToMap.getDeclaredFields.foldLeft(Seq[MappedField[E]]()) { (seq, field) =>
      if (seq.size < length) seq.:+(MappedField(field)) else seq
    }
  }

}

class EntityMapper[E](aTypeToMap: ClassTag[E], mappedFields: Seq[MappedField[E]]) {

  private implicit val runtimeClass: Class[E] = aTypeToMap.runtimeClass.asInstanceOf[Class[E]]
  val constructor: Constructor[_] = runtimeClass.getConstructors.apply(0)

  def this(aTypeToMap: ClassTag[E]) {
    this(aTypeToMap, EntityMapper.extracMappedFields(aTypeToMap.runtimeClass.asInstanceOf[Class[E]]))
  }

  def toObject(document: BSONDocument): E = {
    val params = mappedFields.foldLeft(Seq[Object]())((seq, mappedField) => seq :+ mappedField(document).asInstanceOf[Object])
    constructor.newInstance(params: _*).asInstanceOf[E]
  }

  def toDocument(anObject: E): BSONDocument = {

    val bsonValues = mappedFields.foldLeft(Seq[(String, BSONValue)]()) { (seq, mappedField) =>
      seq.:+(mappedField.name -> mappedField(anObject))
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

  def apply(anEntity: E)(implicit runtimeClass: Class[_]): BSONValue = {
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