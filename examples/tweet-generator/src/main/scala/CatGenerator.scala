import io.circe.Codec
import net.andimiller.datagenerator.DataGeneratorApp
import io.circe.generic.semiauto._
import org.scalacheck.{Arbitrary, Gen}

case class Cat(name: String, age: Int)
object Cat {
  implicit val codec: Codec.AsObject[Cat] = deriveCodec[Cat]
  implicit val arb: Arbitrary[Cat]        = Arbitrary(
    for {
      name <- Gen.alphaStr
      age  <- Gen.chooseNum(0, 30)
    } yield Cat(name, age)
  )
}

object CatGenerator extends DataGeneratorApp[Cat]("cat-generator") {}
