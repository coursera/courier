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
import com.intellij.psi.search.GlobalSearchScope;
import org.coursera.courier.psi.CourierFullnameStubIndex;
import org.coursera.courier.psi.CourierNameStubIndex;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.TypeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CourierResolver {
  public static List<CourierTypeNameDeclaration> findTypeDeclarations(Project project) {
    CourierFullnameStubIndex index = CourierFullnameStubIndex.EP_NAME.findExtension(CourierFullnameStubIndex.class);
    Collection<String> fullnames = index.getAllKeys(project);

    List<CourierTypeNameDeclaration> results = new ArrayList<CourierTypeNameDeclaration>();
    for (String fullname: fullnames) {
      results.addAll(index.get(fullname, project, GlobalSearchScope.allScope(project)));
    }
    return results;
  }

  public static List<CourierTypeNameDeclaration> findTypeDeclarationsByName(Project project, String name) {
    CourierNameStubIndex index = CourierFullnameStubIndex.EP_NAME.findExtension(CourierNameStubIndex.class);
    Collection<CourierTypeNameDeclaration> decls = index.get(name, project, GlobalSearchScope.allScope(project));
    return new ArrayList<CourierTypeNameDeclaration>(decls);
  }

  public static CourierTypeNameDeclaration findTypeDeclaration(Project project, TypeName fullname) {
    CourierFullnameStubIndex index = CourierFullnameStubIndex.EP_NAME.findExtension(CourierFullnameStubIndex.class);
    Collection<CourierTypeNameDeclaration> results = index.get(fullname.toString(), project, GlobalSearchScope.allScope(project));
    if (results.size() > 0) {
      return results.iterator().next();
    } else {
      return null;
    }
  }
}
