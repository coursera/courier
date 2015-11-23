package org.coursera.courier;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CourierFileType extends LanguageFileType {
  public static final CourierFileType INSTANCE = new CourierFileType();

  private CourierFileType() {
    super(CourierLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Courier schema";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Courier schema definition";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "courier";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return CourierIcons.FILE;
  }

  public static final String SAMPLE_CODE =
    "namespace org.example\n" +
      "\n" +
      "import org.example.Member1\n" +
      "import org.example.Member2\n" +
      "\n" +
      "/** \n" +
      " * A Fortune.\n" +
      " */\n" +
      "@language.propertyKey = \"property value\"\n" +
      "record Fortune {\n" +
      "  field1: int? = nil // comment 1\n" +
      "  field2: array[int] = [1, 2, 3]\n" +
      "  /* comment 2 */\n" +
      "  @deprecated\n" +
      "  field3: map[string, int] = { \"a\": 1, \"b\": 2 }\n" +
      "  inline: record Inline {\n" +
      "    inlineField1: union[Member1, Member2]\n" +
      "  }\n" +
      "}\n";
}
