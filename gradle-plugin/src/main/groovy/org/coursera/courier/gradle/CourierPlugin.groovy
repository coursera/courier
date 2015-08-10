/*
 * Copyright 2015 Coursera Inc.
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

package org.coursera.courier.gradle

import org.coursera.courier.api.DefaultGeneratorRunner
import org.coursera.courier.api.GeneratorRunnerOptions
import org.coursera.courier.api.PegasusCodeGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.*
import org.gradle.api.tasks.bundling.Jar

/**
 * Provides a gradle plugin for Courier.
 *
 * This is a an port of:
 *
 * https://github.com/linkedin/rest.li/blob/master/gradle-plugins/
 * src/main/groovy/com/linkedin/pegasus/gradle/PegasusPlugin.groovy#L1163
 *
 * This gradle plugin can run any pegasus data generator implementation that extends
 * PegasusCodeGenerator.  By default, the Courier Scala generator will be run.  To use an alternate
 * generator, set the 'courier.codeGenerator' property in your build.gradle. E.g.:
 *
 * courier {
 *   codeGenerator 'org.coursera.courier.AndroidGenerator'
 * }
 *
 * And make sure to include the jar containing the code generator class in the buildscript
 * dependencies classpath, e.g.:
 *
 * buildscript {
 *   dependencies {
 *     classpath "org.coursera.courier:courier-android:0.5.0"
 *   }
 * }
 *
 * To depend on pegsus schemas from another project, do:
 *
 * dependencies {
 *   pegasus project(path: ':models', configuration: 'pegasus')
 * }
 *
 * This will allow .pdsc files in one project to depends on .pdsc files from another project.
 *
 *
 * To depends on just the courier generated bindings from another project, do:
 *
 * dependencies {
 *   compile project(path: ':models', configuration: 'courier')
 * }
 *
 * If you want both the pegasus schemas AND the source from another project, you can simply do:
 *
 * dependencies {
 *   compile project(path: ':models')
 * }
 *
 */
class CourierPlugin implements Plugin<Project> {

  /**
   * Adds Scala data binding generation to the project.
   */
  @Override
  void apply(Project project) {

    project.extensions.create("courier", CourierPluginExtension)

    project.configurations {
      /**
       * Configurations for for pegasus schemas.
       *
       * Used to manage dependencies between .pdsc files from other projects/artifacts.
       */
      pegasus

      testPegasus {
        extendsFrom pegasus
      }

      /**
       * This is the configuration that is intended to be used in build.gradle files to declare
       * dependencies for the compilation phase of the generated code.
       *
       * This is used purely for it's classpath.
       */
      courierCompile {
        visible = false
      }

      /**
       * Configurations for generated data templates (they also contains the .pdsc files).
       *
       * Published data template jars depends on the configurations used to compile the classes
       * in the jar, this includes the data models/templates used by the data template generator
       * and the classes used to compile the generated classes.
       */
      courier {
        extendsFrom courierCompile
        extendsFrom pegasus
      }

      testCourier {
        extendsFrom courier
        extendsFrom testPegasus
      }
    }

    /**
     * Standard compile configurations for this project.
     */
    Configuration compile = project.configurations.getByName("compile")
    Configuration testCompile = project.configurations.getByName("testCompile")

    // Apply plugin settings to all source sets in the project.
    project.sourceSets.all { SourceSet sourceSet ->

      if (sourceSet.name =~ '[Gg]enerated') {
        return
      }
      if (isTestSourceSet(sourceSet)) {
        configureGeneration(new SourceSetConfig(
            project: project,
            sourceSet: sourceSet,
            compile: testCompile,
            pegasus: project.configurations.testPegasus,
            courier: project.configurations.testCourier,
            courierCompile: project.configurations.courierCompile))
      } else {
        configureGeneration(new SourceSetConfig(
            project: project,
            sourceSet: sourceSet,
            compile: compile,
            pegasus: project.configurations.pegasus,
            courier: project.configurations.courier,
            courierCompile: project.configurations.courierCompile))
      }
    }
  }

