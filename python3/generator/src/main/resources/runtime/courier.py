# Copyright 2017 Coursera Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# =============================================================================
""" The courier runtime.

This file was created and placed here during schema generation, so don't bother
editing it. If we were doing this right we would distribute it as a package
through `pip`, and that will probably eventually happen, but until then it gets
generated right here. Where it's safe.
"""



import avro.io
import avro.schema
import json
from collections.abc import MutableSequence, MutableMapping

def parse(courier_type, json_str):
    if (isinstance(json_str, bytes)):
        json_str = json_str.decode('utf-8')
    json_obj = json.loads(json_str)
    needs_validation = hasattr(courier_type, 'SCHEMA') and courier_type.SCHEMA is not INVALID_SCHEMA
    if needs_validation:
        __validate_recursive(courier_type.SCHEMA, json_obj)

    constructor = courier_type.from_data if (hasattr(courier_type, 'from_data')) else courier_type
    return constructor(json_obj)

def serialize(courier_object):
    return json.dumps(data_value(courier_object))

def validate(courier_object):
    return __validate_avro(courier_object)

def __validate_avro(courier_object):
    can_validate = hasattr(courier_object, '__class__') and \
                   hasattr(courier_object.__class__, 'AVRO_SCHEMA') and \
                   courier_object.AVRO_SCHEMA is not INVALID_SCHEMA
    if not can_validate:
        return
    else:
        value = data_value(courier_object)
        schema = courier_object.__class__.AVRO_SCHEMA
        if not avro.io.Validate(schema, value):
            raise ValidationError('Validity check failed for %s' % repr(courier_object))

def __validate_courier(courier_object):
    can_validate = hasattr(courier_object, '__class__') and \
                   hasattr(courier_object.__class__, 'SCHEMA') and \
                   courier_object.SCHEMA is not INVALID_SCHEMA
    if not can_validate:
        return
    else:
        value = data_value(courier_object)
        schema = courier_object.__class__.SCHEMA
        __validate_recursive(schema, value)


def __validate_recursive(expected_schema, datum, path = []):
    """Determines if a python datum is an instance of a courier schema.

    Args:
      expected_schema: Schema to validate against.
      datum: Datum to validate.
    Returns:
      Throws a ValidationError if the datum is not an instance of the schema.
      Otherwise returns None.
    """

    def __assert_validation(conditional, expected):
        if not conditional:
            raise ValidationError('Error at path %(path)s: Expected %(expected)s but found %(datum)s' % {
                'path': ' / '.join([repr(path_item) for path_item in path]),
                'expected': expected,
                'datum': datum
            })

    schema_type = expected_schema.type
    if schema_type == 'null':
        __assert_validation(datum is None, expected='null')
    elif schema_type == 'boolean':
        __assert_validation(isinstance(datum, bool), expected='boolean')
    elif schema_type == 'string':
        __assert_validation(isinstance(datum, str), expected='string')
    elif schema_type == 'bytes':
        __assert_validation(isinstance(datum, bytes), expected='bytes')
    elif schema_type in ['int', 'long']:
        __assert_validation(isinstance(datum, int), expected='int or long')
    elif schema_type in ['float', 'double']:
        __assert_validation(isinstance(datum, int) or isinstance(datum, float), expected='int or float')
    elif schema_type == 'fixed':
        __assert_validation(
            isinstance(datum, bytes) and (len(datum) == expected_schema.size),
            expected='bytes of length %s' % expected_schema.size
        )
    elif schema_type == 'enum':
        __assert_validation(datum in expected_schema.symbols, expected='one of %s' % str(expected_schema.symbols))
    elif schema_type == 'array':
        __assert_validation(isinstance(datum, list), expected='a list')
        for i, item in enumerate(datum):
            __validate_recursive(expected_schema.items, item, path + [str(i)])
    elif schema_type == 'map':
        __assert_validation(isinstance(datum, dict), 'an object')
        for name, value in datum.items():
            __assert_validation(isinstance(name, str), expected='all keys to be strings')
            # TODO: map validation is broken. Why? Unlike all other collections
            # and records, the schema for Maps can contain the fullname of some
            # other schema (e.g. org.example.TestRecord) without also inlining
            # the content of that schema. That essentially kills the approach of
            # using the base type's SCHEMA property for validation. Instead, if
            # we want to validate the contents of maps, we will need to somehow
            # keep a registry of schema fullnames to *DataSchema objects, so
            # that we can look them up and perform validation when the fullname
            # appears in the "values" properties of the map declaration.
            #
            # At the time I wrote this code, I thought a middle ground could be
            # to include data schemas of all imported types in each file.
            #
            # The java implementation of this solves the problem by keeping
            # state of all encountered data schemas at generation time, and
            # allowing lookup by name when generating the scala. See
            # restli sources:SchemaParser.java::363, which is invoked int his context
            # by:
            #    SchemaParser.java:494 (dataMapToDataSchema, case MAP)
            #    SchemaParser.java:1043 (getSchemaData, parseObject(obj))
            #    SchemaParser.java:221 (parseObject, stringToDataSchema((String) object))
            #    SchemaParser.java:363 (stringToDataSchema, lookupName)
            #    SchemaParser.java:344 (lookupName, _resolver.findDataSchema(fullName, errorMessageBuilder())
            #
            # Below line is commented out until we replicate above for python:
            # __validate_recursive(expected_schema.values, value, path + [str(name)])
    elif schema_type == 'union':
        __assert_validation(isinstance(datum, dict) and len(datum) == 1, 'an object with one key')
        union_member_key, union_branch_datum = datum.items().__iter__().__next__()
        union_branch_schema = expected_schema.types_by_key.get(union_member_key)
        __assert_validation(union_branch_schema, 'an object with a property in: %s' % repr(expected_schema.type_keys))
        __validate_recursive(union_branch_schema, union_branch_datum, path + [union_member_key])

    elif schema_type == 'record':
        __assert_validation(isinstance(datum, dict), expected='an object')
        for field in expected_schema.fields:
            value = datum.get(field.name)
            if value is None and field.optional:
                pass
            else:
                __validate_recursive(field.type_schema, datum.get(field.name), path + [str(field.name)])

    elif schema_type == 'typeref':
        # TODO: we don't support typeref validation yet. As with maps (see giant
        # document above), this would require being able to look up the
        # referenced data schema at runtime, something we can not currently do.
        #
        # We can fix this by implementing some registry of data schemas sigh...
        pass
    else:
        raise ValidationError('Unknown schema type: %r' % schema_type)

