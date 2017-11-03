/*
 * Copyright 2016 Coursera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.coursera.courier.flowtype;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.DataSchema.Type;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.PrimitiveDataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.schema.UnionDataSchema;
import com.linkedin.pegasus.generator.spec.*;
import org.coursera.courier.api.ClassTemplateSpecs;
import org.coursera.courier.lang.DocCommentStyle;
import org.coursera.courier.lang.DocEscaping;
import org.coursera.courier.flowtype.FlowtypeProperties.Optionality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * Main work-horse for populating the ts-lite Rythm templates.
 *
 * Most work delegates to inner classes, so you probably want to look them (linked below)
 *
<<<<<<< HEAD
 * Specifically, {@link FlowtypeEnumSyntax}, {@link TSUnionSyntax}, {@link FlowtypeRecordSyntax}, and {@link FlowtypeTyperefSyntax} are
=======
 * Specifically, {@link TSEnumSyntax}, {@link FlowtypeUnionSyntax}, {@link FlowtypeRecordSyntax}, and {@link FlowtypeTyperefSyntax} are
>>>>>>> Add rough union
 * used directly to populate the templates.
 *
 * @see TSPrimitiveTypeSyntax
 * @see FlowtypeEnumSyntax
 * @see TSArraySyntax
 * @see FlowtypeMapSyntax
 * @see FlowtypeTyperefSyntax
 * @see FlowtypeRecordSyntax
 * @see TSFixedSyntax
 * @see FlowtypeRecordSyntax
 * @see FlowtypeUnionSyntax
 */
public class FlowtypeSyntax {

  /** Config properties passed from the command line parser */
  private final FlowtypeProperties FlowtypeProperties;

  public FlowtypeSyntax(FlowtypeProperties FlowtypeProperties) {
    this.FlowtypeProperties = FlowtypeProperties;
  }

  /**
   * Varying levels of reserved keywords copied from https://github.com/Microsoft/TypeScript/issues/2536
   **/
  private static final Set<String> tsKeywords = new HashSet<String>(Arrays.asList(new String[]{
    // Reserved Words
    "break",
    "case",
    "catch",
    "class",
    "const",
    "continue",
    "debugger",
    "default",
    "delete",
    "do",
    "else",
    "enum",
    "export",
    "extends",
    "false",
    "finally",
    "for",
    "function",
    "if",
    "import",
    "in",
    "instanceof",
    "new",
    "null",
    "return",
    "super",
    "switch",
    "this",
    "throw",
    "true",
    "try",
    "typeof",
    "var",
    "void",
    "while",
    "with",

    // Strict Mode Reserved Words
    "as",
    "implements",
    "interface",
    "let",
    "package",
    "private",
    "protected",
    "public",
    "static",
    "yield",

    // Contextual Keywords
    "any",
    "boolean",
    "constructor",
    "declare",
    "get",
    "module",
    "require",
    "number",
    "set",
    "string",
    "symbol",
    "type",
    "from",
    "of"
  }));


  /** Different choices for how to escaping symbols that match reserved ts keywords. */
  private static enum EscapeStrategy {
    /** Adds a dollar sign after the symbol name when escaping. e.g.: class becomes class$ */
    MANGLE,

    /** Quotes the symbol when escaping. e.g.: class becomes "class" */
    QUOTE
  }

  /**
   * Returns the escaped Pegasus symbol for use in Typescript source code.
   *
   * Pegasus symbols must be of the form [A-Za-z_], so this routine simply checks if the
   * symbol collides with a typescript keyword, and if so, escapes it.
   *
   * @param symbol the symbol to escape
   * @param strategy which strategy to use in escaping
   *
   * @return the escaped Pegasus symbol.
   */
  private static String escapeKeyword(String symbol, EscapeStrategy strategy) {
    if (tsKeywords.contains(symbol)) {
      if (strategy.equals(EscapeStrategy.MANGLE)) {
        return symbol + "$";
      } else {
        return "\"" + symbol + "\"";
      }
    } else {
      return symbol;
    }
  }

  /**
   * Creates a valid typescript import string given a type name (e.g. "Fortune") and the module name,
   * which is usually the pegasus object's namespace.
   *
   * @param typeName Name of the type to import (e.g. "Fortune")
   * @param moduleName That same type's namespace (e.g. "org.example")
   *
   * @return A fully formed import statement. e.g: import { Fortune } from "./org.example.Fortune"
   **/
  private static String importString(String typeName, String moduleName) {
    return new StringBuilder()
        .append("import type { ")
        .append(typeName)
        .append(" } from \"./")
        .append(moduleName)
        .append(".")
        .append(typeName)
        .append("\";")
        .toString();
  }

