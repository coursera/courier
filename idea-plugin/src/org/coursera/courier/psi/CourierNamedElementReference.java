package org.coursera.courier.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public abstract class CourierNamedElementReference extends CourierNamedElementReferenceBase implements CourierTypeReference {
  public CourierNamedElementReference(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    CourierFullyQualifiedName toReplace = getFullyQualifiedName();
    TypeName toReplaceName = new TypeName(toReplace.getText());
    String replacementName = new TypeName(toReplaceName.getNamespace(), name).toString();
    CourierTypeReference replacement = CourierElementFactory.createCourierTypeReference(this.getProject(), replacementName);
    toReplace.replace(replacement.getFullyQualifiedName());
    return this;
  }
}
