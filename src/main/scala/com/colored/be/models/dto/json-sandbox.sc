final case class Foo(
    bar: String
)

import cats.syntax.either._
import cats.syntax.functor._
import io.circe._
import io.circe.Decoder.Result
import io.circe.generic.JsonCodec
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.semiauto._

case class Patch (
  key: String
)

/*
(c: HCursor) => {
  for {
    bar <- c.downField("bar").as[Option[String]]
    baz <- c.downField("baz").as[Option[String]]
  } yield Foo(bar, baz)
}
 */

implicit val decoder: Decoder[Foo] = (c: HCursor) => {
  val eligiblePatches = Map(
    "bar" -> String,
    "baz" -> Int
  )

  def validateKey(k: String): Decoder.Result[String] = {
    if (eligiblePatches.contains(k)) Right(k)
    else Left(
      DecodingFailure("Key not valid.", List())
    )
  }

  for {
    key <- c.downField("field").as[String]
    keyV <- validateKey(key)
    value <- c.downField("baz").as[Option[String]]
  } yield Foo(bar, baz)
}

val jsonStr = "{\"bar\": \"lol\", \"baz\": \"kek\"}"
val jsonStr2 = "{\"field\": \"bar\", value: \"new\"}"

decode[Foo](jsonStr2) match {
  case Left(value) => value.getMessage
  case Right(value) => value.productElement(0)
}
