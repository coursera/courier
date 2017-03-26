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

package org.coursera.courier.py3;

import com.linkedin.data.DataMap;
import com.linkedin.data.avro.SchemaTranslator;
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

import java.util.*;

/**
 * Main work-horse for populating the ts-lite Rythm templates.
 *
 * Most work delegates to inner classes, so you probably want to look them (linked below)
 *
 * Specifically, {@link Py3EnumSyntax}, {@link Py3UnionSyntax}, {@link Py3RecordSyntax}, and {@link Py3TyperefSyntax} are
 * used directly to populate the templates.
 *
 * @see Py3PrimitiveTypeSyntax
 * @see Py3EnumSyntax
 * @see Py3ArraySyntax
 * @see Py3MapSyntax
 * @see Py3TyperefSyntax
 * @see Py3RecordSyntax
 * @see Py3FixedSyntax
 * @see Py3RecordSyntax
 * @see Py3UnionSyntax
 */
public class Py3Syntax {

  /** Config properties passed from the command line parser */
  private final Py3Properties Py3Properties;
  private static String TRIPLE_QUOTE = "\"\"\"";

  public Py3Syntax(Py3Properties Py3Properties) {
    this.Py3Properties = Py3Properties;
  }

  /**
   * Varying levels of reserved keywords copied from https://docs.python.org/3.3/reference/lexical_analysis.html
   **/
  private static final Set<String> tsKeywords = new HashSet<String>(Arrays.asList(new String[]{
    // Python-Reserved Words
    "False",
    "class",
    "finally",
    "is",
    "return",
    "None",
    "continue",
    "for",
    "lambda",
    "try",
    "True",
    "def",
    "from",
    "nonlocal",
    "while",
    "and",
    "del",
    "global",
    "not",
    "with",
    "as",
    "elif",
    "if",
    "or",
    "yield",
    "assert",
    "else",
    "import",
    "pass",
    "break",
    "except",
    "in",
    "raise",

    // Keywords reserved by this implementation
    "data"
  }));


  /** Different choices for how to escaping symbols that match reserved ts keywords. */
  public static enum EscapeStrategy {
    /** Adds an underscore sign after the symbol name when escaping. e.g.: class becomes class$ */
    MANGLE
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
  public static String escapeKeyword(String symbol, EscapeStrategy strategy) {
    if (tsKeywords.contains(symbol)) {
      if (strategy.equals(EscapeStrategy.MANGLE)) {
        return symbol + "_";
      } else {
        return "\"" + symbol + "\"";
      }
    } else {
      return symbol;
    }
  }

  private static class Py3Import {
    private final String _symbolToImport;
    private final String _moduleToImport;
    private final ImportStrategy _importStrategy;

    public Py3Import(String symbolToImport, String moduleToImport, ImportStrategy importStrategy) {
      this._symbolToImport = symbolToImport;
      this._moduleToImport = moduleToImport;
      this._importStrategy = importStrategy;
    }

    public String relativeImport(String importingNamespace) {
      importingNamespace = importingNamespace == null? "": importingNamespace;
      int termsInImportingNamespace = importingNamespace.isEmpty()? 0: importingNamespace.split("\\.").length;

      StringBuilder dotsBuilder = new StringBuilder(".");
      for (int i = 0; i < termsInImportingNamespace; i++) {
        dotsBuilder.append('.');
      }
      String dots = dotsBuilder.toString();

      String importString;
      switch (this._importStrategy) {
        case MODULE_AS_SELF:
          importString = "from " + dots + " import " + this._symbolToImport + " as " + this._symbolToImport;
          break;
        case TYPE_FROM_EPONYMOUS_MODULE:
          importString = "from " + dots + this._moduleToImport + "." + this._symbolToImport + " import " + this._symbolToImport;
          break;
        default:
          throw new IllegalArgumentException(this._importStrategy + " is not a valid strategy");
      }
      return importString;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Py3Import that = (Py3Import) o;

      if (!_symbolToImport.equals(that._symbolToImport)) return false;
      return _moduleToImport.equals(that._moduleToImport);
    }

    @Override
    public int hashCode() {
      int result = _symbolToImport.hashCode();
      result = 31 * result + _moduleToImport.hashCode();
      return result;
    }

