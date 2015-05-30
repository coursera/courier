

package org.coursera.enums

import javax.annotation.Generated
import com.linkedin.data.schema.EnumDataSchema
import com.linkedin.data.template.DataTemplateUtil



@Generated(value = Array("Fruits"), comments = "Courier Data Template.", date = "Sat May 30 14:26:52 PDT 2015")
object Fruits extends Enumeration {
  type Fruits = Value

  
     /**
 * An Apple.
 */ 
    val APPLE = Value("APPLE")
  
    
    val BANANA = Value("BANANA")
  
    
    val ORANGE = Value("ORANGE")
  
    
    val PINEAPPLE = Value("PINEAPPLE")
  

  /**
   * Represents an unrecognized enumeration symbol.
   *
   * May be present when data writer is using a version of enumeration with symbols not yet
   * known to the data reader.
   */
  val $UNKNOWN = Value("$UNKNOWN")

  /**
   * Converts a string to an enumeration value. If the string does not match
   * any of the enumeration values, returns the $UKNOWN enumeration value.
   */
  def fromString(s: String): Value = {
    values.find(_.toString == s).getOrElse($UNKNOWN)
  }

  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"enum","name":"Fruits","namespace":"org.coursera.enums","symbols":["APPLE","BANANA","ORANGE","PINEAPPLE"],"symbolDocs":{"APPLE":"An Apple."}}""").asInstanceOf[EnumDataSchema]
}

