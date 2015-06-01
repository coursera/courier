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

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

import com.linkedin.data.schema.generator.AbstractGenerator
import com.linkedin.data.schema.resolver.FileDataSchemaLocation
import com.linkedin.pegasus.generator.DataSchemaParser
import com.linkedin.pegasus.generator.DefaultGeneratorResult
import com.linkedin.pegasus.generator.GeneratorResult
import com.linkedin.pegasus.generator.JavaCodeGeneratorBase
import com.linkedin.pegasus.generator.PegasusDataTemplateGenerator
import com.linkedin.pegasus.generator.TemplateSpecGenerator
import com.linkedin.util.FileUtil
import org.coursera.courier.generator.specs.Definition
import org.coursera.courier.generator.twirl.TwirlDataTemplateGenerator

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
    val generateImported =
      Option(System.getProperty(PegasusDataTemplateGenerator.GENERATOR_GENERATE_IMPORTED))
        .exists(_.toBoolean)
    run(
      System.getProperty(AbstractGenerator.GENERATOR_RESOLVER_PATH),
      System.getProperty(JavaCodeGeneratorBase.GENERATOR_DEFAULT_PACKAGE),
      generateImported,
      args(0),
      java.util.Arrays.copyOfRange(args, 1, args.length))
  }

  // rest.li-sbt-plugin expects this exact signature for the run method.
  def run(
      resolverPath: String,
      defaultPackage: String,
      generateImported: java.lang.Boolean,
      targetDirectoryPath: String,
      sources: Array[String]): GeneratorResult = {

    val schemaParser = new DataSchemaParser(resolverPath)
    val specGenerator = new TemplateSpecGenerator(schemaParser.getSchemaResolver)

    val targetDirectory = new File(targetDirectoryPath)
    targetDirectory.mkdirs()
    assert(targetDirectory.exists() && targetDirectory.isDirectory,
      s"Unable to create ${targetDirectory.getAbsolutePath}. Directory either does not exist " +
      s"after attempting to create it, or part of the path exists and is not a directory.")

    val generator = new TwirlDataTemplateGenerator()

    TypeConversions.primitiveSchemas.foreach { primitiveSchema =>
      specGenerator.registerDefinedSchema(primitiveSchema)
    }

    val parseResult = schemaParser.parseSources(sources)

    parseResult.getSchemaAndFiles.asScala.foreach { pair =>
      val location = new FileDataSchemaLocation(pair.second)
      specGenerator.generate(pair.first, location)
    }
    val generatedSpecs = specGenerator.getGeneratedSpecs.asScala

    val compilationUnits = generatedSpecs.flatMap { spec =>
      generator.generate(Definition(spec))
    }

    // TODO: needed? SBT plugin already figures out which files have changed.
    //val targetFiles: List[File] = JavaCodeUtil.targetFiles(targetDirectory, generator.getCodeModel, JavaCodeUtil.classLoaderFromResolverPath(schemaParser.getResolverPath), checker)

    val targetFiles = compilationUnits.map { compilationUnit =>
      writeCode(targetDirectory, compilationUnit)
    }

    val modifiedFiles = if (FileUtil.upToDate(parseResult.getSourceFiles, targetFiles.asJavaCollection)) {
      //logger.info("Target files are up-to-date: " + targetFiles)
      Seq()
    } else {
      //logger.info("Generating " + targetFiles.size + " files: " + targetFiles)
      //validateDefinedClassRegistration(dataTemplateGenerator.getCodeModel, dataTemplateGenerator.getGeneratedClasses.keySet)
      //dataTemplateGenerator.getCodeModel.build(new FileCodeWriter(targetDirectory, true))
      targetFiles

    }
    new DefaultGeneratorResult(parseResult.getSourceFiles, targetFiles.asJavaCollection, modifiedFiles.asJavaCollection)
  }

  private[this] def writeCode(targetDirectory: File, generated: GeneratedCode): File = {
    val compilationUnit = generated.compilationUnit

    val namespacePath = compilationUnit.namespace.replace(".", File.separator)
    val directory = new File(targetDirectory, namespacePath)
    directory.mkdirs()
    assert(directory.exists() && directory.isDirectory,
      s"Unable to create ${directory.getAbsolutePath}. Directory either does not exist after " +
        s"attempting to create it, or part of the path exists and is not a directory.")

    val file = new File(directory, s"${compilationUnit.name}.scala")
    if (!file.exists()) {
      assert(file.createNewFile(), s"Unable to create file ${file.getAbsolutePath}")
    }

    val stream = new PrintWriter(new FileOutputStream(file))
    try {
      stream.write(generated.code)
    } finally {
      stream.close()
    }
    file
  }
}
