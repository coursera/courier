package org.coursera.courier;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.psi.TokenType;
import org.coursera.courier.psi.CourierTypes;

public class CourierQuoteHandler extends SimpleTokenSetQuoteHandler {
  public CourierQuoteHandler() {
    super(CourierTypes.STRING, TokenType.BAD_CHARACTER);
  }
}
