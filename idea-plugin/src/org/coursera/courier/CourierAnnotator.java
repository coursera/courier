package org.coursera.courier;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import org.coursera.courier.psi.CourierEnumSymbol;
import org.coursera.courier.psi.CourierFieldName;
import org.coursera.courier.psi.CourierPropNameDeclaration;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.CourierTypeReference;
import org.coursera.courier.psi.CourierVisitor;
import org.jetbrains.annotations.NotNull;

public class CourierAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull final PsiElement element, @NotNull final AnnotationHolder holder) {
    element.accept(new CourierVisitor() {
      @Override
      public void visitFieldName(@NotNull CourierFieldName o) {
        super.visitFieldName(o);
        setHighlighting(o, holder, CourierSyntaxHighlighter.FIELD);
      }
    });
    element.accept(new CourierVisitor() {
      @Override
      public void visitTypeNameDeclaration(@NotNull CourierTypeNameDeclaration o) {
        super.visitTypeNameDeclaration(o);
        setHighlighting(o, holder, CourierSyntaxHighlighter.TYPE_NAME);
      }
    });
    element.accept(new CourierVisitor() {
      @Override
      public void visitTypeReference(@NotNull CourierTypeReference o) {
        super.visitTypeReference(o);
        setHighlighting(o, holder, CourierSyntaxHighlighter.TYPE_REFERENCE);
      }
    });
    element.accept(new CourierVisitor() {
      @Override
      public void visitPropNameDeclaration(@NotNull CourierPropNameDeclaration o) {
        super.visitPropNameDeclaration(o);
        setHighlighting(o, holder, CourierSyntaxHighlighter.PROPERTY);
      }
    });
    element.accept(new CourierVisitor() {
      @Override
      public void visitEnumSymbol(@NotNull CourierEnumSymbol o) {
        super.visitEnumSymbol(o);
        setHighlighting(o, holder, CourierSyntaxHighlighter.FIELD);
      }
    });
  }

  private static void setHighlighting(@NotNull PsiElement element, @NotNull AnnotationHolder holder,
                                      @NotNull TextAttributesKey key) {
    holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
    holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(
        EditorColorsManager.getInstance().getGlobalScheme().getAttributes(key));
  }
}
