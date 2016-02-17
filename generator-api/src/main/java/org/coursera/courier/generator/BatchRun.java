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

package org.coursera.courier.generator;

import com.linkedin.pegasus.generator.DataSchemaParser;
import com.linkedin.pegasus.generator.DefaultGeneratorResult;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Simple batch generator run. All data bindings are regenerated from source schema files
 * on each run.
 */
public class BatchRun implements GeneratorRun {
  private final String[] sources;

  public BatchRun(String[] sources) {
    this.sources = sources;
  }

  @Override
  public String[] getSourcesToParse() {
    return sources;
  }

  @Override
  public DefaultGeneratorResult buildResult(
    Set<ClassTemplateSpec> topLevelSpecs,
    File targetDirectory,
    DataSchemaParser.ParseResult parseResult,
    Collection<File> targetFiles) throws IOException {

    // Delete any unrecognized files from target directory.
    try {
      deleteUnrecognizedFiles(targetDirectory, targetFiles);
    } catch (IOException e) {
      throw new IOException(
        "Unexpected error while clearing unused files from targetDirectory:" +
          targetDirectory.getAbsolutePath(), e);
    }

    // CourierPlugin.prepareCacheUpdate checks if the generator needs to run using an SBT utility,
    // so if we get here we know we should unconditionally run the generator. As a result, the
    // modifiedFiles are always the same as the target files. (if we instead, used
    // FileUtils.upToDate here to do the check, modifiedFiles might be empty if all files are
    // upToDate).
    return new DefaultGeneratorResult(
      parseResult.getSourceFiles(),
      targetFiles,
      targetFiles);
  }

  @SuppressWarnings("unchecked")
  private void deleteUnrecognizedFiles(
    File targetDirectory, Collection<File> targetFiles) throws IOException {
    Collection<File> existingFiles = FileUtils.listFiles(targetDirectory, null, true);

    for (File existingFile : existingFiles) {
      if (!targetFiles.contains(existingFile)) {
        FileUtils.forceDelete(existingFile);
      }
    }
  }
}
