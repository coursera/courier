package org.coursera.courier.schemadoc.psi;

import com.intellij.psi.tree.IElementType;
import org.coursera.courier.schemadoc.SchemadocLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class SchemadocElementType extends IElementType {
  public SchemadocElementType(@NotNull @NonNls String debugName) {
    super(debugName, SchemadocLanguage.INSTANCE);
  }
}
