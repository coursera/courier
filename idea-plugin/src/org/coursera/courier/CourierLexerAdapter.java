package org.coursera.courier;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class CourierLexerAdapter extends FlexAdapter {
  public CourierLexerAdapter() {
    super(new CourierLexer((Reader) null));
  }
}