  // Adapted from
  // https://github.com/linkedin/rest.li/blob/master/gradle-plugins/src/main/groovy/com/
  // linkedin/pegasus/gradle/PegasusPlugin.groovy#L1163
  protected void configureGeneration(SourceSetConfig config) {

    config.dataSchemaDir().mkdirs()

    // Attach the GeneratorTask task to the sourceSet of the project.
    Task generatorTask = config.project.task(config.generateDataBindingTask(), type: GeneratorTask) {
      inputDir = config.dataSchemaDir()
      destinationSourceSetDir = config.generatedDataBindingSourceSetDir()
      resolverPath = config.pegasus
      onlyIf {
        config.dataSchemaDir().exists()
      }
      doFirst {
        deleteGeneratedDir(config.project, config.sourceSet, config.generatedDataBindingSourceSetDir().absolutePath)
      }
    }

    // TODO(jbetz): support source jars
    // TODO(jbetz): support javadoc jars

    // Create new source set for generated code.
    SourceSet targetSourceSet = config.sourceSetContainer().create(config.targetSourceSetName()) {
      // TODO: add source dirs?
      /*java {
        srcDir new File(config.generatedDataBindingSourceSetDir(), "java")
      }*/
      compileClasspath = config.pegasus + config.courierCompile
    }

    // Intellij plugin.
    addGeneratedDir(
        config.project, targetSourceSet, [config.pegasus, config.courierCompile])

    // Make sure that code is generated before compiling it.
    // If using scala, this works because compileScala depends on compileJava. See:
    // https://docs.gradle.org/current/userguide/scala_plugin.html
    final Task compileGeneratedClassesTask = config.project.tasks[targetSourceSet.getCompileTaskName("java")]
    compileGeneratedClassesTask.dependsOn(generatorTask)

    // Create data binding jar file.
    Task dataBindingJarTask = config.project.task(config.dataBindingJarName(), type: Jar, dependsOn: compileGeneratedClassesTask) {
      from(targetSourceSet.output)
      from(config.dataSchemaDir()) {
        // This eachFile applies to all files (both .class and .pdsc)
        eachFile {
          if (it.path.endsWith(".pdsc")) {
            it.path = 'pegasus' + File.separatorChar + it.path.toString()
          }
        }
      }
      appendix = getAppendix(config.sourceSet, 'courier')
      description = 'Generate a data template jar'
    }

    // Create data binding jar file.
    Task schemaJarTask = config.project.task(config.schemaJarName(), type: Jar) {
      from(config.dataSchemaDir()) {
        // This eachFile applies to all files (both .class and .pdsc)
        eachFile {
          if (it.path.endsWith(".pdsc")) {
            it.path = 'pegasus' + File.separatorChar + it.path.toString()
          }
        }
      }
      appendix = getAppendix(config.sourceSet, 'pegasus')
      description = 'Generate a pegasus schema jar'
    }

    // Add the data model and courier date binding jars to the list of project artifacts.
    config.project.artifacts {
      "${config.courier.name}" dataBindingJarTask
      "${config.pegasus.name}" schemaJarTask
    }

    // Include additional dependencies into the appropriate configuration used to compile the
    // input source set must include the generated data binding classes and their dependencies
    // the configuration.
    config.compile
        .extendsFrom(config.pegasus)
        .extendsFrom(config.courierCompile)

    config.project.dependencies.add(config.compile.name, config.project.files(dataBindingJarTask.archivePath))

    // Add dependency for jar task, which transitively depends on other tasks.
    // If using scala, this works because compileScala depends on compileJava. See:
    // https://docs.gradle.org/current/userguide/scala_plugin.html
    config.project.tasks[config.sourceSet.getCompileTaskName("java")].dependsOn(dataBindingJarTask)
  }

  // Return the appendix for generated jar files.
  // The source set name is not included for the main source set.
  private static String getAppendix(SourceSet sourceSet, String suffix)
  {
    return (sourceSet.name.equals('main') ? suffix : "${sourceSet.name}-${suffix}")
  }

  private static String getDataSchemaPath(Project project, SourceSet sourceSet)
  {
    final String override = getOverridePath(project, sourceSet, 'overridePegasusDir')
    if (override == null)
    {
      return "src${File.separatorChar}${sourceSet.name}${File.separatorChar}pegasus"
    }
    else
    {
      return override
    }
  }

  private static String getOverridePath(Project project, SourceSet sourceSet, String overridePropertyName)
  {
    final String sourceSetPropertyName = "${sourceSet.name}.${overridePropertyName}"
    String override = getNonEmptyProperty(project, sourceSetPropertyName)

    if (override == null && sourceSet.name.equals('main'))
    {
      override = getNonEmptyProperty(project, overridePropertyName)
    }

    return override
  }

  /**
   * return the property value if the property exists and is not empty (-Pname=value)
   * return null if property does not exist or the property is empty (-Pname)
   *
   * @param project the project where to look for the property
   * @param propertyName the name of the property
   */
  public static String getNonEmptyProperty(Project project, String propertyName)
  {
    if (!project.hasProperty(propertyName))
    {
      return null
    }

    final String propertyValue = project.property(propertyName).toString()
    if (propertyValue.empty)
    {
      return null
    }

    return propertyValue
  }

