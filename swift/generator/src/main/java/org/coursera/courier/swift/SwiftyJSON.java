package org.coursera.courier.swift;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.*;
import org.coursera.courier.api.CourierMapTemplateSpec;

import java.util.HashMap;
import java.util.Map;

public class SwiftyJSON {

  private static final Map<DataSchema.Type, String> swiftyTypeAccessors;
  static {
    Map<DataSchema.Type, String> map = new HashMap<DataSchema.Type, String>();
    map.put(DataSchema.Type.INT, "int");
    map.put(DataSchema.Type.LONG, "int");
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

  private static final Map<DataSchema.Type, String> swiftyTypeEnumSymbols;
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
    swiftyTypeEnumSymbols = map;
  }

  private SwiftSyntax syntax;

  public SwiftyJSON(SwiftSyntax syntax) {
    this.syntax = syntax;
  }

  public String toGetAccessor(String anchor, UnionTemplateSpec.Member member) {
    ClassTemplateSpec type = member.getClassTemplateSpec();
    // TODO(jbetz): Union members can have custom info.  We need to either pick up latest rest.li
    // code or inspect the typeref.
    return readTypeExpr(expr(anchor), type, null, false).toSwiftCode();
  }

  public String toGetAccessor(String anchor, RecordTemplateSpec.Field field) {
    CustomInfoSpec customInfo = field.getCustomInfo();
    return readTypeExpr(
      expr(anchor), selectFieldType(field), customInfo, syntax.isOptional(field)).toSwiftCode();
  }

  /**
   * Given an expression that evaluates to a SwiftyJson JSON type, and its Pegasus type
   * return a expression that evaluates to the corresponding Swift data binding type.
   *
   * @param expr provides the expression that evaluates to a SwiftyJson JSON type.
   * @param spec provides the pegasus type that the returned expression should toSwiftCode to.
   * @param custom if non-null, provides the custom type info for provided spec
   * @param isOptional if true, the provide expr must evaluate to an optional type and the returned
   *                   expression will evaluate to an optional type.
   */
  private Expr readTypeExpr(Expr expr, ClassTemplateSpec spec, CustomInfoSpec custom, boolean isOptional) {
    DataSchema schema = spec.getSchema();
    Expr directAccessor = checkedTypeAccessorExpr(expr, schema, isOptional);

    // A direct type is a type that is the same for SwiftyJson and our generated Swift data bindings.
    boolean isDirectType = schema.isPrimitive() || schema.getType() == DataSchema.Type.FIXED;

    if (isDirectType) {
      if (custom != null) {
        Expr coercer = expr(custom.getCoercerClass().getClassName());
        if (isOptional) {
          return directAccessor.map(coercer.coercerInput($0));
        } else {
          return coercer.coercerInput(directAccessor);
        }
      } else {
        return directAccessor;
      }
    } else {
      if (custom != null) {
        Expr coercer = expr(custom.getCoercerClass().getClassName());
        if (isOptional) {
          return directAccessor.map(coercer.coercerInput(readTypeExpr($0, spec)));
        } else {
          return coercer.coercerInput(readTypeExpr(directAccessor, spec));
        }
      } else {
        if (isOptional) {
          return directAccessor.map(readTypeExpr($0, spec));
        } else {
          return readTypeExpr(directAccessor, spec);
        }
      }
    }
  }

