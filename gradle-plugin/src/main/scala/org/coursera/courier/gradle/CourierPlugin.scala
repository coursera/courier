package org.coursera.courier.gradle

import java.io.File

import org.coursera.courier.generator.ScalaDataTemplateGenerator
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.scala.ScalaCompile
import org.gradle.plugins.ide.idea.GenerateIdeaModule

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/**
 * Provides a gradle plugin for Courier.
 *
 * This is a an port of:
 *
 * https://github.com/linkedin/rest.li/blob/master/gradle-plugins/
 * src/main/groovy/com/linkedin/pegasus/gradle/PegasusPlugin.groovy#L1163
 */
// TODO(jbetz): cross compile to scala 2.10 and 2.11 ?
class CourierPlugin extends Plugin[Project] {

  // Source set specific state.
  case class SourceSetConfig(
      project: Project,
      sourceSet: SourceSet,
      compile: Configuration,
      dataModel: Configuration,
      courierDataBinding: Configuration,
      courierCompile: Configuration) {

    val sourceSetContainer = project.
      getProperties.get("sourceSets").asInstanceOf[SourceSetContainer]

    val  dataSchemaDir = project.file(getDataSchemaPath(project, sourceSet))

    val generatedDataBindingDir = project.file(getGeneratedDirPath(
        project, sourceSet, genType = "Courier") + File.separatorChar + "scala")

    val generateDataBindingTask = sourceSet.getTaskName("generate", "courier")

    val targetSourceSetName = getGeneratedSourceSetName(sourceSet, "Courier")

    val dataBindingJarName = sourceSet.getName + "CourierJar"
  }

  /**
   * Adds Scala data binding generation to the project.
   */
  def apply(project: Project): Unit = {
    val configurations = project.getConfigurations

    /**
     * Configurations for depending on data schemas, generating data bindings
     * and publishing jars containing data schemas to the project artifacts.
     *
     * Used to declare dependencies on generated bindings and .pdsc files from other
     * projects/artifacts.
     */
    val dataModel = configurations.maybeCreate("dataModel")
    val testDataModel = configurations.maybeCreate("testDataModel")
      .setExtendsFrom(Seq(dataModel).asJava)

    /**
     * This is the configuration that is intended to be used in build.scala files to declare
     * dependencies for the compilation phase of the generated code.
     *
     * This is used purely for it's classpath.
     *
     * TODO(jbetz): implicitly add scala lib to this
     */
    val courierCompile = configurations.maybeCreate("courierCompile")
      .setVisible(false)

    /**
     * Configurations for publishing jars containing data schemas and generated data templates
     * to the project artifacts.
     *
     * Published data template jars depends on the configurations used to compile the classes
     * in the jar, this includes the data models/templates used by the data template generator
     * and the classes used to compile the generated classes.
     */
    val courierDataBinding = configurations.maybeCreate("courier")
      .setExtendsFrom(Seq(courierCompile, dataModel).asJava)

    val testCourierDataBinding = configurations.maybeCreate("testCourier")
      .setExtendsFrom(Seq(courierDataBinding, testDataModel).asJava)

    /**
     * Standard compile configurations for this project.
     */
    val compile = project.getConfigurations.findByName("compile")
    val testCompile = project.getConfigurations.findByName("testCompile")

    // Apply plugin settings to all source sets in the project.
    val sourceSets = project.getProperties.get("sourceSets").asInstanceOf[SourceSetContainer]
    Seq(sourceSets.asScala.toSeq: _*).foreach { sourceSet =>
      if (!sourceSet.getName.equalsIgnoreCase("generated")) {
        val courierConfigurations = if (isTestSourceSet(sourceSet)) {
          SourceSetConfig(
            project, sourceSet, testCompile, testDataModel, testCourierDataBinding, courierCompile)
        } else {
          SourceSetConfig(
            project, sourceSet, compile, dataModel, courierDataBinding, courierCompile)
        }
        configureGeneration(courierConfigurations)
      }
    }
  }

