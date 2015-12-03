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

package org.coursera.courier.codecs

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.nio.charset.Charset

import com.linkedin.data.ByteString
import com.linkedin.data.DataComplex
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.codec.DataCodec
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.BooleanDataSchema
import com.linkedin.data.schema.BytesDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.DoubleDataSchema
import com.linkedin.data.schema.EnumDataSchema
import com.linkedin.data.schema.FixedDataSchema
import com.linkedin.data.schema.FloatDataSchema
import com.linkedin.data.schema.IntegerDataSchema
import com.linkedin.data.schema.LongDataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.StringDataSchema
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.schema.validation.CoercionMode
import com.linkedin.data.schema.validation.RequiredMode
import com.linkedin.data.schema.validation.ValidateDataAgainstSchema
import com.linkedin.data.schema.validation.ValidationOptions

import scala.collection.JavaConverters._
import scala.collection.immutable.SortedSet
import scala.util.parsing.combinator.RegexParsers

/**
 * Provides a URL "Friendly" string encoding of data.
 *
 * This encoding aims to strike a balance between:
 *
 * - Safety, convenience and brevity when used on the URL
 * - JSON equivalence
 *
 * To that end, this encoding:
 *
 * - Guarantees that equivalent data is also string equivalent.
 * - Does not use any URL reserved chars in the core syntax.
 * - Does not require quoting of string primitives.
 *
 * In order to achieve this, the encoding has a few constraints:
 *
 * - All map keys must be sorted in alpha-numeric order for serialization.
 * - Extraneous whitespace is not allowed.
 * - Primitives must be represented in "canonical form". For example there may be no leading or
 *   trailing zeros on numeric types.
 *
 * This encoding also has a few limitations:
 *
 * - Serialized data must be URL encoded before being used in URLs, but only to escape chars that
 *   might appear in string literals.
 * - It is lossy: All primitives types are downgraded to plain strings, but can be "fixed-up",
 *   this is explained in detail below.
 * - An array containing a single empty string (`[""]` in JSON), has the special representation of
 *   `List(~)` in this format. This is to prevent an ambiguity between an empty list and a list
 *   containing a single empty string.
 *
 * JSON Encoding                     | Inline String Encoding
 * ----------------------------------|---------------------------------------
 * `{ "a": "one", "b": "two"}`       | `(a~one,b~two)`
 * `[1, 2, 3]`                       | `List(1,2,3)`
 * `{ "ids": [1,2], "score": 3.14 }` | `(ids~List(1,2),score~3.14)`
 *
 * Escaping:
 *
 * When used in strings the reserved chars `(),~!` must be escaped by prefixing them with `!`.
 *
 * JSON Encoding                        | Inline String Encoding
 * -------------------------------------|---------------------------------------
 * `{ "string": "~An (odd) string !" }` | `(string~!~An !(odd!) string !!)`
 *
 * Fix up:
 *
 * Pegasus provides "Fix-up" to convert plain strings used in this format
 * back to their correct primitive types. It is recommended that fix-up be run on all
 * data deserialized from this format, e.g.:
 *
 * {{{
 *   val validationResult =
 *     ValidateDataAgainstSchema.validate(
 *       dataMap,
 *       schema,
 *       new ValidationOptions(
 *         RequiredMode.FIXUP_ABSENT_WITH_DEFAULT,
 *         CoercionMode.STRING_TO_PRIMITIVE))
 *   if (validationResult.isValid) {
 *     // The data map is now fixed up, if it has been marked as ReadOnly,
 *     // use validationResult.getFixed to get the fixed-up copy, otherwise dataMap
 *     // will be fixed up in-place.
 *   }
 * }}}
 *
 */
class InlineStringCodec extends DataCodec {

  private val DEFAULT_BUFFER_SIZE = 64

  override def writeMap(dataMap: DataMap, outputStream: OutputStream): Unit = {
    InlineStringCodec.Generator.generateMap(dataMap, outputStream)
  }

  override def bytesToMap(bytes: Array[Byte]): DataMap = {
    readMap(new ByteArrayInputStream(bytes))
  }

  override def readMap(inputStream: InputStream): DataMap = {
    InlineStringCodec.Parser.parseMap(inputStream)
  }

  override def mapToBytes(dataMap: DataMap): Array[Byte] = {
    val out = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE)
    writeMap(dataMap, out)
    out.close()
    out.toByteArray
  }

  override def writeList(dataList: DataList, outputStream: OutputStream): Unit = {
    InlineStringCodec.Generator.generateList(dataList, outputStream)
  }

  override def bytesToList(bytes: Array[Byte]): DataList = {
    readList(new ByteArrayInputStream(bytes))
  }

  override def readList(inputStream: InputStream): DataList = {
    InlineStringCodec.Parser.parseList(inputStream)
  }

  override def listToBytes(dataList: DataList): Array[Byte] = {
    val out = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE)
    writeList(dataList, out)
    out.close()
    out.toByteArray
  }
}

object InlineStringCodec {
  val charset = Charset.forName("UTF-8")

