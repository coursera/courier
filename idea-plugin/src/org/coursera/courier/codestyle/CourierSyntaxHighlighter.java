package org.coursera.courier.codestyle;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.CourierLexerAdapter;
import org.coursera.courier.psi.CourierElementType;
import org.coursera.courier.psi.CourierTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

// TODO(jbetz): The layout of this code is from the intelliJ tutorial, but it's super redundant.
// Restructure this reduce all the redundancy.
public class CourierSyntaxHighlighter extends SyntaxHighlighterBase {
  public static final TextAttributesKey IDENTIFIER = createTextAttributesKey("COURIER_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
  public static final TextAttributesKey KEYWORD = createTextAttributesKey("COURIER_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey DOC_COMMENT = createTextAttributesKey("COURIER_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT);
  public static final TextAttributesKey STRING = createTextAttributesKey("COURIER_STRING", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey NUMBER = createTextAttributesKey("COURIER_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey BUILTIN_TYPE_NAME = createTextAttributesKey("COURIER_BUILTIN_TYPE_NAME", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
  public static final TextAttributesKey COMMENT = createTextAttributesKey("COURIER_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
  public static final TextAttributesKey TYPE_NAME = createTextAttributesKey("COURIER_TYPE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME);
  public static final TextAttributesKey TYPE_REFERENCE = createTextAttributesKey("COURIER_TYPE_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
  public static final TextAttributesKey FIELD = createTextAttributesKey("COURIER_FIELD", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
  public static final TextAttributesKey ENUM_SYMBOL = createTextAttributesKey("COURIER_ENUM_SYMBOL", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
  public static final TextAttributesKey PROPERTY = createTextAttributesKey("COURIER_PROPERTY", DefaultLanguageHighlighterColors.METADATA);
  public static final TextAttributesKey COLON = createTextAttributesKey("COURIER_COLON", DefaultLanguageHighlighterColors.OPERATION_SIGN);
  public static final TextAttributesKey OPTIONAL = createTextAttributesKey("COURIER_OPTIONAL", DefaultLanguageHighlighterColors.SEMICOLON);
  public static final TextAttributesKey BRACES = createTextAttributesKey("COURIER_BRACES", DefaultLanguageHighlighterColors.BRACES);
  public static final TextAttributesKey BRACKETS = createTextAttributesKey("COURIER_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
  public static final TextAttributesKey PARENTHESES = createTextAttributesKey("COURIER_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
  public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("COURIER_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

  private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[]{IDENTIFIER};
  private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
  private static final TextAttributesKey[] DOC_COMMENT_KEYS = new TextAttributesKey[]{DOC_COMMENT};
  private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
  private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
  private static final TextAttributesKey[] BUILTIN_TYPE_NAME_KEYS = new TextAttributesKey[]{BUILTIN_TYPE_NAME};
  private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
  private static final TextAttributesKey[] COLON_KEYS = new TextAttributesKey[]{COLON};
  private static final TextAttributesKey[] OPTIONAL_KEYS = new TextAttributesKey[]{OPTIONAL};
  private static final TextAttributesKey[] BRACES_KEYS = new TextAttributesKey[]{BRACES};
  private static final TextAttributesKey[] BRACKETS_KEYS = new TextAttributesKey[]{BRACKETS};
  private static final TextAttributesKey[] PARENTHESES_KEYS = new TextAttributesKey[]{PARENTHESES};
  private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
  private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

  private static final Set<IElementType> KEYWORDS = new HashSet<IElementType>();
  static {
    KEYWORDS.add(CourierTypes.NAMESPACE_KEYWORD);
    KEYWORDS.add(CourierTypes.IMPORT_KEYWORD);
    KEYWORDS.add(CourierTypes.ENUM_KEYWORD);
    KEYWORDS.add(CourierTypes.FIXED_KEYWORD);
    KEYWORDS.add(CourierTypes.RECORD_KEYWORD);
    KEYWORDS.add(CourierTypes.TYPEREF_KEYWORD);
    KEYWORDS.add(CourierTypes.TRUE);
    KEYWORDS.add(CourierTypes.FALSE);
    KEYWORDS.add(CourierTypes.NIL);
    KEYWORDS.add(CourierTypes.NULL);
  }

  private static final Set<IElementType> BUILTIN_TYPES = new HashSet<IElementType>();
  static {
    BUILTIN_TYPES.add(CourierTypes.MAP_KEYWORD);
    BUILTIN_TYPES.add(CourierTypes.ARRAY_KEYWORD);
    BUILTIN_TYPES.add(CourierTypes.UNION_KEYWORD);
  }

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new CourierLexerAdapter();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    if (KEYWORDS.contains(tokenType)) {
      return KEYWORD_KEYS;
    } else if (
      tokenType.equals(CourierElementType.DOC_COMMENT)) {
      return DOC_COMMENT_KEYS;
    } else if (tokenType.equals(CourierTypes.STRING)) {
      return STRING_KEYS;
    } else if (tokenType.equals(CourierTypes.NUMBER_LITERAL)) {
      return NUMBER_KEYS;
    } else if (BUILTIN_TYPES.contains(tokenType)) {
      return BUILTIN_TYPE_NAME_KEYS;
    } else if (
      tokenType.equals(CourierTypes.SINGLE_LINE_COMMENT) ||
      tokenType.equals(CourierTypes.BLOCK_COMMENT_EMPTY) ||
      tokenType.equals(CourierTypes.BLOCK_COMMENT_NON_EMPTY) ||
      tokenType.equals(CourierTypes.COMMA)) {
      return COMMENT_KEYS;
    } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
      return BAD_CHAR_KEYS;
    } else if (tokenType.equals(CourierTypes.COLON)) {
      return COLON_KEYS;
    } else if (tokenType.equals(CourierTypes.QUESTION_MARK)) {
      return OPTIONAL_KEYS;
    } else if (tokenType.equals(CourierTypes.OPEN_BRACE) || tokenType.equals(CourierTypes.CLOSE_BRACE)) {
      return BRACES_KEYS;
    } else if (tokenType.equals(CourierTypes.OPEN_BRACKET) || tokenType.equals(CourierTypes.CLOSE_BRACKET)) {
      return BRACKETS_KEYS;
    } else if (tokenType.equals(CourierTypes.OPEN_PAREN) || tokenType.equals(CourierTypes.CLOSE_PAREN)) {
      return PARENTHESES_KEYS;
    } else if (tokenType.equals(CourierTypes.IDENTIFIER)) {
      return IDENTIFIER_KEYS;
    } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
      return BAD_CHAR_KEYS;
    } else {
      return EMPTY_KEYS;
    }
  }
}