  /**
   * Return a full tsdoc for a type.
   *
   * @param doc the doc string in the type's DataSchema.
   * @param deprecation the object listed under the schema's "deprecation" property
   *
   * @return a fully formed tsdoc for the type.
   */
  private static String docComment(String doc, Object deprecation /* nullable */) {
    StringBuilder docStr = new StringBuilder();

    if (doc != null) {
      docStr.append(doc.trim());
    }

    if (deprecation != null) {
      docStr.append("\n\n").append("@deprecated");
      if (deprecation instanceof String) {
        docStr.append("  ").append(((String)deprecation).trim());
      }
    }
    return DocEscaping.stringToDocComment(docStr.toString(), DocCommentStyle.ASTRISK_MARGIN);
  }


  /**
   * Takes a set of imports constructed with {@link #importString}, and produces a valid import block
   * for use at the top of a typescript source file
   *
   * @param imports the set of imports, each of which is a valid import line in typescript
   * @return the import block, on separate lines.
   */
  private static String flattenImports(Set<String> imports) {
    StringBuilder sb = new StringBuilder();

    for (String import_: imports) {
      sb.append(import_).append("\n");
    }

    return sb.toString();
  }

  /** Describes any type we are representing in the generated typescript */
  interface FlowtypeTypeSyntax {

    /** Return the simple name of the type, in valid typescript. "number" or "string" for example. */
    public String typeName();

    /**
     * Return the set of modules that must be imported in order for some other module
     * to use this type.
     **/
    public Set<String> modulesRequiredToUse();
  }

  /**
   * Describes any type that can be enclosed by another. According to the restli spec this only applies
   * to anonymous unions. https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates
   **/
  private interface TSEnclosedTypeSyntax {
    public String typeNameQualifiedByEnclosedType();
  }

  /**
   * Create a TS*Syntax class around the provided ClassTemplate.
   *
   * That class will perform the heavy lifting of rendering TS-specific strings into the template.
   *
   * @param template the ClassTemplate
   * @return a TS*Syntax class (see {@link FlowtypeSyntax} class-level docs for more info)
   */
  private FlowtypeTypeSyntax createTypeSyntax(ClassTemplateSpec template) {
    if (template instanceof RecordTemplateSpec) {
      return new FlowtypeRecordSyntax((RecordTemplateSpec) template);
    } else if (template instanceof TyperefTemplateSpec) {
      return FlowtypeTyperefSyntaxCreate((TyperefTemplateSpec) template);
    } else if (template instanceof FixedTemplateSpec) {
      return TSFixedSyntaxCreate((FixedTemplateSpec) template);
    } else if (template instanceof EnumTemplateSpec) {
      return new FlowtypeEnumSyntax((EnumTemplateSpec) template);
    } else if (template instanceof PrimitiveTemplateSpec) {
      return new TSPrimitiveTypeSyntax((PrimitiveTemplateSpec) template);
    } else if (template instanceof MapTemplateSpec) {
      return new FlowtypeMapSyntax((MapTemplateSpec) template);
    } else if (template instanceof ArrayTemplateSpec) {
      return new TSArraySyntax((ArrayTemplateSpec) template);
    } else if (template instanceof UnionTemplateSpec) {
      return new FlowtypeUnionSyntax((UnionTemplateSpec) template);
    } else {
      throw new RuntimeException("Unrecognized template spec: " + template + " with schema " + template.getSchema());
    }
  }

  /** Convenience wrapper around {@link #createTypeSyntax(ClassTemplateSpec)}. */
  private FlowtypeTypeSyntax createTypeSyntax(DataSchema schema) {
    return createTypeSyntax(ClassTemplateSpec.createFromDataSchema(schema));
  }

  /**
   * Returns the type name, prefaced with the enclosing class name if there was one.
   *
   * For example, a standalone union called MyUnion will just return "MyUnion".
   * If that same union were enclosed within MyRecord, this would return "MyRecord.MyUnion".
   **/
  String typeNameQualifiedByEnclosingClass(FlowtypeTypeSyntax syntax) {
    if (syntax instanceof TSEnclosedTypeSyntax) {
      return ((TSEnclosedTypeSyntax) syntax).typeNameQualifiedByEnclosedType();
    } else {
      return syntax.typeName();
    }
  }

