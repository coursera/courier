package org.coursera.courier.swift;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;
import org.coursera.courier.api.CourierMapTemplateSpec;

import java.util.Iterator;
import java.util.List;

public class SwiftyJSON {

  private SwiftSyntax syntax;

  public SwiftyJSON(SwiftSyntax syntax) {
    this.syntax = syntax;
  }

  public String toGetAccessors(List<RecordTemplateSpec.Field> fields) {
    Iterator<RecordTemplateSpec.Field> iter = fields.iterator();
    StringBuilder sb = new StringBuilder();
    while (iter.hasNext()) {
      RecordTemplateSpec.Field field = iter.next();
      sb.append(SwiftSyntax.escapeKeyword(field.getSchemaField().getName()));
      sb.append(": ");
      sb.append(toGetAccessor(field));
      if (iter.hasNext()) {
        sb.append(", ");
        sb.append(System.lineSeparator());
      }
    }
    return sb.toString();
  }

  // Assumes the fields are contained in json$.
  // Creates a Swift source string that evaluates to Swifty's JSON type.
  public String toGetAccessor(RecordTemplateSpec.Field field) {
    boolean isOpt = syntax.isOptional(field);
    return toGetAccessor("json[\"" + field.getSchemaField().getName() + "\"]", field.getType(), isOpt);
  }

  public String toGetAccessor(String anchor, UnionTemplateSpec.Member member) {
    ClassTemplateSpec type = member.getClassTemplateSpec();
    return toGetAccessor(anchor, type, false);
  }

  public String toGetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    if (spec.getSchema().isPrimitive() || spec.getSchema().getType() == DataSchema.Type.FIXED) {
      return toAccessor(anchor, spec, isOptional);
    } else {
      return toWrappedGetAccessor(anchor, spec, isOptional);
    }
  }

  public String toWrappedGetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    String directAccessor =  toAccessor(anchor, spec, isOptional);
    if (isOptional) {
      return directAccessor + ".map { "+ toWrappedGetAccessor("$0", spec) + " }";
    } else {
      return toWrappedGetAccessor(directAccessor, spec);
    }
  }

  public String toWrappedGetAccessor(String anchor, ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    if (schemaType == DataSchema.Type.ENUM) {
      EnumTemplateSpec enumSpec = (EnumTemplateSpec)spec;
      return enumSpec.getClassName() + ".read(" + anchor + ")";
    } else if (schemaType == DataSchema.Type.RECORD) {
      RecordTemplateSpec recordSpec = (RecordTemplateSpec)spec;
      return recordSpec.getClassName() + ".read(" + anchor + ")";
    } else if (schemaType == DataSchema.Type.UNION) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec)spec;
      return unionSpec.getClassName() + ".read(" + anchor + ")";
    } else if (schemaType == DataSchema.Type.MAP) {
      CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
      return anchor + ".mapValues { " + toGetAccessor("$0", mapSpec.getValueClass(), false) + " }";
    } else if (schemaType == DataSchema.Type.ARRAY) {
      ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
      return anchor + ".map { " + toGetAccessor("$0", arraySpec.getItemClass(), false) + " }";
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  public String toAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    if (schemaType == DataSchema.Type.INT) {
      return anchor + "." + maybeDirectOptional("int", isOptional); // TODO: just use Int32 here? (On a 32-bit platform, Int is the same size as Int32.)
    } else if (schemaType == DataSchema.Type.LONG) {
      return anchor + "." + maybeDirectOptional("int", isOptional); // TODO: just use Int32 here? (On a 64-bit platform, Int is the same size as Int64.)
    } else if (schemaType == DataSchema.Type.FLOAT) {
      return anchor + "." + maybeDirectOptional("float", isOptional);
    } else if (schemaType == DataSchema.Type.DOUBLE) {
      return anchor + "." + maybeDirectOptional("double", isOptional);
    } else if (schemaType == DataSchema.Type.STRING) {
      return anchor + "." + maybeDirectOptional("string", isOptional);
    } else if (schemaType == DataSchema.Type.BOOLEAN) {
      return anchor + "." + maybeDirectOptional("bool", isOptional);
    } else if (schemaType == DataSchema.Type.BYTES) {
      return anchor + "." + maybeDirectOptional("string", isOptional); // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    } else if (schemaType == DataSchema.Type.FIXED) {
      return anchor + "." + maybeDirectOptional("string", isOptional); // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    } else if (schemaType == DataSchema.Type.ENUM) {
      return anchor + "." + maybeDirectOptional("string", isOptional);
    } else if (schemaType == DataSchema.Type.RECORD) {
      return anchor + "." + maybeDirectOptional("json", isOptional);
    } else if (schemaType == DataSchema.Type.UNION) {
      return anchor + "." + maybeDirectOptional("json", isOptional);
    } else if (schemaType == DataSchema.Type.MAP) {
      return anchor + "." + maybeDirectOptional("dictionary", isOptional);
    } else if (schemaType == DataSchema.Type.ARRAY) {
      return anchor + "." + maybeDirectOptional("array", isOptional);
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  private String maybeDirectOptional(String type, boolean isOptional) {
    if (!isOptional) {
      return type + "Value";
    } else {
      return type;
    }
  }

  public String toSetAccessor(RecordTemplateSpec.Field field) {
    if (syntax.isOptional(field)) {
      String fieldName = SwiftSyntax.escapeKeyword(field.getSchemaField().getName());
      return
          "if let " + fieldName + " = self." + fieldName + " {" + System.lineSeparator() +
          "json[\"" + field.getSchemaField().getName() + "\"] = " +
              toSetAccessor("" + fieldName, field.getType(), false) + "\n" +
          "}";
    } else {
      return
          "json[\"" + field.getSchemaField().getName() + "\"] = " +
          toSetAccessor(SwiftSyntax.escapeKeyword(field.getSchemaField().getName()), field.getType(), false);
    }
  }

  public String toSetAccessor(UnionTemplateSpec.Member member) {
    ClassTemplateSpec type = member.getClassTemplateSpec();
    return toSetAccessor("member", type, false);
  }

  public String toSetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    if (spec.getSchema().isPrimitive() || spec.getSchema().getType() == DataSchema.Type.FIXED) {
      return toDirectSetAccessor(anchor, spec, isOptional);
    } else {
      return toWrappedSetAccessor(anchor, spec, isOptional);
    }
  }

  public String toDirectSetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    return "JSON(" + anchor + ")";
  }

  public String toWrappedSetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    if (schemaType == DataSchema.Type.ENUM) {
      return "JSON(" + anchor + ".write())";
    } else if (schemaType == DataSchema.Type.RECORD) {
      return "JSON(" + anchor + ".write())";
    } else if (schemaType == DataSchema.Type.UNION) {
      return "JSON(" + anchor + ".write())";
    } else if (schemaType == DataSchema.Type.MAP) {
      CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
      if (mapSpec.getValueClass().getSchema().isPrimitive()) {
        return "JSON(" + anchor + ")";
      } else {
        return "JSON(" + anchor + ".mapValues { " + toSetAccessor("$0", mapSpec.getValueClass(), false) + " })";
      }
    } else if (schemaType == DataSchema.Type.ARRAY) {
      ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
      if (arraySpec.getItemClass().getSchema().isPrimitive()) {
        return "JSON(" + anchor + ")";
      } else {
        return "JSON(" + anchor + ".map { " + toSetAccessor("$0", arraySpec.getItemClass(), false) + " })";
      }
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }
}
