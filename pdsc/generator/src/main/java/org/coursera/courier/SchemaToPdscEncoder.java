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

public class SchemaToPdscEncoder {

    public static String schemaToPdsc(NamedDataSchema schema) {
        return schemaToPdsc(schema, JsonBuilder.Pretty.INDENTED);
    }

    /**
     * Replicates structure of SchemaToPdscEncoder::schemaToJson method, but with
     * a SchemaToPdscJsonEncoder in place of the SchemaToJsonEncoder.
     *
     * @param schema is the {@link DataSchema} to build a JSON encoded output for.
     * @param pretty is the pretty printing mode.
     * @return the JSON encoded string representing the {@link DataSchema} in pdsc format.
     */
    public static String schemaToPdsc(NamedDataSchema schema, JsonBuilder.Pretty pretty) {
        JsonBuilder builder = null;
        try {
            builder = new JsonBuilder(pretty);
            final SchemaToPdscJsonEncoder encoder = new SchemaToPdscJsonEncoder(builder, schema);
            encoder.encodeRoot(schema);
            return builder.result();
        } catch (IOException exc) {
            return exc.getMessage();
        } finally {
            if (builder != null) {
                builder.closeQuietly();
            }
        }
    }

    private static class SchemaToPdscJsonEncoder extends SchemaToJsonEncoder {

        NamedDataSchema rootSchema;

        public SchemaToPdscJsonEncoder(JsonBuilder builder, NamedDataSchema rootSchema) {
            super(builder);
            this.rootSchema = rootSchema;
        }

        /**
         * Encode the root type in full using the base encoding definition.
         */
        final protected void encodeRoot(DataSchema schema) throws IOException {
            super.encode(schema);
        }

        /**
         * Encode non-root types as references, rather than full definitions.
         */
        public void encode(DataSchema schema) throws IOException {
            if (schema instanceof ArrayDataSchema) {
                super.encode(schema);
            } else if (schema instanceof MapDataSchema) {
                super.encode(schema);
            } else {
                _builder.writeString(getSchemaRef(schema));
            }
        }

        private String getSchemaRef(DataSchema schema) {
            if (schema instanceof NamedDataSchema) {
                NamedDataSchema namedSchema = (NamedDataSchema) schema;
                if (namedSchema.getNamespace().equals(rootSchema.getNamespace())) {
                    return namedSchema.getName();
                } else {
                    return namedSchema.getFullName();
                }
            } else {
                return schema.getUnionMemberKey();
            }
        }
    }

}
