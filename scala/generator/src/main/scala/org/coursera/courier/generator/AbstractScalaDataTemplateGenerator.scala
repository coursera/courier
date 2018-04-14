package org.coursera.courier.generator

import com.linkedin.pegasus.generator.JavaCodeGeneratorBase
import com.linkedin.pegasus.generator.PegasusDataTemplateGenerator
import org.coursera.courier.ConfigurableScalaGenerator
import org.coursera.courier.api.DefaultGeneratorRunner
import org.coursera.courier.api.GeneratorRunnerOptions

import scala.collection.JavaConverters._

/**
 * Generates Scala data bindings classes from .pdsc schemas.
 *
 * Both the class and companion object hook into the extension points of rest.li-sbt-plugin.
 */
abstract class AbstractScalaDataTemplateGenerator(mixins: GeneratorMixin) {
  def main(args: Array[String]) {
    if (args.length != 3) {
      println(
        s"Usage: ${this.getClass.getName} " +
          s"targetDirectoryPath resolverPath sourcePath1[:sourcePath2]+")
      System.exit(1)
    }
    val targetDirectoryPath = args(0)
    val resolverPath = args(1)
    val sources = args(2).split(java.io.File.pathSeparator)
    val generateImported =
      Option(System.getProperty(PegasusDataTemplateGenerator.GENERATOR_GENERATE_IMPORTED))
        .exists(_.toBoolean)
    val defaultPackage = System.getProperty(JavaCodeGeneratorBase.GENERATOR_DEFAULT_PACKAGE)
    val generateTyperefs = false
    val generatePredef = false // set to true temporarily to manually generateRecord predef

    val result = new DefaultGeneratorRunner().run(
      new ConfigurableScalaGenerator(mixins),
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
