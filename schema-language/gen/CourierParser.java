// Generated from /Users/marc/base/coursera/courier/schema-language/src/main/antlr4/Courier.g4 by ANTLR 4.5.1

  import org.coursera.courier.grammar.ParseUtils;
  import java.util.Arrays;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CourierParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ARRAY=1, ENUM=2, FIXED=3, IMPORT=4, MAP=5, NAMESPACE=6, RECORD=7, TYPEREF=8, 
		UNION=9, OPEN_PAREN=10, CLOSE_PAREN=11, OPEN_BRACE=12, CLOSE_BRACE=13, 
		OPEN_BRACKET=14, CLOSE_BRACKET=15, AT=16, COLON=17, DOT=18, DOTDOTDOT=19, 
		EQ=20, QUESTION_MARK=21, BOOLEAN_LITERAL=22, NULL_LITERAL=23, SCHEMADOC_COMMENT=24, 
		BLOCK_COMMENT=25, LINE_COMMENT=26, NUMBER_LITERAL=27, STRING_LITERAL=28, 
		ID=29, WS=30;
	public static final int
		RULE_document = 0, RULE_namespaceDeclaration = 1, RULE_importDeclarations = 2, 
		RULE_importDeclaration = 3, RULE_typeReference = 4, RULE_typeDeclaration = 5, 
		RULE_namedTypeDeclaration = 6, RULE_anonymousTypeDeclaration = 7, RULE_typeAssignment = 8, 
		RULE_propDeclaration = 9, RULE_propNameDeclaration = 10, RULE_propJsonValue = 11, 
		RULE_recordDeclaration = 12, RULE_enumDeclaration = 13, RULE_enumSymbolDeclarations = 14, 
		RULE_enumSymbolDeclaration = 15, RULE_enumSymbol = 16, RULE_typerefDeclaration = 17, 
		RULE_fixedDeclaration = 18, RULE_unionDeclaration = 19, RULE_unionTypeAssignments = 20, 
		RULE_unionMemberDeclaration = 21, RULE_arrayDeclaration = 22, RULE_arrayTypeAssignments = 23, 
		RULE_mapDeclaration = 24, RULE_mapTypeAssignments = 25, RULE_fieldSelection = 26, 
		RULE_fieldSelectionElement = 27, RULE_fieldInclude = 28, RULE_fieldDeclaration = 29, 
		RULE_fieldDefault = 30, RULE_qualifiedIdentifier = 31, RULE_identifier = 32, 
		RULE_schemadoc = 33, RULE_object = 34, RULE_objectEntry = 35, RULE_array = 36, 
		RULE_jsonValue = 37, RULE_string = 38, RULE_number = 39, RULE_bool = 40, 
		RULE_nullValue = 41;
	public static final String[] ruleNames = {
		"document", "namespaceDeclaration", "importDeclarations", "importDeclaration", 
		"typeReference", "typeDeclaration", "namedTypeDeclaration", "anonymousTypeDeclaration", 
		"typeAssignment", "propDeclaration", "propNameDeclaration", "propJsonValue", 
		"recordDeclaration", "enumDeclaration", "enumSymbolDeclarations", "enumSymbolDeclaration", 
		"enumSymbol", "typerefDeclaration", "fixedDeclaration", "unionDeclaration", 
		"unionTypeAssignments", "unionMemberDeclaration", "arrayDeclaration", 
		"arrayTypeAssignments", "mapDeclaration", "mapTypeAssignments", "fieldSelection", 
		"fieldSelectionElement", "fieldInclude", "fieldDeclaration", "fieldDefault", 
		"qualifiedIdentifier", "identifier", "schemadoc", "object", "objectEntry", 
		"array", "jsonValue", "string", "number", "bool", "nullValue"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'array'", "'enum'", "'fixed'", "'import'", "'map'", "'namespace'", 
		"'record'", "'typeref'", "'union'", "'('", "')'", "'{'", "'}'", "'['", 
		"']'", "'@'", "':'", "'.'", "'...'", "'='", "'?'", null, "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "ARRAY", "ENUM", "FIXED", "IMPORT", "MAP", "NAMESPACE", "RECORD", 
		"TYPEREF", "UNION", "OPEN_PAREN", "CLOSE_PAREN", "OPEN_BRACE", "CLOSE_BRACE", 
		"OPEN_BRACKET", "CLOSE_BRACKET", "AT", "COLON", "DOT", "DOTDOTDOT", "EQ", 
		"QUESTION_MARK", "BOOLEAN_LITERAL", "NULL_LITERAL", "SCHEMADOC_COMMENT", 
		"BLOCK_COMMENT", "LINE_COMMENT", "NUMBER_LITERAL", "STRING_LITERAL", "ID", 
		"WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Courier.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CourierParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class DocumentContext extends ParserRuleContext {
		public ImportDeclarationsContext importDeclarations() {
			return getRuleContext(ImportDeclarationsContext.class,0);
		}
		public NamedTypeDeclarationContext namedTypeDeclaration() {
			return getRuleContext(NamedTypeDeclarationContext.class,0);
		}
		public NamespaceDeclarationContext namespaceDeclaration() {
			return getRuleContext(NamespaceDeclarationContext.class,0);
		}
		public DocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_document; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitDocument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitDocument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentContext document() throws RecognitionException {
		DocumentContext _localctx = new DocumentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_document);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			_la = _input.LA(1);
			if (_la==NAMESPACE) {
				{
				setState(84);
				namespaceDeclaration();
				}
			}

			setState(87);
			importDeclarations();
			setState(88);
			namedTypeDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NamespaceDeclarationContext extends ParserRuleContext {
		public TerminalNode NAMESPACE() { return getToken(CourierParser.NAMESPACE, 0); }
		public QualifiedIdentifierContext qualifiedIdentifier() {
			return getRuleContext(QualifiedIdentifierContext.class,0);
		}
		public NamespaceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterNamespaceDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitNamespaceDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitNamespaceDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDeclarationContext namespaceDeclaration() throws RecognitionException {
		NamespaceDeclarationContext _localctx = new NamespaceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_namespaceDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(NAMESPACE);
			setState(91);
			qualifiedIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportDeclarationsContext extends ParserRuleContext {
		public List<ImportDeclarationContext> importDeclaration() {
			return getRuleContexts(ImportDeclarationContext.class);
		}
		public ImportDeclarationContext importDeclaration(int i) {
			return getRuleContext(ImportDeclarationContext.class,i);
		}
		public ImportDeclarationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclarations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterImportDeclarations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitImportDeclarations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitImportDeclarations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclarationsContext importDeclarations() throws RecognitionException {
		ImportDeclarationsContext _localctx = new ImportDeclarationsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_importDeclarations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(93);
				importDeclaration();
				}
				}
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportDeclarationContext extends ParserRuleContext {
		public QualifiedIdentifierContext type;
		public TerminalNode IMPORT() { return getToken(CourierParser.IMPORT, 0); }
		public QualifiedIdentifierContext qualifiedIdentifier() {
			return getRuleContext(QualifiedIdentifierContext.class,0);
		}
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterImportDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitImportDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitImportDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_importDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			match(IMPORT);
			setState(100);
			((ImportDeclarationContext)_localctx).type = qualifiedIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeReferenceContext extends ParserRuleContext {
		public String value;
		public QualifiedIdentifierContext qualifiedIdentifier;
		public QualifiedIdentifierContext qualifiedIdentifier() {
			return getRuleContext(QualifiedIdentifierContext.class,0);
		}
		public TypeReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterTypeReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitTypeReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitTypeReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeReferenceContext typeReference() throws RecognitionException {
		TypeReferenceContext _localctx = new TypeReferenceContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_typeReference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			((TypeReferenceContext)_localctx).qualifiedIdentifier = qualifiedIdentifier();

			  ((TypeReferenceContext)_localctx).value =  ((TypeReferenceContext)_localctx).qualifiedIdentifier.value;

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeDeclarationContext extends ParserRuleContext {
		public NamedTypeDeclarationContext namedTypeDeclaration() {
			return getRuleContext(NamedTypeDeclarationContext.class,0);
		}
		public AnonymousTypeDeclarationContext anonymousTypeDeclaration() {
			return getRuleContext(AnonymousTypeDeclarationContext.class,0);
		}
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_typeDeclaration);
		try {
			setState(107);
			switch (_input.LA(1)) {
			case ENUM:
			case FIXED:
			case RECORD:
			case TYPEREF:
			case AT:
			case SCHEMADOC_COMMENT:
				enterOuterAlt(_localctx, 1);
				{
				setState(105);
				namedTypeDeclaration();
				}
				break;
			case ARRAY:
			case MAP:
			case UNION:
				enterOuterAlt(_localctx, 2);
				{
				setState(106);
				anonymousTypeDeclaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NamedTypeDeclarationContext extends ParserRuleContext {
		public SchemadocContext doc;
		public PropDeclarationContext propDeclaration;
		public List<PropDeclarationContext> props = new ArrayList<PropDeclarationContext>();
		public RecordDeclarationContext recordDeclaration() {
			return getRuleContext(RecordDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public TyperefDeclarationContext typerefDeclaration() {
			return getRuleContext(TyperefDeclarationContext.class,0);
		}
		public FixedDeclarationContext fixedDeclaration() {
			return getRuleContext(FixedDeclarationContext.class,0);
		}
		public SchemadocContext schemadoc() {
			return getRuleContext(SchemadocContext.class,0);
		}
		public List<PropDeclarationContext> propDeclaration() {
			return getRuleContexts(PropDeclarationContext.class);
		}
		public PropDeclarationContext propDeclaration(int i) {
			return getRuleContext(PropDeclarationContext.class,i);
		}
		public NamedTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterNamedTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitNamedTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitNamedTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedTypeDeclarationContext namedTypeDeclaration() throws RecognitionException {
		NamedTypeDeclarationContext _localctx = new NamedTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_namedTypeDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			_la = _input.LA(1);
			if (_la==SCHEMADOC_COMMENT) {
				{
				setState(109);
				((NamedTypeDeclarationContext)_localctx).doc = schemadoc();
				}
			}

			setState(115);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(112);
				((NamedTypeDeclarationContext)_localctx).propDeclaration = propDeclaration();
				((NamedTypeDeclarationContext)_localctx).props.add(((NamedTypeDeclarationContext)_localctx).propDeclaration);
				}
				}
				setState(117);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(122);
			switch (_input.LA(1)) {
			case RECORD:
				{
				setState(118);
				recordDeclaration();
				}
				break;
			case ENUM:
				{
				setState(119);
				enumDeclaration();
				}
				break;
			case TYPEREF:
				{
				setState(120);
				typerefDeclaration();
				}
				break;
			case FIXED:
				{
				setState(121);
				fixedDeclaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnonymousTypeDeclarationContext extends ParserRuleContext {
		public UnionDeclarationContext unionDeclaration() {
			return getRuleContext(UnionDeclarationContext.class,0);
		}
		public ArrayDeclarationContext arrayDeclaration() {
			return getRuleContext(ArrayDeclarationContext.class,0);
		}
		public MapDeclarationContext mapDeclaration() {
			return getRuleContext(MapDeclarationContext.class,0);
		}
		public AnonymousTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anonymousTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterAnonymousTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitAnonymousTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitAnonymousTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnonymousTypeDeclarationContext anonymousTypeDeclaration() throws RecognitionException {
		AnonymousTypeDeclarationContext _localctx = new AnonymousTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_anonymousTypeDeclaration);
		try {
			setState(127);
			switch (_input.LA(1)) {
			case UNION:
				enterOuterAlt(_localctx, 1);
				{
				setState(124);
				unionDeclaration();
				}
				break;
			case ARRAY:
				enterOuterAlt(_localctx, 2);
				{
				setState(125);
				arrayDeclaration();
				}
				break;
			case MAP:
				enterOuterAlt(_localctx, 3);
				{
				setState(126);
				mapDeclaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeAssignmentContext extends ParserRuleContext {
		public TypeReferenceContext typeReference() {
			return getRuleContext(TypeReferenceContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public TypeAssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeAssignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterTypeAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitTypeAssignment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitTypeAssignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeAssignmentContext typeAssignment() throws RecognitionException {
		TypeAssignmentContext _localctx = new TypeAssignmentContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_typeAssignment);
		try {
			setState(131);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(129);
				typeReference();
				}
				break;
			case ARRAY:
			case ENUM:
			case FIXED:
			case MAP:
			case RECORD:
			case TYPEREF:
			case UNION:
			case AT:
			case SCHEMADOC_COMMENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(130);
				typeDeclaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropDeclarationContext extends ParserRuleContext {
		public String name;
		public List<String> path;
		public PropNameDeclarationContext propNameDeclaration;
		public PropNameDeclarationContext propNameDeclaration() {
			return getRuleContext(PropNameDeclarationContext.class,0);
		}
		public PropJsonValueContext propJsonValue() {
			return getRuleContext(PropJsonValueContext.class,0);
		}
		public PropDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterPropDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitPropDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitPropDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropDeclarationContext propDeclaration() throws RecognitionException {
		PropDeclarationContext _localctx = new PropDeclarationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_propDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			((PropDeclarationContext)_localctx).propNameDeclaration = propNameDeclaration();
			setState(135);
			_la = _input.LA(1);
			if (_la==OPEN_PAREN || _la==EQ) {
				{
				setState(134);
				propJsonValue();
				}
			}


			  ((PropDeclarationContext)_localctx).name =  ((PropDeclarationContext)_localctx).propNameDeclaration.name;
			  ((PropDeclarationContext)_localctx).path =  Arrays.asList(((PropDeclarationContext)_localctx).propNameDeclaration.name.split("\\."));

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropNameDeclarationContext extends ParserRuleContext {
		public String name;
		public QualifiedIdentifierContext qualifiedIdentifier;
		public TerminalNode AT() { return getToken(CourierParser.AT, 0); }
		public QualifiedIdentifierContext qualifiedIdentifier() {
			return getRuleContext(QualifiedIdentifierContext.class,0);
		}
		public PropNameDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propNameDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterPropNameDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitPropNameDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitPropNameDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropNameDeclarationContext propNameDeclaration() throws RecognitionException {
		PropNameDeclarationContext _localctx = new PropNameDeclarationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_propNameDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(AT);
			setState(140);
			((PropNameDeclarationContext)_localctx).qualifiedIdentifier = qualifiedIdentifier();

			  ((PropNameDeclarationContext)_localctx).name =  ((PropNameDeclarationContext)_localctx).qualifiedIdentifier.value;

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropJsonValueContext extends ParserRuleContext {
		public TerminalNode OPEN_PAREN() { return getToken(CourierParser.OPEN_PAREN, 0); }
		public JsonValueContext jsonValue() {
			return getRuleContext(JsonValueContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(CourierParser.CLOSE_PAREN, 0); }
		public TerminalNode EQ() { return getToken(CourierParser.EQ, 0); }
		public PropJsonValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propJsonValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterPropJsonValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitPropJsonValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitPropJsonValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropJsonValueContext propJsonValue() throws RecognitionException {
		PropJsonValueContext _localctx = new PropJsonValueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_propJsonValue);
		try {
			setState(149);
			switch (_input.LA(1)) {
			case OPEN_PAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(143);
				match(OPEN_PAREN);
				setState(144);
				jsonValue();
				setState(145);
				match(CLOSE_PAREN);
				}
				break;
			case EQ:
				enterOuterAlt(_localctx, 2);
				{
				setState(147);
				match(EQ);
				setState(148);
				jsonValue();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RecordDeclarationContext extends ParserRuleContext {
		public String name;
		public IdentifierContext identifier;
		public FieldSelectionContext recordDecl;
		public TerminalNode RECORD() { return getToken(CourierParser.RECORD, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public FieldSelectionContext fieldSelection() {
			return getRuleContext(FieldSelectionContext.class,0);
		}
		public RecordDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recordDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterRecordDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitRecordDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitRecordDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RecordDeclarationContext recordDeclaration() throws RecognitionException {
		RecordDeclarationContext _localctx = new RecordDeclarationContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_recordDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			match(RECORD);
			setState(152);
			((RecordDeclarationContext)_localctx).identifier = identifier();
			setState(153);
			((RecordDeclarationContext)_localctx).recordDecl = fieldSelection();

			  ((RecordDeclarationContext)_localctx).name =  ((RecordDeclarationContext)_localctx).identifier.value;

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumDeclarationContext extends ParserRuleContext {
		public String name;
		public IdentifierContext identifier;
		public EnumSymbolDeclarationsContext enumDecl;
		public TerminalNode ENUM() { return getToken(CourierParser.ENUM, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public EnumSymbolDeclarationsContext enumSymbolDeclarations() {
			return getRuleContext(EnumSymbolDeclarationsContext.class,0);
		}
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterEnumDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitEnumDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitEnumDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_enumDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			match(ENUM);
			setState(157);
			((EnumDeclarationContext)_localctx).identifier = identifier();
			setState(158);
			((EnumDeclarationContext)_localctx).enumDecl = enumSymbolDeclarations();

			  ((EnumDeclarationContext)_localctx).name =  ((EnumDeclarationContext)_localctx).identifier.value;

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumSymbolDeclarationsContext extends ParserRuleContext {
		public EnumSymbolDeclarationContext enumSymbolDeclaration;
		public List<EnumSymbolDeclarationContext> symbolDecls = new ArrayList<EnumSymbolDeclarationContext>();
		public TerminalNode OPEN_BRACE() { return getToken(CourierParser.OPEN_BRACE, 0); }
		public TerminalNode CLOSE_BRACE() { return getToken(CourierParser.CLOSE_BRACE, 0); }
		public List<EnumSymbolDeclarationContext> enumSymbolDeclaration() {
			return getRuleContexts(EnumSymbolDeclarationContext.class);
		}
		public EnumSymbolDeclarationContext enumSymbolDeclaration(int i) {
			return getRuleContext(EnumSymbolDeclarationContext.class,i);
		}
		public EnumSymbolDeclarationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumSymbolDeclarations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterEnumSymbolDeclarations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitEnumSymbolDeclarations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitEnumSymbolDeclarations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumSymbolDeclarationsContext enumSymbolDeclarations() throws RecognitionException {
		EnumSymbolDeclarationsContext _localctx = new EnumSymbolDeclarationsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_enumSymbolDeclarations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			match(OPEN_BRACE);
			setState(165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << AT) | (1L << SCHEMADOC_COMMENT) | (1L << ID))) != 0)) {
				{
				{
				setState(162);
				((EnumSymbolDeclarationsContext)_localctx).enumSymbolDeclaration = enumSymbolDeclaration();
				((EnumSymbolDeclarationsContext)_localctx).symbolDecls.add(((EnumSymbolDeclarationsContext)_localctx).enumSymbolDeclaration);
				}
				}
				setState(167);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(168);
			match(CLOSE_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumSymbolDeclarationContext extends ParserRuleContext {
		public SchemadocContext doc;
		public PropDeclarationContext propDeclaration;
		public List<PropDeclarationContext> props = new ArrayList<PropDeclarationContext>();
		public EnumSymbolContext symbol;
		public EnumSymbolContext enumSymbol() {
			return getRuleContext(EnumSymbolContext.class,0);
		}
		public SchemadocContext schemadoc() {
			return getRuleContext(SchemadocContext.class,0);
		}
		public List<PropDeclarationContext> propDeclaration() {
			return getRuleContexts(PropDeclarationContext.class);
		}
		public PropDeclarationContext propDeclaration(int i) {
			return getRuleContext(PropDeclarationContext.class,i);
		}
		public EnumSymbolDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumSymbolDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterEnumSymbolDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitEnumSymbolDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitEnumSymbolDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumSymbolDeclarationContext enumSymbolDeclaration() throws RecognitionException {
		EnumSymbolDeclarationContext _localctx = new EnumSymbolDeclarationContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_enumSymbolDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			_la = _input.LA(1);
			if (_la==SCHEMADOC_COMMENT) {
				{
				setState(170);
				((EnumSymbolDeclarationContext)_localctx).doc = schemadoc();
				}
			}

			setState(176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(173);
				((EnumSymbolDeclarationContext)_localctx).propDeclaration = propDeclaration();
				((EnumSymbolDeclarationContext)_localctx).props.add(((EnumSymbolDeclarationContext)_localctx).propDeclaration);
				}
				}
				setState(178);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(179);
			((EnumSymbolDeclarationContext)_localctx).symbol = enumSymbol();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumSymbolContext extends ParserRuleContext {
		public String value;
		public IdentifierContext identifier;
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public EnumSymbolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumSymbol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterEnumSymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitEnumSymbol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitEnumSymbol(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumSymbolContext enumSymbol() throws RecognitionException {
		EnumSymbolContext _localctx = new EnumSymbolContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_enumSymbol);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			((EnumSymbolContext)_localctx).identifier = identifier();

			  ((EnumSymbolContext)_localctx).value =  ((EnumSymbolContext)_localctx).identifier.value;

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TyperefDeclarationContext extends ParserRuleContext {
		public String name;
		public IdentifierContext identifier;
		public TypeAssignmentContext ref;
		public TerminalNode TYPEREF() { return getToken(CourierParser.TYPEREF, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode EQ() { return getToken(CourierParser.EQ, 0); }
		public TypeAssignmentContext typeAssignment() {
			return getRuleContext(TypeAssignmentContext.class,0);
		}
		public TyperefDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typerefDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterTyperefDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitTyperefDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitTyperefDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TyperefDeclarationContext typerefDeclaration() throws RecognitionException {
		TyperefDeclarationContext _localctx = new TyperefDeclarationContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_typerefDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184);
			match(TYPEREF);
			setState(185);
			((TyperefDeclarationContext)_localctx).identifier = identifier();
			setState(186);
			match(EQ);
			setState(187);
			((TyperefDeclarationContext)_localctx).ref = typeAssignment();

			  ((TyperefDeclarationContext)_localctx).name =  ((TyperefDeclarationContext)_localctx).identifier.value;

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FixedDeclarationContext extends ParserRuleContext {
		public String name;
		public int size;
		public IdentifierContext identifier;
		public Token sizeStr;
		public TerminalNode FIXED() { return getToken(CourierParser.FIXED, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode NUMBER_LITERAL() { return getToken(CourierParser.NUMBER_LITERAL, 0); }
		public FixedDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fixedDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterFixedDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitFixedDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitFixedDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FixedDeclarationContext fixedDeclaration() throws RecognitionException {
		FixedDeclarationContext _localctx = new FixedDeclarationContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_fixedDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(FIXED);
			setState(191);
			((FixedDeclarationContext)_localctx).identifier = identifier();
			setState(192);
			((FixedDeclarationContext)_localctx).sizeStr = match(NUMBER_LITERAL);

			  ((FixedDeclarationContext)_localctx).name =  ((FixedDeclarationContext)_localctx).identifier.value;
			  ((FixedDeclarationContext)_localctx).size =  (((FixedDeclarationContext)_localctx).sizeStr!=null?Integer.valueOf(((FixedDeclarationContext)_localctx).sizeStr.getText()):0);

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnionDeclarationContext extends ParserRuleContext {
		public UnionTypeAssignmentsContext typeParams;
		public TerminalNode UNION() { return getToken(CourierParser.UNION, 0); }
		public UnionTypeAssignmentsContext unionTypeAssignments() {
			return getRuleContext(UnionTypeAssignmentsContext.class,0);
		}
		public UnionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterUnionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitUnionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitUnionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionDeclarationContext unionDeclaration() throws RecognitionException {
		UnionDeclarationContext _localctx = new UnionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_unionDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(UNION);
			setState(196);
			((UnionDeclarationContext)_localctx).typeParams = unionTypeAssignments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnionTypeAssignmentsContext extends ParserRuleContext {
		public UnionMemberDeclarationContext unionMemberDeclaration;
		public List<UnionMemberDeclarationContext> members = new ArrayList<UnionMemberDeclarationContext>();
		public TerminalNode OPEN_BRACKET() { return getToken(CourierParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(CourierParser.CLOSE_BRACKET, 0); }
		public List<UnionMemberDeclarationContext> unionMemberDeclaration() {
			return getRuleContexts(UnionMemberDeclarationContext.class);
		}
		public UnionMemberDeclarationContext unionMemberDeclaration(int i) {
			return getRuleContext(UnionMemberDeclarationContext.class,i);
		}
		public UnionTypeAssignmentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionTypeAssignments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterUnionTypeAssignments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitUnionTypeAssignments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitUnionTypeAssignments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionTypeAssignmentsContext unionTypeAssignments() throws RecognitionException {
		UnionTypeAssignmentsContext _localctx = new UnionTypeAssignmentsContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_unionTypeAssignments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			match(OPEN_BRACKET);
			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ARRAY) | (1L << ENUM) | (1L << FIXED) | (1L << MAP) | (1L << RECORD) | (1L << TYPEREF) | (1L << UNION) | (1L << AT) | (1L << SCHEMADOC_COMMENT) | (1L << ID))) != 0)) {
				{
				{
				setState(199);
				((UnionTypeAssignmentsContext)_localctx).unionMemberDeclaration = unionMemberDeclaration();
				((UnionTypeAssignmentsContext)_localctx).members.add(((UnionTypeAssignmentsContext)_localctx).unionMemberDeclaration);
				}
				}
				setState(204);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(205);
			match(CLOSE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnionMemberDeclarationContext extends ParserRuleContext {
		public TypeAssignmentContext member;
		public TypeAssignmentContext typeAssignment() {
			return getRuleContext(TypeAssignmentContext.class,0);
		}
		public UnionMemberDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionMemberDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterUnionMemberDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitUnionMemberDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitUnionMemberDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionMemberDeclarationContext unionMemberDeclaration() throws RecognitionException {
		UnionMemberDeclarationContext _localctx = new UnionMemberDeclarationContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_unionMemberDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			((UnionMemberDeclarationContext)_localctx).member = typeAssignment();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayDeclarationContext extends ParserRuleContext {
		public ArrayTypeAssignmentsContext typeParams;
		public TerminalNode ARRAY() { return getToken(CourierParser.ARRAY, 0); }
		public ArrayTypeAssignmentsContext arrayTypeAssignments() {
			return getRuleContext(ArrayTypeAssignmentsContext.class,0);
		}
		public ArrayDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterArrayDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitArrayDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitArrayDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayDeclarationContext arrayDeclaration() throws RecognitionException {
		ArrayDeclarationContext _localctx = new ArrayDeclarationContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_arrayDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			match(ARRAY);
			setState(210);
			((ArrayDeclarationContext)_localctx).typeParams = arrayTypeAssignments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayTypeAssignmentsContext extends ParserRuleContext {
		public TypeAssignmentContext items;
		public TerminalNode OPEN_BRACKET() { return getToken(CourierParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(CourierParser.CLOSE_BRACKET, 0); }
		public TypeAssignmentContext typeAssignment() {
			return getRuleContext(TypeAssignmentContext.class,0);
		}
		public ArrayTypeAssignmentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayTypeAssignments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterArrayTypeAssignments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitArrayTypeAssignments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitArrayTypeAssignments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTypeAssignmentsContext arrayTypeAssignments() throws RecognitionException {
		ArrayTypeAssignmentsContext _localctx = new ArrayTypeAssignmentsContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_arrayTypeAssignments);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			match(OPEN_BRACKET);
			setState(213);
			((ArrayTypeAssignmentsContext)_localctx).items = typeAssignment();
			setState(214);
			match(CLOSE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MapDeclarationContext extends ParserRuleContext {
		public MapTypeAssignmentsContext typeParams;
		public TerminalNode MAP() { return getToken(CourierParser.MAP, 0); }
		public MapTypeAssignmentsContext mapTypeAssignments() {
			return getRuleContext(MapTypeAssignmentsContext.class,0);
		}
		public MapDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterMapDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitMapDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitMapDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapDeclarationContext mapDeclaration() throws RecognitionException {
		MapDeclarationContext _localctx = new MapDeclarationContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_mapDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			match(MAP);
			setState(217);
			((MapDeclarationContext)_localctx).typeParams = mapTypeAssignments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MapTypeAssignmentsContext extends ParserRuleContext {
		public TypeAssignmentContext key;
		public TypeAssignmentContext value;
		public TerminalNode OPEN_BRACKET() { return getToken(CourierParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(CourierParser.CLOSE_BRACKET, 0); }
		public List<TypeAssignmentContext> typeAssignment() {
			return getRuleContexts(TypeAssignmentContext.class);
		}
		public TypeAssignmentContext typeAssignment(int i) {
			return getRuleContext(TypeAssignmentContext.class,i);
		}
		public MapTypeAssignmentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapTypeAssignments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterMapTypeAssignments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitMapTypeAssignments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitMapTypeAssignments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTypeAssignmentsContext mapTypeAssignments() throws RecognitionException {
		MapTypeAssignmentsContext _localctx = new MapTypeAssignmentsContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_mapTypeAssignments);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			match(OPEN_BRACKET);
			setState(220);
			((MapTypeAssignmentsContext)_localctx).key = typeAssignment();
			setState(221);
			((MapTypeAssignmentsContext)_localctx).value = typeAssignment();
			setState(222);
			match(CLOSE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldSelectionContext extends ParserRuleContext {
		public FieldSelectionElementContext fieldSelectionElement;
		public List<FieldSelectionElementContext> fields = new ArrayList<FieldSelectionElementContext>();
		public TerminalNode OPEN_BRACE() { return getToken(CourierParser.OPEN_BRACE, 0); }
		public TerminalNode CLOSE_BRACE() { return getToken(CourierParser.CLOSE_BRACE, 0); }
		public List<FieldSelectionElementContext> fieldSelectionElement() {
			return getRuleContexts(FieldSelectionElementContext.class);
		}
		public FieldSelectionElementContext fieldSelectionElement(int i) {
			return getRuleContext(FieldSelectionElementContext.class,i);
		}
		public FieldSelectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldSelection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterFieldSelection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitFieldSelection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitFieldSelection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldSelectionContext fieldSelection() throws RecognitionException {
		FieldSelectionContext _localctx = new FieldSelectionContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_fieldSelection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			match(OPEN_BRACE);
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << AT) | (1L << DOTDOTDOT) | (1L << SCHEMADOC_COMMENT) | (1L << ID))) != 0)) {
				{
				{
				setState(225);
				((FieldSelectionContext)_localctx).fieldSelectionElement = fieldSelectionElement();
				((FieldSelectionContext)_localctx).fields.add(((FieldSelectionContext)_localctx).fieldSelectionElement);
				}
				}
				setState(230);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(231);
			match(CLOSE_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldSelectionElementContext extends ParserRuleContext {
		public FieldIncludeContext fieldInclude() {
			return getRuleContext(FieldIncludeContext.class,0);
		}
		public FieldDeclarationContext fieldDeclaration() {
			return getRuleContext(FieldDeclarationContext.class,0);
		}
		public FieldSelectionElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldSelectionElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterFieldSelectionElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitFieldSelectionElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitFieldSelectionElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldSelectionElementContext fieldSelectionElement() throws RecognitionException {
		FieldSelectionElementContext _localctx = new FieldSelectionElementContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_fieldSelectionElement);
		try {
			setState(235);
			switch (_input.LA(1)) {
			case DOTDOTDOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(233);
				fieldInclude();
				}
				break;
			case AT:
			case SCHEMADOC_COMMENT:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(234);
				fieldDeclaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldIncludeContext extends ParserRuleContext {
		public TerminalNode DOTDOTDOT() { return getToken(CourierParser.DOTDOTDOT, 0); }
		public TypeReferenceContext typeReference() {
			return getRuleContext(TypeReferenceContext.class,0);
		}
		public FieldIncludeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldInclude; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterFieldInclude(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitFieldInclude(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitFieldInclude(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldIncludeContext fieldInclude() throws RecognitionException {
		FieldIncludeContext _localctx = new FieldIncludeContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_fieldInclude);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			match(DOTDOTDOT);
			setState(238);
			typeReference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldDeclarationContext extends ParserRuleContext {
		public String name;
		public boolean isOptional;
		public SchemadocContext doc;
		public PropDeclarationContext propDeclaration;
		public List<PropDeclarationContext> props = new ArrayList<PropDeclarationContext>();
		public IdentifierContext fieldName;
		public IdentifierContext identifier;
		public TypeAssignmentContext type;
		public Token QUESTION_MARK;
		public TerminalNode COLON() { return getToken(CourierParser.COLON, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeAssignmentContext typeAssignment() {
			return getRuleContext(TypeAssignmentContext.class,0);
		}
		public TerminalNode QUESTION_MARK() { return getToken(CourierParser.QUESTION_MARK, 0); }
		public FieldDefaultContext fieldDefault() {
			return getRuleContext(FieldDefaultContext.class,0);
		}
		public SchemadocContext schemadoc() {
			return getRuleContext(SchemadocContext.class,0);
		}
		public List<PropDeclarationContext> propDeclaration() {
			return getRuleContexts(PropDeclarationContext.class);
		}
		public PropDeclarationContext propDeclaration(int i) {
			return getRuleContext(PropDeclarationContext.class,i);
		}
		public FieldDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterFieldDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitFieldDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitFieldDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldDeclarationContext fieldDeclaration() throws RecognitionException {
		FieldDeclarationContext _localctx = new FieldDeclarationContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_fieldDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(241);
			_la = _input.LA(1);
			if (_la==SCHEMADOC_COMMENT) {
				{
				setState(240);
				((FieldDeclarationContext)_localctx).doc = schemadoc();
				}
			}

			setState(246);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(243);
				((FieldDeclarationContext)_localctx).propDeclaration = propDeclaration();
				((FieldDeclarationContext)_localctx).props.add(((FieldDeclarationContext)_localctx).propDeclaration);
				}
				}
				setState(248);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(249);
			((FieldDeclarationContext)_localctx).fieldName = ((FieldDeclarationContext)_localctx).identifier = identifier();
			setState(250);
			match(COLON);
			setState(251);
			((FieldDeclarationContext)_localctx).type = typeAssignment();
			setState(253);
			_la = _input.LA(1);
			if (_la==QUESTION_MARK) {
				{
				setState(252);
				((FieldDeclarationContext)_localctx).QUESTION_MARK = match(QUESTION_MARK);
				}
			}

			setState(256);
			_la = _input.LA(1);
			if (_la==EQ) {
				{
				setState(255);
				fieldDefault();
				}
			}


			  ((FieldDeclarationContext)_localctx).name =  ((FieldDeclarationContext)_localctx).identifier.value;
			  ((FieldDeclarationContext)_localctx).isOptional =  ((FieldDeclarationContext)_localctx).QUESTION_MARK() != null;

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldDefaultContext extends ParserRuleContext {
		public TerminalNode EQ() { return getToken(CourierParser.EQ, 0); }
		public JsonValueContext jsonValue() {
			return getRuleContext(JsonValueContext.class,0);
		}
		public FieldDefaultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDefault; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterFieldDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitFieldDefault(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitFieldDefault(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldDefaultContext fieldDefault() throws RecognitionException {
		FieldDefaultContext _localctx = new FieldDefaultContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_fieldDefault);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			match(EQ);
			setState(261);
			jsonValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedIdentifierContext extends ParserRuleContext {
		public String value;
		public List<TerminalNode> ID() { return getTokens(CourierParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(CourierParser.ID, i);
		}
		public List<TerminalNode> DOT() { return getTokens(CourierParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(CourierParser.DOT, i);
		}
		public QualifiedIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterQualifiedIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitQualifiedIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitQualifiedIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedIdentifierContext qualifiedIdentifier() throws RecognitionException {
		QualifiedIdentifierContext _localctx = new QualifiedIdentifierContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_qualifiedIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			match(ID);
			setState(268);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(264);
				match(DOT);
				setState(265);
				match(ID);
				}
				}
				setState(270);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}

			  ((QualifiedIdentifierContext)_localctx).value =  ParseUtils.unescapeIdentifier(_input.getText(_localctx.start, _input.LT(-1)));

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public String value;
		public TerminalNode ID() { return getToken(CourierParser.ID, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			match(ID);

			  ((IdentifierContext)_localctx).value =  ParseUtils.unescapeIdentifier(_input.getText(_localctx.start, _input.LT(-1)));

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SchemadocContext extends ParserRuleContext {
		public String value;
		public Token SCHEMADOC_COMMENT;
		public TerminalNode SCHEMADOC_COMMENT() { return getToken(CourierParser.SCHEMADOC_COMMENT, 0); }
		public SchemadocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemadoc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterSchemadoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitSchemadoc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitSchemadoc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemadocContext schemadoc() throws RecognitionException {
		SchemadocContext _localctx = new SchemadocContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_schemadoc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			((SchemadocContext)_localctx).SCHEMADOC_COMMENT = match(SCHEMADOC_COMMENT);

			  ((SchemadocContext)_localctx).value =  ParseUtils.extractMarkdown((((SchemadocContext)_localctx).SCHEMADOC_COMMENT!=null?((SchemadocContext)_localctx).SCHEMADOC_COMMENT.getText():null));

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectContext extends ParserRuleContext {
		public TerminalNode OPEN_BRACE() { return getToken(CourierParser.OPEN_BRACE, 0); }
		public TerminalNode CLOSE_BRACE() { return getToken(CourierParser.CLOSE_BRACE, 0); }
		public List<ObjectEntryContext> objectEntry() {
			return getRuleContexts(ObjectEntryContext.class);
		}
		public ObjectEntryContext objectEntry(int i) {
			return getRuleContext(ObjectEntryContext.class,i);
		}
		public ObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_object; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitObject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectContext object() throws RecognitionException {
		ObjectContext _localctx = new ObjectContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_object);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			match(OPEN_BRACE);
			setState(283);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==STRING_LITERAL) {
				{
				{
				setState(280);
				objectEntry();
				}
				}
				setState(285);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(286);
			match(CLOSE_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectEntryContext extends ParserRuleContext {
		public StringContext key;
		public JsonValueContext value;
		public TerminalNode COLON() { return getToken(CourierParser.COLON, 0); }
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public JsonValueContext jsonValue() {
			return getRuleContext(JsonValueContext.class,0);
		}
		public ObjectEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectEntry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterObjectEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitObjectEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitObjectEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectEntryContext objectEntry() throws RecognitionException {
		ObjectEntryContext _localctx = new ObjectEntryContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_objectEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			((ObjectEntryContext)_localctx).key = string();
			setState(289);
			match(COLON);
			setState(290);
			((ObjectEntryContext)_localctx).value = jsonValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayContext extends ParserRuleContext {
		public JsonValueContext items;
		public TerminalNode OPEN_BRACKET() { return getToken(CourierParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(CourierParser.CLOSE_BRACKET, 0); }
		public List<JsonValueContext> jsonValue() {
			return getRuleContexts(JsonValueContext.class);
		}
		public JsonValueContext jsonValue(int i) {
			return getRuleContext(JsonValueContext.class,i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(292);
			match(OPEN_BRACKET);
			setState(296);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OPEN_BRACE) | (1L << OPEN_BRACKET) | (1L << BOOLEAN_LITERAL) | (1L << NULL_LITERAL) | (1L << NUMBER_LITERAL) | (1L << STRING_LITERAL))) != 0)) {
				{
				{
				setState(293);
				((ArrayContext)_localctx).items = jsonValue();
				}
				}
				setState(298);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(299);
			match(CLOSE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JsonValueContext extends ParserRuleContext {
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public BoolContext bool() {
			return getRuleContext(BoolContext.class,0);
		}
		public NullValueContext nullValue() {
			return getRuleContext(NullValueContext.class,0);
		}
		public JsonValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterJsonValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitJsonValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitJsonValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonValueContext jsonValue() throws RecognitionException {
		JsonValueContext _localctx = new JsonValueContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_jsonValue);
		try {
			setState(307);
			switch (_input.LA(1)) {
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(301);
				string();
				}
				break;
			case NUMBER_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(302);
				number();
				}
				break;
			case OPEN_BRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(303);
				object();
				}
				break;
			case OPEN_BRACKET:
				enterOuterAlt(_localctx, 4);
				{
				setState(304);
				array();
				}
				break;
			case BOOLEAN_LITERAL:
				enterOuterAlt(_localctx, 5);
				{
				setState(305);
				bool();
				}
				break;
			case NULL_LITERAL:
				enterOuterAlt(_localctx, 6);
				{
				setState(306);
				nullValue();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringContext extends ParserRuleContext {
		public String value;
		public Token STRING_LITERAL;
		public TerminalNode STRING_LITERAL() { return getToken(CourierParser.STRING_LITERAL, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			((StringContext)_localctx).STRING_LITERAL = match(STRING_LITERAL);

			  ((StringContext)_localctx).value =  ParseUtils.extractString((((StringContext)_localctx).STRING_LITERAL!=null?((StringContext)_localctx).STRING_LITERAL.getText():null));

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberContext extends ParserRuleContext {
		public Number value;
		public Token NUMBER_LITERAL;
		public TerminalNode NUMBER_LITERAL() { return getToken(CourierParser.NUMBER_LITERAL, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(312);
			((NumberContext)_localctx).NUMBER_LITERAL = match(NUMBER_LITERAL);

			  ((NumberContext)_localctx).value =  ParseUtils.toNumber((((NumberContext)_localctx).NUMBER_LITERAL!=null?((NumberContext)_localctx).NUMBER_LITERAL.getText():null));

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoolContext extends ParserRuleContext {
		public Boolean value;
		public Token BOOLEAN_LITERAL;
		public TerminalNode BOOLEAN_LITERAL() { return getToken(CourierParser.BOOLEAN_LITERAL, 0); }
		public BoolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitBool(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoolContext bool() throws RecognitionException {
		BoolContext _localctx = new BoolContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_bool);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			((BoolContext)_localctx).BOOLEAN_LITERAL = match(BOOLEAN_LITERAL);

			  ((BoolContext)_localctx).value =  Boolean.valueOf((((BoolContext)_localctx).BOOLEAN_LITERAL!=null?((BoolContext)_localctx).BOOLEAN_LITERAL.getText():null));

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NullValueContext extends ParserRuleContext {
		public TerminalNode NULL_LITERAL() { return getToken(CourierParser.NULL_LITERAL, 0); }
		public NullValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nullValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).enterNullValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CourierListener ) ((CourierListener)listener).exitNullValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CourierVisitor ) return ((CourierVisitor<? extends T>)visitor).visitNullValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NullValueContext nullValue() throws RecognitionException {
		NullValueContext _localctx = new NullValueContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_nullValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			match(NULL_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3 \u0143\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\3"+
		"\2\5\2X\n\2\3\2\3\2\3\2\3\3\3\3\3\3\3\4\7\4a\n\4\f\4\16\4d\13\4\3\5\3"+
		"\5\3\5\3\6\3\6\3\6\3\7\3\7\5\7n\n\7\3\b\5\bq\n\b\3\b\7\bt\n\b\f\b\16\b"+
		"w\13\b\3\b\3\b\3\b\3\b\5\b}\n\b\3\t\3\t\3\t\5\t\u0082\n\t\3\n\3\n\5\n"+
		"\u0086\n\n\3\13\3\13\5\13\u008a\n\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\5\r\u0098\n\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\17\3\17\3\20\3\20\7\20\u00a6\n\20\f\20\16\20\u00a9\13\20\3\20\3\20"+
		"\3\21\5\21\u00ae\n\21\3\21\7\21\u00b1\n\21\f\21\16\21\u00b4\13\21\3\21"+
		"\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\26\3\26\7\26\u00cb\n\26\f\26\16\26\u00ce\13\26"+
		"\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\32\3\32\3\32"+
		"\3\33\3\33\3\33\3\33\3\33\3\34\3\34\7\34\u00e5\n\34\f\34\16\34\u00e8\13"+
		"\34\3\34\3\34\3\35\3\35\5\35\u00ee\n\35\3\36\3\36\3\36\3\37\5\37\u00f4"+
		"\n\37\3\37\7\37\u00f7\n\37\f\37\16\37\u00fa\13\37\3\37\3\37\3\37\3\37"+
		"\5\37\u0100\n\37\3\37\5\37\u0103\n\37\3\37\3\37\3 \3 \3 \3!\3!\3!\7!\u010d"+
		"\n!\f!\16!\u0110\13!\3!\3!\3\"\3\"\3\"\3#\3#\3#\3$\3$\7$\u011c\n$\f$\16"+
		"$\u011f\13$\3$\3$\3%\3%\3%\3%\3&\3&\7&\u0129\n&\f&\16&\u012c\13&\3&\3"+
		"&\3\'\3\'\3\'\3\'\3\'\3\'\5\'\u0136\n\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+"+
		"\3+\3+\2\2,\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\66"+
		"8:<>@BDFHJLNPRT\2\2\u0137\2W\3\2\2\2\4\\\3\2\2\2\6b\3\2\2\2\be\3\2\2\2"+
		"\nh\3\2\2\2\fm\3\2\2\2\16p\3\2\2\2\20\u0081\3\2\2\2\22\u0085\3\2\2\2\24"+
		"\u0087\3\2\2\2\26\u008d\3\2\2\2\30\u0097\3\2\2\2\32\u0099\3\2\2\2\34\u009e"+
		"\3\2\2\2\36\u00a3\3\2\2\2 \u00ad\3\2\2\2\"\u00b7\3\2\2\2$\u00ba\3\2\2"+
		"\2&\u00c0\3\2\2\2(\u00c5\3\2\2\2*\u00c8\3\2\2\2,\u00d1\3\2\2\2.\u00d3"+
		"\3\2\2\2\60\u00d6\3\2\2\2\62\u00da\3\2\2\2\64\u00dd\3\2\2\2\66\u00e2\3"+
		"\2\2\28\u00ed\3\2\2\2:\u00ef\3\2\2\2<\u00f3\3\2\2\2>\u0106\3\2\2\2@\u0109"+
		"\3\2\2\2B\u0113\3\2\2\2D\u0116\3\2\2\2F\u0119\3\2\2\2H\u0122\3\2\2\2J"+
		"\u0126\3\2\2\2L\u0135\3\2\2\2N\u0137\3\2\2\2P\u013a\3\2\2\2R\u013d\3\2"+
		"\2\2T\u0140\3\2\2\2VX\5\4\3\2WV\3\2\2\2WX\3\2\2\2XY\3\2\2\2YZ\5\6\4\2"+
		"Z[\5\16\b\2[\3\3\2\2\2\\]\7\b\2\2]^\5@!\2^\5\3\2\2\2_a\5\b\5\2`_\3\2\2"+
		"\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2c\7\3\2\2\2db\3\2\2\2ef\7\6\2\2fg\5@!"+
		"\2g\t\3\2\2\2hi\5@!\2ij\b\6\1\2j\13\3\2\2\2kn\5\16\b\2ln\5\20\t\2mk\3"+
		"\2\2\2ml\3\2\2\2n\r\3\2\2\2oq\5D#\2po\3\2\2\2pq\3\2\2\2qu\3\2\2\2rt\5"+
		"\24\13\2sr\3\2\2\2tw\3\2\2\2us\3\2\2\2uv\3\2\2\2v|\3\2\2\2wu\3\2\2\2x"+
		"}\5\32\16\2y}\5\34\17\2z}\5$\23\2{}\5&\24\2|x\3\2\2\2|y\3\2\2\2|z\3\2"+
		"\2\2|{\3\2\2\2}\17\3\2\2\2~\u0082\5(\25\2\177\u0082\5.\30\2\u0080\u0082"+
		"\5\62\32\2\u0081~\3\2\2\2\u0081\177\3\2\2\2\u0081\u0080\3\2\2\2\u0082"+
		"\21\3\2\2\2\u0083\u0086\5\n\6\2\u0084\u0086\5\f\7\2\u0085\u0083\3\2\2"+
		"\2\u0085\u0084\3\2\2\2\u0086\23\3\2\2\2\u0087\u0089\5\26\f\2\u0088\u008a"+
		"\5\30\r\2\u0089\u0088\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008b\3\2\2\2"+
		"\u008b\u008c\b\13\1\2\u008c\25\3\2\2\2\u008d\u008e\7\22\2\2\u008e\u008f"+
		"\5@!\2\u008f\u0090\b\f\1\2\u0090\27\3\2\2\2\u0091\u0092\7\f\2\2\u0092"+
		"\u0093\5L\'\2\u0093\u0094\7\r\2\2\u0094\u0098\3\2\2\2\u0095\u0096\7\26"+
		"\2\2\u0096\u0098\5L\'\2\u0097\u0091\3\2\2\2\u0097\u0095\3\2\2\2\u0098"+
		"\31\3\2\2\2\u0099\u009a\7\t\2\2\u009a\u009b\5B\"\2\u009b\u009c\5\66\34"+
		"\2\u009c\u009d\b\16\1\2\u009d\33\3\2\2\2\u009e\u009f\7\4\2\2\u009f\u00a0"+
		"\5B\"\2\u00a0\u00a1\5\36\20\2\u00a1\u00a2\b\17\1\2\u00a2\35\3\2\2\2\u00a3"+
		"\u00a7\7\16\2\2\u00a4\u00a6\5 \21\2\u00a5\u00a4\3\2\2\2\u00a6\u00a9\3"+
		"\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00aa\3\2\2\2\u00a9"+
		"\u00a7\3\2\2\2\u00aa\u00ab\7\17\2\2\u00ab\37\3\2\2\2\u00ac\u00ae\5D#\2"+
		"\u00ad\u00ac\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00b2\3\2\2\2\u00af\u00b1"+
		"\5\24\13\2\u00b0\u00af\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b0\3\2\2\2"+
		"\u00b2\u00b3\3\2\2\2\u00b3\u00b5\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00b6"+
		"\5\"\22\2\u00b6!\3\2\2\2\u00b7\u00b8\5B\"\2\u00b8\u00b9\b\22\1\2\u00b9"+
		"#\3\2\2\2\u00ba\u00bb\7\n\2\2\u00bb\u00bc\5B\"\2\u00bc\u00bd\7\26\2\2"+
		"\u00bd\u00be\5\22\n\2\u00be\u00bf\b\23\1\2\u00bf%\3\2\2\2\u00c0\u00c1"+
		"\7\5\2\2\u00c1\u00c2\5B\"\2\u00c2\u00c3\7\35\2\2\u00c3\u00c4\b\24\1\2"+
		"\u00c4\'\3\2\2\2\u00c5\u00c6\7\13\2\2\u00c6\u00c7\5*\26\2\u00c7)\3\2\2"+
		"\2\u00c8\u00cc\7\20\2\2\u00c9\u00cb\5,\27\2\u00ca\u00c9\3\2\2\2\u00cb"+
		"\u00ce\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00cf\3\2"+
		"\2\2\u00ce\u00cc\3\2\2\2\u00cf\u00d0\7\21\2\2\u00d0+\3\2\2\2\u00d1\u00d2"+
		"\5\22\n\2\u00d2-\3\2\2\2\u00d3\u00d4\7\3\2\2\u00d4\u00d5\5\60\31\2\u00d5"+
		"/\3\2\2\2\u00d6\u00d7\7\20\2\2\u00d7\u00d8\5\22\n\2\u00d8\u00d9\7\21\2"+
		"\2\u00d9\61\3\2\2\2\u00da\u00db\7\7\2\2\u00db\u00dc\5\64\33\2\u00dc\63"+
		"\3\2\2\2\u00dd\u00de\7\20\2\2\u00de\u00df\5\22\n\2\u00df\u00e0\5\22\n"+
		"\2\u00e0\u00e1\7\21\2\2\u00e1\65\3\2\2\2\u00e2\u00e6\7\16\2\2\u00e3\u00e5"+
		"\58\35\2\u00e4\u00e3\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6"+
		"\u00e7\3\2\2\2\u00e7\u00e9\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00ea\7\17"+
		"\2\2\u00ea\67\3\2\2\2\u00eb\u00ee\5:\36\2\u00ec\u00ee\5<\37\2\u00ed\u00eb"+
		"\3\2\2\2\u00ed\u00ec\3\2\2\2\u00ee9\3\2\2\2\u00ef\u00f0\7\25\2\2\u00f0"+
		"\u00f1\5\n\6\2\u00f1;\3\2\2\2\u00f2\u00f4\5D#\2\u00f3\u00f2\3\2\2\2\u00f3"+
		"\u00f4\3\2\2\2\u00f4\u00f8\3\2\2\2\u00f5\u00f7\5\24\13\2\u00f6\u00f5\3"+
		"\2\2\2\u00f7\u00fa\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9"+
		"\u00fb\3\2\2\2\u00fa\u00f8\3\2\2\2\u00fb\u00fc\5B\"\2\u00fc\u00fd\7\23"+
		"\2\2\u00fd\u00ff\5\22\n\2\u00fe\u0100\7\27\2\2\u00ff\u00fe\3\2\2\2\u00ff"+
		"\u0100\3\2\2\2\u0100\u0102\3\2\2\2\u0101\u0103\5> \2\u0102\u0101\3\2\2"+
		"\2\u0102\u0103\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0105\b\37\1\2\u0105"+
		"=\3\2\2\2\u0106\u0107\7\26\2\2\u0107\u0108\5L\'\2\u0108?\3\2\2\2\u0109"+
		"\u010e\7\37\2\2\u010a\u010b\7\24\2\2\u010b\u010d\7\37\2\2\u010c\u010a"+
		"\3\2\2\2\u010d\u0110\3\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f"+
		"\u0111\3\2\2\2\u0110\u010e\3\2\2\2\u0111\u0112\b!\1\2\u0112A\3\2\2\2\u0113"+
		"\u0114\7\37\2\2\u0114\u0115\b\"\1\2\u0115C\3\2\2\2\u0116\u0117\7\32\2"+
		"\2\u0117\u0118\b#\1\2\u0118E\3\2\2\2\u0119\u011d\7\16\2\2\u011a\u011c"+
		"\5H%\2\u011b\u011a\3\2\2\2\u011c\u011f\3\2\2\2\u011d\u011b\3\2\2\2\u011d"+
		"\u011e\3\2\2\2\u011e\u0120\3\2\2\2\u011f\u011d\3\2\2\2\u0120\u0121\7\17"+
		"\2\2\u0121G\3\2\2\2\u0122\u0123\5N(\2\u0123\u0124\7\23\2\2\u0124\u0125"+
		"\5L\'\2\u0125I\3\2\2\2\u0126\u012a\7\20\2\2\u0127\u0129\5L\'\2\u0128\u0127"+
		"\3\2\2\2\u0129\u012c\3\2\2\2\u012a\u0128\3\2\2\2\u012a\u012b\3\2\2\2\u012b"+
		"\u012d\3\2\2\2\u012c\u012a\3\2\2\2\u012d\u012e\7\21\2\2\u012eK\3\2\2\2"+
		"\u012f\u0136\5N(\2\u0130\u0136\5P)\2\u0131\u0136\5F$\2\u0132\u0136\5J"+
		"&\2\u0133\u0136\5R*\2\u0134\u0136\5T+\2\u0135\u012f\3\2\2\2\u0135\u0130"+
		"\3\2\2\2\u0135\u0131\3\2\2\2\u0135\u0132\3\2\2\2\u0135\u0133\3\2\2\2\u0135"+
		"\u0134\3\2\2\2\u0136M\3\2\2\2\u0137\u0138\7\36\2\2\u0138\u0139\b(\1\2"+
		"\u0139O\3\2\2\2\u013a\u013b\7\35\2\2\u013b\u013c\b)\1\2\u013cQ\3\2\2\2"+
		"\u013d\u013e\7\30\2\2\u013e\u013f\b*\1\2\u013fS\3\2\2\2\u0140\u0141\7"+
		"\31\2\2\u0141U\3\2\2\2\32Wbmpu|\u0081\u0085\u0089\u0097\u00a7\u00ad\u00b2"+
		"\u00cc\u00e6\u00ed\u00f3\u00f8\u00ff\u0102\u010e\u011d\u012a\u0135";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}