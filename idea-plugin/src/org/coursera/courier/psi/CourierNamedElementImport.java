package org.coursera.courier.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class CourierNamedElementImport extends CourierNamedElementReference implements CourierImportDeclaration {
  public CourierNamedElementImport(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public TypeName getFullname() {
    return TypeName.unescaped(getFullyQualifiedName().getText());
  }
}
