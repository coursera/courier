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
import com.linkedin.data.schema.DataSchemaLocation;
import com.linkedin.pegasus.generator.DataSchemaParser;
import com.linkedin.pegasus.generator.DefaultGeneratorResult;
import com.linkedin.pegasus.generator.GeneratorResult;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import org.apache.commons.io.FileUtils;
import org.coursera.courier.incremental.ReferenceGraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    MultiFormatSchemaParser schemaParser =
        new MultiFormatSchemaParser(options.getResolverPath(), options.getParsersForFileFormats());

    CourierTemplateSpecGenerator specGenerator = new CourierTemplateSpecGenerator(
        schemaParser.getSchemaResolver(),
        options.getDataNamespace(),
        generator.customTypeLanguage());

    File targetDirectory = new File(options.getTargetDirectoryPath());

    try {
      FileUtils.forceMkdir(targetDirectory);
    } catch (IOException e) {
      throw new IOException(
          "Unable to create targetDirectory, or directory path exists but is not a directory: " +
              targetDirectory.getAbsolutePath(), e);
    }

    for (DataSchema defined: generator.definedSchemas()) {
      specGenerator.registerDefinedSchema(defined);
    }

    // TODO(jbetz): Trim the references file list to those that have actually changed or that are
    // new.  This needs to be computed from the data in the references file plus the list of
    // changed+new files provided by the build system (which should also be provided as an input
    // to this runner class). The result of the computation should be reduced set of files for the
    // getSources() list.
    GeneratorRunnerOptions.IncrementalCompilationOptions incrementalOptions =
      options.getIncrementalCompilationOptions();

    String[] sourcesToGenerate;
    boolean incrementalPass = false;
    if (incrementalOptions != null) {
      File referencedBy = new File(incrementalOptions.referencedByFilePath);
      if (!referencedBy.exists()) {
        sourcesToGenerate = options.getSources();
      } else {
        Set<String> changed = new HashSet<>(Arrays.asList(incrementalOptions.changedSources));
        Set<String> incrementalSources =
          ReferenceGraph.load(referencedBy).getTransitivelyReferencedBy(changed);
        sourcesToGenerate = incrementalSources.toArray(new String[incrementalSources.size()]);
        incrementalPass = true;
      }
    } else {
      sourcesToGenerate = options.getSources();
    }

    DataSchemaParser.ParseResult parseResult = schemaParser.parseSources(sourcesToGenerate);

    for (Map.Entry<DataSchema, DataSchemaLocation> entry: parseResult.getSchemaAndLocations().entrySet()) {
      specGenerator.generate(entry.getKey(), entry.getValue());
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

    // TODO(jbetz): Write references to a file for subsequent builds, the file name should be
    // provided as an input to this runner class
    if (incrementalOptions != null) {
      File referencedBy = new File(incrementalOptions.referencedByFilePath);
      SpecTransformer transformer = new SpecTransformer(
        new File(incrementalOptions.sourceDirectoryPath));
      if (incrementalPass) {
        // Full reference graph before the change
        ReferenceGraph graphToUpdate = ReferenceGraph.load(referencedBy);
        // Partial reference graph that includes all references from the changed types
        ReferenceGraph delta = ReferenceGraph.buildReferenceGraph(topLevelSpecs, transformer);
        System.err.println("delta: " + delta);
        graphToUpdate.applyDelta(delta);
        graphToUpdate.save(referencedBy);
      } else {
        ReferenceGraph.buildReferenceGraph(topLevelSpecs, transformer).save(referencedBy);
      }
    }

      // Write the resulting files.
    Collection<File> targetFiles = new HashSet<File>();
    for (GeneratedCode result: generated) {
      targetFiles.add(writeCode(targetDirectory, result));
    }

    // Delete any unrecognized files from target directory.
    // TODO(jbetz): Sort out how deletion should work for incremental.
    /*try {
      deleteUnrecognizedFiles(targetDirectory, targetFiles);
    } catch (IOException e) {
      throw new IOException(
          "Unexpected error while clearing unused files from targetDirectory:" +
              targetDirectory.getAbsolutePath(), e);
    }*/

    if (incrementalPass) {
      Collection<File> allFiles = FileUtils.listFiles(
        targetDirectory, new String[] { "scala" }, true); // TODO: pass in language file extension

      return new DefaultGeneratorResult(
        parseResult.getSourceFiles(),
        allFiles,
        allFiles);
    } else {
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
  }

  private static class SpecTransformer implements ReferenceGraph.SpecToGraphLabelTransformer {
    private File targetDirectory;

    public SpecTransformer(File targetDirectory) {
      this.targetDirectory = targetDirectory;
    }

    @Override
    public String toLabel(ClassTemplateSpec spec) {
      return new GeneratedCodeTargetFile(
        spec.getClassName(), spec.getNamespace(), "courier")
        .toFile(targetDirectory).getAbsolutePath();
    }
  }

  @SuppressWarnings("unchecked")
  private void deleteUnrecognizedFiles(
      File targetDirectory, Collection<File> targetFiles) throws IOException {
    Collection<File> existingFiles =
        (Collection<File>)FileUtils.listFiles(targetDirectory, null, true);

    for (File existingFile : existingFiles) {
      if (!targetFiles.contains(existingFile)) {
        FileUtils.forceDelete(existingFile);
      }
    }
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
    Set<ClassTemplateSpec> specs =
        new HashSet<ClassTemplateSpec>(ClassTemplateSpecs.allReferencedTypes(spec));
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
