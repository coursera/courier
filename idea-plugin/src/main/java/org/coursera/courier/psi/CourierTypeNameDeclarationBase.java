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

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import org.coursera.courier.CourierIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class CourierTypeNameDeclarationBase extends StubBasedPsiElementBase<CourierTypeNameDeclarationStub> implements CourierTypeNameDeclarationInterface, CourierTypeNameDeclaration, NavigationItem {
  public CourierTypeNameDeclarationBase(@NotNull ASTNode node) {
    super(node);
  }

  public CourierTypeNameDeclarationBase(CourierTypeNameDeclarationStub stub, IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String getName() {
    return getFullname().getName();
  }

  public CourierFile getCourierFile() {
    return (CourierFile)getContainingFile();
  }

  public TypeName getFullname() {
    ASTNode nameNode = getNode().findChildByType(CourierTypes.SIMPLE_NAME);
    if (nameNode != null) {
      String unescapedName = nameNode.getText();
      if (TypeName.isPrimitive(unescapedName)) {
        return TypeName.escaped(unescapedName);
      } else {
        CourierNamespace namespace = getCourierFile().getNamespace();
        if (namespace != null) {
          return TypeName.escaped(namespace.getText(), unescapedName);
        } else {
          return TypeName.escaped(unescapedName);
        }
      }
    }
    return null;
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    CourierTypeNameDeclaration replacement = CourierElementFactory.createTypeNameDeclaration(this.getProject(), name);
    getSimpleName().replace(replacement.getSimpleName());
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

  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      @Nullable
      @Override
      public String getPresentableText() {
        return getName();
      }

      @Nullable
      @Override
      public String getLocationString() {
        String namespace = getFullname().getNamespace();
        if (namespace != null) {
          return "(" + namespace + ")";
        } else {
          return "";
        }
      }

      @Nullable
      @Override
      public Icon getIcon(boolean unused) {
        return CourierIcons.FILE;
      }
    };
  }
}