    /**
     * Takes a set of imports constructed with {@link #importString}, and produces a valid import block
     * for use at the top of a typescript source file
     *
     * @param imports the set of imports, each of which is a valid import line in typescript
     * @return the import block, on separate lines.
     */
    public static String relativeImportBlock(Collection<Py3Import> imports, String importingNamespace) {
      StringBuilder sb = new StringBuilder();

      for (Py3Import import_: imports) {
        sb.append(import_.relativeImport(importingNamespace)).append("\n");
      }

      return sb.toString();
    }

    static Py3Import COURIER_RUNTIME = new Py3Import("courier", "", ImportStrategy.MODULE_AS_SELF);
    public static enum ImportStrategy {
      TYPE_FROM_EPONYMOUS_MODULE, MODULE_AS_SELF
    }
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
    if (doc != null && !"".equals(doc)) {
      StringBuilder docStr = new StringBuilder(TRIPLE_QUOTE);

      docStr.append(doc.trim());

      if (deprecation != null) {
        docStr.append("\n\n").append("@deprecated");
        if (deprecation instanceof String) {
          docStr.append("  ").append(((String)deprecation).trim());
        }
      }
      docStr.append(TRIPLE_QUOTE);

      return docStr.toString();
    } else {
      return "";
    }
  }

  private String _toAvroSchemaJsonSafe(DataSchema dataSchema) {
    try {
      return _tripleQuote(SchemaTranslator.dataToAvroSchemaJson(dataSchema));
    } catch (Exception e) {
      e.printStackTrace();
      return "\"\"";
    }
  }

  /** Describes any type we are representing in the generated python */
  interface Py3TypeSyntax {

    /** Return the simple name of the type, in valid typescript. "number" or "string" for example. */
    public String typeName();

    public String constructor();

    /**
     * Return the set of modules that must be imported in order for some other module
     * to use this type.
     **/
    public Set<Py3Import> modulesRequiredToUse();
  }

  /**
   * Describes any type that can be enclosed by another. According to the restli spec this only applies
   * to anonymous unions. https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates
   **/
  private interface Py3EnclosedTypeSyntax {
    public String typeNameQualifiedByEnclosedType();
    public String constructorQualifiedByEnclosedType();
  }

  private interface Py3CollectionSyntax {
    public String collectionTypeName();
  }

  /**
   * Create a Py3*Syntax class around the provided ClassTemplate.
   *
   * That class will perform the heavy lifting of rendering Py3-specific strings into the template.
   *
   * @param template the ClassTemplate
   * @return a Py3*Syntax class (see {@link Py3Syntax} class-level docs for more info)
   */
  private Py3TypeSyntax createTypeSyntax(ClassTemplateSpec template) {
    if (template instanceof RecordTemplateSpec) {
      return new Py3RecordSyntax((RecordTemplateSpec) template);
    } else if (template instanceof TyperefTemplateSpec) {
      return Py3TyperefSyntaxCreate((TyperefTemplateSpec) template);
    } else if (template instanceof FixedTemplateSpec) {
      return Py3FixedSyntaxCreate((FixedTemplateSpec) template);
    } else if (template instanceof EnumTemplateSpec) {
      return new Py3EnumSyntax((EnumTemplateSpec) template);
    } else if (template instanceof PrimitiveTemplateSpec) {
      return new Py3PrimitiveTypeSyntax((PrimitiveTemplateSpec) template);
    } else if (template instanceof MapTemplateSpec) {
      return new Py3MapSyntax((MapTemplateSpec) template);
    } else if (template instanceof ArrayTemplateSpec) {
      return new Py3ArraySyntax((ArrayTemplateSpec) template);
    } else if (template instanceof UnionTemplateSpec) {
      return new Py3UnionSyntax((UnionTemplateSpec) template);
    } else {
      throw new RuntimeException("Unrecognized template spec: " + template + " with schema " + template.getSchema());
    }
  }

  /** Convenience wrapper around {@link #createTypeSyntax(ClassTemplateSpec)}. */
  private Py3TypeSyntax createTypeSyntax(DataSchema schema) {
    return createTypeSyntax(ClassTemplateSpec.createFromDataSchema(schema));
  }

