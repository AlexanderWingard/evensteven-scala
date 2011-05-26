package code
package snippet

import scala.xml.{NodeSeq,Text}
import net.liftweb.http._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.common._

import net.liftweb.mapper._
import model.Event

object EvenSnippet extends DispatchSnippet {
  val exampleEvent = """
* First bill
"""

  def dispatch = {
    case "goToEvent" => form _
    case "showEvent" => event _
  }

  def form(xhtml : NodeSeq) : NodeSeq = {
    var name = ""

    def go()  = S.redirectTo("/event/" + name)
    bind("f", xhtml, 
	 "eventName" -> SHtml.text(name, name = _),
	 "go" -> SHtml.submit("Go", go))
  }

  def event(xhtml : NodeSeq) : NodeSeq = {
    S.param("eventName") match {
      case Full(s) if s.length > 0 && s != "index" =>
	Event.find(By(Event.name, s)) match {
	  case Full(event) =>
	    Text(event.data.is)
	  case _ =>
	    Text(exampleEvent)
	}
      case _ =>
	S.redirectTo("/")
    }
  }
}
