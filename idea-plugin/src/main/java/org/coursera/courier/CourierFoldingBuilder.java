package org.coursera.courier;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.coursera.courier.psi.CourierImportDeclarations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CourierFoldingBuilder extends FoldingBuilderEx {
  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
    CourierImportDeclarations imports = PsiTreeUtil.findChildOfType(root, CourierImportDeclarations.class);
    if (imports != null && imports.getImportDeclarationList().size() > 0) {
      return new FoldingDescriptor[] {
        new FoldingDescriptor(imports, imports.getTextRange())
      };
    } else {
      return new FoldingDescriptor[] {};
    }
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    return false;
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    return "...";
  }
}
