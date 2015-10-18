package org.coursera.courier.swift;

import java.util.Arrays;
import java.util.Map;

public class ModuleMapping {

  private Map<String, String> namespaceToModule;
  public ModuleMapping(Map<String, String> namespaceToModule) {
    this.namespaceToModule = namespaceToModule;
  }

  public ModuleClass toModuleClass(String namespace, String classPath) {
    if (namespaceToModule.containsKey(namespace)) {
      return new ModuleClass(namespaceToModule.get(namespace), classPath);
    } else {
      int index = namespace.lastIndexOf('.');
      if (index >= 0) {
        String nextNamespace = namespace.substring(0, index);
        String remainder = namespace.substring(0, index + 1);
        return toModuleClass(nextNamespace, remainder + "." + classPath);
      } else {
        return null;
      }
    }
  }
}
