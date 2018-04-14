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

package org.coursera.courier.sbt

import java.io.File.pathSeparator

import org.apache.commons.lang3.exception.ExceptionUtils
import org.coursera.courier.ScalaGenerator
import org.coursera.courier.api.DefaultGeneratorRunner
import org.coursera.courier.api.GeneratorRunnerOptions
import org.coursera.courier.api.ParserForFileFormat
import org.coursera.courier.api.PegasusCodeGenerator
import org.coursera.courier.grammar.CourierSchemaParserFactory
import sbt.Keys._
import sbt._
import xsbti.Severity

import scala.collection.JavaConverters._

/**
 * Based on the SBT Plugin from Sleipnir by Dmitriy Yefremov, which is in turn based on
 * the rest.li-sbt-plugin project.
 *
 * @author Dmitriy Yefremov
 * @author Dean Thompson
 * @author Jim Brikman
 * @author Joe Betz
 */
object CourierPlugin extends Plugin {

  /**
   * Courier's version number.
   *
   * Code generation is always re-run if this version differs from it's value from the
   * previous run (even if there are no changes to .courier files).
   *
   * If this version is unchanged, the code generation is only run if changes to the .courier
   * files are detected.
   *
   * Note that changes to the courier generator must still follow semantic versioning-- any
   * backward incompatible changes should be reflected in the major version.
   */
  // SBT correctly sets the implementation version using the value from version.sbt
  val VERSION = getClass.getPackage.getImplementationVersion

  val courierGenerator = taskKey[Seq[File]]("Generates Scala bindings for .pdsc and .courier files")

  val courierGeneratorClass = settingKey[Class[_ <: PegasusCodeGenerator]](
    "PegasusCodeGenerator implementation class run by this generator.")

  val courierSourceDirectory = settingKey[File](
    "Directory with .pdsc and .courier files used to generateRecord Scala bindings")

  val courierDestinationDirectory = settingKey[File]("Directory with the generated bindings")

  val courierPrefix = settingKey[Option[String]](
    "Namespace prefix used for generated Scala classes")

  val courierCacheSources = taskKey[File]("Caches .pdsc and .courier sources")

  val courierVersionFile = taskKey[File](
    "Version file for cache busting 'courierCacheSources' when the Courier generator version is " +
    "bumped")

  val packageDataModel = taskKey[File]("Produces a data model jar containing only pdsc files")

  val generateTyperefs = settingKey[Boolean](
    "If set to true, generateRecord 'TyperefInfo' classes for all typerefs.")

  /**
   * Settings that need to be added to the project to enable generation of Scala bindings for PDSC
   * files located in the project.
   */
  val courierSettings: Seq[Def.Setting[_]] =
    // TODO(jbetz): The below addition of pegasusArtifacts results in SBT projects that
    // fail to build correctly. This needs to be fixed and tested carefully before re-enabling.
    courierSettings(Compile) ++ courierSettings(Test)/* ++ pegasusArtifacts(Compile)*/ ++ Seq(

    courierPrefix := Some("scala"),

    libraryDependencies += {
      val version = CourierPlugin.getClass.getPackage.getImplementationVersion
      "org.coursera.courier" % s"courier-generator_${scalaBinaryVersion.value}" % version
    })

