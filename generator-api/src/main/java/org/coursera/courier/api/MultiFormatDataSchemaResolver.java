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

import com.linkedin.data.schema.DataSchemaLocation;
import com.linkedin.data.schema.DataSchemaResolver;
import com.linkedin.data.schema.Name;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.resolver.FileDataSchemaResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Combines multiple file format specific resolvers (and respective file format specific parsers)
 * into a single resolver.
 *
 * E.g. a resolver for the ".pdsc" file format and the ".courier" file format, each with their
 * own file format specific parsers, can be combined into a single resolver able to look up
 * schemas of either file format.
 */
public class MultiFormatDataSchemaResolver implements DataSchemaResolver {

  private final Map<String, DataSchemaResolver> resolversByExtension;
  private final List<DataSchemaResolver> resolvers;

  public MultiFormatDataSchemaResolver(
      String resolverPath,
      List<ParserForFileFormat> parsersForFormats) {
    resolversByExtension = new HashMap<String, DataSchemaResolver>();
    resolvers = new ArrayList<DataSchemaResolver>();
    for (ParserForFileFormat parserForFormat: parsersForFormats) {
      ResolverOverrideSchemaParserFactory overrideFactory =
          new ResolverOverrideSchemaParserFactory(parserForFormat.parserFactory, this);
      FileDataSchemaResolver resolver = new FileDataSchemaResolver(overrideFactory, resolverPath);
      resolver.setExtension("." + parserForFormat.fileExtension);
      resolversByExtension.put(parserForFormat.fileExtension, resolver);
      resolvers.add(resolver);
    }
  }

  @Override
  public Map<String, NamedDataSchema> bindings() {
    Map<String, NamedDataSchema> results = new HashMap<String, NamedDataSchema>();
    for (DataSchemaResolver resolver: resolvers) {
      results.putAll(resolver.bindings());
    }
    return results;
  }

  @Override
  public Map<String, DataSchemaLocation> nameToDataSchemaLocations() {
    Map<String, DataSchemaLocation> results = new HashMap<String, DataSchemaLocation>();
    for (DataSchemaResolver resolver: resolvers) {
      results.putAll(resolver.nameToDataSchemaLocations());
    }
    return results;
  }

  @Override
  public NamedDataSchema findDataSchema(String name, StringBuilder errorMessageBuilder) {
    for (DataSchemaResolver resolver: resolvers) {
      NamedDataSchema result = resolver.findDataSchema(name, errorMessageBuilder);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  @Override
  public void bindNameToSchema(Name name, NamedDataSchema schema, DataSchemaLocation location) {
    for (DataSchemaResolver resolver: resolvers) {
      resolver.bindNameToSchema(name, schema, location);
    }

  }

  @Override
  public NamedDataSchema existingDataSchema(String name) {
    for (DataSchemaResolver resolver: resolvers) {
      NamedDataSchema result = resolver.existingDataSchema(name);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  @Override
  public boolean locationResolved(DataSchemaLocation location) {
    for (DataSchemaResolver resolver: resolvers) {
      if (resolver.locationResolved(location)) {
        return true;
      }
    }
    return false;
  }
}