  /**
   * Returns the type name, prefaced with the enclosing class name if there was one.
   *
   * For example, a standalone union called MyUnion will just return "MyUnion".
   * If that same union were enclosed within MyRecord, this would return "MyRecord.MyUnion".
   **/
  String typeNameQualifiedByEnclosingClass(Py3TypeSyntax syntax) {
    if (syntax instanceof Py3EnclosedTypeSyntax) {
      return ((Py3EnclosedTypeSyntax) syntax).typeNameQualifiedByEnclosedType();
    } else {
      return syntax.typeName();
    }
  }

  String constructorQualifiedByEnclosingClass(Py3TypeSyntax syntax) {
    if (syntax instanceof Py3EnclosedTypeSyntax) {
      return ((Py3EnclosedTypeSyntax) syntax).constructorQualifiedByEnclosedType();
    } else {
      return syntax.constructor();
    }
  }

  private String _tripleQuote(String toQuote) {
    return TRIPLE_QUOTE + toQuote + TRIPLE_QUOTE;
  }

  /** Py3-specific syntax for Maps */
  private class Py3MapSyntax implements Py3TypeSyntax, Py3CollectionSyntax {
    private final MapTemplateSpec _template;

    Py3MapSyntax(MapTemplateSpec _template) {
      this._template = _template;
    }

    @Override
    public String typeName() {
      // (This comment is duplicated from Py3ArraySyntax.typeName for your benefit)
      // Sadly the behavior of this function is indirectly controlled by the one calling it: Py3RecordFieldSyntax.
      // That class has the unfortunate behavior that it can produce 2 different ClassTemplateSpecs, one of which works for
      // some cases, and one of which works for the others. See its own "typeName" definition for details but essentially
      // it will give us one of the ClassTemplateSpecs and call typeName. If we then return null
      // then it will give it a shot with the other ClassTemplateSpec. Unfortunately we have to do this because if
      // we try to just use the first one, we will return "Map<null>". This is also why we special-case unions here.
      // we have to access a specific ClassTemplate
      boolean valueIsUnion = _template.getValueClass() instanceof UnionTemplateSpec;
      Py3TypeSyntax itemTypeSyntax = valueIsUnion? createTypeSyntax(_template.getValueClass()): _valueTypeSyntax();
      String valueTypeName = typeNameQualifiedByEnclosingClass(itemTypeSyntax);
      return valueTypeName == null? null: valueTypeName;
    }

    @Override
    public String collectionTypeName() {
      return "map";
    }

    @Override
    public String constructor() {
      // Sadly the behavior of this function is indirectly controlled by the one calling it: Py3RecordFieldSyntax.
      // That class has the unfortunate behavior that it can produce 2 different ClassTemplateSpecs, one of which works for
      // some cases, and one of which works for the others. See its own "typeName" definition for details but essentially
      // it will give us one of the ClassTemplateSpecs and call typeName. If we then return null
      // then it will give it a shot with the other ClassTemplateSpec. Unfortunately we have to do this because if
      // we try to just use the first one, we will return "Array<null>". This is also why we special-case unions here.
      // we have to access a specific ClassTemplate
      boolean itemIsUnion = _template.getValueClass() instanceof UnionTemplateSpec;
      Py3TypeSyntax itemTypeSyntax = itemIsUnion? createTypeSyntax(_template.getValueClass()): _valueTypeSyntax();
      String itemTypeName = typeNameQualifiedByEnclosingClass(itemTypeSyntax);
      return itemTypeName == null? null: "courier.map(" + itemTypeName + ")";
    }

    @Override
    public Set<Py3Import> modulesRequiredToUse() {
      Set<Py3Import> requiredForUse = new HashSet<>();

      requiredForUse.addAll(_valueTypeSyntax().modulesRequiredToUse());
      requiredForUse.add(Py3Import.COURIER_RUNTIME);

      return requiredForUse;
    }

    //
    // Private Py3MapSyntax members
    //
    private Py3TypeSyntax _valueTypeSyntax() {
      return createTypeSyntax(_template.getSchema().getValues());
    }
  }

  /** Py3-specific syntax for Arrays */
  private class Py3ArraySyntax implements Py3TypeSyntax, Py3CollectionSyntax {
    private final ArrayTemplateSpec _template;

    Py3ArraySyntax(ArrayTemplateSpec _template) {
      this._template = _template;
    }

