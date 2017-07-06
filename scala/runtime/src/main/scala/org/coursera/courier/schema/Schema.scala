package org.coursera.courier.schema

import play.api.libs.json.JsArray
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import shapeless.HList
import shapeless.LabelledGeneric
import shapeless.Poly1
import shapeless.Typeable
import shapeless.ops.hlist.LiftAll
import shapeless.ops.hlist.Mapper
import shapeless.ops.hlist.ToTraversable
import shapeless.ops.record.UnzipFields

import scala.reflect.ClassTag

class Schema[A] {
  def asJson[
  Repr <: HList,
  Keys <: HList,
  Values <: HList,
  Typeables <: HList,
  MapperOut <: HList
  ](implicit
    clazz: ClassTag[A],
    generic: LabelledGeneric.Aux[A, Repr],
    unzip: UnzipFields.Aux[Repr, Keys, Values],
    typeable: LiftAll.Aux[Typeable, Values, Typeables],
    fieldTypes: Mapper.Aux[Schema.describe.type, Typeables, MapperOut],
    traversable1: ToTraversable.Aux[Keys, List, Symbol],
    traversable2: ToTraversable.Aux[MapperOut, List, String]
  ): JsObject = {
    val keys = unzip.keys.toList.map(_.name)
    val types: List[String] = fieldTypes(typeable.instances).toList
    val fields = keys.zip(types).map {
      case (name, typ) =>
        Json.obj(
          "name" -> name,
          "type" -> typ
        )
    }
    Json.obj(
      "type" -> "record",
      "name" -> clazz.runtimeClass.getSimpleName,
      "namespace" -> clazz.runtimeClass.getPackage.getName,
      "fields" -> JsArray(fields)
    )
  }
}

object Schema {
  def apply[A] = new Schema[A]

  object describe extends Poly1 {
    implicit def typeable[T] = at[Typeable[T]](_.describe)
  }
}
