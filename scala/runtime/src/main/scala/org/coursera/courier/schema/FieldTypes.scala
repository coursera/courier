package org.coursera.courier.schema

import shapeless.DepFn0
import shapeless.HList
import shapeless.HNil
import shapeless.labelled.FieldType

import scala.reflect.ClassTag

trait FieldTypes[L <: HList] extends DepFn0 { type Out <: HList }

object FieldTypes {
  type Aux[L <: HList, Out0 <: HList] = FieldTypes[L] { type Out = Out0 }

  def apply[L <: HList](implicit fieldTypes: FieldTypes[L]): Aux[L, fieldTypes.Out] = fieldTypes

  implicit def hnilFieldTypes[L <: HNil]: Aux[L, HNil] = new FieldTypes[L] {
    type Out = HNil
    def apply(): Out = HNil
  }
  type ::[+A, +B <: shapeless.HList] = shapeless.::[A, B]

  implicit def hlistFieldTypes[K, V, Rest <: HList](
    implicit fieldTypesRest: FieldTypes[Rest],
    clazz: ClassTag[V]
  ): Aux[FieldType[K, V] :: Rest, String :: fieldTypesRest.Out] = new FieldTypes[FieldType[K, V] :: Rest] {
    type Out = String :: fieldTypesRest.Out

    def apply(): Out = clazz.runtimeClass.getName :: fieldTypesRest()
  }
}
