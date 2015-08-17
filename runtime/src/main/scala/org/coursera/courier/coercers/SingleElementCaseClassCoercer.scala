/*
 Copyright 2015 Coursera Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.coursera.courier.coercers

import com.linkedin.data.ByteString
import com.linkedin.data.template.Custom
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.template.DirectCoercer
import org.coursera.courier.coercers.SingleElementCaseClassCoercer.ElementCoercer

/**
 * Coercer for single element ("arity 1") Scala case classes.
 *
 * Alleviates the need to hand write coercers for single element case classes. To use, simply
 * identify the Scala case class to coerce to in a typeref Pegasus schema definition.
 *
 * For example, to coerce to the Scala case class:
 *
 * ```scala
 * case class SlugId(slug: String)
 * ```
 *
 * Define a Pegasus typeref schema like:
 *
 * ```json
 * {
 *   "name": "SlugId",
 *   "namespace": "org.example.schemas",
 *   "type": "typeref",
 *   "ref": "string",
 *   "scala": {
 *     "class": "org.example.SlugId"
 *   }
 * }
 * ```
 *
 * The class to coerce to must:
 * - Be public and extend [[Product]]. All case classes extend [[Product]].
 * - Have a single public constructor.
 * - Have a single public field/element. That is, [[Product.productArity]] must return 1.
 * - Have an element that is (a) a primitive, or (b) another Pegasus CustomType that has a
 *   registered coercer. `Product.productElement(0)` must return this element.
 * - (Case classes *may* extend [[AnyVal]] if desired)
 *
 * A primitive may be:
 * - Any Scala [[AnyVal]] type except [[Unit]]
 * - Any boxed primitive type (e.g. `java.lang.Integer`)
 *
 * A few primitives are handled as special cases by this coercer:
 * - [[Char]] is represented as a single char `string` Pegasus type (JSON string)
 * - [[Byte]] is represented as a single byte `bytes` Pegasus type (JSON string with avro byte
 *   encoding)
 * - [[Short]] is represented as a `int` pegasus type (JSON number).
 *
 *
 * "Chaining" is supported. For example, if the `SlugId` custom type is defined as shown in
 * the above example, the Scala case class:
 *
 * ```scala
 * case class ClassId(slug: SlugId)
 * ```
 *
 * Can be bound as a custom type that "chains" off of `SlugId`:
 *
 * ```json
 * {
 *   "name": "ClassId",
 *   "namespace": "org.example.schemas",
 *   "type": "typeref",
 *   "ref": "org.example.schemas.SlugId",
 *   "scala": {
 *     "class": "org.example.ClassId"
 *   }
 * }
 * ```
 *
 * "Chaining" also works with ref types that have custom coercers. For example, if
 * `org.example.DateTime` is custom type with a `coercerClass` to `org.joda.time.DataTime`, in can
 * be bound to a custom type using:
 *
 * ```scala
 * case class CreatedAt(time: org.joda.time.DateTime)
 * ```
 *
 * ```json
 * {
 *   "name": "CreatedAt",
 *   "namespace": "org.example.schemas",
 *   "type": "typeref",
 *   "ref": "org.example.schemas.DateTime",
 *   "scala": {
 *     "class": "org.example.CreatedAt"
 *   }
 * }
 * ```
 *
 */
class SingleElementCaseClassCoercer[T <: Product](
    productType: Class[T],
    coercer: ElementCoercer) extends DirectCoercer[T] {

  override def coerceInput(obj: T): AnyRef = {
    coercer.coerceInput(obj)
  }

  override def coerceOutput(obj: AnyRef): T = {
    val coerced = coercer.coerceOutput(obj)
    assert(coerced.getClass == productType,
      s"Internal error in SingleElementCaseClassCoercer. When coercing to $productType, internal " +
      s"coercer returned a ${coercer.getClass}")
    coerced.asInstanceOf[T]
  }
}

object SingleElementCaseClassCoercer {

  /**
   * Registers coercion for a Scala case class.
   *
   * The coercer is dynamically built at runtime based on the provided type information.
   *
   * @param caseClassType provides the type of the case class, this is typically the pegasus
   *                      customType class.
   * @param dataType provides the pegasus data type to coerce the case class to.
   * @tparam T provides case class type.
   */
  def registerCoercer[T <: Product](caseClassType: Class[T], dataType: Class[_]): Unit = {
    val elementCoercer = buildElementCoercer(caseClassType, dataType)
    Custom.registerCoercer(
      new SingleElementCaseClassCoercer(caseClassType, elementCoercer), caseClassType)
  }

  // The type of the Scala case class element we bind to is known at runtime, and as a result,
  // we have limited static type information to work with in the below coercion code.
  // Instead we rely on Class types and perform runtime type checks and guards.
  private trait ElementCoercer {
    def coerceInput(in: Any): AnyRef
    def coerceOutput(out: AnyRef): AnyRef
  }

  /**
   * Examines the provided single element case class and builds a coercer for it.
   *
   * @param caseClassType provides a single element case class.
   * @param dataType provides the pegasus data type to coerce to. For typerefs, this must be the
   *                 fully dereferenced type.
   * @return a coercer that can marshall to/from the Scala case class and the pegasus data type.
   */
  private[this] def buildElementCoercer[T <: Product](
      caseClassType: Class[T],
      dataType: Class[_]): ElementCoercer = {

    val arity = CaseClassReflect.productArity(caseClassType)
    require(arity == 1,
      s"Case class must have exactly one public field, but has: $arity " +
      s" fields: ${caseClassType.getClass}")
    val elemType = CaseClassReflect.productElementType(caseClassType, 0)

    val elementCoercer = extendedPrimitiveCoercers.get(elemType).map { coercer =>
        dataTypeCoercer(coercer, dataType, dataType)
      }.getOrElse {
        dataTypeCoercer(defaultCoercer, elemType, dataType)
      }
    caseClassCoercer(caseClassType, elementCoercer)
  }