  /** TS-specific syntax for Maps */
  private class FlowtypeMapSyntax implements FlowtypeTypeSyntax {
    private final MapTemplateSpec _template;

    FlowtypeMapSyntax(MapTemplateSpec _template) {
      this._template = _template;
    }

    @Override
    public String typeName() {
      // (This comment is duplicated from TSArraySyntax.typeName for your benefit)
      // Sadly the behavior of this function is indirectly controlled by the one calling it: FlowRecordFieldSyntax.
      // That class has the unfortunate behavior that it can produce 2 different ClassTemplateSpecs, one of which works for
      // some cases, and one of which works for the others. See its own "typeName" definition for details but essentially
      // it will give us one of the ClassTemplateSpecs and call typeName. If we then return null
      // then it will give it a shot with the other ClassTemplateSpec. Unfortunately we have to do this because if
      // we try to just use the first one, we will return "Map<null>". This is also why we special-case unions here.
      // we have to access a specific ClassTemplate
      boolean valueIsUnion = _template.getValueClass() instanceof UnionTemplateSpec;
      FlowtypeTypeSyntax itemTypeSyntax = valueIsUnion? createTypeSyntax(_template.getValueClass()): _valueTypeSyntax();
      String valueTypeName = typeNameQualifiedByEnclosingClass(itemTypeSyntax);
      return valueTypeName == null? null: "Map<" + valueTypeName + ">";
    }

    @Override
    public Set<String> modulesRequiredToUse() {
      Set<String> modules = new HashSet<>();
      modules.add("import type { Map } from \"./CourierRuntime\";"); // Our runtime contains a typedef for Map<ValueT>
      modules.addAll(_valueTypeSyntax().modulesRequiredToUse()); // Need the map's value type to compile code that uses this type.
      return modules;
    }

    //
    // Private FlowtypeMapSyntax members
    //
    private FlowtypeTypeSyntax _valueTypeSyntax() {
      return createTypeSyntax(_template.getSchema().getValues());
    }
  }

  /** TS-specific syntax for Arrays */
  private class TSArraySyntax implements FlowtypeTypeSyntax {
    private final ArrayTemplateSpec _template;

    TSArraySyntax(ArrayTemplateSpec _template) {
      this._template = _template;
    }

    @Override
    public String typeName() {
      // Sadly the behavior of this function is indirectly controlled by the one calling it: FlowRecordFieldSyntax.
      // That class has the unfortunate behavior that it can produce 2 different ClassTemplateSpecs, one of which works for
      // some cases, and one of which works for the others. See its own "typeName" definition for details but essentially
      // it will give us one of the ClassTemplateSpecs and call typeName. If we then return null
      // then it will give it a shot with the other ClassTemplateSpec. Unfortunately we have to do this because if
      // we try to just use the first one, we will return "Array<null>". This is also why we special-case unions here.
      // we have to access a specific ClassTemplate
      boolean itemIsUnion = _template.getItemClass() instanceof UnionTemplateSpec;
      FlowtypeTypeSyntax itemTypeSyntax = itemIsUnion? createTypeSyntax(_template.getItemClass()): _itemTypeSyntax();
      String itemTypeName = typeNameQualifiedByEnclosingClass(itemTypeSyntax);
      return itemTypeName == null? null: "Array<" + itemTypeName + ">";
    }

    @Override
    public Set<String> modulesRequiredToUse() {
      return _itemTypeSyntax().modulesRequiredToUse(); // Need to import the array's index type to compile code that uses this type
    }

    //
    // Private TSArraySyntax members
    //
    private FlowtypeTypeSyntax _itemTypeSyntax() {
      return createTypeSyntax(_template.getSchema().getItems());
    }
  }

  /** Pegasus types that should be rendered as "number" in typescript */
  private static final Set<Type> TS_NUMBER_TYPES = new HashSet<>(
      Arrays.asList(
          new Type[] { Type.INT, Type.LONG, Type.FLOAT, Type.DOUBLE }
      )
  );

  /** Pegasus types that should be rendered as "string" in typescript */
  private static final Set<Type> TS_STRING_TYPES = new HashSet<>(
      Arrays.asList(
          new Type[] { Type.STRING, Type.BYTES, Type.FIXED }
      )
  );

  /** TS-specific syntax for all primitive types: Integer, Long, Float, Double, Boolean, String, Byte. */
  private class TSPrimitiveTypeSyntax implements FlowtypeTypeSyntax {
    private final PrimitiveTemplateSpec _template;
    private final PrimitiveDataSchema _schema;

