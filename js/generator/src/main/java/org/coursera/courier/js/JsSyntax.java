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

package org.coursera.courier.js;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec;
import org.coursera.courier.api.ClassTemplateSpecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides utilities for code generating js source code (ES6 to be exact).
 */
public class JsSyntax {

  // ES6
  private static final Set<String> keywords = new HashSet<String>(Arrays.asList(new String[] {
    "do", "if", "in", "for", "let", "new", "try", "var", "case", "else", "enum", "eval", "null",
    "this", "true", "void", "with", "await", "break", "catch", "class", "const", "false", "super",
    "throw", "while", "yield", "delete", "export", "import", "public", "return", "static", "switch",
    "typeof", "default", "extends", "finally", "package", "private", "continue", "debugger",
    "function", "arguments", "interface", "protected", "implements", "instanceof"
  }));

  /**
   * Returns the escaped Pegasus symbol for use in JS source code.
   *
   * Pegasus symbols must be of the form [A-Za-z_], so this routine simply checks if the
   * symbol collides with a js keyword, and if so, escapes it.
   *
   * (Because only fields are generated, symbols like hashCode do not collide with method names
   * from Object and may be used).
   *
   * @param symbol the symbol to escape
   * @return the escaped Pegasus symbol.
   */
  public static String escapeKeyword(String symbol) {
    if (keywords.contains(symbol)) {
      return symbol + "$";
    } else {
      return symbol;
    }
  }

  public static List<String> imports(ClassTemplateSpec spec) {
    List<String> imports = new ArrayList<>();
    for (ClassTemplateSpec referenced: ClassTemplateSpecs.directReferencedTypes(spec)) {
      DataSchema.Type type = referenced.getSchema().getDereferencedType();
      if (type == DataSchema.Type.ARRAY || type == DataSchema.Type.MAP || type == DataSchema.Type.FIXED) {
        continue; // TODO: properly compute referenced types for case where collection types are not reified
      }
      if (referenced != null && referenced.getClassName() != null) {
        imports.add(referenced.getClassName());
      }
    }
    Collections.sort(imports);
    return imports;
  }

  public static String propType(ClassTemplateSpec spec, boolean optional) {
    return propType(spec) + (optional ? "" : ".isRequired");
  }

  public static String propType(ClassTemplateSpec spec) {
    DataSchema schema = spec.getSchema();
    DataSchema.Type type = schema.getType();
    switch (type) {
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
        return "React.PropTypes.number";
      case ARRAY:
        return "React.PropTypes.array"; // TODO: Switch to React.PropTypes.arrayOf(<item type>)
      case MAP:
        return "React.PropTypes.object"; // TODO: Switch to React.PropTypes.objectOf(<value type>)
      case BOOLEAN:
        return "React.PropTypes.bool";
      case STRING:
      case BYTES:
      case FIXED:
        return "React.PropTypes.string";
      case RECORD:
      case ENUM:
      case UNION:
      case TYPEREF:
        return escapeKeyword(spec.getClassName());
      default:
        throw new IllegalArgumentException("Unsupported type: " + type.name());
    }
  }
}
