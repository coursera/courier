package org.coursera.courier.schemadoc;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.schemadoc.psi.SchemadocTypes;
import com.intellij.psi.TokenType;
%%

%class SchemadocLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

WHITE_SPACE=[\ \t\f\r\n]
DOC_COMMENT_START = "/**"
DOC_COMMENT_CONTENT = ( [^*\ \t\f\r\n] )*
DOC_COMMENT_ASTRISK = "*"
DOC_COMMENT_END = "*/"

%state YYDOC_COMMENT

%%

<YYDOC_COMMENT> {
  {DOC_COMMENT_END}     { yybegin(YYINITIAL); return SchemadocTypes.DOC_COMMENT_END; }
  {WHITE_SPACE}+        { yybegin(YYDOC_COMMENT); return TokenType.WHITE_SPACE; }
  {DOC_COMMENT_ASTRISK} { yybegin(YYDOC_COMMENT); return SchemadocTypes.DOC_COMMENT_CONTENT; }
  {DOC_COMMENT_CONTENT} { yybegin(YYDOC_COMMENT); return SchemadocTypes.DOC_COMMENT_CONTENT; }
}

<YYINITIAL> {DOC_COMMENT_START}     { yybegin(YYDOC_COMMENT); return SchemadocTypes.DOC_COMMENT_START; }
{WHITE_SPACE}+          { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
[^]                     { return TokenType.BAD_CHARACTER; }
