package org.coursera.courier.swift;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;

public class GlobalConfig {
  public final SwiftProperties defaults;

  public GlobalConfig(
      SwiftProperties.Optionality defaultOptionality,
      boolean defaultEquatable,
      boolean defaultOmit) {
    defaults = new SwiftProperties(defaultOptionality, defaultEquatable, defaultOmit);
  }

  public SwiftProperties lookupSwiftProperties(ClassTemplateSpec templateSpec) {
    DataSchema schema = templateSpec.getSchema();
    if (templateSpec instanceof UnionTemplateSpec && templateSpec.getOriginalTyperefSchema() != null) {
      schema = templateSpec.getOriginalTyperefSchema();
    }

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

      Boolean maybeOmit = properties.getBoolean("omit");
      boolean omit = maybeOmit == null ? defaults.omit : maybeOmit;

      return new SwiftProperties(optionality, equatable, omit);
    }
  }
}
