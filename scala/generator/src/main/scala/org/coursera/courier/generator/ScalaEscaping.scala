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

package org.coursera.courier.generator

object ScalaEscaping {
  // Avro's naming rules (https://avro.apache.org/docs/1.7.7/spec.html#Names) are fairly strict, and
  // we only need to escape valid avro names, which makes this fairly straight-forward.
  //
  // This keyword list is based on:
  // http://stackoverflow.com/questions/22037430/
  //   programatically-checking-whether-a-string-is-a-reserved-word-in-scala
  val keywords = Set(
    "abstract", "true", "val", "do", "throw", "package", "macro", "object", "false",
    "this", "if", "then", "var", "trait", ".", "catch", "with", "def", "else", "class", "type",
    "lazy", "null", "override", "protected", "private", "sealed", "finally", "new",
    "implicit", "extends", "final", "for", "return", "case", "import", "forSome", "super",
    "while", "yield", "try", "match")

  def escapeSymbol(symbol: String): String = {
    if (keywords.contains(symbol)) {
      s"`$symbol`"
    } else {
      symbol
    }
  }

  def escapeStringLiteral(str: String): String = tripleQuoted(str)

  private def tripleQuoted(str: String) = "\"\"\"" + escapeTripleQuotedStrings(str) + "\"\"\""

  // There must be a better way to escape """ in a string literal, but here's what I've got.
  //
  // 1) close a triple quoted string
  // 2) emits 3 quotes in using a normal string
  // 3) open a new triple quoted string
  //
  // (""" + "\"\"\"" + """)
  //
  // So a string like 'before """ after' would become 'before " + "\"\"\"" + " after' and can
  // safely be put into a string literal in Scala source
  private val escapedTripleQuote = "\"\"\" + \"\\\\\"\\\\\"\\\\\"\" + \"\"\""
  private val unescapedTripleQuoteRegex = "\"\"\"".r

  private def escapeTripleQuotedStrings(str: String): String = {
    unescapedTripleQuoteRegex.replaceAllIn(str, escapedTripleQuote)
  }
}
