package org.coursera.courier.android;

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
 *     "android": {
 *       "mutability": "MUTABLE",
 *       "arrayStyle": "LISTS",
 *       "optionality": "BASIC"
 *     }
 *   }
 * </code>
 */
public class AndroidProperties {

  /**
   * "mutability" property.
   *
   * Defines the two major data binding generation modes supported by this generator.
   */
  public enum Mutability {

    /**
     * Generates immutable types with parameterized constructors, a mutable builder utility class,
     * and public final fields.
     */
    IMMUTABLE,

    /**
     * Provided for compatibility with "basic" GSON data bindings.
     *
     * Generates simple classes with a default constructor and public fields.
     */
    MUTABLE
  }

  /**
   * "arrayStyle" property.
   *
   * Java representations of a Pegasus 'array' supported by this generator.
   */
  public enum ArrayStyle {

    /**
     * Represent a Pegasus 'array' as a {@link java.util.List} (e.g. "List&lt;Course&gt;")
     *
     * For immutable data bindings, all Lists are unmodifiable.
     */
    LISTS,

    /**
     * rovided for compatibility with "basic" GSON data bindings.
     *
     * Represent a Pegasus 'array' as a Java array (e.g. "Course[]").
     *
     * Since all Java arrays are mutable, this mode may be used with {@link Mutability#MUTABLE} only.
     */
    ARRAYS
  }

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

  public final ArrayStyle arrayStyle;
  public final Mutability mutability;
  public final Optionality optionality;

  public static final AndroidProperties DEFAULT =
      new AndroidProperties(ArrayStyle.LISTS, Mutability.IMMUTABLE, Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT);

  public AndroidProperties(
      ArrayStyle arrayStyle, Mutability mutability, Optionality optionality) {
    this.arrayStyle = arrayStyle;
    this.mutability = mutability;
    this.optionality = optionality;
  }

  public static AndroidProperties lookupAndroidProperties(ClassTemplateSpec templateSpec) {
    DataSchema schema = templateSpec.getSchema();
    if (schema == null) {
      return DEFAULT;
    } else {
      Object android = schema.getProperties().get("android");
      if (android == null || !(android instanceof DataMap)) {
        return DEFAULT;
      }
      DataMap properties = ((DataMap) android);

      String arrayStyleStr = properties.getString("arrayStyle");
      String mutabilityStr = properties.getString("mutability");
      String primitiveStyleStr = properties.getString("primitiveStyle");

      ArrayStyle arrayStyle =
          arrayStyleStr == null ? DEFAULT.arrayStyle : ArrayStyle.valueOf(arrayStyleStr);
      Mutability mutability =
          mutabilityStr == null ? DEFAULT.mutability : Mutability.valueOf(mutabilityStr);
      Optionality optionality =
          primitiveStyleStr == null ? DEFAULT.optionality : Optionality.valueOf(primitiveStyleStr);

      if (mutability == Mutability.IMMUTABLE) {
        if (arrayStyle == ArrayStyle.ARRAYS) {
          throw new IllegalArgumentException(
            "In pegasus schema '" + templateSpec.getFullName() + "', " +
            "to guarantee immutablity, 'arrayStyle': '" + ArrayStyle.ARRAYS + "' may not be used " +
            "with 'mutability': '" + Mutability.IMMUTABLE + "'. Use " + ArrayStyle.LISTS + " instead.");
        }
        if (optionality == Optionality.BASIC) {
          throw new IllegalArgumentException(
            "In pegasus schema '" + templateSpec.getFullName() + "', " +
            "to support projections, 'optionality': '" + Optionality.BASIC + "' may not " +
            "be used with 'mutability': '" + Mutability.IMMUTABLE + "'. Use " + Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT + " instead.");
        }
      }
      return new AndroidProperties(arrayStyle, mutability, optionality);
    }
  }
}
