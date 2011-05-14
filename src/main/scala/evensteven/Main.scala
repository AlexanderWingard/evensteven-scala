package evensteven

import scala.io.Source
import util.matching.Regex

object Evensteven {
  val billRegexp = new Regex("""\*\s*(\S+)\s*""")
  val splitRegexp = new Regex("""\s*([\+-]*)(\d+)\s*([a-zA-Z,\s]+)(.*)""")
  def main(args: Array[String]) = {
    println("Hello EvenSteven")
  }

  def parse(source : Source) = {
    source.getLines().foldLeft(List() : List[Bill]) { (bills, line) =>
      line match {
	case billRegexp(name) =>
	  Bill(name) :: bills
	case splitRegexp(kind, amount, splitters, comment) =>
	  val split = Split(splitters.split(",").map(_.trim).toList, amount.toInt)
	  val bill :: tail = bills
	  kind match {
	    case "" =>
	      bill.copy(split = Some(split)) :: tail
	    case "-" =>
	      bill.copy(subSplits = split :: bill.subSplits) :: tail
	    case "+" =>
	      bill.copy(payments = split :: bill.payments) :: tail
	  }
	case other =>
	  bills
      }
    }
  }
}

case class Bill(name : String, split : Option[Split] = None, subSplits : List[Split] = List(), payments : List[Split] = List())

case class Split(splitters : List[String], amount : Float) {
  def split = {
    val splitAmount = amount / splitters.length
    splitters.foldLeft(Map() : Map[String, Float]) { (acc, splitter) =>
      acc + (splitter -> splitAmount)
    }
  }
}

case class Transfer(from : String, to : String, amount : Float)
