/*
 * Copyright 2015 Coursera Inc.
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

package org.coursera.courier.api;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.ComplexDataSchema;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.DataSchemaLocation;
import com.linkedin.data.schema.DataSchemaResolver;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.FixedDataSchema;
import com.linkedin.data.schema.MapDataSchema;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.PrimitiveDataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaParser;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.schema.UnionDataSchema;
import com.linkedin.data.template.DataTemplate;
import com.linkedin.pegasus.generator.CodeUtil;
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.CustomInfoSpec;
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec;
import com.linkedin.pegasus.generator.spec.FixedTemplateSpec;
import com.linkedin.pegasus.generator.spec.ModifierSpec;
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Modified version of {@link com.linkedin.pegasus.generator.TemplateSpecGenerator} that
 * adds support for Courier specific functionality:
 *
 * <ul>
 *   <li>Typed map key support.</li>
 *   <li>Use 'scala' instead of 'java' for custom properties.</li>
 *   <li>Use 'org.coursera.courier.data' as base namespace.</li>
 * </ul>
 *
 * @author Keren Jin
 */
// TODO(jbetz): Submit pull requests to the rest.li project to provide extension points
// that we can use instead of keeping a modified version of this class.
public class CourierTemplateSpecGenerator {
  private static final Logger _log = LoggerFactory.getLogger(CourierTemplateSpecGenerator.class);

  private static final String CLASS_PROPERTY = "class";

  // For Courier, use 'scala' as the property name for custom type properties.
  private static final String SCALA_PROPERTY = "scala";
  private final String courierPackageName = "org.coursera.courier.data";

  private static final String COERCER_CLASS_PROPERTY = "coercerClass";
  private static final String ARRAY_SUFFIX = "Array";
  private static final String MAP_SUFFIX = "Map";
  private static final String[] SPECIAL_SUFFIXES = {ARRAY_SUFFIX, MAP_SUFFIX};
  private static final String _templatePackageName = DataTemplate.class.getPackage().getName();

  private final Collection<ClassTemplateSpec> _classTemplateSpecs = new HashSet<ClassTemplateSpec>();
  /**
   * Map of {@link com.linkedin.pegasus.generator.spec.ClassTemplateSpec} to {@link com.linkedin.data.schema.DataSchemaLocation}.
   */
  private final Map<ClassTemplateSpec, DataSchemaLocation> _classToDataSchemaLocationMap = new HashMap<ClassTemplateSpec, DataSchemaLocation>();
  /**
   * Map of Java class name to a {@link com.linkedin.data.schema.DataSchema}.
   */
  private final Map<String, DataSchema> _classNameToSchemaMap = new HashMap<String, DataSchema>(100);
  /**
   * Map of {@link com.linkedin.data.schema.DataSchema} to {@link com.linkedin.pegasus.generator.spec.ClassTemplateSpec}.
   */
  private final IdentityHashMap<DataSchema, ClassTemplateSpec> _schemaToClassMap = new IdentityHashMap<DataSchema, ClassTemplateSpec>(100);
  /**
   * Map of {@link com.linkedin.data.schema.DataSchema} to the information about the immediate dereferenced {@link com.linkedin.data.schema.DataSchema} with custom Java class binding.
   */
  private final Deque<DataSchemaLocation> _locationStack = new ArrayDeque<DataSchemaLocation>();
  private final Map<DataSchema, CustomInfoSpec> _immediateCustomMap = new IdentityHashMap<DataSchema, CustomInfoSpec>();

  private final DataSchemaResolver _schemaResolver;
  private final SchemaParser _schemaParser;
  private final String _dataNamespace;

  /**
   * Return Java class name for a {@link com.linkedin.data.schema.NamedDataSchema}.
   *
   * @param schema provides the {@link com.linkedin.data.schema.NamedDataSchema}.
   *
   * @return the fully qualified Java class name for the provided {@link com.linkedin.data.schema.NamedDataSchema}.
   */
  public static String classNameForNamedSchema(NamedDataSchema schema)
  {
    final StringBuilder sb = new StringBuilder();
    final String namespace = schema.getNamespace();
    if (!namespace.isEmpty())
    {
      sb.append(namespace);
      sb.append('.');
    }
    sb.append(schema.getName());
    return sb.toString();
  }

  /**
   * @param dataNamespace Provides the namespace to use for generated classes that have no other
   *                      reasonable default namespace.  E.g. An generated type for array of ints
   *                      would be generated in this namespace.
   */
  public CourierTemplateSpecGenerator(DataSchemaResolver schemaResolver, String dataNamespace)
  {
    _schemaResolver = schemaResolver;
    _schemaParser = new SchemaParser(schemaResolver);
    _dataNamespace = dataNamespace;
  }

