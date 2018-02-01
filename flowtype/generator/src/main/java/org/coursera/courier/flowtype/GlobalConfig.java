package org.coursera.courier.flowtype;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;

public class GlobalConfig {
  public final FlowtypeProperties defaults;

  public GlobalConfig(
      FlowtypeProperties.Optionality defaultOptionality,
      boolean defaultEquatable,
      boolean defaultOmit) {
    defaults = new FlowtypeProperties(defaultOptionality, defaultEquatable, defaultOmit);
  }

  public FlowtypeProperties lookupFlowtypeProperties(ClassTemplateSpec templateSpec) {
    DataSchema schema = templateSpec.getSchema();
    if (templateSpec instanceof UnionTemplateSpec && templateSpec.getOriginalTyperefSchema() != null) {
      schema = templateSpec.getOriginalTyperefSchema();
    }

    if (schema == null) {
      return defaults;
    } else {
      Object typescript = schema.getProperties().get("typescript");
      if (typescript == null || !(typescript instanceof DataMap)) {
        return defaults;
      }
      DataMap properties = ((DataMap) typescript);

      String optionalityString = properties.getString("optionality");

      FlowtypeProperties.Optionality optionality =
          optionalityString == null ? defaults.optionality : FlowtypeProperties.Optionality.valueOf(optionalityString);

      Boolean maybeEquatable = properties.getBoolean("equatable");
      boolean equatable =  maybeEquatable == null ? defaults.equatable : maybeEquatable;

      Boolean maybeOmit = properties.getBoolean("omit");
      boolean omit = maybeOmit == null ? defaults.omit : maybeOmit;

      return new FlowtypeProperties(optionality, equatable, omit);
    }
  }
}
