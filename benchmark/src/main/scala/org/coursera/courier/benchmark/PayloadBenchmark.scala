package org.coursera.courier.benchmark

import org.coursera.benchmark.Body
import org.coursera.benchmark.ContentType
import org.coursera.benchmark.Element
import org.coursera.benchmark.ElementArray
import org.coursera.benchmark.Payload
import org.coursera.courier.data.FloatMap
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.openjdk.jmh.annotations.Benchmark
import play.api.libs.json._

class PayloadBenchmark {
  import PayloadBenchmark._

  @Benchmark
  def writeCourier(): Unit = {
    val r1 = Payload("Lorem ipsum dolor sit amet, consectetur adipiscing elit", 100,
      ElementArray(
        Element(title, 1, Some(0.5d), true, Body(FloatMap("jazz" -> 0.25f), paragraph, ContentType.AUDIO)),
        Element(title, 2, Some(0.5d), true, Body(FloatMap("jazz" -> 0.25f), paragraph, ContentType.AUDIO)),
        Element(title, 3, Some(0.5d), true, Body(FloatMap("jazz" -> 0.25f), paragraph, ContentType.AUDIO)),
        Element(title, 4, Some(0.5d), true, Body(FloatMap("jazz" -> 0.25f), paragraph, ContentType.AUDIO)),
        Element(title, 5, Some(0.5d), true, Body(FloatMap("jazz" -> 0.25f), paragraph, ContentType.AUDIO))
      ))
    DataTemplates.writeDataMap(r1.data())
  }

  @Benchmark
  def writePlayJson(): Unit = {
    val r1 = PayloadPlayJson("Lorem ipsum dolor sit amet, consectetur adipiscing elit", 100,
      Seq(
        ElementPlayJson(title, 1, Some(0.5d), true, BodyPlayJson(Map("jazz" -> 0.25f), paragraph, ContentTypePlayJson.AUDIO)),
        ElementPlayJson(title, 2, Some(0.5d), true, BodyPlayJson(Map("jazz" -> 0.25f), paragraph, ContentTypePlayJson.AUDIO)),
        ElementPlayJson(title, 3, Some(0.5d), true, BodyPlayJson(Map("jazz" -> 0.25f), paragraph, ContentTypePlayJson.AUDIO)),
        ElementPlayJson(title, 4, Some(0.5d), true, BodyPlayJson(Map("jazz" -> 0.25f), paragraph, ContentTypePlayJson.AUDIO)),
        ElementPlayJson(title, 5, Some(0.5d), true, BodyPlayJson(Map("jazz" -> 0.25f), paragraph, ContentTypePlayJson.AUDIO))
    ))
    Json.stringify(Json.toJson(r1))
  }

  @Benchmark
  def readCourier(): Unit = {
    val payload = Payload(DataTemplates.readDataMap(primitivesJson), DataConversion.SetReadOnly)
    assert(payload.title == title)
    assert(payload.offset == 100)
    payload.elements.foreach { element =>
      assert(element.title == title)
      assert(element.version >= 1)
      assert(element.weight == Some(0.5d))
      assert(element.active)
      assert(element.body.termWeights.get("jazz") == Some(0.25f))
      assert(element.body.text == paragraph)
      assert(element.body.enum == ContentType.AUDIO)
    }
  }

  @Benchmark
  def readPlayJson(): Unit = {
    val payload = Json.parse(primitivesJson).as[PayloadPlayJson]
    assert(payload.title == title)
    assert(payload.offset == 100)
    payload.elements.foreach { element =>
      assert(element.title == title)
      assert(element.version >= 1)
      assert(element.weight == Some(0.5d))
      assert(element.active)
      assert(element.body.termWeights.get("jazz") == Some(0.25f))
      assert(element.body.text == paragraph)
      assert(element.body.enum == ContentTypePlayJson.AUDIO)
    }
  }
}

object PayloadBenchmark {
  private val title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit"
  private val paragraph = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

  private val primitivesJson =
    s"""{
       |  "title" : "$title",
       |  "offset" : 100,
       |  "elements" : [
       |    { "title": "$title", "version": 1, "weight": 0.5, "active": true, "body": { "termWeights": { "jazz": 0.25 }, "text": "$paragraph", "enum": "AUDIO" } },
       |    { "title": "$title", "version": 2, "weight": 0.5, "active": true, "body": { "termWeights": { "jazz": 0.25 }, "text": "$paragraph", "enum": "AUDIO" } },
       |    { "title": "$title", "version": 3, "weight": 0.5, "active": true, "body": { "termWeights": { "jazz": 0.25 }, "text": "$paragraph", "enum": "AUDIO" } },
       |    { "title": "$title", "version": 4, "weight": 0.5, "active": true, "body": { "termWeights": { "jazz": 0.25 }, "text": "$paragraph", "enum": "AUDIO" } },
       |    { "title": "$title", "version": 5, "weight": 0.5, "active": true, "body": { "termWeights": { "jazz": 0.25 }, "text": "$paragraph", "enum": "AUDIO" } }
       |  ]
       |}""".stripMargin
}
