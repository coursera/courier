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

package org.coursera.courier.maven;

import com.linkedin.pegasus.generator.GeneratorResult;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.coursera.courier.api.DefaultGeneratorRunner;
import org.coursera.courier.api.GeneratorRunnerOptions;
import org.coursera.courier.api.PegasusCodeGenerator;

import java.io.File;
import java.util.Collection;

@Mojo(
  name = "schemas",
  defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class CourierMojo extends AbstractMojo {

  /**
   * Sources
   */
  @Parameter(
    defaultValue = "${basedir}/src/main/courier")
  File sourceDirectory;

  @Parameter(
    defaultValue = "${project.build.directory}/generated-sources/courier")
  File outputDirectory;

  @Parameter(defaultValue = "org.coursera.courier.ScalaGenerator")
  String codeGenerator;

  @Override
  public void execute() {
    Collection<File> files = FileUtils.listFiles(
      sourceDirectory,
      new String[] {"pdsc", "courier"},
      true);

    String[] sources = new String[files.size()];
    int i = 0;
    for (File f: files) {
      sources[i++] = f.getAbsolutePath();
    }
    GeneratorRunnerOptions options =
      new GeneratorRunnerOptions(
        outputDirectory.getAbsolutePath(),
        sources,
        sourceDirectory.getAbsolutePath());
    try {

      PegasusCodeGenerator generator = (PegasusCodeGenerator)Class.forName(codeGenerator).newInstance();

      GeneratorResult result = new DefaultGeneratorRunner().run(
        generator,
        options);

      getLog().info("source files: " + result.getSourceFiles());
      getLog().info("modified files: " + result.getModifiedFiles());
      getLog().info("target files: " + result.getTargetFiles());

    } catch (Throwable e) {
      getLog().error(e.getMessage());
    }
  }
}
