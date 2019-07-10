package org.coursera.courier;

import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.codec.DataLocation;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.coursera.courier.grammar.CourierLexer;
import org.coursera.courier.grammar.CourierParser;
import org.coursera.courier.grammar.CourierParser.*;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class CourierDocumentToPdscConverter {

    public static List<DataMap> convert(Reader reader) throws IOException {
        CourierDocumentToPdscConverter converter = new CourierDocumentToPdscConverter();
        converter.parseInput(reader);
        return converter.pdscs;
    }

    String namespace;
    List<DataMap> pdscs;
    Map<String, String> importsByName;

    private CourierDocumentToPdscConverter() {
        this.pdscs = new ArrayList<>();
        this.importsByName = new HashMap<>();
    }

    private void parseInput(Reader reader) throws IOException {
        CourierLexer lexer;
        lexer = new CourierLexer(new ANTLRInputStream(reader));
        CourierParser parser = new CourierParser(new CommonTokenStream(lexer));
        parseDocument(parser.document());
    }

    private void parseDocument(DocumentContext antlrDocument) throws ParseException {
        this.namespace = antlrDocument.namespaceDeclaration().qualifiedIdentifier().value;
        antlrDocument.importDeclarations().importDeclaration().forEach(this::addImport);
        processNamedType(antlrDocument.namedTypeDeclaration());
    }

    private void addImport(ImportDeclarationContext ctx) {
        String _import = ctx.qualifiedIdentifier().value;
        String[] tokens = _import.split("\\.");
        String name = tokens[tokens.length - 1];
        importsByName.put(name, _import);
    }

    private void processNamedType(NamedTypeDeclarationContext namedType) throws ParseException {
        DataMap pdsc;
        if (namedType.recordDeclaration() != null) {
            pdsc = parseRecord(namedType, namedType.recordDeclaration());
//        } else if (namedType.typerefDeclaration() != null) {
//            return parseTyperef(namedType, namedType.typerefDeclaration());
//        } else if (namedType.fixedDeclaration() != null) {
//            return parseFixed(namedType, namedType.fixedDeclaration());
//        } else if (namedType.enumDeclaration() != null) {
//            return parseEnum(namedType, namedType.enumDeclaration());
        } else {
            throw new ParseException(namedType,
                    "Unrecognized named type parse node: " + namedType.getText());
        }
        pdscs.add(pdsc);
    }

    private DataMap parseRecord(
            NamedTypeDeclarationContext namedType,
                             RecordDeclarationContext record) {
        DataMap data = initNamedType(namedType);
        data.put("type", "record");
        data.put("name", record.name);
        DataList fields = new DataList(
                record.fieldSelection().fields.stream().map(this::parseField)
                    .collect(Collectors.toList()));
        data.put("fields", fields);
        return data;
    }

    private DataMap initNamedType(NamedTypeDeclarationContext namedType) {
        DataMap data = new DataMap();
        data.put("namespace", this.namespace);
        return data;
    }

    private DataMap parseField(FieldSelectionElementContext fieldElemContext) {
        DataMap data = new DataMap();
        FieldDeclarationContext fieldContext = fieldElemContext.fieldDeclaration();
        if (fieldContext.doc != null) {
            data.put("doc", fieldContext.doc.value);
        }
        data.put("name", fieldContext.name);
        data.put("type", toFullyQualifiedName(fieldContext.type.typeReference().value));
        return data;
    }

    private String toFullyQualifiedName(String name) {
        return importsByName.getOrDefault(name, this.namespace + "." + name);
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

}