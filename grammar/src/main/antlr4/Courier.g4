grammar Courier;

@header {
  import org.coursera.courier.grammar.ParseUtils;
}

document: namespaceDeclaration namedTypeDeclaration;

namespaceDeclaration: 'namespace' namespace;

namespace returns [String value]: parts+=identifier ('.' parts+=identifier)* {
  $value = ParseUtils.join($parts);
};

typeReference returns [String value]:
    fullyQualifiedTypeName { $value = $fullyQualifiedTypeName.value; } |
    simpleName { $value = $simpleName.value; };

typeNameDeclaration returns [String value]: simpleName {
  $value = $simpleName.value;
};

simpleName returns [String value]: identifier {
  $value = $identifier.value;
};

fullyQualifiedTypeName returns [String value]: parts+=identifier ('.' parts+=identifier)* {
  $value = ParseUtils.join($parts);
};

typeDeclaration: namedTypeDeclaration | anonymousTypeDeclaration;

namedTypeDeclaration: doc=schemadoc? props+=propDeclaration*
  (recordDeclaration | enumDeclaration | typerefDeclaration | fixedDeclaration);

anonymousTypeDeclaration: unionDeclaration | arrayDeclaration | mapDeclaration;

typeAssignment: typeReference | typeDeclaration;

propDeclaration returns [String name]: propNameDeclaration propJsonValue? {
  $name = $propNameDeclaration.name;
};

propNameDeclaration returns [String name]: '@' propName { $name = $propName.value; };

propName returns [String value]: parts+=identifier ('.' parts+=identifier)* {
  $value = ParseUtils.join($parts);
};

// TODO(jbetz): remove '( )' form once migrated to '=' form.
propJsonValue: '(' jsonValue ')' | '=' jsonValue;

recordDeclaration: 'record' name=typeNameDeclaration recordDecl=fieldSelection;

enumDeclaration: 'enum' name=typeNameDeclaration enumDecl=enumSymbolDeclarations;

enumSymbolDeclarations: '{' symbolDecls+=enumSymbolDeclaration* '}';

enumSymbolDeclaration: doc=schemadoc? props+=propDeclaration* symbol=enumSymbol;

enumSymbol returns [String value]: identifier {
  $value = $identifier.value;
};

typerefDeclaration: 'typeref' name=typeNameDeclaration '=' ref=typeAssignment;

fixedDeclaration returns[int size]:
  'fixed' name=typeNameDeclaration sizeStr=NON_NEGATIVE_INTEGER_LITERAL {
  $size = $sizeStr.int;
};

unionDeclaration: 'union' typeParams=unionTypeAssignments;

unionTypeAssignments: '[' members+=unionMemberDeclaration* ']';

unionMemberDeclaration: doc=schemadoc? props+=propDeclaration* member=typeAssignment;

arrayDeclaration: 'array' typeParams=arrayTypeAssignments;

arrayTypeAssignments: '[' items=typeAssignment ']';

mapDeclaration: 'map' typeParams=mapTypeAssignments;

mapTypeAssignments: '[' key=typeAssignment value=typeAssignment ']';

fieldSelection: '{' fields+=fieldSelectionElement* '}';

fieldSelectionElement: fieldInclude | fieldDeclaration;

fieldInclude: '...' typeReference;

fieldDeclaration returns [String name, boolean isOptional]:
    doc=schemadoc? props+=propDeclaration* fieldName ':' type=typeAssignment QUESTION_MARK?
    fieldDefault? {
  $name = $fieldName.name;
  $isOptional = $QUESTION_MARK() != null;
};

fieldName returns [String name]: identifier { $name = $identifier.value; };

fieldDefault returns [boolean isNil]: '=' (jsonValue | NIL) {
  $isNil = $NIL() != null;
};

identifier returns [String value]:
    PLAIN_IDENTIFIER { $value = $text; } |
    ESCAPED_IDENTIFIER { $value = ParseUtils.stripEscaping($text); };

schemadoc returns [String value]: SCHEMADOC_COMMENT {
  $value = ParseUtils.extractMarkdown($SCHEMADOC_COMMENT.text);
};

// JSON
object: '{' objectEntry* '}';

objectEntry: key=string ':' value=jsonValue ;

array: '[' items=jsonValue* ']';

jsonValue: string | number | object | array | bool | nullValue;

string returns [String value]: STRING_LITERAL {
  $value = ParseUtils.extractString($STRING_LITERAL.text);
};

number returns [Number value]: NUMBER_LITERAL {
  if ($NUMBER_LITERAL.text.contains(".")) {
    $value = Double.valueOf($NUMBER_LITERAL.text);
  } else {
    $value = Long.valueOf($NUMBER_LITERAL.text);
  }
};

bool returns [Boolean value]: BOOLEAN_LITERAL {
  $value = Boolean.valueOf($BOOLEAN_LITERAL.text);
};

nullValue: NULL_LITERAL;

NIL: 'nil';
QUESTION_MARK: '?';
BOOLEAN_LITERAL: 'true' | 'false';
NULL_LITERAL: 'null';

SCHEMADOC_COMMENT: '/**' .*? '*/';
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~['\r\n']* -> skip;

NUMBER_LITERAL: INTEGER_LITERAL ( '.' [0-9]+)? ([eE][+-]?[0-9]+)?;
INTEGER_LITERAL: '-'? NON_NEGATIVE_INTEGER_LITERAL;
NON_NEGATIVE_INTEGER_LITERAL: '0' | [1-9] [0-9]*;

fragment HEX: [0-9a-fA-F];
fragment UNICODE: 'u' HEX HEX HEX HEX;
fragment ESC:   '\\' (["\\/bfnrt] | UNICODE);
STRING_LITERAL: '"' (ESC | ~["\\])* '"';

PLAIN_IDENTIFIER: [A-Za-z_] [A-Za-z0-9_]*; // Avro/Pegasus identifiers
ESCAPED_IDENTIFIER: '`' PLAIN_IDENTIFIER '`'; // Scala/Swift style keyword escaping

WS: [ \t\n\r\f,]+ -> skip;
