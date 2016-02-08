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
import org.apache.commons.io.IOUtils;
import org.coursera.courier.api.DefaultGeneratorRunner;
import org.coursera.courier.api.GeneratedCode;
import org.coursera.courier.api.GeneratedCodeTargetFile;
import org.coursera.courier.api.GeneratorRunnerOptions;
import org.coursera.courier.api.PegasusCodeGenerator;
import org.coursera.courier.lang.DocCommentStyle;
import org.coursera.courier.lang.PoorMansCStyleSourceFormatter;
import org.coursera.courier.swift.GlobalConfig;
import org.coursera.courier.swift.SwiftProperties;
import org.coursera.courier.swift.SwiftSyntax;
import org.coursera.courier.swift.SwiftyJSON;
import org.rythmengine.RythmEngine;
import org.rythmengine.exception.RythmException;
import org.rythmengine.resource.ClasspathResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

/**
 * Courier code generator for Swift.
 */
public class SwiftGenerator implements PegasusCodeGenerator {
  private static final SwiftProperties.Optionality defaultOptionality =
      SwiftProperties.Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT;

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

    SwiftProperties.Optionality optionality = defaultOptionality;
    if (args.length > 3) {
      optionality = SwiftProperties.Optionality.valueOf(args[3]);
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
      new DefaultGeneratorRunner().run(new SwiftGenerator(globalConfig), options);

    for (File file: result.getTargetFiles()) {
      System.out.println(file.getAbsolutePath());
    }

    InputStream runtime = ClassLoader.getSystemResourceAsStream("runtime/CourierRuntime.swift");
    IOUtils.copy(runtime, new FileOutputStream(new File(targetPath, "CourierRuntime.swift")));
  }

  public SwiftGenerator() {
    this(new GlobalConfig(
        defaultOptionality,
        false,
        false));
  }

  public SwiftGenerator(GlobalConfig globalConfig) {
    this.globalConfig = globalConfig;
    this.engine = new RythmEngine();
    this.engine.registerResourceLoader(new ClasspathResourceLoader(engine, "/"));
  }

  public static class SwiftCompilationUnit extends GeneratedCodeTargetFile {
    public SwiftCompilationUnit(String name, String namespace) {
      super(name, namespace, "swift");
    }
  }

  private static final PoorMansCStyleSourceFormatter formatter =
    new PoorMansCStyleSourceFormatter(4, DocCommentStyle.NO_MARGIN);

  /**
   * See {@link org.coursera.courier.swift.SwiftProperties} for customization options.
   */
  @Override
  public GeneratedCode generate(ClassTemplateSpec templateSpec) {

    String code;
    SwiftProperties swiftProperties = globalConfig.lookupSwiftProperties(templateSpec);
    if (swiftProperties.omit) return null;

    SwiftSyntax syntax = new SwiftSyntax(templateSpec, swiftProperties, globalConfig);
    SwiftyJSON swifty = new SwiftyJSON(syntax);

    try {
      if (templateSpec instanceof RecordTemplateSpec) {
        code = engine.render("rythm-swift/record.txt", templateSpec, syntax, swifty);
      } else if (templateSpec instanceof EnumTemplateSpec) {
        code = engine.render("rythm-swift/enum.txt", templateSpec);
      } else if (templateSpec instanceof UnionTemplateSpec) {
        code = engine.render("rythm-swift/union.txt", templateSpec, syntax, swifty);
      } else {
        return null; // Indicates that we are declining to generate code for the type (e.g. map or array)
      }
    } catch (RythmException e) {
      throw new RuntimeException(
        "Internal error in generator while processing " + templateSpec.getFullName(), e);
    }
    SwiftCompilationUnit compilationUnit =
        new SwiftCompilationUnit(
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
    return "swift";
  }

  @Override
  public String customTypeLanguage() {
    return "swift";
  }
}