  /**
   * @return location of the {@link com.linkedin.pegasus.generator.spec.ClassTemplateSpec} is originated, most likely the pdsc file that defines it
   */
  public DataSchemaLocation getClassLocation(ClassTemplateSpec classSpec)
  {
    return _classToDataSchemaLocationMap.get(classSpec);
  }

  /**
   * Instead of generate spec for the specify {@link com.linkedin.data.schema.DataSchema}, assume it is already defined in the system.
   */
  public void registerDefinedSchema(DataSchema schema)
  {
    final ClassTemplateSpec spec = createFromDataSchema(schema);
    _schemaToClassMap.put(schema, spec);
    _classNameToSchemaMap.put(spec.getFullName(), schema);
  }

  /**
   * Generate {@link com.linkedin.pegasus.generator.spec.ClassTemplateSpec} from the specified {@link com.linkedin.data.schema.DataSchema} without knowing the location.
   */
  public ClassTemplateSpec generate(DataSchema schema)
  {
    return processSchema(schema, null, null);
  }

  /**
   * Generate {@link com.linkedin.pegasus.generator.spec.ClassTemplateSpec} from the specified {@link com.linkedin.data.schema.DataSchema} and its location.
   */
  public ClassTemplateSpec generate(DataSchema schema, DataSchemaLocation location)
  {
    pushCurrentLocation(location);
    final ClassTemplateSpec result = generate(schema);
    popCurrentLocation();
    return result;
  }

  public Collection<ClassTemplateSpec> getGeneratedSpecs()
  {
    return _classTemplateSpecs;
  }

  /**
   * Emit message if the schema is a {@link com.linkedin.data.schema.NamedDataSchema} and the class name ends with one of the special suffixes, e.g. "Array", "Map".
   * <p/>
   * <p/>
   * This may potentially conflict with class names for Java binding for array or map of this type.
   *
   * @param className provides the class name.
   */
  private static void checkClassNameForSpecialSuffix(String className)
  {
    for (String suffix : SPECIAL_SUFFIXES)
    {
      if (className.endsWith(suffix))
      {
        _log.warn("Class name for named type ends with a suffix that may conflict with derived class names for unnamed types" +
            ", name: " + className +
            ", suffix: " + suffix);
        break;
      }
    }
  }

  /**
   * Allow custom class to to bound to record or typeref of primitive types that are not enums.
   */
  private static boolean allowCustomClass(DataSchema schema)
  {
    boolean result = false;
    final DataSchema.Type type = schema.getType();
    if (type == DataSchema.Type.TYPEREF || type == DataSchema.Type.RECORD)
    {
      // allow custom class only if the dereferenced type is a record or a primitive types that are not enums
      final DataSchema dereferencedSchema = schema.getDereferencedDataSchema();
      if (dereferencedSchema.getType() == DataSchema.Type.RECORD || (CodeUtil.isDirectType(dereferencedSchema) && dereferencedSchema.getType() != DataSchema.Type.ENUM))
      {
        result = true;
      }
    }
    return result;
  }

  private static DataSchema dereferenceIfTyperef(DataSchema schema)
  {
    final DataSchema.Type type = schema.getType();
    return type == DataSchema.Type.TYPEREF ? ((TyperefDataSchema) schema).getRef() : null;
  }

  /*
   * Return exception for trying to use null type outside of a union.
   */
  private static IllegalArgumentException nullTypeNotAllowed(ClassTemplateSpec enclosingClass, String memberName)
  {
    return new IllegalArgumentException("The null type can only be used in unions, null found" + enclosingClassAndMemberNameToString(enclosingClass, memberName));
  }

  /*
   * Return exception for unrecognized schema type.
   */
  private static IllegalStateException unrecognizedSchemaType(ClassTemplateSpec enclosingClass, String memberName, DataSchema schema)
  {
    return new IllegalStateException("Unrecognized schema: " + schema +
        enclosingClassAndMemberNameToString(enclosingClass, memberName));
  }

  /*
   * Generate human consumable representation of enclosing class and field name.
   */
  private static String enclosingClassAndMemberNameToString(ClassTemplateSpec enclosingClass, String memberName)
  {
    final StringBuilder sb = new StringBuilder();
    if (memberName != null)
    {
      sb.append(" in ");
      sb.append(memberName);
    }
    if (enclosingClass != null)
    {
      sb.append(" in ");
      sb.append(enclosingClass.getFullName());
    }
    return sb.toString();
  }

