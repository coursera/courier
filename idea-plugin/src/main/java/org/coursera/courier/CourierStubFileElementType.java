package org.coursera.courier;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IStubFileElementType;

public class CourierStubFileElementType extends IStubFileElementType<CourierPsiFileStub> {

  public CourierStubFileElementType(Language language) {
    super(language);
  }

  @Override
  public int getStubVersion() {
    return 4;
  }

  public String getExternalId() {
    return "courier.FILE";
  }
}
