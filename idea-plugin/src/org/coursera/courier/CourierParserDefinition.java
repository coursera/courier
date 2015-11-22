package org.coursera.courier;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.coursera.courier.parser.CourierParser;
import org.coursera.courier.psi.CourierElementType;
import org.coursera.courier.psi.CourierFile;
import org.coursera.courier.psi.CourierSchemadocComment;
import org.coursera.courier.psi.CourierTypeDeclarationStubType;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.CourierTypeNameDeclarationStub;
import org.coursera.courier.psi.CourierTypeNameDeclarationStubImpl;
import org.coursera.courier.psi.CourierTypes;
import org.coursera.courier.psi.impl.CourierTypeNameDeclarationImpl;
import org.jetbrains.annotations.NotNull;

public class CourierParserDefinition implements ParserDefinition {
  public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
  public static final TokenSet COMMENTS = TokenSet.create(
      CourierTypes.SINGLE_LINE_COMMENT,
      CourierTypes.BLOCK_COMMENT_EMPTY,
      CourierTypes.BLOCK_COMMENT_NON_EMPTY,
      CourierTypes.COMMA);
  public static final TokenSet STRING = TokenSet.create(CourierTypes.STRING);

  public static final IStubFileElementType FILE = new CourierStubFileElementType(CourierFileType.INSTANCE.getLanguage());

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new CourierLexerAdapter();
  }

  @NotNull
  public TokenSet getWhitespaceTokens() {
    return WHITE_SPACES;
  }

  @NotNull
  public TokenSet getCommentTokens() {
    return COMMENTS;
  }

  @NotNull
  public TokenSet getStringLiteralElements() {
    return STRING;
  }

  @NotNull
  public PsiParser createParser(final Project project) {
    return new CourierParser();
  }

  @Override
  public IFileElementType getFileNodeType() {
    return FILE;
  }

  public PsiFile createFile(FileViewProvider viewProvider) {
    return new CourierFile(viewProvider);
  }

  public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    final IElementType type = node.getElementType();
    if (type instanceof CourierElementType) {
      return ((CourierElementType)type).createPsi(node);
    } else if (type == CourierTypes.TYPE_NAME_DECLARATION) {
      return new CourierTypeNameDeclarationImpl(node);
    }
    throw new IllegalStateException("Incorrect node for CourierParserDefinition: " + node + " (" + type + ")");
  }
}
