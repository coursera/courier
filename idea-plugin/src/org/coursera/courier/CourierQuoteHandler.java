package org.coursera.courier;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.TokenType;
import org.coursera.courier.psi.CourierTypes;

public class CourierQuoteHandler extends SimpleTokenSetQuoteHandler {
  public CourierQuoteHandler() {
    super(CourierTypes.STRING, TokenType.BAD_CHARACTER);
  }

  @Override
  public boolean hasNonClosedLiteral(Editor editor, HighlighterIterator iterator, int offset) {
    return true;
  }
}
