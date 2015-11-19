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

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.containers.HashSet;
import org.coursera.courier.CourierResolver;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CourierNamedElementReference extends CourierNamedElementBase {
  public CourierNamedElementReference(@NotNull ASTNode node) {
    super(node);
  }

  public String getFullname() {
    CourierFullyQualifiedName qualifiedName = PsiTreeUtil.findChildOfType(this, CourierFullyQualifiedName.class);
    if (qualifiedName != null) {
      String rawName = qualifiedName.getText();
      if (CourierTokenType.PRIMITIVE_TYPES.contains(rawName)) {
        return rawName;
      }
      if (rawName.contains(".")) {
        return rawName;
      } else {
        Map<String, String> imports = importsByName(PsiTreeUtil.findChildrenOfType(getContainingFile(), CourierImportDeclaration.class));
        String importedName = imports.get(rawName);
        if (importedName != null) {
          return importedName;
        } else {
          CourierNamespace namespace = PsiTreeUtil.findChildOfType(getContainingFile(), CourierNamespace.class);
          if (namespace != null) {
            return namespace.getText() + "." + rawName;
          }
        }
      }
    }
    return null;
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    ASTNode qualifiedName = getNode().findChildByType(CourierTypes.FULLY_QUALIFIED_NAME);
    if (qualifiedName != null) {
      //qualifiedName.getLastChildNode().getPsi()
      // TODO(jbetz): figure out how to replace a node properly
    }
    return this;
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    if (CourierTokenType.PRIMITIVE_TYPES.contains(getFullname())) {
      return null;
    }
    ASTNode qualifiedName = getNode().findChildByType(CourierTypes.FULLY_QUALIFIED_NAME);
    if (qualifiedName != null) {
      return qualifiedName.getLastChildNode().getPsi();
    }
    return null;
  }

  @Override
  public PsiReference getReference() {
    if (CourierTokenType.PRIMITIVE_TYPES.contains(getFullname())) {
      return null;
    }
    CourierTypeNameDeclaration declaration = CourierResolver.findTypeDeclaration(getProject(), getFullname());
    if (declaration != null) {
      // need the offsets of the declaration name within the text of this element
      String name = declaration.getName();
      int start = getText().lastIndexOf(name);
      int end = start + name.length();
      return new CourierReference(this, declaration, new TextRange(start, end));
    }
    return null;
  }

  public static Map<String, String> importsByName(Collection<CourierImportDeclaration> imports) {
    Map<String, String> byName = new HashMap<String, String>();
    for (CourierImportDeclaration importDeclaration : imports) {
      byName.put(importDeclaration.getName(), importDeclaration.getFullname());
    }
    return byName;
  }
}
