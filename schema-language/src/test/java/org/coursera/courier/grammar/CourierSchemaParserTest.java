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

import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.Null;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.FixedDataSchema;
import com.linkedin.data.schema.MapDataSchema;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.schema.UnionDataSchema;
import com.linkedin.data.schema.resolver.DefaultDataSchemaResolver;
import com.linkedin.data.schema.resolver.FileDataSchemaResolver;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Tests for {@link CourierSchemaParser} covering parsing of various schema types,
 * error handling paths, and edge cases.
 */
public class CourierSchemaParserTest {

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  private CourierSchemaParser newParser() {
    return new CourierSchemaParser(new DefaultDataSchemaResolver());
  }

  /** Parse a schema string and return the first top-level schema, asserting no errors. */
  private NamedDataSchema parseOk(String source) {
    CourierSchemaParser parser = newParser();
    parser.parse(source);
    Assert.assertFalse("Unexpected parse errors: " + parser.errorMessage(), parser.hasError());
    List<DataSchema> schemas = parser.topLevelDataSchemas();
    Assert.assertFalse("Expected at least one top-level schema", schemas.isEmpty());
    return (NamedDataSchema) schemas.get(0);
  }

  /** Parse a schema string and assert that errors are present; return the error message. */
  private String parseExpectError(String source) {
    CourierSchemaParser parser = newParser();
    parser.parse(source);
    Assert.assertTrue("Expected parse errors but none occurred", parser.hasError());
    return parser.errorMessage();
  }

  // ---------------------------------------------------------------------------
  // parse(String) — basic record
  // ---------------------------------------------------------------------------

