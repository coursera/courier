package org.coursera.courier.psi;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.coursera.courier.CourierLanguage;
import org.coursera.courier.psi.impl.CourierTypeNameDeclarationImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CourierTypeDeclarationStubType extends IStubElementType<CourierTypeNameDeclarationStub, CourierTypeNameDeclaration> {
  public CourierTypeDeclarationStubType(String debugName) {
    super(debugName, CourierLanguage.INSTANCE);
  }

  @Override
  public CourierTypeNameDeclaration createPsi(@NotNull CourierTypeNameDeclarationStub stub) {
    return new CourierTypeNameDeclarationImpl(stub, this);
  }

  @Override
  public CourierTypeNameDeclarationStub createStub(@NotNull CourierTypeNameDeclaration psi, StubElement parentStub) {
    return new CourierTypeNameDeclarationStubImpl(parentStub, psi.getFullname());
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "courier.typedecl";
  }

  @Override
  public void serialize(@NotNull CourierTypeNameDeclarationStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getFullname().toString());
  }

  @NotNull
  @Override
  public CourierTypeNameDeclarationStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new CourierTypeNameDeclarationStubImpl(parentStub, new TypeName(dataStream.readName().getString()));
  }

  @Override
  public void indexStub(@NotNull CourierTypeNameDeclarationStub stub, @NotNull IndexSink sink) {
    sink.occurrence(CourierNameStubIndex.KEY, stub.getFullname().getName());
    sink.occurrence(CourierFullnameStubIndex.KEY, stub.getFullname().toString());
  }
}
