import org.specs2.mutable.Specification

/**
 * Created by rodrigo.gdea@gmail.com on 28/08/14.
 */
class SimpleMongoMapperSpec extends Specification {
  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size (11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }
}
