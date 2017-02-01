// Generated from /Users/marc/base/coursera/courier/schema-language/src/main/antlr4/Courier.g4 by ANTLR 4.5.1

  import org.coursera.courier.grammar.ParseUtils;
  import java.util.Arrays;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CourierLexer extends Lexer {
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
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"ARRAY", "ENUM", "FIXED", "IMPORT", "MAP", "NAMESPACE", "RECORD", "TYPEREF", 
		"UNION", "OPEN_PAREN", "CLOSE_PAREN", "OPEN_BRACE", "CLOSE_BRACE", "OPEN_BRACKET", 
		"CLOSE_BRACKET", "AT", "COLON", "DOT", "DOTDOTDOT", "EQ", "QUESTION_MARK", 
		"BOOLEAN_LITERAL", "NULL_LITERAL", "SCHEMADOC_COMMENT", "BLOCK_COMMENT", 
		"LINE_COMMENT", "NUMBER_LITERAL", "HEX", "UNICODE", "ESC", "STRING_LITERAL", 
		"ID", "WS"
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


	public CourierLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Courier.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2 \u011b\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16"+
		"\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\5\27\u00a4\n\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\7\31"+
		"\u00b0\n\31\f\31\16\31\u00b3\13\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32"+
		"\7\32\u00bc\n\32\f\32\16\32\u00bf\13\32\3\32\3\32\3\32\3\32\3\32\3\33"+
		"\3\33\3\33\3\33\7\33\u00ca\n\33\f\33\16\33\u00cd\13\33\3\33\3\33\3\34"+
		"\5\34\u00d2\n\34\3\34\3\34\3\34\7\34\u00d7\n\34\f\34\16\34\u00da\13\34"+
		"\5\34\u00dc\n\34\3\34\3\34\6\34\u00e0\n\34\r\34\16\34\u00e1\5\34\u00e4"+
		"\n\34\3\34\3\34\5\34\u00e8\n\34\3\34\6\34\u00eb\n\34\r\34\16\34\u00ec"+
		"\5\34\u00ef\n\34\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\5\37\u00fc\n\37\3 \3 \3 \7 \u0101\n \f \16 \u0104\13 \3 \3 \3!\5!\u0109"+
		"\n!\3!\3!\7!\u010d\n!\f!\16!\u0110\13!\3!\5!\u0113\n!\3\"\6\"\u0116\n"+
		"\"\r\"\16\"\u0117\3\"\3\"\4\u00b1\u00bd\2#\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+"+
		"\27-\30/\31\61\32\63\33\65\34\67\359\2;\2=\2?\36A\37C \3\2\r\5\2\f\f\17"+
		"\17))\3\2\63;\3\2\62;\4\2GGgg\4\2--//\5\2\62;CHch\n\2$$\61\61^^ddhhpp"+
		"ttvv\4\2$$^^\5\2C\\aac|\6\2\62;C\\aac|\6\2\13\f\16\17\"\"..\u012a\2\3"+
		"\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2"+
		"\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31"+
		"\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2"+
		"\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2"+
		"\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\2?\3\2\2\2\2A\3\2\2"+
		"\2\2C\3\2\2\2\3E\3\2\2\2\5K\3\2\2\2\7P\3\2\2\2\tV\3\2\2\2\13]\3\2\2\2"+
		"\ra\3\2\2\2\17k\3\2\2\2\21r\3\2\2\2\23z\3\2\2\2\25\u0080\3\2\2\2\27\u0082"+
		"\3\2\2\2\31\u0084\3\2\2\2\33\u0086\3\2\2\2\35\u0088\3\2\2\2\37\u008a\3"+
		"\2\2\2!\u008c\3\2\2\2#\u008e\3\2\2\2%\u0090\3\2\2\2\'\u0092\3\2\2\2)\u0096"+
		"\3\2\2\2+\u0098\3\2\2\2-\u00a3\3\2\2\2/\u00a5\3\2\2\2\61\u00aa\3\2\2\2"+
		"\63\u00b7\3\2\2\2\65\u00c5\3\2\2\2\67\u00d1\3\2\2\29\u00f0\3\2\2\2;\u00f2"+
		"\3\2\2\2=\u00f8\3\2\2\2?\u00fd\3\2\2\2A\u0108\3\2\2\2C\u0115\3\2\2\2E"+
		"F\7c\2\2FG\7t\2\2GH\7t\2\2HI\7c\2\2IJ\7{\2\2J\4\3\2\2\2KL\7g\2\2LM\7p"+
		"\2\2MN\7w\2\2NO\7o\2\2O\6\3\2\2\2PQ\7h\2\2QR\7k\2\2RS\7z\2\2ST\7g\2\2"+
		"TU\7f\2\2U\b\3\2\2\2VW\7k\2\2WX\7o\2\2XY\7r\2\2YZ\7q\2\2Z[\7t\2\2[\\\7"+
		"v\2\2\\\n\3\2\2\2]^\7o\2\2^_\7c\2\2_`\7r\2\2`\f\3\2\2\2ab\7p\2\2bc\7c"+
		"\2\2cd\7o\2\2de\7g\2\2ef\7u\2\2fg\7r\2\2gh\7c\2\2hi\7e\2\2ij\7g\2\2j\16"+
		"\3\2\2\2kl\7t\2\2lm\7g\2\2mn\7e\2\2no\7q\2\2op\7t\2\2pq\7f\2\2q\20\3\2"+
		"\2\2rs\7v\2\2st\7{\2\2tu\7r\2\2uv\7g\2\2vw\7t\2\2wx\7g\2\2xy\7h\2\2y\22"+
		"\3\2\2\2z{\7w\2\2{|\7p\2\2|}\7k\2\2}~\7q\2\2~\177\7p\2\2\177\24\3\2\2"+
		"\2\u0080\u0081\7*\2\2\u0081\26\3\2\2\2\u0082\u0083\7+\2\2\u0083\30\3\2"+
		"\2\2\u0084\u0085\7}\2\2\u0085\32\3\2\2\2\u0086\u0087\7\177\2\2\u0087\34"+
		"\3\2\2\2\u0088\u0089\7]\2\2\u0089\36\3\2\2\2\u008a\u008b\7_\2\2\u008b"+
		" \3\2\2\2\u008c\u008d\7B\2\2\u008d\"\3\2\2\2\u008e\u008f\7<\2\2\u008f"+
		"$\3\2\2\2\u0090\u0091\7\60\2\2\u0091&\3\2\2\2\u0092\u0093\7\60\2\2\u0093"+
		"\u0094\7\60\2\2\u0094\u0095\7\60\2\2\u0095(\3\2\2\2\u0096\u0097\7?\2\2"+
		"\u0097*\3\2\2\2\u0098\u0099\7A\2\2\u0099,\3\2\2\2\u009a\u009b\7v\2\2\u009b"+
		"\u009c\7t\2\2\u009c\u009d\7w\2\2\u009d\u00a4\7g\2\2\u009e\u009f\7h\2\2"+
		"\u009f\u00a0\7c\2\2\u00a0\u00a1\7n\2\2\u00a1\u00a2\7u\2\2\u00a2\u00a4"+
		"\7g\2\2\u00a3\u009a\3\2\2\2\u00a3\u009e\3\2\2\2\u00a4.\3\2\2\2\u00a5\u00a6"+
		"\7p\2\2\u00a6\u00a7\7w\2\2\u00a7\u00a8\7n\2\2\u00a8\u00a9\7n\2\2\u00a9"+
		"\60\3\2\2\2\u00aa\u00ab\7\61\2\2\u00ab\u00ac\7,\2\2\u00ac\u00ad\7,\2\2"+
		"\u00ad\u00b1\3\2\2\2\u00ae\u00b0\13\2\2\2\u00af\u00ae\3\2\2\2\u00b0\u00b3"+
		"\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\u00b4\3\2\2\2\u00b3"+
		"\u00b1\3\2\2\2\u00b4\u00b5\7,\2\2\u00b5\u00b6\7\61\2\2\u00b6\62\3\2\2"+
		"\2\u00b7\u00b8\7\61\2\2\u00b8\u00b9\7,\2\2\u00b9\u00bd\3\2\2\2\u00ba\u00bc"+
		"\13\2\2\2\u00bb\u00ba\3\2\2\2\u00bc\u00bf\3\2\2\2\u00bd\u00be\3\2\2\2"+
		"\u00bd\u00bb\3\2\2\2\u00be\u00c0\3\2\2\2\u00bf\u00bd\3\2\2\2\u00c0\u00c1"+
		"\7,\2\2\u00c1\u00c2\7\61\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c4\b\32\2\2"+
		"\u00c4\64\3\2\2\2\u00c5\u00c6\7\61\2\2\u00c6\u00c7\7\61\2\2\u00c7\u00cb"+
		"\3\2\2\2\u00c8\u00ca\n\2\2\2\u00c9\u00c8\3\2\2\2\u00ca\u00cd\3\2\2\2\u00cb"+
		"\u00c9\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00ce\3\2\2\2\u00cd\u00cb\3\2"+
		"\2\2\u00ce\u00cf\b\33\2\2\u00cf\66\3\2\2\2\u00d0\u00d2\7/\2\2\u00d1\u00d0"+
		"\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00db\3\2\2\2\u00d3\u00dc\7\62\2\2"+
		"\u00d4\u00d8\t\3\2\2\u00d5\u00d7\t\4\2\2\u00d6\u00d5\3\2\2\2\u00d7\u00da"+
		"\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00dc\3\2\2\2\u00da"+
		"\u00d8\3\2\2\2\u00db\u00d3\3\2\2\2\u00db\u00d4\3\2\2\2\u00dc\u00e3\3\2"+
		"\2\2\u00dd\u00df\7\60\2\2\u00de\u00e0\t\4\2\2\u00df\u00de\3\2\2\2\u00e0"+
		"\u00e1\3\2\2\2\u00e1\u00df\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e4\3\2"+
		"\2\2\u00e3\u00dd\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00ee\3\2\2\2\u00e5"+
		"\u00e7\t\5\2\2\u00e6\u00e8\t\6\2\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2"+
		"\2\2\u00e8\u00ea\3\2\2\2\u00e9\u00eb\t\4\2\2\u00ea\u00e9\3\2\2\2\u00eb"+
		"\u00ec\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00ef\3\2"+
		"\2\2\u00ee\u00e5\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef8\3\2\2\2\u00f0\u00f1"+
		"\t\7\2\2\u00f1:\3\2\2\2\u00f2\u00f3\7w\2\2\u00f3\u00f4\59\35\2\u00f4\u00f5"+
		"\59\35\2\u00f5\u00f6\59\35\2\u00f6\u00f7\59\35\2\u00f7<\3\2\2\2\u00f8"+
		"\u00fb\7^\2\2\u00f9\u00fc\t\b\2\2\u00fa\u00fc\5;\36\2\u00fb\u00f9\3\2"+
		"\2\2\u00fb\u00fa\3\2\2\2\u00fc>\3\2\2\2\u00fd\u0102\7$\2\2\u00fe\u0101"+
		"\5=\37\2\u00ff\u0101\n\t\2\2\u0100\u00fe\3\2\2\2\u0100\u00ff\3\2\2\2\u0101"+
		"\u0104\3\2\2\2\u0102\u0100\3\2\2\2\u0102\u0103\3\2\2\2\u0103\u0105\3\2"+
		"\2\2\u0104\u0102\3\2\2\2\u0105\u0106\7$\2\2\u0106@\3\2\2\2\u0107\u0109"+
		"\7b\2\2\u0108\u0107\3\2\2\2\u0108\u0109\3\2\2\2\u0109\u010a\3\2\2\2\u010a"+
		"\u010e\t\n\2\2\u010b\u010d\t\13\2\2\u010c\u010b\3\2\2\2\u010d\u0110\3"+
		"\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0112\3\2\2\2\u0110"+
		"\u010e\3\2\2\2\u0111\u0113\7b\2\2\u0112\u0111\3\2\2\2\u0112\u0113\3\2"+
		"\2\2\u0113B\3\2\2\2\u0114\u0116\t\f\2\2\u0115\u0114\3\2\2\2\u0116\u0117"+
		"\3\2\2\2\u0117\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u0119\3\2\2\2\u0119"+
		"\u011a\b\"\2\2\u011aD\3\2\2\2\26\2\u00a3\u00b1\u00bd\u00cb\u00d1\u00d8"+
		"\u00db\u00e1\u00e3\u00e7\u00ec\u00ee\u00fb\u0100\u0102\u0108\u010e\u0112"+
		"\u0117\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}