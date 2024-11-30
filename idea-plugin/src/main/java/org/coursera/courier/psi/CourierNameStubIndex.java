package org.coursera.courier.psi;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

public class CourierNameStubIndex extends StringStubIndexExtension<CourierTypeNameDeclaration> {
  public static final StubIndexKey KEY = StubIndexKey.createIndexKey("NAME");

  @NotNull
  @Override
  public StubIndexKey<String, CourierTypeNameDeclaration> getKey() {
    return KEY;
  }
}
