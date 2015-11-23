package org.coursera.courier.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import org.coursera.courier.CourierFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CourierElementFactory {
  @NotNull
  public static CourierFile createCourierFile(@NotNull Project project, @NonNls @NotNull String text) {
    @NonNls String filename = "dummy." + CourierFileType.INSTANCE.getDefaultExtension();
    return (CourierFile) PsiFileFactory.getInstance(project)
      .createFileFromText(filename, CourierFileType.INSTANCE, text);
  }

  public static CourierImportDeclaration createImport(@NotNull Project project, @NonNls @NotNull TypeName typeName) {
    CourierFile file = createCourierFile(project, "namespace dummynamespace\nimport " + typeName.unescape() + "\nrecord Dummy {}");
    return PsiTreeUtil.findChildOfType(file, CourierImportDeclaration.class);
  }

  public static CourierImportDeclarations createImports(@NotNull Project project, @NonNls @NotNull Collection<TypeName> typeNames) {
    StringBuilder builder = new StringBuilder();
    builder.append("namespace dummynamespace\n");
    for (TypeName typeName: typeNames) {
      builder.append("import " + typeName.unescape() + "\n");
    }
    builder.append("record Dummy {}");
    CourierFile file = createCourierFile(project, builder.toString());
    return PsiTreeUtil.findChildOfType(file, CourierImportDeclarations.class);
  }

  public static CourierTypeNameDeclaration createTypeNameDeclaration(@NotNull Project project, @NonNls @NotNull String simpleName) {
    CourierFile file = createCourierFile(project, "namespace dummynamespace\nrecord " + simpleName + " {}");
    return PsiTreeUtil.findChildOfType(file, CourierTypeNameDeclaration.class);
  }

  public static CourierTypeReference createCourierTypeReference(@NotNull Project project, @NonNls @NotNull String name) {
    CourierFile file = createCourierFile(project, "namespace dummynamespace\nrecord Dummy { dummyField: " + name + "}");
    return PsiTreeUtil.findChildOfType(file, CourierTypeReference.class);
  }
}
