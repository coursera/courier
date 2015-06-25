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
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Jar
import org.gradle.plugins.ide.idea.GenerateIdeaModule
import org.gradle.api.specs.Spec

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/**
 * Provides a gradle plugin for Courier.
 */
class CourierPlugin extends Plugin[Project] {

  // Source set specific state.
  case class SourceSetConfig(
      project: Project,
      sourceSet: SourceSet,
      compile: Configuration,
      dataModel: Configuration,
      dataTemplate: Configuration,
      dataTemplateCompile: Configuration) {

    val projectSourceSets = project.getProperties.get("sourceSets").asInstanceOf[SourceSetContainer]

    val dataSchemaDir = project.file(getDataSchemaPath(project, sourceSet))

    val generatedDataTemplateDir = project.file(getGeneratedDirPath(
        project, sourceSet, "DataTemplate") + File.separatorChar + "scala")

    val generateDataTemplateTask = sourceSet.getTaskName("generate", "dataTemplate")

    val targetSourceSetName = getGeneratedSourceSetName(sourceSet, "DataTemplate")

    val dataTemplateJarName = sourceSet.getName + "DataTemplateJar"
  }

  /**
   * Adds Scala data binding generation to the project.
   */
  def apply(project: Project): Unit = {
    val configurations = project.getConfigurations

    // Declare the required Gradle "configurations".
    val dataModel = configurations.create("dataModel")
    val testDataModel = configurations.create("testDataModel")
      .setExtendsFrom(Seq(dataModel).asJava)

    val dataTemplateCompile = configurations.create("dataTemplateCompile")
      .setVisible(false)

    val testDataTemplateCompile = configurations.create("testDataTemplateCompile")
      .setVisible(false)

    val dataTemplate = configurations.create("dataTemplate")
      .setExtendsFrom(Seq(dataTemplateCompile, dataModel).asJava)

    val testDataTemplate = configurations.create("testDataTemplate")
      .setExtendsFrom(Seq(dataTemplate, testDataModel).asJava)

    val compile = project.getConfigurations.findByName("compile")
    val testCompile = project.getConfigurations.findByName("testCompile")


    // Apply plugin settings to all source sets in the project.
    val sourceSets = project.getProperties.get("sourceSets").asInstanceOf[SourceSetContainer]
    Seq(sourceSets.asScala.toSeq: _*).foreach { sourceSet =>
      val courierConfigurations = if (isTestSourceSet(sourceSet)) {
        SourceSetConfig(
          project, sourceSet, testCompile, testDataModel, testDataTemplate, testDataTemplateCompile)
      } else {
        SourceSetConfig(
          project, sourceSet, compile, dataModel, dataTemplate, dataTemplateCompile)
      }
      configureGeneration(courierConfigurations)
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
      config.generateDataTemplateTask).asInstanceOf[GeneratorTask]
    generatorTask.inputDir = config.dataSchemaDir
    generatorTask.destinationDir = config.generatedDataTemplateDir
    generatorTask.resolverPath = config.dataModel
    generatorTask.onlyIf(new Spec[Task] {
      override def isSatisfiedBy(t: Task): Boolean = {
        config.dataSchemaDir.exists()
      }
    })

  // TODO(jbetz): support source jars
  // TODO(jbetz): support javadoc jars

  // Create new source set for generated code.
  val targetSourceSet = config.projectSourceSets.create(config.targetSourceSetName)
  val scala = targetSourceSet.getAllSource.srcDir("scala")
     scala.srcDir(config.generatedDataTemplateDir)

  // Add compile classpath to generated code source set.
  targetSourceSet.setCompileClasspath(config.dataModel.plus(config.dataTemplateCompile))

  // Intellij plugin.
  addGeneratedDir(
    config.project, targetSourceSet, Seq(config.dataModel, config.dataTemplateCompile).asJava)

    // Make sure that code is generated before compiling it.
  val compileGeneratedClassesTask =
    config.project.getTasks.findByName(targetSourceSet.getCompileTaskName("scala"))
  compileGeneratedClassesTask.dependsOn(generatorTask)

    // Create data template jar file.
    val dataTemplateJarTask = config.project.task(
      Map("type" -> classOf[Jar]).asJava, config.dataTemplateJarName)
        .dependsOn(compileGeneratedClassesTask).asInstanceOf[Jar]
    dataTemplateJarTask
      .from(config.dataSchemaDir).eachFile(new Action[FileCopyDetails] {
        def execute(t: FileCopyDetails): Unit = {
          t.setPath(s"pegasus${File.separatorChar}${t.getPath}")
        }
      })
    dataTemplateJarTask.from(targetSourceSet.getOutput)
    dataTemplateJarTask.setAppendix(getAppendix(config.sourceSet, "data-template"))
    dataTemplateJarTask.setDescription("Generate a data template jar")

    // Add the data model and date template jars to the list of project artifacts.
    val artifacts = config.project.getArtifacts
    artifacts.add(config.dataTemplate.getName, dataTemplateJarTask)

    // Include additional dependencies into the appropriate configuration used to compile the
    // input source set must include the generated data template classes and their dependencies
    // the configuration.
    config.compile
        .extendsFrom(config.dataModel)
        .extendsFrom(config.dataTemplateCompile)

    config.project.getDependencies.add(
      config.compile.getName, config.project.files(dataTemplateJarTask.getArchivePath))

    // Add dependency for jar task, which transitively depends on other tasks.
    config.project.getTasks.findByName(
      config.sourceSet.getCompileJavaTaskName).dependsOn(dataTemplateJarTask)
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
    if (project.getProperties.containsKey("ideaModule")) {
      val ideaModule =
        project.getProperties.get("ideaModule").asInstanceOf[GenerateIdeaModule].getModule
      if (isTestSourceSet(sourceSet)) {
        val sourceDirs = ideaModule.getTestSourceDirs
        sourceDirs.addAll(sourceSet.getJava.getSrcDirs)
        ideaModule.setTestSourceDirs(sourceDirs)
      }
      else {
        val sourceDirs = ideaModule.getSourceDirs
        sourceDirs.addAll(sourceSet.getJava.getSrcDirs)
        ideaModule.setSourceDirs(sourceDirs)
      }

      val generatedDirs = ideaModule.getGeneratedSourceDirs
      val files = configurations.asScala.flatMap(_.getFiles.asScala)
      generatedDirs.addAll(files.asJavaCollection)
      ideaModule.setGeneratedSourceDirs(generatedDirs)

      /*val compilePlus = ideaModule.getScopes.get("COMPILE").get("plus")
      compilePlus.addAll(configurations)
      ideaModule.getScopes.get("COMPILE").put("plus", compilePlus)*/
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
