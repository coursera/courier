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
import org.coursera.courier.grammar.CourierParser.JsonValueContext;
import org.coursera.courier.grammar.CourierParser.MapDeclarationContext;
import org.coursera.courier.grammar.CourierParser.NamedTypeDeclarationContext;
import org.coursera.courier.grammar.CourierParser.ObjectEntryContext;
import org.coursera.courier.grammar.CourierParser.PropDeclarationContext;
import org.coursera.courier.grammar.CourierParser.RecordDeclarationContext;
import org.coursera.courier.grammar.CourierParser.TypeAssignmentContext;
import org.coursera.courier.grammar.CourierParser.TypeDeclarationContext;
import org.coursera.courier.grammar.CourierParser.TypeNameDeclarationContext;
import org.coursera.courier.grammar.CourierParser.TypeReferenceContext;
import org.coursera.courier.grammar.CourierParser.TyperefDeclarationContext;
import org.coursera.courier.grammar.CourierParser.UnionDeclarationContext;
import org.coursera.courier.grammar.CourierParser.UnionMemberDeclarationContext;
import org.coursera.courier.grammar.CourierParser.ImportDeclarationContext;
import org.coursera.courier.grammar.CourierParser.ImportDeclarationsContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CourierSchemaParser extends SchemaParser {

  private final List<DataSchema> topLevelSchemas;

  @SuppressWarnings("unchecked")
  public CourierSchemaParser(DataSchemaResolver resolver) {
    super(resolver);
    try {
      // TODO(jbetz): Gahhh! We will submit pull request to rest.li so we can remove this hack.
      java.lang.reflect.Field topLevelDataSchemasField =
        SchemaParser.class.getDeclaredField("_topLevelDataSchemas");
      topLevelDataSchemasField.setAccessible(true);
      this.topLevelSchemas = (List<DataSchema>) topLevelDataSchemasField.get(this);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Parse a representation of a schema from source
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
      } catch(IOException e) {
        ParseError error = new ParseError(new ParseErrorLocation(0, 0), e.getMessage());
        startErrorMessage(error).append(error.message).append("\n");
        return;
      }
      lexer.removeErrorListeners();
      lexer.addErrorListener(errorRecorder);

      CourierParser parser = new CourierParser(new CommonTokenStream(lexer));
      parser.removeErrorListeners();
      parser.addErrorListener(errorRecorder);

      parse(parser.document());
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
   * For recoverable, parse errors the error should instead be recorded using startErrorMessage.
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
  private void parse(DocumentContext document) throws ParseException {
    setCurrentNamespace(
      document.namespaceDeclaration().namespace().value);
    setCurrentImports(document.importDeclarations());
    DataSchema schema = parseNamedType(document.namedTypeDeclaration());
    topLevelSchemas.add(schema);
  }

  private DataSchema parseType(TypeDeclarationContext typ) throws ParseException{
    if (typ.namedTypeDeclaration() != null) {
      return parseNamedType(typ.namedTypeDeclaration());
    } else if (typ.anonymousTypeDeclaration() != null) {
      AnonymousTypeDeclarationContext anon = typ.anonymousTypeDeclaration();
      if (anon.unionDeclaration() != null) {
        return parseUnion(anon.unionDeclaration()).unionDataSchema;
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
    setAnnotations(context, schema);
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

    Map<String, Object> props = setAnnotations(context, schema);

    Map<String, String> symbolDocs = new HashMap<String, String>();
    for (EnumSymbolDeclarationContext symbolDecl : symbolDecls) {
      if (symbolDecl.doc != null) {
        symbolDocs.put(symbolDecl.symbol.value, symbolDecl.doc.value);
      }
    }
    if (symbolDocs.size() > 0) {
      props.put("symbolDocs", new DataMap(symbolDocs));
    }

    DataMap deprecatedSymbols = new DataMap();
    DataMap symbolProperties = new DataMap();
    for (EnumSymbolDeclarationContext symbolDecl: symbolDecls) {
      for (PropDeclarationContext prop: symbolDecl.props) {
        String key = symbolDecl.symbol.value;
        Object value = parsePropValue(prop);
        if (prop.name.equals("deprecated")) {
          deprecatedSymbols.put(key, value);
        } else {
          symbolProperties.put(key, value);
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
    TypeAssignmentContext ref = typeref.ref;

    UnionDataSchemaWithProperties unionWithProps =
      lookupUnionDataSchema(ref.typeDeclaration());
    DataSchema refSchema;
    if (unionWithProps != null) {
      refSchema = unionWithProps.unionDataSchema;
      Map<String, Object> propsToAdd = new HashMap<String, Object>();
      propsToAdd.putAll(schema.getProperties());
      DataMap unionProps = unionWithProps.byNameByMemberProps;
      if (unionProps != null && unionProps.size() > 0) {
        propsToAdd.putAll(unionProps);
      }

      Map<String, String> unionSchemadoc = unionWithProps.schemadocByMember;
      if (unionSchemadoc != null && unionSchemadoc.size() > 0) {
        propsToAdd.put("memberDocs", new DataMap(unionSchemadoc));
      }
      schema.setProperties(propsToAdd);
    } else {
      refSchema = toDataSchema(typeref.ref);
    }
    schema.setReferencedType(refSchema);

    setAnnotations(context, schema);
    return schema;
  }

  private UnionDataSchemaWithProperties lookupUnionDataSchema(
      TypeDeclarationContext typeDecl) throws ParseException {
    if (typeDecl != null) {
      AnonymousTypeDeclarationContext anonDecl = typeDecl.anonymousTypeDeclaration();
      if (anonDecl != null) {
        UnionDeclarationContext unionDecl = anonDecl.unionDeclaration();
        if (unionDecl != null) {
          return parseUnion(unionDecl);
        }
      }
    }
    return null;
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
      if (!typeName.equals("string")) {
        propsToAdd.put("keys", typeName);
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

  private static class UnionDataSchemaWithProperties {
    public final UnionDataSchema unionDataSchema;
    public final Map<String, String> schemadocByMember;
    public final DataMap byNameByMemberProps;

    public UnionDataSchemaWithProperties(
        UnionDataSchema unionDataSchema,
        Map<String, String> schemadocByMember,
        DataMap byNameByMemberProps) {
      this.unionDataSchema = unionDataSchema;
      this.schemadocByMember = schemadocByMember;
      this.byNameByMemberProps = byNameByMemberProps;
    }
  }

  private UnionDataSchemaWithProperties parseUnion(UnionDeclarationContext union) throws ParseException {
    Map<String, String> schemadocByMember = new HashMap<String, String>();
    DataMap byNameByMemberProps = new DataMap();

    UnionDataSchema schema = new UnionDataSchema();
    List<UnionMemberDeclarationContext> members = union.typeParams.members;
    List<DataSchema> types = new ArrayList<DataSchema>(members.size());
    for (UnionMemberDeclarationContext memberDecl: members) {
      TypeAssignmentContext memberType = memberDecl.member;
      DataSchema dataSchema = toDataSchema(memberType);
      if (dataSchema != null) {
        types.add(dataSchema);
        String memberKey = dataSchema.getUnionMemberKey();
        if (memberDecl.schemadoc() != null) {
          String schemadoc = memberDecl.schemadoc().value;
          schemadocByMember.put(memberKey, schemadoc);
        }
        if (memberDecl.props != null) {
          //Map<String, Object> properties = new HashMap<String, Object>();
          for (PropDeclarationContext prop : memberDecl.props) {
            String[] path = prop.name.split("\\.");
            String[] pathWithMemberKey = Arrays.copyOf(path, path.length + 1);
            pathWithMemberKey[path.length] = memberKey;
            addToDataMap(prop, byNameByMemberProps, pathWithMemberKey, parsePropValue(prop));
            /*DataMap byMemberProps;
            if (!byNameByMemberProps.containsKey(prop.name)) {
              byMemberProps = new DataMap();
              byNameByMemberProps.put(prop.name, byMemberProps);
            } else {
              byMemberProps = byNameByMemberProps.getDataMap(prop.name);
            }

            byMemberProps.put(
              memberKey,
              parseJsonValue(prop.propJsonValue().jsonValue()));*/
          }
        }
      }
    }
    schema.setTypes(types, errorMessageBuilder());
    return new UnionDataSchemaWithProperties(schema, schemadocByMember, byNameByMemberProps);
  }

  private RecordDataSchema parseRecord(
      NamedTypeDeclarationContext context,
      RecordDeclarationContext record) throws ParseException {
    Name name = toName(record.name);
    RecordDataSchema schema = new RecordDataSchema(name, RecordDataSchema.RecordType.RECORD);

    bindNameToSchema(name, schema);
    schema.setFields(parseFields(schema, record.recordDecl), errorMessageBuilder());
    setAnnotations(context, schema);
    topLevelSchemas.add(schema);
    return schema;
  }

  private Map<String, Object> setAnnotations(
      NamedTypeDeclarationContext source, NamedDataSchema target) throws ParseException {

    Map<String, Object> properties = new HashMap<String, Object>();
    properties.putAll(target.getProperties());

    if (source.doc != null) {
      target.setDoc(source.doc.value);
    }

    for (PropDeclarationContext prop: source.props) {
      addToProperties(properties, prop);
      /*properties.put(
        prop.name,
        parseJsonValue(prop.propJsonValue().jsonValue()));*/
    }

    target.setProperties(properties);
    return properties;
  }

  private void addToProperties(Map<String, Object> properties, PropDeclarationContext prop) throws ParseException{
    String[] path = prop.name.split("\\.");
    addToDataMap(prop, properties, path, parsePropValue(prop));
  }

  private void addToDataMap(
      ParserRuleContext context,
      Map<String, Object> properties,
      String[] path,
      Object value) throws ParseException {
    Map<String, Object> current = properties;
    for (int i = 0; i < path.length - 1; i++) {
      String pathPart = path[i];
      if (properties.containsKey(pathPart)) {
        Object val = properties.get(pathPart);
        if (!(val instanceof DataMap)) {
          throw new ParseException(
            new ParseError(
              new ParseErrorLocation(context),
              "Conflicting property: " + Arrays.toString(path)));
        }
        current = (DataMap)val;
      } else {
        DataMap next = new DataMap();
        current.put(pathPart, next);
        current = next;
      }
    }
    String terminalPart = path[path.length-1];
    if (current.containsKey(terminalPart)) {
      throw new ParseException(
        new ParseError(
          new ParseErrorLocation(context),
          "Property already defined: " + Arrays.toString(path)));
    } else {
      current.put(terminalPart, value);
    }
  }

  private List<RecordDataSchema.Field> parseFields(
      RecordDataSchema recordSchema,
      FieldSelectionContext fieldGroup) throws ParseException {

    List<RecordDataSchema.Field> results = new ArrayList<RecordDataSchema.Field>();
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
          } else if (fieldDefault.isNil) {
            properties.put("defaultNone", true);
          }
        }

        for (PropDeclarationContext prop : field.props) {
          addToProperties(properties, prop);
          /*properties.put(
            prop.name,
            parseJsonValue(prop.propJsonValue().jsonValue()));*/
        }
        if (field.doc != null) {
          result.setDoc(field.doc.value);
        }
        result.setProperties(properties);
        result.setRecord(recordSchema);
        results.add(result);
      } else if (element.fieldInclude() != null) {
        throw new UnsupportedOperationException();
      } else {
        startErrorMessage(element)
          .append("Unrecognized field element parse node: ")
          .append(element.getText()).append("\n");
      }
    }
    return results;
  }

  private DataSchema toDataSchema(TypeAssignmentContext typeAssignment) throws ParseException {
    TypeReferenceContext typeReference = typeAssignment.typeReference();
    if (typeReference != null) {
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
    }  else if (typeAssignment.typeDeclaration() != null) {
      return parseType(typeAssignment.typeDeclaration());
    } else {
      throw new ParseException(typeAssignment,
        "Unrecognized type assignment parse node: " + typeAssignment.getText() + "\n");
    }
  }

  private Name toName(TypeNameDeclarationContext name) {
    return new Name(name.value, getCurrentNamespace(), errorMessageBuilder());
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
      return jsonValue.number().value;
    } else if (jsonValue.bool() != null) {
      return jsonValue.bool().value;
    } else if (jsonValue.nullValue() != null) {
      return null;
    } else {
      throw new ParseException(jsonValue,
        "Unrecognized JSON parse node: " + jsonValue.getText());
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
