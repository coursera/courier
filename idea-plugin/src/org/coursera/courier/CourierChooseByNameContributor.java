package org.coursera.courier;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.TypeName;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CourierChooseByNameContributor implements ChooseByNameContributor {
  @NotNull
  @Override
  public String[] getNames(Project project, boolean includeNonProjectItems) {
    List<CourierTypeNameDeclaration> typeDeclarations = CourierResolver.findTypeDeclarations(project);
    List<String> names = new ArrayList<String>(typeDeclarations.size());
    for (CourierTypeNameDeclaration typeDeclaration : typeDeclarations) {
      String simpleName = typeDeclaration.getName();
      if (simpleName != null) {
        names.add(simpleName);
      }
    }

    String[] matchArray = names.toArray(new String[names.size()]);
    Arrays.sort(matchArray);
    return matchArray;
  }

  @NotNull
  @Override
  public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
    List<CourierTypeNameDeclaration> matches = CourierResolver.findTypeDeclarationsByName(project, new TypeName(name).getName());

    CourierTypeNameDeclaration[] matchArray = matches.toArray(new CourierTypeNameDeclaration[matches.size()]);
    Arrays.sort(matchArray, DeclComparator.INSTANCE);
    return matchArray;
  }

  private static class DeclComparator implements Comparator<CourierTypeNameDeclaration> {
    public static final DeclComparator INSTANCE = new DeclComparator();

    public int compare(CourierTypeNameDeclaration lhs, CourierTypeNameDeclaration rhs) {
      if (lhs == rhs) return 0;
      else return lhs.getFullname().toString().compareTo(rhs.getFullname().toString());
    }
  }
}
