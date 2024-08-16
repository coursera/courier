/*
 * Copyright 2016 Coursera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.coursera.courier.schemadoc;

import com.intellij.lexer.LexerBase;
import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.text.CharArrayUtil;
import org.coursera.courier.schemadoc.psi.SchemadocTypes;

import java.io.IOException;

/**
 * Based on JavaDocLexer.java.  This lexer wraps doccomment.flex, tokenizing the contents
 * of a doc comment correctly.
 */
public class SchemadocLexer extends MergingLexerAdapter {
  public SchemadocLexer() {
    super(new AsteriskStripperLexer(new DocCommentLexer()),
      TokenSet.create(SchemadocTypes.DOC_COMMENT_CONTENT, TokenType.WHITE_SPACE));
  }

  private static class AsteriskStripperLexer extends LexerBase {
    private final DocCommentLexer myFlex;
    private CharSequence myBuffer;
    private int myBufferIndex;
    private int myBufferEndOffset;
    private int myTokenEndOffset;
    private int myState;
    private IElementType myTokenType;
    private boolean myAfterLineBreak;
    private boolean myInLeadingSpace;

    public AsteriskStripperLexer(final DocCommentLexer flex) {
      myFlex = flex;
    }

    public final void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
      myBuffer = buffer;
      myBufferIndex =  startOffset;
      myBufferEndOffset = endOffset;
      myTokenType = null;
      myTokenEndOffset = startOffset;
      myFlex.reset(myBuffer, startOffset, endOffset, initialState);
    }

    public int getState() {
      return myState;
    }

    public CharSequence getBufferSequence() {
      return myBuffer;
    }

    public int getBufferEnd() {
      return myBufferEndOffset;
    }

    public final IElementType getTokenType() {
      locateToken();
      return myTokenType;
    }

    public final int getTokenStart() {
      locateToken();
      return myBufferIndex;
    }

    public final int getTokenEnd() {
      locateToken();
      return myTokenEndOffset;
    }


    public final void advance() {
      locateToken();
      myTokenType = null;
    }

    protected final void locateToken() {
      if (myTokenType != null) return;
      _locateToken();

      if (myTokenType == TokenType.WHITE_SPACE) {
        myAfterLineBreak = CharArrayUtil.containLineBreaks(myBuffer, getTokenStart(), getTokenEnd());
      }
    }

    private void _locateToken() {
      if (myTokenEndOffset == myBufferEndOffset) {
        myTokenType = null;
        myBufferIndex = myBufferEndOffset;
        return;
      }

      myBufferIndex = myTokenEndOffset;

      if (myAfterLineBreak) {
        myAfterLineBreak = false;
        while (myTokenEndOffset < myBufferEndOffset && myBuffer.charAt(myTokenEndOffset) == '*' &&
          (myTokenEndOffset + 1 >= myBufferEndOffset || myBuffer.charAt(myTokenEndOffset + 1) != '/')) {
          myTokenEndOffset++;
        }

        myInLeadingSpace = true;
        if (myBufferIndex < myTokenEndOffset) {
          myTokenType = SchemadocTypes.DOC_COMMENT_LEADING_ASTRISK;
          return;
        }
      }

      if (myInLeadingSpace) {
        myInLeadingSpace = false;
        boolean lf = false;
        while (myTokenEndOffset < myBufferEndOffset && Character.isWhitespace(myBuffer.charAt(myTokenEndOffset))) {
          if (myBuffer.charAt(myTokenEndOffset) == '\n') lf = true;
          myTokenEndOffset++;
        }

        final int state = myFlex.yystate();
        if (state == DocCommentLexer.COMMENT_DATA || myTokenEndOffset < myBufferEndOffset) {
          myFlex.yybegin(DocCommentLexer.COMMENT_DATA_START);
        }

        if (myBufferIndex < myTokenEndOffset) {
          myTokenType = lf
            ? TokenType.WHITE_SPACE
            : SchemadocTypes.DOC_COMMENT_CONTENT;

          return;
        }
      }

      flexLocateToken();
    }

    private void flexLocateToken() {
      try {
        myState = myFlex.yystate();
        myFlex.goTo(myBufferIndex);
        myTokenType = myFlex.advance();
        myTokenEndOffset = myFlex.getTokenEnd();
      }
      catch (IOException e) {
        // Can't be
      }
    }
  }
}
