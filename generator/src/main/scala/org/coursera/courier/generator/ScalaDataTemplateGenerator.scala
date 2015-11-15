/*
 Copyright 2015 Coursera Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.coursera.courier.generator

import com.linkedin.pegasus.generator.JavaCodeGeneratorBase
import com.linkedin.pegasus.generator.PegasusDataTemplateGenerator
import org.coursera.courier.ScalaGenerator
import org.coursera.courier.api.DefaultGeneratorRunner
import org.coursera.courier.api.GeneratorRunnerOptions

import scala.collection.JavaConverters._

/**
 * Generates Scala data bindings classes from .pdsc schemas.
 *
 * Both the class and companion object hook into the extension points of rest.li-sbt-plugin.
 */
object ScalaDataTemplateGenerator {
  def main(args: Array[String]) {
    if (args.length < 2) {
      println(
        s"Usage: ${ScalaDataTemplateGenerator.getClass.getName} targetDirectoryPath [sourceFile " +
        "or sourceDirectory or schemaName]+")
      System.exit(1)
    }
    val targetDirectoryPath = args(0)
    val resolverPath = args(1)
    val sources = java.util.Arrays.copyOfRange(args, 1, args.length)
    val generateImported =
      Option(System.getProperty(PegasusDataTemplateGenerator.GENERATOR_GENERATE_IMPORTED))
        .exists(_.toBoolean)
    val defaultPackage = System.getProperty(JavaCodeGeneratorBase.GENERATOR_DEFAULT_PACKAGE)
    val generateTyperefs = false
    val generatePredef = false // set to true temporarily to manually generate predef

    val result = new DefaultGeneratorRunner().run(
      new ScalaGenerator(),
      new GeneratorRunnerOptions(
        targetDirectoryPath,
        sources,
        resolverPath)
        .setDefaultPackage(defaultPackage)
        .setDataNamespace(CourierPredef.dataNamespace)
        .setGenerateImported(generateImported)
        .setGenerateTyperefs(generateTyperefs)
        .setGeneratePredef(generatePredef))

    result.getTargetFiles.asScala.foreach { file =>
      System.out.println(file.getAbsolutePath)
    }
  }
}
