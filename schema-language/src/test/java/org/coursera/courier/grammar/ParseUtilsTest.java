/*
 Copyright 2015 Coursera Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.coursera.courier.grammar;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParseUtilsTest {

  // ---------------------------------------------------------------------------
  // toNumber — additional edge cases
  // ---------------------------------------------------------------------------

  @Test
  public void toNumber_intMaxValue_returnsInteger() {
    Number result = ParseUtils.toNumber("2147483647");
    Assert.assertTrue("Expected Integer", result instanceof Integer);
    Assert.assertEquals(Integer.MAX_VALUE, result.intValue());
  }

  @Test
  public void toNumber_intMinValue_returnsInteger() {
    Number result = ParseUtils.toNumber("-2147483648");
    Assert.assertTrue("Expected Integer", result instanceof Integer);
    Assert.assertEquals(Integer.MIN_VALUE, result.intValue());
  }

  @Test
  public void toNumber_longMaxValue_returnsLong() {
    Number result = ParseUtils.toNumber("9223372036854775807");
    Assert.assertTrue("Expected Long", result instanceof Long);
    Assert.assertEquals(Long.MAX_VALUE, result.longValue());
  }

  @Test
  public void toNumber_highPrecisionDecimal_returnsNull() {
    // A decimal with more significant digits than a double can represent exactly:
    // BigDecimal.valueOf(d) will not equal the original BigDecimal value.
    Number result = ParseUtils.toNumber("1.23456789012345678901234567890123456789");
    Assert.assertNull("Expected null for high-precision decimal that cannot round-trip", result);
  }

  // ---------------------------------------------------------------------------
  // toNumber
  // ---------------------------------------------------------------------------

  @Test
  public void toNumber_smallInt_returnsInteger() {
    Number result = ParseUtils.toNumber("42");
    Assert.assertTrue("Expected Integer, got: " + result.getClass(), result instanceof Integer);
    Assert.assertEquals(42, result.intValue());
  }

  @Test
  public void toNumber_negativeInt_returnsInteger() {
    Number result = ParseUtils.toNumber("-7");
    Assert.assertTrue("Expected Integer", result instanceof Integer);
    Assert.assertEquals(-7, result.intValue());
  }

  @Test
  public void toNumber_zero_returnsInteger() {
    Number result = ParseUtils.toNumber("0");
    Assert.assertTrue("Expected Integer", result instanceof Integer);
    Assert.assertEquals(0, result.intValue());
  }

  @Test
  public void toNumber_longValue_returnsLong() {
    // 3_000_000_000 > Integer.MAX_VALUE (2_147_483_647)
    Number result = ParseUtils.toNumber("3000000000");
    Assert.assertTrue("Expected Long, got: " + result.getClass(), result instanceof Long);
    Assert.assertEquals(3_000_000_000L, result.longValue());
  }

  @Test
  public void toNumber_exactFloat_returnsFloat() {
    // 1.5 is representable exactly as both float and double
    Number result = ParseUtils.toNumber("1.5");
    Assert.assertTrue("Expected Float, got: " + result.getClass(), result instanceof Float);
    Assert.assertEquals(1.5f, result.floatValue(), 0.0f);
  }

  @Test
  public void toNumber_doubleOnly_returnsDouble() {
    // 0.1 has an exact decimal BigDecimal representation but not an exact float
    Number result = ParseUtils.toNumber("0.1");
    Assert.assertTrue("Expected Double, got: " + result.getClass(), result instanceof Double);
    Assert.assertEquals(0.1, result.doubleValue(), 1e-15);
  }

  @Test
  public void toNumber_largeDouble_returnsDouble() {
    // 1.5e100 exceeds Float.MAX_VALUE so it can't fit in a float
    Number result = ParseUtils.toNumber("1.5e100");
    Assert.assertTrue("Expected Double, got: " + result.getClass(), result instanceof Double);
    Assert.assertEquals(1.5e100, result.doubleValue(), 0.0);
  }

  // ---------------------------------------------------------------------------
  // extractString
  // ---------------------------------------------------------------------------

  @Test
  public void extractString_simpleQuoted_stripsQuotes() {
    Assert.assertEquals("hello", ParseUtils.extractString("\"hello\""));
  }

  @Test
  public void extractString_withEscape_unescapesJson() {
    // JSON \n should become a real newline
    Assert.assertEquals("a\nb", ParseUtils.extractString("\"a\\nb\""));
  }

  @Test
  public void extractString_emptyString_returnsEmpty() {
    Assert.assertEquals("", ParseUtils.extractString("\"\""));
  }

  // ---------------------------------------------------------------------------
  // stripMargin
  // ---------------------------------------------------------------------------

  @Test
  public void stripMargin_singleLineWithStar_stripsLeadingStar() {
    // " * content" → " content"
    Assert.assertEquals(" content", ParseUtils.stripMargin(" * content"));
  }

  @Test
  public void stripMargin_lineWithoutStar_leftUnchanged() {
    Assert.assertEquals("no star here", ParseUtils.stripMargin("no star here"));
  }

  @Test
  public void stripMargin_emptyString_returnsEmpty() {
    Assert.assertEquals("", ParseUtils.stripMargin(""));
  }

  @Test
  public void stripMargin_multiLine_stripsStarsFromEachLine() {
    // Simulates a real Courier/Scaladoc multi-line comment body after the opening /** and before */
    String input = " * first line\n * second line\n * third line";
    String expected = " first line second line third line";
    Assert.assertEquals(expected, ParseUtils.stripMargin(input));
  }

  @Test
  public void stripMargin_mixedLines_onlyStripLinesWithStar() {
    // A line that starts (after whitespace) without '*' is left unchanged.
    // stripMargin concatenates without re-inserting newlines.
    String input = " * has star\nno star";
    // " * has star" → " has star", "no star" → "no star", concatenated = " has starno star"
    String expected = " has starno star";
    Assert.assertEquals(expected, ParseUtils.stripMargin(input));
  }

  // ---------------------------------------------------------------------------
  // unescapeIdentifier
  // ---------------------------------------------------------------------------

  @Test
  public void unescapeIdentifier_backtickWrapped_removesBackticks() {
    Assert.assertEquals("record", ParseUtils.unescapeIdentifier("`record`"));
  }

  @Test
  public void unescapeIdentifier_noBactticks_unchanged() {
    Assert.assertEquals("normalId", ParseUtils.unescapeIdentifier("normalId"));
  }

  // ---------------------------------------------------------------------------
  // extractMarkdown
  // ---------------------------------------------------------------------------

  @Test
  public void extractMarkdown_simpleDocstring_returnsTrimmedContent() {
    // "/** hello */" → strip first 3 + last 2 → " hello " → stripMargin (no *) → " hello " → trim → "hello"
    Assert.assertEquals("hello", ParseUtils.extractMarkdown("/** hello */"));
  }

  @Test
  public void extractMarkdown_htmlEntities_unescaped() {
    // HTML entity &amp; should be unescaped to &
    Assert.assertEquals("&", ParseUtils.extractMarkdown("/**&amp;*/"));
  }

  @Test
  public void extractMarkdown_escapedCommentChars_unescaped() {
    // &#47;&#42; should become /* and &#42;&#47; should become */
    Assert.assertEquals("/* and */", ParseUtils.extractMarkdown("/**&#47;&#42; and &#42;&#47;*/"));
  }

  @Test
  public void extractMarkdown_multiLineDocstring_stripsStarsAndTrims() {
    // Simulates /** \n * line one\n * line two\n */
    String input = "/**\n * line one\n * line two\n */";
    String result = ParseUtils.extractMarkdown(input);
    Assert.assertTrue("Should contain 'line one'", result.contains("line one"));
    Assert.assertTrue("Should contain 'line two'", result.contains("line two"));
  }

  // ---------------------------------------------------------------------------
  // extractString — additional edge cases
  // ---------------------------------------------------------------------------

  @Test
  public void extractString_unicodeEscape_unescapes() {
    // JSON \u0041 is 'A'
    Assert.assertEquals("A", ParseUtils.extractString("\"\\u0041\""));
  }

  @Test
  public void extractString_tabEscape_unescapes() {
    Assert.assertEquals("\t", ParseUtils.extractString("\"\\t\""));
  }

  // ---------------------------------------------------------------------------
  // unescapeIdentifier — additional edge cases
  // ---------------------------------------------------------------------------

  @Test
  public void unescapeIdentifier_multipleBacktickPairs_removesAll() {
    // e.g. a pathological input with multiple backticks
    Assert.assertEquals("ab", ParseUtils.unescapeIdentifier("`a``b`"));
  }

  // ---------------------------------------------------------------------------
  // join
  // ---------------------------------------------------------------------------

  private static CourierParser.IdentifierContext makeIdContext(String value) {
    CourierParser.IdentifierContext ctx = new CourierParser.IdentifierContext(null, -1);
    ctx.value = value;
    return ctx;
  }

  @Test
  public void join_singleIdentifier_returnsIdentifierValue() {
    List<CourierParser.IdentifierContext> ids =
        Collections.singletonList(makeIdContext("foo"));
    Assert.assertEquals("foo", ParseUtils.join(ids));
  }

  @Test
  public void join_multipleIdentifiers_joinedWithDots() {
    List<CourierParser.IdentifierContext> ids = Arrays.asList(
        makeIdContext("org"),
        makeIdContext("example"),
        makeIdContext("MyType"));
    Assert.assertEquals("org.example.MyType", ParseUtils.join(ids));
  }

  @Test
  public void join_twoIdentifiers_joinedWithDot() {
    List<CourierParser.IdentifierContext> ids = Arrays.asList(
        makeIdContext("com"),
        makeIdContext("example"));
    Assert.assertEquals("com.example", ParseUtils.join(ids));
  }
}