    @Override
    public String typeName() {
      // Sadly the behavior of this function is indirectly controlled by the one calling it: Py3RecordFieldSyntax.
      // That class has the unfortunate behavior that it can produce 2 different ClassTemplateSpecs, one of which works for
      // some cases, and one of which works for the others. See its own "typeName" definition for details but essentially
      // it will give us one of the ClassTemplateSpecs and call typeName. If we then return null
      // then it will give it a shot with the other ClassTemplateSpec. Unfortunately we have to do this because if
      // we try to just use the first one, we will return "Array<null>". This is also why we special-case unions here.
      // we have to access a specific ClassTemplate
      boolean itemIsUnion = _template.getItemClass() instanceof UnionTemplateSpec;
      Py3TypeSyntax itemTypeSyntax = itemIsUnion? createTypeSyntax(_template.getItemClass()): _itemTypeSyntax();
      String itemTypeName = typeNameQualifiedByEnclosingClass(itemTypeSyntax);
      return itemTypeName == null? null: itemTypeName;
    }

    @Override
    public String collectionTypeName() {
      return "array";
    }

    @Override
    public String constructor() {
      // Sadly the behavior of this function is indirectly controlled by the one calling it: Py3RecordFieldSyntax.
      // That class has the unfortunate behavior that it can produce 2 different ClassTemplateSpecs, one of which works for
      // some cases, and one of which works for the others. See its own "typeName" definition for details but essentially
      // it will give us one of the ClassTemplateSpecs and call typeName. If we then return null
      // then it will give it a shot with the other ClassTemplateSpec. Unfortunately we have to do this because if
      // we try to just use the first one, we will return "Array<null>". This is also why we special-case unions here.
      // we have to access a specific ClassTemplate
      boolean itemIsUnion = _template.getItemClass() instanceof UnionTemplateSpec;
      Py3TypeSyntax itemTypeSyntax = itemIsUnion? createTypeSyntax(_template.getItemClass()): _itemTypeSyntax();
      String itemTypeName = constructorQualifiedByEnclosingClass(itemTypeSyntax);
      return itemTypeName == null? null: "courier.array(" + itemTypeName + ")";
    }

    @Override
    public Set<Py3Import> modulesRequiredToUse() {
      Set<Py3Import> requiredForUse = new HashSet<>();

      requiredForUse.addAll(_itemTypeSyntax().modulesRequiredToUse());
      requiredForUse.add(Py3Import.COURIER_RUNTIME);

      return requiredForUse;
    }

    //
    // Private Py3ArraySyntax members
    //
    private Py3TypeSyntax _itemTypeSyntax() {
      return createTypeSyntax(_template.getSchema().getItems());
    }
  }

  /** Pegasus types that should be rendered as "number" in typescript */
  private static final Set<Type> Py3_NUMBER_TYPES = new HashSet<>(
      Arrays.asList(
          new Type[] { Type.INT, Type.LONG, Type.FLOAT, Type.DOUBLE }
      )
  );

  /** Pegasus types that should be rendered as "string" in typescript */
  private static final Set<Type> Py3_STRING_TYPES = new HashSet<>(
      Arrays.asList(
          new Type[] { Type.STRING, Type.BYTES, Type.FIXED }
      )
  );

  /** Py3-specific syntax for all primitive types: Integer, Long, Float, Double, Boolean, String, Byte. */
  private class Py3PrimitiveTypeSyntax implements Py3TypeSyntax {
    private final PrimitiveTemplateSpec _template;
    private final PrimitiveDataSchema _schema;

    Py3PrimitiveTypeSyntax(PrimitiveTemplateSpec _template) {
      this._template = _template;
      this._schema = _template.getSchema();
    }

    @Override
    public String typeName() {
      Type schemaType = _schema.getType();
      if (schemaType == Type.INT || schemaType == Type.LONG) {
        return "int"; // unlike py2, py3 has no 'long' type
      } else if (schemaType == Type.DOUBLE) {
        return "float";
      } else if (schemaType == Type.FLOAT) {
        return "float";
      } else if (Py3_STRING_TYPES.contains(schemaType)) {
        return "str";
      } else if (schemaType == Type.BOOLEAN) {
        return "bool";
      } else {
        throw new IllegalArgumentException("Unexpected type " + schemaType + " in schema " + _schema);
      }
    }

    @Override
    public String constructor() {
      return this.typeName();
    }

    @Override
    public Set<Py3Import> modulesRequiredToUse() {
      return new HashSet<>(); // using a primitive requires no imports
    }
  }

