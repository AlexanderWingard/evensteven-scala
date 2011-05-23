package code
package model

import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.common._


class Event extends LongKeyedMapper[Event] with IdPK {
  def getSingleton = Event

  object data extends MappedTextarea(this, 1000)
  object saveDate extends MappedDateTime(this)
}

object Event extends Event with LongKeyedMetaMapper[Event] {
  override def fieldOrder = List(data)
}