  /**
   * Checks if a class name conflict occurs, if it occurs throws {@link IllegalArgumentException}.
   *
   * @param className provides the Java class name.
   * @param schema    provides the {@link com.linkedin.data.schema.DataSchema} that would be bound if there is no conflict.
   *
   * @throws IllegalArgumentException
   */
  private void checkForClassNameConflict(String className, DataSchema schema)
      throws IllegalArgumentException
  {
    final DataSchema schemaFromClassName = _classNameToSchemaMap.get(className);
    boolean conflict = false;
    if (schemaFromClassName != null && schemaFromClassName != schema)
    {
      final DataSchema.Type schemaType = schema.getType();
      if (schemaFromClassName.getType() != schemaType)
      {
        conflict = true;
      }
      else if (schema instanceof NamedDataSchema)
      {
        conflict = true;
      }
      else if (!schemaFromClassName.equals(schema))
      {
        assert schemaType == DataSchema.Type.ARRAY || schemaType == DataSchema.Type.MAP;
        //
        // see schemaForArrayItemsOrMapValues
        //
        // When the schema bound to the specified class name is different
        // from the specified schema, then emit a log message when this occurs.
        //
        _log.info("Class name: " + className +
            ", bound to schema:" + schemaFromClassName +
            ", instead of schema: " + schema);
      }
    }
    if (conflict)
    {
      throw new IllegalArgumentException("Class name conflict detected, class name: " + className +
          ", class already bound to schema: " + schemaFromClassName +
          ", attempting to rebind to schema: " + schema);
    }
  }

  private DataSchemaLocation currentLocation()
  {
    return _locationStack.getLast();
  }

  private void pushCurrentLocation(DataSchemaLocation location)
  {
    _locationStack.addLast(location);
  }

  private void popCurrentLocation()
  {
    _locationStack.removeLast();
  }

  /**
   * Register a new class TemplateSpec.
   * <p/>
   * Registration is necessary to associate the {@link com.linkedin.pegasus.generator.spec.ClassTemplateSpec} with the source file for which it was generated. This may be used later to determine if generated class should be emitted
   * based on the location of the source file.
   * <p/>
   * Registration also associates the {@link com.linkedin.data.schema.DataSchema} to the generated {@link com.linkedin.pegasus.generator.spec.ClassTemplateSpec} and the generated class's full name to the the {@link com.linkedin.pegasus.generator.spec.ClassTemplateSpec}.
   *
   * @param schema            provides the {@link com.linkedin.data.schema.DataSchema} of the generated class.
   * @param classTemplateSpec provides the generated class.
   */
  private void registerClassTemplateSpec(DataSchema schema, ClassTemplateSpec classTemplateSpec)
  {
    classTemplateSpec.setLocation(currentLocation().toString());
    _schemaToClassMap.put(schema, classTemplateSpec);
    _classNameToSchemaMap.put(classTemplateSpec.getFullName(), schema);
    _classToDataSchemaLocationMap.put(classTemplateSpec, currentLocation());

    if (schema instanceof NamedDataSchema)
    {
      checkClassNameForSpecialSuffix(classTemplateSpec.getFullName());
    }

    _classTemplateSpecs.add(classTemplateSpec);
  }

  private ClassTemplateSpec processSchema(DataSchema schema, ClassTemplateSpec enclosingClass, String memberName)
  {
    ClassTemplateSpec result = null;

    final CustomInfoSpec customInfo = getImmediateCustomInfo(schema);
    while (schema.getType() == DataSchema.Type.TYPEREF)
    {
      final TyperefDataSchema typerefSchema = (TyperefDataSchema) schema;
      final ClassTemplateSpec found = _schemaToClassMap.get(schema);
      if (found == null)
      {
        if (typerefSchema.getRef().getType() == DataSchema.Type.UNION)
        {
          result = generateUnion((UnionDataSchema) typerefSchema.getRef(), typerefSchema);
          break;
        }
        else
        {
          generateTyperef(typerefSchema);
        }
      }
      else if (typerefSchema.getRef().getType() == DataSchema.Type.UNION)
      {
        result = found;
        break;
      }
      schema = typerefSchema.getRef();
    }
    if (result == null)
    {
      assert schema == schema.getDereferencedDataSchema();
      if (schema instanceof ComplexDataSchema)
      {
        final ClassTemplateSpec found = _schemaToClassMap.get(schema);
        if (found == null)
        {
          if (schema instanceof NamedDataSchema)
          {
            result = generateNamedSchema((NamedDataSchema) schema);
          }
          else
          {
            result = generateUnnamedComplexSchema(schema, enclosingClass, memberName);
          }
        }
        else
        {
          result = found;
        }

        if (customInfo != null)
        {
          result = customInfo.getCustomClass();
        }
      }
      else if (schema instanceof PrimitiveDataSchema)
      {
        result = (customInfo != null) ? customInfo.getCustomClass() : getPrimitiveClassForSchema((PrimitiveDataSchema) schema, enclosingClass, memberName);
      }
    }

    if (result == null)
    {
      throw unrecognizedSchemaType(enclosingClass, memberName, schema);
    }

    return result;
  }

