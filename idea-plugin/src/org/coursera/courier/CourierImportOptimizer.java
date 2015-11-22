package org.coursera.courier;

import com.intellij.lang.ImportOptimizer;
import com.intellij.psi.PsiFile;
import org.coursera.courier.psi.CourierFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CourierImportOptimizer implements ImportOptimizer {
  @Override
  public boolean supports(PsiFile file) {
    return file instanceof CourierFile;
  }

  @NotNull
  @Override
  public Runnable processFile(final PsiFile file) {
    return new CollectingInfoRunnable() {
      @Nullable
      @Override
      public String getUserNotificationInfo() {
        return null;
      }

      @Override
      public void run() {
        if (file instanceof CourierFile) {
          CourierFile courierFile = (CourierFile) file;
          courierFile.optimizeImports();
        }
      }
    };
  }
}
