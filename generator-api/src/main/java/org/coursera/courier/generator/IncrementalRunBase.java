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

import com.linkedin.data.schema.NamedDataSchema;
import org.coursera.courier.api.GeneratedCodeTargetFile;
import org.coursera.courier.api.GeneratorRunnerOptions;

import java.io.File;
import java.io.IOException;

/**
 * Common base for IncrementalRun and FullIncrementalRun.
 */
public abstract class IncrementalRunBase implements GeneratorRun {
  protected final GeneratorRunnerOptions.IncrementalCompilationOptions options;
  protected final File referencedBy;
  protected final SpecTransformer transformer;

  public IncrementalRunBase(GeneratorRunnerOptions.IncrementalCompilationOptions options, File targetDirectory) throws IOException {
    this.options = options;
    this.referencedBy = new File(options.referencedByFilePath);
    this.transformer = new SpecTransformer(targetDirectory);
  }

  private static class SpecTransformer implements ReferenceGraph.SpecToGraphLabelTransformer {
    private File targetDirectory;

    public SpecTransformer(File targetDirectory) {
      this.targetDirectory = targetDirectory;
    }

    @Override
    public String toLabel(NamedDataSchema schema) {
      return new GeneratedCodeTargetFile(
        schema.getName(), schema.getNamespace(), "courier")
        .toFile(targetDirectory).getAbsolutePath();
    }
  }
}
