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
import org.coursera.courier.CourierResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CourierNamedElementReferenceBase extends CourierNamedElementBase {
  public CourierNamedElementReferenceBase(@NotNull ASTNode node) {
    super(node);
  }

  public TypeName getFullname() {
    CourierFullyQualifiedName nameNode = PsiTreeUtil.findChildOfType(this, CourierFullyQualifiedName.class);
    if (nameNode != null) {
      return toFullname(getCourierFile(), nameNode.getText());
    }
    return null;
  }

  public static TypeName toFullname(CourierFile courieFile, String unescapedName) {
    if (TypeName.isPrimitive(unescapedName) || unescapedName.contains(".")) {
      return TypeName.escaped(unescapedName);
    } else {
      TypeName importedName = courieFile.lookupImport(TypeName.escape(unescapedName));
      if (importedName != null) {
        return importedName;
      } else {
        CourierNamespace namespace = courieFile.getNamespace();
        if (namespace != null) {
          return TypeName.escaped(namespace.getText(), unescapedName);
        } else {
          return TypeName.escaped(unescapedName);
        }
      }
    }
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    if (getFullname().isPrimitive()) {
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
    if (getFullname().isPrimitive()) {
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
}
