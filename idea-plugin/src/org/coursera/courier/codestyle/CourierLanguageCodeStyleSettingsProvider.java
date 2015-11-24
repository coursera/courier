package org.coursera.courier.codestyle;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import org.coursera.courier.CourierFileType;
import org.coursera.courier.CourierLanguage;
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
    return CourierFileType.SAMPLE_CODE;
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
}
