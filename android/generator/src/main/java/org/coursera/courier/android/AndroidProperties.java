package org.coursera.courier.android;

public class AndroidProperties {
  public enum ArrayStyle { ARRAY, LIST }

  public enum Mutability { MUTABLE, IMMUTABLE }

  public enum PrimitiveStyle { PRIMITIVES, BOXED }

  public final ArrayStyle arrayStyle;
  public final Mutability mutability;
  public final PrimitiveStyle primitiveStyle;

  // TODO: set default to immutable
  public static final AndroidProperties DEFAULT =
      new AndroidProperties(ArrayStyle.LIST, Mutability.MUTABLE, PrimitiveStyle.BOXED);

  public AndroidProperties(
      ArrayStyle arrayStyle, Mutability mutability, PrimitiveStyle primitiveStyle) {
    this.arrayStyle = arrayStyle;
    this.mutability = mutability;
    this.primitiveStyle = primitiveStyle;
  }
}
