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

package org.coursera.courier.generator

import com.linkedin.data.DataMap
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.records.Message
import org.coursera.records.Note
import org.coursera.records.WithFlatTypedDefinition
import org.coursera.records.WithTypedDefinition
import org.coursera.records.WithUnion
import org.coursera.typerefs.FlatTypedDefinition
import org.coursera.typerefs.InlineRecord
import org.coursera.typerefs.InlineRecord2
import org.coursera.typerefs.TypedDefinition
import org.coursera.typerefs.Union
import org.coursera.typerefs.UnionWithInlineRecord
import org.junit.Test

/**
 * Tests for typeref union types:
 *   - Union (typeref with NoteMember | MessageMember)
 *   - TypedDefinition (typed definition union)
 *   - FlatTypedDefinition (flat typed definition union)
 *   - UnionWithInlineRecord (union with inline record types)
 * And wrapper record types for these.
 */
class TyperefUnionGeneratorTest extends GeneratorTest with SchemaFixtures {

  // ---------------------------------------------------------------------------
  // Union typeref (org.coursera.typerefs.Union)
  // ---------------------------------------------------------------------------

  @Test
  def testUnion_noteMember(): Unit = {
    val note = Note("hello")
    val member = Union.NoteMember(note)
    assert(member.value === note)
    assert(member._1 === note)
  }

  @Test
  def testUnion_messageMember(): Unit = {
    val msg = Message(Some("title"), Some("body"))
    val member = Union.MessageMember(msg)
    assert(member.value === msg)
  }

  @Test
  def testUnion_build_noteMember(): Unit = {
    val note = Note("world")
    val original = Union.NoteMember(note)
    val built = Union.build(roundTrip(original.data().asInstanceOf[DataMap]), DataConversion.SetReadOnly)
    assert(built.isInstanceOf[Union.NoteMember])
    assert(built.asInstanceOf[Union.NoteMember].value.text === "world")
  }

  @Test
  def testUnion_build_messageMember(): Unit = {
    val msg = Message(Some("t"), Some("b"))
    val original = Union.MessageMember(msg)
    val built = Union.build(roundTrip(original.data().asInstanceOf[DataMap]), DataConversion.SetReadOnly)
    assert(built.isInstanceOf[Union.MessageMember])
  }

