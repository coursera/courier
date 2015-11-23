package org.coursera.courier;

import com.intellij.lexer.FlexAdapter;

public class CourierLexerAdapter extends FlexAdapter {
  public CourierLexerAdapter() {
    super(new CourierLexer(null));
  }
}
