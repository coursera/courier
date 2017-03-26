import py3bindings.courier as courier
from py3bindings.org.coursera.arrays.WithAnonymousUnionArray import WithAnonymousUnionArray
from py3bindings.org.coursera.arrays.WithCustomArrayTestId import WithCustomArrayTestId
from py3bindings.org.coursera.arrays.WithCustomTypesArray import WithCustomTypesArray
from py3bindings.org.coursera.arrays.WithCustomTypesArrayUnion import WithCustomTypesArrayUnion
from py3bindings.org.coursera.arrays.WithPrimitivesArray import WithPrimitivesArray
from py3bindings.org.coursera.arrays.WithRecordArray import WithRecordArray
from py3bindings.org.coursera.customtypes.BooleanId import BooleanId
from py3bindings.org.coursera.customtypes.BoxedIntId import BoxedIntId
from py3bindings.org.coursera.customtypes.ByteId import ByteId
from py3bindings.org.coursera.customtypes.CaseClassCustomIntWrapper import CaseClassCustomIntWrapper
from py3bindings.org.coursera.customtypes.CaseClassStringIdWrapper import CaseClassStringIdWrapper
from py3bindings.org.coursera.customtypes.CharId import CharId
from py3bindings.org.coursera.customtypes.CustomArrayTestId import CustomArrayTestId
from py3bindings.org.coursera.customtypes.CustomInt import CustomInt
from py3bindings.org.coursera.customtypes.CustomIntWrapper import CustomIntWrapper
from py3bindings.org.coursera.customtypes.CustomMapTestKeyId import CustomMapTestKeyId
from py3bindings.org.coursera.customtypes.CustomMapTestValueId import CustomMapTestValueId
from py3bindings.org.coursera.customtypes.CustomRecord import CustomRecord
from py3bindings.org.coursera.customtypes.CustomRecordTestId import CustomRecordTestId
from py3bindings.org.coursera.customtypes.CustomUnionTestId import CustomUnionTestId
from py3bindings.org.coursera.customtypes.DateTime import DateTime
from py3bindings.org.coursera.customtypes.DoubleId import DoubleId
from py3bindings.org.coursera.customtypes.FloatId import FloatId
from py3bindings.org.coursera.customtypes.IntId import IntId
from py3bindings.org.coursera.customtypes.LongId import LongId
from py3bindings.org.coursera.customtypes.ShortId import ShortId
from py3bindings.org.coursera.customtypes.StringId import StringId
from py3bindings.org.coursera.deprecated.DeprecatedRecord import DeprecatedRecord
from py3bindings.org.coursera.enums.EmptyEnum import EmptyEnum
from py3bindings.org.coursera.enums.EnumProperties import EnumProperties
from py3bindings.org.coursera.enums.Fruits import Fruits
from py3bindings.org.coursera.escaping.class_ import class_
from py3bindings.org.coursera.escaping.DefaultLiteralEscaping import DefaultLiteralEscaping
from py3bindings.org.coursera.escaping.KeywordEscaping import KeywordEscaping
from py3bindings.org.coursera.escaping.ReservedClassFieldEscaping import ReservedClassFieldEscaping
from py3bindings.org.coursera.fixed.Fixed8 import Fixed8
from py3bindings.org.coursera.fixed.WithFixed8 import WithFixed8
from py3bindings.org.coursera.maps.Toggle import Toggle
from py3bindings.org.coursera.maps.WithComplexTypesMap import WithComplexTypesMap
from py3bindings.org.coursera.maps.WithComplexTypesMapUnion import WithComplexTypesMapUnion
from py3bindings.org.coursera.maps.WithCustomMapTestIds import WithCustomMapTestIds
from py3bindings.org.coursera.maps.WithCustomTypesMap import WithCustomTypesMap
from py3bindings.org.coursera.maps.WithPrimitivesMap import WithPrimitivesMap
from py3bindings.org.coursera.maps.WithTypedKeyMap import WithTypedKeyMap
from py3bindings.org.coursera.records.class_ import class_
from py3bindings.org.coursera.records.CourierFile import CourierFile
from py3bindings.org.coursera.records.JsonTest import JsonTest
from py3bindings.org.coursera.records.Message import Message
from py3bindings.org.coursera.records.Note import Note
from py3bindings.org.coursera.records.primitivestyle.Simple import Simple
from py3bindings.org.coursera.records.primitivestyle.WithComplexTypes import WithComplexTypes
from py3bindings.org.coursera.records.primitivestyle.WithPrimitives import WithPrimitives
from py3bindings.org.coursera.records.test.BooleanTyperef import BooleanTyperef
from py3bindings.org.coursera.records.test.BytesTyperef import BytesTyperef
from py3bindings.org.coursera.records.test.DoubleTyperef import DoubleTyperef
from py3bindings.org.coursera.records.test.Empty import Empty
import py3bindings.org.coursera.records.test.Empty
from py3bindings.org.coursera.records.test.Empty2 import Empty2
from py3bindings.org.coursera.records.test.FloatTyperef import FloatTyperef
from py3bindings.org.coursera.records.test.InlineOptionalRecord import InlineOptionalRecord
from py3bindings.org.coursera.records.test.InlineRecord import InlineRecord
from py3bindings.org.coursera.records.test.IntCustomType import IntCustomType
from py3bindings.org.coursera.records.test.IntTyperef import IntTyperef
from py3bindings.org.coursera.records.test.LongTyperef import LongTyperef
from py3bindings.org.coursera.records.test.Message import Message
from py3bindings.org.coursera.records.test.NumericDefaults import NumericDefaults
from py3bindings.org.coursera.records.test.OptionalBooleanTyperef import OptionalBooleanTyperef
from py3bindings.org.coursera.records.test.OptionalBytesTyperef import OptionalBytesTyperef
from py3bindings.org.coursera.records.test.OptionalDoubleTyperef import OptionalDoubleTyperef
from py3bindings.org.coursera.records.test.OptionalFloatTyperef import OptionalFloatTyperef
from py3bindings.org.coursera.records.test.OptionalIntCustomType import OptionalIntCustomType
from py3bindings.org.coursera.records.test.OptionalIntTyperef import OptionalIntTyperef
from py3bindings.org.coursera.records.test.OptionalLongTyperef import OptionalLongTyperef
from py3bindings.org.coursera.records.test.OptionalStringTyperef import OptionalStringTyperef
from py3bindings.org.coursera.records.test.packaging.Empty import Empty
# TODO(py3): Fix this from py3bindings.org.coursera.records.test.RecursivelyDefinedRecord import RecursivelyDefinedRecord
from py3bindings.org.coursera.records.test.Simple import Simple
from py3bindings.org.coursera.records.test.StringTyperef import StringTyperef
from py3bindings.org.coursera.records.test.With22Fields import With22Fields
from py3bindings.org.coursera.records.test.With23Fields import With23Fields
from py3bindings.org.coursera.records.test.WithCaseClassCustomType import WithCaseClassCustomType
from py3bindings.org.coursera.records.test.WithComplexTypeDefaults import WithComplexTypeDefaults
from py3bindings.org.coursera.records.test.WithComplexTyperefs import WithComplexTyperefs
from py3bindings.org.coursera.records.test.WithComplexTypes import WithComplexTypes
from py3bindings.org.coursera.records.test.WithCourierFile import WithCourierFile
from py3bindings.org.coursera.records.test.WithCustomIntWrapper import WithCustomIntWrapper
from py3bindings.org.coursera.records.test.WithCustomRecord import WithCustomRecord
from py3bindings.org.coursera.records.test.WithCustomRecordTestId import WithCustomRecordTestId
from py3bindings.org.coursera.records.test.WithDateTime import WithDateTime
from py3bindings.org.coursera.records.test.WithInclude import WithInclude
from py3bindings.org.coursera.records.test.WithInlineRecord import WithInlineRecord
from py3bindings.org.coursera.records.test.WithOmitField import WithOmitField
from py3bindings.org.coursera.records.test.WithOptionalComplexTypeDefaults import WithOptionalComplexTypeDefaults
from py3bindings.org.coursera.records.test.WithOptionalComplexTypes import WithOptionalComplexTypes
from py3bindings.org.coursera.records.test.WithOptionalComplexTypesDefaultNone import WithOptionalComplexTypesDefaultNone
from py3bindings.org.coursera.records.test.WithOptionalPrimitiveCustomTypes import WithOptionalPrimitiveCustomTypes
from py3bindings.org.coursera.records.test.WithOptionalPrimitiveDefaultNone import WithOptionalPrimitiveDefaultNone
from py3bindings.org.coursera.records.test.WithOptionalPrimitiveDefaults import WithOptionalPrimitiveDefaults
from py3bindings.org.coursera.records.test.WithOptionalPrimitives import WithOptionalPrimitives
from py3bindings.org.coursera.records.test.WithOptionalPrimitiveTyperefs import WithOptionalPrimitiveTyperefs
from py3bindings.org.coursera.records.test.WithPrimitiveCustomTypes import WithPrimitiveCustomTypes
from py3bindings.org.coursera.records.test.WithPrimitiveDefaults import WithPrimitiveDefaults
from py3bindings.org.coursera.records.test.WithPrimitives import WithPrimitives
from py3bindings.org.coursera.records.test.WithPrimitiveTyperefs import WithPrimitiveTyperefs
from py3bindings.org.coursera.records.test.WithUnionWithInlineRecord import WithUnionWithInlineRecord
from py3bindings.org.coursera.records.WithDateTime import WithDateTime
from py3bindings.org.coursera.records.WithFlatTypedDefinition import WithFlatTypedDefinition
from py3bindings.org.coursera.records.WithInclude import WithInclude
from py3bindings.org.coursera.records.WithTypedDefinition import WithTypedDefinition
from py3bindings.org.coursera.records.WithUnion import WithUnion
from py3bindings.org.coursera.typerefs.ArrayTyperef import ArrayTyperef
from py3bindings.org.coursera.typerefs.EnumTyperef import EnumTyperef
from py3bindings.org.coursera.typerefs.FlatTypedDefinition import FlatTypedDefinition
from py3bindings.org.coursera.typerefs.InlineRecord import InlineRecord
from py3bindings.org.coursera.typerefs.InlineRecord2 import InlineRecord2
from py3bindings.org.coursera.typerefs.IntTyperef import IntTyperef
from py3bindings.org.coursera.typerefs.MapTyperef import MapTyperef
from py3bindings.org.coursera.typerefs.RecordTyperef import RecordTyperef
from py3bindings.org.coursera.typerefs.TypedDefinition import TypedDefinition
from py3bindings.org.coursera.typerefs.Union import Union
from py3bindings.org.coursera.typerefs.UnionTyperef import UnionTyperef
from py3bindings.org.coursera.typerefs.UnionWithInlineRecord import UnionWithInlineRecord
from py3bindings.org.coursera.unions.IntCustomType import IntCustomType
from py3bindings.org.coursera.unions.IntTyperef import IntTyperef
from py3bindings.org.coursera.unions.WithComplexTypesUnion import WithComplexTypesUnion
from py3bindings.org.coursera.unions.WithCustomUnionTestId import WithCustomUnionTestId
# TODO(py3): Do we have to support this edge case? from py3bindings.org.coursera.unions.WithEmptyUnion import WithEmptyUnion
from py3bindings.org.coursera.unions.WithPrimitiveCustomTypesUnion import WithPrimitiveCustomTypesUnion
from py3bindings.org.coursera.unions.WithPrimitivesUnion import WithPrimitivesUnion
from py3bindings.org.coursera.unions.WithPrimitiveTyperefsUnion import WithPrimitiveTyperefsUnion
from py3bindings.org.coursera.unions.WithRecordCustomTypeUnion import WithRecordCustomTypeUnion
from py3bindings.org.example.common.DateTime import DateTime
from py3bindings.org.example.common.Timestamp import Timestamp
from py3bindings.org.example.Fortune import Fortune
from py3bindings.org.example.FortuneCookie import FortuneCookie
from py3bindings.org.example.FortuneTelling import FortuneTelling
from py3bindings.org.example.MagicEightBall import MagicEightBall
from py3bindings.org.example.MagicEightBallAnswer import MagicEightBallAnswer
from py3bindings.org.example.other.DateTime import DateTime
from py3bindings.org.example.record import record
from py3bindings.org.example.TyperefExample import TyperefExample
from py3bindings.WithoutNamespace import WithoutNamespace

