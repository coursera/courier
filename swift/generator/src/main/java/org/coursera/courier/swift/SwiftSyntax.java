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

package org.coursera.courier.swift;

import com.linkedin.data.ByteString;
import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.Null;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.BooleanDataSchema;
import com.linkedin.data.schema.BytesDataSchema;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.DataSchema.Type;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.MapDataSchema;
import com.linkedin.data.schema.NullDataSchema;
import com.linkedin.data.schema.PrimitiveDataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.StringDataSchema;
import com.linkedin.data.schema.UnionDataSchema;
import com.linkedin.pegasus.generator.spec.*;
import org.coursera.courier.api.CourierMapTemplateSpec;
import org.coursera.courier.swift.SwiftProperties.Optionality;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Provides utilities for code generating Swift source code.
 */
public class SwiftSyntax {

  private final ClassTemplateSpec classSpec;
  private final SwiftProperties swiftProperties;
  private final GlobalConfig globalConfig;

  public SwiftSyntax(
      ClassTemplateSpec classSpec, SwiftProperties swiftProperties, GlobalConfig globalConfig) {
    this.classSpec = classSpec;
    this.swiftProperties = swiftProperties;
    this.globalConfig = globalConfig;
  }

  private static final Set<String> swiftKeywords = new HashSet<String>(Arrays.asList(new String[]{
      // https://developer.apple.com/library/ios/documentation/Swift/Conceptual/Swift_Programming_Language/LexicalStructure.html
      // In declarations:
      "class", "deinit", "enum", "extension", "func", "import", "init", "inout", "internal", "let",
      "operator", "private", "protocol", "public", "static", "struct", "subscript", "typealias",
      "var",
      // In statements:
      "break", "case", "continue", "default", "defer", "do", "else", "fallthrough", "for", "guard",
      "if", "in", "repeat", "return", "switch", "where", "while",
      // In expressions:
      "as", "catch", "dynamicType", "false", "is", "nil", "rethrows", "super", "self", "Self",
      "throw", "throws", "true", "try", "__COLUMN__", "__FILE__", "__FUNCTION__", "__LINE__",
      // In contexts:
      "associativity", "convenience", "dynamic", "didSet", "final", "get", "infix", "indirect",
      "lazy", "left", "mutating", "none", "nonmutating", "optional", "override", "postfix",
      "precedence", "prefix", "Protocol", "required", "right", "set", "Type", "unowned", "weak",
      "willSet"
  }));

  private static final Set<String> reservedSymbols = new HashSet<String>(Arrays.asList(new String[]{
      // reserved by code generator
      "readJSON", "writeJSON", "validate", "toData" // If removed, we potentially get "invalid redeclaration of coercerOutput errors"
  }));

  /**
   * Returns the escaped Pegasus symbol for use in Swift source code.
   *
   * Pegasus symbols must be of the form [A-Za-z_], so this routine simply checks if the
   * symbol collides with a swift keyword, and if so, escapes it.
   *
   * (Because only fields are generated, symbols like hashCode do not collide with method names
   * from Object and may be used).
   *
   * @param symbol the symbol to escape
   * @return the escaped Pegasus symbol.
   */
  public static String escapeKeyword(String symbol) {
    if (swiftKeywords.contains(symbol)) {
      return "`" + symbol + "`";
    } else if (reservedSymbols.contains(symbol)) {
      return symbol + "$";
    } else {
      return symbol;
    }
  }

  public static String escapeString(String value) {
    return SwiftStringEscaper.escape(value);
  }

  /**
   * Returns the escaped fully qualified name of a {@link ClassTemplateSpec}.
   *
   * @param spec to createGeneratorRunState a escaped fully qualified name for.
   *
   * @return the escaped fullname.
   */
  public static String escapedFullname(ClassTemplateSpec spec) {
    // TODO: Remove below null and introduce module namespacing
    return toFullname(null, escapeKeyword(spec.getClassName()));
  }

  private static String toFullname(String namespace, String className) {
    if (namespace == null) {
      return className;
    } else {
      return namespace + "." + className;
    }
  }

  /**
   * Returns the Swift type of an optional field for the given {@link ClassTemplateSpec} as a
   * Swift source code string.
   *
   * Even if the field is required, it still will be represented as optional when
   * Optionality is set to {@link SwiftProperties.Optionality#REQUIRED_FIELDS_MAY_BE_ABSENT}.
   *
   * @param spec to get a Swift type name for.
   * @param isOptional indicates if the type is optional or not.
   * @return Swift source code string identifying the given type.
   */
  public String toType(ClassTemplateSpec spec, boolean isOptional) {
    String type = toTypeString(spec);
    return type + (isOptional ? "?" : "");
  }

