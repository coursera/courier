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

import com.linkedin.data.schema.DataSchema.Type;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.pegasus.generator.spec.*;
import org.coursera.courier.tslite.TSProperties.Optionality;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides utilities for code generating Typescript source code.
 */
public class TSSyntax {

  private final ClassTemplateSpec classSpec;
  private final TSProperties TSProperties;
  private final GlobalConfig globalConfig;

  public TSSyntax(
      ClassTemplateSpec classSpec, TSProperties TSProperties, GlobalConfig globalConfig) {
    this.classSpec = classSpec;
    this.TSProperties = TSProperties;
    this.globalConfig = globalConfig;
  }
  private static final String SOURCE_INDENTATION = " ";
  private static final Set<String> tsKeywords = new HashSet<String>(Arrays.asList(new String[]{
    // Keywords copied from: https://github.com/Microsoft/TypeScript/issues/2536
    // Reserved Words
    "break",
    "case",
    "catch",
    "class",
    "const",
    "continue",
    "debugger",
    "default",
    "delete",
    "do",
    "else",
    "enum",
    "export",
    "extends",
    "false",
    "finally",
    "for",
    "function",
    "if",
    "import",
    "in",
    "instanceof",
    "new",
    "null",
    "return",
    "super",
    "switch",
    "this",
    "throw",
    "true",
    "try",
    "typeof",
    "var",
    "void",
    "while",
    "with",

    // Strict Mode Reserved Words
    "as",
    "implements",
    "interface",
    "let",
    "package",
    "private",
    "protected",
    "public",
    "static",
    "yield",

    // Contextual Keywords
    "any",
    "boolean",
    "constructor",
    "declare",
    "get",
    "module",
    "require",
    "number",
    "set",
    "string",
    "symbol",
    "type",
    "from",
    "of"
  }));

  /**
   * Returns the escaped Pegasus symbol for use in Typescript source code.
   *
   * Pegasus symbols must be of the form [A-Za-z_], so this routine simply checks if the
   * symbol collides with a typescript keyword, and if so, escapes it.
   *
   * @param symbol the symbol to escape
   * @return the escaped Pegasus symbol.
   */
  public static String escapeKeyword(String symbol) {
    return TSSyntax.escapeKeyword(symbol, EscapeOptions.QUOTE);
  }

  public static enum EscapeOptions {
    /** Adds an underscore after the symbol name when escaping. e.g.: class becomes class_*/
    MANGLE,

    /** Quotes the symbol when escaping. e.g.: class becomes "class" */
    QUOTE
  }

  public static String escapeKeyword(String symbol, EscapeOptions options) {
    if (tsKeywords.contains(symbol)) {
      if (options.equals(EscapeOptions.MANGLE)) {
        return symbol + "_";
      } else {
        return "\"" + symbol + "\"";
      }
    } else {
      return symbol;
    }
  }

  /**
   * Returns the escaped fully qualified name of a {@link ClassTemplateSpec}.
   *
   * @param spec to build a escaped fully qualified name for.
   * @param fullName true if the namespace should be included as well
   *
   * @return the escaped fullname.
   */
  public static String escapedName(ClassTemplateSpec spec, boolean fullName) {
    String enclosingClassPart = spec.getEnclosingClass() != null? spec.getEnclosingClass().getClassName() + ".": "";
    return toFullname(fullName? spec.getNamespace(): null, escapeKeyword(enclosingClassPart + spec.getClassName()));
  }

  private static String toFullname(String namespace, String className) {
    if (namespace == null) {
      return className;
    } else {
      return namespace + "." + className;
    }
  }

  /**
   * Returns the Typescript type of an optional field for the given {@link ClassTemplateSpec} as a
   * Typescript source code string.
   *
   * Even if the field is required, it still will be represented as optional when
   * Optionality is set to {@link TSProperties.Optionality#REQUIRED_FIELDS_MAY_BE_ABSENT}.
   *
   * @param spec to get a Typescript type name for.
   * @return Typescript source code string identifying the given type.
   */
  public String toType(ClassTemplateSpec spec) {
    // TODO(eboto): This is no longer necessary. Delete.
    return toTypeString(spec, true);
  }

  public boolean isOptional(RecordTemplateSpec.Field field) {
    boolean isFieldOptional = field.getSchemaField().getOptional();
    return isFieldOptional || TSProperties.optionality == Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT;
  }

  public String questionMarkIfOptional(RecordTemplateSpec.Field field) {
    return isOptional(field)? "?": "";
  }

