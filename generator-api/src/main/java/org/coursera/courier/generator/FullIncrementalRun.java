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
import org.coursera.courier.api.GeneratorRunnerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * "Full" incremental generator run.
 *
 * Similar to a batch run except that the results of the run are used to compute a reference
 * graph that can be used for subsequent incremental generator runs.
 *
 * Should be used whenever incremental generation is enabled but no reference graph is available,
 * such as after a clean or fresh checkout.
 */
public class FullIncrementalRun extends IncrementalRunBase {
  public FullIncrementalRun(
      GeneratorRunnerOptions.IncrementalCompilationOptions options,
      File targetDirectory) throws IOException {
    super(options, targetDirectory);
  }

  @Override
  public String[] getSourcesToParse() {
    return options.changedSources;
  }

  @Override
  public DefaultGeneratorResult buildResult(
      Set<ClassTemplateSpec> topLevelSpecs,
      File targetDirectory,
      DataSchemaParser.ParseResult parseResult,
      Collection<File> targetFiles) throws IOException {

    // TODO(jbetz): Sort out how deletion should work for incremental.

    ReferenceGraph.buildReferenceGraph(topLevelSpecs, transformer).save(referencedBy);

    return new DefaultGeneratorResult(
      parseResult.getSourceFiles(),
      targetFiles,
      targetFiles);
  }
}
