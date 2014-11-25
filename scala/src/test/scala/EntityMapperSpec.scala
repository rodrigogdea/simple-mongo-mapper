import org.specs2.matcher.MatchFailure
import org.specs2.mutable.Specification
import reactivemongo.bson.{BSONInteger, BSONString, BSONDocument}

import scala.reflect.ClassTag

/**
 * Created by rodrigo on 24/11/14.
 */
class EntityMapperSpec extends Specification {

  "When create an EntityMapper for Type Product" should {

    val entityMapper: EntityMapper[Product] = new EntityMapper(implicitly[ClassTag[Product]])

    "Convert an instance of Product to BSONDoc" in {

      val product: Product = Product("Buje", "Un Buje")
      val document: BSONDocument = entityMapper.toDocument(product)

      document.getAs[String]("name").get  must be equalTo product.name
      document.getAs[String]("description").get must be equalTo product.description
//      document.getAs[Int]("count").get must be equalTo product.count
    }
  }

}


case class Product(name: String, description: String)