  public boolean isEquatable() {
    if (classSpec instanceof UnionTemplateSpec) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec)classSpec;
      TyperefTemplateSpec typerefSpec = unionSpec.getTyperefClass();
      if (typerefSpec == null) return globalConfig.defaults.equatable;
      return globalConfig.lookupTSProperties(typerefSpec).equatable;
    } else {
      return TSProperties.equatable;
    }
  }

  public String toTypeString(RecordTemplateSpec.Field field) {
    return toTypeString(field, true /* fullName */);
  }

  public String toTypeString(RecordTemplateSpec.Field field, boolean fullName) {
    if (field.getSchemaField() != null && field.getSchemaField().getType() instanceof TyperefDataSchema) {
      return ((TyperefDataSchema) field.getSchemaField().getType()).getFullName();
    } else {
      return toTypeString(field.getType(), fullName);
    }
  }

  public String toTypeString(ClassTemplateSpec spec) {
    return toTypeString(spec, true /* fullName */);
  }
  public String filterForUnionGetter(String other) {
    String punctuationEscaped = other.replaceAll("[\\p{Punct}\\p{Space}]", "");
    return Character.toUpperCase(punctuationEscaped.charAt(0)) + punctuationEscaped.substring(1);
  }

  public String filterForUnionGetterKey(String other) {
    String filtered = filterForUnionGetter(other);
    return Character.toLowerCase(filtered.charAt(0)) + filtered.substring(1);
  }
  // emit string representing type
  public String toTypeString(ClassTemplateSpec spec, boolean fullName) {
    if (spec == null) {
      return "void";
    }
    // If we're supporting projections, all fields, even required ones, may be absent.
    // To support this, we box all primitive field types.
    if (spec.getSchema() == null) { // custom type
      return escapedName(spec, fullName);
    }
    Type schemaType = spec.getSchema().getType();

    if (schemaType == Type.INT) {
      return "number";
    } else if (schemaType == Type.LONG) {
      return "number";
    } else if (schemaType == Type.FLOAT) {
      return "number";
    } else if (schemaType == Type.DOUBLE) {
      return "number";
    } else if (schemaType == Type.STRING) {
      return "string";
    } else if (schemaType == Type.BOOLEAN) {
      return "boolean";
    } else if (schemaType == Type.BYTES) {
      return "string";
    } else if (schemaType == Type.FIXED) {
      return "string";
    } else if (schemaType == Type.ENUM) {
      EnumDataSchema enumDataSchema = (EnumDataSchema) spec.getSchema();
      return toFullname(fullName? enumDataSchema.getNamespace(): null, escapeKeyword(enumDataSchema.getName()));
    } else if (schemaType == Type.RECORD) {
      RecordDataSchema recordDataSchema = (RecordDataSchema) spec.getSchema();
      return toFullname(fullName? recordDataSchema.getNamespace(): null, escapeKeyword(recordDataSchema.getName()));
    } else if (schemaType == Type.UNION) {
      return escapedName(spec, fullName);
    } else if (schemaType == Type.MAP) {
      return "courier.Map<" + toTypeString(((MapTemplateSpec) spec).getValueClass(), fullName) + ">";
    } else if (schemaType == Type.ARRAY) {
      ArrayTemplateSpec arraySpec = (ArrayTemplateSpec) spec;
      return "Array<" + toTypeString(((ArrayTemplateSpec) spec).getItemClass(), fullName) + ">";
    } else if (schemaType == Type.TYPEREF) {
      TyperefDataSchema typerefSchema = (TyperefDataSchema) spec.getSchema();
      return toFullname(fullName? typerefSchema.getNamespace(): null, escapeKeyword(typerefSchema.getName()));
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  public static String openNamespace(ClassTemplateSpec spec) {
    String ns = spec.getNamespace();
    if (ns != null && !"".equals(ns)) {
      return new StringBuffer()
          .append("namespace ")
          .append(ns)
          .append(" {")
          .toString();
    } else {
      return "";
    }
  }

  public static String closeNamespace(ClassTemplateSpec spec) {
    String ns = spec.getNamespace();
    if (ns != null && !"".equals(ns)) {
      return "}";
    } else {
      return "";
    }
  }

  /**
   * Returns the union member class name for the given {@link ClassTemplateSpec} as a Typescript
   * source code string.
   *
   * @param spec provides the union member type to get the name for.
   * @return a typescript source code string identifying the union member.
   */
  public static String toUnionMemberName(ClassTemplateSpec spec) {
    if (spec.getSchema() == null) { // custom type
      return spec.getClassName() + "Member";
    }

    Type schemaType = spec.getSchema().getType();
    if (schemaType == Type.INT) {
      return "IntMember";
    } else if (schemaType == Type.LONG) {
      return "LongMember";
    } else if (schemaType == Type.FLOAT) {
      return "FloatMember";
    } else if (schemaType == Type.DOUBLE) {
      return "DoubleMember";
    } else if (schemaType == Type.STRING) {
      return "StringMember";
    } else if (schemaType == Type.BOOLEAN) {
      return "BooleanMember";
    } else if (schemaType == Type.BYTES) {
      return "BytesMember";
    } else if (schemaType == Type.FIXED) {
      return "FixedMember";
    } else if (schemaType == Type.ENUM) {
      return spec.getClassName() + "Member";
    } else if (schemaType == Type.RECORD) {
      return spec.getClassName() + "Member";
    } else if (schemaType == Type.MAP) {
      return "MapMember";
    } else if (schemaType == Type.ARRAY) {
      return "ArrayMember";
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }
}
