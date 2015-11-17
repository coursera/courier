/*
 * Copyright 2015 Coursera Inc.
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

package org.coursera.courier.api;

import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec;

/**
 * @author Keren Jin
 */
public class CourierTyperefTemplateSpec extends TyperefTemplateSpec
{
  private ClassTemplateSpec _ref;
  public CourierTyperefTemplateSpec(TyperefDataSchema schema)
  {
    super(schema);
  }

  public ClassTemplateSpec getRef() {
    return _ref;
  }

  public void setRef(ClassTemplateSpec ref) {
    _ref = ref;
  }

  @Override
  public TyperefDataSchema getSchema()
  {
    return (TyperefDataSchema) super.getSchema();
  }
}
