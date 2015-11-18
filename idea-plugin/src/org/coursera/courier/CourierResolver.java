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

package org.coursera.courier;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import org.coursera.courier.psi.CourierFile;
import org.coursera.courier.psi.CourierImportDeclaration;
import org.coursera.courier.psi.CourierNamespace;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.CourierTypeReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourierResolver {
  public static CourierTypeNameDeclaration findTypeDeclaration(Project project, String fullname) {
    Collection<VirtualFile> virtualFiles =
      FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, CourierFileType.INSTANCE, GlobalSearchScope.allScope(project));
    for (VirtualFile virtualFile : virtualFiles) {
      CourierFile simpleFile = (CourierFile) PsiManager.getInstance(project).findFile(virtualFile);
      if (simpleFile != null) {
        CourierTypeNameDeclaration declaration = PsiTreeUtil.findChildOfType(simpleFile, CourierTypeNameDeclaration.class);
        if (declaration != null) {
          String typeFullname = declaration.getFullname();
          if (typeFullname.equals(fullname)) {
            return declaration;
          }
        }
      }
    }
    return null;
  }

  public static List<CourierTypeReference> findTypeReferences(Project project, String fullname) {
    List<CourierTypeReference> result = null;
    Collection<VirtualFile> virtualFiles =
      FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, CourierFileType.INSTANCE, GlobalSearchScope.allScope(project));
    for (VirtualFile virtualFile : virtualFiles) {
      CourierFile simpleFile = (CourierFile) PsiManager.getInstance(project).findFile(virtualFile);
      if (simpleFile != null) {
        Collection<CourierTypeReference> references = PsiTreeUtil.findChildrenOfType(simpleFile, CourierTypeReference.class);
        for (CourierTypeReference reference : references) {
          if (reference.getFullname().equals(fullname)) {
            if (result == null) {
              result = new ArrayList<CourierTypeReference>();
            }
            result.add(reference);
          }
        }
      }
    }
    return result != null ? result : Collections.<CourierTypeReference>emptyList();
  }
}
