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
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.pegasus.generator.GeneratorResult;
import com.linkedin.pegasus.generator.JavaCodeGeneratorBase;
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec;
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

  private String escape(String identifier) {
    if (keywords.contains(identifier)) {
      return "`" + identifier + "`";
    } else {
      return identifier;
    }
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

    StringBuilder builder = new StringBuilder();
    builder.append("namespace ").append(spec.getNamespace()).append("\n");
    builder.append("\n");
    builder.append(generateType(null, spec));
    String code = PoorMansCStyleSourceFormatter.format(builder.toString());
    return new GeneratedCode(
      new GeneratedCodeTargetFile(
        spec.getClassName(),
        spec.getNamespace() == null ? "" : spec.getNamespace(),
        "courier"),
      code);
  }

  public String generateRefOrDecl(ClassTemplateSpec container, ClassTemplateSpec spec) {
    if ((spec.getSchema() instanceof NamedDataSchema) && (spec.getEnclosingClass() == null || !spec.getEnclosingClass().equals(container))) {
      if (Objects.equals(container.getNamespace(), spec.getNamespace())) {
        return escape(spec.getClassName());
      } else {
        // TODO: escape fullname
        return spec.getFullName();
      }
    } else {
      return generateType(container, spec);
    }
  }

  public String generateType(ClassTemplateSpec container, ClassTemplateSpec spec) {
    if (spec instanceof PrimitiveTemplateSpec) {
      PrimitiveTemplateSpec primitive = (PrimitiveTemplateSpec) spec;
      return primitive.getSchema().getUnionMemberKey();
    } else if (spec instanceof RecordTemplateSpec) {
      RecordTemplateSpec record = (RecordTemplateSpec)spec;
      return generateRecord(record);
    } else if (spec instanceof EnumTemplateSpec) {
      EnumTemplateSpec enumeration = (EnumTemplateSpec)spec;
      return generateEnumeration(enumeration);
    } else if (spec instanceof UnionTemplateSpec) {
      UnionTemplateSpec union = (UnionTemplateSpec) spec;
      return generateUnion(container, union);
    } else if (spec instanceof CourierMapTemplateSpec) {
      CourierMapTemplateSpec map = (CourierMapTemplateSpec) spec;
      return generateMap(map);
    } else if (spec instanceof ArrayTemplateSpec) {
      ArrayTemplateSpec array = (ArrayTemplateSpec) spec;
      return generateArray(array);
    } else if (spec instanceof CourierTyperefTemplateSpec) {
      CourierTyperefTemplateSpec typeref = (CourierTyperefTemplateSpec) spec;
      return generateTyperef(typeref);
    } else {
      return "unknown:" + spec;
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

  public String schemadocAndProperties(String doc, Map<String, Object> properties, boolean multiline) {
    StringBuilder builder = new StringBuilder();
    if (doc != null && doc.trim().length() > 0) {
      builder.append(SchemadocEscaping.stringToSchemadoc(doc)).append("\n");
    }
    if (properties != null) {
      for (Map.Entry<String, Object> property: properties.entrySet()) {
        String key = property.getKey();
        Object value = property.getValue();

        if (key.equals("defaultNone")) {
          return "";
        }

        builder.append("@").append(key);
        if (!value.equals(Boolean.TRUE)) {
          builder.append("(").append(toJson(value, multiline)).append(")");
        }
        builder.append("\n");
      }
    }
    return builder.toString();
  }

  // TODO: respect getEnclosingTask
  public String generateRecord(RecordTemplateSpec record) {
    StringBuilder builder = new StringBuilder();
    builder.append(schemadocAndProperties(record));
    builder.append("record ").append(escape(record.getClassName())).append(" {\n");
    for (RecordTemplateSpec.Field field: record.getFields()) {
      if (!field.getSchemaField().getRecord().equals(record.getSchema())) continue;
      builder.append(schemadocAndProperties(field));
      builder.append("  ").append(escape(field.getSchemaField().getName()));
      builder.append(": ").append(generateRefOrDecl(record, field.getType()));
      if (field.getSchemaField().getOptional()) {
        builder.append("?");
      }
      if (field.getSchemaField().getDefault() != null) {
        builder.append(" = ");
        builder.append(toJson(field.getSchemaField().getDefault(), false));
      }
      builder.append("\n");
    }
    builder.append("}");
    return builder.toString();
  }

  public String generateMap(CourierMapTemplateSpec map) {
    StringBuilder builder = new StringBuilder();
    builder
      .append("map[");
    if (map.getKeyClass() != null) {
      builder
        .append(generateRefOrDecl(map, map.getKeyClass()))
        .append(", ");
    } else {
      builder.append("string, ");
    }
    builder
      .append(generateRefOrDecl(map, map.getValueClass()))
      .append("]");
    return builder.toString();
  }

  public String generateArray(ArrayTemplateSpec array) {
    StringBuilder builder = new StringBuilder();
    builder
      .append("array[")
      .append(generateRefOrDecl(array, array.getItemClass()))
      .append("]");
    return builder.toString();
  }

  public String generateUnion(ClassTemplateSpec container, UnionTemplateSpec union) {
    if (container == null) {
      container = union;
    }
    StringBuilder builder = new StringBuilder();
    builder.append("union[");
    Iterator<UnionTemplateSpec.Member> iter = union.getMembers().iterator();
    while (iter.hasNext()) {
      UnionTemplateSpec.Member member = iter.next();
      builder.append(generateRefOrDecl(container, member.getClassTemplateSpec()));
      if (iter.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append("]");
    return builder.toString();
  }

  public String generateEnumeration(EnumTemplateSpec enumeration) {
    StringBuilder builder = new StringBuilder();
    builder.append(schemadocAndProperties(enumeration));
    builder.append("enum ").append(enumeration.getClassName()).append(" {\n");
    Iterator<String> iter = enumeration.getSchema().getSymbols().iterator();
    while (iter.hasNext()) {
      String symbol = iter.next();
      builder.append("  ").append(escape(symbol)).append("\n");
    }
    builder.append("}");
    return builder.toString();
  }

  public String generateTyperef(CourierTyperefTemplateSpec typeref) {
    StringBuilder builder = new StringBuilder();
    builder.append(schemadocAndProperties(typeref));
    builder.append("typeref ").append(escape(typeref.getClassName()));
    builder.append(" = ");
    builder.append(
      generateRefOrDecl(
        typeref,
        typeref.getRef()));
    return builder.toString();
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