  // Adapted from
  // https://github.com/linkedin/rest.li/blob/master/gradle-plugins/src/main/groovy/com/
  // linkedin/pegasus/gradle/PegasusPlugin.groovy#L1163
  def configureGeneration(config: SourceSetConfig): Unit = {

    config.dataSchemaDir.mkdirs()

    // Attach the GeneratorTask task to the sourceSet of the project.
    val generatorTask = config.project.task(
      Map("type" -> classOf[GeneratorTask]).asJava,
      config.generateDataBindingTask).asInstanceOf[GeneratorTask]
    generatorTask.inputDir = config.dataSchemaDir
    generatorTask.destinationDir = config.generatedDataBindingDir
    generatorTask.resolverPath = config.dataModel
    generatorTask.onlyIf(new Spec[Task] {
      override def isSatisfiedBy(t: Task): Boolean = {
        config.dataSchemaDir.exists()
      }
    })

  // TODO(jbetz): support source jars
  // TODO(jbetz): support javadoc jars

  // Create new source set for generated code.
  val targetSourceSet = config.sourceSetContainer.maybeCreate(config.targetSourceSetName)

  // Add compile classpath to generated code source set.
  // TODO: only scala should be set for this sourceSet, java and resources should be removed.
  targetSourceSet.setCompileClasspath(config.dataModel.plus(config.courierCompile))

  // Intellij plugin.
  addGeneratedDir(
    config.project, targetSourceSet, Seq(config.dataModel, config.courierCompile).asJava)

    // Make sure that code is generated before compiling it.
  val compileGeneratedClassesTask =
    config.project.getTasks.findByName(targetSourceSet.getCompileTaskName("scala"))
  compileGeneratedClassesTask.dependsOn(generatorTask)

    // Create data binding jar file.
    val dataBindingJarTask = config.project.task(
      Map("type" -> classOf[Jar]).asJava, config.dataBindingJarName)
        .dependsOn(compileGeneratedClassesTask).asInstanceOf[Jar]

    dataBindingJarTask.from(targetSourceSet.getOutput)
    dataBindingJarTask.from(config.dataSchemaDir)

      // This eachFile applies to all files (both .class and .pdsc)
    dataBindingJarTask.eachFile(new Action[FileCopyDetails] {
      def execute(t: FileCopyDetails): Unit = {
        if(t.getPath.endsWith(".pdsc")) {
          t.setPath(s"pegasus${File.separatorChar}${t.getPath}")
        }
      }
    })

    dataBindingJarTask.setAppendix(getAppendix(config.sourceSet, "courier"))
    dataBindingJarTask.setDescription("Generate a data binding jar")

    // Add the data model and courier date binding jars to the list of project artifacts.
    val artifacts = config.project.getArtifacts
    artifacts.add(config.courierDataBinding.getName, dataBindingJarTask)

    // Include additional dependencies into the appropriate configuration used to compile the
    // input source set must include the generated data binding classes and their dependencies
    // the configuration.
    config.compile
        .extendsFrom(config.dataModel)
        .extendsFrom(config.courierCompile)

    config.project.getDependencies.add(
      config.compile.getName, config.project.files(dataBindingJarTask.getArchivePath))

    // Add dependency for jar task, which transitively depends on other tasks.
    config.project.getTasks.findByName(
      config.sourceSet.getCompileTaskName("scala")).dependsOn(dataBindingJarTask)
  }

  private[this] def getAppendix(sourceSet: SourceSet, suffix: String): String = {
    if (sourceSet.getName.equals("main")) suffix else s"${sourceSet.getName}-$suffix"
  }

  private[this] def getDataSchemaPath(project: Project, sourceSet: SourceSet): String = {
    getOverridePath(project, sourceSet, "overridePegasusDir").getOrElse(
      s"src${File.separatorChar}${sourceSet.getName}${File.separatorChar}pegasus")
  }

