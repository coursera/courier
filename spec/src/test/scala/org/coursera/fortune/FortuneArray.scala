package org.coursera.fortune

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.data.DataTemplates
import org.coursera.data.DataTemplates.DataConversion
import org.coursera.data.generated

import scala.collection.GenTraversable
import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

@generated(source = "/org/coursera/fortune/FortuneArrayExample.pdsc")
final class FortuneArray(private val dataList: DataList)
  extends IndexedSeq[Fortune]
  with Product
  with GenTraversable[Fortune]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(idx: Int) = {
    Fortune(dataList.get(idx).asInstanceOf[DataMap], DataTemplates.DataConversion.SetReadOnly)
  }

  override def apply(idx: Int): Fortune = lookup(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = FortuneArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
}

object FortuneArray {
  private val SCHEMA = DataTemplateUtil.parseSchema(
    """
      |{
      |  "type" : "array",
      |  "items" : {
      |    "type": "record",
      |    "name": "Fortune",
      |    "namespace": "org.coursera.fortune",
      |    "doc": "A fortune.",
      |    "fields": [
      |      {
      |        "name": "telling",
      |        "doc": "The fortune telling.",
      |        "type": [
      |          {
      |            "type": "record",
      |            "name": "FortuneCookie",
      |            "namespace": "org.coursera.fortune",
      |            "doc": "A fortune cookie.",
      |            "fields": [
      |              {
      |                "name": "message",
      |                "type": "string",
      |                "doc": "A fortune cookie message."
      |              },
      |              {
      |                "name": "certainty",
      |                "type": "float",
      |                "optional": true
      |              }
      |            ]
      |          },
      |          {
      |            "type": "record",
      |            "name": "MagicEightBall",
      |            "namespace": "org.coursera.fortune",
      |            "fields": [
      |              {
      |                "name": "question",
      |                "type": "string"
      |              },
      |              {
      |                "name": "answer",
      |                "type": {
      |                  "name": "MagicEightBallAnswer",
      |                  "namespace": "org.coursera.fortune",
      |                  "doc": "Magic eight ball answers.",
      |                  "type": "enum",
      |                  "symbols": [
      |                    "IT_IS_CERTAIN",
      |                    "ASK_AGAIN_LATER",
      |                    "OUTLOOK_NOT_SO_GOOD"
      |                  ],
      |                  "symbolDocs": {
      |                    "ASK_AGAIN_LATER": "Where later is at least 10 ms from now."
      |                  }
      |                }
      |              }
      |            ]
      |          },
      |          "string"
      |        ]
      |      }
      |    ]
      |  }
      |}
      |""".stripMargin
  ).asInstanceOf[ArrayDataSchema]

  val empty = FortuneArray()

  def apply(elems: Fortune*): FortuneArray = {
    new FortuneArray(new DataList(elems.map(_.data()).toList.asJava))
  }

  def apply(collection: Traversable[Fortune]): FortuneArray = {
    new FortuneArray(new DataList(collection.map(_.data()).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion) : FortuneArray = {
    new FortuneArray(DataTemplates.makeImmutable(dataList, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[FortuneArray, Fortune, FortuneArray] {
    def apply(from: FortuneArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: FortuneArray) extends mutable.Builder[Fortune, FortuneArray] {
    def this() = this(new FortuneArray(new DataList()))

    val elems = new DataList(initial.dataList)

    def +=(x: Fortune): this.type = {
      elems.add(x.data())
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.setReadOnly()
      new FortuneArray(elems)
    }
  }
}