def parse_avro_schema(schema_json):
    try:
        return avro.schema.Parse(schema_json)
    except (avro.schema.SchemaParseException, json.decoder.JSONDecodeError) as e:
        return INVALID_SCHEMA

def parse_schema(schema_json):
    schema_data = json.loads(schema_json)
    return DataSchema.from_data(schema_data)

def data_value(courier_object_or_primitive):
    if (hasattr(courier_object_or_primitive, 'data')):
        # Serialize the 'data' members of enums, records, and unions
        return getattr(courier_object_or_primitive, 'data')
    elif isinstance(courier_object_or_primitive, list):
        return [data_value(item) for item in courier_object_or_primitive]
    elif isinstance(courier_object_or_primitive, dict):
        return {k: data_value(v) for k, v in courier_object_or_primitive.items()}
    else:
        # Serialize ints and strs directly
        return courier_object_or_primitive

def construct_object(value, constructor):
    if hasattr(constructor, 'from_self_or_value'):
        return constructor.from_self_or_value(value)
    else:
        return value

def array(courier_type):
    return lambda items: Array(courier_type, items)

def map(courier_type):
    return lambda items: Map(courier_type, items)

class ValidationError(Exception):
    def __init__(self, message):
        super(ValidationError, self).__init__(message)

class Record:
    def __init__(self, data=None):
        self.data = data if data is not None else {}

    def _set_data_field(self, data_key, new_value, field_type_constructor, validate_new_value=True):
        old_data_value = UNINITIALIZED
        if data_key in self.data:
            old_data_value = self.data[data_key]

        if new_value in [None, OPTIONAL]:
            if data_key in self.data:
                del self.data[data_key]
        else:
            courier_obj = construct_object(new_value, field_type_constructor)
            self.data[data_key] = data_value(courier_obj)

        try:
            validate_new_value and validate(self)
        except ValidationError:
            if old_data_value is not UNINITIALIZED:
                self.data[data_key] = old_data_value
            raise ValidationError('%s is not a valid value for %s.%s' % (new_value, self.__class__.__name__, data_key))

    def _get_data_field(self, data_key, type_constructor):
        field_data = self.data.get(data_key)
        if field_data is not None:
            # TODO(py3) unify all of these `from_data` calls
            constructor = type_constructor.from_data if (hasattr(type_constructor, 'from_data')) else type_constructor
            courier_obj = constructor(field_data)
            if hasattr(courier_obj, 'as_value_type'):
                return courier_obj.as_value_type
            else:
                return courier_obj

