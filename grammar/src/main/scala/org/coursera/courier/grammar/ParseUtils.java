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

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Iterator;
import java.util.List;

public class ParseUtils {
  public static String extractMarkdown(String schemaDoc) {
    return stripMargin(schemaDoc.substring(3, schemaDoc.length() -2)).trim();
  }

  public static String extractString(String stringLiteral) {
    return StringEscapeUtils
      .unescapeJson(stringLiteral.substring(1, stringLiteral.length() - 1));
  }

  public static String stripMargin(String schemadoc) {
    char marginChar = '*';
    StringBuilder buf = new StringBuilder();
    for (String line: schemadoc.split("\n")) {
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
}
