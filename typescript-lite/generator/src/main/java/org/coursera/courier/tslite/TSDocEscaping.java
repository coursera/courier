/*
 * Copyright 2016 Coursera Inc.
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

package org.coursera.courier.tslite;

import org.coursera.courier.lang.DocCommentStyle;
import org.coursera.courier.lang.DocEscaping;

public class TSDocEscaping {
  /**
   * Returns a doc comment, as a string of Typescript source, for the given documentation string.
   *
   * @param doc provides the javadoc for the symbol, if any.  May be null.
   * @return an escaped doc comment string.
   */
  public static String stringToDocComment(String doc) {
    boolean emptyDoc = (doc == null || doc.trim().isEmpty());
    if (emptyDoc) return "";

    return DocEscaping.stringToDocComment(doc, DocCommentStyle.ASTRISK_MARGIN);
  }

  /**
   * Return a deprecated attribute, as a string of typescript source.
   * @param deprecatedProp provides the deprecated Pegasus schema property, if any.  May be a
   *                       Boolean, a String, or null.
   *
   * @return a Typescript doc string indicating that a type or field is deprecated.
   */
  public static String deprecatedToString(Object deprecatedProp) {
    boolean emptyDeprecated = (deprecatedProp == null);
    if (emptyDeprecated) {
      return "";
    } else if (deprecatedProp instanceof String) {
      return "/** @deprecated \"" + deprecatedProp + " */";
    } else {
      return "/** @deprecated */";
    }
  }
}
