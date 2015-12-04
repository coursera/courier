package org.coursera.courier.swift;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;

import java.io.IOException;
import java.io.Writer;

public class SwiftStringEscaper {

  /**
   * @param value provides the swift identifier to escape.
   *
   * @return the contents of a Swift source code string literal that represents the given string.  Surrounding
   * quotes are not appended.
   */
  public static String escape(String value) {
    return ESCAPE_SWIFT.translate(value);
  }

  // https://developer.apple.com/library/ios/documentation/Swift/Conceptual/Swift_Programming_Language/StringsAndCharacters.html
  private static final String[][] SWIFT_CTRL_CHARS_ESCAPE = {
      {"\"", "\\\""},
      {"\\", "\\\\"},
      {"\b", "\\u{8}"},
      {"\n", "\\n"},
      {"\t", "\\t"},
      {"\f", "\\u{C}"},
      {"\r", "\\r"}
  };

  private static final CharSequenceTranslator ESCAPE_SWIFT =
      new LookupTranslator(
          SWIFT_CTRL_CHARS_ESCAPE
      ).with(
          SwiftUnicodeEscaper.outsideOf(32, 0x7f)
      );

  // Apache lang based unicode escaper for Swift
  private static final class SwiftUnicodeEscaper extends UnicodeEscaper {
    private final int below;
    private final int above;
    private final boolean between;

    public static SwiftUnicodeEscaper outsideOf(final int codepointLow, final int codepointHigh) {
      return new SwiftUnicodeEscaper(codepointLow, codepointHigh, false);
    }

    protected SwiftUnicodeEscaper(final int below, final int above, final boolean between) {
      super(below, above, between);
      this.below = below;
      this.above = above;
      this.between = between;
    }

    @Override
    public boolean translate(final int codepoint, final Writer out) throws IOException {
      if (between) {
        if (codepoint < below || codepoint > above) {
          return false;
        }
      } else {
        if (codepoint >= below && codepoint <= above) {
          return false;
        }
      }

      // TODO: Handle potential + sign per various Unicode escape implementations
      if (codepoint > 0xffff) {
        out.write(toUtf16Escape(codepoint));
      } else if (codepoint > 0xfff) {
        out.write("\\u{" + hex(codepoint) + "}");
      } else if (codepoint > 0xff) {
        out.write("\\u{0" + hex(codepoint) + "}");
      } else if (codepoint > 0xf) {
        out.write("\\u{00" + hex(codepoint) + "}");
      } else {
        out.write("\\u{000" + hex(codepoint) + "}");
      }
      return true;
    }
  }
}
