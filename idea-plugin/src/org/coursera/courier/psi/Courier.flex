package org.coursera.courier;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.psi.CourierTypes;
import com.intellij.psi.TokenType;
import org.coursera.courier.psi.CourierElementType;

%%

%class CourierLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

LineTerminator = \n|\r|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]
Comma = ","

BlockCommentEmpty = "/**/"
BlockCommentNonEmpty = "/*" [^*] ~ "*/"
// Comment can be the last line of the file, without line terminator.
LineComment = "//" {InputCharacter}* {LineTerminator}?

//SchemadocStart = "/**"
//SchemadocContent = ( [^*] | \*+ [^/*] )*
//SchemadocEnd = "*/"

// Based on _JavaLexer.flex:
DOC_COMMENT="/*""*"+("/"|([^"/""*"]{COMMENT_TAIL}))?
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?

Identifier = [A-Za-z_] [A-Za-z0-9_]* // Avro/Pegasus identifiers

EscapedIdentifier = "`" {Identifier} "`"

// From json.org:
NonNegativeIntegerLiteral = 0 | [1-9][0-9]*
IntegerLiteral = -? {NonNegativeIntegerLiteral}
NumberLiteral = {IntegerLiteral} (\.[0-9]+)? ([eE][+-]?[0-9]+)?
StringLiteral = \" ( [^\"\\] | \\ ( [\"\\/bfnrt] | u[0-9]{4} ) )* \"

%state SCHEMADOC_CONT

%%

<YYINITIAL> {
  /* keywords */
  "namespace"            { return CourierTypes.NAMESPACE_KEYWORD; }
  "import"               { return CourierTypes.IMPORT_KEYWORD; }
  "record"               { return CourierTypes.RECORD_KEYWORD; }
  "key"                  { return CourierTypes.KEY_KEYWORD; }
  "enum"                 { return CourierTypes.ENUM_KEYWORD; }
  "fixed"                { return CourierTypes.FIXED_KEYWORD; }
  "typeref"              { return CourierTypes.TYPEREF_KEYWORD; }
  "union"                { return CourierTypes.UNION_KEYWORD; }
  "map"                  { return CourierTypes.MAP_KEYWORD; }
  "array"                { return CourierTypes.ARRAY_KEYWORD; }

  "null"                 { return CourierTypes.NULL; }
  "true"                 { return CourierTypes.TRUE; }
  "false"                { return CourierTypes.FALSE; }

  "("                    { return CourierTypes.OPEN_PAREN; }
  ")"                    { return CourierTypes.CLOSE_PAREN; }
  "{"                    { return CourierTypes.OPEN_BRACE; }
  "}"                    { return CourierTypes.CLOSE_BRACE; }
  "["                    { return CourierTypes.OPEN_BRACKET; }
  "]"                    { return CourierTypes.CLOSE_BRACKET; }
  ":"                    { return CourierTypes.COLON; }
  "="                    { return CourierTypes.EQUALS; }
  "@"                    { return CourierTypes.AT; }
  "..."                  { return CourierTypes.DOTDOTDOT; }
  "."                    { return CourierTypes.DOT; }
  "?"                    { return CourierTypes.QUESTION_MARK; }
  //","                  { return CourierTypes.COMMA; }

  /* identifiers */
  {Identifier}           { return CourierTypes.IDENTIFIER; }
  {EscapedIdentifier}    { return CourierTypes.IDENTIFIER; }

  /* literals */
  {NumberLiteral}        { return CourierTypes.NUMBER_LITERAL; }
  {StringLiteral}        { return CourierTypes.STRING; }
}

{DOC_COMMENT}? { yybegin(YYINITIAL); return CourierElementType.DOC_COMMENT; }


{LineComment}            { yybegin(YYINITIAL); return CourierTypes.SINGLE_LINE_COMMENT; }
{BlockCommentEmpty}      { yybegin(YYINITIAL); return CourierTypes.BLOCK_COMMENT_EMPTY; }
{BlockCommentNonEmpty}   { yybegin(YYINITIAL); return CourierTypes.BLOCK_COMMENT_NON_EMPTY; }

// We render insignificant commas as a comment
{Comma}                  { yybegin(YYINITIAL); return CourierTypes.COMMA; }

{WhiteSpace}+            { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

/* error fallback */
[^]                      { return TokenType.BAD_CHARACTER; }