  /**
   * Coerces to/from a single element scala case class.
   *
   * Requires that the element data is already properly coerced to the correct type.
   *
   * @param caseClassType provides the case class to coerce to/from.
   * @param wrapped provides a chained coercer that provides properly typed data.
   * @return a coercer that wraps data with the case class when coercing out and unwraps the data
   *         from the case class when coercing in.
   */
  private[this] def caseClassCoercer[T <: Product](
      caseClassType: Class[T],
      wrapped: ElementCoercer) = new ElementCoercer {

    def coerceInput(obj: Any) = {
      // we've already validated the arity is 1
      val element = obj match {
        case product: Product => autobox(product.productElement(0))
        case _: AnyRef => throw new IllegalArgumentException(
          s"Expected product, but got data: $obj of type ${obj.getClass}"
        )
      }
      wrapped.coerceInput(element)
    }

    def coerceOutput(obj: AnyRef) = {
      val boxedElementType = wrapped.coerceOutput(obj)
      autobox(CaseClassReflect
        .newInstance(caseClassType, boxedElementType))
    }
  }

  /**
   * Wraps a element coercer with a pegasus data coercer.
   * This will, for example, convert a string to a [[ByteString]] before applying the byteCoercer.
   *
   * See [[DataTemplateUtil.coerceOutput]] for details.
   */
  private[this] def dataTypeCoercer[T](
      wrapped: ElementCoercer,
      targetType: Class[_],
      dataType: Class[_]) = new ElementCoercer {

    def coerceInput(value: Any): AnyRef = {
      // Scala does not provide a type bounds for `getClass`. It just returns `Class[_]`.
      // In Java the type bounds are correctly defined as `Class<? extends |X|>` (via compiler
      // trickery). So while the types here are correct, Scala doesn't help us out wit them and we
      // must explicitly cast our types here to appease the compiler.
      val input = wrapped.coerceInput(value).asInstanceOf[T]
      val clazz = input.getClass.asInstanceOf[Class[T]]
      DataTemplateUtil.coerceInput(input, clazz, dataType)
    }

    def coerceOutput(value: AnyRef): AnyRef = {
      wrapped.coerceOutput(
        autobox(DataTemplateUtil.coerceOutput(value, targetType)))
    }
  }

  private[this] val defaultCoercer = new ElementCoercer {
    def coerceInput(value: Any) = autobox(value)
    def coerceOutput(value: AnyRef) = value
  }

  // Scala is capable of autoboxing all Any types to AnyRef (even Unit.. see BoxedUnit).
  //
  // scala> (1: Int).asInstanceOf[AnyRef].getClass
  // res3: Class[_ <: AnyRef] = class java.lang.Integer
  //
  private[this] def autobox(any: Any): AnyRef = any.asInstanceOf[AnyRef]

  //
  // Supplementary coercers for Scala types that do not have a corresponding Pegasus type already.
  //

  /**
   * Coerces [[Short]] and [[java.lang.Short]] to [[java.lang.Integer]].
   */
  private[this] val shortCoercer = new ElementCoercer {
    def coerceInput(in: Any) = {
      in match {
        case short: Short => Int.box(short.toInt)
        case boxed: java.lang.Short => Int.box(boxed.toInt)
        case _: Any => throw new IllegalArgumentException()
      }

    }
    def coerceOutput(ref: AnyRef) = {
      ref match {
        case integer: java.lang.Integer => Short.box(integer.shortValue())
        case _: Any => throw new IllegalArgumentException()
      }
    }
  }

  /**
   * Coerces [[Byte]] and [[java.lang.Byte]] to [[ByteString]].
   */
  private[this] val byteCoercer = new ElementCoercer {
    def coerceInput(in: Any) = {
      in match {
        case byte: Byte => ByteString.copy(Array(byte))
        case boxed: java.lang.Byte => ByteString.copy(Array(boxed.byteValue()))
        case _: Any => throw new IllegalArgumentException()
      }

    }
    def coerceOutput(ref: AnyRef) = {
      ref match {
        case bytes: ByteString =>
          require(bytes.length() == 1,
            s"Exactly one byte required, but ${bytes.length()} found: ${bytes.asAvroString()}")
          Byte.box(bytes.copyBytes()(0))
        case _: Any => throw new IllegalArgumentException()
      }

    }
  }

  /**
   * Coerces [[Char]] and [[java.lang.Character]] to [[String]].
   */
  private[this] val charCoercer = new ElementCoercer {
    def coerceInput(in: Any) = {
      in match {
        case char: Char => char.toString
        case boxed: java.lang.Character => boxed.toString
        case _: Any => throw new IllegalArgumentException()
      }
    }
    def coerceOutput(ref: AnyRef) = {
      ref match {
        case string: String =>
          require(string.length() == 1,
            s"Exactly one char required, but ${string.length()} found: $string")
          Char.box(string.charAt(0))
        case _: Any => throw new IllegalArgumentException()
      }
    }
  }

  private[this] lazy val extendedPrimitiveCoercers = Map[Class[_], ElementCoercer](
    classOf[Short] -> shortCoercer,
    classOf[java.lang.Short] -> shortCoercer,
    classOf[Byte] -> byteCoercer,
    classOf[java.lang.Byte] -> byteCoercer,
    classOf[Char] -> charCoercer,
    classOf[java.lang.Character] -> charCoercer)
}
