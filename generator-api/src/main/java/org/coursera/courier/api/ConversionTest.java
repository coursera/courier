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

import com.linkedin.data.DataMap;
import com.linkedin.data.codec.JacksonDataCodec;
import com.linkedin.data.codec.PrettyPrinterJacksonDataCodec;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.DataSchemaLocation;
import com.linkedin.data.schema.DataSchemaResolver;
import com.linkedin.data.schema.JsonBuilder;
import com.linkedin.data.schema.Name;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.SchemaParserFactory;
import com.linkedin.data.schema.SchemaToJsonEncoder;
import com.linkedin.data.schema.StringDataSchemaLocation;
import com.linkedin.data.schema.resolver.DefaultDataSchemaResolver;
import com.linkedin.data.schema.resolver.FileDataSchemaLocation;
import com.linkedin.data.schema.resolver.FileDataSchemaResolver;
import com.linkedin.data.template.DataTemplateUtil;
import org.apache.commons.io.FileUtils;
import org.coursera.courier.grammar.CourierSchemaParserFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class ConversionTest {
  public static void main(String[] args) throws IOException {
    String path = TestSchema.pegasusPath.getAbsolutePath();
    Iterator files = FileUtils.iterateFiles(TestSchema.pegasusPath, new String[]{"pdsc"}, true);
    while (files.hasNext()) {
      File file = (File)files.next();
      String relativePath = file.getAbsolutePath().substring(path.length());
      int idx = relativePath.lastIndexOf(".");
      String schemaName = relativePath.substring(1, idx).replace(File.separator, ".");
      System.err.println("schema:" + schemaName);
      NamedDataSchema pegasus = TestSchema.loadPegasus(schemaName).schema;
      NamedDataSchema courier = TestSchema.loadCourier(schemaName).schema;
      assertSame(pegasus, courier);
    }
  }

  private static JacksonDataCodec dataCodec = new JacksonDataCodec();
  private static PrettyPrinterJacksonDataCodec prettyCodec = new PrettyPrinterJacksonDataCodec();
  private static DataMap readJsonToMap(String string) throws IOException {
    return dataCodec.stringToMap(string);
  }

  private static void assertSame(DataSchema lhs, DataSchema rhs) throws IOException {
    //System.err.println("comparing " + lhs + " with " + rhs);
    DataMap lhsData = readJsonToMap(toJson(lhs));
    DataMap rhsData = readJsonToMap(toJson(rhs));
    if (!Objects.equals(lhsData, rhsData)) {
      System.err.println(prettyCodec.mapToString(lhsData) + "\n != \n" + prettyCodec.mapToString(rhsData));
    }
  }

  private static String toJson(DataSchema schema) {
    return SchemaToJsonEncoder.schemaToJson(schema, JsonBuilder.Pretty.INDENTED);
  }

  private static class TestSchema {
    private final NamedDataSchema schema;
    private final DataSchemaResolver resolver;
    private final DataSchemaLocation location;

    public TestSchema(NamedDataSchema schema, DataSchemaResolver resolver, DataSchemaLocation location) {
      this.schema = schema;
      this.resolver = resolver;
      this.location = location;
    }

    public static File pegasusPath = new File(System.getProperty("project.dir") + "/src/main/pegasus");
    private static FileDataSchemaResolver pegasusFileResolver = new FileDataSchemaResolver(
      SchemaParserFactory.instance(), pegasusPath.getAbsolutePath());

    public static TestSchema loadPegasus(String schemaName) {
      StringBuilder why = new StringBuilder();
      NamedDataSchema schema = pegasusFileResolver.findDataSchema(schemaName, why);
      FileDataSchemaLocation location = new FileDataSchemaLocation(
        new File(pegasusPath + "/" + schemaName.replace('.', '/') + ".pdsc"));
      return new TestSchema(schema, pegasusFileResolver, location);
    }

    private static File courierPath = new File(System.getProperty("project.dir") + "/src/main/courier");
    private static FileDataSchemaResolver courierFileResolver =
      new FileDataSchemaResolver(
        new CourierSchemaParserFactory(),
        courierPath.getAbsolutePath());
    static {
      courierFileResolver.setExtension(".courier");
    }

    public static TestSchema loadCourier(String schemaName) {
      StringBuilder why = new StringBuilder();
      NamedDataSchema schema = courierFileResolver.findDataSchema(schemaName, why);
      FileDataSchemaLocation location = new FileDataSchemaLocation(
        new File(pegasusPath + "/" + schemaName.replace('.', '/') + ".courier"));
      return new TestSchema(schema, courierFileResolver, location);
    }

    public static TestSchema fromJson(String schemaJson) {
      NamedDataSchema schema = (NamedDataSchema)DataTemplateUtil.parseSchema(schemaJson);
      StringDataSchemaLocation location = new StringDataSchemaLocation(schema.getFullName());

      DefaultDataSchemaResolver resolver = new DefaultDataSchemaResolver();
      resolver.bindNameToSchema(new Name(schema.getFullName()), schema, location);

      return new TestSchema(schema, resolver, location);
    }
  }
}
