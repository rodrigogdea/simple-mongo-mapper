import org.specs2.mutable.Specification
import reactivemongo.bson.BSONDocument

import scala.reflect.ClassTag

/**
 * Created by rodrigo on 24/11/14.
 */
class EntityMapperSpec extends Specification {

  "When create an EntityMapper for Type Product" should {

    val runtimeClass = implicitly[ClassTag[Product]].runtimeClass.asInstanceOf[Class[Product]]
    val entityMapper = EntityMapper(runtimeClass)

    "Convert an instance of Product to BSONDoc" in {

      val product: Product = Product("Buje", "Un Buje", 23, 234.56)
      val document: BSONDocument = entityMapper.toDocument(product)

      document.getAs[String]("name").get must be equalTo product.name
      document.getAs[String]("description").get must be equalTo product.description
      document.getAs[Int]("stock").get must be equalTo product.stock
      document.getAs[Double]("price").get must be equalTo product.price
    }

    "Convert an instance of BSONDoc to Product" in {
      val aBsonDoc: BSONDocument = BSONDocument(
        "name" -> "Engranaje",
        "description" -> "Un Engranaje",
        "stock" -> 38,
        "price" -> 45.21
      )

      val product: Product = entityMapper.toObject(aBsonDoc)

      product.name must be equalTo "Engranaje"
      product.description must be equalTo "Un Engranaje"
      product.stock must be equalTo 38
      product.price must be equalTo 45.21
    }
  }

}


case class Product(name: String, description: String, stock: Int, price: Double) {
  private val otherField: String = ""
}