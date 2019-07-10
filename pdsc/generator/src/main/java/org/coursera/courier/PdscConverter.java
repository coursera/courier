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

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.pegasus.generator.GeneratorResult;
import com.linkedin.pegasus.generator.spec.*;
import org.coursera.courier.api.*;
import org.coursera.courier.lang.DocCommentStyle;
import org.coursera.courier.lang.PoorMansCStyleSourceFormatter;

import java.io.*;
import java.util.Collection;
import java.util.Collections;


/**
 * .courier to .pdsc transformer
 */
/**
 * Courier code generator for Typescript.
 */
public class PdscConverter implements PegasusCodeGenerator {

  public static void main(String[] args) throws Throwable {

    if (args.length != 3) {
      throw new IllegalArgumentException(
              "Usage: targetPath resolverPath1[:resolverPath2]+ sourcePath1[:sourcePath2]");
    }
    String targetPath = args[0];
    String resolverPath = args[1];
    String sourcePathString = args[2];
    String[] sourcePaths = sourcePathString.split(":");


    GeneratorRunnerOptions options =
            new GeneratorRunnerOptions(targetPath, sourcePaths, resolverPath);

    GeneratorResult result = new DefaultGeneratorRunner().run(new PdscConverter(), options);

    for (File file: result.getTargetFiles()) {
      System.out.println(file.getAbsolutePath());
    }

//    IOUtils.copy(runtime, new FileOutputStream(new File(targetPath, "CourierRuntime.ts")));
  }

  public PdscConverter() {
  }


  public static class PdscCompilationUnit extends GeneratedCodeTargetFile {
    public PdscCompilationUnit(String name, String namespace) {
      super(name, namespace, "pdsc");
    }
  }

  private static final PoorMansCStyleSourceFormatter formatter =
          new PoorMansCStyleSourceFormatter(2, DocCommentStyle.ASTRISK_MARGIN);

  @Override
  public GeneratedCode generate(ClassTemplateSpec templateSpec) {

    String code;
    if (templateSpec instanceof RecordTemplateSpec) {
      code = SchemaToPdscEncoder.schemaToPdsc(templateSpec.getSchema());
    } else if (templateSpec instanceof EnumTemplateSpec) {
      code = SchemaToPdscEncoder.schemaToPdsc(templateSpec.getSchema());
    } else if (templateSpec instanceof UnionTemplateSpec) {
      code = SchemaToPdscEncoder.schemaToPdsc(templateSpec.getSchema());
    } else if (templateSpec instanceof TyperefTemplateSpec) {
      TyperefTemplateSpec typerefSpec = (TyperefTemplateSpec) templateSpec;
      code = SchemaToPdscEncoder.schemaToPdsc(templateSpec.getSchema());
    } else if (templateSpec instanceof FixedTemplateSpec) {
      code = SchemaToPdscEncoder.schemaToPdsc(templateSpec.getSchema());
    } else {
      return null; // Indicates that we are declining to generate code for the type (e.g. map or array)
    }
    PdscCompilationUnit compilationUnit =
            new PdscCompilationUnit(
                    templateSpec.getFullName(), "");
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

