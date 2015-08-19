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

package org.coursera.courier.android;

import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.DataSchema.Type;
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import org.coursera.courier.api.CourierMapTemplateSpec;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class JavaSyntax {

  public enum ArrayStyle { ARRAY, LIST }

  private final ArrayStyle arrayStyle;

  public JavaSyntax(ArrayStyle arrayStyle) {
    this.arrayStyle = arrayStyle;
  }

  private static final Set<String> javaKeywords = new HashSet<String>(Arrays.asList(new String[]{
      "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
      "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
      "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
      "native", "new", "package", "private", "protected", "public", "return", "short", "static",
      "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient",
      "try", "void", "volatile", "while"
  }));

  public static String escapeKeyword(String symbol) {
    if (javaKeywords.contains(symbol)) {
      return symbol + "$";
    } else {
      return symbol;
    }
  }

  public static String escapedFullname(ClassTemplateSpec spec) {
    return toFullname(spec.getNamespace(), escapeKeyword(spec.getClassName()));
  }

  public static String toFullname(String namespace, String className) {
    if (namespace == null) {
      return className;
    } else {
      return namespace + "." + className;
    }
  }

  public String toType(ClassTemplateSpec spec) {
    return toType(spec, false);
  }

  public String toType(ClassTemplateSpec spec, boolean boxed) {

    // TODO: support custom types properly
    if (spec.getSchema() == null) { // custom type
      return escapedFullname(spec);
    }
    Type schemaType = spec.getSchema().getType();
    if (schemaType == Type.INT) {
      if (boxed) {
        return "Integer";
      } else {
        return "int";
      }
    } else if (schemaType == Type.LONG) {
      if (boxed) {
        return "Long";
      } else {
        return "long";
      }
    } else if (schemaType == Type.FLOAT) {
      if (boxed) {
        return "Float";
      } else {
        return "float";
      }
    } else if (schemaType == Type.DOUBLE) {
      if (boxed) {
        return "Double";
      } else {
        return "double";
      }
    } else if (schemaType == Type.STRING) {
      return "String";
    } else if (schemaType == Type.BOOLEAN) {
      if (boxed) {
        return "Boolean";
      } else {
        return "boolean";
      }
    } else if (schemaType == Type.BYTES) {
      return "String"; // TODO(jbetz): provide an adapter for converting pegasus byte strings to java byte[]
    } else if (schemaType == Type.FIXED) {
      return "String"; // TODO(jbetz): provide an adapter for converting pegasus byte strings to java byte[]
    } else if (schemaType == Type.ENUM) {
      return escapedFullname(spec);
    } else if (schemaType == Type.RECORD) {
      return escapedFullname(spec);
    } else if (schemaType == Type.UNION) {
      return escapedFullname(spec);
    } else if (schemaType == Type.MAP) {
      return "Map<String, " + toType(((CourierMapTemplateSpec) spec).getValueClass(), true) + ">";
    } else if (schemaType == Type.ARRAY) {
      if (arrayStyle == ArrayStyle.ARRAY) {
        return toType(((ArrayTemplateSpec) spec).getItemClass()) + "[]";
      } else if (arrayStyle == ArrayStyle.LIST) {
	return "List<" + toType(((ArrayTemplateSpec) spec).getItemClass(), true) + ">";
      } else {
        throw new IllegalArgumentException();
      }
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  public String toUnionMemberName(ClassTemplateSpec spec) {

    // TODO: support custom types properly
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

  public String fieldList(List<RecordTemplateSpec.Field> fields) {
    StringBuilder sb = new StringBuilder();
    Iterator<RecordTemplateSpec.Field> iter = fields.iterator();
    while(iter.hasNext()) {
      RecordTemplateSpec.Field field = iter.next();
      sb.append(escapeKeyword(field.getSchemaField().getName()));
      if (iter.hasNext()) sb.append(", ");
    }
    return sb.toString();
  }

  public String fieldAndTypeList(List<RecordTemplateSpec.Field> fields) {
    StringBuilder sb = new StringBuilder();
    Iterator<RecordTemplateSpec.Field> iter = fields.iterator();
    while(iter.hasNext()) {
      RecordTemplateSpec.Field field = iter.next();
      sb.append(toType(field.getType()));
      sb.append(" ");
      sb.append(escapeKeyword(field.getSchemaField().getName()));
      if (iter.hasNext()) sb.append(", ");
    }
    return sb.toString();
  }

  public String hashCodeList(List<RecordTemplateSpec.Field> fields) {
    StringBuilder sb = new StringBuilder();
    Iterator<RecordTemplateSpec.Field> iter = fields.iterator();
    while(iter.hasNext()) {
      RecordTemplateSpec.Field field = iter.next();
      if (field.getSchemaField().getType().getType() == Type.ARRAY && arrayStyle == ArrayStyle.ARRAY) {
        ArrayDataSchema arraySchema = (ArrayDataSchema) field.getSchemaField().getType();
        if (arraySchema.getItems().getDereferencedDataSchema().isPrimitive()) {
          sb.append("Arrays.hashCode(");
        } else {
          sb.append("Arrays.deepHashCode(");
        }
        sb.append(escapeKeyword(field.getSchemaField().getName()));
        sb.append(")");
      } else {
        sb.append(escapeKeyword(field.getSchemaField().getName()));
      }
      if (iter.hasNext()) sb.append(", ");
    }
    return sb.toString();
  }
}
