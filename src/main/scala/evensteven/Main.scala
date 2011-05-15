package evensteven

import scala.io.Source
import util.matching.Regex

object Evensteven {
  val billRegexp = new Regex("""\*\s*(\S+)\s*""")
  val splitRegexp = new Regex("""\s*([\+-]*)(\d+)\s*([a-zA-Z,\s]+)(.*)""")
  val transferRegexp = new Regex("""\s*>(\d+)\s+([a-zA-Z])[\s,]+([a-zA-Z])(.*)""")

  def main(args: Array[String]) = {
    println("Hello EvenSteven")
  }

  def parse(source : Source) = {
    source.getLines().foldLeft(List() : List[Bill]) { (bills, line) =>
      line match {
	case billRegexp(name) =>
	  Bill(name) :: bills
	case splitRegexp(kind, amount, splitters, comment) =>
	  val splitSplitters = splitters.split(",").map(_.trim).toList
	  val bill :: tail = bills
	  kind match {
	    case "" =>
	      bill.copy(split = Some(Split(splitSplitters, -amount.toFloat))) :: tail
	    case "-" =>
	      bill.copy(subSplits = Split(splitSplitters, -amount.toFloat) :: bill.subSplits) :: tail
	    case "+" =>
	      bill.copy(payments = Split(splitSplitters, amount.toFloat) :: bill.payments) :: tail
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

case class Result(total : Map[String, Float] = Map()) {
  def + (bill : Bill) = {
    val subSplitSum = bill.subSplits.foldLeft(0f)(_ + _.amount)
    val split = bill.split.get
    val adjusted = split.copy(amount = split.amount - subSplitSum)
    
    val allSplits = adjusted :: bill.subSplits ++ bill.payments
    
    val res = allSplits.foldLeft(Map().withDefaultValue(0f) : Map[String, Float]) { (acc, split) =>
      split.split.foldLeft(acc) { (acc, pair) =>
	acc(pair._1) += pair._2
       }
    }
    Result(res)
  }
}
