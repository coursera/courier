/*
 * Copyright 2019 Coursera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.coursera.courier;

import com.linkedin.data.schema.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.linkedin.data.schema.DataSchemaConstants.*;

public class SchemaToPdscEncoder extends SchemaToJsonEncoder {

    public static String schemaToPdsc(DataSchema schema) {
        return schemaToPdsc(schema, JsonBuilder.Pretty.INDENTED);
    }

    /**
     * Replicates structure of SchemaToPdscEncoder::schemaToJson method, but with
     * a SchemaToPdscEncoder in place of the SchemaToJsonEncoder.
     *
     * @param schema is the {@link DataSchema} to build a JSON encoded output for.
     * @param pretty is the pretty printing mode.
     * @return the JSON encoded string representing the {@link DataSchema} in pdsc format.
     */
    public static String schemaToPdsc(DataSchema schema, JsonBuilder.Pretty pretty)
    {
        JsonBuilder builder = null;
        try
        {
            builder = new JsonBuilder(pretty);
            final SchemaToJsonEncoder encoder = new SchemaToPdscEncoder(builder);
            encoder.encode(schema);
            return  builder.result();
        }
        catch (IOException exc)
        {
            return exc.getMessage();
        }
        finally
        {
            if (builder != null)
            {
                builder.closeQuietly();
            }
        }
    }


    private SchemaToPdscEncoder(JsonBuilder builder)
    {
        super(builder);
    }

    /**
     * Encode a field's type as a string containing the canonical type name.
     *
     * Overrides the superclass behavior, which includes the full type schema, rather than its name.
     *
     * @param field providing the type to encode.
     * @throws IOException if there is an error while encoding.
     */
    final protected void encodeFieldType(RecordDataSchema.Field field) throws IOException
    {
        _builder.writeFieldName(TYPE_KEY);
        DataSchema fieldSchema = field.getType();
        if (fieldSchema instanceof TyperefDataSchema) {
            _builder.writeString(((TyperefDataSchema) fieldSchema).getFullName());
        } else {
            _builder.writeString(fieldSchema.getUnionMemberKey());
        }
    }

    /**
     * Encode the specified un-named {@link DataSchema}.
     *
     * Un-named {@link DataSchema}'s are the {@link DataSchema}'s for the
     * map, array, and union types.
     *
     * Overrides superclass behavior which encodes full union member schemas rather than named
     * references.
     *
     * @param schema to encode.
     * @throws IOException if there is an error while encoding.
     */
    final protected void encodeUnnamed(DataSchema schema) throws IOException
    {
        if (schema instanceof UnionDataSchema) {
            List<String> memberNames = ((UnionDataSchema) schema)
                    .getTypes()
                    .stream()
                    .map(ref -> ref.getUnionMemberKey())
                    .collect(Collectors.toList());
            _builder.writeStringArray(memberNames);
        } else {
            super.encodeUnnamed(schema);
        }
    }
}
