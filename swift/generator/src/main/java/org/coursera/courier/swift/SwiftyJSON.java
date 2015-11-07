package org.coursera.courier.swift;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;
import org.coursera.courier.api.CourierMapTemplateSpec;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class SwiftyJSON {

  private static final Map<DataSchema.Type, String> schemaTypeToSwiftyType;
  static {
    schemaTypeToSwiftyType = new HashMap<DataSchema.Type, String>();
    schemaTypeToSwiftyType.put(DataSchema.Type.INT, "int"); // TODO(jbetz): just use Int32 here? (On a 64-bit platform, Int is the same size as Int64.)
    schemaTypeToSwiftyType.put(DataSchema.Type.LONG, "int"); // TODO(jbetz): just use Int64 here? (On a 32-bit platform, Int is the same size as Int32.)
    schemaTypeToSwiftyType.put(DataSchema.Type.FLOAT, "float");
    schemaTypeToSwiftyType.put(DataSchema.Type.DOUBLE, "double");
    schemaTypeToSwiftyType.put(DataSchema.Type.STRING, "string");
    schemaTypeToSwiftyType.put(DataSchema.Type.BOOLEAN, "bool");
    schemaTypeToSwiftyType.put(DataSchema.Type.BYTES, "string"); // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    schemaTypeToSwiftyType.put(DataSchema.Type.FIXED, "string"); // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    schemaTypeToSwiftyType.put(DataSchema.Type.ENUM, "string");
    schemaTypeToSwiftyType.put(DataSchema.Type.RECORD, "json");
    schemaTypeToSwiftyType.put(DataSchema.Type.UNION, "json");
    schemaTypeToSwiftyType.put(DataSchema.Type.MAP, "dictionary");
    schemaTypeToSwiftyType.put(DataSchema.Type.ARRAY, "array");
  }

  private static final Map<DataSchema.Type, String> schemaTypetoSwiftyTypeEnum;
  static {
    schemaTypetoSwiftyTypeEnum = new HashMap<DataSchema.Type, String>();
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.INT, ".Number"); // TODO(jbetz): just use Int32 here? (On a 64-bit platform, Int is the same size as Int64.)
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.LONG, ".Number"); // TODO(jbetz): just use Int64 here? (On a 32-bit platform, Int is the same size as Int32.)
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.FLOAT, ".Number");
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.DOUBLE, ".Number");
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.STRING, ".String");
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.BOOLEAN, ".Bool");
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.BYTES, ".String"); // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.FIXED, ".String"); // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.ENUM, ".String");
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.RECORD, ".Dictionary");
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.UNION, ".Dictionary");
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.MAP, ".Dictionary");
    schemaTypetoSwiftyTypeEnum.put(DataSchema.Type.ARRAY, ".Array");
  }

  private SwiftSyntax syntax;

  public SwiftyJSON(SwiftSyntax syntax) {
    this.syntax = syntax;
  }

  // Assumes the fields are contained in json$.
  // Creates a Swift source string that evaluates to Swifty's JSON type.

  public String toGetAccessor(String anchor, UnionTemplateSpec.Member member) {
    ClassTemplateSpec type = member.getClassTemplateSpec();
    return toGetAccessor(anchor, type, false);
  }

  public String toGetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    return buildGetAccessor(anchor, spec, isOptional).toString();
  }

  /**
   * Constructs a Swift expression that converts the given anchor expression into the
   * swift types used to represent the given spec.
   *
   * For example, if the anchor is "example" and the spec is for a record type called "Message"
   * the resulting expression would be "try Message.readJSON(example)".
   */
  private Expr buildGetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    if (spec.getSchema().isPrimitive() || spec.getSchema().getType() == DataSchema.Type.FIXED) {
      return directAccessor(anchor, spec, isOptional);
    } else {
      return buildComplexTypeGetAccessor(anchor, spec, isOptional);
    }
  }

  private Expr buildComplexTypeGetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    Expr directAccessor = directAccessor(anchor, spec, isOptional);
    if (isOptional) {
      return throwExpr(directAccessor.undecorated() + ".map {" + buildComplexTypeGetAccessor("$0", spec).toString() + " }");
    } else {
      return throwExpr(buildComplexTypeGetAccessor(directAccessor.undecorated(), spec).undecorated());
    }
  }

  private Expr buildComplexTypeGetAccessor(String anchor, ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    if (schemaType == DataSchema.Type.ENUM) {
      EnumTemplateSpec enumSpec = (EnumTemplateSpec)spec;
      return expr(enumSpec.getClassName() + ".read(" + anchor + ")");
    } else if (schemaType == DataSchema.Type.RECORD) {
      RecordTemplateSpec recordSpec = (RecordTemplateSpec)spec;
      return throwExpr(recordSpec.getClassName() + ".readJSON(" + anchor + ")");
    } else if (schemaType == DataSchema.Type.UNION) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec)spec;
      return throwExpr(unionSpec.getClassName() + ".readJSON(" + anchor + ")");
    } else if (schemaType == DataSchema.Type.MAP) {
      CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
      Expr inner = buildGetAccessor("$0", mapSpec.getValueClass(), false);
      return inner.wrap(anchor + ".mapValues { ", inner, " }");
    } else if (schemaType == DataSchema.Type.ARRAY) {
      ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
      Expr inner = buildGetAccessor("$0", arraySpec.getItemClass(), false);
      return inner.wrap(anchor + ".map { ", inner, " }");
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  public Expr directAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    if (isOptional) {
      return throwExpr(anchor + ".optional(" + swiftyTypeEnum(spec) + ")." + maybeDirectOptional(swiftyType(spec), isOptional));
    } else {
      return throwExpr(anchor + ".required(" + swiftyTypeEnum(spec) + ")." + maybeDirectOptional(swiftyType(spec), isOptional));
    }
  }

  public String swiftyType(ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    String swiftyType = schemaTypeToSwiftyType.get(schemaType);
    if (swiftyType == null) {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
    return swiftyType;
  }

  public String swiftyTypeEnum(ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    String swiftyType = schemaTypetoSwiftyTypeEnum.get(schemaType);
    if (swiftyType == null) {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
    return swiftyType;
  }

  private String maybeDirectOptional(String type, boolean isOptional) {
    if (!isOptional) {
      return type + "Value";
    } else {
      return type;
    }
  }

  public String toSetAccessor(UnionTemplateSpec.Member member) {
    ClassTemplateSpec type = member.getClassTemplateSpec();
    return toSetAccessor("member", type);
  }

  public String toSetAccessor(String anchor, ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    if (schemaType == DataSchema.Type.ENUM) {
      return anchor + ".write()";
    } else if (EnumSet.of(DataSchema.Type.RECORD, DataSchema.Type.UNION).contains(schemaType)) {
      return anchor + ".writeData()";
    } else if (schemaType == DataSchema.Type.MAP) {
      CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
      if (mapSpec.getValueClass().getSchema().isPrimitive()) {
        return anchor;
      } else {
        return anchor + ".mapValues { " + toSetAccessor("$0", mapSpec.getValueClass()) + " }";
      }
    } else if (schemaType == DataSchema.Type.ARRAY) {
      ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
      if (arraySpec.getItemClass().getSchema().isPrimitive()) {
        return anchor;
      } else {
        return anchor + ".map { " + toSetAccessor("$0", arraySpec.getItemClass()) + " }";
      }
    } else {
      return anchor;
    }
  }

  private interface Expr {
    Expr wrap(String prefix, Expr wrappedExpr, String postfix);
    String undecorated();
  }

  private static class PlainExpr implements Expr {
    public final String expr;

    public PlainExpr(String expr) {
      this.expr = expr;
    }

    public Expr wrap(String prefix, Expr wrappedExpr, String postfix) {
      return new PlainExpr(prefix + wrappedExpr.toString() + postfix);
    }

    public String undecorated() {
      return expr;
    }

    public String toString() {
      return expr;
    }
  }

  private static class ThrowsExpr implements Expr {
    public final String expr;
    public ThrowsExpr(String expr) {
      this.expr = expr;
    }

    public Expr wrap(String prefix, Expr wrappedExpr, String postfix) {
      return new ThrowsExpr(prefix + wrappedExpr.toString() + postfix);
    }

    public String undecorated() {
      return expr;
    }

    public String toString() {
      return "try " + expr;
    }
  }

  private static Expr throwExpr(String expr) {
    return new ThrowsExpr(expr);
  }

  private static Expr expr(String expr) {
    return new PlainExpr(expr);
  }

}
