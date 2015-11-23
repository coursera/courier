package org.coursera.courier.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public abstract class CourierNamedElementImportBase extends CourierNamedElementReferenceBase implements CourierNamedElementImport, CourierImportDeclaration {
  public CourierNamedElementImportBase(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public TypeName getFullname() {
    return TypeName.unescaped(getFullyQualifiedName().getText());
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    String namespace = getFullname().getNamespace();
    TypeName fullname = new TypeName(namespace, name);
    CourierTypeReference replacement = CourierElementFactory.createCourierTypeReference(this.getProject(), fullname.toString());
    getFullyQualifiedName().replace(replacement.getFullyQualifiedName());
    return this;
  }

  @Override
  public boolean isUsed() {
    for (CourierTypeReference ref: getCourierFile().getTypeReferences()) {
      if (ref.getFullname().equals(getFullname())) {
        return true;
      }
    }
    return false;
  }
}
