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

package org.coursera.courier;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.GeneratorResult;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;
import org.coursera.courier.api.DefaultGeneratorRunner;
import org.coursera.courier.api.GeneratedCode;
import org.coursera.courier.api.GeneratedCodeTargetFile;
import org.coursera.courier.api.GeneratorRunnerOptions;
import org.coursera.courier.api.PegasusCodeGenerator;
import org.coursera.courier.js.GlobalConfig;
import org.coursera.courier.js.JsProperties;
import org.coursera.courier.lang.DocCommentStyle;
import org.coursera.courier.lang.PoorMansCStyleSourceFormatter;
import org.rythmengine.RythmEngine;
import org.rythmengine.exception.RythmException;
import org.rythmengine.resource.ClasspathResourceLoader;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

/**
 * Courier code generator for js.
 */
public class ReactGenerator implements PegasusCodeGenerator {
  private final GlobalConfig globalConfig;
  private final RythmEngine engine;

  public static void main(String[] args) throws Throwable {
    // TODO(jbetz): use a CLI parser library

    if (args.length < 3 || args.length > 5) {
      throw new IllegalArgumentException(
          "Usage: targetPath resolverPath1[:resolverPath2]+ sourcePath1[:sourcePath2]+");
    }
    String targetPath = args[0];
    String resolverPath = args[1];
    String sourcePathString = args[2];
    String[] sourcePaths = sourcePathString.split(":");

    GeneratorRunnerOptions options =
        new GeneratorRunnerOptions(targetPath, sourcePaths, resolverPath);

    GlobalConfig globalConfig = new GlobalConfig(false);
    GeneratorResult result =
      new DefaultGeneratorRunner().run(new ReactGenerator(globalConfig), options);

    for (File file: result.getTargetFiles()) {
      System.out.println(file.getAbsolutePath());
    }
  }

  public ReactGenerator() {
    this(new GlobalConfig(false));
  }

  public ReactGenerator(GlobalConfig globalConfig) {
    this.globalConfig = globalConfig;
    this.engine = new RythmEngine();
    this.engine.registerResourceLoader(new ClasspathResourceLoader(engine, "/"));
  }

  public static class JsCompilationUnit extends GeneratedCodeTargetFile {
    public JsCompilationUnit(String name, String namespace) {
      super(name, namespace, "jsx");
    }
  }

  private static final PoorMansCStyleSourceFormatter formatter =
    new PoorMansCStyleSourceFormatter(4, DocCommentStyle.NO_MARGIN);

  /**
   * See {@link JsProperties} for customization options.
   */
  @Override
  public GeneratedCode generate(ClassTemplateSpec templateSpec) {

    String code;
    JsProperties jsProperties = globalConfig.lookupJsProperties(templateSpec);
    if (jsProperties.omit) return null;

    try {
      if (templateSpec instanceof RecordTemplateSpec) {
        code = engine.render("rythm-react/record.txt", templateSpec);
      } else if (templateSpec instanceof EnumTemplateSpec) {
        code = engine.render("rythm-react/enum.txt", templateSpec);
      } else if (templateSpec instanceof UnionTemplateSpec) {
        code = engine.render("rythm-react/union.txt", templateSpec);
      } else {
        return null; // Indicates that we are declining to generate code for the type (e.g. map or array)
      }
    } catch (RythmException e) {
      throw new RuntimeException(
        "Internal error in generator while processing " + templateSpec.getFullName(), e);
    }
    JsCompilationUnit compilationUnit =
        new JsCompilationUnit(
            templateSpec.getClassName(), "");
    code = formatter.format(code);
    return new GeneratedCode(compilationUnit, code);
  }

  @Override
  public Collection<GeneratedCode> generatePredef() {
    return Collections.emptySet();
  }

  @Override
  public Collection<DataSchema> definedSchemas() {
    return Collections.emptySet();
  }

  @Override
  public String buildLanguage() {
    return "js";
  }

  @Override
  public String customTypeLanguage() {
    return "js";
  }
}
