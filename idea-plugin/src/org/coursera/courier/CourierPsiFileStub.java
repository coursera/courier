package org.coursera.courier;

import com.intellij.psi.stubs.PsiFileStubImpl;
import org.coursera.courier.psi.CourierFile;

public class CourierPsiFileStub extends PsiFileStubImpl<CourierFile> {
  public CourierPsiFileStub(CourierFile file) {
    super(file);
  }
}
