package org.coursera.courier.generator.customtypes

import java.util.UUID

import com.linkedin.data.template.Custom
import com.linkedin.data.template.DirectCoercer

object UUIDCoercer extends DirectCoercer[UUID] {

  override def coerceInput(obj: UUID): AnyRef = {
    obj.toString
  }

  override def coerceOutput(obj: Any): UUID = {
    obj match {
      case value: java.lang.String => UUID.fromString(value)
      case _: Any => throw new IllegalArgumentException()
    }
  }

  registerCoercer()

  final def registerCoercer(): Unit = {
    Custom.registerCoercer(this, classOf[UUID])
  }

}