  val defaultValidationOptions = new ValidationOptions(
    RequiredMode.FIXUP_ABSENT_WITH_DEFAULT,
    CoercionMode.STRING_TO_PRIMITIVE)

  val instance = new InlineStringCodec()

  /**
   * Returns the deserialized string value, either a primitive Scala type, a `ByteString`,
   * a `DataMap` or a `DataList`, depending on the schema provided.
   * Throws an `IOException` or `NumberFormatException` if deserialization
   * fails.
   *
   * For primitive types Scala's built in primitive serializiation/deserialization is used.
   * For `ByteString`, "avro string" format is used.
   * For complex types, `InlineStringCodec` is used.
   */
  def stringToData(key: String, schema: DataSchema): AnyRef = {
    schema.getDereferencedDataSchema match {
      case _: IntegerDataSchema => Int.box(key.toInt)
      case _: LongDataSchema => Long.box(key.toInt)
      case _: FloatDataSchema => Float.box(key.toFloat)
      case _: DoubleDataSchema => Double.box(key.toDouble)
      case _: BooleanDataSchema => Boolean.box(key.toBoolean)
      case _: StringDataSchema => key
      case _: BytesDataSchema => ByteString.copyString(key, InlineStringCodec.charset)
      case _: RecordDataSchema | _: UnionDataSchema | _: MapDataSchema =>
        stringToDataMap(key, schema)
      case _: ArrayDataSchema => stringToDataList(key, schema)
      case _: EnumDataSchema => key
      case _: FixedDataSchema => ByteString.copyString(key, InlineStringCodec.charset)
      case unknown: DataSchema =>
        throw new IllegalArgumentException(s"Unsupported schema type: ${unknown.getClass}")
    }
  }

  private[this] def stringToDataMap(key: String, schema: DataSchema): DataMap = {
    val map = instance.bytesToMap(key.getBytes(InlineStringCodec.charset))
    val validationResult = ValidateDataAgainstSchema.validate(map, schema, defaultValidationOptions)
    if (!validationResult.isValid) {
      throw new IOException(validationResult.getMessages.asScala.map(_.toString).mkString(", "))
    }
    map
  }

  private[this] def stringToDataList(key: String, schema: DataSchema): DataList = {
    val list = instance.bytesToList(key.getBytes(InlineStringCodec.charset))
    val validationResult =
      ValidateDataAgainstSchema.validate(list, schema, defaultValidationOptions)
    if (!validationResult.isValid) {
      throw new IOException(validationResult.getMessages.asScala.map(_.toString).mkString(", "))
    }
    list
  }

  /**
   * Returns the serialized value of the given data. Accepts primitive Scala types, `ByteString`,
   * `DataMap` and `DataList`.
   *
   * For primitive types Scala's built in primitive serializiation/deserialization is used.
   * For `ByteString`, "avro string" format is used.
   * For complex types, `InlineStringCodec` is used.
   */
  def dataToString(any: AnyRef): String = {
    any match {
      case map: DataMap => new String(instance.mapToBytes(map), InlineStringCodec.charset)
      case list: DataList => new String(instance.listToBytes(list), InlineStringCodec.charset)
      case int: java.lang.Integer => int.toString
      case long: java.lang.Long => long.toString
      case float: java.lang.Float => float.toString
      case double: java.lang.Double => double.toString
      case boolean: java.lang.Boolean => boolean.toString
      case string: String => string
      case bytes: ByteString => bytes.asAvroString()
      case unknown: AnyRef =>
        throw new IllegalArgumentException(s"Unsupported type: ${unknown.getClass}")
    }
  }

  private[this] val escapeChar = '!'
  private[this] val startCollection = '('
  private[this] val endCollection = ')'
  private[this] val itemSeparator = ','
  private[this] val keyValueSeparator = '~'

  private[this] val listPrefix = "List"

  private[this] val reservedCharSet = SortedSet(
    startCollection, endCollection, itemSeparator, keyValueSeparator, escapeChar)

  private[this] val reservedChars = reservedCharSet.mkString("")

  private[this] val escapeRegex = s"""([$reservedChars])""".r
  private[this] val unEscapeRegex = s"""$escapeChar([$reservedChars])""".r

  def escape(unescaped: String): String = {
    escapeRegex.replaceAllIn(unescaped, s"$escapeChar$$1")
  }

  def unescape(escaped: String): String = {
    unEscapeRegex.replaceAllIn(escaped, "$1")
  }

  object Parser extends RegexParsers {
    // TODO(jbetz): We should consider adding a Strict mode to this codec that would
    // fail if map keys are not sorted, there is any extraneous whitespace or primitives are
    // provided in a non-canonical form.

    // When we parse, we are more forgiving for some of our format rules.
    // While whitespace is not allowed in the format, we tolerate it when parsing.
    // We are also forgiving when parsing map keys-- we don't require they are sorted.

    override protected val whiteSpace = """(\s)+""".r

    def parseMap[T](input: InputStream): DataMap = {
      handleParseErrors(parseAll(mapParser, new InputStreamReader(input)))
    }

