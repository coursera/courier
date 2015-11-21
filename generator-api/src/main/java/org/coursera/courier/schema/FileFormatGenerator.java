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

package org.coursera.courier.schema;

import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.codec.JacksonDataCodec;
import com.linkedin.data.codec.PrettyPrinterJacksonDataCodec;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.MapDataSchema;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.pegasus.generator.GeneratorResult;
import com.linkedin.pegasus.generator.JavaCodeGeneratorBase;
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec;
import com.linkedin.pegasus.generator.spec.FixedTemplateSpec;
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;
import org.apache.commons.lang3.StringEscapeUtils;
import org.coursera.courier.api.CourierMapTemplateSpec;
import org.coursera.courier.api.CourierTyperefTemplateSpec;
import org.coursera.courier.api.DefaultGeneratorRunner;
import org.coursera.courier.api.GeneratedCode;
import org.coursera.courier.api.GeneratedCodeTargetFile;
import org.coursera.courier.api.GeneratorRunnerOptions;
import org.coursera.courier.api.PegasusCodeGenerator;
import org.coursera.courier.api.PoorMansCStyleSourceFormatter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FileFormatGenerator implements PegasusCodeGenerator {

  public static void main(String[] args) throws Throwable {
    if (args.length < 2) {
      System.err.println(
        "Usage: " + FileFormatGenerator.class.getName() + " targetDirectoryPath [sourceFile " +
          "or sourceDirectory or schemaName]+");
      System.exit(1);
    }
    String targetDirectoryPath = args[0];
    String resolverPath = args[1];
    String[] sources = java.util.Arrays.copyOfRange(args, 1, args.length);
    String defaultPackage =
      System.getProperty(JavaCodeGeneratorBase.GENERATOR_DEFAULT_PACKAGE);

    GeneratorResult result = new DefaultGeneratorRunner().run(
      new FileFormatGenerator(),
      new GeneratorRunnerOptions(
        targetDirectoryPath,
        sources,
        resolverPath)
        .setDefaultPackage(defaultPackage)
        .setGenerateTyperefs(true));

    for (File file: result.getTargetFiles()) {
      System.out.println(file.getAbsolutePath());
    }
  }

  private static final Set<String> keywords = new HashSet<String>(Arrays.asList(new String[] {
    "record", "union", "enum", "typeref", "fixed", "array", "map", "namespace", "true", "false",
    "null", "nil"
  }));

  private String escapeNamespaced(String namespaced) {
    StringBuilder builder = new StringBuilder();
    String[] parts = namespaced.split("\\.");
    int len = parts.length;
    for (int i = 0; i < len; i++) {
      builder.append(escape(parts[i]));
      if (i < len - 1) {
        builder.append(".");
      }
    }
    return builder.toString();
  }

  private String escape(String identifier) {
    if (keywords.contains(identifier)) {
      return "`" + identifier + "`";
    } else {
      return identifier;
    }
  }

  private String unescapeDoc(String docstring) {
    return StringEscapeUtils.unescapeHtml4(docstring);
  }

  private static JacksonDataCodec singleLineCodec = new JacksonDataCodec();
  private static JacksonDataCodec multiLineCodec = new PrettyPrinterJacksonDataCodec();

  private String toJson(Object jsValue, boolean multiline) {
    JacksonDataCodec codec = multiline ? multiLineCodec : singleLineCodec;
    try {
      if (jsValue instanceof DataMap) {
        return codec.mapToString((DataMap) jsValue);
      } else if (jsValue instanceof DataList) {
        return codec.listToString((DataList) jsValue);
      } else if (jsValue instanceof String) {
        return '"' + StringEscapeUtils.escapeJson((String) jsValue) + '"';
      } else if (jsValue instanceof Number) {
        return jsValue.toString();
      } else if (jsValue instanceof Boolean) {
        return jsValue.toString();
      } else if (jsValue == null) {
        return "null";
      } else {
        throw new IllegalArgumentException("Unexpected type: " + jsValue.getClass());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public GeneratedCode generate(ClassTemplateSpec spec) {
    if (!(spec.getSchema() instanceof NamedDataSchema)) return null;

    GeneratedType generatedType = generateType(null, spec, spec.getSchema());

    StringBuilder builder = new StringBuilder();
    builder.append("namespace ").append(escapeNamespaced(spec.getNamespace())).append("\n");
    builder.append("\n");

    for (String importNamespace: generatedType.importedTypes) {
      builder.append("import ").append(importNamespace).append("\n");
    }
    builder.append("\n");

    builder.append(generatedType.code);
    String code = PoorMansCStyleSourceFormatter.format(builder.toString());
    return new GeneratedCode(
      new GeneratedCodeTargetFile(
        spec.getClassName(),
        spec.getNamespace() == null ? "" : spec.getNamespace(),
        "courier"),
      code);
  }

  public GeneratedType generateRefOrDecl(ClassTemplateSpec container, ClassTemplateSpec spec, DataSchema dataSchema) {
    if (isRef(container, spec) && dataSchema instanceof NamedDataSchema) {
      NamedDataSchema named = (NamedDataSchema)dataSchema;
      return generated(escape(named.getName()), escapeNamespaced(named.getFullName()));
      //return escape(spec.getClassName());
    } else {
      return generateType(container, spec, dataSchema);
    }
  }

  public boolean isRef(ClassTemplateSpec container, ClassTemplateSpec spec) {
    return
      ((spec.getSchema() instanceof NamedDataSchema) &&
      (spec.getEnclosingClass() == null || !spec.getEnclosingClass().equals(container)));
  }

  public static class GeneratedType {
    public final String code;
    public final Set<String> importedTypes;

    public GeneratedType(String code, Set<String> importedTypes) {
      this.code = code;
      this.importedTypes = importedTypes;
    }

    @Override
    public String toString() {
      throw new RuntimeException();
    }
  }

  public static GeneratedType generated(String code) {
    return new GeneratedType(code, Collections.<String>emptySet());
  }

  public static GeneratedType generated(String code, Set<String> importedTypes) {
    return new GeneratedType(code, importedTypes);
  }

  public static GeneratedType generated(String code, String... importedTypes) {
    return new GeneratedType(code, new HashSet<String>(Arrays.asList(importedTypes)));
  }

  public GeneratedType generateType(ClassTemplateSpec container, ClassTemplateSpec spec, DataSchema schema) {
    if (spec instanceof PrimitiveTemplateSpec) {
      PrimitiveTemplateSpec primitive = (PrimitiveTemplateSpec) spec;
      return generated(primitive.getSchema().getUnionMemberKey());
    } else if (spec instanceof RecordTemplateSpec) {
      RecordTemplateSpec record = (RecordTemplateSpec)spec;
      RecordDataSchema recordSchema = (RecordDataSchema)schema;
      return generateRecord(record, recordSchema);
    } else if (spec instanceof EnumTemplateSpec) {
      EnumTemplateSpec enumeration = (EnumTemplateSpec)spec;
      return generateEnumeration(enumeration);
    } else if (spec instanceof UnionTemplateSpec) {
      UnionTemplateSpec union = (UnionTemplateSpec) spec;
      return generateUnion(container, union);
    } else if (spec instanceof CourierMapTemplateSpec) {
      CourierMapTemplateSpec map = (CourierMapTemplateSpec) spec;
      MapDataSchema mapSchema = (MapDataSchema) schema;
      return generateMap(map, mapSchema);
    } else if (spec instanceof ArrayTemplateSpec) {
      ArrayTemplateSpec array = (ArrayTemplateSpec) spec;
      ArrayDataSchema arraySchema = (ArrayDataSchema) schema;
      return generateArray(array, arraySchema);
    } else if (spec instanceof CourierTyperefTemplateSpec) {
      CourierTyperefTemplateSpec typeref = (CourierTyperefTemplateSpec) spec;
      //TyperefDataSchema typerefSchema = (TyperefDataSchema) schema;
      return generateTyperef(typeref, schema);
    } else if (spec instanceof FixedTemplateSpec) {
      FixedTemplateSpec fixed = (FixedTemplateSpec) spec;
      return generateFixed(fixed);
    } else {
      throw new IllegalArgumentException("Unsupported type: " + spec.getSchema().getType());
    }
  }

  public String schemadocAndProperties(ClassTemplateSpec spec) {
    DataSchema schema = spec.getSchema();
    Map<String, Object> properties = schema.getProperties();

    String doc = null;
    if (schema instanceof NamedDataSchema) {
      doc = ((NamedDataSchema)schema).getDoc();
    }
    return schemadocAndProperties(doc, properties, true);
  }

  public String schemadocAndProperties(RecordTemplateSpec.Field field) {
    RecordDataSchema.Field schemaField = field.getSchemaField();
    Map<String, Object> properties = schemaField.getProperties();
    return schemadocAndProperties(schemaField.getDoc(), properties, false);
  }

  public String schemadocAndProperties(
    String doc, Map<String, Object> properties, boolean multiline) {
    StringBuilder builder = new StringBuilder();
    if (doc != null && doc.trim().length() > 0) {
      builder.append("\n");
      builder.append(SchemadocEscaping.stringToSchemadoc(doc)).append("\n");
    }
    if (properties != null) {
      for (Map.Entry<String, Object> property: properties.entrySet()) {
        String key = property.getKey();
        Object value = property.getValue();

        if (key.equals("defaultNone")) continue;

        builder.append("@").append(key);
        if (!value.equals(Boolean.TRUE)) {
          builder.append("(").append(toJson(value, multiline)).append(")");
        }
        builder.append("\n");
      }
    }
    return builder.toString();
  }

  public GeneratedType generateRecord(RecordTemplateSpec record, RecordDataSchema schema) {
    StringBuilder builder = new StringBuilder();
    builder.append(schemadocAndProperties(record));
    builder.append("record ").append(escape(record.getClassName())).append(" {\n");
    for (NamedDataSchema include: record.getSchema().getInclude()) {
      builder.append("...").append(
        include.getName()); // TODO: make sure include namespaces are added
    }
    Set<String> importedTypes = new HashSet<String>();
    for (RecordTemplateSpec.Field field: record.getFields()) {
      RecordDataSchema.Field fieldSchema = schema.getField(field.getSchemaField().getName());
      if (!field.getSchemaField().getRecord().equals(record.getSchema())) continue;
      builder.append(schemadocAndProperties(field));
      builder.append(escape(field.getSchemaField().getName()));
      GeneratedType generatedFieldType = generateRefOrDecl(record, field.getType(), fieldSchema.getType());
      importedTypes.addAll(generatedFieldType.importedTypes);
      builder.append(": ").append(generatedFieldType.code);
      if (field.getSchemaField().getOptional()) {
        builder.append("?");
      }
      Map<String, Object> props = field.getSchemaField().getProperties();
      if (field.getSchemaField().getDefault() != null) {
        builder.append(" = ");
        builder.append(toJson(field.getSchemaField().getDefault(), false));
      } else if (props != null && Objects.equals(props.get("defaultNone"), Boolean.TRUE)) {
        builder.append(" = ");
        builder.append("nil");
      }
      builder.append("\n");
    }
    builder.append("}");
    return generated(builder.toString(), importedTypes);
  }

  public GeneratedType generateMap(CourierMapTemplateSpec map, MapDataSchema mapSchema) {
    StringBuilder builder = new StringBuilder();
    Set<String> importedTypes = new HashSet<String>();
    builder
      .append("map[");
    if (map.getKeyClass() != null) {
      GeneratedType generatedKeyType = generateRefOrDecl(map, map.getKeyClass(), map.getKeySchema());
      importedTypes.addAll(generatedKeyType.importedTypes);
      builder
        .append(generatedKeyType.code)
        .append(", ");
    } else {
      builder.append("string, ");
    }
    GeneratedType generatedValueType = generateRefOrDecl(map, map.getValueClass(), mapSchema.getValues());
    importedTypes.addAll(generatedValueType.importedTypes);
    builder
      .append(generatedValueType.code)
      .append("]");
    return generated(builder.toString(), importedTypes);
  }

  public GeneratedType generateArray(ArrayTemplateSpec array, ArrayDataSchema arraySchema) {
    Set<String> importedTypes = new HashSet<String>();
    StringBuilder builder = new StringBuilder();
    GeneratedType generatedItemType = generateRefOrDecl(array, array.getItemClass(), arraySchema.getItems());
    importedTypes.addAll(generatedItemType.importedTypes);
    builder
      .append("array[")
      // TODO: getItemClass gets mapped to StringArray, which will be assigned the first
      // typeref encountered the dereferences to the same type as this array.
      // How to get the correct type back here? (also applies to map keys and values..)
      .append(generatedItemType.code)
      .append("]");
    return generated(builder.toString(), importedTypes);
  }

  public GeneratedType generateUnion(ClassTemplateSpec container, UnionTemplateSpec union) {
    if (container == null) {
      container = union;
    }

    Set<String> importedTypes = new HashSet<String>();
    StringBuilder builder = new StringBuilder();
    List<UnionTemplateSpec.Member> members = union.getMembers();
    boolean multiline = members.size() > 3;
    Iterator<UnionTemplateSpec.Member> iter = members.iterator();
    builder.append("union[");
    if (multiline) builder.append("\n");
    while (iter.hasNext()) {
      UnionTemplateSpec.Member member = iter.next();
      GeneratedType generatedMemberType = generateRefOrDecl(container, member.getClassTemplateSpec(), member.getSchema());
      importedTypes.addAll(generatedMemberType.importedTypes);
      builder.append(generatedMemberType.code);
      if (iter.hasNext()) {
        builder.append(",");
        builder.append(multiline ? "\n" : " ");
      }
    }
    if (multiline) builder.append("\n");
    builder.append("]");
    return generated(builder.toString(), importedTypes);
  }

  public GeneratedType generateEnumeration(EnumTemplateSpec enumeration) {
    StringBuilder builder = new StringBuilder();
    builder.append(schemadocAndProperties(enumeration));
    builder.append("enum ").append(enumeration.getClassName()).append(" {\n");
    for (String symbol: enumeration.getSchema().getSymbols()) {
      builder.append(escape(symbol)).append("\n");
    }
    builder.append("}");
    return generated(builder.toString());
  }

  public GeneratedType generateTyperef(CourierTyperefTemplateSpec typeref, DataSchema schema) {
    StringBuilder builder = new StringBuilder();
    // TODO:
    // `typeref.getRef()` may return the incorrect type for maps and arrays
    // because the TemplateSpec system is designed to only generate one type per dereferenced
    // type (e.g. StringArray).  As a result, any non-custom typerefs that deference to that
    // type all return the same ClassTemplateSpec for `typeref.getRef()`.
    // What we need is:
    //  some hint, either as part of `typeref.getRef()` or a sibling method, that indicates the
    //  actual type.
    GeneratedType generatedRefType = generateRefOrDecl(
      typeref,
      typeref.getRef(),
      typeref.getSchema().getRef());
    Set<String> importedTypes = generatedRefType.importedTypes;
    builder.append(schemadocAndProperties(typeref));
    builder.append("typeref ").append(escape(typeref.getClassName()));
    builder.append(" = ");
    builder.append(generatedRefType.code);
    return generated(builder.toString(), importedTypes);
  }

  public GeneratedType generateFixed(FixedTemplateSpec fixed) {
    StringBuilder builder = new StringBuilder();
    builder
      .append("fixed ")
      .append(escape(fixed.getClassName()))
      .append(fixed.getSchema().getSize());
    return generated(builder.toString());
  }

  @Override
  public Collection<GeneratedCode> generatePredef() {
    return Collections.emptySet();
  }

  @Override
  public Collection<DataSchema> definedSchemas() {
    return Collections.emptySet();
  }

  @Override
  public String buildLanguage() {
    return "courier";
  }

  @Override
  public String customTypeLanguage() {
    return "courier";
  }
}