    TSPrimitiveTypeSyntax(PrimitiveTemplateSpec _template) {
      this._template = _template;
      this._schema = _template.getSchema();
    }

    @Override
    public String typeName() {
      Type schemaType = _schema.getType();
      if (TS_NUMBER_TYPES.contains(schemaType)) {
        return "number";
      } else if (TS_STRING_TYPES.contains(schemaType)) {
        return "string";
      } else if (schemaType == Type.BOOLEAN) {
        return "boolean";
      } else {
        throw new IllegalArgumentException("Unexpected type " + schemaType + " in schema " + _schema);
      }
    }

    @Override
    public Set<String> modulesRequiredToUse() {
      return new HashSet<>(); // using a primitive requires no imports
    }
  }

  /**
   * Helper class that more-or-less wraps {@link NamedDataSchema}.
   *
   * Helps reduce code bloat for Records, Enums, and Typerefs.
   **/
  private class FlowtypeNamedTypeSyntax {
    private final NamedDataSchema _dataSchema;

    public FlowtypeNamedTypeSyntax(NamedDataSchema _dataSchema) {
      this._dataSchema = _dataSchema;
    }

    public String typeName() {
      return FlowtypeSyntax.escapeKeyword(this._dataSchema.getName(), EscapeStrategy.MANGLE);
    }

    public String docString() {
      return docComment(
          _dataSchema.getDoc(),
          _dataSchema.getProperties().get("deprecated")
      );
    }

    public Set<String> modulesRequiredToUse() {
      Set<String> modules = new HashSet<>();
      // Named types get their own files, so you have to import them in order to use them.
      modules.add(importString(_dataSchema.getName(), _dataSchema.getNamespace()));
      return modules;
    }
  }

  /** TS syntax for Fixed types. */
  public class TSFixedSyntax  implements FlowtypeTypeSyntax {
    private final FixedTemplateSpec _template;
    private final FlowtypeNamedTypeSyntax _namedSyntax;

    public TSFixedSyntax(FixedTemplateSpec template, FlowtypeNamedTypeSyntax namedSyntax) {
      this._template = template;
      this._namedSyntax = namedSyntax;
    }

    public String docString() {
      return _namedSyntax.docString();
    }

    public String typeName() {
      return _namedSyntax.typeName();
    }

    @Override
    public Set<String> modulesRequiredToUse() {
      return _namedSyntax.modulesRequiredToUse();
    }
  }

  /** Create a new TSFixedSyntax */
  public TSFixedSyntax TSFixedSyntaxCreate(FixedTemplateSpec template) {
    return new TSFixedSyntax(template, new FlowtypeNamedTypeSyntax(template.getSchema()));
  }

  /**
   * TS representation of a Union type's member (e.g. the "int" in "union[int]").
   */
  public class FlowtypeUnionMemberSyntax {
    private final FlowtypeUnionSyntax _parentSyntax;
    private final UnionDataSchema _schema;
    private final UnionTemplateSpec.Member _member;

    public FlowtypeUnionMemberSyntax(FlowtypeUnionSyntax _parentSyntax, UnionDataSchema _schema, UnionTemplateSpec.Member _member) {
      this._parentSyntax = _parentSyntax;
      this._schema = _schema;
      this._member = _member;
    }

    /**
     * Provides a partially-qualified representation of this type's "Member" sister.
     * For example, if you had a courier union[int] typeref as "MyUnion", this method would
     * return "MyUnion.IntMember".
     **/
    String fullUnionMemberTypeName() {
      return _parentSyntax.typeName() + "." + this.unionMemberTypeName();
    }

    /**
     * Returns the symbol used to access this union member's index in the union's "unpack" return object.
     *
     * For example, given union[FortuneCookie], the return object from "unpack" would be { fortuneCookie: union["namespace.FortuneCookie"] as FortuneCookie }
     */
    public String unpackString() {
      DataSchema schema = _memberSchema();
      String unpackNameBase;
      if (schema instanceof PrimitiveDataSchema) {
        unpackNameBase = schema.getUnionMemberKey();
      } else {
        unpackNameBase = _memberTypeSyntax().typeName();
      }

      String punctuationEscaped = unpackNameBase.replaceAll("[\\p{Punct}\\p{Space}]", "");
      String lowerCased = Character.toLowerCase(punctuationEscaped.charAt(0)) + punctuationEscaped.substring(1);

      return escapeKeyword(lowerCased, EscapeStrategy.MANGLE);
    }

