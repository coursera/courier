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

import com.linkedin.data.schema.DataSchemaParser;
import com.linkedin.data.schema.DataSchemaParserFactory;
import com.linkedin.data.schema.DataSchemaResolver;
import com.linkedin.data.schema.SchemaParser;
import com.linkedin.data.schema.SchemaParserFactory;
import com.linkedin.data.schema.validation.ValidationOptions;

/**
 * Wraps a SchemaParserFactory overriding the resolver used to always be the one provided to this
 * classes constructor, not the one passed to create.
 */
// TODO(jbetz):
// This class is a short-term workaround. We should make deeper changes to pegasus
// and submit a pull request to the rest.li project.
//
// In AbstractDataSchemaResolver.parse(), a resolver is able to create a parser that, in turn,
// only uses that particular resolver. This is problematic when using MultiFormatDataSchemaResolver
// because it allows the parser for a single file format (e.g. the resolver for ".pdsc" files)
// to only use the resolver for that file format when resolving schemas by name.
// As a result, a schema defined in a ".pdsc" references a schema defined in a ".courier" file
// (or vis-versa) can fail to resolve.
class ResolverOverrideSchemaParserFactory implements DataSchemaParserFactory {

  private final DataSchemaParserFactory underlying;
  private final DataSchemaResolver resolver;

  public ResolverOverrideSchemaParserFactory(
      DataSchemaParserFactory underlying,
      DataSchemaResolver resolver) {
    this.underlying = underlying;
    this.resolver = resolver;
  }

  @Override
  public DataSchemaParser create(DataSchemaResolver resolver) {
    return underlying.create(this.resolver);
  }
}