class Union:
    def __init__(self, data=None):
        self.data = data if data is not None else {}

    def _set_union(self, data_key, new_value):
        old_data = self.data

    @classmethod
    def from_self_or_value(cls, self_or_value):
        return self_or_value if isinstance(self_or_value, cls) else cls(value=self_or_value)

    def _set_from_value(self, new_value):
        old_data = self.data
        new_data_value = data_value(new_value)
        new_member_key = None

        for (member_key, type_info) in self.__class__._TYPES_BY_KEY.items():
            type = type_info['type']
            if member_key == 'array' and isinstance(new_value, MutableSequence):
                new_member_key = member_key
            elif member_key == 'map' and isinstance(new_value, MutableMapping):
                new_member_key = member_key
            elif isinstance(new_value, type):
                new_member_key = member_key
        if new_member_key is None:
            # TODO(py3): better error here
            raise ValueError('Unacceptable value "%s" for union schema %s' % (repr(new_value), str(self.__class__.AVRO_SCHEMA)))

        # Edit data mutably because it may belong to a larger data tree of
        # some other object
        self.data.clear()
        self.data[new_member_key] = new_data_value

class Array(MutableSequence):
    def __init__(self, courier_index_type, data=None):
        self.data = [] if data is None else data
        self._item_constructor = courier_index_type.from_data if hasattr(courier_index_type, 'from_data') else courier_index_type

    #
    # MutableSequence abstract method implementations
    #
    def __len__(self):
        return self.data.__len__()

    def __getitem__(self, key):
        """Returns either an item or a slice of the Array. For details, see:
        https://docs.python.org/3/reference/datamodel.html#object.__getitem__
        Arguments:
        key -- either an integer (index to the array), or a slice object
        """
        item_or_slice = self.data.__getitem__(key)
        if isinstance(key, slice):
            # The key was a slice. Return a new Courier Array.
            return Array(self._item_constructor, data=item_or_slice)

        # The key was an integer. Return an item of the underlying type.
        return self._construct_item(item_or_slice)

    def __setitem__(self, key, item):
        courier_obj = construct_object(item, self._item_constructor)
        return self.data.__setitem__(key, data_value(courier_obj))

    def __delitem__(self, key):
        return self.data.__delitem__(key)

    def insert(self, idx, item):
        return self.data.insert(idx, data_value(item))

    #
    # Built-in implementations
    #
    def __repr__(self):
        return 'courier.Array(' + repr(self.data) + ')'

    def __eq__(self, other):
        return isinstance(other, MutableSequence) and data_value(self) == data_value(other)

    #
    # Private implementations
    #
    def _construct_item(self, item):
        item = self._item_constructor(item)
        return item if not hasattr(item, 'as_value_type') else item.as_value_type

class Map(MutableMapping):
    def __init__(self, courier_index_type, data=None):
        self.data = {} if data is None else data
        self._item_constructor = courier_index_type.from_data if hasattr(courier_index_type, 'from_data') else courier_index_type

    def items(self):
        for key in self.data:
            yield (key, self._construct_item(self.data[key]))

    #
    # MutableMapping abstract method implementations
    #
    def __iter__(self):
        return self.data.__iter__()

    def __len__(self):
        return self.data.__len__()

    def __getitem__(self, key):
        return self._construct_item(self.data.__getitem__(key))

    def __setitem__(self, key, item):
        courier_obj = construct_object(item, self._item_constructor)
        return self.data.__setitem__(key, data_value(courier_obj))

    def __delitem__(self, key):
        return self.data.__delitem__(key)

    #
    # Built-in implementations
    #
    def __repr__(self):
        return 'courier.Map(' + repr(self.data) + ')'

    def __eq__(self, other):
        return isinstance(other, MutableMapping) and data_value(self) == data_value(other)

    #
    # Private implementations
    #
    def _construct_item(self, item):
        item = self._item_constructor(item)
        return item if not hasattr(item, 'as_value_type') else item.as_value_type

REQUIRED = "__COURIER_REQUIRED__"
OPTIONAL = "__COURIER_OPTIONAL__"
UNINITIALIZED = "__COURIER_UNINITIALIZED__"
INVALID_SCHEMA = "__COURIER_INVALID_SCHEMA__"

