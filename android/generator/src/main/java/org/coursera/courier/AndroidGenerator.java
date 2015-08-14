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
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;
import org.coursera.courier.android.JavaSyntax;
import org.coursera.courier.android.PoorMansJavaSourceFormatter;
import org.coursera.courier.android.TypedDefinitions;
import org.coursera.courier.api.DefaultGeneratorRunner;
import org.coursera.courier.api.GeneratedCode;
import org.coursera.courier.api.GeneratedCodeTargetFile;
import org.coursera.courier.api.GeneratorRunnerOptions;
import org.coursera.courier.api.PegasusCodeGenerator;
import org.rythmengine.RythmEngine;
import org.rythmengine.resource.ClasspathResourceLoader;

import java.util.Collection;
import java.util.Collections;

public class AndroidGenerator implements PegasusCodeGenerator {
  private final RythmEngine engine;

  public static void main(String[] args) throws Throwable {
    if (args.length != 3) {
      throw new IllegalArgumentException(
          "Usage: targetPath resolverPath sourcePath1[:sourcePath2]+");
    }
    String targetPath = args[0];
    String resolverPath = args[1];
    String sourcePathString = args[2];
    String[] sourcePaths = sourcePathString.split(":");

    new DefaultGeneratorRunner().run(
        new AndroidGenerator(),
        new GeneratorRunnerOptions(targetPath, sourcePaths, resolverPath));
  }

  public AndroidGenerator() {
    engine = new RythmEngine();
    engine.registerResourceLoader(new ClasspathResourceLoader(engine, "/"));
  }

  public static class JavaCompilationUnit extends GeneratedCodeTargetFile {
    public JavaCompilationUnit(String name, String namespace) {
      super(name, namespace, "java");
    }
  }

  @Override
  public GeneratedCode generate(ClassTemplateSpec templateSpec) {

    String code;
    if (templateSpec instanceof RecordTemplateSpec) {
      code = engine.render("rythm/record.txt", templateSpec);
    } else if (templateSpec instanceof EnumTemplateSpec) {
      code = engine.render("rythm/enum.txt", templateSpec);
    } else if (templateSpec instanceof UnionTemplateSpec) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec) templateSpec;
      if (TypedDefinitions.isTypedDefinition(unionSpec)) {
        code = engine.render(
            "rythm/typedDefinition.txt",
            templateSpec,
            TypedDefinitions.getTypedDefinitionMapping(unionSpec, false));
      } else if (TypedDefinitions.isFlatTypedDefinition(unionSpec)) {
        code = engine.render(
            "rythm/flatTypedDefinition.txt",
            templateSpec,
            TypedDefinitions.getTypedDefinitionMapping(unionSpec, true));
      } else {
        code = engine.render("rythm/union.txt", templateSpec);
      }
    } else {
      return null; // Indicates that we are declining to generate code for the type (e.g. map or array)
    }
    JavaCompilationUnit compilationUnit = new JavaCompilationUnit(JavaSyntax.escapeKeyword(templateSpec.getClassName()), templateSpec.getNamespace());
    code = PoorMansJavaSourceFormatter.format(code);

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
  public String language() {
    return "java";
  }
}
