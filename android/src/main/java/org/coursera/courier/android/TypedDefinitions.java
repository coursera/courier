package org.coursera.courier.android;

import com.linkedin.data.DataMap;
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;

import java.util.HashMap;
import java.util.Map;

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
