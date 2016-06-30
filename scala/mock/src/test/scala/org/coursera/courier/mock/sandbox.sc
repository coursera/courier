import com.linkedin.data.DataMap
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.TyperefDataSchema
import org.coursera.courier.companions.RecordCompanion
import org.coursera.courier.mock.RecordValueGenerator
import org.coursera.courier.mock.ValueGenerator
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.courier.templates.ScalaRecordTemplate
import org.coursera.records.Note
import org.coursera.records.test.WithCaseClassCustomType
import org.example.Fortune
import org.example.FortuneCookie
import org.example.MagicEightBall
import org.joda.time.DateTime
import scala.collection.JavaConverters._


def foo[K](companion: RecordCompanion[K]): K = {
  val dataGenerator
}