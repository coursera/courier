package org.coursera.courier;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import org.coursera.courier.psi.CourierNamedElement;
import org.coursera.courier.psi.CourierTypeNameDeclaration;

public class CourierRefactoringSupportProvider extends RefactoringSupportProvider {
  @Override
  public boolean isMemberInplaceRenameAvailable(PsiElement element, PsiElement context) {
    return element instanceof CourierNamedElement || element instanceof CourierTypeNameDeclaration;
  }
}