  /**
   * Determine whether a custom class has been defined for the {@link com.linkedin.data.schema.DataSchema}.
   * <p/>
   * A custom class is defined through the "java" property of the schema. Within this property, a custom class is specified if "java" is a map that contains a "class" property whose value is a string.
   * This value specifies the Java class name of the custom class.
   * <p/>
   * The map may optionally include a "coercerClass" property to specify a coercer class that should be initialized.
   *
   * @param schema to look for custom class specification in.
   *
   * @return null if no custom class is specified, otherwise return the custom class and the coercer class, the coercer class may be null if no coercer class is specified.
   *
   * @see com.linkedin.data.template.Custom#initializeCoercerClass(Class)
   */
  private CustomClasses getCustomClasses(DataSchema schema)
  {
    CustomClasses customClasses = null;
    final Map<String, Object> properties = schema.getProperties();
    final Object java = properties.get(SCALA_PROPERTY);
    if (java != null)
    {
      if (java.getClass() != DataMap.class)
      {
        throw new IllegalArgumentException(schema + " has \"java\" property that is not a DataMap");
      }
      final DataMap map = (DataMap) java;
      final Object custom = map.get(CLASS_PROPERTY);
      if (custom != null)
      {
        if (custom.getClass() != String.class)
        {
          throw new IllegalArgumentException(schema + " has \"java\" property with \"class\" that is not a string");
        }
        // a custom class specification has been found
        customClasses = new CustomClasses();
        customClasses.customClass = new ClassTemplateSpec();
        customClasses.customClass.setFullName((String) custom);
        if (!allowCustomClass(schema))
        {
          throw new IllegalArgumentException(schema + " cannot have custom class binding");
        }
      }
      // check for coercer class
      final Object coercerClass = map.get(COERCER_CLASS_PROPERTY);
      if (coercerClass != null)
      {
        if (coercerClass.getClass() != String.class)
        {
          throw new IllegalArgumentException(schema + " has \"java\" property with \"coercerClass\" that is not a string");
        }
        if (customClasses == null)
        {
          throw new IllegalArgumentException(schema + " has \"java\" property with \"coercerClass\" but does not have \"class\" property");
        }
        // a custom class specification has been found
        customClasses.customCoercerClass = new ClassTemplateSpec();
        customClasses.customCoercerClass.setFullName((String) coercerClass);
      }
    }
    return customClasses;
  }

  private CustomInfoSpec getImmediateCustomInfo(DataSchema schema)
  {
    if (_immediateCustomMap.containsKey(schema))
    {
      return _immediateCustomMap.get(schema);
    }

    CustomInfoSpec immediate = null;
    for (DataSchema current = schema; current != null; current = dereferenceIfTyperef(current))
    {
      final CustomClasses customClasses = getCustomClasses(current);
      if (customClasses != null)
      {
        immediate = new CustomInfoSpec((NamedDataSchema) schema, (NamedDataSchema) current, customClasses.customClass, customClasses.customCoercerClass);
        break;
      }
    }

    // immediate may be null
    _immediateCustomMap.put(schema, immediate);
    return immediate;
  }

  private ClassTemplateSpec getPrimitiveClassForSchema(PrimitiveDataSchema schema, ClassTemplateSpec enclosingClass, String memberName)
  {
    switch (schema.getType())
    {
      case INT:
      case DOUBLE:
      case BOOLEAN:
      case STRING:
      case LONG:
      case FLOAT:
      case BYTES:
        return PrimitiveTemplateSpec.getInstance(schema.getType());
      case NULL:
        throw nullTypeNotAllowed(enclosingClass, memberName);
    }
    throw unrecognizedSchemaType(enclosingClass, memberName, schema);
  }

  private ClassTemplateSpec generateNamedSchema(NamedDataSchema schema)
  {
    pushCurrentLocation(_schemaResolver.nameToDataSchemaLocations().get(schema.getFullName()));

    final String className = classNameForNamedSchema(schema);
    checkForClassNameConflict(className, schema);

    final ClassTemplateSpec templateClass;
    switch (schema.getType())
    {
      case RECORD:
        templateClass = generateRecord((RecordDataSchema) schema);
        break;
      case ENUM:
        templateClass = generateEnum((EnumDataSchema) schema);
        break;
      case FIXED:
        templateClass = generateFixed((FixedDataSchema) schema);
        break;
      default:
        throw unrecognizedSchemaType(null, null, schema);
    }

    popCurrentLocation();

    return templateClass;
  }

