package evensteven

import scala.io.Source
import util.matching.Regex

object Evensteven {
  val billRegexp = new Regex("""\*\s*(.+)\s*""")
  val splitRegexp = new Regex("""\s*([\+-]*)([\d,]+)\s*([a-zA-Z,\s]+)(.*)""")
  val transferRegexp = new Regex("""\s*>\s*([\d,]+)\s+([a-zA-Z]+)[\s,]+([a-zA-Z]+)(.*)""")
  val currencyRegexp = new Regex("""#\s*([\d,]+)(.*)""")

  def main(args: Array[String]) = {
    println("Hello EvenSteven")
  }

  def parse(source : Source) = {
    source.getLines().foldLeft(List() : List[EvenEntity]) { (entities, line) =>
      line match {
	case billRegexp(name) =>
	  Bill(name) :: entities
	case splitRegexp(kind, amount, splitters, comment) =>
	  val splitSplitters = splitters.split(",").map(_.trim).toList
	  val (prefix, head :: tail) = entities.span(!_.isInstanceOf[Bill])
	  val bill = head.asInstanceOf[Bill]
	  val newBill =
	  kind match {
	    case "" =>
	      bill.copy(split = Some(Split(splitSplitters, -amount.replace(',','.').toFloat)))
	    case "-" =>
	      bill.copy(subSplits = Split(splitSplitters, -amount.replace(',','.').toFloat) :: bill.subSplits)
	    case "+" =>
	      bill.copy(payments = Split(splitSplitters, amount.replace(',','.').toFloat) :: bill.payments)
	  }
	  prefix ++ (newBill :: tail)
	case transferRegexp(amount, from, to, comment) =>
	  Transfer(from, to, amount.replace(',','.').toFloat) :: entities
	case currencyRegexp(factor, comment) =>
	  Currency(factor.replace(',','.').toFloat) :: entities
	case other =>
	  entities
      }
    }.reverse
  }
}

case class Result(res : Map[String, Float] = Map(), currency : Float = 1) {
  def + (arg : EvenEntity) = {
    arg match {
      case newCurrency : Currency =>
	this.copy(currency = newCurrency.factor)
      case splittable : Splittable =>
	val currencyAdjusted = splittable.even.mapValues(_ / currency)
	val merged = splittable.mergeMaps(res, currencyAdjusted)
	this.copy(res = merged)
    }
  }
  def localRes = res.mapValues(_ * currency)
}

abstract class EvenEntity

trait Splittable
{
  def even : Map[String, Float]

  def mergeMaps(m1 : Map[String, Float], m2 : Map[String, Float]) = {
    m1.foldLeft(m2.withDefaultValue(0f)) { (acc, pair) =>
      acc(pair._1) += pair._2
    }
  }
}

case class Bill(name : String, split : Option[Split] = None, subSplits : List[Split] = List(), payments : List[Split] = List()) extends EvenEntity with Splittable{

  def even = {
    val subSplitSum = subSplits.foldLeft(0f)(_ + _.amount)
    val adjusted = split.get.copy(amount = split.get.amount - subSplitSum)
    val allSplits = adjusted :: subSplits ++ payments

    allSplits.foldLeft(Map() : Map[String, Float])((acc, split) => mergeMaps(acc, split.even))
  }
}

case class Split(splitters : List[String], amount : Float) extends Splittable{
  def even = {
    val splitAmount = amount / splitters.length
    splitters.foldLeft(Map() : Map[String, Float]) { (acc, splitter) =>
      acc + (splitter -> splitAmount)
    }
  }
}

case class Transfer(from : String, to : String, amount : Float) extends EvenEntity with Splittable {
  def even = Map((from -> amount), (to -> -amount))
}

case class Currency(factor : Float) extends EvenEntity

case class Acc(result : Map[String, Float] = Map())
