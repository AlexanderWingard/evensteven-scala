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
  100 Alex, Lasse, Stefan, Christian
  -20 Alex, Lasse // Två glassar
  +100 Lasse
""";

  describe ("EvenSteven") {

    val bills = Evensteven.parse(Source.fromString(example))
    val result = Result() + bills.head

    it("should parse one bill") {
      bills should have length (1)
    }
    it("should parse title correctly") {
      val bill = bills.head
      bill.name should equal ("Matkasse")
    }
    it("should parse correct number of splitters") {
      val bill = bills.head

      bill.split.get.splitters should have length (4)
      bill.subSplits.head.splitters should have size (2)
      bill.payments.head.splitters should have size (1)
    }
    it("should parse names correctly") {
      val bill = bills.head
      val splitters = bill.split.get.splitters
      splitters should contain ("Alex")
      splitters should contain ("Lasse")
      splitters should contain ("Stefan")
      splitters should contain ("Christian")
    }
    it("should calculate a bill correctly") {
      result.total("Stefan") should equal (-20)
      result.total("Christian") should equal (-20)
      result.total("Lasse") should equal (70)
      result.total("Alex") should equal (-30)
    }
  }
  describe  ("A split") {
    val split = Split(List("Alex", "Lars"), 100)

    it("should work") {
      val splitted = split.split
      splitted("Alex") should equal (50)
      splitted("Lars") should equal (50)
    }
  }
}
