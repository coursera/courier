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
import com.linkedin.data.schema.*;
import com.linkedin.data.schema.resolver.DefaultDataSchemaResolver;
import com.linkedin.data.schema.resolver.FileDataSchemaLocation;
import com.linkedin.data.schema.resolver.FileDataSchemaResolver;
import com.linkedin.data.template.DataTemplateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class EnumTest {
  @Test
  public void enumSymbolDocs() throws IOException {
    EnumDataSchema schema = (EnumDataSchema) TestSchema.loadCourier("org.coursera.enums.Fruits").schema;
    Assert.assertEquals(schema.getSymbolDocs().get("APPLE"), "An Apple.");
  }

  @Test
  public void roundTrip() throws IOException {
    EnumDataSchema schema = (EnumDataSchema) TestSchema.loadCourier("org.coursera.enums.Fruits").schema;
    String serialized = schema.toString();
    EnumDataSchema parsed = (EnumDataSchema) DataTemplateUtil.parseSchema(serialized);
    Assert.assertEquals(schema, parsed);
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
        new File(courierPath + "/" + schemaName.replace('.', '/') + ".courier"));
      Assert.assertTrue(why.toString(), schema != null);
      return new TestSchema(schema, courierFileResolver, location);
    }
  }
}