  private ClassTemplateSpec generateUnnamedComplexSchema(DataSchema schema, ClassTemplateSpec enclosingClass, String memberName)
  {
    if (schema instanceof ArrayDataSchema)
    {
      return generateArray((ArrayDataSchema) schema, enclosingClass, memberName);
    }
    else if (schema instanceof MapDataSchema)
    {
      return generateMap((MapDataSchema) schema, enclosingClass, memberName);
    }
    else if (schema instanceof UnionDataSchema)
    {
      return generateUnion((UnionDataSchema) schema, enclosingClass, memberName);
    }
    else
    {
      throw unrecognizedSchemaType(enclosingClass, memberName, schema);
    }
  }

  private ClassTemplateSpec determineDataClass(DataSchema schema, ClassTemplateSpec enclosingClass, String memberName)
  {
    final ClassTemplateSpec result;
    final DataSchema dereferencedSchema = schema.getDereferencedDataSchema();
    if (dereferencedSchema.getType() == DataSchema.Type.ENUM)
    {
      result = PrimitiveTemplateSpec.getInstance(DataSchema.Type.STRING);
    }
    else if (CodeUtil.isDirectType(dereferencedSchema))
    {
      result = getPrimitiveClassForSchema((PrimitiveDataSchema) dereferencedSchema, enclosingClass, memberName);
    }
    else
    {
      result = null;
    }
    return result;
  }

  private ArrayTemplateSpec generateArray(ArrayDataSchema schema, ClassTemplateSpec enclosingClass, String memberName)
  {
    final DataSchema itemSchema = schema.getItems();

    final ClassInfo classInfo = classInfoForUnnamed(enclosingClass, memberName, schema);
    if (classInfo.existingClass != null)
    {
    /* When type refs are used as item types inside some unnamed complex schemas like map and array,
     * the type refs are de-referenced and the underlying real type is used in the generated class.
     * In those cases the type refs are not processed by the class generation logic, an explicit
     * schema processing is necessary in order to processSchema the data template classes for those type
     * refs.
     */
      processSchema(itemSchema, enclosingClass, memberName);

      return (ArrayTemplateSpec) classInfo.existingClass;
    }

    final ArrayTemplateSpec arrayClass = (ArrayTemplateSpec) classInfo.definedClass;
    registerClassTemplateSpec(schema, arrayClass);

    arrayClass.setItemClass(processSchema(itemSchema, enclosingClass, memberName));
    arrayClass.setItemDataClass(determineDataClass(itemSchema, enclosingClass, memberName));

    final CustomInfoSpec customInfo = getImmediateCustomInfo(itemSchema);
    arrayClass.setCustomInfo(customInfo);

    return arrayClass;
  }

  private CourierMapTemplateSpec generateMap(MapDataSchema schema, ClassTemplateSpec enclosingClass, String memberName)
  {
    final DataSchema valueSchema = schema.getValues();

    final ClassInfo classInfo = classInfoForUnnamed(enclosingClass, memberName, schema);

    // For Courier, check the keys property for a data schema.
    final Object keysSchemaData = schema.getProperties().get("keys");
    DataSchema keySchema;
    if (keysSchemaData != null)
    {
      keySchema = _schemaParser.parseObject(keysSchemaData);
    }
    else
    {
      keySchema = null;
    }

    if (classInfo.existingClass != null)
    {
    /* When type refs are used as item types inside some unnamed complex schemas like map and array,
     * the type refs are de-referenced and the underlying real type is used in the generated class.
     * In those cases the type refs are not processed by the class generation logic, an explicit
     * schema processing is necessary in order to processSchema the data template classes for those type
     * refs.
     */
      processSchema(valueSchema, enclosingClass, memberName);

      // For Courier, if there is a schema for map keys, make sure we include in in generation.
      if (keySchema != null)
      {
        if (classInfo.existingClass != null)
        {
          processSchema(keySchema, enclosingClass, memberName);
        }
      }

      return (CourierMapTemplateSpec) classInfo.existingClass;
    }

    final CourierMapTemplateSpec mapClass = (CourierMapTemplateSpec) classInfo.definedClass;
    registerClassTemplateSpec(schema, mapClass);

    mapClass.setValueClass(processSchema(valueSchema, enclosingClass, memberName));
    mapClass.setValueDataClass(determineDataClass(valueSchema, enclosingClass, memberName));

    final CustomInfoSpec customInfo = getImmediateCustomInfo(valueSchema);
    mapClass.setCustomInfo(customInfo);

    // For Courier, if there is a schema for map keys, add it's information to the TemplateSpec.
    if (keySchema != null)
    {
      mapClass.setKeySchema(keySchema);
      mapClass.setKeyClass(processSchema(keySchema, enclosingClass, memberName));
      mapClass.setKeyDataClass(determineDataClass(keySchema, enclosingClass, memberName));
      final CustomInfoSpec keyCustomInfo = getImmediateCustomInfo(keySchema);
      mapClass.setKeyCustomInfo(keyCustomInfo);
    }

    return mapClass;
  }

