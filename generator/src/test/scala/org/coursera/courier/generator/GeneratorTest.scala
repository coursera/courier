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

package org.coursera.courier.generator

import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

import com.linkedin.data.schema.NamedDataSchema
import com.linkedin.data.schema.resolver.DefaultDataSchemaResolver
import com.linkedin.pegasus.generator.TemplateSpecGenerator
import org.coursera.courier.generator.twirl.TwirlDataTemplateGenerator
import com.linkedin.data.schema.Name

trait GeneratorTest {

  def generateTestSchemas(testSchemas: Seq[TestSchema]): Unit = {
    val scalaGenerator = new TwirlDataTemplateGenerator()

    testSchemas.map { testSchema =>
      val specGenerator = new TemplateSpecGenerator(testSchema.resolver)
      val generated = scalaGenerator.generate(specGenerator.generate(testSchema.schema))

      val baseDir = sys.props("project.dir") + "/src/test/scala"
      generated.foreach { generatedCode =>
        val relativePath = new File(
          baseDir,
          generatedCode.compilationUnit.namespace.replace('.', File.separatorChar))
        val filename = generatedCode.compilationUnit.name
        relativePath.mkdirs()
        val file = new File(relativePath, filename + ".scala")
        val output = new PrintStream(new FileOutputStream(file))
        try {
          output.print(generatedCode.code)
        } finally {
          output.close()
        }
      }
    }
  }
}
