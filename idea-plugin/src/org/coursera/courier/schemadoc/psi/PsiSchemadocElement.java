package org.coursera.courier.schemadoc.psi;

import com.intellij.psi.PsiDocCommentBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LazyParseablePsiElement;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.psi.CourierElementType;
import org.jetbrains.annotations.Nullable;

public class PsiSchemadocElement extends LazyParseablePsiElement implements PsiDocCommentBase {
  public PsiSchemadocElement(CharSequence buffer) {
    super(CourierElementType.DOC_COMMENT, buffer);
  }

  @Nullable
  @Override
  public PsiElement getOwner() {
    return null;
  }

  @Override
  public IElementType getTokenType() {
    return getElementType();
  }
}
