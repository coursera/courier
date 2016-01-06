
package org.example;

import java.util.List;
import javax.annotation.Generated;
import com.linkedin.data.DataMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.SetMode;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from /Users/jbetz/base/coursera/courier/gradle-plugin/example/schemas/build/libs/schemas-pegasus.jar:pegasus/org/example/Fortune.pdsc.", date = "Tue Jan 05 16:59:14 PST 2016")
public class Fortune
    extends RecordTemplate
{

    private final static Fortune.Fields _fields = new Fortune.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("{\"type\":\"record\",\"name\":\"Fortune\",\"namespace\":\"org.example\",\"fields\":[{\"name\":\"message\",\"type\":\"string\"}]}"));
    private final static RecordDataSchema.Field FIELD_Message = SCHEMA.getField("message");

    public Fortune() {
        super(new DataMap(), SCHEMA);
    }

    public Fortune(DataMap data) {
        super(data, SCHEMA);
    }

    public static Fortune.Fields fields() {
        return _fields;
    }

    /**
     * Existence checker for message
     * 
     * @see Fortune.Fields#message
     */
    public boolean hasMessage() {
        return contains(FIELD_Message);
    }

    /**
     * Remover for message
     * 
     * @see Fortune.Fields#message
     */
    public void removeMessage() {
        remove(FIELD_Message);
    }

    /**
     * Getter for message
     * 
     * @see Fortune.Fields#message
     */
    public String getMessage(GetMode mode) {
        return obtainDirect(FIELD_Message, String.class, mode);
    }

    /**
     * Getter for message
     * 
     * @see Fortune.Fields#message
     */
    public String getMessage() {
        return obtainDirect(FIELD_Message, String.class, GetMode.STRICT);
    }

    /**
     * Setter for message
     * 
     * @see Fortune.Fields#message
     */
    public Fortune setMessage(String value, SetMode mode) {
        putDirect(FIELD_Message, String.class, String.class, value, mode);
        return this;
    }

    /**
     * Setter for message
     * 
     * @see Fortune.Fields#message
     */
    public Fortune setMessage(String value) {
        putDirect(FIELD_Message, String.class, String.class, value, SetMode.DISALLOW_NULL);
        return this;
    }

    @Override
    public Fortune clone()
        throws CloneNotSupportedException
    {
        return ((Fortune) super.clone());
    }

    @Override
    public Fortune copy()
        throws CloneNotSupportedException
    {
        return ((Fortune) super.copy());
    }

    public static class Fields
        extends PathSpec
    {


        public Fields(List<String> path, String name) {
            super(path, name);
        }

        public Fields() {
            super();
        }

        public PathSpec message() {
            return new PathSpec(getPathComponents(), "message");
        }

    }

}
