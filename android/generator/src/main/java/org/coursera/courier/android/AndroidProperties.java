package org.coursera.courier.android;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;

public class AndroidProperties {

  /**
   * The two major data binding generation modes supported by this generator.
   */
  public enum Mutability {

    /**
     * Generates immutable types.
     */
    IMMUTABLE,

    /**
     * Provided for compatibility with basic existing GSON style data bindings.
     *
     * Generates simple classes with a default constructor and public fields.
     */
    MUTABLE
  }

  /**
   * Java representations of a Pegasus 'array' supported by this generator.
   */
  public enum ArrayStyle {

    /**
     * Represent a Pegasus 'array' as a Java array.
     *
     * Since all Java arrays are mutable, this may be used with 'MUTABLE' data bindings only only.
     */
    ARRAYS,

    /**
     * Represent a Pegasus 'array' as a {@link java.util.List}.
     */
    LISTS
  }

  /**
   * Java representations of Pegasus primitive types supported by this generator.
   */
  public enum PrimitiveStyle {

    // TODO(jbetz): Remove? This will be unsafe when used with projections.
    /**
     * WARN: this mode is unsafe when used in conjunction with projections, as a read/modify/write
     * pattern on a projection could result in the default value of primitives (e.g. 0 for ints)
     * to be accidentally written.
     *
     * Pegasus primitives are represented as Java primitives.
     */
    PRIMITIVES,

    /**
     * Pegasus primitives are represented as Java boxed primitive types.
     * E.g. {@link java.lang.Integer}.
     */
    BOXED
  }

  public final ArrayStyle arrayStyle;
  public final Mutability mutability;
  public final PrimitiveStyle primitiveStyle;

  public static final AndroidProperties DEFAULT =
      new AndroidProperties(ArrayStyle.LISTS, Mutability.IMMUTABLE, PrimitiveStyle.BOXED);

  public AndroidProperties(
      ArrayStyle arrayStyle, Mutability mutability, PrimitiveStyle primitiveStyle) {
    this.arrayStyle = arrayStyle;
    this.mutability = mutability;
    this.primitiveStyle = primitiveStyle;
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
      PrimitiveStyle primitiveStyle =
          primitiveStyleStr == null ? DEFAULT.primitiveStyle : PrimitiveStyle.valueOf(primitiveStyleStr);

      if (mutability == Mutability.IMMUTABLE) {
        if (arrayStyle == ArrayStyle.ARRAYS) {
          throw new IllegalArgumentException(
            "In pegasus schema '" + templateSpec.getFullName() + "', " +
            "to guarantee immutablity, 'arrayStyle': '" + ArrayStyle.ARRAYS + "' may not be used " +
            "with 'mutability': '" + Mutability.IMMUTABLE + "'. Use " + ArrayStyle.LISTS + " instead.");
        }
        if (primitiveStyle == PrimitiveStyle.PRIMITIVES) {
          throw new IllegalArgumentException(
            "In pegasus schema '" + templateSpec.getFullName() + "', " +
            "to support projections, 'primitiveStyle': '" + PrimitiveStyle.PRIMITIVES + "' may not " +
            "be used with 'mutability': '" + Mutability.IMMUTABLE + "'. Use " + PrimitiveStyle.BOXED + " instead.");
        }
      }
      return new AndroidProperties(arrayStyle, mutability, primitiveStyle);
    }
  }
}