  // Compute the directory name that will contain a type generated code of an input source set.
  // e.g. genType may be 'DataTemplate' or 'Rest'
  private static String getGeneratedDirPath(Project project, SourceSet sourceSet, String genType)
  {
    final String override = getOverridePath(project, sourceSet, 'overrideGeneratedDir')
    final String sourceSetName = getGeneratedSourceSetName(sourceSet, genType)
    final String base
    if (override == null)
    {
      base = 'src'
    }
    else
    {
      base = override
    }

    return "${base}${File.separatorChar}${sourceSetName}"
  }

  // Compute the name of the source set that will contain a type of an input generated code.
  // e.g. genType may be 'DataTemplate' or 'Rest'
  private static String getGeneratedSourceSetName(SourceSet sourceSet, String genType)
  {
    return "${sourceSet.name}Generated${genType}"
  }

  private static final String TEST_DIR_REGEX = '^(integ)?[Tt]est'

  private static boolean isTestSourceSet(SourceSet sourceSet)
  {
    return (boolean)(sourceSet.name =~ TEST_DIR_REGEX)
  }

  private static addGeneratedDir(Project project, SourceSet sourceSet, Collection<Configuration> configurations)
  {
    // stupid if block needed because of stupid assignment required to update source dirs
    if (isTestSourceSet(sourceSet))
    {
      Set<File> sourceDirs = project.ideaModule.module.testSourceDirs
      sourceDirs.addAll(sourceSet.java.srcDirs)
      // this is stupid but assignment is required
      project.ideaModule.module.testSourceDirs = sourceDirs
    }
    else
    {
      Set<File> sourceDirs = project.ideaModule.module.sourceDirs
      sourceDirs.addAll(sourceSet.java.srcDirs)
      // this is stupid but assignment is required
      project.ideaModule.module.sourceDirs = sourceDirs
    }
    Collection compilePlus = project.ideaModule.module.scopes.COMPILE.plus
    compilePlus.addAll(configurations)
    project.ideaModule.module.scopes.COMPILE.plus = compilePlus
  }

  private static void deleteGeneratedDir(Project project, SourceSet sourceSet, String dirType)
  {
    final String generatedDirPath = getGeneratedDirPath(project, sourceSet, dirType)
    project.logger.info("Delete generated directory ${generatedDirPath}")
    project.delete(generatedDirPath)
  }

  static class GeneratorTask extends DefaultTask {
    @OutputDirectory File destinationSourceSetDir
    @InputDirectory File inputDir
    @InputFiles FileCollection resolverPath

    @TaskAction
    protected void generate() {
      String pegasusCodeGeneratorClass =
          project.courier.codeGenerator == null ? "org.coursera.courier.ScalaGenerator" : project.courier.codeGenerator
      PegasusCodeGenerator generator = Class.forName(pegasusCodeGeneratorClass).newInstance()
      File destinationDir = new File(destinationSourceSetDir, generator.language())
      destinationDir.delete()
      destinationDir.mkdirs()

      FileTree pdscFiles = project.fileTree(dir: inputDir, includes: ["**${File.separatorChar}*pdsc"])

      final String[] pdscFileArray = pdscFiles.collect { it.absolutePath } as String[]
      final String resolverPathStr = (resolverPath + project.files(inputDir)).asPath
      DefaultGeneratorRunner generatorRunner = new DefaultGeneratorRunner()
      generatorRunner.run(
          generator,
          new GeneratorRunnerOptions(destinationDir.absolutePath, pdscFileArray, resolverPathStr))
    }
  }

  static class SourceSetConfig {
    Project project
    SourceSet sourceSet
    Configuration compile
    Configuration pegasus
    Configuration courier
    Configuration courierCompile

    SourceSetContainer sourceSetContainer() { project.sourceSets }
    File dataSchemaDir() { project.file(getDataSchemaPath(project, sourceSet)) }

    // E.g. /src/mainGeneratedCourier or /src/testGeneratedCourier
    File generatedDataBindingSourceSetDir() {
      project.file(getGeneratedDirPath(
          project, sourceSet, "Courier") + File.separatorChar)
    }

    String generateDataBindingTask() { sourceSet.getTaskName("generate", "courier") }

    String targetSourceSetName() { getGeneratedSourceSetName(sourceSet, "Courier") }

    String dataBindingJarName() { sourceSet.name + "CourierJar" }

    String schemaJarName() { sourceSet.name + "PegasusJar" }
  }

  static class CourierPluginExtension {
    String codeGenerator
  }
}