  private UnionTemplateSpec generateUnion(UnionDataSchema schema, ClassTemplateSpec enclosingClass, String memberName)
  {
    if (enclosingClass == null || memberName == null)
    {
      throw new IllegalArgumentException("Cannot processSchema template for top level union: " + schema);
    }
    final ClassInfo classInfo = classInfoForUnnamed(enclosingClass, memberName, schema);
    if (classInfo.existingClass != null)
    {
      return (UnionTemplateSpec) classInfo.existingClass;
    }
    final UnionTemplateSpec unionClass = (UnionTemplateSpec) classInfo.definedClass;
    registerClassTemplateSpec(schema, unionClass);
    return generateUnion(schema, unionClass);
  }

  private ClassTemplateSpec generateUnion(UnionDataSchema schema, TyperefDataSchema typerefDataSchema)
  {
    assert typerefDataSchema.getRef() == schema;

    pushCurrentLocation(_schemaResolver.nameToDataSchemaLocations().get(typerefDataSchema.getFullName()));

    final UnionTemplateSpec unionClass = new UnionTemplateSpec(schema);
    unionClass.setNamespace(typerefDataSchema.getNamespace());
    unionClass.setClassName(typerefDataSchema.getName());
    unionClass.setModifiers(ModifierSpec.PUBLIC);
    registerClassTemplateSpec(typerefDataSchema, unionClass);

    final TyperefTemplateSpec typerefInfoClass = new TyperefTemplateSpec(typerefDataSchema);
    typerefInfoClass.setEnclosingClass(unionClass);
    typerefInfoClass.setClassName("UnionTyperefInfo");
    typerefInfoClass.setModifiers(ModifierSpec.PRIVATE, ModifierSpec.STATIC, ModifierSpec.FINAL);

    final UnionTemplateSpec result = generateUnion(schema, unionClass);
    result.setTyperefClass(typerefInfoClass);

    popCurrentLocation();
    return result;
  }

  private UnionTemplateSpec generateUnion(UnionDataSchema schema, UnionTemplateSpec unionClass)
  {
    for (DataSchema memberType : schema.getTypes())
    {
      final UnionTemplateSpec.Member newMember = new UnionTemplateSpec.Member();
      unionClass.getMembers().add(newMember);

      newMember.setSchema(memberType);

      if (memberType.getDereferencedType() != DataSchema.Type.NULL)
      {
        newMember.setClassTemplateSpec(processSchema(memberType, unionClass, memberType.getUnionMemberKey()));
        newMember.setDataClass(determineDataClass(memberType, unionClass, memberType.getUnionMemberKey()));
      }
    }

    return unionClass;
  }

  private ClassTemplateSpec generateEnum(EnumDataSchema schema)
  {
    final EnumTemplateSpec enumClass = new EnumTemplateSpec(schema);
    enumClass.setNamespace(schema.getNamespace());
    enumClass.setClassName(schema.getName());
    enumClass.setModifiers(ModifierSpec.PUBLIC);
    registerClassTemplateSpec(schema, enumClass);
    return enumClass;
  }

  private ClassTemplateSpec generateFixed(FixedDataSchema schema)
  {
    final FixedTemplateSpec fixedClass = new FixedTemplateSpec(schema);
    fixedClass.setNamespace(schema.getNamespace());
    fixedClass.setClassName(schema.getName());
    fixedClass.setModifiers(ModifierSpec.PUBLIC);
    registerClassTemplateSpec(schema, fixedClass);

    return fixedClass;
  }

  private TyperefTemplateSpec generateTyperef(TyperefDataSchema schema)
  {
    final TyperefTemplateSpec typerefClass = new TyperefTemplateSpec(schema);
    typerefClass.setNamespace(schema.getNamespace());
    typerefClass.setClassName(schema.getName());
    typerefClass.setModifiers(ModifierSpec.PUBLIC);
    registerClassTemplateSpec(schema, typerefClass);
    return typerefClass;
  }

