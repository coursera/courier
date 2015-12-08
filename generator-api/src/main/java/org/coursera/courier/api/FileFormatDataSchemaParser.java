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
import com.linkedin.data.schema.DataSchemaLocation;
import com.linkedin.data.schema.DataSchemaResolver;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.SchemaParser;
import com.linkedin.data.schema.SchemaParserFactory;
import com.linkedin.data.schema.resolver.FileDataSchemaLocation;
import com.linkedin.pegasus.generator.CodeUtil;
import com.linkedin.pegasus.generator.DataSchemaParser.ParseResult;
import com.linkedin.util.FileUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A slight generalization of the com.linkedin.pegasus.generator.DataSchemaParser class from
 * the pegasus codebase.
 *
 * Parses all source files of a particular data schema file type (e.g. ".pdsc").
 *
 * The file format is determined by the fileExtension provided and must be supported by the
 * provided SchemaParserFactory and DataSchemaResolver.
 *
 * @author Joe Betz
 * @author Keren Jin
 */
// TODO(jbetz):
// Replace with https://github.com/coursera/courier/tree/with-restli-upstream-fixes
// once https://github.com/linkedin/rest.li/pull/60 is accepted.
public class FileFormatDataSchemaParser
{
  private final DataSchemaResolver _schemaResolver;
  private final SchemaParserFactory _schemaParserFactory;
  private final String _fileExtension;
  private final FileExtensionFilter _fileExtensionFilter;

  /**
   * Initialize my {@link DataSchemaResolver} with the resolver path.
   */
  public FileFormatDataSchemaParser(DataSchemaResolver schemaResolver, SchemaParserFactory schemaParserFactory, String fileExtension)
  {
    _schemaResolver = schemaResolver;
    _schemaParserFactory = schemaParserFactory;
    _fileExtension = fileExtension;
    _fileExtensionFilter = new FileExtensionFilter(fileExtension);
  }

  public DataSchemaResolver getSchemaResolver()
  {
    return _schemaResolver;
  }

  private static class FileExtensionFilter implements FileFilter {
    private final String fileExtension;

    public FileExtensionFilter(String fileExtension) {
      this.fileExtension = fileExtension;
    }

    public boolean accept(File file) {
      return FilenameUtils.getExtension(file.getName()).equals(fileExtension);
    }
  }

  /**
   * Parses sources that specify paths to schema files and/or fully qualified schema names.
   *
   * @param sources provides the paths to schema files and/or fully qualified schema names.
   *
   * @return {@link ParseResult} for what were read.
   *
   * @throws java.io.IOException if there are problems opening or deleting files.
   */
  public ParseResult parseSources(String sources[])
      throws IOException
  {
    final CourierParseResult result = new CourierParseResult();

    try
    {
      for (String source : sources)
      {
        final File sourceFile = new File(source);
        if (sourceFile.exists())
        {
          if (sourceFile.isDirectory())
          {
            final List<File> sourceFilesInDirectory = FileUtil.listFiles(sourceFile, _fileExtensionFilter);
            for (File f : sourceFilesInDirectory)
            {
              parseFile(f, result);
            }
          }
          else
          {
            parseFile(sourceFile, result);
          }
        }
        else
        {
          final StringBuilder errorMessage = new StringBuilder();
          final DataSchema schema = _schemaResolver.findDataSchema(source, errorMessage);
          if (schema == null)
          {
            result.addMessage("File cannot be opened or schema name cannot be resolved: ").addMessage(source).addMessage("\n");
          }
          if (errorMessage.length() > 0)
          {
            result.addMessage(errorMessage.toString());
          }
        }
      }

      for (Map.Entry<String, DataSchemaLocation> entry : _schemaResolver.nameToDataSchemaLocations().entrySet()) {
        final DataSchema schema = _schemaResolver.bindings().get(entry.getKey());
        result.getSchemaAndLocations().put(schema, entry.getValue());
      }

      if (result.getMessage().length() > 0)
      {
        throw new IOException(result.getMessage());
      }

      appendSourceFilesFromSchemaResolver(result);

      return result;
    }
    catch (RuntimeException e)
    {
      if (result.getMessage().length() > 0)
      {
        e = new RuntimeException("Unexpected " + e.getClass().getSimpleName() + " encountered.\n" +
            "This may be caused by the following parsing or processing errors:\n" +
            result.getMessage(), e);
      }
      throw e;
    }
  }

