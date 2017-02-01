// Generated from /Users/marc/base/coursera/courier/schema-language/src/main/antlr4/Courier.g4 by ANTLR 4.5.1

  import org.coursera.courier.grammar.ParseUtils;
  import java.util.Arrays;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CourierParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CourierVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CourierParser#document}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocument(CourierParser.DocumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#namespaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceDeclaration(CourierParser.NamespaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#importDeclarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclarations(CourierParser.ImportDeclarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#importDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclaration(CourierParser.ImportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#typeReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeReference(CourierParser.TypeReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDeclaration(CourierParser.TypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#namedTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedTypeDeclaration(CourierParser.NamedTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#anonymousTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnonymousTypeDeclaration(CourierParser.AnonymousTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#typeAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAssignment(CourierParser.TypeAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#propDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropDeclaration(CourierParser.PropDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#propNameDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropNameDeclaration(CourierParser.PropNameDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#propJsonValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropJsonValue(CourierParser.PropJsonValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#recordDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordDeclaration(CourierParser.RecordDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#enumDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDeclaration(CourierParser.EnumDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#enumSymbolDeclarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumSymbolDeclarations(CourierParser.EnumSymbolDeclarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#enumSymbolDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumSymbolDeclaration(CourierParser.EnumSymbolDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#enumSymbol}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumSymbol(CourierParser.EnumSymbolContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#typerefDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTyperefDeclaration(CourierParser.TyperefDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#fixedDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFixedDeclaration(CourierParser.FixedDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#unionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionDeclaration(CourierParser.UnionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#unionTypeAssignments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionTypeAssignments(CourierParser.UnionTypeAssignmentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#unionMemberDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionMemberDeclaration(CourierParser.UnionMemberDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#arrayDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayDeclaration(CourierParser.ArrayDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#arrayTypeAssignments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayTypeAssignments(CourierParser.ArrayTypeAssignmentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#mapDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapDeclaration(CourierParser.MapDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#mapTypeAssignments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapTypeAssignments(CourierParser.MapTypeAssignmentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#fieldSelection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldSelection(CourierParser.FieldSelectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#fieldSelectionElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldSelectionElement(CourierParser.FieldSelectionElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#fieldInclude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldInclude(CourierParser.FieldIncludeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDeclaration(CourierParser.FieldDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#fieldDefault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDefault(CourierParser.FieldDefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdentifier(CourierParser.QualifiedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(CourierParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#schemadoc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemadoc(CourierParser.SchemadocContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(CourierParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#objectEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectEntry(CourierParser.ObjectEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(CourierParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#jsonValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonValue(CourierParser.JsonValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(CourierParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(CourierParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#bool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(CourierParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by {@link CourierParser#nullValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullValue(CourierParser.NullValueContext ctx);
}