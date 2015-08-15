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

public class GeneratorRunnerOptions {
  private String resolverPath;
  private String targetDirectoryPath;
  private String[] sources;

  private String defaultPackage = "";
  private String dataNamespace = "org.coursera.courier.data";

  private boolean generateImported = false;
  private boolean generateTyperefs = false;
  private boolean generatePredef = false;

  public GeneratorRunnerOptions(
      String targetDirectoryPath,
      String[] sources,
      String resolverPath) {
    this.targetDirectoryPath = targetDirectoryPath;
    this.sources = sources;
    this.resolverPath = resolverPath;
  }

  public GeneratorRunnerOptions setDefaultPackage(String defaultPackage) {
    this.defaultPackage = defaultPackage;
    return this;
  }

  public GeneratorRunnerOptions setDataNamespace(String defaultNamespace) {
    this.dataNamespace = defaultNamespace;
    return this;
  }

  public GeneratorRunnerOptions setGenerateImported(boolean generateImported) {
    this.generateImported = generateImported;
    return this;
  }

  public GeneratorRunnerOptions setGenerateTyperefs(boolean generateTyperefs) {
    this.generateTyperefs = generateTyperefs;
    return this;
  }

  public GeneratorRunnerOptions setGeneratePredef(boolean generatePredef) {
    this.generatePredef = generatePredef;
    return this;
  }

  public String getResolverPath() {
    return resolverPath;
  }

  public String getTargetDirectoryPath() {
    return targetDirectoryPath;
  }

  public String[] getSources() {
    return sources;
  }

  public String getDefaultPackage() {
    return defaultPackage;
  }

  public String getDataNamespace() {
    return dataNamespace;
  }

  public boolean isGenerateImported() {
    return generateImported;
  }

  public boolean isGenerateTyperefs() {
    return generateTyperefs;
  }

  public boolean isGeneratePredef() {
    return generatePredef;
  }
}
