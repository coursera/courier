package org.coursera.courier.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.intellij.psi.tree.IReparseableElementType;
import org.coursera.courier.CourierLanguage;
import org.coursera.courier.CourierParserDefinition;
import org.coursera.courier.schemadoc.psi.PsiSchemadocElement;
import org.coursera.courier.schemadoc.psi.SchemadocParserDefinition;
import org.coursera.courier.schemadoc.psi.SchemadocTypes;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CourierElementType extends IElementType {
  public CourierElementType(@NotNull @NonNls String debugName) {
    super(debugName, CourierLanguage.INSTANCE);
  }

  public PsiElement createPsi(ASTNode node) {
    return CourierTypes.Factory.createElement(node);
  }

  /**
   * Based on JavaDocElementType.DOC_COMMENT.
   *
   * Delegate all doc comment parsing to the 'schemadoc' parser.  The Courier lexer one of these
   * elements for each doc comment encountered, and leaves them as-is to be lazily parsed by
   * the SchemadocParser as needed.
   *
   * The primary reason this is done this ways is so that doc comments have a single token type
   * that can be returned by CourierCommenter.getDocumentationCommentTokenType() but still
   * have distinct content tokens that can be formatted by CourierFormattingModelBuilder.
   *
   * @see com.intellij.codeInsight.editorActions.EnterHandler
   * @see org.coursera.courier.formatter.CourierFormattingModelBuilder
   * @see org.coursera.courier.formatter.CourierCommenter#getDocumentationCommentTokenType
   * @see org.coursera.courier.formatter.CourierCommenter
   * @see org.coursera.courier.schemadoc.parser.SchemadocParser
   */
  public static final ILazyParseableElementType DOC_COMMENT =
    new IReparseableElementType("DOC_COMMENT", CourierLanguage.INSTANCE) {
      @Override
      public ASTNode createNode(final CharSequence text) {
        return new PsiSchemadocElement(text);
      }

      public ASTNode parseContents(ASTNode chameleon) {
        PsiElement parentElement = chameleon.getTreeParent().getPsi();
        assert parentElement != null : "parent psi is null: " + chameleon;
        return doParseContents(chameleon, parentElement);
      }

      protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
        Project project = psi.getProject();
        SchemadocParserDefinition parserDefinition = SchemadocParserDefinition.INSTANCE;
        PsiBuilder builder =
          PsiBuilderFactory.getInstance().createBuilder(
            parserDefinition, parserDefinition.createLexer(project), chameleon.getChars());
        PsiParser parser = parserDefinition.createParser(project);
        ASTNode node = parser.parse(SchemadocTypes.COMMENT, builder);
        return node.getFirstChildNode();
      }

      @Override
      public boolean isParsable(
        final CharSequence buffer, Language fileLanguage, final Project project) {
        if (!StringUtil.startsWith(buffer, "/**") ||
          !StringUtil.endsWith(buffer, "*/")) return false;

        // Prevent the parser from consuming non- doc comment tokens.
        Lexer lexer = CourierParserDefinition.INSTANCE.createLexer(project);
        lexer.start(buffer);
        if (lexer.getTokenType() == DOC_COMMENT) {
          lexer.advance();
          if (lexer.getTokenType() == null) {
            return true;
          }
        }
        return false;
      }
    };
}