  /**
   * Helper class that more-or-less wraps {@link NamedDataSchema}.
   *
   * Helps reduce code bloat for Records, Enums, and Typerefs.
   **/
  private class Py3NamedTypeSyntax {
    private final NamedDataSchema _dataSchema;

    public Py3NamedTypeSyntax(NamedDataSchema _dataSchema) {
      this._dataSchema = _dataSchema;
    }

    public String typeName() {
      return Py3Syntax.escapeKeyword(this._dataSchema.getName(), EscapeStrategy.MANGLE);
    }

    public String docString() {
      return docComment(
          _dataSchema.getDoc(),
          _dataSchema.getProperties().get("deprecated")
      );
    }

    public Set<Py3Import> modulesRequiredToUse() {
      Set<Py3Import> modules = new HashSet<>();
      // Named types get their own files, so you have to import them in order to use them.
      Py3Import theImport = new Py3Import(
        _dataSchema.getName(),
        _dataSchema.getNamespace(),
        Py3Import.ImportStrategy.TYPE_FROM_EPONYMOUS_MODULE
      );
      modules.add(theImport);
      return modules;
    }
  }

  /** Py3 syntax for Fixed types. */
  public class Py3FixedSyntax  implements Py3TypeSyntax {
    private final FixedTemplateSpec _template;
    private final Py3NamedTypeSyntax _namedSyntax;

    public Py3FixedSyntax(FixedTemplateSpec template, Py3NamedTypeSyntax namedSyntax) {
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
    public String constructor() {
      return this.typeName();
    }

    @Override
    public Set<Py3Import> modulesRequiredToUse() {
      return _namedSyntax.modulesRequiredToUse();
    }
  }

  /** Create a new Py3FixedSyntax */
  public Py3FixedSyntax Py3FixedSyntaxCreate(FixedTemplateSpec template) {
    return new Py3FixedSyntax(template, new Py3NamedTypeSyntax(template.getSchema()));
  }

  /**
   * Py3 representation of a Union type's member (e.g. the "int" in "union[int]").
   */
  public class Py3UnionMemberSyntax {
    private final Py3UnionSyntax _parentSyntax;
    private final UnionDataSchema _schema;
    private final UnionTemplateSpec.Member _member;

    public Py3UnionMemberSyntax(Py3UnionSyntax _parentSyntax, UnionDataSchema _schema, UnionTemplateSpec.Member _member) {
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

    public String accessorName() {
      String accessorTypeName = this.typeName();
      Py3TypeSyntax memberSyntax = this._memberTypeSyntax();
      if (memberSyntax instanceof Py3CollectionSyntax) {
        accessorTypeName += "_" + ((Py3CollectionSyntax) memberSyntax).collectionTypeName();
      }
      return "as_" + accessorTypeName;
    }
    public String constructor() {
      return _memberTypeSyntax().constructor();
    }

    /** The set of modules imports that need to be included in order to use the type represented by this union member */
    Set<Py3Import> typeModules() {
      return _memberTypeSyntax().modulesRequiredToUse();
    }

    //
    // Private UnionMemberSyntax members
    //
    private DataSchema _memberSchema() {
      return _member.getSchema();
    }
    private Py3TypeSyntax _memberTypeSyntax() {
      return createTypeSyntax(_member.getSchema());
    }
  }

  /** Py3-specific representation of a Union type. */
  public class Py3UnionSyntax implements Py3TypeSyntax, Py3EnclosedTypeSyntax {
    private final UnionTemplateSpec _template;
    private final UnionDataSchema _schema;

