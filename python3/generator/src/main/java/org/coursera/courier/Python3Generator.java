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

package org.coursera.courier;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.GeneratorResult;
import com.linkedin.pegasus.generator.spec.*;
import org.apache.commons.io.IOUtils;
import org.coursera.courier.api.DefaultGeneratorRunner;
import org.coursera.courier.api.GeneratedCode;
import org.coursera.courier.api.GeneratedCodeTargetFile;
import org.coursera.courier.api.GeneratorRunnerOptions;
import org.coursera.courier.api.PegasusCodeGenerator;
import org.coursera.courier.lang.DocCommentStyle;
import org.coursera.courier.lang.PoorMansCStyleSourceFormatter;
import org.coursera.courier.py3.GlobalConfig;
import org.coursera.courier.py3.Py3Properties;
import org.coursera.courier.py3.Py3Syntax;
import org.rythmengine.RythmEngine;
import org.rythmengine.conf.RythmConfigurationKey;
import org.rythmengine.exception.RythmException;
import org.rythmengine.resource.ClasspathResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

/**
 * Courier code generator for Typescript.
 */
public class Python3Generator implements PegasusCodeGenerator {
  private static final Py3Properties.Optionality defaultOptionality =
      Py3Properties.Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT;

  private final GlobalConfig globalConfig;
  private final RythmEngine engine;

  public static void main(String[] args) throws Throwable {
    // TODO(jbetz): use a CLI parser library

    if (args.length < 3 || args.length > 5) {
      throw new IllegalArgumentException(
          "Usage: targetPath resolverPath1[:resolverPath2]+ sourcePath1[:sourcePath2]+ [REQUIRED_FIELDS_MAY_BE_ABSENT|STRICT] [EQUATABLE]");
    }
    String targetPath = args[0];
    String resolverPath = args[1];
    String sourcePathString = args[2];
    String[] sourcePaths = sourcePathString.split(":");

    Py3Properties.Optionality optionality = defaultOptionality;
    if (args.length > 3) {
      optionality = Py3Properties.Optionality.valueOf(args[3]);
    }

    boolean equatable = false;
    if (args.length > 4) {
      if (!args[4].equals("EQUATABLE")) {
        throw new IllegalArgumentException("If present 4th argument must be 'EQUATABLE'");
      }
      equatable = true;
    }

    GeneratorRunnerOptions options =
        new GeneratorRunnerOptions(targetPath, sourcePaths, resolverPath);

    GlobalConfig globalConfig = new GlobalConfig(optionality, equatable, false);
    GeneratorResult result =
      new DefaultGeneratorRunner().run(new Python3Generator(globalConfig), options);

    for (File file: result.getTargetFiles()) {
      System.out.println(file.getAbsolutePath());
    }

    InputStream runtime = Python3Generator.class.getClassLoader().getResourceAsStream("runtime/courier.py");
    IOUtils.copy(runtime, new FileOutputStream(new File(targetPath, "courier.py")));
  }

  public Python3Generator() {
    this(new GlobalConfig(
        defaultOptionality,
        false,
        false));
  }

  public Python3Generator(GlobalConfig globalConfig) {
    this.globalConfig = globalConfig;
    Properties props = new Properties();
    props.setProperty(RythmConfigurationKey.CODEGEN_COMPACT_ENABLED.getKey(), "false");
    this.engine = new RythmEngine(props);
    this.engine.registerResourceLoader(new ClasspathResourceLoader(engine, "/"));
  }

  public static class Py3CompilationUnit extends GeneratedCodeTargetFile {
    public Py3CompilationUnit(String name, String namespace) {
      super(Py3Syntax.escapeKeyword(name, Py3Syntax.EscapeStrategy.MANGLE), namespace, "py");
    }
  }

  public static class Py3GeneratedCode extends GeneratedCode {
    public Py3GeneratedCode(GeneratedCodeTargetFile target, String code) {
      super(target, code);
    }
  }

  /**
   * See {@link Py3Properties} for customization options.
   */
  @Override
  public GeneratedCode generate(ClassTemplateSpec templateSpec) {

    String code;
    Py3Properties Py3Properties = globalConfig.lookupPy3Properties(templateSpec);
    if (Py3Properties.omit) return null;

    Py3Syntax syntax = new Py3Syntax(Py3Properties);
    try {
      if (templateSpec instanceof RecordTemplateSpec) {
        code = engine.render("rythm-py3/record.txt", syntax.new Py3RecordSyntax((RecordTemplateSpec) templateSpec));
      } else if (templateSpec instanceof EnumTemplateSpec) {
        code = engine.render("rythm-py3/enum.txt", syntax.new Py3EnumSyntax((EnumTemplateSpec) templateSpec));
      } else if (templateSpec instanceof UnionTemplateSpec) {
        code = engine.render("rythm-py3/union.txt", syntax.new Py3UnionSyntax((UnionTemplateSpec) templateSpec));
      } else if (templateSpec instanceof TyperefTemplateSpec) {
        TyperefTemplateSpec typerefSpec = (TyperefTemplateSpec) templateSpec;
        code = engine.render("rythm-py3/typeref.txt", syntax.Py3TyperefSyntaxCreate(typerefSpec));
      } else if (templateSpec instanceof FixedTemplateSpec) {
        code = engine.render("rythm-py3/fixed.txt", syntax.Py3FixedSyntaxCreate((FixedTemplateSpec) templateSpec));
      } else {
        return null; // Indicates that we are declining to generate code for the type (e.g. map or array)
      }
    } catch (RythmException e) {
      throw new RuntimeException(
        "Internal error in generator while processing " + templateSpec.getFullName(), e);
    }
    Py3CompilationUnit compilationUnit =
        new Py3CompilationUnit(
            templateSpec.getClassName(), templateSpec.getNamespace());

    return new Py3GeneratedCode(compilationUnit, code);
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
    return "python3";
  }

  @Override
  public String customTypeLanguage() {
    return "python3";
  }
}
