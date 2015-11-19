package org.coursera.courier.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.HashSet;
import org.coursera.courier.CourierLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CourierTokenType extends IElementType {
  public CourierTokenType(@NotNull @NonNls String debugName) {
    super(debugName, CourierLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "CourierTokenType." + super.toString();
  }

  public static final Set<String> PRIMITIVE_TYPES = new HashSet<String>();
  static {
    PRIMITIVE_TYPES.add("string");
    PRIMITIVE_TYPES.add("boolean");
    PRIMITIVE_TYPES.add("int");
    PRIMITIVE_TYPES.add("long");
    PRIMITIVE_TYPES.add("float");
    PRIMITIVE_TYPES.add("double");
    PRIMITIVE_TYPES.add("bytes");
  }
}
