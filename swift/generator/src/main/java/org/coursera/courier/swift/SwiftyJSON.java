package org.coursera.courier.swift;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;
import org.coursera.courier.api.CourierMapTemplateSpec;

import java.util.HashMap;
import java.util.Map;

public class SwiftyJSON {

  private static final Map<DataSchema.Type, String> swiftyTypeAccessors;
  static {
    Map<DataSchema.Type, String> map = new HashMap<DataSchema.Type, String>();
    map.put(DataSchema.Type.INT, "int"); // TODO(jbetz): just use Int32 here? (On a 64-bit platform, Int is the same size as Int64.)
    map.put(DataSchema.Type.LONG, "int"); // TODO(jbetz): just use Int64 here? (On a 32-bit platform, Int is the same size as Int32.)
    map.put(DataSchema.Type.FLOAT, "float");
    map.put(DataSchema.Type.DOUBLE, "double");
    map.put(DataSchema.Type.STRING, "string");
    map.put(DataSchema.Type.BOOLEAN, "bool");
    map.put(DataSchema.Type.BYTES, "string"); // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    map.put(DataSchema.Type.FIXED, "string"); // TODO(jbetz): provide an adapter for converting pegasus byte strings to swift byte[]
    map.put(DataSchema.Type.ENUM, "string");
    map.put(DataSchema.Type.RECORD, "json");
    map.put(DataSchema.Type.UNION, "json");
    map.put(DataSchema.Type.MAP, "dictionary");
    map.put(DataSchema.Type.ARRAY, "array");
    swiftyTypeAccessors = map;
  }

  private static final Map<DataSchema.Type, String> swiftyTypeIdentifiers;
  static {
    Map<DataSchema.Type, String> map = new HashMap<DataSchema.Type, String>();
    map.put(DataSchema.Type.INT, ".Number");
    map.put(DataSchema.Type.LONG, ".Number");
    map.put(DataSchema.Type.FLOAT, ".Number");
    map.put(DataSchema.Type.DOUBLE, ".Number");
    map.put(DataSchema.Type.STRING, ".String");
    map.put(DataSchema.Type.BOOLEAN, ".Bool");
    map.put(DataSchema.Type.BYTES, ".String");
    map.put(DataSchema.Type.FIXED, ".String");
    map.put(DataSchema.Type.ENUM, ".String");
    map.put(DataSchema.Type.RECORD, ".Dictionary");
    map.put(DataSchema.Type.UNION, ".Dictionary");
    map.put(DataSchema.Type.MAP, ".Dictionary");
    map.put(DataSchema.Type.ARRAY, ".Array");
    swiftyTypeIdentifiers = map;
  }

  private SwiftSyntax syntax;

  public SwiftyJSON(SwiftSyntax syntax) {
    this.syntax = syntax;
  }

  /**
   * See toGetAccessor, below.
   */
  public String toGetAccessor(String anchor, UnionTemplateSpec.Member member) {
    ClassTemplateSpec type = member.getClassTemplateSpec();
    return toGetAccessor(anchor, type, false);
  }