  private[this] def getOverridePath(
      project: Project, sourceSet: SourceSet, overridePropertyName: String): Option[String] = {
    val sourceSetPropertyName = s"${sourceSet.getName}.$overridePropertyName"
    val overrideProperty = getNonEmptyProperty(project, sourceSetPropertyName)

    if (overrideProperty.isEmpty && sourceSet.getName.equals("main")) {
      getNonEmptyProperty(project, overridePropertyName)
    } else {
      overrideProperty
    }
  }

  private[this] def getNonEmptyProperty(project: Project, propertyName: String): Option[String] = {
    if (project.hasProperty(propertyName)) {
      val propertyValue = project.property(propertyName).toString
      if (!propertyValue.isEmpty) {
        Some(propertyValue)
      } else {
        None
      }
    } else {
      None
    }
  }

  private[this] def getGeneratedDirPath(
      project: Project, sourceSet: SourceSet, genType: String): String = {
    val overridePath = getOverridePath(project, sourceSet, "overrideGeneratedDir")
    val sourceSetName = getGeneratedSourceSetName(sourceSet, genType)
    val base = overridePath.getOrElse("src")
    s"$base${File.separatorChar}$sourceSetName"
  }

  private[this] def getGeneratedSourceSetName(sourceSet: SourceSet, genType: String): String = {
    s"${sourceSet.getName}Generated$genType"
  }

  private[this] def TEST_DIR_REGEX = "^(integ)?[Tt]est".r
  private[this] def isTestSourceSet(sourceSet: SourceSet): Boolean = {
    TEST_DIR_REGEX.findFirstMatchIn(sourceSet.getName).isDefined
  }

  private[this] def addGeneratedDir(
      project: Project,
      sourceSet: SourceSet,
      configurations: java.util.Collection[Configuration]): Unit = {

    Option(project.getTasks.findByName("ideaModule")).map { ideaModuleTask =>
      val ideaModule = ideaModuleTask.asInstanceOf[GenerateIdeaModule].getModule

      val scalaCompileTask = project.getTasks.findByName(sourceSet.getCompileTaskName("scala"))
        .asInstanceOf[ScalaCompile]

      if (isTestSourceSet(sourceSet)) {
        val sourceDirs = ideaModule.getTestSourceDirs
        sourceDirs.addAll(scalaCompileTask.getSource.getFiles)
        ideaModule.setTestSourceDirs(sourceDirs)
      }
      else {
        val sourceDirs = ideaModule.getSourceDirs
        sourceDirs.addAll(scalaCompileTask.getSource.getFiles)
        ideaModule.setSourceDirs(sourceDirs)
      }

      val compilePlus = ideaModule.getScopes.get("COMPILE").get("plus")
      compilePlus.addAll(configurations)
      ideaModule.getScopes.get("COMPILE").put("plus", compilePlus)
    }
  }
}

class GeneratorTask extends DefaultTask {
  @OutputDirectory @BeanProperty var destinationDir: File = _
  @InputDirectory @BeanProperty var inputDir: File = _
  @InputFiles @BeanProperty var resolverPath: FileCollection = _

  @TaskAction
  protected def generate(): Unit = {
    destinationDir.delete()
    destinationDir.mkdirs()

    val includes = Seq(s"**${File.separatorChar}*pdsc").asJava
    val pdscFiles = getProject.fileTree(
      Map(
        "dir" -> inputDir,
        "includes" -> includes).asJava)
    val pdscFileArray = pdscFiles.iterator().asScala.map(_.getAbsolutePath).toArray
    val resolverPathList = resolverPath.asScala.toList
    val resolverPathStr = (inputDir :: resolverPathList).mkString(File.pathSeparator)

    ScalaDataTemplateGenerator.run(
      resolverPath = resolverPathStr,
      defaultPackage =  "",
      generateImported = false,
      targetDirectoryPath = destinationDir.getAbsolutePath,
      sources = pdscFileArray,
      generateTyperefs = false)
  }
}
