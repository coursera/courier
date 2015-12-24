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

package org.coursera.courier;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.DataSchemaConstants;
import com.linkedin.pegasus.generator.JavaCodeGeneratorBase;
import com.linkedin.pegasus.generator.JavaDataTemplateGenerator;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JPackage;
import org.apache.commons.io.output.NullOutputStream;
import org.coursera.courier.api.DefaultGeneratorRunner;
import org.coursera.courier.api.GeneratedCode;
import org.coursera.courier.api.GeneratedCodeTargetFile;
import org.coursera.courier.api.GeneratorRunnerOptions;
import org.coursera.courier.api.PegasusCodeGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Generator for Pegasus style Java bindings.
 */
public class JavaGenerator implements PegasusCodeGenerator {
  public static void main(String[] args) throws Throwable {
    if (args.length != 3) {
      throw new IllegalArgumentException(
        "Usage: " + JavaGenerator.class.getName() +
          " targetPath resolverPath sourcePath1[:sourcePath2]+");
    }
    String targetPath = args[0];
    String resolverPath = args[1];
    String sourcePathString = args[2];
    String[] sourcePaths = sourcePathString.split(File.pathSeparator);

    GeneratorRunnerOptions options =
      new GeneratorRunnerOptions(targetPath, sourcePaths, resolverPath);

    new DefaultGeneratorRunner().run(new JavaGenerator(), options);
  }

  public JavaGenerator() {
  }

  public static class JavaCompilationUnit extends GeneratedCodeTargetFile {
    public JavaCompilationUnit(String name, String namespace) {
      super(name, namespace, "java");
    }
  }

  @Override
  public GeneratedCode generate(ClassTemplateSpec templateSpec) {
    if (predef.contains(templateSpec.getSchema())) {
      return null;
    }

    JavaDataTemplateGenerator.Config config = new JavaDataTemplateGenerator.Config();
    JavaDataTemplateGenerator generator = new JavaDataTemplateGenerator(config);
    JClass result = generator.generate(templateSpec);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String code;
    try {
      generator.getCodeModel().build(
        new CapturingCodeWriter(templateSpec.getNamespace(), templateSpec.getClassName(), out));
      out.flush();
      out.close();
      code = out.toString("UTF-8");
    } catch (IOException e) {
      throw new RuntimeException("Error generating code for " + templateSpec.getFullName(), e);
    }

    if (code.trim().equals("")) {
      throw new RuntimeException("Failed to generate code for " + templateSpec.getFullName());
    }

    return new GeneratedCode(
      new JavaCompilationUnit(
        result.name(), result._package().name()), code);
  }

  @Override
  public Collection<GeneratedCode> generatePredef() {
    return Collections.emptySet();
  }

  private static final Collection<DataSchema> predef = new HashSet<>();
  static {
    predef.add(DataSchemaConstants.INTEGER_DATA_SCHEMA);
    predef.add(DataSchemaConstants.LONG_DATA_SCHEMA);
    predef.add(DataSchemaConstants.FLOAT_DATA_SCHEMA);
    predef.add(DataSchemaConstants.DOUBLE_DATA_SCHEMA);
    predef.add(DataSchemaConstants.BOOLEAN_DATA_SCHEMA);
    predef.add(DataSchemaConstants.STRING_DATA_SCHEMA);
    predef.add(DataSchemaConstants.BYTES_DATA_SCHEMA);
    predef.add(DataSchemaConstants.NULL_DATA_SCHEMA);

    predef.addAll(JavaDataTemplateGenerator.PredefinedJavaClasses.keySet());
  }

  /**
   * A code writer for CodeModel that captures the generated code for a single
   * class as a string.
   *
   * CodeModel may generate multiple Java classes in a single run.  This writer
   * captures the generated code for only the desired class and ignores the rest.
   */
  private static class CapturingCodeWriter extends CodeWriter {
    private final PrintStream out;
    private final String namespace;
    private final String className;

    // Get access to the escaper that Pegasus uses for Java.
    private static final class Escaper extends JavaCodeGeneratorBase {
      public Escaper() {
        super("");
      }

      public static String escape(String name) {
        return escapeReserved(name);
      }
    }

    /**
     * @param namespace provides the namespace of the generated class to capture.
     * @param className provides the class name of the generated class to capture. The class
     *                  name must be unescaped.
     * @param os
     *      This stream will be closed at the end of the code generation.
     */
    public CapturingCodeWriter(String namespace, String className, OutputStream os) {
      this.out = new PrintStream(os);
      this.namespace = namespace;
      this.className = className;
    }

    private static final int SUFFIX_LENGTH = ".java".length();

    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
      boolean namespaceMatches =
        (pkg.isUnnamed() && (namespace == null || namespace.isEmpty())) ||
        pkg.name().equals(namespace);

      String name = fileName.substring(0, fileName.length() - SUFFIX_LENGTH);
      boolean classNameMatches = name.equals(Escaper.escape(className));

      // Ignore all but the class we're intentionally generating code for.
      if (namespaceMatches && classNameMatches) {
        return out;
      } else {
        return new NullOutputStream();
      }
    }

    public void close() throws IOException {
      out.close();
    }
  }

  @Override
  public Collection<DataSchema> definedSchemas() {
    return predef;
  }

  @Override
  public String buildLanguage() {
    return "java";
  }

  @Override
  public String customTypeLanguage() {
    return "java";
  }
}