  private def courierSettings(conf: Configuration): Seq[Def.Setting[_]] = Seq(

    courierGeneratorClass in conf := classOf[ScalaGenerator],

    courierSourceDirectory in conf := (sourceDirectory in conf).value / "pegasus",

    courierDestinationDirectory in conf := (sourceManaged in conf).value / "courier",

    courierVersionFile in conf :=
      (streams in conf).value.cacheDirectory / "VERSION",

    courierCacheSources in conf := (streams in conf).value.cacheDirectory / "pdsc.sources",

    watchSources in conf := ((courierSourceDirectory in conf).value ** sourceFileFilter).get,

    courierGenerator in conf := {
      // Allow use of slf4j logging inside the generator code that we call into later
      //StaticLoggerBinder.sbtLogger = streams.value.log

      val s = (streams in conf).value
      val log = s.log
      val src = (courierSourceDirectory in conf).value
      val dst = (courierDestinationDirectory in conf).value
      val namespacePrefix = courierPrefix.value
      val genTyperefs = (generateTyperefs in conf).value
      val generatorClass = (courierGeneratorClass in conf).value

      // adds in .pdscs from projects that this project .dependsOn
      val resolverPathFiles = Seq(src.getAbsolutePath) ++
        (managedClasspath in conf).value.map(_.data.getAbsolutePath) ++
        (internalDependencyClasspath in conf).value.map(_.data.getAbsolutePath)
      val resolverPath = resolverPathFiles.mkString(pathSeparator)

      val versionFile = (courierVersionFile in conf).value
      val cacheFileSources = (courierCacheSources in conf).value
      val sourceFiles = (src ** sourceFileFilter).get
      val previousScalaFiles = (dst ** "*.scala").get

      if (!versionFile.exists() || IO.read(versionFile).trim != VERSION) {
        log.info(s"Courier: Generator version update detected: $VERSION.")
        IO.write(versionFile, s"$VERSION\n")
      }

      val (anyFilesChanged, cacheSourceFiles) = {
        prepareCacheUpdate(cacheFileSources, sourceFiles :+ versionFile, s)
      }

      log.debug("Courier: Detected changed files: " + anyFilesChanged)
      val results = if (anyFilesChanged) {
        log.info(
          s"Courier: Generating Scala bindings for .pdsc and .courier files for '${conf.name}' " +
          s"configuration.")
        log.debug("Courier: Resolver path: " + resolverPath)
        log.debug("Courier: Source path: " + src)
        log.debug("Courier: Destination path: " + dst)
        try {
          val generator = generatorClass.newInstance()
          val result = new DefaultGeneratorRunner().run(
            generator,
            new GeneratorRunnerOptions(
              dst.absolutePath,
              sourceFiles.map(_.absolutePath).toArray,
              resolverPath)
                .setDefaultPackage(namespacePrefix.getOrElse(""))
                .setGenerateTyperefs(genTyperefs)
              .addParserForFileFormat(
                new ParserForFileFormat("courier", new CourierSchemaParserFactory())))

          // NOTE: Deleting stale files does not work properly with courier activated on two
          // different projects.
          //val staleFiles = previousScalaFiles.sorted.diff(generatedFiles.sorted)
          //log.info("Not deleting stale files " + staleFiles.mkString(", "))
          //IO.delete(staleFiles)
          cacheSourceFiles()
          result.getModifiedFiles.asScala.toSeq

        } catch {
          case e: java.io.IOException => {
            e.getMessage match {
              case JsonParseExceptionRegExp(source, line, column) =>
                log.error(e.getMessage)
                val message =
                  s"Json parse error in $source: line: ${line.toInt}, column:  ${column.toInt}"
                throw new CourierCompilationException(
                  Some(file(source)),
                  message,
                  Option(line.toInt),
                  Option(column.toInt),
                  Severity.Error)
              case SchemaParseExceptionRegExp(source, line, column) =>
                log.error(e.getMessage)
                throw new CourierCompilationException(
                  Some(file(source)),
                  e.getMessage, // include all parse error results in the message
                  Option(line.toInt),
                  Option(column.toInt),
                  Severity.Error)
              case _ =>
                throw new MessageOnlyException(
                  "Courier generator error, cause: " + ExceptionUtils.getStackTrace(e))
            }
          }
          case e: Throwable => {
            throw e
          }
        }
      } else {
        previousScalaFiles
      }
      results
    },

    sourceGenerators in conf += (courierGenerator in conf).taskValue,

    unmanagedSourceDirectories in conf += (courierSourceDirectory in conf).value,

    managedSourceDirectories in conf += (courierDestinationDirectory in conf).value,

    cleanFiles in conf += (courierDestinationDirectory in conf).value,

    generateTyperefs in conf := false)

  private[this] val sourceFileFilter: FileFilter = ("*.pdsc" || "*.courier")

