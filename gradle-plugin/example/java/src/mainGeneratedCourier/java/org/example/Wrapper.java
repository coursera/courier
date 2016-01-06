
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
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from /Users/jbetz/base/coursera/courier/gradle-plugin/example/java/src/main/pegasus/org/example/Wrapper.pdsc.", date = "Tue Jan 05 16:59:14 PST 2016")
public class Wrapper
    extends RecordTemplate
{

    private final static Wrapper.Fields _fields = new Wrapper.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("{\"type\":\"record\",\"name\":\"Wrapper\",\"namespace\":\"org.example\",\"fields\":[{\"name\":\"fortune\",\"type\":{\"type\":\"record\",\"name\":\"Fortune\",\"fields\":[{\"name\":\"message\",\"type\":\"string\"}]}}]}"));
    private final static RecordDataSchema.Field FIELD_Fortune = SCHEMA.getField("fortune");

    public Wrapper() {
        super(new DataMap(), SCHEMA);
    }

    public Wrapper(DataMap data) {
        super(data, SCHEMA);
    }

    public static Wrapper.Fields fields() {
        return _fields;
    }

    /**
     * Existence checker for fortune
     * 
     * @see Wrapper.Fields#fortune
     */
    public boolean hasFortune() {
        return contains(FIELD_Fortune);
    }

    /**
     * Remover for fortune
     * 
     * @see Wrapper.Fields#fortune
     */
    public void removeFortune() {
        remove(FIELD_Fortune);
    }

    /**
     * Getter for fortune
     * 
     * @see Wrapper.Fields#fortune
     */
    public Fortune getFortune(GetMode mode) {
        return obtainWrapped(FIELD_Fortune, Fortune.class, mode);
    }

    /**
     * Getter for fortune
     * 
     * @see Wrapper.Fields#fortune
     */
    public Fortune getFortune() {
        return obtainWrapped(FIELD_Fortune, Fortune.class, GetMode.STRICT);
    }

    /**
     * Setter for fortune
     * 
     * @see Wrapper.Fields#fortune
     */
    public Wrapper setFortune(Fortune value, SetMode mode) {
        putWrapped(FIELD_Fortune, Fortune.class, value, mode);
        return this;
    }

    /**
     * Setter for fortune
     * 
     * @see Wrapper.Fields#fortune
     */
    public Wrapper setFortune(Fortune value) {
        putWrapped(FIELD_Fortune, Fortune.class, value, SetMode.DISALLOW_NULL);
        return this;
    }

    @Override
    public Wrapper clone()
        throws CloneNotSupportedException
    {
        return ((Wrapper) super.clone());
    }

    @Override
    public Wrapper copy()
        throws CloneNotSupportedException
    {
        return ((Wrapper) super.copy());
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

        public org.example.Fortune.Fields fortune() {
            return new org.example.Fortune.Fields(getPathComponents(), "fortune");
        }

    }

}
