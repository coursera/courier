/*
 * Copyright 2015 Coursera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.coursera.courier.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CourierReference extends PsiReferenceBase<PsiElement> {

  private CourierTypeNameDeclaration target;

  public CourierReference(@NotNull PsiElement element, CourierTypeNameDeclaration target, TextRange textRange) {
    super(element, textRange);
    this.target = target;
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    return target;
  }

  @Override
  public boolean isReferenceTo(PsiElement element) {
    if (!(element instanceof  CourierTypeNameDeclaration)) return false;
    PsiElement resolved = resolve();
    if (resolved != null) {
      return resolved.equals(element);
    }
    return false;
  }

  @Override
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    PsiElement element = getElement();
    if (element instanceof CourierImportDeclaration) {
      CourierImportDeclaration importDecl = (CourierImportDeclaration) element;
      return importDecl.setName(newElementName);
    } else if (element instanceof CourierTypeReference) {
      CourierTypeReference reference = (CourierTypeReference) element;
      return reference.setName(newElementName);
    } else {
      return element;
    }
  }

  @Override
  public String toString() {
    return target.getContainingFile().getName();
  }

  @NotNull
  @Override
  public Object[] getVariants() {

    return new Object[] {};
  }
}
