package org.coursera.courier;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;
import org.coursera.courier.android.TypedDefinitions;
import org.coursera.courier.api.GeneratedCode;
import org.coursera.courier.api.GeneratedCodeTargetFile;
import org.coursera.courier.api.PegasusCodeGenerator;
import org.rythmengine.RythmEngine;
import org.rythmengine.resource.ClasspathResourceLoader;

import java.util.Collection;
import java.util.Collections;

public class AndroidGenerator implements PegasusCodeGenerator {
  private final RythmEngine engine;

  public AndroidGenerator() {
    engine = new RythmEngine();
    engine.registerResourceLoader(new ClasspathResourceLoader(engine, "/"));
  }

  public static class JavaCompilationUnit extends GeneratedCodeTargetFile {
    public JavaCompilationUnit(String name, String namespace) {
      super(name, namespace, "java");
    }
  }

  @Override
  public GeneratedCode generate(ClassTemplateSpec templateSpec) {

    // TODO: for custom types, will need to use something like: https://sites.google.com/site/gson/gson-type-adapters-for-common-classes-1
    String code;
    if (templateSpec instanceof RecordTemplateSpec) {
      code = engine.render("rythm/record.txt", templateSpec);
    } else if (templateSpec instanceof EnumTemplateSpec) {
      code = engine.render("rythm/enum.txt", templateSpec);
    } else if (templateSpec instanceof UnionTemplateSpec) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec) templateSpec;
      if (TypedDefinitions.isTypedDefinition(unionSpec)) {
        code = engine.render(
            "rythm/typedDefinition.txt",
            templateSpec,
            TypedDefinitions.getTypedDefinitionMapping(unionSpec, false));
      } else if (TypedDefinitions.isFlatTypedDefinition(unionSpec)) {
        code = engine.render(
            "rythm/flatTypedDefinition.txt",
            templateSpec,
            TypedDefinitions.getTypedDefinitionMapping(unionSpec, true));
      } else {
        code = engine.render("rythm/union.txt", templateSpec);
      }
    } else {
      return null; //throw new IllegalArgumentException("Unrecognized template spec type: " + templateSpec.getClass());
    }
    JavaCompilationUnit compilationUnit = new JavaCompilationUnit(templateSpec.getClassName(), templateSpec.getNamespace());
    // TODO: find a better java source formatter, jalopy is crazy old and depends on obsolete log4j apis
    /*
    StringBuffer sb = new StringBuffer();
    Jalopy formatter = new Jalopy();
    formatter.setInput(code, compilationUnit.toFile(new File("")).getAbsolutePath());
    formatter.setOutput(sb);
    formatter.format();
    code = sb.toString();*/

    return new GeneratedCode(compilationUnit, code);
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
  public String language() {
    return "java";
  }
}