import unittest


class TestCourierBindings(unittest.TestCase):
    def test_enum_find_by_name(self):
        self.assertEqual(MagicEightBallAnswer.find_by_name('IT_IS_CERTAIN'), MagicEightBallAnswer.IT_IS_CERTAIN)
        self.assertRaises(KeyError, lambda: MagicEightBallAnswer.find_by_name('Does not exist'))

    def test_enum_all(self):
        self.assertEqual(len(MagicEightBallAnswer.ALL), 3)
        self.assertTrue(MagicEightBallAnswer.IT_IS_CERTAIN in MagicEightBallAnswer.ALL)
        self.assertTrue(MagicEightBallAnswer.ASK_AGAIN_LATER in MagicEightBallAnswer.ALL)
        self.assertTrue(MagicEightBallAnswer.OUTLOOK_NOT_SO_GOOD in MagicEightBallAnswer.ALL)

    def test_enum_from_string(self):
        good_answer = courier.parse(MagicEightBallAnswer, '"IT_IS_CERTAIN"')
        self.assertEqual(good_answer, MagicEightBallAnswer.IT_IS_CERTAIN)
        self.assertRaises(courier.ValidationError, lambda: courier.parse(MagicEightBallAnswer, '"WELL_THIS_DOESNT_EXIST"'))

    def test_enum_empty(self):
        self.assertEqual(len(EmptyEnum.ALL), 0)
        self.assertRaises(KeyError, lambda: EmptyEnum.find_by_name('ANYTHING'))

    def test_enum_properties(self):
        """ TODO(py3) Not yet implemented in python bindings. This is stuff like:

            enum EnumProperties {
              @color = "red"
              APPLE
            }

        """
        pass

    def test_record_json_dumps_and_loads(self):
        question = 'Will I ever love again?'
        answer = MagicEightBallAnswer.IT_IS_CERTAIN
        eight_ball = MagicEightBall(question=question, answer=answer)
        expected_json = """{"question": "Will I ever love again?", "answer": "IT_IS_CERTAIN"}"""
        serialized_json = courier.serialize(eight_ball)
        reloaded_eight_ball = courier.parse(MagicEightBall, serialized_json)
        self.assertEqual(eight_ball.question, question)
        self.assertEqual(eight_ball.answer, answer)
        self.assertEqual(courier.serialize(eight_ball), expected_json)
        self.assertEqual(reloaded_eight_ball, eight_ball)

    def test_record_set_and_get(self):
        question = 'Will I ever love again?'
        answer = MagicEightBallAnswer.IT_IS_CERTAIN
        eight_ball = MagicEightBall(question=question, answer=answer)
        eight_ball.question = "??"
        eight_ball.answer = MagicEightBallAnswer.ASK_AGAIN_LATER
        self.assertEqual(eight_ball.question, "??")
        self.assertEqual(eight_ball.answer, MagicEightBallAnswer.ASK_AGAIN_LATER)
        self.assertEqual(courier.serialize(eight_ball), """{"question": "??", "answer": "ASK_AGAIN_LATER"}""")

    def test_record_validate_shallow(self):
        eight_ball = self.__make_eight_ball()
        courier.validate(eight_ball) # should be valid immediately after creation

        def invalidate_eight_ball():
            eight_ball.question = 1234

        def invalid_8ball_from_data():
            return MagicEightBall.from_data({
              'question': '??',
              'answer': 'Well this isnt a valid enum'
            })

        def invalid_8ball_from_construction():
            return MagicEightBall(
                question="Will I ever love again?"
                # missing answer
            )

        self.assertRaises(courier.ValidationError, invalidate_eight_ball)
        courier.validate(eight_ball) # the invalid changes should not have stuck
        self.assertRaises(courier.ValidationError, invalid_8ball_from_data)
        self.assertRaises(TypeError, invalid_8ball_from_construction)

    def test_record_validate_deep(self):
        pass

    def test_record_with_typeref_field(self):
        pass

    def test_record_with_union_field(self):
        pass

    def test_record_with_list_field(self):
        pass

    def test_with_typed_definition(self):
        """ TODO(py3): see reference suite WithFlatTypedDefinition.courier """
        pass

    def test_with_flat_typed_definition(self):
        """ TODO(py3): see reference suite WithFlatTypedDefinition.courier """
        pass

    def test_record_without_namespace(self):
        record = WithoutNamespace(field1="herp")
        self.assertEqual(courier.parse(WithoutNamespace, courier.serialize(record)), record)

    def test_record_with_reserved_name(self):
        record = class_(private="herp")
        self.assertEqual(courier.serialize(record), """{"private": "herp"}""")

    def test_with_include(self):
        """" TODO(py3): see reference suite WithInclude.courier """
        pass

    def test_json_property(self):
        """" TODO(py3): Implement support for @json property.

        But what does it do? It's supposed to work on a record like this:
            @json = {
              "negativeNumber": -3000000000
            }
            record JsonTest {
            }

        """
        pass

    def test_omit(self):
        """" TODO(py3): Implement support for @py3.omit property

        For those types that we don't support generation for yet. Looks like:

            @py3.omit
            record Message {
              title: string?
              body: string?a
            }
        """
        pass

    def test_union(self):
        union = WithComplexTypesUnion.Union(
          value=py3bindings.org.coursera.records.test.Empty.Empty()
        )

        self.assertEqual(union.value, py3bindings.org.coursera.records.test.Empty.Empty())

    def test_union_as_record_field(self):
        record = WithComplexTypesUnion(union=Fruits.APPLE)
        def assert_json_equal(json):
            self.assertEqual(courier.serialize(record), json)
        assert_json_equal("""{"union": {"org.coursera.enums.Fruits": "APPLE"}}""")

        record.union = Fruits.ORANGE
        assert_json_equal("""{"union": {"org.coursera.enums.Fruits": "ORANGE"}}""")

        record.union = py3bindings.org.coursera.records.test.Empty.Empty()
        assert_json_equal("""{"union": {"org.coursera.records.test.Empty": {}}}""")


        def bad_type_should_raise_value_error():
            record.union = 1

        self.assertRaises(ValueError, bad_type_should_raise_value_error)

        # After raising, the union should still be in its last good state
        self.assertEqual(record.union, py3bindings.org.coursera.records.test.Empty.Empty())

        # I should be able to set the union explicitly
        union = WithComplexTypesUnion.Union(Fruits.APPLE)
        record.union = union
        self.assertEqual(record.union, Fruits.APPLE)


    def test_array_primitive(self):
        ints = [1,2,3,4]
        longs = [5,6,7,8]
        floats = [9.0,10.0,11.0,12.0]
        doubles = [13.0,14.0,15.0,16.0]
        booleans = [True, False]
        strings = ["seventeen", "eighteen"]

        rec = WithPrimitivesArray(
            ints = ints,
            longs = longs,
            floats = floats,
            doubles = doubles,
            booleans = booleans,
            strings = strings,
            bytes = []
        )
        courier.validate(rec) # should not throw
        expected_str = """{"ints": [1, 2, 3, 4], "longs": [5, 6, 7, 8], "floats": [9.0, 10.0, 11.0, 12.0], "doubles": [13.0, 14.0, 15.0, 16.0], "booleans": [true, false], "strings": ["seventeen", "eighteen"], "bytes": []}"""

        self.assertEqual(courier.serialize(rec), expected_str)
        self.assertEqual(courier.parse(WithPrimitivesArray, expected_str), rec)

        rec.ints = [100, 101]
        self.assertTrue('"ints": [100, 101]' in courier.serialize(rec))

    def test_array_and_map_complex(self):
        custom_ints = [1, 2]
        hello = Simple(message="Hello")
        world = Simple(message="world")
        bonjour = Simple(message="Bonjour")
        tout_le_monde = Simple(message="tout le monde")
        arrays = [[hello, world], [bonjour, tout_le_monde]]
        maps = [
          {'greeting': hello },
          {'greeting': bonjour }
        ]
        unions = []
        fixed = []
        rec = WithCustomTypesArray(
            ints = custom_ints,
            arrays = arrays,
            maps = maps,
            unions = unions,
            fixed = fixed
        )
        courier.validate(rec) # should not throw
        json = """{"ints": [1, 2], "arrays": [[{"message": "Hello"}, {"message": "world"}], [{"message": "Bonjour"}, {"message": "tout le monde"}]], "maps": [{"greeting": {"message": "Hello"}}, {"greeting": {"message": "Bonjour"}}], "unions": [], "fixed": []}"""
        self.assertEqual(courier.parse(WithCustomTypesArray, json), rec)

        # Exercise the courier.Arrays
        self.assertEqual(len(rec.arrays), 2)
        self.assertEqual(rec.arrays[0][0], hello)
        self.assertEqual(rec.arrays[0][1], world)
        self.assertEqual(rec.arrays[1][0], bonjour)
        self.assertEqual(rec.arrays[1][1], tout_le_monde)
        for item in rec.arrays[0]:
            self.assertTrue(item in [hello, world])
        rec.arrays[0] = rec.arrays[1]
        self.assertEqual(rec.arrays[0][0], bonjour)
        self.assertEqual(2, len(rec.arrays))
        rec.arrays.extend([rec.arrays[0], rec.arrays[0]])
        self.assertEqual(4, len(rec.arrays))

        # Exercise the courier.Maps
        self.assertEqual(len(rec.maps), 2)
        self.assertEqual(rec.maps[0]['greeting'], hello)
        self.assertEqual(rec.maps[1]['greeting'], bonjour)
        french_map = rec.maps[1]
        self.assertRaises(KeyError, lambda: french_map['world'])
        french_map['world'] = tout_le_monde
        self.assertEqual(french_map['world'], tout_le_monde)
        courier.serialize(rec)


    def __make_eight_ball(self):
        return MagicEightBall('Will I ever love again?', MagicEightBallAnswer.IT_IS_CERTAIN)

if __name__ == '__main__':
    unittest.main()
