package org.coursera.courier.psi;

import com.intellij.psi.tree.IElementType;
import org.coursera.courier.CourierLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CourierTokenType extends IElementType {
  public CourierTokenType(@NotNull @NonNls String debugName) {
    super(debugName, CourierLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "CourierTokenType." + super.toString();
  }
}
