package org.coursera.test

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.validation.RequiredMode
import com.linkedin.data.schema.validation.ValidateDataAgainstSchema
import com.linkedin.data.schema.validation.ValidationOptions
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.JacksonDataTemplateCodec
import com.linkedin.data.template.PrettyPrinterJacksonDataTemplateCodec
import org.coursera.data.DataTemplates.DataConversion
import org.coursera.data.IntArray
import org.coursera.fortune.Fortune
import org.coursera.fortune.FortuneArray
import org.coursera.fortune.FortuneCookie
import org.coursera.fortune.FortuneMap
import org.coursera.fortune.MagicEightBall
import org.coursera.fortune.MagicEightBallAnswer
import org.coursera.fortune.TyperefExample
import org.coursera.models.common.UserId
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

class FortuneTest {
  val simpleFortune = "Today is your lucky day!"
  val fortuneCookie = FortuneCookie("The fortune you seek is in another cookie.", None, IntArray(1, 2, 3))
  val magicEightBall = MagicEightBall("Can I haz sports car?", MagicEightBallAnswer.OUTLOOK_NOT_SO_GOOD)

  val stringFortune = Fortune(Fortune.Telling.StringMember(simpleFortune), new DateTime(1430849546000L, DateTimeZone.UTC))
  val enumFortune = Fortune(Fortune.Telling.MagicEightBallMember(magicEightBall), new DateTime(1430849573000L, DateTimeZone.UTC))
  val recordFortune = Fortune(Fortune.Telling.FortuneCookieMember(fortuneCookie), new DateTime(1430849596000l, DateTimeZone.UTC))

  val stringFortuneJson =
    """
      |{
      |  "createdAt" : "2015-05-05T18:12:26.000Z",
      |  "telling" : {
      |    "string" : "Today is your lucky day!"
      |  }
      |}
    """.stripMargin.trim

  val enumFortuneJson =
    """
      |{
      |  "createdAt" : "2015-05-05T18:12:53.000Z",
      |  "telling" : {
      |    "org.coursera.fortune.MagicEightBall" : {
      |      "answer" : "OUTLOOK_NOT_SO_GOOD",
      |      "question" : "Can I haz sports car?"
      |    }
      |  }
      |}
    """.stripMargin.trim

  val recordFortuneJson =
  """
    |{
    |  "createdAt" : "2015-05-05T18:13:16.000Z",
    |  "telling" : {
    |    "org.coursera.fortune.FortuneCookie" : {
    |      "message" : "The fortune you seek is in another cookie.",
    |      "luckyNumbers" : [ 1, 2, 3 ]
    |    }
    |  }
    |}
  """.stripMargin.trim

  @Test
  def testUnion(): Unit = {
    val fortunes = Map(
      stringFortune -> stringFortuneJson,
      enumFortune -> enumFortuneJson,
      recordFortune -> recordFortuneJson)

    fortunes.foreach { case (fortune, json) =>
      assertValid(fortune)
      val serialized = mapToJson(fortune)
      assertEquals(json, serialized)
      val deserialized = Fortune(readJsonToMap(json), DataConversion.SetReadOnly)
      assertValid(deserialized)
      assertEquals(fortune, deserialized)
    }

    fortunes.keys.toList.combinations(2).foreach { case List(a, b) =>
      assertNotEquals(a, b)
    }
  }

  @Test
  def testArray(): Unit = {
    val fortuneArray = FortuneArray(stringFortune, enumFortune, recordFortune)
    val roundTripped = FortuneArray(
      readJsonToList(listToJson(fortuneArray)), DataConversion.SetReadOnly)
    assertEquals(roundTripped, fortuneArray)

    IntArray(List(1, 2, 3))
  }

  @Test
  def testMap(): Unit = {
    val fortuneMap = FortuneMap("str" -> stringFortune, "enum" -> enumFortune, "record" -> recordFortune)
    val roundTripped = FortuneMap(
      readJsonToMap(mapToJson(fortuneMap)), DataConversion.SetReadOnly)
    assertEquals(roundTripped, fortuneMap)
  }

  @Test
  def testTyperef(): Unit = {
    val providedTimestamp = TyperefExample(1430849546000L, UserId(1))
    val defaultTimestamp = TyperefExample(userId = UserId(1))
    val cases = Seq(providedTimestamp, defaultTimestamp)

    cases.foreach { testsCase =>
      val roundTripped = TyperefExample(
        readJsonToMap(mapToJson(testsCase)), DataConversion.SetReadOnly)
      assertEquals(roundTripped, testsCase)
    }
  }

  def assertValid(dataTemplate: DataTemplate[_]) = {
    val result = ValidateDataAgainstSchema.validate(
      dataTemplate,
      new ValidationOptions(RequiredMode.FIXUP_ABSENT_WITH_DEFAULT))
    result.isValid
  }

  private[this] val prettyPrinter = new PrettyPrinterJacksonDataTemplateCodec

  private[this] def printJson(dataTemplate: DataTemplate[DataMap]): Unit = printJson(dataTemplate.data)
  private[this] def printJson(dataMap: DataMap): Unit = println(mapToJson(dataMap))
  private[this] def mapToJson(dataTemplate: DataTemplate[DataMap]): String = mapToJson(dataTemplate.data)
  private[this] def listToJson(dataTemplate: DataTemplate[DataList]): String = listToJson(dataTemplate.data)
  private[this] def mapToJson(dataMap: DataMap): String = prettyPrinter.mapToString(dataMap)
  private[this] def listToJson(dataList: DataList): String = prettyPrinter.listToString(dataList)

  private[this] val codec = new JacksonDataTemplateCodec
  private[this] def readJsonToMap(string: String): DataMap = codec.stringToMap(string)
  private[this] def readJsonToList(string: String): DataList = codec.stringToList(string)
}
