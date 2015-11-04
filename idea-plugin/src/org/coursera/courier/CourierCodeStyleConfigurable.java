package org.coursera.courier;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class CourierCodeStyleConfigurable extends CodeStyleAbstractConfigurable {
  private static final Logger LOG = Logger.getInstance("#" + CourierCommenter.class.getName());

  public CourierCodeStyleConfigurable(@NotNull CodeStyleSettings settings, CodeStyleSettings cloneSettings) {
    super(settings, cloneSettings, "Courier");
  }

  @NotNull
  @Override
  protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
    return new CourierCodeStyleMainPanel(getCurrentSettings(), settings);
  }

  @Override
  public String getHelpTopic() {
    return null;
  }

  private static class CourierCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
    private CourierCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
      super(CourierLanguage.INSTANCE, currentSettings, settings);
    }

    @Override
    protected void addSpacesTab(CodeStyleSettings settings) {
    }

    @Override
    protected void addBlankLinesTab(CodeStyleSettings settings) {
    }

    @Override
    protected void addWrappingAndBracesTab(CodeStyleSettings settings) {
    }
  }
}