    /**
     * Returns the union member class name for the given {@link ClassTemplateSpec} as a Typescript
     * source code string.
     *
     * @return a typescript source code string identifying the union member.
     */
    public String unionMemberTypeName() {
      DataSchema memberSchema = _memberSchema();
      Type memberType = _memberSchema().getType();
      if (memberSchema.isPrimitive() || memberType == Type.MAP || memberType == Type.ARRAY) {
        String unionMemberKey = _memberSchema().getUnionMemberKey();
        String camelCasedName = Character.toUpperCase(unionMemberKey.charAt(0)) + unionMemberKey.substring(1);
        return camelCasedName + "Member"; // IntMember, DoubleMember, FixedMember etc
      } else if (memberSchema instanceof NamedDataSchema) {
        String className = ((NamedDataSchema) memberSchema).getName();
        return className + "Member"; // e.g: FortuneCookieMember
      } else {
        throw new IllegalArgumentException("Don't know how to handle schema of type " + memberSchema.getType());
      }
    }

    public String unionMemberKey() {
      return _member.getSchema().getUnionMemberKey();
    }

    public String typeName() {
      return _memberTypeSyntax().typeName();
    }

    /** The set of modules imports that need to be included in order to use the type represented by this union member */
    Set<String> typeModules() {
      return _memberTypeSyntax().modulesRequiredToUse();
    }

    //
    // Private UnionMemberSyntax members
    //
    private DataSchema _memberSchema() {
      return _member.getSchema();
    }
    private FlowtypeTypeSyntax _memberTypeSyntax() {
      return createTypeSyntax(_member.getSchema());
    }
  }

  /** TS-specific representation of a Union type. */
  public class FlowtypeUnionSyntax implements FlowtypeTypeSyntax, TSEnclosedTypeSyntax {
    private final UnionTemplateSpec _template;
    private final UnionDataSchema _schema;

    public FlowtypeUnionSyntax(UnionTemplateSpec _template) {
      this._template = _template;
      this._schema = _template.getSchema();
    }

    @Override
    public String typeNameQualifiedByEnclosedType() {
      if (_template.getEnclosingClass() != null) {
        return createTypeSyntax(_template.getEnclosingClass()).typeName() + "." + this.typeName();
      } else {
        return this.typeName();
      }
    }

    @Override
    public String typeName() {
      if (_template.getTyperefClass() != null) {
        // If this union was typerefed then just use the typeref name
        FlowtypeTyperefSyntax refSyntax = FlowtypeTyperefSyntaxCreate(_template.getTyperefClass());
        return refSyntax.typeName();
      } else {
        // I actually never figured out why this works, so I'm very sorry if you're dealing
        // with the repercussions here.
        return escapeKeyword(this._template.getClassName(), EscapeStrategy.MANGLE);
      }
    }

    /** Return the whole typescript import block for the file in which this union is declared. */
    public String imports() {
      Set<String> allImports = new HashSet<>();

      // Only print out the imports for non-enclosed union types. Enclosed ones will be handled
      // by the enclosing record.
      if (!_isEnclosedType()) {
        for (FlowtypeUnionMemberSyntax member: this.members()) {
          allImports.addAll(member.typeModules());
        }
      }

      return flattenImports(allImports);
    }

    @Override
    public Set<String> modulesRequiredToUse() {
      Set<String> modules = new HashSet<String>();
      // enclosed types dont report modules -- their enclosing types will do so for them!
      if (!_isEnclosedType() && this.typeName() != null) {
        modules.add(importString(this.typeName(), this._template.getNamespace()));
      }
      return modules;
    }

    public String docString() {
      if (this._template.getTyperefClass() != null) {
        return new FlowtypeNamedTypeSyntax(this._template.getTyperefClass().getSchema()).docString();
      } else {
        return "";
      }
    }

    /**
     * Produces the "MyUnionMember" typename.
     *
     * For example, union[int, string] produces a few extra types: IntMember, StringMember, etc. Each of those inherit
     * from "MyUnionMember" (or whatever your union type is called)
     **/
    public String memberBaseTypeName() {
      return this.typeName() + "Member";
    }

