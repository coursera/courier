package org.coursera.courier;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.TypeName;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CourierChooseByNameContributor implements ChooseByNameContributor {
  @NotNull
  @Override
  public String[] getNames(Project project, boolean includeNonProjectItems) {
    List<CourierTypeNameDeclaration> typeDeclarations = CourierResolver.findTypeDeclarations(project);
    List<String> names = new ArrayList<String>(typeDeclarations.size());
    for (CourierTypeNameDeclaration typeDeclaration : typeDeclarations) {
      names.add(typeDeclaration.getFullname().toString());
    }
    return names.toArray(new String[names.size()]);
  }

  @NotNull
  @Override
  public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
    List<CourierTypeNameDeclaration> matches = CourierResolver.findTypeDeclarations(project, new TypeName(name));
    return matches.toArray(new NavigationItem[matches.size()]);
  }
}
