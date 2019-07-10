package org.coursera.courier;

import com.linkedin.data.schema.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.linkedin.data.schema.DataSchemaConstants.*;
import static com.linkedin.data.schema.DataSchemaConstants.ALIASES_KEY;


public class SchemaToPdscEncoder extends SchemaToJsonEncoder {

    public static String schemaToPdsc(DataSchema schema) {
        return schemaToPdsc(schema, JsonBuilder.Pretty.INDENTED);
    }

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


    public SchemaToPdscEncoder(JsonBuilder builder)
    {
        super(builder);
    }

    /**
     * Encode a field's type (i.e. {@link DataSchema}.
     *
     * @param field providing the type to encode.
     * @throws IOException if there is an error while encoding.
     */
    protected void encodeFieldType(RecordDataSchema.Field field) throws IOException
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
     * Encode a {@link NamedDataSchema}.
     *
     * A {@link NamedDataSchema}'s are the {@link DataSchema}'s for the
     * typeref, enum, fixed, and record types.
     *
     * @param schema to encode.
     * @throws IOException if there is an error while encoding.
     */
    protected void encodeUnnamed(DataSchema schema) throws IOException
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