  public boolean isOptional(RecordTemplateSpec.Field field) {
    boolean isFieldOptional = field.getSchemaField().getOptional();
    return isFieldOptional || swiftProperties.optionality == Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT;
  }

  public boolean isEquatable() {
    if (classSpec instanceof UnionTemplateSpec) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec)classSpec;
      TyperefTemplateSpec typerefSpec = unionSpec.getTyperefClass();
      if (typerefSpec == null) return globalConfig.defaults.equatable;
      return globalConfig.lookupSwiftProperties(typerefSpec).equatable;
    } else {
      return swiftProperties.equatable;
    }
  }

  // emit string representing type
  private String toTypeString(ClassTemplateSpec spec) {
    // If we're supporting projections, all fields, even required ones, may be absent.
    // To support this, we box all primitive field types.
    if (spec.getSchema() == null) { // custom type
      return escapedFullname(spec);
    }
    Type schemaType = spec.getSchema().getType();
    if (schemaType == Type.INT) {
      return "Int"; // TODO: just use Int32 here? (On a 32-bit platform, Int is the same size as Int32.)
    } else if (schemaType == Type.LONG) {
      return "Int"; // TODO: just use Int32 here? (On a 64-bit platform, Int is the same size as Int64.)
    } else if (schemaType == Type.FLOAT) {
      return "Float";
    } else if (schemaType == Type.DOUBLE) {
      return "Double";
    } else if (schemaType == Type.STRING) {
      return "String";
    } else if (schemaType == Type.BOOLEAN) {
      return "Bool";
    } else if (schemaType == Type.BYTES) {
      return "String"; // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    } else if (schemaType == Type.FIXED) {
      return "String"; // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    } else if (schemaType == Type.ENUM) {
      return escapedFullname(spec);
    } else if (schemaType == Type.RECORD) {
      return escapedFullname(spec);
    } else if (schemaType == Type.UNION) {
      return escapedFullname(spec);
    } else if (schemaType == Type.MAP) {
      return "[String: " + toTypeString(((CourierMapTemplateSpec) spec).getValueClass()) + "]";
    } else if (schemaType == Type.ARRAY) {
      return "[" + toTypeString(((ArrayTemplateSpec) spec).getItemClass()) + "]";
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  /**
   * Returns the union member class name for the given {@link ClassTemplateSpec} as a Swift
   * source code string.
   *
   * @param spec provides the union member type to get the name for.
   * @return a Swift source code string identifying the union member.
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

  /**
   * @param field field to createGeneratorRunState a default literal for.
   *
   * @return A swift source code string representing the literal value of the field default, or null
   * if the field does not have a default.
   */
  public String defaultToLiteral(RecordTemplateSpec.Field field) {
    boolean explicit = Boolean.TRUE.equals(field.getSchemaField().getProperties().get("explicit"));
    if (!explicit && field.getSchemaField().getOptional()) {
      return "nil";
    } else {
      CustomInfoSpec customInfo = field.getCustomInfo();
      ClassTemplateSpec fieldType;
      if (customInfo != null) {
        DataSchema refSchema = customInfo.getCustomSchema().getDereferencedDataSchema();
        fieldType = ClassTemplateSpec.createFromDataSchema(refSchema);
        String coercer = customInfo.getCoercerClass().getClassName();
        String uncoerced = toLiteral(fieldType.getSchema(), field.getSchemaField().getDefault());
        if (uncoerced == null) {
          return null;
        } else {
          // TODO(jbetz): try! should be avoided. Is there anything reasonable we can do instead
          // given that swift does not allow use to propagate errors in initializers?
          return SwiftyJSON.expr(coercer).coercerInput(SwiftyJSON.expr(uncoerced)).toSwiftCode().replaceFirst("try", "try!");
        }
      } else {
        fieldType = field.getType();
        return toLiteral(fieldType.getSchema(), field.getSchemaField().getDefault());
      }
    }
  }

  public static String toLiteral(DataSchema schema, Object value) {
    if (value == null) return null;

    if (schema instanceof EnumDataSchema) {
      if (value instanceof String) {
        return "." + escapeKeyword(value.toString());
      }
    } else if (schema instanceof PrimitiveDataSchema) {
      if (schema instanceof StringDataSchema && value instanceof String) {
        return "\"" + escapeString((String)value) + "\"";
      } else if (schema instanceof BooleanDataSchema && value instanceof Boolean) {
        return value.toString();
      } else if (schema instanceof BytesDataSchema && value instanceof ByteString) {
        ByteString bytes = (ByteString)value;
        return "\"" + escapeString(bytes.asAvroString()) + "\"";
      } else if (schema instanceof NullDataSchema && value == Null.getInstance()) {
        return "nil"; // TODO: sort out handling of Null
      } else if (value instanceof Number) { // TODO: verify numbers are formatted correctly for swift
        return value.toString();
      }
    } else if (schema instanceof RecordDataSchema && value instanceof DataMap) {
      StringBuilder sb = new StringBuilder();
      DataMap dataMap = (DataMap)value;
      RecordDataSchema recordSchema = (RecordDataSchema)schema;
      return toRecordLiteral(sb, dataMap, recordSchema);
    } else if (schema instanceof UnionDataSchema && value instanceof DataMap) {
      DataMap unionMap = (DataMap)value;
      UnionDataSchema unionSchema = (UnionDataSchema)schema;
      return toUnionLiteral(unionMap, unionSchema);
    } else if (schema instanceof ArrayDataSchema && value instanceof DataList) {
      DataList dataList = (DataList)value;
      ArrayDataSchema arraySchema = (ArrayDataSchema)schema;
      return toArrayLiteral(dataList, arraySchema);
    } else if (schema instanceof MapDataSchema && value instanceof DataMap) {
      DataMap dataMap = (DataMap)value;
      MapDataSchema mapSchema = (MapDataSchema)schema;
      return toMapLiteral(dataMap, mapSchema);
    }
    throw new IllegalArgumentException("Unsupported default value: " + value + " for type: " + schema.getType());
  }

  private static String toRecordLiteral(StringBuilder sb, DataMap dataMap, RecordDataSchema recordSchema) {
    Iterator<RecordDataSchema.Field> iter = recordSchema.getFields().iterator();
    sb.append(escapeKeyword(recordSchema.getName()));
    sb.append("(");
    while (iter.hasNext()) {
      RecordDataSchema.Field field = iter.next();
      sb.append(escapeKeyword(field.getName()));
      sb.append(": ");
      sb.append(toLiteral(field.getType(), dataMap.get(field.getName())));
      if (iter.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append(")");
    return sb.toString();
  }

  private static String toArrayLiteral(DataList dataList, ArrayDataSchema arraySchema) {
    StringBuilder sb = new StringBuilder();
    DataSchema itemsSchema = arraySchema.getItems().getDereferencedDataSchema();
    Iterator<Object> iter = dataList.iterator();
    sb.append("[");
    while (iter.hasNext()) {
      Object element = iter.next();
      sb.append(toLiteral(itemsSchema, element));
      if (iter.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  private static String toUnionLiteral(DataMap unionMap, UnionDataSchema unionSchema) {
    Iterator<Map.Entry<String, Object>> iter = unionMap.entrySet().iterator();
    if (!iter.hasNext()) {
      throw new IllegalArgumentException();
    }
    Map.Entry<String, Object> entry = iter.next();
    DataSchema memberType = unionSchema.getTypeByName(entry.getKey());
    if (memberType == null) {
      throw new IllegalArgumentException("Unrecognized union member in literal: " + entry.getKey());
    }
    StringBuilder sb = new StringBuilder();
    sb.append(".");
    sb.append(toUnionMemberName(ClassTemplateSpec.createFromDataSchema(memberType)));
    sb.append("(");
    sb.append(toLiteral(memberType, entry.getValue()));
    sb.append(")");
    return sb.toString();
  }

  private static String toMapLiteral(DataMap dataMap, MapDataSchema mapSchema) {
    StringBuilder sb = new StringBuilder();
    DataSchema valuesSchema = mapSchema.getValues().getDereferencedDataSchema();
    Iterator<Map.Entry<String, Object>> iter = dataMap.entrySet().iterator();
    sb.append("[");
    while (iter.hasNext()) {
      Map.Entry<String, Object> entry = iter.next();
      sb.append("\"");
      sb.append(escapeString(entry.getKey()));
      sb.append("\"");
      sb.append(": ");
      sb.append(toLiteral(valuesSchema, entry.getValue()));
      if (iter.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }
}
