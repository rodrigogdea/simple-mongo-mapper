import org.specs2.mutable.Specification
import reactivemongo.bson.BSONDocument

import scala.reflect.ClassTag

/**
 * Created by rodrigo on 24/11/14.
 */
class EntityMapperSpec extends Specification {

  "When create an EntityMapper for Type Product" should {

    val entityMapper: EntityMapper[Product] = new EntityMapper(implicitly[ClassTag[Product]])

    "Convert an instance of Product to BSONDoc" in {

      val product: Product = Product("Buje", "Un Buje", 23, 234.56)
      val document: BSONDocument = entityMapper.toDocument(product)

      document.getAs[String]("name").get  must be equalTo product.name
      document.getAs[String]("description").get must be equalTo product.description
      document.getAs[Int]("stock").get must be equalTo product.stock
      document.getAs[Double]("price").get must be equalTo product.price
    }
  }

}


case class Product(name: String, description: String, stock: Int, price: Double)