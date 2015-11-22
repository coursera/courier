package org.coursera.courier.psi;

import com.intellij.psi.stubs.StubElement;

public interface CourierTypeNameDeclarationStub extends StubElement<CourierTypeNameDeclaration> {
  TypeName getFullname();
}
