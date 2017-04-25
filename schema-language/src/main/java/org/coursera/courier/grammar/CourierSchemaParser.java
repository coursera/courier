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
import com.linkedin.data.codec.DataLocation;
import com.linkedin.data.codec.JacksonDataCodec;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.DataSchemaResolver;
import com.linkedin.data.schema.DataSchemaUtil;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.FixedDataSchema;
import com.linkedin.data.schema.JsonBuilder;
import com.linkedin.data.schema.MapDataSchema;
import com.linkedin.data.schema.Name;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.RecordDataSchema.Field;
import com.linkedin.data.schema.SchemaParser;
import com.linkedin.data.schema.SchemaToJsonEncoder;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.schema.UnionDataSchema;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.coursera.courier.grammar.CourierParser.AnonymousTypeDeclarationContext;
import org.coursera.courier.grammar.CourierParser.ArrayDeclarationContext;
import org.coursera.courier.grammar.CourierParser.DocumentContext;
import org.coursera.courier.grammar.CourierParser.EnumDeclarationContext;
import org.coursera.courier.grammar.CourierParser.EnumSymbolDeclarationContext;
import org.coursera.courier.grammar.CourierParser.FieldDeclarationContext;
import org.coursera.courier.grammar.CourierParser.FieldDefaultContext;
import org.coursera.courier.grammar.CourierParser.FieldSelectionContext;
import org.coursera.courier.grammar.CourierParser.FieldSelectionElementContext;
import org.coursera.courier.grammar.CourierParser.FixedDeclarationContext;
import org.coursera.courier.grammar.CourierParser.ImportDeclarationContext;
import org.coursera.courier.grammar.CourierParser.ImportDeclarationsContext;
import org.coursera.courier.grammar.CourierParser.JsonValueContext;
import org.coursera.courier.grammar.CourierParser.KeyDeclarationContext;
import org.coursera.courier.grammar.CourierParser.MapDeclarationContext;
import org.coursera.courier.grammar.CourierParser.NamedTypeDeclarationContext;
import org.coursera.courier.grammar.CourierParser.ObjectEntryContext;
import org.coursera.courier.grammar.CourierParser.PropDeclarationContext;
import org.coursera.courier.grammar.CourierParser.RecordDeclarationContext;
import org.coursera.courier.grammar.CourierParser.TypeAssignmentContext;
import org.coursera.courier.grammar.CourierParser.TypeDeclarationContext;
import org.coursera.courier.grammar.CourierParser.TypeReferenceContext;
import org.coursera.courier.grammar.CourierParser.TyperefDeclarationContext;
import org.coursera.courier.grammar.CourierParser.UnionDeclarationContext;
import org.coursera.courier.grammar.CourierParser.UnionMemberDeclarationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Parses courier schema language source and generates Pegasus DataSchema types.
 */
public class CourierSchemaParser extends SchemaParser {

  private final List<DataSchema> topLevelSchemas;

