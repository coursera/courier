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

package org.coursera.courier.api;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;

import java.util.Collection;

/**
 * Data binding code generator for Pegasus schemas.
 */
public interface PegasusCodeGenerator {

  /**
   * Generates code for the given spec.
   *
   * Because ClassTemplateSpec can currently contain nested type declarations that should be
   * generated into top level class files, a single call to generate can produce multiple files.
   */
  public GeneratedCode generate(ClassTemplateSpec spec);

  /**
   * Generate all predefined types.
   *
   * We only generate schemas for pre defined types when re-generating types in courier-runtime.
   */
  public Collection<GeneratedCode> generatePredef();

  /**
   * List of defined schemas. These will be excluded from generation.
   */
  public Collection<DataSchema> definedSchemas();

  /**
   * Used for the "src/main/{language}" paths of build systems.
   */
  public String language();
}
