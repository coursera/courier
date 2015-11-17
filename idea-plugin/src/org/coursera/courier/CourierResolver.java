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

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.coursera.courier.psi.CourierFile;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.CourierTypeReference;
import org.coursera.courier.psi.TypeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// TODO(jbetz): All the scans preformed here are obviously inefficient, we should replace with
// a IntelliJ style index of some kind..
public class CourierResolver {
  public static List<CourierTypeNameDeclaration> findTypeDeclarations(Project project) {
    List<CourierTypeNameDeclaration> results = new ArrayList<CourierTypeNameDeclaration>();
    for (CourierFile simpleFile: getCourierFiles(project)) {
      if (simpleFile != null) {
        CourierTypeNameDeclaration declaration = simpleFile.getPrimaryTypeDeclaration();
        if (declaration != null) {
          results.add(declaration);
        }
      }
    }
    return results;
  }

  public static List<CourierTypeNameDeclaration> findTypeDeclarations(Project project, TypeName fullname) {
    // TODO: Optimize. Only files in the namespace of the fullname need to be considered.
    List<CourierTypeNameDeclaration> results = new ArrayList<CourierTypeNameDeclaration>();
    for (CourierFile simpleFile: getCourierFiles(project)) {
      CourierTypeNameDeclaration declaration = simpleFile.getPrimaryTypeDeclaration();
      if (declaration != null && declaration.getFullname().equals(fullname)) {
        results.add(declaration);
      }
    }
    return results;
  }

  public static CourierTypeNameDeclaration findTypeDeclaration(Project project, TypeName fullname) {
    // TODO: Optimize. Only files in the namespace of the fullname need to be considered.
    for (CourierFile simpleFile: getCourierFiles(project)) {
      CourierTypeNameDeclaration declaration = simpleFile.getPrimaryTypeDeclaration();
      if (declaration != null) {
        TypeName typeFullname = declaration.getFullname();
        if (typeFullname.equals(fullname)) {
          return declaration;
        }
      }
    }
    return null;
  }

  public static List<CourierTypeReference> findTypeReferences(Project project, TypeName fullname) {
    // TODO: Optimize. Only files in the namespace of the fullname need to be considered.
    List<CourierTypeReference> result = null;
    for (CourierFile simpleFile: getCourierFiles(project)) {
      Collection<CourierTypeReference> references = simpleFile.getTypeReferences();
      for (CourierTypeReference reference : references) {
        if (reference.getFullname().equals(fullname)) {
          if (result == null) {
            result = new ArrayList<CourierTypeReference>();
          }
          result.add(reference);
        }
      }
    }
    return result != null ? result : Collections.<CourierTypeReference>emptyList();
  }

  private static Iterable<CourierFile> getCourierFiles(final Project project) {
    Collection<VirtualFile> virtualFiles =
      FileBasedIndex.getInstance().getContainingFiles(
        FileTypeIndex.NAME, CourierFileType.INSTANCE, GlobalSearchScope.allScope(project));

    Iterable<CourierFile> files = Iterables.transform(
      virtualFiles,
      new Function<VirtualFile, CourierFile>() {
      @Override
      public CourierFile apply(VirtualFile virtualFile) {
        return (CourierFile) PsiManager.getInstance(project).findFile(virtualFile);
      }
    });
    return Iterables.filter(files, Predicates.notNull());
  }
}
