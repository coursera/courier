"""
 Copyright 2016 Coursera Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
"""

import avro.io
import avro.schema
import json
from collections.abc import MutableSequence, MutableMapping

def loads(courier_type, json_str):
    json_obj = json.loads(json_str)
    needs_validation = hasattr(courier_type, 'AVRO_SCHEMA')
    if (not needs_validation or avro.io.Validate(avro.schema.Parse(courier_type.AVRO_SCHEMA), json_obj)):
        constructor = courier_type.from_data if (hasattr(courier_type, 'from_data')) else courier_type;
        return constructor(json_obj)
    else:
        raise ValidationError("Invalid json string while reading a '%s' type: %s" % (courier_type, json_str))

def dumps(courier_object):
    return json.dumps(data_value(courier_object))

def validate(courier_object):
    can_validate = hasattr(courier_object, '__class__') and \
        hasattr(courier_object.__class__, 'AVRO_SCHEMA')
    if not can_validate:
        return
    else:
        value = data_value(courier_object)
        schema = avro.schema.Parse(courier_object.__class__.AVRO_SCHEMA)
        if not avro.io.Validate(schema, value):
            raise ValidationError('Validity check failed for %s' % repr(courier_object))

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

def array(courier_type):
    return lambda items: Array(courier_type, items)

def map(courier_type):
    return lambda items: Map(courier_type, items)

class ValidationError(Exception):
    def __init__(self, message):
        super(ValidationError, self).__init__(message)

class Array(MutableSequence):
    def __init__(self, courier_index_type, data = []):
        self.data = data
        self._construct_item = courier_index_type.from_data if hasattr(courier_index_type, 'from_data') else courier_index_type

    #
    # MutableSequence abstract method implementations
    #
    def __len__(self):
        return self.data.__len__()

    def __getitem__(self, key):
        return self._construct_item(self.data.__getitem__(key))

    def __setitem__(self, key, item):
        return self.data.__setitem__(key, data_value(item))

    def __delitem__(self, key):
        return self.data.__delitem__(key)

    def insert(self, idx, item):
        return self.data.insert(idx, data_value(item))

    #
    # Built-in implementations
    #
    def __repr__(self):
        return 'courier.Array(' + repr(self.data) + ')'

class Map (MutableMapping):
    def __init__(self, courier_index_type, data = {}):
        self.data = data
        self._construct_item = courier_index_type.from_data if hasattr(courier_index_type, 'from_data') else courier_index_type

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

    def __setitem__(self, key, value):
        return self.data.__setitem__(key, data_value(value))

    def __delitem__(self, key):
        return self.data.__delitem__(key)

    #
    # Built-in implementations
    #
    def __repr__(self):
        return 'courier.Map(' + repr(self.data) + ')'
