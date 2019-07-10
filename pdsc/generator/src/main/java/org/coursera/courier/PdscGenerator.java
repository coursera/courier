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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

/**
 * Courier code generator for Typescript.
 */
public class PdscGenerator implements PegasusCodeGenerator {

  public static void main(String[] args) throws Throwable {

    if (args.length < 3 || args.length > 5) {
      throw new IllegalArgumentException(
          "Usage: targetPath resolverPath1[:resolverPath2]+ sourcePath1[:sourcePath2]+ [REQUIRED_FIELDS_MAY_BE_ABSENT|STRICT] [EQUATABLE]");
    }
    String targetPath = args[0];
    String resolverPath = args[1];
    String sourcePathString = args[2];
    String[] sourcePaths = sourcePathString.split(":");

    GeneratorRunnerOptions options =
        new GeneratorRunnerOptions(targetPath, sourcePaths, resolverPath);

    GeneratorResult result =
      new DefaultGeneratorRunner().run(new PdscGenerator(), options);

    for (File file: result.getTargetFiles()) {
      System.out.println(file.getAbsolutePath());
    }

    InputStream runtime = PdscGenerator.class.getClassLoader().getResourceAsStream("runtime/CourierRuntime.ts");
    IOUtils.copy(runtime, new FileOutputStream(new File(targetPath, "CourierRuntime.ts")));
  }


  public static class PdscCompilationUnit extends GeneratedCodeTargetFile {
    public PdscCompilationUnit(String name, String namespace) {
      super(name, namespace, "pdsc");
    }
  }

  private static final PoorMansCStyleSourceFormatter formatter =
    new PoorMansCStyleSourceFormatter(2, DocCommentStyle.ASTRISK_MARGIN);

  /**
   * See {@link org.coursera.courier.tslite.TSProperties} for customization options.
   */
  @Override
  public GeneratedCode generate(ClassTemplateSpec templateSpec) {

    String code;
    if (templateSpec instanceof RecordTemplateSpec) {
      code = "test";
    } else if (templateSpec instanceof EnumTemplateSpec) {
      code = "test";
    } else if (templateSpec instanceof UnionTemplateSpec) {
      code = "test";
    } else if (templateSpec instanceof TyperefTemplateSpec) {
      TyperefTemplateSpec typerefSpec = (TyperefTemplateSpec) templateSpec;
      code = "test";
    } else if (templateSpec instanceof FixedTemplateSpec) {
      code = "test";
    } else {
      return null; // Indicates that we are declining to generate code for the type (e.g. map or array)
    }
    PdscCompilationUnit compilationUnit =
        new PdscCompilationUnit(
            templateSpec.getFullName(), "");
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
    return "pdsc";
  }

  @Override
  public String customTypeLanguage() {
    return "pdsc";
  }
}
