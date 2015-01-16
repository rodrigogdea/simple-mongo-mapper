import java.lang.reflect.{Constructor, Field, Method}

import reactivemongo.bson._

/**
 * Created by rodrigo on 31/08/14.
 */

object EntityMapper {

  /**
   * Create an EntityMapper that maps its parameters that have basic types.
   * @param aTypeToMap
   * @tparam E
   * @return
   */
  def apply[E](aTypeToMap: Class[E]): EntityMapper[E] = {
    EntityMapper[E](aTypeToMap, extractMappedFields(aTypeToMap): _*)
  }

  def builder[E](aTypeToMap: Class[E]): EntityMapperBuilder[E] = EntityMapperBuilder[E](aTypeToMap)

  def extractMappedFields[E](aTypeToMap: Class[E]): Seq[MappedField[E, _, _ <: BSONValue]] = {
    val constructor: Constructor[_] = aTypeToMap.getConstructors.apply(0)
    val maxArguments: Int = constructor.getParameterTypes.length

    aTypeToMap.getDeclaredFields.foldLeft(Seq[MappedField[E, _, _ <: BSONValue]]()) { (seq, field) =>
      if (seq.size < maxArguments) {

        val mappedField = field.getType match {
          case c if c == classOf[Int] => MappedFieldInt[E](field)
          case c if c == classOf[String] => MappedFieldString[E](field)
          case c if c == classOf[Double] => MappedFieldDouble[E](field)
        }

        seq.:+(mappedField)
      } else seq
    }
  }

}



case class EntityMapper[E](runtimeClass: Class[E], mappedFields: MappedField[E, _, _ <: BSONValue]*) {
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


case class EntityMapperBuilder[E](aTypeToMap: Class[E]){

  val seqBuilder = Seq.newBuilder[MappedField[E,_, _ <: BSONValue]]

  def mapField(mappedField: MappedField[E, _, _ <: BSONValue]):EntityMapperBuilder[E] = {
    seqBuilder += mappedField
    this
  }

  def build(): EntityMapper[E] = {
    EntityMapper[E](aTypeToMap, seqBuilder.result(): _*)
  }
}