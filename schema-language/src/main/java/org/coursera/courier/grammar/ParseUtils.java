/*
 * Copyright 2015 Coursera Inc.
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

package org.coursera.courier.grammar;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

public class ParseUtils {
  public static String extractMarkdown(String schemaDoc) {
    return unescapeDocstring(
      stripMargin(schemaDoc.substring(3, schemaDoc.length() - 2)).trim());
  }

  private static String unescapeDocstring(String escaped) {
    // unescape "/*" and "*/"
    String commentUnescaped = escaped
      .replace("&#47;&#42;", "/*")
      .replace("&#42;&#47;", "*/");
    return StringEscapeUtils.unescapeHtml4(commentUnescaped);
  }

  public static String extractString(String stringLiteral) {
    return StringEscapeUtils
      .unescapeJson(stringLiteral.substring(1, stringLiteral.length() - 1));
  }

  public static String stripMargin(String schemadoc) {
    char marginChar = '*';
    StringBuilder buf = new StringBuilder();
    for (String line : schemadoc.split("\n")) {
      int len = line.length();
      int index = 0;
      while (index < len && line.charAt(index) <= ' ') {
        index++;
      }

      if (index < len && line.charAt(index) == marginChar) {
        buf.append(line.substring(index + 1));
      } else {
        buf.append(line);
      }
    }
    return buf.toString();
  }

  public static String stripEscaping(String identifier) {
    return identifier.substring(1, identifier.length() - 1);
  }

  public static String join(List<CourierParser.IdentifierContext> identifiers) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<CourierParser.IdentifierContext> iter = identifiers.iterator();
    while (iter.hasNext()) {
      stringBuilder.append(iter.next().value);
      if (iter.hasNext()) {
        stringBuilder.append(".");
      }
    }
    return stringBuilder.toString();
  }

  // There must be a better way.
  public static Number toNumber(String string) {
    BigDecimal value = new BigDecimal(string);
    try {
      long l = value.longValueExact();
      int i = (int) l;
      if ((long) i == l) {
        return i;
      } else {
        return l;
      }
    } catch (ArithmeticException e) {
      double d = value.doubleValue();
      if (BigDecimal.valueOf(d).equals(value)) {
        float f = (float) d;
        if ((double) f == d) {
          return (float) d;
        } else {
          return d;
        }
      } else {
        return null;
      }
    }
  }
}
