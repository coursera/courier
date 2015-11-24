package org.coursera.courier.schemadoc;

import com.intellij.lexer.FlexAdapter;

public class SchemadocLexerAdapter extends FlexAdapter {
  public SchemadocLexerAdapter() {
    super(new SchemadocLexer(null));
  }
}
