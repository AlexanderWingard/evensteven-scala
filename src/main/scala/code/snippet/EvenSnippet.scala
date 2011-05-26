package code
package snippet

import scala.xml.{NodeSeq,Text}
import net.liftweb.http._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.common._

object EvenSnippet extends DispatchSnippet {
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
	Text("hello")
      case _ =>
	S.redirectTo("/")
    }
  }
}
