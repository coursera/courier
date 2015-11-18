package org.coursera.courier.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class CourierNamedElementBase extends ASTWrapperPsiElement implements CourierNamedElement {
  public CourierNamedElementBase(@NotNull ASTNode node) {
    super(node);
  }

  public String getName() {
    String fullname = getFullname();
    int idx = fullname.lastIndexOf('.');
    if (idx > 0 && idx < fullname.length() - 2) {
      return fullname.substring(idx + 1);
    } else {
      return fullname;
    }
  }
}
