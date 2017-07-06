package org.coursera.records.test

import com.linkedin.data.ByteString
import org.coursera.courier.schema.Schema

case class WithPrimitivesSimple (
  
  intField: Int,
  
  longField: Long,
  
  floatField: Float,
  
  doubleField: Double,
  
  booleanField: Boolean,
  
  stringField: String,
  
  bytesField: ByteString
  
)
object WithPrimitivesSimple {
  val SCHEMA = Schema[WithPrimitivesSimple].asJson
}
