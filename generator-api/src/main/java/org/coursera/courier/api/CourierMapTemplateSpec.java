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

import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.MapDataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.CustomInfoSpec;

/**
 * Modified version of {@link com.linkedin.pegasus.generator.spec.MapTemplateSpec} that
 * adds support for Courier specific functionality.
 *
 * @author Keren Jin
 */
// TODO(jbetz): Submit pull requests to the rest.li project to provide extension points
// that we can use instead of keeping a modified version of this class.
public class CourierMapTemplateSpec extends ClassTemplateSpec
{
  private ClassTemplateSpec _valueClass;
  private ClassTemplateSpec _valueDataClass;
  private CustomInfoSpec _customInfo;

  // For Courier, add typed map key support.
  private DataSchema _keySchema;
  private ClassTemplateSpec _keyClass;
  private ClassTemplateSpec _keyDataClass;
  private CustomInfoSpec _keyCustomInfo;

  public CourierMapTemplateSpec(MapDataSchema schema)
  {
    setSchema(schema);
  }

  @Override
  public MapDataSchema getSchema()
  {
    return (MapDataSchema) super.getSchema();
  }

  public ClassTemplateSpec getValueClass()
  {
    return _valueClass;
  }

  public void setValueClass(ClassTemplateSpec valueClass)
  {
    _valueClass = valueClass;
  }

  public ClassTemplateSpec getValueDataClass()
  {
    return _valueDataClass;
  }

  public void setValueDataClass(ClassTemplateSpec valueDataClass)
  {
    _valueDataClass = valueDataClass;
  }

  public CustomInfoSpec getCustomInfo()
  {
    return _customInfo;
  }

  public void setCustomInfo(CustomInfoSpec customInfo)
  {
    _customInfo = customInfo;
  }

  public DataSchema getKeySchema()
  {
    return _keySchema;
  }

  public void setKeySchema(DataSchema keySchema) { _keySchema = keySchema; }

  public ClassTemplateSpec getKeyClass()
  {
    return _keyClass;
  }

  public void setKeyClass(ClassTemplateSpec keyClass)
  {
    _keyClass = keyClass;
  }

  public ClassTemplateSpec getKeyDataClass()
  {
    return _keyDataClass;
  }

  public void setKeyDataClass(ClassTemplateSpec keyDataClass)
  {
    _keyDataClass = keyDataClass;
  }

  public CustomInfoSpec getKeyCustomInfo()
  {
    return _keyCustomInfo;
  }

  public void setKeyCustomInfo(CustomInfoSpec keyCustomInfo)
  {
    _keyCustomInfo = keyCustomInfo;
  }
}
