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

package org.coursera.courier.android;

import com.linkedin.data.DataMap;
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides utilities to:
 * - Identify if a type definition has been registered with a Pegasus union schema.
 * - Extract the typed definitions mapping from the schema.
 *
 * Typed definitions are an alternate representation of unions. There are two main formats:
 *
 * TypedDefinition JSON:
 *
 * <code>
 *   {
 *     "typeName": "...",
 *     "definition": { "field1": ..., "field2": ... }
 *   }
 * </code>
 *
 * FlatTypedDefinition JSON:
 *
 * <code>
 *   {
 *     "typeName": "...",
 *     "field1": ...,
 *     "field2": ...
 *   }
 * </code>
 */
public class TypedDefinitions {

  public static boolean isTypedDefinition(UnionTemplateSpec unionSpec) {
    TyperefTemplateSpec typerefClass = unionSpec.getTyperefClass();
    return (typerefClass != null && typerefClass.getSchema().getProperties().containsKey("typedDefinition"));
  }

  public static boolean isFlatTypedDefinition(UnionTemplateSpec unionSpec) {
    TyperefTemplateSpec typerefClass = unionSpec.getTyperefClass();
    return (typerefClass != null && typerefClass.getSchema().getProperties().containsKey("flatTypedDefinition"));
  }

  public static Map<String, String> getTypedDefinitionMapping(UnionTemplateSpec unionSpec, boolean flat) {
    String propertyName = flat ? "flatTypedDefinition" : "typedDefinition";
    TyperefTemplateSpec typerefClass = unionSpec.getTyperefClass();
    DataMap properties = (DataMap)typerefClass.getSchema().getProperties().get(propertyName);
    HashMap<String, String> results = new HashMap<String, String>();
    for (String memberKey: properties.keySet()) {
      String typeName = properties.getString(memberKey);
      results.put(memberKey, typeName);
    }
    return results;
  }
}
