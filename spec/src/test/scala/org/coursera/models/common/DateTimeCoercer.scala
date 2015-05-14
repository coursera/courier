package org.coursera.models.common

import com.linkedin.data.template.Custom
import com.linkedin.data.template.DirectCoercer
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

class DateTimeCoercer extends DirectCoercer[DateTime] {

  override def coerceInput(obj: DateTime): AnyRef = {
    DateTimeCoercer.iso8601Format.print(obj)
  }

  override def coerceOutput(obj: Any): DateTime = {
    obj match {
      case string: String => DateTimeCoercer.iso8601Format.parseDateTime(string)
      case _: Any =>
        throw new IllegalArgumentException(
          s"DateTime field must be string, but was ${obj.getClass}")
    }
  }
}

object DateTimeCoercer {

  registerCoercer()

  def registerCoercer(): Unit = {
    Custom.registerCoercer(new DateTimeCoercer, classOf[DateTime])
  }

  val iso8601Format = ISODateTimeFormat.dateTime()
}