  /**
   * Given an expression that evaluates to a SwiftyJson JSON type, and the Pegasus type is should
   * be deserialized to, return a expression that evaluates to the deserialized Swift data binding
   * type.
   */
  public String toGetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    return readTypeExpr(expr(anchor), spec, isOptional).evaluated();
  }

  private Expr readTypeExpr(Expr expr, ClassTemplateSpec spec, boolean isOptional) {
    DataSchema schema = spec.getSchema();
    CheckedThrowExpr directAccessor = checkedTypeAccessorExpr(expr, spec, isOptional);
    if (schema.isPrimitive() || schema.getType() == DataSchema.Type.FIXED) {
      return directAccessor;
    } else {
      if (isOptional) {
        Expr readClosure = readTypeExpr(expr("$0"), spec);
        return directAccessor.apply(".map {" + readClosure.evaluated() + " }", readClosure);
      } else {
        return readTypeExpr(directAccessor, spec);
      }
    }
  }

  private Expr readTypeExpr(Expr expr, ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    switch (schemaType) {
      case ENUM:
        return expr(spec.getClassName()).apply(".read(" + expr.evaluated() + ")", expr, false);
      case RECORD:
      case UNION:
        // readJSON throws
        return checkedThrowExpr(spec.getClassName()).apply(".readJSON(" + expr.evaluated() + ")", expr);
      case MAP:
        CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
        Expr readValueClosure = readTypeExpr(expr("$0"), mapSpec.getValueClass(), false);
        return expr.apply(".mapValues { " + readValueClosure.evaluated() + " }", readValueClosure);
      case ARRAY:
        ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
        Expr readItemClosure = readTypeExpr(expr("$0"), arraySpec.getItemClass(), false);
        return expr.apply(".map { " + readItemClosure.evaluated() + " }", readItemClosure);
      default:
        throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  private CheckedThrowExpr checkedTypeAccessorExpr(Expr expr, ClassTemplateSpec spec, boolean isOptional) {
    String typeIdentifier = typeIdentifier(spec);
    String typeAccessor = typeAccessor(spec, isOptional);
    if (isOptional) {
      return checkedThrowExpr(expr.apply(".optional(" + typeIdentifier + ")." + typeAccessor));
    } else {
      return checkedThrowExpr(expr.apply(".required(" + typeIdentifier + ")." + typeAccessor));
    }
  }

  /**
   * A SwiftyJson property name for a particular Pegasus type.
   *
   * E.g. a required string is accessed using the "stringValue" property.
   */
  private String typeAccessor(ClassTemplateSpec spec, boolean isOptional) {
    String type = typeAccessor(spec);
    if (!isOptional) {
      return type + "Value";
    } else {
      return type;
    }
  }

  private String typeAccessor(ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    String swiftyType = swiftyTypeAccessors.get(schemaType);
    if (swiftyType == null) {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
    return swiftyType;
  }

  private String typeIdentifier(ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    String swiftyType = swiftyTypeIdentifiers.get(schemaType);
    if (swiftyType == null) {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
    return swiftyType;
  }

  public String toSetAccessor(UnionTemplateSpec.Member member) {
    ClassTemplateSpec type = member.getClassTemplateSpec();
    return toSetAccessor("member", type);
  }

  /**
   * Given an expression that evaluates to a Swift data binding type, and the pegasus type of the
   * data binding, return an expression that evaluates to the serialized SwiftyJson JSON type.
   */
  public String toSetAccessor(String anchor, ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    switch (schemaType) {
      case ENUM:
        return anchor + ".write()";
      case RECORD:
      case UNION:
        return anchor + ".writeData()";
      case MAP:
        CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
        if (mapSpec.getValueClass().getSchema().isPrimitive()) {
          return anchor;
        } else {
          return anchor + ".mapValues { " + toSetAccessor("$0", mapSpec.getValueClass()) + " }";
        }
      case ARRAY:
        ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
        if (arraySpec.getItemClass().getSchema().isPrimitive()) {
          return anchor;
        } else {
          return anchor + ".map { " + toSetAccessor("$0", arraySpec.getItemClass()) + " }";
        }
      default:
        return anchor;
    }
  }

  /*
   * Utilities to track Swift "throws" propagation, since thrown errors must be explicitly
   * propagated at at each expression, including closures using "try".
   */

  private static class Expr {
    public final String expr;
    public final boolean rethrows;

    public Expr(String expr) {
      this(expr, true);
    }

    public Expr(String expr, boolean rethrows) {
      this.expr = expr;
      this.rethrows = rethrows;
    }

    public Expr apply(String invocation) {
      return new Expr(expr + invocation);
    }

    public Expr apply(String invocation, Expr nestedExpr) {
      return apply(invocation, nestedExpr, true);
    }

    // rethrows indicates if the applied method is a rethrows method (.map is, but Enum.read is
    // not).
    public Expr apply(String invocation, Expr nestedExpr, boolean rethrows) {
      if (nestedExpr instanceof CheckedThrowExpr) {
        return new CheckedThrowExpr(expr + invocation, rethrows);
      }
      return new Expr(expr + invocation, rethrows);
    }

    public String evaluated() {
      return expr;
    }
  }

  private static class CheckedThrowExpr extends Expr {
    public CheckedThrowExpr(String expr) {
      super(expr);
    }

    public CheckedThrowExpr(String expr, boolean rethrows) {
      super(expr, rethrows);
    }

    @Override
    public CheckedThrowExpr apply(String invocation) {
      return new CheckedThrowExpr(expr + invocation);
    }

    @Override
    public CheckedThrowExpr apply(String invocation, Expr nestedExpr) {
      return new CheckedThrowExpr(expr + invocation);
    }

    @Override
    public String evaluated() {
      if (rethrows) {
        return "try " + expr;
      } else {
        return expr;
      }
    }
  }

  private static CheckedThrowExpr checkedThrowExpr(String expr) {
    return new CheckedThrowExpr(expr);
  }

  private static CheckedThrowExpr checkedThrowExpr(Expr expr) {
    if (expr instanceof CheckedThrowExpr) {
      return (CheckedThrowExpr)expr;
    }
    return new CheckedThrowExpr(expr.evaluated());
  }

  private static Expr expr(String expr) {
    return new Expr(expr);
  }

}
