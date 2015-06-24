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

import sbt._
import Keys._
import org.coursera.courier.sbt.CourierPlugin._

object Multiproject extends Build {

  val courierVersion = "0.3.0"

  lazy val example = Project("example", file("example"))
    .dependsOn(schemas)
    .aggregate(schemas)

  lazy val schemas = Project("schemas", file("schemas"))
    .settings(courierSettings: _*)
    .settings(libraryDependencies += "org.coursera.courier" %% "courier-runtime" % courierVersion)

  lazy val root = Project(id = "root", base = file("."))
    .aggregate(example, schemas)
}
