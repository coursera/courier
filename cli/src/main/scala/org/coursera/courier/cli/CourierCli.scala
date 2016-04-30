package org.coursera.courier.cli

import org.coursera.courier.generator.ScalaDataTemplateGenerator
import org.coursera.courier.{AndroidGenerator, JavaGenerator, ScalaGenerator, SwiftGenerator, TypeScriptLiteGenerator}

object CourierCli extends App {
  val maybeSubcommandResult = for {
    firstArg <- args.headOption
    subcommand <- Subcommand.allByName.get(firstArg)
  } yield {
    subcommand.main(args.tail)
  }

  if (maybeSubcommandResult.isEmpty) {
    val commandList = Subcommand.allByName.keys.toSeq.sorted.mkString("\n\t")
    println(s"Please provide a sub-command as the first argument. Valid choices:\n\t$commandList")
  }
}

case class Subcommand(name: String, main: (Array[String]) => Unit)
object Subcommand {
  val all = Vector(
    Subcommand("swift", SwiftGenerator.main),
    Subcommand("ts", TypeScriptLiteGenerator.main),
    Subcommand("android", AndroidGenerator.main),
    Subcommand("java", JavaGenerator.main),
    Subcommand("scala", ScalaDataTemplateGenerator.main)
  )
  val allByName = all.map(cmd => (cmd.name, cmd)).toMap
}
