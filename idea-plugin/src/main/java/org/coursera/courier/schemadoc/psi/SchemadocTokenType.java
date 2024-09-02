package org.coursera.courier.schemadoc.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.coursera.courier.schemadoc.SchemadocLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class SchemadocTokenType extends IElementType {
  public SchemadocTokenType(@NotNull @NonNls String debugName) {
    super(debugName, SchemadocLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "SchemadocTokenType." + super.toString();
  }

  public static TokenSet DOC_COMMENT_TOKENS = TokenSet.create(
    SchemadocTypes.DOC_COMMENT_START,
    SchemadocTypes.DOC_COMMENT_CONTENT,
    SchemadocTypes.DOC_COMMENT_LEADING_ASTRISK,
    SchemadocTypes.DOC_COMMENT_END);
}
