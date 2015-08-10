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

import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.resolver.FileDataSchemaLocation;
import com.linkedin.pegasus.generator.CodeUtil;
import com.linkedin.pegasus.generator.DataSchemaParser;
import com.linkedin.pegasus.generator.DefaultGeneratorResult;
import com.linkedin.pegasus.generator.GeneratorResult;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Runs a {@link org.coursera.courier.api.PegasusCodeGenerator} for all the .pdsc files in a
 * project.
 */
public class DefaultGeneratorRunner implements GeneratorRunner {
  @Override
  public GeneratorResult run(
      PegasusCodeGenerator generator,
      GeneratorRunnerOptions options) throws IOException {
    DataSchemaParser schemaParser = new DataSchemaParser(options.getResolverPath());
    CourierTemplateSpecGenerator specGenerator = new CourierTemplateSpecGenerator(
        schemaParser.getSchemaResolver(), options.getDataNamespace());

    File targetDirectory = new File(options.getTargetDirectoryPath());
    targetDirectory.delete();
    targetDirectory.mkdirs();

    if (!targetDirectory.exists() || !targetDirectory.isDirectory()) {
      throw new IllegalArgumentException(
          "unable to create directory, or directory path exists but is not a directory: " +
              targetDirectory.getAbsolutePath());
    }

    for (DataSchema defined: generator.definedSchemas()) {
      specGenerator.registerDefinedSchema(defined);
    }

    DataSchemaParser.ParseResult parseResult = schemaParser.parseSources(options.getSources());

    for (CodeUtil.Pair<DataSchema, File> pair: parseResult.getSchemaAndFiles()) {
      FileDataSchemaLocation location = new FileDataSchemaLocation(pair.second);
      specGenerator.generate(pair.first, location);
    }

    // Build a set of top level types so that we only generate each class file exactly once
    // and so that we don't accidentally stack overflow if types are recursively defined.
    Set<ClassTemplateSpec> topLevelSpecs = new HashSet<ClassTemplateSpec>();
    for(ClassTemplateSpec generatedSpec: specGenerator.getGeneratedSpecs()) {
      topLevelSpecs.addAll(findTopLevelTypes(generatedSpec));
    }

    // Run the generator.
    Collection<GeneratedCode> generated;
    if (options.isGeneratePredef()) {
      generated = generator.generatePredef();
    } else {
      generated = new HashSet<GeneratedCode>();
      for (ClassTemplateSpec topLevel : topLevelSpecs) {
        GeneratedCode generatedForTopLevel = generator.generate(topLevel);
        if (generatedForTopLevel != null) {
          generated.add(generatedForTopLevel);
        }
      }
    }

    // Write the resulting files.
    Collection<File> targetFiles = new HashSet<File>();
    for (GeneratedCode result: generated) {
      targetFiles.add(writeCode(targetDirectory, result));
    }

    // CourierPlugin.prepareCacheUpdate checks if the generator needs to run using an SBT utility,
    // so if we get here we know we should unconditionally run the generator. As a result, the
    // modifiedFiles are always the same as the target files. (if we instead, used
    // FileUtils.upToDate here to do the check, modifiedFiles might be empty if all files are
    // upToDate).
    Collection<File> modifiedFiles = targetFiles;

    return new DefaultGeneratorResult(
        parseResult.getSourceFiles(),
        targetFiles,
        modifiedFiles);
  }


  /**
   * Currently, one ClassDefinition is provided per .pdsc file. But some of those .pdsc contain
   * inline schema definitions that should be generated into top level classes.
   *
   * This method traverses the spec hierarchy, finding all specs that should be generated as top
   * level classes.
   *
   * I've asked the rest.li team to consider restructuring the generator utilities so that one
   * ClassDefinition per top level class is provided. If they restructure the utilities, this
   * method should no longer be needed.
   */
  private static Set<ClassTemplateSpec> findTopLevelTypes(ClassTemplateSpec spec) {
    Set<ClassTemplateSpec> specs = new HashSet<ClassTemplateSpec>(ClassTemplateSpecs.allReferencedTypes(spec));
    specs.add(spec);

    Iterator<ClassTemplateSpec> iterator = specs.iterator();
    while (iterator.hasNext()) {
      ClassTemplateSpec entry = iterator.next();
      if (entry.getEnclosingClass() != null) {
        iterator.remove();
      }
    }
    return specs;
  }

  private static File writeCode(
      File targetDirectory, GeneratedCode generated) throws IOException {
    GeneratedCodeTargetFile target = generated.getTarget();
    File file = target.toFile(targetDirectory);
    File directory = file.getParentFile();
    directory.mkdirs();
    if (!directory.exists() || !directory.isDirectory()) {
      throw new IllegalArgumentException(
          "unable to create directory, or directory path exists but is not a directory: " +
              directory.getAbsolutePath());
    }

    if (!file.exists()) {
      if (!file.createNewFile()) {
        throw new IllegalArgumentException("unable to create file: " + file.getAbsolutePath());
      }
    }

    PrintWriter stream = new PrintWriter(new FileOutputStream(file));
    try {
      stream.write(generated.getCode());
    } finally {
      stream.close();
    }
    return file;
  }
}
