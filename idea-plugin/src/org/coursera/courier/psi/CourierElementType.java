package org.coursera.courier.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.CourierLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CourierElementType extends IElementType {
  public CourierElementType(@NotNull @NonNls String debugName) {
    super(debugName, CourierLanguage.INSTANCE);
  }

  public PsiElement createPsi(ASTNode node) {
    if (node.getElementType() == CourierTypes.SCHEMADOC) {
      return new CourierSchemadocComment(node);
    } else {
      return CourierTypes.Factory.createElement(node);
    }
  }
}
