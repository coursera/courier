package org.coursera.courier.swift;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;

public class GlobalConfig {
  public final SwiftProperties defaults;

  public GlobalConfig(SwiftProperties.Optionality defaultOptionality, boolean defaultEquatable) {
    defaults = new SwiftProperties(defaultOptionality, defaultEquatable);
  }

  public SwiftProperties lookupSwiftProperties(ClassTemplateSpec templateSpec) {
    DataSchema schema = templateSpec.getSchema();
    if (schema == null) {
      return defaults;
    } else {
      Object swift = schema.getProperties().get("swift");
      if (swift == null || !(swift instanceof DataMap)) {
        return defaults;
      }
      DataMap properties = ((DataMap) swift);

      String optionalityString = properties.getString("optionality");

      SwiftProperties.Optionality optionality =
          optionalityString == null ? defaults.optionality : SwiftProperties.Optionality.valueOf(optionalityString);

      Boolean maybeEquatable = properties.getBoolean("equatable");
      boolean equatable =  maybeEquatable == null ? defaults.equatable : maybeEquatable;

      return new SwiftProperties(optionality, equatable);
    }
  }
}
