import java.lang.reflect.{Method, Field}

import reactivemongo.bson._

/**
 * Created by rodrigo on 27/11/14.
 */
abstract class MappedField[E, V <: Any, J <: BSONValue](field: Field) {
  val name = field.getName

  def apply(document: BSONDocument): V

  def apply(anEntity: E, runtimeClass: Class[E]): J

  def getValue(anEntity: E, runtimeClass: Class[E]): Any = {
    val method: Method = runtimeClass.getMethod(name)
    method.invoke(anEntity)
  }

}

case class IntMappedField[E](field: Field) extends MappedField[E, Int, BSONInteger](field) {
  override def apply(document: BSONDocument): Int = document.getAs[Int](name).get
  override def apply(anEntity: E, runtimeClass: Class[E]): BSONInteger = BSONInteger(getValue(anEntity, runtimeClass).asInstanceOf[Int])
}

case class DoubleMappedField[E](field: Field) extends MappedField[E, Double, BSONDouble](field) {
  override def apply(document: BSONDocument): Double = document.getAs[Double](name).get
  override def apply(anEntity: E, runtimeClass: Class[E]): BSONDouble = BSONDouble(getValue(anEntity, runtimeClass).asInstanceOf[Double])
}

case class StringMappedField[E](field: Field) extends MappedField[E, String, BSONString](field) {
  override def apply(document: BSONDocument): String = document.getAs[String](name).get
  override def apply(anEntity: E, runtimeClass: Class[E]): BSONString = BSONString(getValue(anEntity, runtimeClass).asInstanceOf[String])
}
