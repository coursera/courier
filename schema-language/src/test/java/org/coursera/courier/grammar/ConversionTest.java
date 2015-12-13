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

package org.coursera.courier.grammar;

import com.linkedin.data.DataMap;
import com.linkedin.data.codec.JacksonDataCodec;
import com.linkedin.data.codec.PrettyPrinterJacksonDataCodec;
import com.linkedin.data.schema.*;
import com.linkedin.data.schema.resolver.DefaultDataSchemaResolver;
import com.linkedin.data.schema.resolver.FileDataSchemaLocation;
import com.linkedin.data.schema.resolver.FileDataSchemaResolver;
import com.linkedin.data.template.DataTemplateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ConversionTest {
  @Test
  public void courierToPdsc() throws IOException {

    String[] schemasNames = new String[] {
      "org.example.Fortune",
      "org.example.FortuneCookie",
      "org.example.MagicEightBall",
      "org.example.MagicEightBallAnswer",
      "org.example.TyperefExample",
      "org.example.common.DateTime",
      "org.example.common.Timestamp"
    };
    for (String schemaName: schemasNames) {
      NamedDataSchema pegasus = TestSchema.loadPegasus(schemaName).schema;
      NamedDataSchema courier = TestSchema.loadCourier(schemaName).schema;
      assertSame(
        pegasus,
        courier);
    }
  }

  private JacksonDataCodec dataCodec = new JacksonDataCodec();
  private PrettyPrinterJacksonDataCodec prettyCodec = new PrettyPrinterJacksonDataCodec();
  private DataMap readJsonToMap(String string) throws IOException {
    return dataCodec.stringToMap(string);
  }

  private void assertSame(DataSchema lhs, DataSchema rhs) throws IOException {
    DataMap lhsData = readJsonToMap(toJson(lhs));
    DataMap rhsData = readJsonToMap(toJson(rhs));
    Assert.assertEquals(
      prettyCodec.mapToString(lhsData) + "\n != \n" + prettyCodec.mapToString(rhsData),
      lhsData, rhsData);
  }

  private String toJson(DataSchema schema) {
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

    private static File pegasusPath =
      new File(System.getProperty("referencesuite.srcdir")  +
      File.separator + "main" + File.separator + "pegasus");

    private static FileDataSchemaResolver pegasusFileResolver = new FileDataSchemaResolver(
      SchemaParserFactory.instance(), pegasusPath.getAbsolutePath());

    public static TestSchema loadPegasus(String schemaName) {
      StringBuilder why = new StringBuilder();
      NamedDataSchema schema = pegasusFileResolver.findDataSchema(schemaName, why);
      FileDataSchemaLocation location = new FileDataSchemaLocation(
        new File(pegasusPath + File.separator + schemaName.replace('.', File.separatorChar) + ".pdsc"));
      Assert.assertTrue(why.toString(), schema != null);
      return new TestSchema(schema, pegasusFileResolver, location);
    }

    private static File courierPath =
      new File(System.getProperty("referencesuite.srcdir") +
      File.separator + "main" + File.separator + "courier");

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
      Assert.assertTrue(why.toString(), schema != null);
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
