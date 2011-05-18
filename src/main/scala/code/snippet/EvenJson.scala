package code
package snippet

import net.liftweb._
import http._
import common._
import util._
import js._
import JsCmds._
import JE._

import scala.xml.NodeSeq

object XString {
  def unapply(in: Any): Option[String] = in match {
    case s: String => Some(s)
    case _ => None
  }
}

object EvenJsonHandler extends SessionVar[JsonHandler] (
  new JsonHandler {
    def apply(in : Any) : JsCmd = in match {
      case JsonCmd("sendEven", resp, XString(s), _) =>
	Call(resp, s)
      case _ => Noop
    }
  })

object EvenJson extends DispatchSnippet {
  val dispatch = Map("render" -> buildFuncs _)

  def buildFuncs(in: NodeSeq) : NodeSeq =
    Script(EvenJsonHandler.is.jsCmd &
	   Function("sendEven", List("callback", "input"),
		    EvenJsonHandler.is.call("sendEven",
					    JsVar("callback"),
					    JsVar("input"))))
}
