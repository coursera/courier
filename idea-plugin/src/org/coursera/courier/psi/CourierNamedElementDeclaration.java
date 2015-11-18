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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.coursera.courier.CourierResolver;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CourierNamedElementDeclaration extends CourierNamedElementBase {
  public CourierNamedElementDeclaration(@NotNull ASTNode node) {
    super(node);
  }

  public String getFullname() {
    ASTNode simpleName = getNode().findChildByType(CourierTypes.SIMPLE_NAME);
    if (simpleName != null) {
      CourierNamespace namespace = PsiTreeUtil.findChildOfType(getContainingFile(), CourierNamespace.class);
      if (namespace != null) {
        return namespace.getText() + "." + simpleName.getText();
      } else {
        return simpleName.getText();
      }
    }
    return null;
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    ASTNode identifierNode = getNode().findChildByType(CourierTypes.SIMPLE_NAME);
    if (identifierNode != null) {
      // TODO(jbetz): figure out how to replace a node properly
    }
    return this;
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    ASTNode simpleName = getNode().findChildByType(CourierTypes.SIMPLE_NAME);
    if (simpleName != null) {
      return simpleName.getFirstChildNode().getPsi();
    }
    return null;
  }
}
