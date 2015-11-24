package org.coursera.courier;

import com.intellij.codeInsight.editorActions.CommentCompleteHandler;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.psi.CourierElementType;
import org.coursera.courier.psi.CourierTypes;
import org.coursera.courier.schemadoc.psi.SchemadocTokenType;

public class CourierCommentCompleteHandler implements CommentCompleteHandler {
  @Override
  public boolean isCommentComplete(PsiComment psiComment, CodeDocumentationAwareCommenter codeDocumentationAwareCommenter, Editor editor) {
    if (psiComment.getTokenType() == CourierElementType.DOC_COMMENT) {
      return psiComment.getText().trim().endsWith("*/");
    }
    return false;
  }

  @Override
  public boolean isApplicable(PsiComment psiComment, CodeDocumentationAwareCommenter codeDocumentationAwareCommenter) {
    IElementType type = psiComment.getTokenType();
    return SchemadocTokenType.DOC_COMMENT_TOKENS.contains(type); // ||
      //psiComment.getTokenType() == CourierTypes.SCHEMADOC;
  }
}
