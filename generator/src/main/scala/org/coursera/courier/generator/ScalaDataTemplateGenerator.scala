package org.coursera.courier.generator

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

import com.linkedin.data.schema._
import com.linkedin.data.schema.generator.AbstractGenerator
import com.linkedin.pegasus.generator.CodeGenerator
import com.linkedin.pegasus.generator.GeneratorResult
import com.linkedin.pegasus.generator.DataTemplateGenerator
import com.typesafe.scalalogging.slf4j.StrictLogging

import scala.collection.JavaConverters._

import treehugger.forest._
import definitions._
import treehuggerDSL._

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
      Option(System.getProperty(DataTemplateGenerator.GENERATOR_GENERATE_IMPORTED))
        .exists(_.toBoolean)
    run(
      System.getProperty(AbstractGenerator.GENERATOR_RESOLVER_PATH),
      System.getProperty(CodeGenerator.GENERATOR_DEFAULT_PACKAGE),
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
    val config = new DataTemplateGenerator.Config(
      resolverPath,
      defaultPackage,
      Option(generateImported).getOrElse(java.lang.Boolean.FALSE))
    val targetDirectory  = new File(targetDirectoryPath)
    targetDirectory.mkdirs()
    assert(targetDirectory.exists() && targetDirectory.isDirectory,
      s"Unable to create ${targetDirectory.getAbsolutePath}. Directory either does not exist " +
      s"after attempting to create it, or part of the path exists and is not a directory.")

    val generator = new ScalaDataTemplateGenerator(config, targetDirectory)
    generator.generate(targetDirectoryPath, sources)
  }
}

class ScalaDataTemplateGenerator(
    config: DataTemplateGenerator.Config,
    targetDirectory: File)
  extends DataTemplateGenerator with StrictLogging with SchemaHandler {

  private[this] val scalaGenerator = new TreehuggerDataTemplateGenerator(this)

  // This logic is essentially the same as found in PegasusDataTemplateGenerator.
  // It checks for updated .pdsc files and only calls the generator for file that have changed.
  private[generator] def generate(
      targetDirectoryPath: String,
      sources: Array[String]): GeneratorResult = {
    initializeDefaultPackage()
    initSchemaResolver()
    val sourceFiles = parseSources(sources).asScala
    val targetFiles = super.targetFiles(targetDirectory).asScala
    val modifiedFiles =
      if (upToDate(sourceFiles.asJava, targetFiles.asJava)) {
        logger.info(s"Target files are up-to-date: $targetFiles")
        List.empty
      }
      else {
        logger.info(s"Generating ${targetFiles.size} files: $targetFiles")
        validateDefinedClassRegistration()
        targetFiles
      }
    new DataTemplateGenerator.Result(
      sourceFiles.asJavaCollection,
      targetFiles.asJavaCollection,
      modifiedFiles.asJavaCollection)
  }

  override def handleSchema(schema: DataSchema): Unit = {
    try {
      writeCode(scalaGenerator.generate(schema))
    } catch {
      case t: Throwable =>
        logger.error(t.getMessage)
    }
  }

  private[this] def writeCode(generated: GeneratedCode): Unit = {
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
      stream.write(treeToString(generated.tree))
    } finally {
      stream.close()
    }
  }

  override def getConfig: DataTemplateGenerator.Config = config
}
