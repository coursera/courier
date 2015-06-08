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

import org.apache.commons.lang.StringEscapeUtils

object ScaladocEscaping {
  def stringToScaladoc(raw: String): Option[String] = {
    raw.trim match {
      case empty: String if empty.isEmpty => None
      case nonEmpty: String =>
        Some(s"""/**
                | * ${escape(raw).replaceAll("\\n", "\n * ")}
                | */""".stripMargin)
    }
  }

  // TODO(jbetz): escape markdown as well, or leave it as is and use it as the markup for doc
  // strings?
  def escape(raw: String): String = {
    val htmlEscaped = StringEscapeUtils.escapeHtml(raw)

    // escape "/*" and "*/" by replacing all slashes and asterisks with the entities
    htmlEscaped
        .replace("/*", "&#47;&#42;")
        .replace("*/", "&#42;&#47;")
  }
}