  private RecordTemplateSpec generateRecord(RecordDataSchema schema)
  {
    final RecordTemplateSpec recordClass = new RecordTemplateSpec(schema);
    recordClass.setNamespace(schema.getNamespace());
    recordClass.setClassName(schema.getName());
    recordClass.setModifiers(ModifierSpec.PUBLIC);
    registerClassTemplateSpec(schema, recordClass);

    // processSchema included schemas first, so that unnamed classes will belong to the defining class
    // instead of the current class
    final List<NamedDataSchema> includes = schema.getInclude();
    for (NamedDataSchema includedSchema : includes)
    {
      generate(includedSchema);
    }

    final Map<CustomInfoSpec, Object> customInfoMap = new IdentityHashMap<CustomInfoSpec, Object>(schema.getFields().size() * 2);

    for (RecordDataSchema.Field field : schema.getFields())
    {
      final ClassTemplateSpec fieldClass = processSchema(field.getType(), recordClass, field.getName());
      final RecordTemplateSpec.Field newField = new RecordTemplateSpec.Field();
      newField.setSchemaField(field);
      newField.setType(fieldClass);
      newField.setDataClass(determineDataClass(field.getType(), recordClass, field.getName()));

      final CustomInfoSpec customInfo = getImmediateCustomInfo(field.getType());
      if (customInfo != null && !customInfoMap.containsKey(customInfo))
      {
        customInfoMap.put(customInfo, null);
        newField.setCustomInfo(customInfo);
      }

      recordClass.addField(newField);
    }

    return recordClass;
  }

  /*
   * Determine name and class for unnamed types.
   */
  private ClassInfo classInfoForUnnamed(ClassTemplateSpec enclosingClass, String name, DataSchema schema)
  {
    assert !(schema instanceof NamedDataSchema);
    assert !(schema instanceof PrimitiveDataSchema);

    final ClassInfo classInfo = classNameForUnnamedTraverse(enclosingClass, name, schema);
    final String className = classInfo.fullName();

    final DataSchema schemaFromClassName = _classNameToSchemaMap.get(className);
    if (schemaFromClassName == null)
    {
      final ClassTemplateSpec classTemplateSpec = createFromDataSchema(schema);

      if (enclosingClass != null && classInfo.namespace.equals(enclosingClass.getFullName()))
      {
        classTemplateSpec.setEnclosingClass(enclosingClass);
        classTemplateSpec.setClassName(classInfo.name);
        classTemplateSpec.setModifiers(ModifierSpec.PUBLIC, ModifierSpec.STATIC, ModifierSpec.FINAL);
      }
      else
      {
        classTemplateSpec.setNamespace(classInfo.namespace);
        classTemplateSpec.setClassName(classInfo.name);
        classTemplateSpec.setModifiers(ModifierSpec.PUBLIC);
      }
      classInfo.definedClass = classTemplateSpec;
    }
    else
    {
      checkForClassNameConflict(className, schema);
      classInfo.existingClass = _schemaToClassMap.get(schemaFromClassName);
    }

    return classInfo;
  }

  // For Courier, use CourierMapTemplateSpec for maps.
  private ClassTemplateSpec createFromDataSchema(DataSchema schema)
  {
    if (schema instanceof MapDataSchema)
    {
      return new CourierMapTemplateSpec((MapDataSchema) schema);
    }
    return ClassTemplateSpec.createFromDataSchema(schema);
  }