  @SuppressWarnings("unchecked")
  public CourierSchemaParser(DataSchemaResolver resolver) {
    super(resolver);
    try {
      // TODO(jbetz):
      // Replace with https://github.com/coursera/courier/tree/with-restli-upstream-fixes
      // once https://github.com/linkedin/rest.li/pull/60 is accepted.
      java.lang.reflect.Field topLevelDataSchemasField =
        SchemaParser.class.getDeclaredField("_topLevelDataSchemas");
      topLevelDataSchemasField.setAccessible(true);
      this.topLevelSchemas = (List<DataSchema>) topLevelDataSchemasField.get(this);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Parse a representation of a schema from source.
   *
   * The top level {{DataSchema}'s parsed are in {{#topLevelDataSchemas}.
   * These are the types that are not defined within other types.
   * Parse errors are in {{#errorMessageBuilder} and indicated
   * by {{#hasError()}.
   *
   * @param source with the source code representation of the schema.
   */
  @Override
  public void parse(String source) {
    parse(new StringReader(source));
  }

  /**
   * Parse a JSON representation of a schema from an {{java.io.InputStream}}.
   *
   * The top level {{DataSchema}}'s parsed are in {{#topLevelDataSchemas}}.
   * These are the types that are not defined within other types.
   * Parse errors are in {{#errorMessageBuilder}} and indicated
   * by {{#hasError()}}.
   *
   * @param inputStream with the JSON representation of the schema.
   */
  @Override
  public void parse(InputStream inputStream) {
    parse(new InputStreamReader(inputStream));
  }

  /**
   * Parse a JSON representation of a schema from a {{java.io.Reader}}.
   *
   * The top level {{DataSchema}}'s parsed are in {{#topLevelDataSchemas}}.
   * These are the types that are not defined within other types.
   * Parse errors are in {{#errorMessageBuilder}} and indicated
   * by {{#hasError()}}.
   *
   * @param reader with the JSON representation of the schema.
   */
  @Override
  public void parse(Reader reader) {
    try {
      ErrorRecorder errorRecorder = new ErrorRecorder();
      CourierLexer lexer;
      try {
        lexer = new CourierLexer(new ANTLRInputStream(reader));
      } catch (IOException e) {
        ParseError error = new ParseError(new ParseErrorLocation(0, 0), e.getMessage());
        startErrorMessage(error).append(error.message).append("\n");
        return;
      }
      lexer.removeErrorListeners();
      lexer.addErrorListener(errorRecorder);

      CourierParser parser = new CourierParser(new CommonTokenStream(lexer));
      parser.removeErrorListeners();
      parser.addErrorListener(errorRecorder);

      DocumentContext antlrDocument = parser.document();
//      System.out.println("\n!!!!!!!!\n"+antlrDocument.toStringTree()+"\n!!!!!!!!\n");
      parse(antlrDocument);

      if (errorRecorder.errors.size() > 0) {
        for (ParseError error : errorRecorder.errors) {
          startErrorMessage(error).append(error.message).append("\n");
        }
      }
    } catch (ParseException e) {
      startErrorMessage(e.error).append(e.getMessage()).append("\n");
    }
  }

  private StringBuilder startErrorMessage(ParseError error) {
    return errorMessageBuilder().append(error.location).append(": ");
  }

  private StringBuilder startErrorMessage(ParserRuleContext context) {
    return errorMessageBuilder().append(new ParseErrorLocation(context)).append(": ");
  }

  /**
   * An ANTLR lexer or parser error.
   */
  private class ParseError {
    public final ParseErrorLocation location;
    public final String message;

    public ParseError(ParseErrorLocation location, String message) {
      this.location = location;
      this.message = message;
    }
  }

  /**
   * Pegasus DataLocation implementation for tracking ANTLR lexer and parser error source
   * coordinates.
   */
  private class ParseErrorLocation implements DataLocation {
    public final int line;
    public final int column;

    public ParseErrorLocation(ParserRuleContext context) {
      Token start = context.getStart();
      this.line = start.getLine();
      this.column = start.getCharPositionInLine();
    }

    public ParseErrorLocation(int line, int column) {
      this.line = line;
      this.column = column;
    }

    @Override
    public int compareTo(DataLocation location) {
      if (!(location instanceof ParseErrorLocation)) {
        return -1;
      } else {
        ParseErrorLocation other = (ParseErrorLocation)location;
        int lineCompare = this.line - other.line;
        if (lineCompare != 0) {
          return lineCompare;
        }
        return this.column - other.column;
      }
    }

    @Override
    public String toString() {
      return line + "," + column;
    }
  }

  /**
   * An exceptional parse error.  Should only be thrown for parse errors that are unrecoverable,
   * i.e. errors forcing the parser to must halt immediately and not continue to parse the
   * document in search of other potential errors to report.
   *
   * For recoverable parse errors, the error should instead be recorded using startErrorMessage.
   */
  private class ParseException extends IOException {
    public final ParseError error;

    public ParseException(ParserRuleContext context, String msg) {
      this(new ParseError(new ParseErrorLocation(context), msg));
    }
    public ParseException(ParseError error) {
      super(error.message);
      this.error = error;
    }
  }

  /**
   * Error recorder to capture ANTLR lexer and parser errors.
   */
  private class ErrorRecorder extends BaseErrorListener {
    public final List<ParseError> errors = new LinkedList<ParseError>();

    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int column,
                            String msg,
                            RecognitionException e) {
      ParseErrorLocation location = new ParseErrorLocation(line, column);
      errors.add(new ParseError(location, msg));
    }
  }

  /**
   * Parse list of Data objects.
   *
   * The {{DataSchema}'s parsed are in {{#topLevelDataSchemas}.
   * Parse errors are in {{#errorMessageBuilder} and indicated
   * by {{#hasError()}.
   *
   * @param document provides the source code in AST form
   */
  private String parse(DocumentContext document) throws ParseException {
    if (document.namespaceDeclaration() != null) {
      setCurrentNamespace(
        document.namespaceDeclaration().qualifiedIdentifier().value);
    } else {
      setCurrentNamespace("");
    }

    setCurrentImports(document.importDeclarations());
    NamedDataSchema schema = parseNamedType(document.namedTypeDeclaration());
    topLevelSchemas.add(schema);
    return schema.getFullName();
  }

  private DataSchema parseType(TypeDeclarationContext typ) throws ParseException{
    if (typ.namedTypeDeclaration() != null) {
      return parseNamedType(typ.namedTypeDeclaration());
    } else if (typ.anonymousTypeDeclaration() != null) {
      AnonymousTypeDeclarationContext anon = typ.anonymousTypeDeclaration();
      if (anon.unionDeclaration() != null) {
        return parseUnion(anon.unionDeclaration(), false);
      } else if (anon.mapDeclaration() != null) {
        return parseMap(anon.mapDeclaration());
      } else if (anon.arrayDeclaration() != null) {
        return parseArray(anon.arrayDeclaration());
      } else {
        throw new ParseException(anon,
          "Unrecognized type parse node: " + anon.getText());
      }
    } else {
      throw new ParseException(typ,
        "Unrecognized typ declaration parse node: " + typ.getText());
    }
  }

  private NamedDataSchema parseNamedType(
      NamedTypeDeclarationContext namedType) throws ParseException {
    if (namedType.recordDeclaration() != null) {
      return parseRecord(namedType, namedType.recordDeclaration());
    } else if (namedType.typerefDeclaration() != null) {
      return parseTyperef(namedType, namedType.typerefDeclaration());
    } else if (namedType.fixedDeclaration() != null) {
      return parseFixed(namedType, namedType.fixedDeclaration());
    } else if (namedType.enumDeclaration() != null) {
      return parseEnum(namedType, namedType.enumDeclaration());
    } else if (namedType.keyDeclaration() != null) {
      return parseKey(namedType, namedType.keyDeclaration());
    } else {
      throw new ParseException(namedType,
        "Unrecognized named type parse node: " + namedType.getText());
    }
  }

  private FixedDataSchema parseFixed(
      NamedTypeDeclarationContext context,
      FixedDeclarationContext fixed) throws ParseException {
    Name name = toName(fixed.name);
    FixedDataSchema schema = new FixedDataSchema(name);

    bindNameToSchema(name, schema);

    schema.setSize(fixed.size, errorMessageBuilder());
    setProperties(context, schema);
    return schema;
  }

  private EnumDataSchema parseEnum(
      NamedTypeDeclarationContext context,
      EnumDeclarationContext enumDecl) throws ParseException {
    Name name = toName(enumDecl.name);
    EnumDataSchema schema = new EnumDataSchema(name);

    bindNameToSchema(name, schema);

    List<EnumSymbolDeclarationContext> symbolDecls = enumDecl.enumDecl.symbolDecls;

    List<String> symbols = new ArrayList<String>(symbolDecls.size());
    for (EnumSymbolDeclarationContext symbolDecl : symbolDecls) {
      symbols.add(symbolDecl.symbol.value);
    }
    schema.setSymbols(symbols, errorMessageBuilder());

    Map<String, Object> props = setProperties(context, schema);

    Map<String, Object> symbolDocs = new HashMap<String, Object>();
    for (EnumSymbolDeclarationContext symbolDecl : symbolDecls) {
      if (symbolDecl.doc != null) {
        symbolDocs.put(symbolDecl.symbol.value, symbolDecl.doc.value);
      }
    }
    if (symbolDocs.size() > 0) {
      schema.setSymbolDocs(symbolDocs, errorMessageBuilder());
    }

    DataMap deprecatedSymbols = new DataMap();
    DataMap symbolProperties = new DataMap();
    for (EnumSymbolDeclarationContext symbolDecl: symbolDecls) {
      for (PropDeclarationContext prop: symbolDecl.props) {
        String symbol = symbolDecl.symbol.value;
        Object value = parsePropValue(prop);
        if (prop.name.equals("deprecated")) {
          deprecatedSymbols.put(symbol, value);
        } else {
          List<String> path = new ArrayList<>(prop.path);
          path.add(0, symbol);
          addPropertiesAtPath(prop, symbolProperties, path, value);
        }
      }
    }

    if (deprecatedSymbols.size() > 0) {
      props.put("deprecatedSymbols", deprecatedSymbols);
    }

    if (symbolProperties.size() > 0) {
      props.put("symbolProperties", symbolProperties);
    }

    schema.setProperties(props);
    return schema;
  }

  private TyperefDataSchema parseTyperef(
      NamedTypeDeclarationContext context,
      TyperefDeclarationContext typeref) throws ParseException {

    Name name = toName(typeref.name);
    TyperefDataSchema schema = new TyperefDataSchema(name);
    bindNameToSchema(name, schema);
    DataSchema refSchema = toDataSchema(typeref.ref);
    schema.setReferencedType(refSchema);

    setProperties(context, schema);
    return schema;
  }

  private ArrayDataSchema parseArray(ArrayDeclarationContext array) throws ParseException {
    return new ArrayDataSchema(toDataSchema(array.typeParams.items));
  }

  private static JacksonDataCodec codec = new JacksonDataCodec();

  private MapDataSchema parseMap(MapDeclarationContext map) throws ParseException {
    TypeAssignmentContext keyType = map.typeParams.key;
    TypeAssignmentContext valueType = map.typeParams.value;
    MapDataSchema schema = new MapDataSchema(toDataSchema(valueType));
    Map<String, Object> propsToAdd = new HashMap<String, Object>();

    if (keyType.typeReference() != null) {
      String typeName = keyType.typeReference().value;

      // TODO(jbetz):
      // Replace with https://github.com/coursera/courier/tree/with-restli-upstream-fixes
      // once https://github.com/linkedin/rest.li/pull/61 is accepted.
      if (!typeName.equals("string")) {
        String qualifiedKeyName = computeFullName(typeName);
        propsToAdd.put("keys", qualifiedKeyName);
      }
    } else if (keyType.typeDeclaration() != null) {
      DataSchema keySchema = parseType(keyType.typeDeclaration());
      String json = SchemaToJsonEncoder.schemaToJson(keySchema, JsonBuilder.Pretty.COMPACT);
      try {
        DataMap dataMap = codec.stringToMap(json);
        propsToAdd.put("keys", dataMap);
      } catch (IOException e) {
        startErrorMessage(map)
          .append("Unexpected error parsing map: ").append(e.getMessage()).append("\n");
      }
    }

    schema.setProperties(propsToAdd);
    return schema;
  }

  private UnionDataSchema parseUnion(
      UnionDeclarationContext union, boolean withinTypref) throws ParseException {
    UnionDataSchema schema = new UnionDataSchema();
    List<UnionMemberDeclarationContext> members = union.typeParams.members;
    List<DataSchema> types = new ArrayList<DataSchema>(members.size());
    for (UnionMemberDeclarationContext memberDecl: members) {
      TypeAssignmentContext memberType = memberDecl.member;
      DataSchema dataSchema = toDataSchema(memberType);
      if (dataSchema != null) {
        types.add(dataSchema);
      }
    }
    schema.setTypes(types, errorMessageBuilder());
    return schema;
  }

  private RecordDataSchema parseKey(
      NamedTypeDeclarationContext context,
      KeyDeclarationContext key) throws ParseException {
    Name name = toName(key.name);
    RecordDataSchema schema = new RecordDataSchema(name, RecordDataSchema.RecordType.RECORD);

    bindNameToSchema(name, schema);
    FieldsAndIncludes fieldsAndIncludes = parseFields(schema, key.keyDecl);
    schema.setFields(fieldsAndIncludes.fields, errorMessageBuilder());
    validateDefaults(schema);
    schema.setInclude(fieldsAndIncludes.includes);
    setProperties(context, schema);
    return schema;
  }

  private RecordDataSchema parseRecord(
          NamedTypeDeclarationContext context,
          RecordDeclarationContext record) throws ParseException {
    Name name = toName(record.name);
    RecordDataSchema schema = new RecordDataSchema(name, RecordDataSchema.RecordType.RECORD);

    bindNameToSchema(name, schema);
    FieldsAndIncludes fieldsAndIncludes = parseFields(schema, record.recordDecl);
    schema.setFields(fieldsAndIncludes.fields, errorMessageBuilder());
    validateDefaults(schema);
    schema.setInclude(fieldsAndIncludes.includes);
    setProperties(context, schema);
    return schema;
  }

  private Map<String, Object> setProperties(
      NamedTypeDeclarationContext source, NamedDataSchema target) throws ParseException {

    Map<String, Object> properties = new HashMap<String, Object>();
    properties.putAll(target.getProperties());

    if (source.doc != null) {
      target.setDoc(source.doc.value);
    }

    for (PropDeclarationContext prop: source.props) {
      addPropertiesAtPath(properties, prop);
    }

    target.setProperties(properties);
    return properties;
  }

  /**
   * Adds additional properties to an existing properties map at the location identified by
   * the given PropDeclarationContexts path.
   *
   * @param existingProperties provides the existing properties to add the additional properties to.
   * @param prop provides the ANTLR property AST node to add the properties for.
   */
  private void addPropertiesAtPath(
      Map<String, Object> existingProperties, PropDeclarationContext prop) throws ParseException{
    addPropertiesAtPath(prop, existingProperties, prop.path, parsePropValue(prop));
  }

  /**
   * Adds additional properties to an existing properties map at the location identified by
   * the given path.
   *
   * This allows for properties defined with paths such as:
   *
   * {@literal @}a.b = "x"
   * {@literal @}a.c = "y"
   *
   * to be merged together into a property map like:
   *
   * { "a": { "b": "x", "c": "y" }}
   *
   * Examples:
   *
   * <pre>
   * existing properties        | path  | value         | result
   * ---------------------------|-------|---------------|----------------------------------------
   * {}                         | a.b.c | true          | { "a": { "b": { "c": true } } }
   * { "a": {} }                | a.b   | true          | { "a": { "b": true } }
   * { "a": {} }                | a.b   | { "z": "x" }  | { "a": { "b": { "z": "x" } } }
   * { "a": { "c": "x"}} }      | a.b   | true          | { "a": { "b": true, "c": "x"} } }
   * { "a": { "b": "x"}} }      | a.b   | "y"           | ParseError "Conflicting property: a.b"
   * </pre>
   *
   * The existing properties are traversed using the given path, adding DataMaps as needed to
   * complete the traversal. If any of data elements in the existing properties along the path are
   * not DataMaps, a ParseError is thrown to report the conflict.
   *
   * @param context provides the parsing context for error reporting purposes.
   * @param existingProperties provides the properties to add to.
   * @param path provides the path of the property to insert.
   * @param value provides the value of the property to insert.
   * @throws ParseException if the path of the properties to add conflicts with data already
   * in the properties map or if a property is already exists at the path.
   */
  private void addPropertiesAtPath(
      ParserRuleContext context,
      Map<String, Object> existingProperties,
      Iterable<String> path,
      Object value) throws ParseException {
    Map<String, Object> current = existingProperties;
    Iterator<String> iter = path.iterator();
    while (iter.hasNext()) {
      String pathPart = iter.next();
      if (iter.hasNext()) {
        if (existingProperties.containsKey(pathPart)) {
          Object val = existingProperties.get(pathPart);
          if (!(val instanceof DataMap)) {
            throw new ParseException(
              new ParseError(
                new ParseErrorLocation(context),
                "Conflicting property: " + path.toString()));
          }
          current = (DataMap) val;
        } else {
          DataMap next = new DataMap();
          current.put(pathPart, next);
          current = next;
        }
      } else {
        if (current.containsKey(pathPart)) {
          throw new ParseException(
            new ParseError(
              new ParseErrorLocation(context),
              "Property already defined: " + path.toString()));
        } else {
          current.put(pathPart, value);
        }
      }
    }
  }

  private static class FieldsAndIncludes {
    public final List<RecordDataSchema.Field> fields;
    public final List<NamedDataSchema> includes;

    public FieldsAndIncludes(List<Field> fields, List<NamedDataSchema> includes) {
      this.fields = fields;
      this.includes = includes;
    }
  }

  private FieldsAndIncludes parseFields(
      RecordDataSchema recordSchema,
      FieldSelectionContext fieldGroup) throws ParseException {

    List<RecordDataSchema.Field> results = new ArrayList<RecordDataSchema.Field>();
    List<NamedDataSchema> includes = new ArrayList<NamedDataSchema>();
    for (FieldSelectionElementContext element : fieldGroup.fields) {
      FieldDeclarationContext field = element.fieldDeclaration();
      if (field != null) {
        Field result = new Field(toDataSchema(field.type));
        Map<String, Object> properties = new HashMap<String, Object>();
        result.setName(field.name, errorMessageBuilder());
        result.setOptional(field.isOptional);

        FieldDefaultContext fieldDefault = field.fieldDefault();
        if (fieldDefault != null) {
          JsonValueContext defaultValue = fieldDefault.jsonValue();
          if (defaultValue != null) {
            result.setDefault(parseJsonValue(defaultValue));
          }
        }

        for (PropDeclarationContext prop : field.props) {
          addPropertiesAtPath(properties, prop);
        }
        if (field.doc != null) {
          result.setDoc(field.doc.value);
        }
        result.setProperties(properties);
        result.setRecord(recordSchema);
        results.add(result);
      } else if (element.fieldInclude() != null) {
        TypeReferenceContext includeRef = element.fieldInclude().typeReference();
        DataSchema includedSchema = toDataSchema(includeRef);
        if (includedSchema != null) {
          DataSchema dereferencedIncludedSchema = includedSchema.getDereferencedDataSchema();
          if (dereferencedIncludedSchema instanceof RecordDataSchema) {
            RecordDataSchema includedRecordSchema = (RecordDataSchema) dereferencedIncludedSchema;
            results.addAll(includedRecordSchema.getFields());

            includes.add(includedRecordSchema);
          } else {
            startErrorMessage(element)
              .append("Include is not a record type: ")
              .append(includeRef.value).append("\n");
          }
        }
      } else {
        startErrorMessage(element)
          .append("Unrecognized field element parse node: ")
          .append(element.getText()).append("\n");
      }
    }
    return new FieldsAndIncludes(results, includes);
  }

  private DataSchema toDataSchema(TypeReferenceContext typeReference) throws ParseException {
    DataSchema dataSchema = stringToDataSchema(typeReference.value);
    if (dataSchema != null) {
      return dataSchema;
    } else {
      startErrorMessage(typeReference)
        .append("Type not found: ")
        .append(typeReference.value).append("\n");
      // Pegasus is designed to track null data schema references as errors, so we intentionally
      // return null here.
      return null;
    }
  }

  private DataSchema toDataSchema(TypeAssignmentContext typeAssignment) throws ParseException {
    TypeReferenceContext typeReference = typeAssignment.typeReference();
    if (typeReference != null) {
      return toDataSchema(typeReference);
    }  else if (typeAssignment.typeDeclaration() != null) {
      return parseType(typeAssignment.typeDeclaration());
    } else {
      throw new ParseException(typeAssignment,
        "Unrecognized type assignment parse node: " + typeAssignment.getText() + "\n");
    }
  }

  private Name toName(String name) {
    return new Name(name, getCurrentNamespace(), errorMessageBuilder());
  }

  private Object parsePropValue(
      CourierParser.PropDeclarationContext prop) throws ParseException {
    if (prop.propJsonValue() != null) {
      return parseJsonValue(prop.propJsonValue().jsonValue());
    } else {
      return Boolean.TRUE;
    }
  }

  private Object parseJsonValue(JsonValueContext jsonValue) throws ParseException {
    if (jsonValue.array() != null) {
      DataList dataList = new DataList();
      for (JsonValueContext item: jsonValue.array().jsonValue()) {
        dataList.add(parseJsonValue(item));
      }
      return dataList;
    } else if (jsonValue.object() != null) {
      DataMap dataMap = new DataMap();
      for (ObjectEntryContext entry: jsonValue.object().objectEntry()) {
        dataMap.put(entry.key.value, parseJsonValue(entry.value));
      }
      return dataMap;
    } else if (jsonValue.string() != null) {
      return jsonValue.string().value;
    } else if (jsonValue.number() != null) {
      Number numberValue = jsonValue.number().value;
      if (numberValue == null) {
        startErrorMessage(jsonValue)
          .append("'")
          .append(jsonValue.number().getText())
          .append("' is not a valid int, long, float or double.")
          .append("\n");
        return 0;
      }
      return numberValue;
    } else if (jsonValue.bool() != null) {
      return jsonValue.bool().value;
    } else if (jsonValue.nullValue() != null) {
      return Null.getInstance();
    } else {
      startErrorMessage(jsonValue)
        .append("Unrecognized JSON parse node: ")
        .append(jsonValue.getText())
        .append("\n");
      return Null.getInstance();
    }
  }

  // Extended fullname computation to handle imports
  @Override
  public String computeFullName(String name) {
    String fullname;
    DataSchema schema = DataSchemaUtil.typeStringToPrimitiveDataSchema(name);
    if (schema != null)
    {
      fullname = name;
    }
    else if (Name.isFullName(name) || getCurrentNamespace().isEmpty())
    {
      fullname = name;  // already a fullname
    }
    else if (currentImports.containsKey(name)) {
      // imported names are higher precedence than names in current namespace
      fullname = currentImports.get(name).getFullName();
    }
    else
    {
      fullname = getCurrentNamespace() + "." + name; // assumed to be in current namespace
    }
    return fullname;
  }

  // simple name -> fullname
  private Map<String, Name> currentImports;

  private void setCurrentImports(ImportDeclarationsContext imports) {
    Map<String, Name> importsBySimpleName = new HashMap<String, Name>();
    for (ImportDeclarationContext importDecl: imports.importDeclaration()) {
      String importedFullname = importDecl.type.value;
      Name importedName = new Name(importedFullname);
      String importedSimpleName = importedName.getName();
      if (importsBySimpleName.containsKey(importedSimpleName)) {
        startErrorMessage(importDecl)
          .append("'")
          .append(importsBySimpleName.get(importedSimpleName))
          .append("' is already defined in an import.")
          .append("\n");
      }
      importsBySimpleName.put(importedSimpleName, importedName);
    }
    this.currentImports = importsBySimpleName;
  }
}
