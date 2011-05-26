package code
package model

import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.common._


class Event extends LongKeyedMapper[Event] with IdPK {
  def getSingleton = Event

  object name extends MappedString(this, 50) 
  object data extends MappedText(this)
  object saveDate extends MappedDateTime(this)
}

object Event extends Event with LongKeyedMetaMapper[Event] {
  override def fieldOrder = List(data)
}
