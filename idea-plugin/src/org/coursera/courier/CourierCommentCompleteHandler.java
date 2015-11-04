package org.coursera.courier;

import com.intellij.codeInsight.editorActions.CommentCompleteHandler;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.psi.CourierSchemadocComment;
import org.coursera.courier.psi.CourierTypes;

public class CourierCommentCompleteHandler implements CommentCompleteHandler {
  @Override
  public boolean isCommentComplete(PsiComment psiComment, CodeDocumentationAwareCommenter codeDocumentationAwareCommenter, Editor editor) {
    if (psiComment.getTokenType() == CourierTypes.SCHEMADOC) {
      return psiComment.getLastChild().getNode().getElementType() == CourierTypes.SCHEMADOC_END;
    } else {
      PsiElement parent = psiComment.getParent();
      if (parent != null && parent.getNode().getElementType() == CourierTypes.SCHEMADOC) {
        return parent.getLastChild().getNode().getElementType() == CourierTypes.SCHEMADOC_END;
      } else {
        return false;
      }
    }
  }

  @Override
  public boolean isApplicable(PsiComment psiComment, CodeDocumentationAwareCommenter codeDocumentationAwareCommenter) {
    IElementType type = psiComment.getTokenType();
    return CourierSchemadocComment.tokens.contains(type) || psiComment.getTokenType() == CourierTypes.SCHEMADOC;
  }
}
