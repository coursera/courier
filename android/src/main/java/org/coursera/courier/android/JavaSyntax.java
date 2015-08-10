package org.coursera.courier.android;

import com.linkedin.data.schema.DataSchema.Type;
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import org.coursera.courier.api.CourierMapTemplateSpec;

public class JavaSyntax {
  public static String toType(ClassTemplateSpec spec) {
    return toType(spec, false);
  }

  public static String toType(ClassTemplateSpec spec, boolean boxed) {
    Type schemaType = spec.getSchema().getType();
    if (schemaType == Type.INT) {
      if (boxed) {
        return "java.lang.Integer";
      } else {
        return "int";
      }
    } else if (schemaType == Type.LONG) {
      if (boxed) {
        return "java.lang.Long";
      } else {
        return "long";
      }
    } else if (schemaType == Type.FLOAT) {
      if (boxed) {
        return "java.lang.Float";
      } else {
        return "float";
      }
    } else if (schemaType == Type.DOUBLE) {
      if (boxed) {
        return "java.lang.Double";
      } else {
        return "double";
      }
    } else if (schemaType == Type.STRING) {
      return "String";
    } else if (schemaType == Type.BOOLEAN) {
      if (boxed) {
        return "java.lang.Boolean";
      } else {
        return "boolean";
      }
    } else if (schemaType == Type.BYTES) {
      return "byte[]";
    } else if (schemaType == Type.FIXED) {
      return "byte[]";
    } else if (schemaType == Type.ENUM) {
      return spec.getFullName();
    } else if (schemaType == Type.RECORD) {
      return spec.getFullName();
    } else if (schemaType == Type.UNION) {
      return spec.getFullName();
    } else if (schemaType == Type.MAP) {
      return "java.util.Map<String, " + toType(((CourierMapTemplateSpec) spec).getValueClass(), true) + ">";
    } else if (schemaType == Type.ARRAY) {
      return toType(((ArrayTemplateSpec) spec).getItemClass()) + "[]";
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  public static String toUnionMemberName(ClassTemplateSpec spec) {
    Type schemaType = spec.getSchema().getType();
    if (schemaType == Type.INT) {
      return "Int";
    } else if (schemaType == Type.LONG) {
      return "Long";
    } else if (schemaType == Type.FLOAT) {
      return "Float";
    } else if (schemaType == Type.DOUBLE) {
      return "Double";
    } else if (schemaType == Type.STRING) {
      return "String";
    } else if (schemaType == Type.BOOLEAN) {
      return "Boolean";
    } else if (schemaType == Type.BYTES) {
      return "Bytes";
    } else if (schemaType == Type.FIXED) {
      return "Fixed";
    } else if (schemaType == Type.ENUM) {
      return spec.getClassName();
    } else if (schemaType == Type.RECORD) {
      return spec.getClassName();
    } else if (schemaType == Type.MAP) {
      return "Map";
    } else if (schemaType == Type.ARRAY) {
      return "Array";
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  public static String escape(String symbol) {
    return symbol; // TODO: escape Java keywords and reserved chars
  }
}
