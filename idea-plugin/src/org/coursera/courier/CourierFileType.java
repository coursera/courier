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
}
