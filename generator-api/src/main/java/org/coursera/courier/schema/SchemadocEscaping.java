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

package org.coursera.courier.schema;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.WordUtils;

public class SchemadocEscaping {
  /**
   * Returns a schemadoc comment, as a string of java source, for the given documentation string
   * and deprecated property.
   *
   * @param doc provides the schemadoc for the symbol, if any.  May be null.
   * @return an escaped schemadoc string.
   */
  public static String stringToSchemadoc(String doc) {
    if (doc == null || doc.trim().isEmpty()) return "";

    String schemadoc = wrap(escape(doc)).replaceAll("\\n", "\n * ");
    return
      "/**\n" +
      (" * " + schemadoc + "\n") +
      " */";
  }

  private static int WRAP_HIGH_WATERMARK = 180;
  private static int WRAP_TARGET_LINE_LENGTH = 100;

  private static String wrap(String text) {
    StringBuilder builder = new StringBuilder();
    for (String line: text.split("\n")) {
      if (line.length() > WRAP_HIGH_WATERMARK) {
        builder.append(WordUtils.wrap(line, WRAP_TARGET_LINE_LENGTH));
      } else {
        builder.append(text);
      }
      builder.append("\n");
    }
    return builder.toString().trim();
  }

  private static String escape(String raw) {
    String htmlEscaped = StringEscapeUtils.escapeHtml4(raw);

    // escape "/*" and "*/" by replacing all slashes and asterisks with the entities
    return htmlEscaped
        .replace("/*", "&#47;&#42;")
        .replace("*/", "&#42;&#47;");
  }
}
