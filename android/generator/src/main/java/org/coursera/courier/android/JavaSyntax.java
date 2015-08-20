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
import org.coursera.courier.android.AndroidProperties.ArrayStyle;
import org.coursera.courier.android.AndroidProperties.Optionality;
import org.coursera.courier.api.CourierMapTemplateSpec;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Provides utilities for code generating Java source code.
 */
public class JavaSyntax {

  public final AndroidProperties androidProperties;

  public JavaSyntax(AndroidProperties androidProperties) {
    this.androidProperties = androidProperties;
  }

  private static final Set<String> javaKeywords = new HashSet<String>(Arrays.asList(new String[]{
      "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
      "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
      "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
      "native", "new", "package", "private", "protected", "public", "return", "short", "static",
      "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient",
      "try", "void", "volatile", "while"
  }));

  /**
   * Returns the escaped Pegasus symbol for use in Java source code.
   *
   * Pegasus symbols must be of the form [A-Za-z_], so this routine simply checks if the
   * symbol collides with a java keyword, and if so, escapes it.
   *
   * (Because only fields are generated, symbols like hashCode do not collide with method names
   * from Object and may be used).
   */
  public static String escapeKeyword(String symbol) {
    if (javaKeywords.contains(symbol)) {
      return symbol + "$";
    } else {
      return symbol;
    }
  }

  /**
   * Returns the escaped fully qualified name of a {@link ClassTemplateSpec}.
   */
  public static String escapedFullname(ClassTemplateSpec spec) {
    return toFullname(spec.getNamespace(), escapeKeyword(spec.getClassName()));
  }

  private static String toFullname(String namespace, String className) {
    if (namespace == null) {
      return className;
    } else {
      return namespace + "." + className;
    }
  }

  /**
   * Returns the Java type of the given {@link ClassTemplateSpec} as a Java source code string.
   *
   * Primitive types are represented using the {@link AndroidProperties.Optionality} for this
   * instance.
   */
  public String toType(ClassTemplateSpec spec) {
    return toType(spec, androidProperties.optionality);
  }

  /**
   * Returns the Java type of an optional field for the given {@link ClassTemplateSpec} as a
   * Java source code string.
   *
   * If the field is optional it is always represented as a
   * {@link AndroidProperties.Optionality#REQUIRED_FIELDS_MAY_BE_ABSENT} type else it is
   * represented using the {@link AndroidProperties.Optionality} for this instance.
   */
  public String toOptionalType(ClassTemplateSpec spec, boolean optional) {
    return toType(
        spec,
        optional ? Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT : androidProperties.optionality);
  }

  /**
   * Returns the Java type of the given {@link ClassTemplateSpec} as a Java source code string.
   *
   * Primitive types are represented as specified by the provided @{link PrimitiveStyle}.
   */
  public String toType(ClassTemplateSpec spec, Optionality optionality) {
    // If we're supporting projections, all fields, even required ones, may be absent.
    // To support this, we box all primitive field types.
    boolean boxed = optionality == Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT;

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
      return "Map<String, " + toType(((CourierMapTemplateSpec) spec).getValueClass(), Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT) + ">";
    } else if (schemaType == Type.ARRAY) {
      if (androidProperties.arrayStyle == ArrayStyle.ARRAYS) {
        return toType(((ArrayTemplateSpec) spec).getItemClass(), Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT) + "[]";
      } else if (androidProperties.arrayStyle == ArrayStyle.LISTS) {
	      return "List<" + toType(((ArrayTemplateSpec) spec).getItemClass(), Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT) + ">";
      } else {
        throw new IllegalArgumentException();
      }
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  /**
   * Returns the union member class name for the given {@link ClassTemplateSpec} as a Java
   * source code string.
   */
  public String toUnionMemberName(ClassTemplateSpec spec) {

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
   * Returns the fields as a list of parameters for inclusion in Java source.  E.g.:
   *
   * <code>
   *   field1, field2, field3, field4
   * </code>
   */
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

  /**
   * Returns the fields as a list of parameter declarations for inclusion in Java source. E.g.:
   *
   * <code>
   *   org.example.Record field1, List<Integer> field2, Map<String, Integer> field3, Integer field4
   * </code>
   */
  public String fieldAndTypeList(List<RecordTemplateSpec.Field> fields) {
    StringBuilder sb = new StringBuilder();
    Iterator<RecordTemplateSpec.Field> iter = fields.iterator();
    while(iter.hasNext()) {
      RecordTemplateSpec.Field field = iter.next();
      sb.append(toOptionalType(field.getType(), field.getSchemaField().getOptional()));
      sb.append(" ");
      sb.append(escapeKeyword(field.getSchemaField().getName()));
      if (iter.hasNext()) sb.append(", ");
    }
    return sb.toString();
  }

  /**
   * Returns Java source code that computes the hashCodes of each of the fields, as a list of
   * parameters.
   *
   * This is the same as {@link #fieldList} except when the fields are Java arrays, in which
   * case they are wrapped with a utilty method to hash them correctly.  E.g.
   *
   * <code>
   *   intField, stringField, mapField, Arrays.deepHashCode(javaArrayField), recordField
   * </code>
   */
  public String hashCodeList(List<RecordTemplateSpec.Field> fields) {
    StringBuilder sb = new StringBuilder();
    Iterator<RecordTemplateSpec.Field> iter = fields.iterator();
    while(iter.hasNext()) {
      RecordTemplateSpec.Field field = iter.next();
      Type schemaType = field.getSchemaField().getType().getType();
      if (schemaType == Type.ARRAY && androidProperties.arrayStyle == ArrayStyle.ARRAYS) {
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
