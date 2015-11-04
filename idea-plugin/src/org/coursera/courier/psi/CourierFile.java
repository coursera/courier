package org.coursera.courier.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.coursera.courier.CourierFileType;
import org.coursera.courier.CourierLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CourierFile extends PsiFileBase {
  public CourierFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, CourierLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return CourierFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "Courier File";
  }

  @Override
  public Icon getIcon(int flags) {
    return super.getIcon(flags);
  }
}
