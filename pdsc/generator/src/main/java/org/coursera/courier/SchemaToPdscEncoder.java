package org.coursera.courier;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.JsonBuilder;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaToJsonEncoder;

import java.io.IOException;

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
        System.out.println(fieldSchema);
        _builder.writeString(fieldSchema.getUnionMemberKey());
    }


}