  @Test
  public void parse_simpleRecord_returnsRecordSchema() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record SimpleRec { name: string }");
    Assert.assertTrue(schema instanceof RecordDataSchema);
    Assert.assertEquals("org.test.SimpleRec", schema.getFullName());
  }

  // ---------------------------------------------------------------------------
  // parse(InputStream) — delegates to Reader path
  // ---------------------------------------------------------------------------

  @Test
  public void parse_inputStream_parsesRecord() {
    String source = "namespace org.test\nrecord StreamRec { value: int }";
    byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
    CourierSchemaParser parser = newParser();
    parser.parse(new ByteArrayInputStream(bytes));
    Assert.assertFalse(parser.errorMessage(), parser.hasError());
    Assert.assertEquals(1, parser.topLevelDataSchemas().size());
    Assert.assertEquals("org.test.StreamRec",
        parser.topLevelDataSchemas().get(0).getUnionMemberKey());
  }

  // ---------------------------------------------------------------------------
  // parse(Reader) — delegates to Reader path directly
  // ---------------------------------------------------------------------------

  @Test
  public void parse_reader_parsesRecord() {
    String source = "namespace org.test\nrecord ReaderRec { value: long }";
    CourierSchemaParser parser = newParser();
    parser.parse(new StringReader(source));
    Assert.assertFalse(parser.errorMessage(), parser.hasError());
    Assert.assertEquals(1, parser.topLevelDataSchemas().size());
  }

  // ---------------------------------------------------------------------------
  // Syntax error → errorRecorder path
  // ---------------------------------------------------------------------------

  @Test
  public void parse_syntaxError_recordsError() {
    String errorMsg = parseExpectError("namespace org.test\nrecord {{{{");
    Assert.assertFalse("Error message should be non-empty", errorMsg.isEmpty());
  }

  // ---------------------------------------------------------------------------
  // Fixed schema
  // ---------------------------------------------------------------------------

  @Test
  public void parse_fixedSchema_returnsFixedDataSchema() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\nfixed MyFixed 16");
    Assert.assertTrue(schema instanceof FixedDataSchema);
    Assert.assertEquals("org.test.MyFixed", schema.getFullName());
    Assert.assertEquals(16, ((FixedDataSchema) schema).getSize());
  }

  @Test
  public void parse_fixedSchema_withDoc_setsDoc() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n/** A fixed-length bytes. */\nfixed DocFixed 8");
    Assert.assertTrue(schema instanceof FixedDataSchema);
    Assert.assertNotNull(schema.getDoc());
    Assert.assertFalse(schema.getDoc().isEmpty());
  }

  // ---------------------------------------------------------------------------
  // Enum schema
  // ---------------------------------------------------------------------------

  @Test
  public void parse_enumSchema_returnsEnumDataSchema() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\nenum Color { RED GREEN BLUE }");
    Assert.assertTrue(schema instanceof EnumDataSchema);
    EnumDataSchema enumSchema = (EnumDataSchema) schema;
    Assert.assertTrue(enumSchema.getSymbols().contains("RED"));
    Assert.assertTrue(enumSchema.getSymbols().contains("GREEN"));
    Assert.assertTrue(enumSchema.getSymbols().contains("BLUE"));
  }

  @Test
  public void parse_enumWithSymbolDocs_setsSymbolDocs() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "enum Fruit {\n" +
        "  /** An apple. */\n" +
        "  APPLE\n" +
        "  BANANA\n" +
        "}");
    Assert.assertTrue(schema instanceof EnumDataSchema);
    EnumDataSchema enumSchema = (EnumDataSchema) schema;
    Assert.assertEquals("An apple.", enumSchema.getSymbolDocs().get("APPLE"));
    Assert.assertNull(enumSchema.getSymbolDocs().get("BANANA"));
  }

  @Test
  public void parse_enumWithDeprecatedSymbol_setsDeprecatedSymbols() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "enum Status {\n" +
        "  @deprecated\n" +
        "  OLD_VALUE\n" +
        "  NEW_VALUE\n" +
        "}");
    Assert.assertTrue(schema instanceof EnumDataSchema);
    EnumDataSchema enumSchema = (EnumDataSchema) schema;
    Map<String, Object> props = enumSchema.getProperties();
    Assert.assertTrue("Expected deprecatedSymbols property", props.containsKey("deprecatedSymbols"));
    DataMap deprecated = (DataMap) props.get("deprecatedSymbols");
    Assert.assertTrue(deprecated.containsKey("OLD_VALUE"));
  }

  @Test
  public void parse_enumWithSymbolProperty_setsSymbolProperties() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "enum Colors {\n" +
        "  @color = \"red\"\n" +
        "  RED\n" +
        "  @color = \"blue\"\n" +
        "  BLUE\n" +
        "}");
    Assert.assertTrue(schema instanceof EnumDataSchema);
    EnumDataSchema enumSchema = (EnumDataSchema) schema;
    Map<String, Object> props = enumSchema.getProperties();
    Assert.assertTrue("Expected symbolProperties", props.containsKey("symbolProperties"));
  }

  // ---------------------------------------------------------------------------
  // Typeref schema
  // ---------------------------------------------------------------------------

  @Test
  public void parse_typerefSchema_returnsTyperefDataSchema() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\ntyperef MyString = string");
    Assert.assertTrue(schema instanceof TyperefDataSchema);
    Assert.assertEquals("org.test.MyString", schema.getFullName());
    TyperefDataSchema typeref = (TyperefDataSchema) schema;
    Assert.assertNotNull(typeref.getRef());
  }

  // ---------------------------------------------------------------------------
  // Record with union (inline anonymous union)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_recordWithInlineUnion_parsesUnion() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithUnion { value: union[int, string] }");
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    RecordDataSchema.Field field = record.getField("value");
    Assert.assertNotNull(field);
    Assert.assertTrue(field.getType() instanceof UnionDataSchema);
  }

  // ---------------------------------------------------------------------------
  // Record with array (inline anonymous array)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_recordWithInlineArray_parsesArray() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithArray { items: array[string] }");
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    Assert.assertNotNull(record.getField("items"));
  }

  // ---------------------------------------------------------------------------
  // Map schema — string key (most common)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_recordWithStringKeyMap_parsesMap() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithMap { data: map[string, int] }");
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    RecordDataSchema.Field field = record.getField("data");
    Assert.assertNotNull(field);
    Assert.assertTrue(field.getType() instanceof MapDataSchema);
  }

  // ---------------------------------------------------------------------------
  // Map schema — non-string primitive key (triggers "keys" property)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_recordWithNonStringPrimitiveKeyMap_setsKeysProperty() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithIntKeyMap { data: map[int, string] }");
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    RecordDataSchema.Field field = record.getField("data");
    MapDataSchema mapSchema = (MapDataSchema) field.getType();
    // Non-string key should produce a "keys" property
    Assert.assertNotNull(mapSchema.getProperties().get("keys"));
  }

  // ---------------------------------------------------------------------------
  // Map schema — inline record key (complex type key)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_recordWithInlineRecordKeyMap_setsKeysProperty() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithRecordKeyMap {\n" +
        "  data: map[\n" +
        "    record InlineKey { id: int },\n" +
        "    string\n" +
        "  ]\n" +
        "}");
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    RecordDataSchema.Field field = record.getField("data");
    MapDataSchema mapSchema = (MapDataSchema) field.getType();
    Assert.assertNotNull("Expected 'keys' property for inline-record-key map",
        mapSchema.getProperties().get("keys"));
  }

  // ---------------------------------------------------------------------------
  // Record with includes
  // ---------------------------------------------------------------------------

  @Test
  public void parse_recordWithInclude_includesFields() {
    // Two-pass: first register Base, then parse WithInclude referencing it.
    String baseSource = "namespace org.test\nrecord Base { id: int }";
    CourierSchemaParser parser = newParser();
    parser.parse(baseSource);
    Assert.assertFalse(parser.errorMessage(), parser.hasError());

    // Bind base schema to resolver so the include can resolve it
    DefaultDataSchemaResolver resolver = new DefaultDataSchemaResolver();
    CourierSchemaParser p1 = new CourierSchemaParser(resolver);
    p1.parse(baseSource);
    Assert.assertFalse(p1.errorMessage(), p1.hasError());

    CourierSchemaParser p2 = new CourierSchemaParser(resolver);
    p2.parse("namespace org.test\nrecord Extended { ...Base\n extra: string }");
    Assert.assertFalse(p2.errorMessage(), p2.hasError());

    RecordDataSchema extended =
        (RecordDataSchema) p2.topLevelDataSchemas().get(0);
    // Should have both the included field 'id' and the direct field 'extra'
    Assert.assertNotNull("Expected included field 'id'", extended.getField("id"));
    Assert.assertNotNull("Expected direct field 'extra'", extended.getField("extra"));
  }

  // ---------------------------------------------------------------------------
  // Record with field doc, optional, and property
  // ---------------------------------------------------------------------------

  @Test
  public void parse_fieldWithDoc_setsFieldDoc() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithDocField {\n" +
        "  /** The name. */\n" +
        "  name: string\n" +
        "}");
    RecordDataSchema record = (RecordDataSchema) schema;
    RecordDataSchema.Field field = record.getField("name");
    Assert.assertNotNull(field.getDoc());
    Assert.assertFalse(field.getDoc().isEmpty());
  }

  @Test
  public void parse_optionalField_isOptional() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithOptional { name: string? }");
    RecordDataSchema record = (RecordDataSchema) schema;
    Assert.assertTrue(record.getField("name").getOptional());
  }

  @Test
  public void parse_fieldWithProperty_setsProperty() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithPropField {\n" +
        "  @deprecated\n" +
        "  oldField: string\n" +
        "}");
    RecordDataSchema record = (RecordDataSchema) schema;
    RecordDataSchema.Field field = record.getField("oldField");
    Assert.assertNotNull("Expected deprecated property on field",
        field.getProperties().get("deprecated"));
  }

  // ---------------------------------------------------------------------------
  // Default values — primitives and JSON
  // ---------------------------------------------------------------------------

  @Test
  public void parse_fieldWithIntDefault_setsDefault() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithDefault { count: int = 42 }");
    RecordDataSchema record = (RecordDataSchema) schema;
    Object def = record.getField("count").getDefault();
    Assert.assertEquals(42, def);
  }

  @Test
  public void parse_fieldWithStringDefault_setsDefault() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithStrDefault { msg: string = \"hello\" }");
    RecordDataSchema record = (RecordDataSchema) schema;
    Object def = record.getField("msg").getDefault();
    Assert.assertEquals("hello", def);
  }

  @Test
  public void parse_fieldWithBooleanDefault_setsDefault() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithBoolDefault { flag: boolean = true }");
    RecordDataSchema record = (RecordDataSchema) schema;
    Object def = record.getField("flag").getDefault();
    Assert.assertEquals(Boolean.TRUE, def);
  }

  @Test
  public void parse_propertyWithNullValue_returnsNullInstance() {
    // The parseJsonValue null branch is triggered when a JSON null literal appears
    // as a property value. @propName = null is valid courier syntax.
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "@myProp = null\n" +
        "record WithNullProp { value: int }");
    Assert.assertTrue(schema instanceof RecordDataSchema);
    Map<String, Object> props = schema.getProperties();
    Assert.assertTrue("Expected myProp property", props.containsKey("myProp"));
    Assert.assertEquals(Null.getInstance(), props.get("myProp"));
  }

  @Test
  public void parse_fieldWithArrayDefault_setsDataList() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithArrayDefault { items: array[int] = [1, 2, 3] }");
    RecordDataSchema record = (RecordDataSchema) schema;
    Object def = record.getField("items").getDefault();
    Assert.assertTrue("Expected DataList default", def instanceof DataList);
    DataList list = (DataList) def;
    Assert.assertEquals(3, list.size());
  }

  @Test
  public void parse_fieldWithObjectDefault_setsDataMap() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "record WithMapDefault { data: map[string, int] = { \"a\": 1 } }");
    RecordDataSchema record = (RecordDataSchema) schema;
    Object def = record.getField("data").getDefault();
    Assert.assertTrue("Expected DataMap default", def instanceof DataMap);
    DataMap map = (DataMap) def;
    Assert.assertEquals(1, map.get("a"));
  }

  // ---------------------------------------------------------------------------
  // Schema-level doc and properties
  // ---------------------------------------------------------------------------

  @Test
  public void parse_schemaWithDoc_setsDoc() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "/** A documented record. */\n" +
        "record Documented { value: string }");
    Assert.assertNotNull(schema.getDoc());
    Assert.assertFalse(schema.getDoc().isEmpty());
  }

  @Test
  public void parse_schemaWithProperty_setsProperty() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "@validate.regex.regex = \"[a-z]+\"\n" +
        "record Validated { value: string }");
    Assert.assertFalse(schema.getProperties().isEmpty());
  }

  // ---------------------------------------------------------------------------
  // Namespace-less schema (empty namespace)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_noNamespace_emptyNamespace() {
    NamedDataSchema schema = parseOk("record NoNamespace { x: int }");
    Assert.assertEquals("NoNamespace", schema.getFullName());
  }

  // ---------------------------------------------------------------------------
  // computeFullName — imports take precedence over namespace
  // ---------------------------------------------------------------------------

  @Test
  public void parse_importedTypeName_resolvedViaImport() {
    // Register the imported type first
    DefaultDataSchemaResolver resolver = new DefaultDataSchemaResolver();
    CourierSchemaParser p1 = new CourierSchemaParser(resolver);
    p1.parse("namespace org.example\nrecord SomeType { id: int }");
    Assert.assertFalse(p1.errorMessage(), p1.hasError());

    CourierSchemaParser p2 = new CourierSchemaParser(resolver);
    p2.parse(
        "namespace org.consumer\n" +
        "import org.example.SomeType\n" +
        "record Consumer { ref: SomeType }");
    Assert.assertFalse(p2.errorMessage(), p2.hasError());
    RecordDataSchema consumer = (RecordDataSchema) p2.topLevelDataSchemas().get(0);
    Assert.assertNotNull(consumer.getField("ref"));
  }

  // ---------------------------------------------------------------------------
  // Unknown type reference → error recorded, null returned (non-fatal)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_unknownTypeRef_recordsError() {
    String errorMsg = parseExpectError(
        "namespace org.test\nrecord Bad { x: NonExistentType }");
    Assert.assertFalse(errorMsg.isEmpty());
  }

  // ---------------------------------------------------------------------------
  // Duplicate import → error recorded
  // ---------------------------------------------------------------------------

  @Test
  public void parse_duplicateImport_recordsError() {
    // Register the types first
    DefaultDataSchemaResolver resolver = new DefaultDataSchemaResolver();
    CourierSchemaParser p1 = new CourierSchemaParser(resolver);
    p1.parse("namespace org.a\nrecord Foo { id: int }");
    CourierSchemaParser p2 = new CourierSchemaParser(resolver);
    p2.parse("namespace org.b\nrecord Foo { id: int }");

    CourierSchemaParser p3 = new CourierSchemaParser(resolver);
    p3.parse(
        "namespace org.consumer\n" +
        "import org.a.Foo\n" +
        "import org.b.Foo\n" +
        "record Consumer { x: int }");
    Assert.assertTrue("Expected duplicate import error", p3.hasError());
    Assert.assertTrue(p3.errorMessage().contains("already defined in an import"));
  }

  // ---------------------------------------------------------------------------
  // Record with include of a non-record type → error
  // ---------------------------------------------------------------------------

  @Test
  public void parse_includeOfNonRecordType_recordsError() {
    DefaultDataSchemaResolver resolver = new DefaultDataSchemaResolver();
    CourierSchemaParser p1 = new CourierSchemaParser(resolver);
    p1.parse("namespace org.test\ntyperef MyString = string");
    Assert.assertFalse(p1.errorMessage(), p1.hasError());

    CourierSchemaParser p2 = new CourierSchemaParser(resolver);
    p2.parse("namespace org.test\nrecord Bad { ...MyString }");
    Assert.assertTrue("Expected error for non-record include", p2.hasError());
    Assert.assertTrue(p2.errorMessage().contains("Include is not a record type"));
  }

  // ---------------------------------------------------------------------------
  // Union in typeref
  // ---------------------------------------------------------------------------

  @Test
  public void parse_typerefToUnion_parsesUnionRef() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "typeref MyUnion = union[int, string, boolean]");
    Assert.assertTrue(schema instanceof TyperefDataSchema);
    TyperefDataSchema typeref = (TyperefDataSchema) schema;
    Assert.assertTrue(typeref.getRef() instanceof UnionDataSchema);
    UnionDataSchema union = (UnionDataSchema) typeref.getRef();
    Assert.assertEquals(3, union.getTypes().size());
  }

  // ---------------------------------------------------------------------------
  // Record with multiple property paths (nested property merging)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_nestedPropertyPaths_mergedIntoNestedMap() {
    NamedDataSchema schema = parseOk(
        "namespace org.test\n" +
        "@validate.min = 0\n" +
        "@validate.max = 100\n" +
        "record WithNestedProps { value: int }");
    Map<String, Object> props = schema.getProperties();
    Assert.assertTrue("Expected 'validate' property", props.containsKey("validate"));
    Object validate = props.get("validate");
    Assert.assertTrue(validate instanceof DataMap);
    DataMap validateMap = (DataMap) validate;
    Assert.assertEquals(0, validateMap.get("min"));
    Assert.assertEquals(100, validateMap.get("max"));
  }

  // ---------------------------------------------------------------------------
  // ParseErrorLocation.toString() — covered indirectly via syntax error message formatting
  // ---------------------------------------------------------------------------

  @Test
  public void parse_syntaxError_errorMessageContainsLineAndColumn() {
    // Ensures ParseErrorLocation.toString() is called: the error message format is "line,col: msg"
    CourierSchemaParser parser = newParser();
    parser.parse("namespace org.test\nrecord Broken {{{{{");
    Assert.assertTrue("Expected parse errors", parser.hasError());
    // Error message should contain a line/column prefix like "2,15:"
    String errorMsg = parser.errorMessage();
    Assert.assertFalse("Error message should be non-empty", errorMsg.isEmpty());
    // It should look like "LINE,COL: ..."
    Assert.assertTrue("Error message should contain comma-separated line,col prefix",
        errorMsg.matches("(?s).*\\d+,\\d+:.*"));
  }

  // ---------------------------------------------------------------------------
  // Conflicting property path (nested path traversal with non-DataMap existing value)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_conflictingPropertyPath_recordsError() {
    // Set @a = "foo" first, then @a.b = "bar" — 'a' is a String, not a DataMap,
    // so the nested path traversal hits the "Conflicting property" branch.
    CourierSchemaParser parser = newParser();
    parser.parse(
        "namespace org.test\n" +
        "@a = \"foo\"\n" +
        "@a.b = \"bar\"\n" +
        "record ConflictProp { value: int }");
    Assert.assertTrue("Expected error for conflicting property", parser.hasError());
    Assert.assertTrue(parser.errorMessage().contains("Conflicting property"));
  }

  // ---------------------------------------------------------------------------
  // Duplicate property at same path (property already defined)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_duplicatePropertyAtSamePath_recordsError() {
    // Define @myProp twice — second should fail as "Property already defined"
    CourierSchemaParser parser = newParser();
    parser.parse(
        "namespace org.test\n" +
        "@myProp = \"first\"\n" +
        "@myProp = \"second\"\n" +
        "record DupProp { value: int }");
    Assert.assertTrue("Expected error for duplicate property", parser.hasError());
    Assert.assertTrue(parser.errorMessage().contains("Property already defined"));
  }

  // ---------------------------------------------------------------------------
  // File-based resolution — loading from reference suite
  // ---------------------------------------------------------------------------

  @Test
  public void parse_fileBasedFixedSchema_parsesCorrectly() {
    File courierPath = new File(
        System.getProperty("referencesuite.srcdir") +
        File.separator + "main" + File.separator + "courier");
    FileDataSchemaResolver resolver = new FileDataSchemaResolver(
        new CourierSchemaParserFactory(), courierPath.getAbsolutePath());
    resolver.setExtension(".courier");
    StringBuilder errors = new StringBuilder();
    NamedDataSchema schema = resolver.findDataSchema("org.coursera.fixed.Fixed8", errors);
    Assert.assertTrue(errors.toString(), schema != null);
    Assert.assertTrue(schema instanceof FixedDataSchema);
    Assert.assertEquals(8, ((FixedDataSchema) schema).getSize());
  }

  @Test
  public void parse_fileBasedEnumWithSymbolProperties_parsesCorrectly() {
    File courierPath = new File(
        System.getProperty("referencesuite.srcdir") +
        File.separator + "main" + File.separator + "courier");
    FileDataSchemaResolver resolver = new FileDataSchemaResolver(
        new CourierSchemaParserFactory(), courierPath.getAbsolutePath());
    resolver.setExtension(".courier");
    StringBuilder errors = new StringBuilder();
    NamedDataSchema schema = resolver.findDataSchema("org.coursera.enums.EnumProperties", errors);
    Assert.assertTrue(errors.toString(), schema != null);
    Assert.assertTrue(schema instanceof EnumDataSchema);
    Map<String, Object> props = schema.getProperties();
    Assert.assertTrue("Expected symbolProperties", props.containsKey("symbolProperties"));
  }

  @Test
  public void parse_fileBasedMapWithTypedKey_parsesKeysProperty() {
    File courierPath = new File(
        System.getProperty("referencesuite.srcdir") +
        File.separator + "main" + File.separator + "courier");
    FileDataSchemaResolver resolver = new FileDataSchemaResolver(
        new CourierSchemaParserFactory(), courierPath.getAbsolutePath());
    resolver.setExtension(".courier");
    StringBuilder errors = new StringBuilder();
    NamedDataSchema schema =
        resolver.findDataSchema("org.coursera.maps.WithTypedKeyMap", errors);
    Assert.assertTrue(errors.toString(), schema != null);
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    // "ints" field is map[int, string] — should have "keys" property
    RecordDataSchema.Field intsField = record.getField("ints");
    Assert.assertNotNull(intsField);
    MapDataSchema mapSchema = (MapDataSchema) intsField.getType();
    Assert.assertNotNull("Expected 'keys' property for int-keyed map",
        mapSchema.getProperties().get("keys"));
  }

  @Test
  public void parse_fileBasedRecordWithInclude_includesBaseFields() {
    File courierPath = new File(
        System.getProperty("referencesuite.srcdir") +
        File.separator + "main" + File.separator + "courier");
    FileDataSchemaResolver resolver = new FileDataSchemaResolver(
        new CourierSchemaParserFactory(), courierPath.getAbsolutePath());
    resolver.setExtension(".courier");
    StringBuilder errors = new StringBuilder();
    NamedDataSchema schema =
        resolver.findDataSchema("org.coursera.records.test.WithInclude", errors);
    Assert.assertTrue(errors.toString(), schema != null);
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    // WithInclude does ...Simple (which has 'message') plus 'direct'
    Assert.assertNotNull("Expected included field 'message'", record.getField("message"));
    Assert.assertNotNull("Expected direct field 'direct'", record.getField("direct"));
  }

  @Test
  public void parse_fileBasedRecordWithPrimitiveDefaults_parsesAllDefaults() {
    File courierPath = new File(
        System.getProperty("referencesuite.srcdir") +
        File.separator + "main" + File.separator + "courier");
    FileDataSchemaResolver resolver = new FileDataSchemaResolver(
        new CourierSchemaParserFactory(), courierPath.getAbsolutePath());
    resolver.setExtension(".courier");
    StringBuilder errors = new StringBuilder();
    NamedDataSchema schema =
        resolver.findDataSchema("org.coursera.records.test.WithPrimitiveDefaults", errors);
    Assert.assertTrue(errors.toString(), schema != null);
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    Assert.assertEquals(1, record.getField("intWithDefault").getDefault());
    Assert.assertEquals(Boolean.TRUE, record.getField("booleanWithDefault").getDefault());
    Assert.assertEquals("DEFAULT", record.getField("stringWithDefault").getDefault());
  }

  @Test
  public void parse_fileBasedRecordWithComplexTypeDefaults_parsesAllDefaults() {
    File courierPath = new File(
        System.getProperty("referencesuite.srcdir") +
        File.separator + "main" + File.separator + "courier");
    FileDataSchemaResolver resolver = new FileDataSchemaResolver(
        new CourierSchemaParserFactory(), courierPath.getAbsolutePath());
    resolver.setExtension(".courier");
    StringBuilder errors = new StringBuilder();
    NamedDataSchema schema =
        resolver.findDataSchema("org.coursera.records.test.WithComplexTypeDefaults", errors);
    Assert.assertTrue(errors.toString(), schema != null);
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    // array default
    Object arrayDefault = record.getField("array").getDefault();
    Assert.assertTrue("Expected DataList for array default", arrayDefault instanceof DataList);
    // map default
    Object mapDefault = record.getField("map").getDefault();
    Assert.assertTrue("Expected DataMap for map default", mapDefault instanceof DataMap);
    // record default
    Object recordDefault = record.getField("record").getDefault();
    Assert.assertTrue("Expected DataMap for record default", recordDefault instanceof DataMap);
  }

  @Test
  public void parse_fileBasedNumericDefaults_parsesNumbers() {
    File courierPath = new File(
        System.getProperty("referencesuite.srcdir") +
        File.separator + "main" + File.separator + "courier");
    FileDataSchemaResolver resolver = new FileDataSchemaResolver(
        new CourierSchemaParserFactory(), courierPath.getAbsolutePath());
    resolver.setExtension(".courier");
    StringBuilder errors = new StringBuilder();
    NamedDataSchema schema =
        resolver.findDataSchema("org.coursera.records.test.NumericDefaults", errors);
    Assert.assertTrue(errors.toString(), schema != null);
    RecordDataSchema record = (RecordDataSchema) schema;
    Assert.assertEquals(Integer.MAX_VALUE, record.getField("i").getDefault());
    Assert.assertEquals(Long.MAX_VALUE, ((Number) record.getField("l").getDefault()).longValue());
  }

  @Test
  public void parse_fileBasedEnumWithDeprecated_parsesDeprecatedRecord() {
    File courierPath = new File(
        System.getProperty("referencesuite.srcdir") +
        File.separator + "main" + File.separator + "courier");
    FileDataSchemaResolver resolver = new FileDataSchemaResolver(
        new CourierSchemaParserFactory(), courierPath.getAbsolutePath());
    resolver.setExtension(".courier");
    StringBuilder errors = new StringBuilder();
    NamedDataSchema schema =
        resolver.findDataSchema("org.coursera.deprecated.DeprecatedRecord", errors);
    Assert.assertTrue(errors.toString(), schema != null);
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    // @deprecated on the record itself
    Assert.assertNotNull("Expected deprecated property on schema",
        record.getProperties().get("deprecated"));
    // @deprecated on field1
    Assert.assertNotNull("Expected deprecated on field1",
        record.getField("field1").getProperties().get("deprecated"));
  }

  @Test
  public void parse_fileBasedMapWithComplexTypes_parsesUnionValueType() {
    File courierPath = new File(
        System.getProperty("referencesuite.srcdir") +
        File.separator + "main" + File.separator + "courier");
    FileDataSchemaResolver resolver = new FileDataSchemaResolver(
        new CourierSchemaParserFactory(), courierPath.getAbsolutePath());
    resolver.setExtension(".courier");
    StringBuilder errors = new StringBuilder();
    NamedDataSchema schema =
        resolver.findDataSchema("org.coursera.maps.WithComplexTypesMap", errors);
    Assert.assertTrue(errors.toString(), schema != null);
    Assert.assertTrue(schema instanceof RecordDataSchema);
    RecordDataSchema record = (RecordDataSchema) schema;
    // 'unions' field has an inline typeref as value type
    Assert.assertNotNull(record.getField("unions"));
  }

  // ---------------------------------------------------------------------------
  // parse(Reader) — IOException during ANTLRInputStream construction
  // Covers lines 154-157: catch (IOException e) in parse(Reader)
  // ---------------------------------------------------------------------------

  @Test
  public void parse_readerThrowsIOException_recordsErrorAndReturns() throws Exception {
    Reader throwingReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        throw new IOException("simulated read failure");
      }
      @Override
      public void close() throws IOException {
        // no-op
      }
    };
    CourierSchemaParser parser = newParser();
    parser.parse(throwingReader);
    // Should have recorded an error (not thrown), and hasError() should be true
    Assert.assertTrue("Expected error from IOException in Reader", parser.hasError());
    Assert.assertTrue("Error message should contain the IOException message",
        parser.errorMessage().contains("simulated read failure"));
  }
}