    def parseList[T](input: InputStream): DataList = {
      handleParseErrors(parseAll(listParser, new InputStreamReader(input)))
    }

    private[this] def handleParseErrors[T](parseResult: ParseResult[T]): T = {
      parseResult match {
        case Success(result, _) => result
        case failure: NoSuccess =>
          throw new IOException(
            s"${failure.msg} line: ${failure.next.pos.line} column: ${failure.next.pos.column}")
      }
    }

    private[this] val primitiveRegex = s"""([^$reservedChars]|$escapeChar[$reservedChars])*""".r

    // This codec does not distinguish between primitive types, all are simply a string value
    // Pegasus will "Fix-up" these values into the correct primitive types when the data is
    // validated.
    private[this] val primitiveParser: Parser[String] =
      primitiveRegex ^^ { value =>
      unescape(value)
    }

    private[this] val generalListParser: Parser[DataList] = {
      listPrefix ~ startCollection ~> repsep(dataParser, itemSeparator) <~ endCollection ^^ {
        case List(singleElement) if singleElement == "" => // see specialCaseListParser, below
          new DataList(0)
        case values: List[AnyRef] =>
          val dataList = new DataList(values.size)
          values.foreach { value =>
            dataList.add(value)
          }
          dataList
      }
    }

    // An annoyance of unquoted strings is that a list with a single empty string needs to be
    // distinguishable from an empty list somehow. We have decided to treat `List()` as an empty
    // list. So we provide a special representation of a List containing a single empty
    // string: `List(~)`.
    private[this] val specialCaseListParser: Parser[DataList] = {
      listPrefix ~ startCollection ~ keyValueSeparator ~ endCollection ^^ {
        case _: AnyRef =>
          val dataList = new DataList(1)
          dataList.add("")
          dataList
      }
    }

    private[this] val listParser: Parser[DataList] = generalListParser | specialCaseListParser

    private[this] val keyValueParser: Parser[(String, AnyRef)] = {
      primitiveParser ~ keyValueSeparator ~ dataParser ^^ {
        case key ~ _ ~ value => (key, value)
      }
    }

    private[this] val mapParser: Parser[DataMap] = {
      startCollection ~> repsep(keyValueParser, itemSeparator) <~ endCollection ^^ {
        case kvs: List[(String, AnyRef)] =>
          val dataMap = new DataMap(kvs.size)
          kvs.foreach { case (key, value) =>
            dataMap.put(key, value)
          }
          dataMap
      }
    }

    private[this] val dataComplexParser: Parser[DataComplex] = mapParser | listParser

    private[this] val dataParser: Parser[AnyRef] = dataComplexParser | primitiveParser
  }

  object Generator {
    private[this] val startCollectionBytes = startCollection.toString.getBytes(charset)
    private[this] val endCollectionBytes = endCollection.toString.getBytes(charset)
    private[this] val keyValueSeparatorBytes = keyValueSeparator.toString.getBytes(charset)
    private[this] val itemSeparatorBytes = itemSeparator.toString.getBytes(charset)
    private[this] val listPrefixBytes = listPrefix.getBytes(charset)

    def generateList(dataList: DataList, outputStream: OutputStream): Unit = {

      if(dataList.size() == 1 && dataList.get(0) == "") { // see specialCaseListParser, above
        outputStream.write(listPrefixBytes)
        outputStream.write(startCollectionBytes)
        outputStream.write(keyValueSeparatorBytes)
        outputStream.write(endCollectionBytes)
      } else {
        outputStream.write(listPrefixBytes)
        outputStream.write(startCollectionBytes)
        dataList.iterator().asScala.zipWithIndex.foreach { case (dataElement, idx) =>
          generate(dataElement, outputStream)
          if (idx < dataList.size() - 1) {
            outputStream.write(itemSeparatorBytes)
          }
        }
        outputStream.write(endCollectionBytes)
      }
    }

    def generateMap(dataMap: DataMap, outputStream: OutputStream): Unit = {
      val sortedEntries = dataMap.entrySet().asScala.toList.sortBy(entry => entry.getKey)

      outputStream.write(startCollectionBytes)
      sortedEntries.zipWithIndex.foreach { case (entry, idx) =>
        outputStream.write(escape(entry.getKey).getBytes(charset))
        outputStream.write(keyValueSeparatorBytes)
        generate(entry.getValue, outputStream)
        if (idx < dataMap.size() - 1) {
          outputStream.write(itemSeparatorBytes)
        }
      }
      outputStream.write(endCollectionBytes)
    }

    private[this] def generate(data: AnyRef, outputStream: OutputStream): Unit = {
      data match {
        case map: DataMap =>
          generateMap(map, outputStream)
        case list: DataList =>
          generateList(list, outputStream)
        case bytes: ByteString =>
          outputStream.write(bytes.asAvroString().getBytes(charset))
        case primitive: AnyRef =>
          outputStream.write(escape(primitive.toString).getBytes(charset))
      }
    }
  }
}