class DataSchema:
    def __init__(self, data, schema_type=None, enclosing_schemas=[]):
        self._data = data
        self._enclosing_schemas = enclosing_schemas
        cls = self.__class__
        self.type = (hasattr(cls, 'TYPE') and cls.TYPE) or schema_type
        if not self.type:
            raise ValidationError('Schema must have a type, but none was provided on class %s, nor in the constructor' % cls.__name__)

        if isinstance(data, dict):
            self.namespace = self._derive_namespace()
            self.name = data.get('name')
            self.full_name = self.namespace + '.' + self.name if self.namespace else self.name

        self.union_member_key = getattr(self, 'full_name', None) or self.type

    def __repr__(self):
        return str(self.__class__.__name__) + '(' + str(self._data) + ')'

    def _derive_namespace(self):
        """
        Encode namespace behavior from https://avro.apache.org/docs/1.8.1/spec.html. Quoted here:
        In record, enum and fixed definitions, the fullname is determined in one of the following ways:

          * A name and namespace are both specified. For example, one might use
           "name": "X", "namespace": "org.foo" to indicate the fullname
           org.foo.X.
          
          * A fullname is specified. If the name specified contains a dot, then
            it is assumed to be a fullname, and any namespace also specified is
            ignored. For example, use "name": "org.foo.X" to indicate the
            fullname org.foo.X.
          
          * A name only is specified, i.e., a name that contains no dots. In
            this case the namespace is taken from the most tightly enclosing
            schema or protocol. For example, if "name": "X" is specified, and
            this occurs within a field of the record definition of org.foo.Y,
            then the fullname is org.foo.X. If there is no enclosing namespace
            then the null namespace is used.
            
            References to previously defined names are as in the latter two cases above: if they contain a dot they are a fullname, if they do not contain a dot, the namespace is the namespace of the enclosing definition.

            Primitive type names have no namespace and their names may not be defined in any namespace.
        """
        data = self._data
        if isinstance(data, dict) and self.type in ['record', 'enum', 'fixed']:
            ns_from_data = data.get('namespace')
            enclosing_schema_nses = [enclosing_schema.namespace for enclosing_schema in self._enclosing_schemas if hasattr(enclosing_schema, 'namespace')]
            ns_from_enclosing_schemas = len(enclosing_schema_nses) > 0 and enclosing_schema_nses[0]
            return ns_from_data or ns_from_enclosing_schemas

    @staticmethod
    def from_data(data, enclosing_schemas=[]):
        schema_type = 'union' if isinstance(data, list) \
            else data.get('type') if isinstance(data, dict) \
            else data
        schema_class = SCHEMA_CLASSES_BY_TYPE.get(schema_type)

        return schema_class(data, enclosing_schemas=enclosing_schemas) if schema_class \
            else DataSchema(data, schema_type=schema_type, enclosing_schemas=enclosing_schemas)

class RecordDataSchema(DataSchema):
    TYPE = 'record'
    def __init__(self, data, enclosing_schemas=[]):
        super(self.__class__, self).__init__(data, enclosing_schemas=enclosing_schemas)
        self.fields = [RecordDataSchema.Field(field_schema, [self] + enclosing_schemas) for field_schema in data['fields']]

    class Field:
        def __init__(self, data, enclosing_schemas=[]):
            self._data = data
            self.name = data['name']
            self.optional = data.get('optional', False)
            self.type_schema = DataSchema.from_data(data['type'], enclosing_schemas=enclosing_schemas)

class UnionDataSchema(DataSchema):
    TYPE = 'union'
    def __init__(self, data, enclosing_schemas=[]):
        super(self.__class__, self).__init__(data, enclosing_schemas=enclosing_schemas)
        self.types = [DataSchema.from_data(type_schema, enclosing_schemas=[self] + self._enclosing_schemas) for type_schema in data]
        self.types_by_key = dict([(type_schema.union_member_key, type_schema) for type_schema in self.types])
        self.type_keys = [type_schema.union_member_key for type_schema in self.types]

class EnumDataSchema(DataSchema):
    TYPE = 'enum'
    def __init__(self, data, enclosing_schemas=[]):
        super(self.__class__, self).__init__(data,  enclosing_schemas=enclosing_schemas)
        self.type = 'enum'

        # An array of valid enum strings
        self.symbols = data['symbols']

class TyperefDataSchema(DataSchema):
    TYPE = 'typeref'
    def __init__(self, data, enclosing_schemas=[]):
        super(self.__class__, self).__init__(data,  enclosing_schemas=enclosing_schemas)

class ArrayDataSchema(DataSchema):
    TYPE = 'array'
    def __init__(self, data, enclosing_schemas=[]):
        super(self.__class__, self).__init__(data,  enclosing_schemas=enclosing_schemas)
        self.items = DataSchema.from_data(data['items'])

class MapDataSchema(DataSchema):
    TYPE = 'map'

    def __init__(self, data, enclosing_schemas=[]):
        super(self.__class__, self).__init__(data,  enclosing_schemas=enclosing_schemas)
        self.values = DataSchema.from_data(data['values'])

SCHEMA_CLASSES = [
    RecordDataSchema,
    UnionDataSchema,
    EnumDataSchema,
    TyperefDataSchema,
    ArrayDataSchema,
    MapDataSchema,
]

SCHEMA_CLASSES_BY_TYPE = dict([(cls.TYPE, cls) for cls in SCHEMA_CLASSES])
