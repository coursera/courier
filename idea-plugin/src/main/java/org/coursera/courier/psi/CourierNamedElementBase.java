package org.coursera.courier.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class CourierNamedElementBase extends ASTWrapperPsiElement implements CourierNamedElement {
  public CourierNamedElementBase(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String getName() {
    return getFullname().getName();
  }

  public CourierFile getCourierFile() {
    return (CourierFile)getContainingFile();
  }
}
