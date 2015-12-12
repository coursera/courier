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
 *       "optionality": "STRICT"
 *     }
 *   }
 * </code>
 */
public class SwiftProperties {

  /**
   * "optionality" property.
   *
   * Swift representations of Pegasus primitive types supported by this generator.
   */
  public enum Optionality {

    /**
     * Allows required fields to be absent, useful when working with projections.
     *
     * "nil" is used to represent un-projected fields (required or optional) as well as absent
     * optional fields.
     */
    REQUIRED_FIELDS_MAY_BE_ABSENT,

    // TODO(jbetz): Remove as soon as we've migrated away from this usage pattern.
    /**
     * WARNING: this mode is unsafe when used in conjunction with projections, as a read/modify/coercerOutput
     * pattern on a projection could result in the default value of primitives (e.g. 0 for ints)
     * to be accidentally written.
     *
     * Required fields generated as non-optional Swift properties.
     *
     * Optional fields are generated as optional Swift properties, where an absent optional field
     * value is represented as `nil`.
     *
     * When reading JSON, if a required field is absent, the field in data binding
     * will default to the schema defined default value.
     *
     * When writing JSON, all required primitive fields will be written, even if they are the
     * schema defined default value.
     */
    STRICT
  }

  public final Optionality optionality;
  public final boolean equatable;
  public final boolean omit;

  public SwiftProperties(Optionality optionality, boolean equatable, boolean omit) {
    this.optionality = optionality;
    this.equatable = equatable;
    this.omit = omit;
  }

}
