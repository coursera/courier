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

import xsbti.{Severity, Problem, Position, Maybe}
import sbt._

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
    severity: Severity) extends xsbti.CompileFailed {

  def arguments(): Array[String] = Array()
  def problems(): Array[Problem] = Array(
    new RestliCompilationProblem(source, message, atLine, column, severity))
  def line = atLine.map(_.asInstanceOf[java.lang.Integer]).orNull
  def position = column.map(_.asInstanceOf[java.lang.Integer]).orNull
  def sourceName = source.map(_.getAbsolutePath).orNull
}

class RestliCompilationProblem(
    source: Option[File],
    msg: String,
    atLine: Option[Int],
    column: Option[Int],
    svrty: Severity) extends Problem {
  def category(): String = "Rest.li"
  def severity(): Severity = svrty
  def message(): String = msg
  def position(): Position = new RestliCompilationErrorPosition(source, atLine, column)
}

class RestliCompilationErrorPosition(
    source: Option[File],
    atLine: Option[Int],
    column: Option[Int]) extends Position {

  def toMaybe[T](option: Option[T]) = option.map {
    optionValue => new Just(optionValue)
  }.getOrElse(new Nothing[T])

  def line(): Maybe[Integer] = toMaybe(atLine.map(_.asInstanceOf[java.lang.Integer]))
  def lineContent(): String = ""
  def offset(): Maybe[Integer] = toMaybe(column.map(_.asInstanceOf[java.lang.Integer]))
  def pointer(): Maybe[Integer] = new Nothing[Integer]
  def pointerSpace(): Maybe[String] = new Nothing[String]
  def sourcePath(): Maybe[String] = toMaybe(source.map(_.getAbsolutePath))
  def sourceFile(): Maybe[File] = toMaybe(source)
}

class Just[T](value: T) extends Maybe[T] {
  def isDefined: Boolean = true
  def get(): T = value
}

class Nothing[T] extends Maybe[T] {
  def isDefined: Boolean = false
  def get(): T = throw new IllegalStateException("Cannot call get() on a Maybe that is Nothing.")
}
