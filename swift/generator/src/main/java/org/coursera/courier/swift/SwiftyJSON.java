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
    return toTryGetAccessor(anchor, spec, isOptional).toString();
  }

  private TryAccessor toTryGetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    if (spec.getSchema().isPrimitive() || spec.getSchema().getType() == DataSchema.Type.FIXED) {
      return toAccessor(anchor, spec, isOptional);
    } else {
      return toWrappedTryGetAccessor(anchor, spec, isOptional);
    }
  }

  private TryAccessor toWrappedTryGetAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    TryAccessor directAccessor = toAccessor(anchor, spec, isOptional);
    if (isOptional) {
      TryAccessor wrapped = toWrappedTryGetAccessor("$0", spec);
      return wrapped.maybeTry(directAccessor + ".map { "+ wrapped + " }");
    } else {
      return toWrappedTryGetAccessor(directAccessor.toString(), spec);
    }
  }

  private static class TryAccessor {
    public final String accessor;
    public final Boolean mightThrow;
    public TryAccessor(String accessor, Boolean mightThrow) {
      this.accessor = accessor;
      this.mightThrow = mightThrow;
    }

    public TryAccessor maybeTry(String wrappedAccessor) {
      if (mightThrow) {
        return new TryAccessor("try " + wrappedAccessor, true);
      } else {
        return new TryAccessor(wrappedAccessor, false);
      }
    }

    public String toString() {
      return accessor;
    }
  }

  private static TryAccessor accessorWithTry(String accessor) {
    return new TryAccessor(accessor, true);
  }

  private static TryAccessor accessor(String accessor) {
    return new TryAccessor(accessor, false);
  }

  private TryAccessor toWrappedTryGetAccessor(String anchor, ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    if (schemaType == DataSchema.Type.ENUM) {
      EnumTemplateSpec enumSpec = (EnumTemplateSpec)spec;
      return accessor(enumSpec.getClassName() + ".read(" + anchor + ")");
    } else if (schemaType == DataSchema.Type.RECORD) {
      RecordTemplateSpec recordSpec = (RecordTemplateSpec)spec;
      return accessorWithTry("try " + recordSpec.getClassName() + ".readJSON(" + anchor + ")");
    } else if (schemaType == DataSchema.Type.UNION) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec)spec;
      return accessorWithTry("try " + unionSpec.getClassName() + ".readJSON(" + anchor + ")");
    } else if (schemaType == DataSchema.Type.MAP) {
      CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
      TryAccessor wrapped = toTryGetAccessor("$0", mapSpec.getValueClass(), false);
      return wrapped.maybeTry(anchor + ".mapValues { " + wrapped.toString() + " }");
    } else if (schemaType == DataSchema.Type.ARRAY) {
      ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
      TryAccessor wrapped = toTryGetAccessor("$0", arraySpec.getItemClass(), false);
      return wrapped.maybeTry(anchor + ".map { " + wrapped.toString() + " }");
    } else {
      throw new IllegalArgumentException("unrecognized type: " + schemaType);
    }
  }

  public TryAccessor toAccessor(String anchor, ClassTemplateSpec spec, boolean isOptional) {
    return accessor(anchor + "." + maybeDirectOptional(swiftyType(spec), isOptional));
  }

  public String swiftyType(ClassTemplateSpec spec) {
    DataSchema.Type schemaType = spec.getSchema().getType();
    String swiftyType = schemaTypeToSwiftyType.get(schemaType);
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
}
