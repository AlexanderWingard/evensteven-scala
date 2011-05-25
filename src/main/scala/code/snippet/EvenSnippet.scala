package code
package snippet

import scala.xml.{NodeSeq,Text}
import net.liftweb.http._
import net.liftweb.util._
import net.liftweb.util.Helpers._

object EvenSnippet extends DispatchSnippet {
  def dispatch = {
    case "goToEvent" => form _
  }

  def form(xhtml : NodeSeq) : NodeSeq = {
    var name = ""

    def go()  = S.redirectTo("/event/" + name)
    bind("f", xhtml, 
	 "eventName" -> SHtml.text(name, name = _),
	 "go" -> SHtml.submit("Go", go))
  }
}