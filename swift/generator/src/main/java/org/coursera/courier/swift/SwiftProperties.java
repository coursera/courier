package org.coursera.courier.swift;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;

/**
 * Customizable properties that may be added to a Pegasus schema.
 *
 * Example usage:
 *
 * <code>
 *   {
 *     "name": "Fortune",
 *     "namespace": "org.example",
 *     "type": "record",
 *     "fields": [ ... ],
 *     "swift": {
 *     }
 *   }
 * </code>
 */
public class SwiftProperties {

  /**
   * "optionality" property.
   *
   * Java representations of Pegasus primitive types supported by this generator.
   */
  public enum Optionality {

    /**
     * Allows required fields to be absent, useful when working with projections.
     *
     * "null" is used to represent un-projected fields (required or optional) as well as absent
     * optional fields.
     *
     * Pegasus primitives are represented as Java boxed primitive types.
     * E.g. {@link java.lang.Integer} such that they are nullable.
     */
    REQUIRED_FIELDS_MAY_BE_ABSENT,

    // TODO(jbetz): Remove as soon as we've migrated away from this usage pattern.
    /**
     * WARNING: this mode is unsafe when used in conjunction with projections, as a read/modify/write
     * pattern on a projection could result in the default value of primitives (e.g. 0 for ints)
     * to be accidentally written.
     *
     * Provided for compatibility with "basic" GSON data bindings.
     *
     * Required primitive fields are represented as Java primitives and must be present. If not
     * explicitly set, they will default to the Java defined default value for the particular
     * primitive type (e.g. 0 for int).
     *
     * Also, when reading JSON, if a required primitive field is absent, the field in data binding
     * will default to the Java defined default value of the primitive type.
     *
     * When writing JSON, all required primitive fields will be written, even if they are the
     * Java defined default value of the primitive type.
     *
     * Optional primitive fields are represented as nullable Java boxed primitive types.
     */
    BASIC
  }

  public final Optionality optionality;
  public final boolean equatable;

  public static final SwiftProperties DEFAULT =
      new SwiftProperties(Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT, false);

  public SwiftProperties(Optionality optionality, boolean equatable) {
    this.optionality = optionality;
    this.equatable = equatable;
  }

  public static SwiftProperties lookupSwiftProperties(ClassTemplateSpec templateSpec) {
    DataSchema schema = templateSpec.getSchema();
    if (schema == null) {
      return DEFAULT;
    } else {
      Object swift = schema.getProperties().get("swift");
      if (swift == null || !(swift instanceof DataMap)) {
        return DEFAULT;
      }
      DataMap properties = ((DataMap) swift);

      String primitiveStyleStr = properties.getString("primitiveStyle");

      Optionality optionality =
          primitiveStyleStr == null ? DEFAULT.optionality : Optionality.valueOf(primitiveStyleStr);

      Boolean maybeEquatable = properties.getBoolean("equatable");
      boolean equatable =  maybeEquatable == null ? DEFAULT.equatable : maybeEquatable;

      return new SwiftProperties(optionality, equatable);
    }
  }
}
