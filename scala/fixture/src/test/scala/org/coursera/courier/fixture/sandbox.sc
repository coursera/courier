import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.DataSchema.Type.BOOLEAN
import com.linkedin.data.schema.DataSchema.Type.BYTES
import com.linkedin.data.schema.DataSchema.Type.DOUBLE
import com.linkedin.data.schema.DataSchema.Type.FLOAT
import com.linkedin.data.schema.DataSchema.Type.INT
import com.linkedin.data.schema.DataSchema.Type.LONG
import com.linkedin.data.schema.DataSchema.Type.STRING
import org.coursera.courier.generator.customtypes.CustomInt

Map[DataSchema.Type, String](
  INT -> Int.getClass.getName,
  LONG -> Long.getClass.getName,
  FLOAT -> Float.getClass.getName,
  DOUBLE -> Double.getClass.getName,
  BOOLEAN -> Boolean.getClass.getName,
  STRING -> "string",
  BYTES -> "bytes")


CustomInt.getClass.getName