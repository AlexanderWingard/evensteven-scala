package evensteven.test

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

import scala.io.Source

import evensteven._

class EvenTest extends Spec with ShouldMatchers {

  describe ("A bill") {
  val example =
"""
* Matkasse
  // Lite Mat
  100 Alex, Lasse, Stefan, Christian
  -20 Alex, Lasse // Två glassar
  +100 Lasse
""";
    val bill = Evensteven.parse(Source.fromString(example)).head.asInstanceOf[Bill]
    val result = bill.even
    it("should parse title correctly") {
      bill.name should equal ("Matkasse")
    }
    it("should parse correct number of splitters") {
      bill.split.get.splitters should have length (4)
      bill.subSplits.head.splitters should have size (2)
      bill.payments.head.splitters should have size (1)
    }
    it("should parse names correctly") {
      val splitters = bill.split.get.splitters
      splitters should contain ("Alex")
      splitters should contain ("Lasse")
      splitters should contain ("Stefan")
      splitters should contain ("Christian")
    }
    it("should calculate a bill correctly") {
      result("Stefan") should equal (-20)
      result("Christian") should equal (-20)
      result("Lasse") should equal (70)
      result("Alex") should equal (-30)
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
  describe("A transfer") {
  val transferExample =
"""
  >20 Alex Jeppe
""";
    val transfer = Evensteven.parse(Source.fromString(transferExample)).head.asInstanceOf[Transfer]
    it("Should parse a transfer correctly") {
      transfer.from should equal ("Alex")
      transfer.to should equal ("Jeppe")
      transfer.amount should equal (20)
    }
  }
  describe("A bigger example") {
    val example =
"""
* Mat
  100 Alex, Malin
* Boende
  100 Alex, Malin
>20 Malin Alex
* Bil
  100 Alex, Malin
""";
    val result = Evensteven.parse(Source.fromString(example))
    it("should parse two bills") {
      result should have length (4)
    }
    it("should return correct order") {
      result(0).asInstanceOf[Bill].name should equal ("Mat")
      result(1).asInstanceOf[Bill].name should equal ("Boende")
      result(2).asInstanceOf[Transfer].amount should equal (20)
      result(3).asInstanceOf[Bill].name should equal ("Bil")
    }
  }
}