  private Expr readTypeExpr(Expr expr, ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    Expr className = expr(spec.getClassName());
    switch (schemaType) {
      case ENUM:
        return className.enumRead(expr);
      case RECORD:
      case UNION:
        return className.readJSON(expr);
      case MAP:
        CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
        ClassTemplateSpec valueSpec = selectMapValueType(mapSpec);
        return expr.mapValues(readTypeExpr($0, valueSpec, mapSpec.getCustomInfo(), false));
      case ARRAY:
        ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
        ClassTemplateSpec itemSpec = selectArrayItemsType(arraySpec);
        return expr.map(readTypeExpr($0, itemSpec, arraySpec.getCustomInfo(), false));
      default:
        throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  private Expr checkedTypeAccessorExpr(Expr expr, DataSchema schema, boolean isOptional) {
    Expr typeIdentifier = expr(typeEnumSymbol(schema));
    String typeAccessor = typeAccessor(schema, isOptional);
    if (isOptional) {
      return expr.optional(typeIdentifier).dot(typeAccessor);
    } else {
      return expr.required(typeIdentifier).dot(typeAccessor);
    }
  }

  /**
   * A SwiftyJson property name for a particular Pegasus type.
   *
   * E.g. a required string is accessed using the "stringValue" property.
   */
  private String typeAccessor(DataSchema schema, boolean isOptional) {
    String type = typeAccessor(schema);
    if (!isOptional) {
      return type + "Value";
    } else {
      return type;
    }
  }

  private String typeAccessor(DataSchema schema) {
    DataSchema.Type schemaType = schema.getType();
    String swiftyType = swiftyTypeAccessors.get(schemaType);
    if (swiftyType == null) {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
    return swiftyType;
  }

  private String typeEnumSymbol(DataSchema schema) {
    DataSchema.Type schemaType = schema.getType();
    String swiftyType = swiftyTypeEnumSymbols.get(schemaType);
    if (swiftyType == null) {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
    return swiftyType;
  }

  public String toSetAccessor(String anchor, UnionTemplateSpec.Member member) {
    // TODO(jbetz): apply custom type of union member
    return writeTypeExpr(expr(anchor), member.getClassTemplateSpec()).toSwiftCode();
  }

  public String toSetAccessor(String anchor, RecordTemplateSpec.Field field) {
    return writeTypeExpr(expr(anchor), selectFieldType(field), field.getCustomInfo()).toSwiftCode();
  }

  /**
   * Given an expression that evaluates to a Swift data binding type, and it's pegasus type,
   * return an expression that evaluates to the corresponding SwiftyJson JSON type.
   *
   * @param expr provides the expression that evaluates to a SwiftyJson JSON type.
   * @param spec provides the pegasus type that the returned expression should toSwiftCode to.
   */
  private Expr writeTypeExpr(Expr expr, ClassTemplateSpec spec, CustomInfoSpec customInfo) {
    if (customInfo != null) {
      Expr coercer = expr(customInfo.getCoercerClass().getClassName());
      return coercer.coercerOutput(writeTypeExpr(expr, spec));
    } else {
      return writeTypeExpr(expr, spec);
    }
  }

  private Expr writeTypeExpr(Expr expr, ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    switch (schemaType) {
      case ENUM:
        return expr.enumWrite();
      case RECORD:
      case UNION:
        return expr.writeData();
      case MAP:
        CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
        ClassTemplateSpec valueSpec = selectMapValueType(mapSpec);
        CustomInfoSpec customInfo = mapSpec.getCustomInfo();
        if (customInfo != null) {
          Expr coercer = expr(customInfo.getCoercerClass().getClassName());
          return expr.mapValues(writeTypeExpr(coercer.coercerOutput($0), valueSpec));
        } else {
          if (valueSpec.getSchema().isPrimitive()) {
            return expr;
          } else {
            return expr.mapValues(writeTypeExpr($0, valueSpec));
          }
        }
      case ARRAY:
        ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
        ClassTemplateSpec itemSpec = selectArrayItemsType(arraySpec);
        CustomInfoSpec arrayCustomInfo = arraySpec.getCustomInfo();
        if (arrayCustomInfo != null) {
          Expr coercer = expr(arrayCustomInfo.getCoercerClass().getClassName());
          return expr.map(writeTypeExpr(coercer.coercerOutput($0), itemSpec));
        } else {
          if (itemSpec.getSchema().isPrimitive()) {
            return expr;
          } else {
            return expr.map(writeTypeExpr($0, itemSpec));
          }
        }
      default:
        return expr;
    }
  }

  // Determine the field type to use in bindings.
  private ClassTemplateSpec selectFieldType(RecordTemplateSpec.Field field) {
    return getDereferencedType(field.getCustomInfo(), field.getType());
  }

  private ClassTemplateSpec selectMapValueType(CourierMapTemplateSpec mapSpec) {
    return getDereferencedType(mapSpec.getCustomInfo(), mapSpec.getValueClass());
  }

  private ClassTemplateSpec selectArrayItemsType(ArrayTemplateSpec arraySpec) {
    return getDereferencedType(arraySpec.getCustomInfo(), arraySpec.getItemClass());
  }

  // If the customInfo is null, return the fallback, otherwise get the dereferenced custom schema
  // type from the customInfo
  private ClassTemplateSpec getDereferencedType(CustomInfoSpec customInfo, ClassTemplateSpec fallback) {
    if (customInfo != null) {
      DataSchema refSchema = customInfo.getCustomSchema().getDereferencedDataSchema();
      return ClassTemplateSpec.createFromDataSchema(refSchema);
    } else {
      return fallback;
    }
  }

  /*
   * Utilities to track Swift "throws" propagation, since thrown errors must be explicitly
   * propagated at at each expression, including closures using "try".
   */
  // TODO(jbetz): This should be made considerably more type-safe.
  static class Expr {
    private final String expr;
    private final boolean directThrows; // This expression node directly throws
    private final boolean containsThrow; // This expression contains a sub-expression that throws
    private final boolean rethrows; // This expression "rethrows" (throws if it has a child expression that throws)

    public Expr(String expr) {
      this(expr, false, false, false);
    }

    public Expr(String expr, boolean directlyThrows, boolean containsThrow, boolean rethrows) {
      this.expr = expr;
      this.directThrows = directlyThrows;
      this.containsThrow = containsThrow;
      this.rethrows = rethrows;
    }

    public Expr map(Expr body) {
      return apply("map", body).rethrows();
    }

    public Expr mapValues(Expr body) {
      return apply("mapValues", body).rethrows();
    }

    public Expr readJSON(Expr param) {
      return call("readJSON", param).directlyThrows();
    }

    public Expr writeData() {
      return call("writeData");
    }

    public Expr enumRead(Expr param) {
      return call("read", param);
    }

    public Expr enumWrite() {
      return call("write");
    }

    public Expr coercerInput(Expr param) {
      return call("coerceInput", param).directlyThrows();
    }

    public Expr coercerOutput(Expr param) {
      return call("coerceOutput", param);
    }

    public Expr optional(Expr param) {
      return call("optional", param).directlyThrows();
    }

    public Expr required(Expr param) {
      return call("required", param).directlyThrows();
    }

    public Expr directlyThrows() {
      return new Expr(expr, true, true, rethrows);
    }

    public Expr rethrows() {
      return new Expr(expr, directThrows, containsThrow, true);
    }

    public Expr dot(String member) {
      return new Expr(expr + "." + member, false, containsThrow, true);
    }

    public Expr call(String method) {
      return new Expr(
        expr + "." + method + "()",
        false,
        containsThrow,
        false);
    }

    // rethrows indicates if the calls method is a rethrows method (e.g.
    // .map() is, but Enum.read is not).
    public Expr call(String method, Expr argument1) {
      return new Expr(
        expr + "." + method + "(" + argument1.toSwiftCode() + ")",
        false,
        containsThrow || argument1.containsThrow,
        false);
    }

    public Expr apply(String method, Expr body) {
      return new Expr(
        expr + "." + method + " { " + body.toSwiftCode() + " }",
        false,
        containsThrow || body.containsThrow,
        false);
    }

    public String toSwiftCode() {
      if (directThrows || (containsThrow && rethrows)) {
        return "try " + expr;
      } else {
        return expr;
      }
    }
  }

  static Expr $0 = expr("$0");

  static Expr expr(String expr) {
    return new Expr(expr);
  }
}