  /**
   * Parse a source that specifies a file (not a fully qualified schema name).
   *
   * @param schemaSourceFile provides the source file.
   *
   * @param result {@link ParseResult} to update.
   *
   * @throws IOException if there is a file access error.
   */
  private void parseFile(File schemaSourceFile, CourierParseResult result)
      throws IOException
  {
    if (wasResolved(schemaSourceFile))
    {
      return;
    }

    final List<DataSchema> schemas = parseSchema(schemaSourceFile, result);

    for (DataSchema schema : schemas)
    {
      validateSchemaWithFilePath(schemaSourceFile, schema);
      result.getSchemaAndLocations().put(schema, new FileDataSchemaLocation(schemaSourceFile));
      result.getSourceFiles().add(schemaSourceFile);
    }
  }

  /**
   * Checks that the schema name and namespace match the file name and path.  These must match for FileDataSchemaResolver to find a schema pdscs by fully qualified name.
   */
  private void validateSchemaWithFilePath(File schemaSourceFile, DataSchema schema)
  {
    if (schemaSourceFile != null && schemaSourceFile.isFile() && schema instanceof NamedDataSchema)
    {
      final NamedDataSchema namedDataSchema = (NamedDataSchema) schema;
      final String namespace = namedDataSchema.getNamespace();

      if (!FileUtil.removeFileExtension(schemaSourceFile.getName()).equalsIgnoreCase(namedDataSchema.getName()))
      {
        throw new IllegalArgumentException(namedDataSchema.getFullName() + " has name that does not match filename '" +
            schemaSourceFile.getAbsolutePath() + "'");
      }

      final String directory = schemaSourceFile.getParentFile().getAbsolutePath();
      if (!directory.endsWith(namespace.replace('.', File.separatorChar)))
      {
        throw new IllegalArgumentException(namedDataSchema.getFullName() + " has namespace that does not match " +
            "file path '" + schemaSourceFile.getAbsolutePath() + "'");
      }
    }
  }

  /**
   * Whether a source file has already been resolved to data schemas.
   *
   * @param schemaSourceFile provides the source file.
   *
   * @return true if this source file has already been resolved to data schemas.
   */
  private boolean wasResolved(File schemaSourceFile)
  {
    final FileDataSchemaLocation schemaLocation = new FileDataSchemaLocation(schemaSourceFile);
    return _schemaResolver.locationResolved(schemaLocation);
  }

  /**
   * Parse a source file to obtain the data schemas contained within.
   *
   * @param schemaSourceFile provides the source file.
   *
   * @param result {@link ParseResult} to update.
   *
   * @return the data schemas within the source file.
   *
   * @throws IOException if there is a file access error.
   */
  private List<DataSchema> parseSchema(final File schemaSourceFile, CourierParseResult result)
      throws IOException
  {
    SchemaParser parser = _schemaParserFactory.create(_schemaResolver);
    final FileInputStream schemaStream = new SchemaFileInputStream(schemaSourceFile);
    try
    {
      parser.setLocation(new FileDataSchemaLocation(schemaSourceFile));
      parser.parse(schemaStream);
      if (parser.hasError())
      {
        return Collections.emptyList();
      }
      return parser.topLevelDataSchemas();
    }
    finally
    {
      schemaStream.close();
      if (parser.hasError())
      {
        result.addMessage(schemaSourceFile.getPath() + ",");
        result.addMessage(parser.errorMessage());
      }
    }
  }

  /**
   * Append source files that were resolved through {@link DataSchemaResolver} to the provided list.
   *
   * @param result to append the files that were resolved through {@link DataSchemaResolver}.
   */
  private void appendSourceFilesFromSchemaResolver(ParseResult result)
  {
    for (Map.Entry<String, DataSchemaLocation> entry : _schemaResolver.nameToDataSchemaLocations().entrySet())
    {
      final File sourceFile = entry.getValue().getSourceFile();
      if (sourceFile != null)
      {
        result.getSourceFiles().add(sourceFile);
      }
    }
  }

  private static class SchemaFileInputStream extends FileInputStream
  {
    private File _schemaSourceFile;

    private SchemaFileInputStream(File file)
        throws FileNotFoundException
    {
      super(file);
      _schemaSourceFile = file;
    }

    @Override
    public String toString()
    {
      return _schemaSourceFile.toString();
    }
  }

  public static class CourierParseResult extends ParseResult {
    private final StringBuilder messageBuilder;

    public CourierParseResult() {
      try {
        // TODO(jbetz): Fix underlying pegasus code and submit change request
        // back to pegasus project.
        java.lang.reflect.Field topLevelDataSchemasField =
          ParseResult.class.getDeclaredField("_messageBuilder");
        topLevelDataSchemasField.setAccessible(true);
        this.messageBuilder = (StringBuilder) topLevelDataSchemasField.get(this);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    public CourierParseResult addMessage(String message) {
      messageBuilder.append(message);
      return this;
    }
  }

}