  private[this] val JsonParseExceptionRegExp =
    """(?s).*\[Source: (.*?); line: (\d*), column: (\d*)\].*?""".r

  private[this] val SchemaParseExceptionRegExp =
    """(?s)\s*(.*?),(\d*),(\d*):.*?""".r

  /**
   * Returns an indication of whether `sourceFiles` and their modify dates differ from what is
   * recorded in `cacheFile`, plus a function that can be called to write `sourceFiles` and their
   * modify dates to `cacheFile`.
   */
  private[this] def prepareCacheUpdate(cacheFile: File, sourceFiles: Seq[File],
                         streams: std.TaskStreams[_]): (Boolean, () => Unit) = {
    val fileToModifiedMap = sourceFiles.map(f => f -> FileInfo.lastModified(f)).toMap

    val (_, previousFileToModifiedMap) = Sync.readInfo(cacheFile)(FileInfo.lastModified.format)
    //we only care about the source files here
    val relation = Seq.fill(sourceFiles.size)(file(".")).zip(sourceFiles)

    streams.log.debug(
      s"${fileToModifiedMap.size} <- current VS previous -> ${previousFileToModifiedMap.size}")
    val anyFilesChanged = !cacheFile.exists || (previousFileToModifiedMap != fileToModifiedMap)
    def updateCache() {
      Sync.writeInfo(cacheFile, Relation.empty[File, File] ++ relation.toMap,
        sourceFiles.map(f => f -> FileInfo.lastModified(f)).toMap)(FileInfo.lastModified.format)
    }
    (anyFilesChanged, updateCache)
  }

  /**
   * Generates settings that place the artifact generated by `packagingTaskKey` in the specified
   * `ivyConfig`, while also suffixing the artifact name with "-" and the `ivyConfig`.
   */
  private[this] def restliArtifactSettings(
      packagingTaskKey : TaskKey[File])(ivyConfig : String): Seq[Def.Setting[_]] = {
    val config = Configurations.config(ivyConfig)

    Seq(
      (artifact in packagingTaskKey) <<= (artifact in packagingTaskKey) { artifact =>
        artifact.copy(name = artifact.name + "-" + ivyConfig,
          configurations = artifact.configurations ++ Seq(config))
      },
      ivyConfigurations += config
    )
  }

  /**
   * Finds descendants of `dir` matching `globExpr` and maps them to paths relative to `dir`.
   */
  private[this] def pegasusMappings(dir : File, fileFilter : FileFilter): Seq[(File, String)] = {
    Seq(dir).flatMap(d => Path.allSubpaths(d).filter{ case (f, id) => fileFilter.accept(f) } )
  }

  // Returns settings that can be applied to a project to cause it to package the Pegasus artifacts.
  private[this] def pegasusArtifacts(conf: Configuration): Seq[Def.Setting[_]] = {
    def packageDataModelMappings: Def.Initialize[Task[Seq[(File, String)]]] =
      (courierSourceDirectory in conf).map { (dir) =>
        pegasusMappings(dir, sourceFileFilter)
      }

    // The resulting settings create the two packaging tasks, put their artifacts in specific
    // Ivy configs, and add their artifacts to the project.

    val defaultConfig = config("default").extend(Runtime).describedAs(
      "Configuration for default artifacts.")

    val dataTemplateConfig = new Configuration(
      name = "courier",
      description = "Courier generated data templates.",
      isPublic = true,
      extendsConfigs = List(Compile),
      transitive = true)

    Defaults.packageTaskSettings(packageDataModel, packageDataModelMappings) ++
      restliArtifactSettings(packageDataModel)("pegasus") ++
      Seq(
        packagedArtifacts <++= Classpaths.packaged(Seq(packageDataModel)),
        artifacts <++= Classpaths.artifactDefs(Seq(packageDataModel)),
        ivyConfigurations ++= List(dataTemplateConfig, defaultConfig),
        artifact in (Compile, packageBin) ~= { (art: Artifact) =>
          art.copy(configurations = art.configurations ++ List(dataTemplateConfig))
        }
      )
  }

}
