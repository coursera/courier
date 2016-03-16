package org.coursera.courier.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.alignment.AlignmentStrategy;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.coursera.courier.CourierLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.coursera.courier.psi.CourierTypes.BLOCK_COMMENT;
import static org.coursera.courier.psi.CourierTypes.COLON;
import static org.coursera.courier.psi.CourierTypes.COMMA;
import static org.coursera.courier.psi.CourierTypes.DOT;
import static org.coursera.courier.psi.CourierTypes.ENUM_SYMBOL_DECLARATION;
import static org.coursera.courier.psi.CourierTypes.EQUALS;
import static org.coursera.courier.psi.CourierTypes.FIELD_SELECTION_ELEMENT;
import static org.coursera.courier.psi.CourierTypes.IMPORT_DECLARATION;
import static org.coursera.courier.psi.CourierTypes.IMPORT_DECLARATIONS;
import static org.coursera.courier.psi.CourierTypes.IMPORT_KEYWORD;
import static org.coursera.courier.psi.CourierTypes.LINE_COMMENT;
import static org.coursera.courier.psi.CourierTypes.NAMESPACE_DECLARATION;
import static org.coursera.courier.psi.CourierTypes.NAMESPACE_KEYWORD;
import static org.coursera.courier.psi.CourierTypes.QUESTION_MARK;
import static org.coursera.courier.psi.CourierTypes.TYPE_DECLARATION;
import static org.coursera.courier.schemadoc.psi.SchemadocTypes.DOC_COMMENT_LEADING_ASTRISK;

public class CourierFormattingModelBuilder implements FormattingModelBuilder {
  @NotNull
  @Override
  public FormattingModel createModel(
    @NotNull PsiElement element, @NotNull CodeStyleSettings settings) {
    Block block =
      AbstractCourierBlock.createBlock(
        element.getNode(),
        Indent.getAbsoluteNoneIndent(),
        null,
        AlignmentStrategy.getNullStrategy(),
        settings,
        createSpacingBuilder(settings));

    return FormattingModelProvider.createFormattingModelForPsiFile(
      element.getContainingFile(), block, settings);
  }

  @NotNull
  private static SpacingBuilder createSpacingBuilder(@NotNull CodeStyleSettings settings) {
    // TODO: all the below hard coded values can potentially be replaced with code style config
    // variables.
    return new SpacingBuilder(settings, CourierLanguage.INSTANCE)
      .before(COMMA).spaceIf(false)
      .after(COMMA).spaceIf(true)
      .before(COLON).spaceIf(false)
      .after(COLON).spaceIf(true)
      .before(EQUALS).spaceIf(true)
      .after(EQUALS).spaceIf(true)
      .before(QUESTION_MARK).spaceIf(false)
      .after(QUESTION_MARK).spaceIf(true)
      .around(DOT).none()

      .before(NAMESPACE_DECLARATION).blankLines(0)
      .after(NAMESPACE_DECLARATION).blankLines(1)
      .after(NAMESPACE_KEYWORD).spaceIf(true)

      .around(IMPORT_DECLARATIONS).blankLines(1)
      .around(IMPORT_DECLARATION).lineBreakInCode()
      .after(IMPORT_KEYWORD).spaceIf(true)

      .before(TYPE_DECLARATION).lineBreakInCode()
      .after(TYPE_DECLARATION).lineBreakInCode()
      .after(FIELD_SELECTION_ELEMENT).lineBreakInCode()
      .after(ENUM_SYMBOL_DECLARATION).lineBreakInCode()

      .after(LINE_COMMENT).lineBreakInCode()
      .after(BLOCK_COMMENT).lineBreakInCode()
      ;
  }

  @Nullable
  @Override
  public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
    return null;
  }
}
