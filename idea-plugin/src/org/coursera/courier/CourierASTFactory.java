package org.coursera.courier;

import com.intellij.lang.ASTFactory;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.psi.CourierSchemadocComment;
import org.jetbrains.annotations.NotNull;

public class CourierASTFactory extends ASTFactory {
  @Override
  public LeafElement createLeaf(@NotNull final IElementType type, @NotNull final CharSequence text) {
    if (CourierSchemadocComment.tokens.contains(type)) {
      return new PsiCommentImpl(type, text);
    } else {
      return super.createLeaf(type, text);
    }
  }
}
