package org.coursera.courier;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;

public class CourierLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
  @NotNull
  @Override
  public Language getLanguage() {
    return CourierLanguage.INSTANCE;
  }

  @NotNull
  @Override
  public String getCodeSample(@NotNull SettingsType settingsType) {
    return DEFAULT_CODE_SAMPLE;
  }

  @Override
  public IndentOptionsEditor getIndentOptionsEditor() {
    return new SmartIndentOptionsEditor();
  }

  @Override
  public CommonCodeStyleSettings getDefaultCommonSettings() {
    CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(getLanguage());
    CommonCodeStyleSettings.IndentOptions indentOptions = defaultSettings.initIndentOptions();
    indentOptions.INDENT_SIZE = 2;
    indentOptions.CONTINUATION_INDENT_SIZE = 2;
    indentOptions.TAB_SIZE = 2;
    indentOptions.USE_TAB_CHARACTER = false;
    return defaultSettings;
  }

  private static final String DEFAULT_CODE_SAMPLE =
      "namespace org.example\n" +
      "\n" +
      "/** \n" +
      " * A Fortune.\n" +
      " */\n" +
      "@property1(\"property value\")\n" +
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
