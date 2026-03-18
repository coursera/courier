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

import com.linkedin.data.schema.SchemaParser;
import com.linkedin.data.schema.resolver.DefaultDataSchemaResolver;
import com.linkedin.data.schema.validation.ValidationOptions;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link CourierSchemaParserFactory}, which is hand-written and currently has 0%
 * JaCoCo coverage.
 */
public class CourierSchemaParserFactoryTest {

  @Test
  public void defaultConstructor_create_returnsCourierSchemaParser() {
    CourierSchemaParserFactory factory = new CourierSchemaParserFactory();
    SchemaParser parser = factory.create(new DefaultDataSchemaResolver());
    Assert.assertNotNull("create() should return a non-null parser", parser);
    Assert.assertTrue(
        "Expected CourierSchemaParser, got: " + parser.getClass(),
        parser instanceof CourierSchemaParser);
  }

  @Test
  public void validationOptionsConstructor_create_returnsCourierSchemaParser() {
    ValidationOptions options = new ValidationOptions();
    CourierSchemaParserFactory factory = new CourierSchemaParserFactory(options);
    SchemaParser parser = factory.create(new DefaultDataSchemaResolver());
    Assert.assertNotNull("create() should return a non-null parser", parser);
    Assert.assertTrue(
        "Expected CourierSchemaParser, got: " + parser.getClass(),
        parser instanceof CourierSchemaParser);
  }

  @Test
  public void create_withNullResolver_returnsCourierSchemaParser() {
    CourierSchemaParserFactory factory = new CourierSchemaParserFactory();
    // CourierSchemaParser accepts a null resolver (delegates to SchemaParser)
    SchemaParser parser = factory.create(null);
    Assert.assertNotNull(parser);
    Assert.assertTrue(parser instanceof CourierSchemaParser);
  }

  @Test
  public void defaultConstructor_create_canParseSimpleRecord() {
    CourierSchemaParserFactory factory = new CourierSchemaParserFactory();
    CourierSchemaParser parser = (CourierSchemaParser) factory.create(new DefaultDataSchemaResolver());
    // Minimal Courier source schema (not PDSC/JSON — CourierSchemaParser parses .courier format)
    parser.parse("namespace org.test\nrecord TestRecord {\n  field: string\n}");
    Assert.assertFalse(
        "Parser should have no errors for a valid schema, but got: " + parser.errorMessageBuilder(),
        parser.hasError());
  }
}
