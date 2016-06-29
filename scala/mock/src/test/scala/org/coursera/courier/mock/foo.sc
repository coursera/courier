import com.linkedin.data.DataMap
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.RecordDataSchema
import org.coursera.courier.companions.RecordCompanion
import org.coursera.courier.mock.RecordValueGenerator
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.courier.templates.ScalaRecordTemplate
import org.coursera.records.Note
import org.coursera.records.test.WithCaseClassCustomType
import org.example.FortuneCookie
import scala.collection.JavaConverters._


//val a = 1
//
//val cookie = FortuneCookie("All", None, List.empty)
//
val schema = FortuneCookie.SCHEMA
schema.getField("luckyNumbers").getDefault
//val t = schema.getField("luckyNumbers").getType match {
//  case arraySchema: ArrayDataSchema => arraySchema
//}
//t.getItems.getType
//
//val generator = new RecordValueGenerator(FortuneCookie.SCHEMA)
//generator.next()
//
//
//FortuneCookie(generator.next(), DataConversion.SetReadOnly)
