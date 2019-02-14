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

import xsbti.{Severity, Problem, Position}
import java.util.Optional
import sbt._

import scala.language.implicitConversions

object CourierExceptionCompat {

  /**
   * Based on RestliCompilationException from the rest.li-sbt-plugin.
   *
   * In order to produce exceptions that can be pretty printed by play (or any framework built on
   * SBT).  We must produce a xsbti.CompilationFailed exception.   Because sbt only provides an
   * interface, we must implement all intefaces from xsbti that we require.
   */
  class CourierCompilationException(
      source: Option[File],
      message: String,
      atLine: Option[Int],
      column: Option[Int],
      severity: Severity)
      extends xsbti.CompileFailed
      with FeedbackProvidedException {

    def arguments(): Array[String] = Array()
    def problems(): Array[Problem] =
      Array(new CourierCompilationProblem(source, message, atLine, column, severity))
    def line = atLine.map(_.asInstanceOf[java.lang.Integer]).orNull
    def position = column.map(_.asInstanceOf[java.lang.Integer]).orNull
    def sourceName = source.map(_.getAbsolutePath).orNull
  }

  class CourierCompilationProblem(
      source: Option[File],
      msg: String,
      atLine: Option[Int],
      column: Option[Int],
      svrty: Severity)
      extends Problem {
    def category(): String = "Courier"
    def severity(): Severity = svrty
    def message(): String = msg
    def position(): Position = new CourierCompilationErrorPosition(source, atLine, column)
  }

  class CourierCompilationErrorPosition(
      source: Option[File],
      atLine: Option[Int],
      column: Option[Int])
      extends Position {

    def toMaybe[T](option: Option[T]) =
      option.map { optionValue =>
        optionValue
      }.asJava

    def line(): Optional[Integer] = toMaybe(atLine.map(_.asInstanceOf[java.lang.Integer]))
    def lineContent(): String = ""
    def offset(): Optional[Integer] = toMaybe(column.map(_.asInstanceOf[java.lang.Integer]))
    def pointer(): Optional[Integer] = Optional.empty[java.lang.Integer]
    def pointerSpace(): Optional[String] = Optional.empty[String]
    def sourcePath(): Optional[String] = toMaybe(source.map(_.getAbsolutePath))
    def sourceFile(): Optional[File] = toMaybe(source)
  }
}
