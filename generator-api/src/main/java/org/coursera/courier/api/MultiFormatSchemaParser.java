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

import com.linkedin.data.schema.DataSchemaResolver;
import com.linkedin.pegasus.generator.DataSchemaParser;
import com.linkedin.pegasus.generator.DataSchemaParser.ParseResult;
import com.linkedin.util.FileUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Combines multiple file format specific parsers into a single parser.
 *
 * E.g. All ".pdsc" files are delegated to one parser and all ".courier" files to another parser.
 */
public class MultiFormatSchemaParser {
  private final Map<String, FileFormatDataSchemaParser> parserByFileExtension;
  private final MultiFormatDataSchemaResolver resolver;

  public MultiFormatSchemaParser(
      String resolverPath,
      List<ParserForFileFormat> parsersForFormats) {

    MultiFormatDataSchemaResolver resolver =
      new MultiFormatDataSchemaResolver(resolverPath, parsersForFormats);

    parserByFileExtension = new HashMap<String, FileFormatDataSchemaParser>();
    for (ParserForFileFormat parserForFormat : parsersForFormats) {
      ResolverOverrideSchemaParserFactory overrideFactory =
        new ResolverOverrideSchemaParserFactory(parserForFormat.parserFactory, resolver);
      FileFormatDataSchemaParser fileFormatParser =
        new FileFormatDataSchemaParser(resolver, overrideFactory, parserForFormat.fileExtension);
      parserByFileExtension.put(parserForFormat.fileExtension, fileFormatParser);
    }
    this.resolver = resolver;
  }

  private static class FileExtensionFilter implements FileFilter {
    private final Set<String> extensions;

    public FileExtensionFilter(Set<String> extensions) {
      this.extensions = extensions;
    }

    public boolean accept(File file) {
      return extensions.contains(FilenameUtils.getExtension(file.getName()));
    }
  }

  public DataSchemaResolver getSchemaResolver() {
    return resolver;
  }

  public DataSchemaParser.ParseResult parseSources(String sources[]) throws IOException {
    Set<String> fileExtensions = parserByFileExtension.keySet();
    Map<String, List<String>> byExtension =
      new HashMap<String, List<String>>(fileExtensions.size());
    for (String fileExtension : fileExtensions) {
      byExtension.put(fileExtension, new ArrayList<String>());
    }

    for (String source : sources) {
      final File sourceFile = new File(source);
      if (sourceFile.exists()) {
        if (sourceFile.isDirectory()) {
          final FileExtensionFilter filter = new FileExtensionFilter(fileExtensions);
          final List<File> sourceFilesInDirectory = FileUtil.listFiles(sourceFile, filter);
          for (File f : sourceFilesInDirectory) {
            String ext = FilenameUtils.getExtension(f.getName());
            List<String> filesForExtension = byExtension.get(ext);
            if (filesForExtension != null) {
              filesForExtension.add(f.getAbsolutePath());
            }
          }
        } else {
          String ext = FilenameUtils.getExtension(sourceFile.getName());
          List<String> filesForExtension = byExtension.get(ext);
          if (filesForExtension != null) {
            filesForExtension.add(sourceFile.getAbsolutePath());
          }
        }
      }
    }

    List<ParseResult> results = new ArrayList<ParseResult>();
    for (Map.Entry<String, List<String>> entry : byExtension.entrySet()) {
      String ext = entry.getKey();
      List<String> files = entry.getValue();
      ParseResult parseResult =
        parserByFileExtension.get(ext).parseSources(files.toArray(new String[files.size()]));
      results.add(parseResult);
    }

    return combine(results);
  }

  private static ParseResult combine(Collection<ParseResult> parseResults) {
    FileFormatDataSchemaParser.CourierParseResult combined = new FileFormatDataSchemaParser.CourierParseResult();
    for (ParseResult result : parseResults) {
      combined.getSchemaAndLocations().putAll(result.getSchemaAndLocations());
      combined.getSourceFiles().addAll((result.getSourceFiles()));
      combined.addMessage(result.getMessage().toString());
    }
    return combined;
  }
}
