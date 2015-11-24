package org.coursera.courier.codestyle;

import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;

public class CourierCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
  @Override
  public String getConfigurableDisplayName() {
    return "Courier";
  }

  @NotNull
  @Override
  public Configurable createSettingsPage(@NotNull CodeStyleSettings settings, CodeStyleSettings originalSettings) {
    return new CourierCodeStyleConfigurable(settings, originalSettings);
  }
}