  @Test
  def testUnion_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "value")
    dataMap.makeReadOnly()
    val built = Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[Union.$UnknownMember])
  }

  @Test
  def testUnion_unapply(): Unit = {
    val note = Note("text")
    val member = Union.NoteMember(note)
    val Union.NoteMember(v) = member
    assert(v.text === "text")
  }

  @Test
  def testUnion_equality(): Unit = {
    val a = Union.NoteMember(Note("a"))
    val b = Union.NoteMember(Note("a"))
    val c = Union.NoteMember(Note("b"))
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testUnion_toString(): Unit = {
    val m = Union.NoteMember(Note("test"))
    assert(m.toString.nonEmpty)
  }

  @Test
  def testUnion_implicit_note(): Unit = {
    val note = Note("implicit")
    val u: Union = note
    assert(u.isInstanceOf[Union.NoteMember])
  }

  @Test
  def testUnion_implicit_message(): Unit = {
    val msg = Message(Some("t"), None)
    val u: Union = msg
    assert(u.isInstanceOf[Union.MessageMember])
  }

  // ---------------------------------------------------------------------------
  // WithUnion wrapper record
  // ---------------------------------------------------------------------------

  @Test
  def testWithUnion_construction(): Unit = {
    val note = Note("note text")
    val r = WithUnion(Union.NoteMember(note))
    assert(r.value.isInstanceOf[Union.NoteMember])
  }

  @Test
  def testWithUnion_roundTrip(): Unit = {
    val original = WithUnion(Union.NoteMember(Note("round")))
    val roundTripped = WithUnion.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithUnion_equality(): Unit = {
    val a = WithUnion(Union.NoteMember(Note("a")))
    val b = WithUnion(Union.NoteMember(Note("a")))
    val c = WithUnion(Union.NoteMember(Note("b")))
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testWithUnion_copy(): Unit = {
    val original = WithUnion(Union.NoteMember(Note("orig")))
    val copied = original.copy(value = Union.MessageMember(Message(Some("msg"), None)))
    assert(copied.value.isInstanceOf[Union.MessageMember])
  }

  // ---------------------------------------------------------------------------
  // TypedDefinition
  // ---------------------------------------------------------------------------

  @Test
  def testTypedDefinition_noteMember(): Unit = {
    val note = Note("typed note")
    val member = TypedDefinition.NoteMember(note)
    assert(member.value.text === "typed note")
  }

  @Test
  def testTypedDefinition_messageMember(): Unit = {
    val msg = Message(Some("t"), None)
    val member = TypedDefinition.MessageMember(msg)
    assert(member.value === msg)
  }

  @Test
  def testTypedDefinition_build_noteMember(): Unit = {
    val note = Note("build test")
    val original = TypedDefinition.NoteMember(note)
    val built = TypedDefinition.build(roundTrip(original.data().asInstanceOf[DataMap]), DataConversion.SetReadOnly)
    assert(built.isInstanceOf[TypedDefinition.NoteMember])
    assert(built.asInstanceOf[TypedDefinition.NoteMember].value.text === "build test")
  }

  @Test
  def testTypedDefinition_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "value")
    dataMap.makeReadOnly()
    val built = TypedDefinition.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[TypedDefinition.$UnknownMember])
  }

  @Test
  def testTypedDefinition_equality(): Unit = {
    val a = TypedDefinition.NoteMember(Note("same"))
    val b = TypedDefinition.NoteMember(Note("same"))
    val c = TypedDefinition.NoteMember(Note("diff"))
    assert(a === b)
    assert(a !== c)
  }

  // ---------------------------------------------------------------------------
  // WithTypedDefinition wrapper
  // ---------------------------------------------------------------------------

  @Test
  def testWithTypedDefinition_construction(): Unit = {
    val note = Note("td note")
    val r = WithTypedDefinition(TypedDefinition.NoteMember(note))
    assert(r.value.isInstanceOf[TypedDefinition.NoteMember])
  }

  @Test
  def testWithTypedDefinition_roundTrip(): Unit = {
    val original = WithTypedDefinition(TypedDefinition.NoteMember(Note("rt")))
    val roundTripped =
      WithTypedDefinition.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithTypedDefinition_equality(): Unit = {
    val a = WithTypedDefinition(TypedDefinition.NoteMember(Note("a")))
    val b = WithTypedDefinition(TypedDefinition.NoteMember(Note("a")))
    assert(a === b)
  }

  // ---------------------------------------------------------------------------
  // FlatTypedDefinition
  // ---------------------------------------------------------------------------

  @Test
  def testFlatTypedDefinition_noteMember(): Unit = {
    val note = Note("flat note")
    val member = FlatTypedDefinition.NoteMember(note)
    assert(member.value.text === "flat note")
  }

  @Test
  def testFlatTypedDefinition_messageMember(): Unit = {
    val msg = Message(Some("flat title"), None)
    val member = FlatTypedDefinition.MessageMember(msg)
    assert(member.value === msg)
  }

  @Test
  def testFlatTypedDefinition_build_noteMember(): Unit = {
    val note = Note("flat build")
    val original = FlatTypedDefinition.NoteMember(note)
    val built = FlatTypedDefinition.build(roundTrip(original.data().asInstanceOf[DataMap]), DataConversion.SetReadOnly)
    assert(built.isInstanceOf[FlatTypedDefinition.NoteMember])
  }

  @Test
  def testFlatTypedDefinition_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "value")
    dataMap.makeReadOnly()
    val built = FlatTypedDefinition.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[FlatTypedDefinition.$UnknownMember])
  }

  @Test
  def testFlatTypedDefinition_equality(): Unit = {
    val a = FlatTypedDefinition.NoteMember(Note("x"))
    val b = FlatTypedDefinition.NoteMember(Note("x"))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // WithFlatTypedDefinition wrapper
  // ---------------------------------------------------------------------------

  @Test
  def testWithFlatTypedDefinition_construction(): Unit = {
    val note = Note("flat def note")
    val r = WithFlatTypedDefinition(FlatTypedDefinition.NoteMember(note))
    assert(r.value.isInstanceOf[FlatTypedDefinition.NoteMember])
  }

  @Test
  def testWithFlatTypedDefinition_roundTrip(): Unit = {
    val original = WithFlatTypedDefinition(FlatTypedDefinition.NoteMember(Note("rt")))
    val roundTripped =
      WithFlatTypedDefinition.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  // ---------------------------------------------------------------------------
  // InlineRecord, InlineRecord2
  // ---------------------------------------------------------------------------

  @Test
  def testInlineRecord_construction(): Unit = {
    val r = InlineRecord(Some(42))
    assert(r.value === Some(42))
  }

  @Test
  def testInlineRecord_empty(): Unit = {
    val r = InlineRecord()
    assert(r.value === None)
  }

  @Test
  def testInlineRecord_roundTrip(): Unit = {
    val original = InlineRecord(Some(10))
    val roundTripped = InlineRecord.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testInlineRecord_equality(): Unit = {
    val a = InlineRecord(Some(1))
    val b = InlineRecord(Some(1))
    val c = InlineRecord(None)
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testInlineRecord_copy(): Unit = {
    val r = InlineRecord(Some(5))
    val copied = r.copy(value = Some(99))
    assert(copied.value === Some(99))
  }

  @Test
  def testInlineRecord2_construction(): Unit = {
    val r = InlineRecord2()
    assert(r.productArity === 0)
  }

  @Test
  def testInlineRecord2_roundTrip(): Unit = {
    val original = InlineRecord2()
    val roundTripped = InlineRecord2.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  // ---------------------------------------------------------------------------
  // UnionWithInlineRecord
  // ---------------------------------------------------------------------------

  @Test
  def testUnionWithInlineRecord_inlineRecordMember(): Unit = {
    val ir = InlineRecord(Some(5))
    val member = UnionWithInlineRecord.InlineRecordMember(ir)
    assert(member.value === ir)
    assert(member.value.value === Some(5))
  }

  @Test
  def testUnionWithInlineRecord_inlineRecord2Member(): Unit = {
    val ir2 = InlineRecord2()
    val member = UnionWithInlineRecord.InlineRecord2Member(ir2)
    assert(member.value === ir2)
  }

  @Test
  def testUnionWithInlineRecord_build_inlineRecord(): Unit = {
    val ir = InlineRecord(Some(7))
    val original = UnionWithInlineRecord.InlineRecordMember(ir)
    val built = UnionWithInlineRecord.build(roundTrip(original.data().asInstanceOf[DataMap]), DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionWithInlineRecord.InlineRecordMember])
    assert(built.asInstanceOf[UnionWithInlineRecord.InlineRecordMember].value.value === Some(7))
  }

  @Test
  def testUnionWithInlineRecord_build_inlineRecord2(): Unit = {
    val ir2 = InlineRecord2()
    val original = UnionWithInlineRecord.InlineRecord2Member(ir2)
    val built = UnionWithInlineRecord.build(roundTrip(original.data().asInstanceOf[DataMap]), DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionWithInlineRecord.InlineRecord2Member])
  }

  @Test
  def testUnionWithInlineRecord_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "value")
    dataMap.makeReadOnly()
    val built = UnionWithInlineRecord.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionWithInlineRecord.$UnknownMember])
  }

  @Test
  def testUnionWithInlineRecord_unapply(): Unit = {
    val ir = InlineRecord(Some(3))
    val member = UnionWithInlineRecord.InlineRecordMember(ir)
    val UnionWithInlineRecord.InlineRecordMember(v) = member
    assert(v.value === Some(3))
  }

  @Test
  def testUnionWithInlineRecord_equality(): Unit = {
    val a = UnionWithInlineRecord.InlineRecordMember(InlineRecord(Some(1)))
    val b = UnionWithInlineRecord.InlineRecordMember(InlineRecord(Some(1)))
    val c = UnionWithInlineRecord.InlineRecordMember(InlineRecord(Some(2)))
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testUnionWithInlineRecord_implicit(): Unit = {
    val ir = InlineRecord(Some(99))
    val u: UnionWithInlineRecord = ir
    assert(u.isInstanceOf[UnionWithInlineRecord.InlineRecordMember])
  }
}
