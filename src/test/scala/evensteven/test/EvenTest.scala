package evensteven.test

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

import scala.io.Source

import evensteven._

class EvenTest extends Spec with ShouldMatchers {
  val example =
"""
* Matkasse
  // Lite Mat
  100 Alex, Lasse, Stefan, Chrisitan
  -20 Alex, Lasse // Två glassar
  +100 Lasse
""";

  describe ("EvenSteven") {

    val result = Evensteven.parse(Source.fromString(example))

    it("should parse one bill") {
      result should have length (1)
    }
    it("should parse title correctly") {
      val bill = result.head
      bill.name should equal ("Matkasse")
    }
    it("should parse correct number of splitters") {
      val bill = result.head

      bill.split.get.splitters should have length (4)
      bill.subSplits.head.splitters should have size (2)
      bill.payments.head.splitters should have size (1)
    }
  }
}