    /**
     * Given union[int, string, FortuneCookie] this returns the typescript equivalent: "number" | "string" | FortuneCookie
     **/
    public String unionTypeExpression() {
      StringBuilder sb = new StringBuilder();

      List<FlowtypeUnionMemberSyntax> members = this.members();
      for (int i = 0; i < members.size(); i++) {
        boolean isLast = (i == members.size() - 1);
        FlowtypeUnionMemberSyntax member = members.get(i);
        sb.append(member.typeName());

        if (!isLast) {
          sb.append(" | ");
        }
      }

      return sb.toString();
    }

    /**
     * The same as {@link #unionTypeExpression}, but for the *Member interfaces that provide string-lookup.
     *
     * So given union[int, string, FortuneCookie] this returns "MyUnion.IntMember | MyUnion.StringMember | MyUnion.FortuneCookieMember"
     *
     */
    public String memberUnionTypeExpression() {
      List<FlowtypeUnionMemberSyntax> members = this.members();

      if (members.isEmpty()) {
        return "void";
      } else {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < members.size(); i++) {
          boolean isLast = (i == members.size() - 1);
          FlowtypeUnionMemberSyntax member = members.get(i);
          // sb.append(member.fullUnionMemberTypeName());
          sb.append(member.typeName());

          if (!isLast) {
            sb.append(" | ");
          }
        }

        return sb.toString();
      }
    }

    /** Return the syntax for each member */
    public List<FlowtypeUnionMemberSyntax> members() {
      List<FlowtypeUnionMemberSyntax> memberSyntax = new ArrayList<>();

      for (UnionTemplateSpec.Member member : this._template.getMembers()) {
        memberSyntax.add(new FlowtypeUnionMemberSyntax(this, _schema, member));
      }

      return memberSyntax;
    }

    /** Returns true in the usual case that this isn't some stupid empty union. */
    public boolean requiresCompanionModule() {
      return !this._template.getMembers().isEmpty();
    }

