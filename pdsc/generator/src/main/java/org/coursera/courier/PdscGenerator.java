/*
 * Copyright 2019 Coursera Inc.
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
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.pegasus.generator.GeneratorResult;
import com.linkedin.pegasus.generator.spec.*;
import org.coursera.courier.api.*;
import org.coursera.courier.lang.DocCommentStyle;
import org.coursera.courier.lang.PoorMansCStyleSourceFormatter;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


/**
 * .courier to .pdsc transformer
 */
public class PdscGenerator implements PegasusCodeGenerator {

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

    PegasusCodeGenerator generator = new PdscGenerator(sourcePaths);
    GeneratorResult result = new DefaultGeneratorRunner().run(generator, options);

    for (File file: result.getTargetFiles()) {
      System.out.println(file.getAbsolutePath());
    }
  }

  Set<String> sourcePaths;

  public PdscGenerator(String[] sourcePaths) {
    this.sourcePaths = Arrays.asList(sourcePaths)
            .stream().map(Paths::get)
            .map(path -> path.toAbsolutePath().toString())
            .collect(Collectors.toSet());
  }


  public static class PdscCompilationUnit extends GeneratedCodeTargetFile {
    public PdscCompilationUnit(String fullName) {
      super( fullName.replaceAll("\\.", "/") + ".pdsc");
    }
  }

  private static final PoorMansCStyleSourceFormatter formatter =
          new PoorMansCStyleSourceFormatter(2, DocCommentStyle.ASTRISK_MARGIN);

  @Override
  public GeneratedCode generate(ClassTemplateSpec templateSpec) {
    if (!this.sourcePaths.contains(templateSpec.getLocation())) {
      return null; // Only generate if declared in one of the target source files.
    }

    String code;
    if (templateSpec instanceof RecordTemplateSpec) {
      code = SchemaToPdscEncoder.schemaToPdsc((NamedDataSchema)templateSpec.getSchema());
    } else if (templateSpec instanceof EnumTemplateSpec) {
      code = SchemaToPdscEncoder.schemaToPdsc((NamedDataSchema)templateSpec.getSchema());
    } else if (templateSpec instanceof UnionTemplateSpec) {
      code = SchemaToPdscEncoder.schemaToPdsc(templateSpec.getOriginalTyperefSchema());
    } else if (templateSpec instanceof TyperefTemplateSpec) {
      code = SchemaToPdscEncoder.schemaToPdsc((NamedDataSchema)templateSpec.getSchema());
    } else if (templateSpec instanceof FixedTemplateSpec) {
      code = SchemaToPdscEncoder.schemaToPdsc((NamedDataSchema)templateSpec.getSchema());
    } else {
      return null; // Indicates that we are declining to generate code for the type (e.g. map or array)
    }
    PdscCompilationUnit compilationUnit = new PdscCompilationUnit(templateSpec.getFullName());
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
    return "pegasus";
  }

  @Override
  public String customTypeLanguage() {
    return "pegasus";
  }
}

