package org.coursera.courier.psi;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;

public class CourierTypeNameDeclarationStubImpl extends StubBase<CourierTypeNameDeclaration> implements CourierTypeNameDeclarationStub {
  TypeName fullname;

  public CourierTypeNameDeclarationStubImpl(final StubElement parent, TypeName fullname) {
    // TODO: remove cast, this should already be sorted out in CourierTypes.  Need to
    // fix the problem there.
    super(parent, (IStubElementType)CourierTypes.TYPE_NAME_DECLARATION);
    this.fullname = fullname;
  }

  @Override
  public TypeName getFullname() {
    return fullname;
  }
}
