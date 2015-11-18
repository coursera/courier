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

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.coursera.courier.CourierIcons;
import org.coursera.courier.CourierResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
    PsiElement resolved = resolve();
    if (resolved != null) {
      return resolved.equals(element);
    }
    return false;
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    Project project = myElement.getProject();
    List<CourierTypeReference> reference = CourierResolver.findTypeReferences(project, target.getFullname());
    List<LookupElement> variants = new ArrayList<LookupElement>();
    for (final CourierTypeReference property : reference) {
      variants.add(LookupElementBuilder.create(property).
        withIcon(CourierIcons.FILE).
        withTypeText(property.getContainingFile().getName())
      );
    }
    return variants.toArray();
  }
}