    private boolean _isEnclosedType() {
      return _template.getEnclosingClass() != null;
    }
  }

  /** The TS representation of a single field in a Record */
  public class FlowRecordFieldSyntax  implements FlowtypeTypeSyntax {
    private final RecordTemplateSpec _template;
    private final RecordDataSchema _schema;
    private final RecordTemplateSpec.Field _field;

    public FlowRecordFieldSyntax(RecordTemplateSpec _template, RecordTemplateSpec.Field _field) {
      this._template = _template;
      this._schema = _template.getSchema();
      this._field = _field;
    }

    @Override
    public Set<String> modulesRequiredToUse() {
      Set<String> modules = new HashSet<>();
      // since this record lives in its own file you have to import it to use it.
      modules.add(importString(_schema.getNamespace(), this.typeName()));
      return modules;
    }

    /** The typescript property for getting this field. */
    public String accessorName() {
      return escapeKeyword(_schemaField().getName(), EscapeStrategy.QUOTE);
    }

    public String typeName() {
      // To resolve type name we have to determine whether to use the DataSchema in _field.getType() or
      // the one in _field.getSchemaField().getType(). We reach first for the schemaField as it does not swallow
      // Typerefs. (e.g. if a type was defined as CustomInt, it will give us the string CustomInt, whereas
      // field.getType() would dereference all the way to the bottom).
      //
      // The only problem with schemaField is that it _does_ swallow the type names for enclosed unions. ARGH
      // can we catch a break?? Thankfully in the case of the enclosed union it ends up returning null, so
      // we back off to _field.getType() if schemaField returned null.
      FlowtypeTypeSyntax candidateSyntax = createTypeSyntax(_schemaField().getType());
      if (candidateSyntax.typeName() == null || "".equals(candidateSyntax)) {
        candidateSyntax = createTypeSyntax(_field.getType());
      }

      return typeNameQualifiedByEnclosingClass(candidateSyntax);
    }

    public String docString() {
      return docComment(
          _schemaField().getDoc(),
          _schemaField().getProperties().get("deprecated")
      );
    }

    /** The modules that the containing Record module has to import in order to compile. */
    public Set<String> typeModules() {
      return _fieldTypeSyntax().modulesRequiredToUse();
    }

    /**
     * Just returns a "?" if this was an optional field either due to being decalred optional, or opting not to pass
     * the STRICT directive into the generator.
     **/
    public String questionMarkIfOptional() {
      boolean isFieldOptional = _schemaField().getOptional();
      boolean markFieldAsOptional = isFieldOptional || FlowtypeProperties.optionality == Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT;

      return markFieldAsOptional? "?": "";
    }

    //
    // Private members
    //
    private RecordDataSchema.Field _schemaField() {
      return _field.getSchemaField();
    }
    private FlowtypeTypeSyntax _fieldTypeSyntax() {
      return createTypeSyntax(_schemaField().getType());
    }
  }

  /** TS-specific syntax for Records */
  public class FlowtypeRecordSyntax implements FlowtypeTypeSyntax {
    private final RecordTemplateSpec _template;
    private final RecordDataSchema _schema;
    private final FlowtypeNamedTypeSyntax _namedTypeSyntax;

    public FlowtypeRecordSyntax(RecordTemplateSpec _template) {
      this._template = _template;
      this._schema = _template.getSchema();
      this._namedTypeSyntax = new FlowtypeNamedTypeSyntax(_schema);
    }

    public String docString() {
      return _namedTypeSyntax.docString();
    }

    public List<FlowRecordFieldSyntax> fields() {
      List<FlowRecordFieldSyntax> fields = new ArrayList<>();

      for (RecordTemplateSpec.Field fieldSpec: _template.getFields()) {
        fields.add(new FlowRecordFieldSyntax(_template, fieldSpec));
      }

      return fields;
    }

    public Set<FlowtypeUnionSyntax> enclosedUnions() {
      Set<FlowtypeUnionSyntax> unions = new HashSet<>();
      for (ClassTemplateSpec spec: ClassTemplateSpecs.allContainedTypes(_template)) {
        if (spec instanceof UnionTemplateSpec) {
          unions.add(new FlowtypeUnionSyntax((UnionTemplateSpec) spec));
        }
      }

      return unions;
    }

    @Override
    public Set<String> modulesRequiredToUse() {
      return _namedTypeSyntax.modulesRequiredToUse();
    }

    public String typeName() {
      return escapeKeyword(_schema.getName(), EscapeStrategy.MANGLE);
    }

    /**
     * Returns true if a companion module needs to be declared for this record's interface. This is true if the record
     * has enclosing types that must be defined within the record's namespace.
     **/
    public boolean requiresCompanionModule() {
      return !ClassTemplateSpecs.allContainedTypes(_template).isEmpty();
    }

    /** The complete typescript import block for this record */
    public String imports() {
      Set<String> imports = new HashSet<>();

      for (FlowRecordFieldSyntax fieldSyntax: this.fields()) {
        imports.addAll(fieldSyntax.typeModules());
      }

      for (FlowtypeUnionSyntax union: this.enclosedUnions()) {
        for (FlowtypeUnionMemberSyntax unionMember: union.members()) {
          imports.addAll(unionMember.typeModules());
        }
      }

      return flattenImports(imports);
    }
  }

  /** Flowtype syntax for typerefs. */
  public class FlowtypeTyperefSyntax implements FlowtypeTypeSyntax {
    private final TyperefTemplateSpec _template;
    private final TyperefDataSchema _dataSchema;
    private final FlowtypeNamedTypeSyntax _namedTypeSyntax;

    public FlowtypeTyperefSyntax(TyperefTemplateSpec _template, TyperefDataSchema _dataSchema, FlowtypeNamedTypeSyntax _namedTypeSyntax) {
      this._template = _template;
      this._dataSchema = _dataSchema;
      this._namedTypeSyntax = _namedTypeSyntax;
    }

    public String docString() {
      return _namedTypeSyntax.docString();
    }

    @Override
    public Set<String> modulesRequiredToUse() {
      return _namedTypeSyntax.modulesRequiredToUse();
    }

    public String typeName() {
      // Have to use _dataSchema.getName() instead of _template.getClassName() here because otherwise
      // generics will return strings like Array<null> instead of Array<CustomTyperefName>. Not sure why??
      return escapeKeyword(_dataSchema.getName(), EscapeStrategy.MANGLE);
    }

    /** The type that this typeref refers to. */
    public String refTypeName() {
      return createTypeSyntax(_refType()).typeName();
    }

    /** Import block for this typeref's module file */
    public String imports() {
      // Gotta import the referenced type in order to compile this typeref's own module
      Set<String> refTypeImport = createTypeSyntax(_refType()).modulesRequiredToUse();
      return flattenImports(refTypeImport);
    }

    //
    // Private members
    //
    private ClassTemplateSpec _refType() {
      return ClassTemplateSpec.createFromDataSchema(_dataSchema.getRef());
    }
  }

  /** Create a new TyperefSyntax */
  public FlowtypeTyperefSyntax FlowtypeTyperefSyntaxCreate(TyperefTemplateSpec template) {
    return new FlowtypeTyperefSyntax(template, template.getSchema(), new FlowtypeNamedTypeSyntax(template.getSchema()));
  }

  /** TS syntax for the symbol of an enum */
  public class FlowtypeEnumSymbolSyntax {
    private final EnumTemplateSpec _template;
    private final EnumDataSchema _dataSchema;
    private final String _symbolString;

    public FlowtypeEnumSymbolSyntax(EnumTemplateSpec _template, EnumDataSchema _dataSchema, String _symbolString) {
      this._template = _template;
      this._dataSchema = _dataSchema;
      this._symbolString = _symbolString;
    }

    /**
     * Returns the quoted value that will be transmitted for this enum over the wire.
     *
     * Used to make a string-literal union representing the enum.
     **/
    public String stringLiteralValue() {
      return "\"" + _symbolString + "\"";
    }

    /**
     * Returns a variable name that can represent the enum value. Will be used to make something like
     * const PINEAPPLE: Fruits = "PINEAPPLE";
     */
    public String moduleConstValue() {
      return escapeKeyword(_symbolString, EscapeStrategy.MANGLE);
    }

    public String docString() {
      String symbolDoc = _dataSchema.getSymbolDocs().get(_symbolString);
      DataMap deprecatedSymbols = (DataMap) _dataSchema.getProperties().get("deprecatedSymbols");
      Object symbolDeprecation = null;

      if (deprecatedSymbols != null) {
        symbolDeprecation = deprecatedSymbols.get(_symbolString);
      }
      return docComment(
          symbolDoc,
          symbolDeprecation
      );
    }
  }

  /** TS syntax for enumerations. {@link FlowtypeEnumSymbolSyntax}. */
  public class FlowtypeEnumSyntax  implements FlowtypeTypeSyntax {
    private final EnumTemplateSpec _template;
    private final EnumDataSchema _dataSchema;
    private final FlowtypeNamedTypeSyntax _namedTypeSyntax;

    public FlowtypeEnumSyntax(EnumTemplateSpec _template) {
      this._template = _template;
      this._dataSchema = _template.getSchema();
      this._namedTypeSyntax = new FlowtypeNamedTypeSyntax(_dataSchema);
    }

    public String typeName() {
      return _namedTypeSyntax.typeName();
    }

    public String docString() {
      return _namedTypeSyntax.docString();
    }

    /**
     * Returns true in the usual case that we need a module with the same name as this type in which to house
     * the enum's constants.
     **/
    public boolean requiresCompanionModule() {
      return this.symbols().size() > 0;
    }

    /**
     * Creates the string literal union for this enum.
     *
     * e.g. for Fruits { APPLE, ORANGE } it will produce the following valid typescript:
     *
     * "APPLE" | "ORANGE"
     **/
    public String stringLiteralUnion() {
      List<FlowtypeEnumSymbolSyntax> symbols = this.symbols();
      if (symbols.size() == 0) {
        return "void"; // Helps us compile if some bozo declared an empty union.
      } else {
        return this._interleaveSymbolStrings(" | ");
      }
    }

    /**
     * Creates the typescript array literal for all values of this enum.
     * e.g. for Fruits { APPLE, ORANGE } it will produce the following valid typescript:
     *
     * ["APPLE", "ORANGE"]
     */
    public String arrayLiteral() {
      return "[" + this._interleaveSymbolStrings(", ") + "]";
    }

    @Override
    public Set<String> modulesRequiredToUse() {
      // Since this sucker is declared in its own file you've gotta import it to use it.
      return _namedTypeSyntax.modulesRequiredToUse();
    }

    /** Syntax for all the values in this enum */
    public List<FlowtypeEnumSymbolSyntax> symbols() {
      List<FlowtypeEnumSymbolSyntax> symbols = new ArrayList<>();
      for (String symbol : _dataSchema.getSymbols()) {
        symbols.add(new FlowtypeEnumSymbolSyntax(_template, _dataSchema, symbol));
      }
      return symbols;
    }

    private String _interleaveSymbolStrings(String delimiter) {
      List<FlowtypeEnumSymbolSyntax> symbols = this.symbols();

      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < symbols.size(); i++) {
        FlowtypeEnumSymbolSyntax symbol = symbols.get(i);
        boolean isLast = (i + 1 == symbols.size());
        sb.append(symbol.stringLiteralValue());

        if (!isLast) {
          sb.append(delimiter);
        }
      }
      return sb.toString();
    }
  }
}