    public Py3UnionSyntax(UnionTemplateSpec _template) {
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
    public String constructorQualifiedByEnclosedType() {
      if (_template.getEnclosingClass() != null) {
        return createTypeSyntax(_template.getEnclosingClass()).constructor() + "." + this.constructor();
      } else {
        return this.constructor();
      }
    }

    @Override
    public String typeName() {
      if (_template.getTyperefClass() != null) {
        // If this union was typerefed then just use the typeref name
        Py3TyperefSyntax refSyntax = Py3TyperefSyntaxCreate(_template.getTyperefClass());
        return refSyntax.typeName();
      } else {
        // I actually never figured out why this works, so I'm very sorry if you're dealing
        // with the repercussions here.
        return escapeKeyword(this._template.getClassName(), EscapeStrategy.MANGLE);
      }
    }

    @Override
    public String constructor() {
      return this.typeName();
    }

    public String avroSchemaJson() {
      return _toAvroSchemaJsonSafe(this._schema);
    }

    /** Return the whole python import block for the file in which this union is declared. */
    public String imports() {
      Set<Py3Import> allImports = new HashSet<>();

      // Only print out the imports for non-enclosed union types. Enclosed ones will be handled
      // by the enclosing record.
      if (!_isEnclosedType()) {
        allImports.add(Py3Import.COURIER_RUNTIME);

        for (Py3UnionMemberSyntax member: this.members()) {
          allImports.addAll(member.typeModules());
        }

      }

      return Py3Import.relativeImportBlock(allImports, this._template.getNamespace());
    }

    @Override
    public Set<Py3Import> modulesRequiredToUse() {
      Set<Py3Import> modules = new HashSet<>();
      // enclosed types dont report modules -- their enclosing types will do so for them!
      if (!_isEnclosedType() && this.typeName() != null) {
        modules.add(
          new Py3Import(
            this.typeName(),
            this._template.getNamespace(),
            Py3Import.ImportStrategy.TYPE_FROM_EPONYMOUS_MODULE
          )
        );
      }
      return modules;
    }

    public String docString() {
      if (this._template.getTyperefClass() != null) {
        return new Py3NamedTypeSyntax(this._template.getTyperefClass().getSchema()).docString();
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

      List<Py3UnionMemberSyntax> members = this.members();
      for (int i = 0; i < members.size(); i++) {
        boolean isLast = (i == members.size() - 1);
        Py3UnionMemberSyntax member = members.get(i);
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
      List<Py3UnionMemberSyntax> members = this.members();

      if (members.isEmpty()) {
        return "void";
      } else {
        StringBuilder sb = new StringBuilder();


        for (int i = 0; i < members.size(); i++) {
          boolean isLast = (i == members.size() - 1);
          Py3UnionMemberSyntax member = members.get(i);
          sb.append(member.fullUnionMemberTypeName());

          if (!isLast) {
            sb.append(" | ");
          }
        }

        return sb.toString();
      }
    }

    /** Return the syntax for each member */
    public List<Py3UnionMemberSyntax> members() {
      List<Py3UnionMemberSyntax> memberSyntax = new ArrayList<>();

      for (UnionTemplateSpec.Member member : this._template.getMembers()) {
        memberSyntax.add(new Py3UnionMemberSyntax(this, _schema, member));
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

  /** The Py3 representation of a single field in a Record */
  public class Py3RecordFieldSyntax  implements Py3TypeSyntax {
    private final RecordTemplateSpec _template;
    private final RecordDataSchema _schema;
    private final RecordTemplateSpec.Field _field;

    public Py3RecordFieldSyntax(RecordTemplateSpec _template, RecordTemplateSpec.Field _field) {
      this._template = _template;
      this._schema = _template.getSchema();
      this._field = _field;
    }

    @Override
    public Set<Py3Import> modulesRequiredToUse() {
      Set<Py3Import> modules = new HashSet<>();
      // since this record lives in its own file you have to import it to use it.
      modules.add(new Py3Import(this._schema.getNamespace(), this.typeName(), Py3Import.ImportStrategy.TYPE_FROM_EPONYMOUS_MODULE));
      return modules;
    }

    /** The typescript property for getting this field. */
    public String accessorName() {
      return escapeKeyword(_schemaField().getName(), EscapeStrategy.MANGLE);
    }

    public String jsonKey() {
      return _schemaField().getName();
    }

    public String typeName() {
      return typeNameQualifiedByEnclosingClass(this._fieldSyntax());
    }

    @Override
    public String constructor() {
      return constructorQualifiedByEnclosingClass(this._fieldSyntax());
    }

    public String constructorDefault() {
      return _schemaField().getOptional()? "courier.OPTIONAL": "courier.REQUIRED";
    }

    public String docString() {
      return docComment(
          _schemaField().getDoc(),
          _schemaField().getProperties().get("deprecated")
      );
    }

    /** The modules that the containing Record module has to import in order to compile. */
    public Set<Py3Import> typeModules() {
      return _fieldTypeSyntax().modulesRequiredToUse();
    }

    /**
     * Just returns a "?" if this was an optional field either due to being decalred optional, or opting not to pass
     * the STRICT directive into the generator.
     **/
    public String questionMarkIfOptional() {
      boolean isFieldOptional = _schemaField().getOptional();
      boolean markFieldAsOptional = isFieldOptional || Py3Properties.optionality == org.coursera.courier.py3.Py3Properties.Optionality.REQUIRED_FIELDS_MAY_BE_ABSENT;

      return markFieldAsOptional? "?": "";
    }

    //
    // Private members
    //
    private RecordDataSchema.Field _schemaField() {
      return _field.getSchemaField();
    }
    private Py3TypeSyntax _fieldTypeSyntax() {
      return createTypeSyntax(_schemaField().getType());
    }
    private Py3TypeSyntax _fieldSyntax() {
      // To resolve type name we have to determine whether to use the DataSchema in _field.getType() or
      // the one in _field.getSchemaField().getType(). We reach first for the schemaField as it does not swallow
      // Typerefs. (e.g. if a type was defined as CustomInt, it will give us the string CustomInt, whereas
      // field.getType() would dereference all the way to the bottom).
      //
      // The only problem with schemaField is that it _does_ swallow the type names for enclosed unions. ARGH
      // can we catch a break?? Thankfully in the case of the enclosed union it ends up returning null, so
      // we back off to _field.getType() if schemaField returned null.
      Py3TypeSyntax candidateSyntax = createTypeSyntax(_schemaField().getType());
      if (candidateSyntax.typeName() == null || "".equals(candidateSyntax)) {
        candidateSyntax = createTypeSyntax(_field.getType());
      }

      return candidateSyntax;
    }
  }

  /** Py3-specific syntax for Records */
  public class Py3RecordSyntax implements Py3TypeSyntax {
    private final RecordTemplateSpec _template;
    private final RecordDataSchema _schema;
    private final Py3NamedTypeSyntax _namedTypeSyntax;

    public Py3RecordSyntax(RecordTemplateSpec _template) {
      this._template = _template;
      this._schema = _template.getSchema();
      this._namedTypeSyntax = new Py3NamedTypeSyntax(_schema);
    }

    public String docString() {
      return _namedTypeSyntax.docString();
    }

    public List<Py3RecordFieldSyntax> fields() {
      List<Py3RecordFieldSyntax> fields = new ArrayList<>();

      for (RecordTemplateSpec.Field fieldSpec: _template.getFields()) {
        fields.add(new Py3RecordFieldSyntax(_template, fieldSpec));
      }

      return fields;
    }

    public Set<Py3UnionSyntax> enclosedUnions() {
      Set<Py3UnionSyntax> unions = new HashSet<>();
      for (ClassTemplateSpec spec: ClassTemplateSpecs.allContainedTypes(_template)) {
        if (spec instanceof UnionTemplateSpec) {
          unions.add(new Py3UnionSyntax((UnionTemplateSpec) spec));
        }
      }

      return unions;
    }

    public String avroSchemaJson() {
      return _toAvroSchemaJsonSafe(this._schema);
    }

    @Override
    public Set<Py3Import> modulesRequiredToUse() {
      return _namedTypeSyntax.modulesRequiredToUse();
    }

    public String typeName() {
      return escapeKeyword(_schema.getName(), EscapeStrategy.MANGLE);
    }

    @Override
    public String constructor() {
      return this.typeName();
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
      Set<Py3Import> allImports = new HashSet<>();

      allImports.add(Py3Import.COURIER_RUNTIME);
      for (Py3RecordFieldSyntax fieldSyntax: this.fields()) {
        allImports.addAll(fieldSyntax.typeModules());
      }

      for (Py3UnionSyntax union: this.enclosedUnions()) {
        for (Py3UnionMemberSyntax unionMember: union.members()) {
          allImports.addAll(unionMember.typeModules());
        }
      }

      return Py3Import.relativeImportBlock(allImports, this._schema.getNamespace());
    }
  }

  /** Py3 syntax for typerefs. */
  public class Py3TyperefSyntax implements Py3TypeSyntax {
    private final TyperefTemplateSpec _template;
    private final TyperefDataSchema _dataSchema;
    private final Py3NamedTypeSyntax _namedTypeSyntax;

    public Py3TyperefSyntax(TyperefTemplateSpec _template, TyperefDataSchema _dataSchema, Py3NamedTypeSyntax _namedTypeSyntax) {
      this._template = _template;
      this._dataSchema = _dataSchema;
      this._namedTypeSyntax = _namedTypeSyntax;
    }

    public String docString() {
      return _namedTypeSyntax.docString();
    }

    @Override
    public Set<Py3Import> modulesRequiredToUse() {
      return _namedTypeSyntax.modulesRequiredToUse();
    }

    public String typeName() {
      // Have to use _dataSchema.getName() instead of _template.getClassName() here because otherwise
      // generics will return strings like Array<null> instead of Array<CustomTyperefName>. Not sure why??
      return escapeKeyword(_dataSchema.getName(), EscapeStrategy.MANGLE);
    }

    @Override
    public String constructor() {
      return this.typeName();
    }

    /** The type that this typeref refers to. */
    public String refTypeConstructor() {
      return createTypeSyntax(_refType()).constructor();
    }

    /** Import block for this typeref's module file */
    public String imports() {
      // Gotta import the referenced type in order to compile this typeref's own module
      Set<Py3Import> refTypeImport = createTypeSyntax(_refType()).modulesRequiredToUse();
      return Py3Import.relativeImportBlock(refTypeImport, this._template.getNamespace());
    }

    //
    // Private members
    //
    private ClassTemplateSpec _refType() {
      return ClassTemplateSpec.createFromDataSchema(_dataSchema.getRef());
    }
  }

  /** Create a new TyperefSyntax */
  public Py3TyperefSyntax Py3TyperefSyntaxCreate(TyperefTemplateSpec template) {
    return new Py3TyperefSyntax(template, template.getSchema(), new Py3NamedTypeSyntax(template.getSchema()));
  }

  /** Py3 syntax for the symbol of an enum */
  public class Py3EnumSymbolSyntax {
    private final EnumTemplateSpec _template;
    private final EnumDataSchema _dataSchema;
    private final String _symbolString;

    public Py3EnumSymbolSyntax(EnumTemplateSpec _template, EnumDataSchema _dataSchema, String _symbolString) {
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

  /** Py3 syntax for enumerations. {@link Py3EnumSymbolSyntax}. */
  public class Py3EnumSyntax  implements Py3TypeSyntax {
    private final EnumTemplateSpec _template;
    private final EnumDataSchema _dataSchema;
    private final Py3NamedTypeSyntax _namedTypeSyntax;

    public Py3EnumSyntax(EnumTemplateSpec _template) {
      this._template = _template;
      this._dataSchema = _template.getSchema();
      this._namedTypeSyntax = new Py3NamedTypeSyntax(_dataSchema);
    }

    public String typeName() {
      return _namedTypeSyntax.typeName();
    }

    @Override
    public String constructor() {
      return this.typeName() + ".from_data";
    }

    public String docString() {
      return _namedTypeSyntax.docString();
    }

    public String avroSchemaJson() {
      return _toAvroSchemaJsonSafe(this._dataSchema);
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
      List<Py3EnumSymbolSyntax> symbols = this.symbols();
      if (symbols.size() == 0) {
        return "void"; // Helps us compile if some bozo declared an empty union.
      } else {
        return this._interleaveSymbolStrings(" | ");
      }
    }

    /**
     * Creates the typescript array literal for all values of this enum.
     * e.g. for Fruits { APPLE, ORANGE } it will produce the following valid python:
     *
     * ["APPLE", "ORANGE"]
     */
    public String arrayLiteral() {
      return "[" + this._interleaveSymbolStrings(", ") + "]";
    }

    @Override
    public Set<Py3Import> modulesRequiredToUse() {
      // Since this sucker is declared in its own file you've gotta import it to use it.
      return _namedTypeSyntax.modulesRequiredToUse();
    }

    /** Syntax for all the values in this enum */
    public List<Py3EnumSymbolSyntax> symbols() {
      List<Py3EnumSymbolSyntax> symbols = new ArrayList<>();
      for (String symbol : _dataSchema.getSymbols()) {
        symbols.add(new Py3EnumSymbolSyntax(_template, _dataSchema, symbol));
      }
      return symbols;
    }

    public String imports() {
      return Py3Import.COURIER_RUNTIME.relativeImport(this._dataSchema.getNamespace());
    }

    private String _interleaveSymbolStrings(String delimiter) {
      List<Py3EnumSymbolSyntax> symbols = this.symbols();

      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < symbols.size(); i++) {
        Py3EnumSymbolSyntax symbol = symbols.get(i);
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
