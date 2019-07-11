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

package org.coursera.courier;

import com.linkedin.data.DataMap;
import com.linkedin.data.codec.JacksonDataCodec;
import com.linkedin.data.codec.PrettyPrinterJacksonDataCodec;
import com.linkedin.data.schema.*;
import com.linkedin.data.schema.resolver.FileDataSchemaResolver;
import org.coursera.courier.grammar.CourierSchemaParserFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConversionTest {
    @Test
    public void courierToPdsc() throws IOException {

        String[] schemasNames = new String[]{
                "org.example.Apostrophe",
                "org.example.Fortune",
                "org.example.FortuneCookie",
                "org.example.MagicEightBall",
                "org.example.MagicEightBallAnswer",
                "org.example.TyperefExample",
                "org.example.common.DateTime",
                "org.example.common.Timestamp"
        };
        for (String schemaName : schemasNames) {
            DataMap pegasus = PdscSchema.load(schemaName).schemaJson;
            DataMap courier = CourierSchema.loadCourier(schemaName).pdscSchema();
            assertSame(
                    pegasus,
                    courier);
        }
    }

    private static PrettyPrinterJacksonDataCodec prettyCodec = new PrettyPrinterJacksonDataCodec();
    private static JacksonDataCodec dataCodec = new JacksonDataCodec();

    private void assertSame(DataMap lhs, DataMap rhs) throws IOException {
        Assert.assertEquals(
                prettyCodec.mapToString(lhs) + "\n != \n" + prettyCodec.mapToString(rhs),
                lhs, rhs);
    }

    private static class CourierSchema {
        private final NamedDataSchema schema;

        public CourierSchema(NamedDataSchema schema) {
            this.schema = schema;
        }

        public DataMap pdscSchema() throws IOException {
            return dataCodec.stringToMap(SchemaToPdscEncoder.schemaToPdsc(this.schema));
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

        public static CourierSchema loadCourier(String schemaName) {
            StringBuilder why = new StringBuilder();
            NamedDataSchema schema = courierFileResolver.findDataSchema(schemaName, why);
            Assert.assertTrue(why.toString(), schema != null);
            return new CourierSchema(schema);
        }

    }

    private static class PdscSchema {
        private final DataMap schemaJson;

        public PdscSchema(DataMap schemaJson) {
            this.schemaJson = schemaJson;
        }

        private static File pegasusPath =
                new File(System.getProperty("referencesuite.srcdir") +
                        File.separator + "main" + File.separator + "pegasus");

        public static PdscSchema load(String schemaName) throws IOException {
            String relPath = schemaName.replaceAll("\\.", "/") + ".pdsc";
            String jsonString = new String(
                    Files.readAllBytes(Paths.get(pegasusPath.toString(), relPath)));
            return new PdscSchema(dataCodec.stringToMap(jsonString));
        }
    }
}