  private ClassInfo classNameForUnnamedTraverse(ClassTemplateSpec enclosingClass, String memberName, DataSchema schema)
  {
    final DataSchema dereferencedDataSchema = schema.getDereferencedDataSchema();
    switch (dereferencedDataSchema.getType())
    {
      case ARRAY:
        final ArrayDataSchema arraySchema = (ArrayDataSchema) dereferencedDataSchema;
        CustomInfoSpec customInfo = getImmediateCustomInfo(arraySchema.getItems());
        if (customInfo != null)
        {
          return new ClassInfo(customInfo.getCustomSchema().getNamespace(), customInfo.getCustomSchema().getName() + ARRAY_SUFFIX);
        }
        else
        {
          final ClassInfo classInfo = classNameForUnnamedTraverse(enclosingClass, memberName, arraySchema.getItems());
          classInfo.name += ARRAY_SUFFIX;
          return classInfo;
        }
      case MAP:
        final MapDataSchema mapSchema = (MapDataSchema) dereferencedDataSchema;

        // For Courier, if the schema has a key type, add the name of the type to the generated
        // class name.
        ClassAndSchema keyClassInfo = getMapKeyName(mapSchema, enclosingClass, memberName);
        ClassAndSchema valueClassInfo = getMapValueName(mapSchema, enclosingClass, memberName);

        String className;
        if (keyClassInfo != null)
        {
          className = keyClassInfo.classInfo.name + "To" + valueClassInfo.classInfo.name + MAP_SUFFIX;
        } else {
          className = valueClassInfo.classInfo.name + MAP_SUFFIX;
        }

        // Pick the best possible namespace
        String namespace;
        if (valueClassInfo.schema instanceof PrimitiveDataSchema)
        {
          if (keyClassInfo != null && !(keyClassInfo.schema instanceof PrimitiveDataSchema))
          {
            namespace = keyClassInfo.classInfo.namespace;
          }
          else
          {
            namespace = _dataNamespace;
          }
        }
        else
        {
          namespace = valueClassInfo.classInfo.namespace;
        }
        return new ClassInfo(namespace, className);

      case UNION:
        if (schema.getType() == DataSchema.Type.TYPEREF)
        {
          DataSchema referencedDataSchema;
          TyperefDataSchema typerefDataSchema = (TyperefDataSchema) schema;
          while ((referencedDataSchema = typerefDataSchema.getDereferencedDataSchema()) != dereferencedDataSchema)
          {
            typerefDataSchema = (TyperefDataSchema) referencedDataSchema;
          }
          return new ClassInfo(typerefDataSchema.getNamespace(), CodeUtil.capitalize(typerefDataSchema.getName()));
        }
        else
        {
          return new ClassInfo(enclosingClass.getFullName(), CodeUtil.capitalize(memberName));
        }

      case FIXED:
      case RECORD:
      case ENUM:
        final NamedDataSchema namedSchema = (NamedDataSchema) dereferencedDataSchema;
        return new ClassInfo(namedSchema.getNamespace(), CodeUtil.capitalize(namedSchema.getName()));

      case BOOLEAN:
        return new ClassInfo(courierPackageName, "Boolean");

      case INT:
        return new ClassInfo(courierPackageName, "Int");

      case LONG:
        return new ClassInfo(courierPackageName, "Long");

      case FLOAT:
        return new ClassInfo(courierPackageName, "Float");

      case DOUBLE:
        return new ClassInfo(courierPackageName, "Double");

      case STRING:
        return new ClassInfo(courierPackageName, "String");

      case BYTES:
        return new ClassInfo(courierPackageName, "ByteString");

      case NULL:
        throw nullTypeNotAllowed(enclosingClass, memberName);

      default:
        throw unrecognizedSchemaType(enclosingClass, memberName, dereferencedDataSchema);
    }
  }

  private ClassAndSchema getMapKeyName(MapDataSchema mapSchema, ClassTemplateSpec enclosingClass, String memberName)
  {
    final Object keysSchemaData = mapSchema.getProperties().get("keys");
    if (keysSchemaData != null)
    {
      DataSchema keySchema = _schemaParser.parseObject(keysSchemaData);
      final ClassInfo keyClassInfo = classNameForUnnamedTraverse(enclosingClass, memberName, keySchema);
      final CustomInfoSpec keyCustomInfo = getImmediateCustomInfo(keySchema);

      if (keyCustomInfo != null)
      {
        NamedDataSchema customSchema = keyCustomInfo.getCustomSchema();
        return new ClassAndSchema(new ClassInfo(customSchema.getNamespace(), customSchema.getName()), customSchema);
      }
      else
      {
        if (keyClassInfo.name.equals("String"))
        {
          return null;
        }
        else
        {
          return new ClassAndSchema(keyClassInfo, keySchema);
        }
      }
    }
    else
    {
      return null;
    }
  }

  private ClassAndSchema getMapValueName(MapDataSchema mapSchema, ClassTemplateSpec enclosingClass, String memberName)
  {
    CustomInfoSpec customInfo = getImmediateCustomInfo(mapSchema.getValues());
    if (customInfo != null)
    {
      return new ClassAndSchema(new ClassInfo(customInfo.getCustomSchema().getNamespace(), customInfo.getCustomSchema().getName()), customInfo.getCustomSchema());
    }
    else
    {
      return new ClassAndSchema(classNameForUnnamedTraverse(enclosingClass, memberName, mapSchema.getValues()), mapSchema.getValues());
    }
  }

  private static class ClassAndSchema {
    private ClassInfo classInfo;
    private DataSchema schema;

    private ClassAndSchema(ClassInfo classInfo, DataSchema schema)
    {
      this.classInfo = classInfo;
      this.schema = schema;
    }
  }

  private static class CustomClasses
  {
    private ClassTemplateSpec customClass;
    private ClassTemplateSpec customCoercerClass;
  }

  private static class ClassInfo
  {
    private String namespace;
    private String name;
    private ClassTemplateSpec existingClass;
    private ClassTemplateSpec definedClass;

    private ClassInfo(String namespace, String name)
    {
      this.namespace = namespace;
      this.name = name;
    }

    private String fullName()
    {
      return namespace.isEmpty() ? name : namespace + '.' + name;
    }
  }
}
