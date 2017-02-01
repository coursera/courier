// Generated from /Users/marc/base/coursera/courier/schema-language/src/main/antlr4/Courier.g4 by ANTLR 4.5.1

  import org.coursera.courier.grammar.ParseUtils;
  import java.util.Arrays;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CourierParser}.
 */
public interface CourierListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CourierParser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(CourierParser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(CourierParser.DocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#namespaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceDeclaration(CourierParser.NamespaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#namespaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceDeclaration(CourierParser.NamespaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#importDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclarations(CourierParser.ImportDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#importDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclarations(CourierParser.ImportDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclaration(CourierParser.ImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclaration(CourierParser.ImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#typeReference}.
	 * @param ctx the parse tree
	 */
	void enterTypeReference(CourierParser.TypeReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#typeReference}.
	 * @param ctx the parse tree
	 */
	void exitTypeReference(CourierParser.TypeReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(CourierParser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(CourierParser.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#namedTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterNamedTypeDeclaration(CourierParser.NamedTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#namedTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitNamedTypeDeclaration(CourierParser.NamedTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#anonymousTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnonymousTypeDeclaration(CourierParser.AnonymousTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#anonymousTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnonymousTypeDeclaration(CourierParser.AnonymousTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#typeAssignment}.
	 * @param ctx the parse tree
	 */
	void enterTypeAssignment(CourierParser.TypeAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#typeAssignment}.
	 * @param ctx the parse tree
	 */
	void exitTypeAssignment(CourierParser.TypeAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#propDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPropDeclaration(CourierParser.PropDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#propDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPropDeclaration(CourierParser.PropDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#propNameDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPropNameDeclaration(CourierParser.PropNameDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#propNameDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPropNameDeclaration(CourierParser.PropNameDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#propJsonValue}.
	 * @param ctx the parse tree
	 */
	void enterPropJsonValue(CourierParser.PropJsonValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#propJsonValue}.
	 * @param ctx the parse tree
	 */
	void exitPropJsonValue(CourierParser.PropJsonValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#recordDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterRecordDeclaration(CourierParser.RecordDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#recordDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitRecordDeclaration(CourierParser.RecordDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDeclaration(CourierParser.EnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDeclaration(CourierParser.EnumDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#enumSymbolDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterEnumSymbolDeclarations(CourierParser.EnumSymbolDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#enumSymbolDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitEnumSymbolDeclarations(CourierParser.EnumSymbolDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#enumSymbolDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumSymbolDeclaration(CourierParser.EnumSymbolDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#enumSymbolDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumSymbolDeclaration(CourierParser.EnumSymbolDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#enumSymbol}.
	 * @param ctx the parse tree
	 */
	void enterEnumSymbol(CourierParser.EnumSymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#enumSymbol}.
	 * @param ctx the parse tree
	 */
	void exitEnumSymbol(CourierParser.EnumSymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#typerefDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTyperefDeclaration(CourierParser.TyperefDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#typerefDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTyperefDeclaration(CourierParser.TyperefDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#fixedDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFixedDeclaration(CourierParser.FixedDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#fixedDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFixedDeclaration(CourierParser.FixedDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#unionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterUnionDeclaration(CourierParser.UnionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#unionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitUnionDeclaration(CourierParser.UnionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#unionTypeAssignments}.
	 * @param ctx the parse tree
	 */
	void enterUnionTypeAssignments(CourierParser.UnionTypeAssignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#unionTypeAssignments}.
	 * @param ctx the parse tree
	 */
	void exitUnionTypeAssignments(CourierParser.UnionTypeAssignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#unionMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterUnionMemberDeclaration(CourierParser.UnionMemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#unionMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitUnionMemberDeclaration(CourierParser.UnionMemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#arrayDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterArrayDeclaration(CourierParser.ArrayDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#arrayDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitArrayDeclaration(CourierParser.ArrayDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#arrayTypeAssignments}.
	 * @param ctx the parse tree
	 */
	void enterArrayTypeAssignments(CourierParser.ArrayTypeAssignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#arrayTypeAssignments}.
	 * @param ctx the parse tree
	 */
	void exitArrayTypeAssignments(CourierParser.ArrayTypeAssignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#mapDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMapDeclaration(CourierParser.MapDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#mapDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMapDeclaration(CourierParser.MapDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#mapTypeAssignments}.
	 * @param ctx the parse tree
	 */
	void enterMapTypeAssignments(CourierParser.MapTypeAssignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#mapTypeAssignments}.
	 * @param ctx the parse tree
	 */
	void exitMapTypeAssignments(CourierParser.MapTypeAssignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#fieldSelection}.
	 * @param ctx the parse tree
	 */
	void enterFieldSelection(CourierParser.FieldSelectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#fieldSelection}.
	 * @param ctx the parse tree
	 */
	void exitFieldSelection(CourierParser.FieldSelectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#fieldSelectionElement}.
	 * @param ctx the parse tree
	 */
	void enterFieldSelectionElement(CourierParser.FieldSelectionElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#fieldSelectionElement}.
	 * @param ctx the parse tree
	 */
	void exitFieldSelectionElement(CourierParser.FieldSelectionElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#fieldInclude}.
	 * @param ctx the parse tree
	 */
	void enterFieldInclude(CourierParser.FieldIncludeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#fieldInclude}.
	 * @param ctx the parse tree
	 */
	void exitFieldInclude(CourierParser.FieldIncludeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(CourierParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(CourierParser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#fieldDefault}.
	 * @param ctx the parse tree
	 */
	void enterFieldDefault(CourierParser.FieldDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#fieldDefault}.
	 * @param ctx the parse tree
	 */
	void exitFieldDefault(CourierParser.FieldDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedIdentifier(CourierParser.QualifiedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedIdentifier(CourierParser.QualifiedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(CourierParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(CourierParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#schemadoc}.
	 * @param ctx the parse tree
	 */
	void enterSchemadoc(CourierParser.SchemadocContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#schemadoc}.
	 * @param ctx the parse tree
	 */
	void exitSchemadoc(CourierParser.SchemadocContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#object}.
	 * @param ctx the parse tree
	 */
	void enterObject(CourierParser.ObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#object}.
	 * @param ctx the parse tree
	 */
	void exitObject(CourierParser.ObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#objectEntry}.
	 * @param ctx the parse tree
	 */
	void enterObjectEntry(CourierParser.ObjectEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#objectEntry}.
	 * @param ctx the parse tree
	 */
	void exitObjectEntry(CourierParser.ObjectEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(CourierParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(CourierParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#jsonValue}.
	 * @param ctx the parse tree
	 */
	void enterJsonValue(CourierParser.JsonValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#jsonValue}.
	 * @param ctx the parse tree
	 */
	void exitJsonValue(CourierParser.JsonValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(CourierParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(CourierParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(CourierParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(CourierParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#bool}.
	 * @param ctx the parse tree
	 */
	void enterBool(CourierParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#bool}.
	 * @param ctx the parse tree
	 */
	void exitBool(CourierParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by {@link CourierParser#nullValue}.
	 * @param ctx the parse tree
	 */
	void enterNullValue(CourierParser.NullValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CourierParser#nullValue}.
	 * @param ctx the parse tree
	 */
	void exitNullValue(CourierParser.NullValueContext ctx);
}