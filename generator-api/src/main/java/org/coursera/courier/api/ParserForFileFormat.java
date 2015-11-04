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

package org.coursera.courier.api;

import com.linkedin.data.schema.SchemaParserFactory;

/**
 * Identifies the SchemaParserFactory that should be used for a particular file format (e.g.
 * ".pdsc" or ".courier").
 */
public class ParserForFileFormat {

  /**
   * File extension without a preceding ".".  E.g. "pdsc", "courier".
   */
  public final String fileExtension;
  public final SchemaParserFactory parserFactory;

  public ParserForFileFormat(String fileExtension, SchemaParserFactory parserFactory) {
    assert !fileExtension.contains(".");
    this.fileExtension = fileExtension;
    this.parserFactory = parserFactory;
  }
}
