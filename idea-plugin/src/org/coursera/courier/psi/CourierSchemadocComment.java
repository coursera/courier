package org.coursera.courier.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CourierSchemadocComment extends ASTWrapperPsiElement implements PsiComment {
  public CourierSchemadocComment(@NotNull final ASTNode node) {
    super(node);
  }

  @Nullable
  public PsiElement getOwner() {
    return null; // TODO(jbetz); traverse up and find the field or type decl. node
  }

  public IElementType getTokenType() {
    return getNode().getElementType();
  }

  public static final IElementType element = CourierTypes.SCHEMADOC;
  public static final TokenSet tokens = TokenSet.create(
      CourierTypes.SCHEMADOC_START,
      CourierTypes.SCHEMADOC_CONTENT,
      CourierTypes.SCHEMADOC_END);
}
