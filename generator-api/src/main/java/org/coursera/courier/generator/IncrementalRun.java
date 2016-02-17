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
import org.coursera.courier.api.GeneratorRunnerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * An incremental generator run.
 *
 * Only generates bindings if the source schema file has changed, or if schema files it
 * (transitively) depends on have changed.
 */
public class IncrementalRun extends IncrementalRunBase {
  private final ReferenceGraph referenceGraph;
  private final String[] incrementalSources;
  public IncrementalRun(
      GeneratorRunnerOptions.IncrementalCompilationOptions options,
      File targetDirectory) throws IOException {
    super(options, targetDirectory);
    Set<String> changed = new HashSet<>(Arrays.asList(options.changedSources));
    this.referenceGraph = ReferenceGraph.load(this.referencedBy);
    Set<String> incrementalSourceSet = referenceGraph.getTransitivelyReferencedBy(changed);
    this.incrementalSources = incrementalSourceSet.toArray(new String[incrementalSourceSet.size()]);
  }

  @Override
  public String[] getSourcesToParse() {
    return incrementalSources;
  }

  @Override
  public DefaultGeneratorResult buildResult(
    Set<ClassTemplateSpec> topLevelSpecs,
    File targetDirectory,
    DataSchemaParser.ParseResult parseResult,
    Collection<File> targetFiles) throws IOException {

    // TODO(jbetz): Sort out how deletion should work for incremental.

    // Full reference graph before the change
    ReferenceGraph graphToUpdate = ReferenceGraph.load(referencedBy);
    // Partial reference graph that includes all references from the changed types
    ReferenceGraph delta = ReferenceGraph.buildReferenceGraph(topLevelSpecs, transformer);
    graphToUpdate.applyDelta(delta);
    graphToUpdate.save(referencedBy);

    Collection<File> allFiles = FileUtils.listFiles(
      targetDirectory, new String[] { "scala" }, true); // TODO: pass in language file extension

    return new DefaultGeneratorResult(
      parseResult.getSourceFiles(),
      allFiles,
      allFiles);
  }
}
