package org.coursera.courier.py3;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;

public class GlobalConfig {
  public final Py3Properties defaults;

  public GlobalConfig(
      Py3Properties.Optionality defaultOptionality,
      boolean defaultEquatable,
      boolean defaultOmit) {
    defaults = new Py3Properties(defaultOptionality, defaultEquatable, defaultOmit);
  }

  public Py3Properties lookupPy3Properties(ClassTemplateSpec templateSpec) {
    DataSchema schema = templateSpec.getSchema();
    if (templateSpec instanceof UnionTemplateSpec && templateSpec.getOriginalTyperefSchema() != null) {
      schema = templateSpec.getOriginalTyperefSchema();
    }

    if (schema == null) {
      return defaults;
    } else {
      Object typescript = schema.getProperties().get("python3");
      if (typescript == null || !(typescript instanceof DataMap)) {
        return defaults;
      }
      DataMap properties = ((DataMap) typescript);

      String optionalityString = properties.getString("optionality");

      Py3Properties.Optionality optionality =
          optionalityString == null ? defaults.optionality : Py3Properties.Optionality.valueOf(optionalityString);

      Boolean maybeEquatable = properties.getBoolean("equatable");
      boolean equatable =  maybeEquatable == null ? defaults.equatable : maybeEquatable;

      Boolean maybeOmit = properties.getBoolean("omit");
      boolean omit = maybeOmit == null ? defaults.omit : maybeOmit;

      return new Py3Properties(optionality, equatable, omit);
    }
  }
}
