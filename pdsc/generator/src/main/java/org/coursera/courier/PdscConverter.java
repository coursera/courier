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

package org.coursera.courier;


import com.linkedin.data.DataMap;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.coursera.courier.grammar.CourierLexer;
import org.coursera.courier.grammar.CourierParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * .courier to .pdsc transformer
 */
public class PdscConverter {

  public static void main(String[] args) throws Throwable {

    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Usage: targetPath sourcePath1[:sourcePath2]+");
    }
    String targetPath = args[0];
    String sourcePathString = args[1];
    String[] sourcePaths = sourcePathString.split(":");
    for (String path : sourcePaths) {
      convert(path).forEach(thing -> System.out.println(thing.pdsc));
    }
  }

  private static class PdscUnit {
    String name;
    String namespace;
    DataMap pdsc;
    PdscUnit(DataMap pdsc) {
      this.name = pdsc.get("name").toString();
      this.namespace = pdsc.get("namespace").toString();
      this.pdsc = pdsc;
    }
  }

  private static List<PdscUnit> convert(String sourcePath) throws IOException {
    Reader reader = new FileReader(sourcePath);
    List<PdscUnit> result = convert(reader);
    reader.close();
    return result;
  }


  private static List<PdscUnit> convert(Reader reader) throws IOException {
    return CourierDocumentToPdscConverter.convert(reader)
            .stream()
            .map(pdsc -> new PdscUnit(pdsc))
            .collect(Collectors.toList());
  }

}
