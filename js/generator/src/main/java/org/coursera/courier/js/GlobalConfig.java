/*
 * Copyright 2016 Coursera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.coursera.courier.js;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;

public class GlobalConfig {
  public final JsProperties defaults;

  public GlobalConfig(boolean defaultOmit) {
    defaults = new JsProperties(defaultOmit);
  }

  public JsProperties lookupJsProperties(ClassTemplateSpec templateSpec) {
    DataSchema schema = templateSpec.getSchema();
    if (templateSpec instanceof UnionTemplateSpec && templateSpec.getOriginalTyperefSchema() != null) {
      schema = templateSpec.getOriginalTyperefSchema();
    }

    if (schema == null) {
      return defaults;
    } else {
      Object js = schema.getProperties().get("js");
      if (js == null || !(js instanceof DataMap)) {
        return defaults;
      }
      DataMap properties = ((DataMap) js);

      Boolean maybeOmit = properties.getBoolean("omit");
      boolean omit = maybeOmit == null ? defaults.omit : maybeOmit;

      return new JsProperties(omit);
    }
  }
}
