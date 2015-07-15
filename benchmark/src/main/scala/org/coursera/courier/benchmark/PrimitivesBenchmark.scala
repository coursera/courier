package org.coursera.courier.benchmark

import com.linkedin.data.ByteString
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.openjdk.jmh.annotations.Benchmark
import org.coursera.benchmark.Primitives
import play.api.libs.json._

class PrimitivesBenchmark {
  import PrimitivesBenchmark._

  /*@Benchmark
  def writePrimitives(): Unit = {
    val r1 = Primitives(1, 2L, 3.3f, 4.4d, true, "str")
    DataTemplates.writeDataMap(r1.data())
  }

  @Benchmark
  def writePrimitivesPlayJson(): Unit = {
    val r1 = PrimitivesPlayJson(1, 2L, 3.3f, 4.4d, true, "str")
    Json.stringify(Json.toJson(r1))
  }

  @Benchmark
  def readPrimitives(): Unit = {
    val primitives = Primitives(DataTemplates.readDataMap(primitivesJson), DataConversion.SetReadOnly)
    assert(primitives.intField == 1)
    assert(primitives.longField == 2L)
    assert(primitives.floatField == 3.3f)
    assert(primitives.doubleField == 4.4d)
    assert(primitives.booleanField)
    assert(primitives.stringField == "str")
  }

  @Benchmark
  def readPrimitivesPlayJson(): Unit = {
    val primitives = Json.parse(primitivesJson).as[PrimitivesPlayJson]
    assert(primitives.intField == 1)
    assert(primitives.longField == 2L)
    assert(primitives.floatField == 3.3f)
    assert(primitives.doubleField == 4.4d)
    assert(primitives.booleanField)
    assert(primitives.stringField == "str")
  }

  @Benchmark
  def roundTripPrimitives(): Unit = {
    val r1 = Primitives(1, 2L, 3.3f, 4.4d, true, "str")
    val json = DataTemplates.writeDataMap(r1.data())
    val primitives = Primitives(DataTemplates.readDataMap(json), DataConversion.SetReadOnly)
    assert(primitives.intField == 1)
    assert(primitives.longField == 2L)
    assert(primitives.floatField == 3.3f)
    assert(primitives.doubleField == 4.4d)
    assert(primitives.booleanField)
    assert(primitives.stringField == "str")
  }

  @Benchmark
  def roundTripPrimitivesPlayJson(): Unit = {
    val r1 = PrimitivesPlayJson(1, 2L, 3.3f, 4.4d, true, "str")
    val string = Json.stringify(Json.toJson(r1))
    val primitives = Json.parse(string).as[PrimitivesPlayJson]
    assert(primitives.intField == 1)
    assert(primitives.longField == 2L)
    assert(primitives.floatField == 3.3f)
    assert(primitives.doubleField == 4.4d)
    assert(primitives.booleanField)
    assert(primitives.stringField == "str")
  }

  @Benchmark
  def writeReadPrimitives(): Unit = {
    val primitives = Primitives(1, 2L, 3.3f, 4.4d, true, "str")
    assert(primitives.intField == 1)
    assert(primitives.longField == 2L)
    assert(primitives.floatField == 3.3f)
    assert(primitives.doubleField == 4.4d)
    assert(primitives.booleanField)
    assert(primitives.stringField == "str")
  }

  @Benchmark
  def writeReadPrimitivesPlayJson(): Unit = {
    val primitives = PrimitivesPlayJson(1, 2L, 3.3f, 4.4d, true, "str")
    assert(primitives.intField == 1)
    assert(primitives.longField == 2L)
    assert(primitives.floatField == 3.3f)
    assert(primitives.doubleField == 4.4d)
    assert(primitives.booleanField)
    assert(primitives.stringField == "str")
  }*/
}

object PrimitivesBenchmark {
  private val primitivesJson =
    s"""{
       |  "floatField" : 3.3,
       |  "doubleField" : 4.4,
       |  "intField" : 1,
       |  "longField" : 2,
       |  "booleanField" : true,
       |  "stringField" : "str"
       |}""".stripMargin
}
