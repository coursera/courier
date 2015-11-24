package org.coursera.courier.formatter;

import com.intellij.codeInsight.editorActions.CommentCompleteHandler;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.psi.CourierElementType;
import org.coursera.courier.psi.CourierTypes;
import org.coursera.courier.schemadoc.psi.SchemadocTokenType;
import org.coursera.courier.schemadoc.psi.SchemadocTypes;

public class CourierCommentCompleteHandler implements CommentCompleteHandler {
  @Override
  public boolean isCommentComplete(PsiComment psiComment, CodeDocumentationAwareCommenter codeDocumentationAwareCommenter, Editor editor) {
    if (psiComment.getTokenType() == CourierElementType.DOC_COMMENT) {
      PsiElement last = psiComment.getLastChild();
      String text = psiComment.getText();
      int firstDocCommentStart = text.indexOf("/*");
      int lastDocCommentStart = text.lastIndexOf("/*");
      boolean hasNestedDocCommentStart = (firstDocCommentStart > -1 && lastDocCommentStart > -1 && firstDocCommentStart != lastDocCommentStart);
      return (!hasNestedDocCommentStart && last != null && last.getNode().getElementType() == SchemadocTypes.DOC_COMMENT_END);
    }
    return true;
  }

  @Override
  public boolean isApplicable(PsiComment psiComment, CodeDocumentationAwareCommenter codeDocumentationAwareCommenter) {
    return psiComment.getTokenType() == CourierElementType.DOC_COMMENT;
  }
}
