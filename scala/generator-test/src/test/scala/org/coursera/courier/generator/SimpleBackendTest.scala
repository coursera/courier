package org.coursera.courier.generator

import org.coursera.records.test.WithPrimitives
import org.coursera.records.test.WithPrimitivesSimple
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite
import play.api.libs.json.Json

class SimpleBackendTest extends JUnitSuite with AssertionsForJUnit {

  @Test
  def schemasMatch(): Unit = {
    // Courier uses `string` instead of `String` so we homogenize here
    val old = Json.parse(
      WithPrimitives.SCHEMA.toString
        .replaceAll("\"string\"", "\"String\"")
        .replaceAll("\"bytes\"", "\"ByteString\"")
        .replaceAll("\"boolean\"", "\"Boolean\"")
        .replaceAll("\"int\"", "\"Int\"")
        .replaceAll("\"long\"", "\"Long\"")
        .replaceAll("\"float\"", "\"Float\"")
        .replaceAll("\"double\"", "\"Double\"")
    )
    val `new` = WithPrimitivesSimple.SCHEMA
    // We skip `name` because those are necessarily different
    assert(`new` \ "type" === old \ "type")
    assert(`new` \ "namespace" === old \ "namespace")
    assert(`new` \ "fields" === old \ "fields")
  }

}
