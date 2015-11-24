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

%state COMMENT_DATA_START
%state COMMENT_DATA

WHITE_DOC_SPACE_CHAR=[\ \t\f\n\r]
WHITE_DOC_SPACE_NO_LR=[\ \t\f]

%%

// Based on _JavaDocLexer.flex
<YYINITIAL> "/**" { yybegin(COMMENT_DATA_START); return SchemadocTypes.DOC_COMMENT_START; }
<COMMENT_DATA_START> {WHITE_DOC_SPACE_CHAR}+ { return TokenType.WHITE_SPACE; }
<COMMENT_DATA>  {WHITE_DOC_SPACE_NO_LR}+ { return SchemadocTypes.DOC_COMMENT_CONTENT; }
<COMMENT_DATA>  [\n\r]+{WHITE_DOC_SPACE_CHAR}* { return TokenType.WHITE_SPACE; }
<COMMENT_DATA_START, COMMENT_DATA> . { yybegin(COMMENT_DATA); return SchemadocTypes.DOC_COMMENT_CONTENT; }
"*"+"/" { return SchemadocTypes.DOC_COMMENT_END; }
[^] { return TokenType.BAD_CHARACTER; }

//
//WHITE_SPACE=[\ \t\f\r\n]
//DOC_COMMENT_START = "/**"
//DOC_COMMENT_CONTENT = ( [^*\ \t\f\r\n] )*
//DOC_COMMENT_ASTRISKS = "*"+
//DOC_COMMENT_END = "*/"
//
//%state YYDOC_COMMENT
//
//%%
//
//<YYDOC_COMMENT> {
//  {DOC_COMMENT_END}      { yybegin(YYINITIAL); return SchemadocTypes.DOC_COMMENT_END; }
//  {WHITE_SPACE}+         { yybegin(YYDOC_COMMENT); return TokenType.WHITE_SPACE; }
//  {DOC_COMMENT_ASTRISKS} { yybegin(YYDOC_COMMENT); return SchemadocTypes.DOC_COMMENT_CONTENT; }
//  {DOC_COMMENT_CONTENT}  { yybegin(YYDOC_COMMENT); return SchemadocTypes.DOC_COMMENT_CONTENT; }
//}
//
//<YYINITIAL> {DOC_COMMENT_START} { yybegin(YYDOC_COMMENT); return SchemadocTypes.DOC_COMMENT_START; }
//{WHITE_SPACE}+                  { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
//[^]                             { return TokenType.BAD_CHARACTER; }
