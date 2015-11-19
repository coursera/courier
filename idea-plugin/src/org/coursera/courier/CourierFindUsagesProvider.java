package org.coursera.courier;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import org.coursera.courier.psi.CourierNamedTypeDeclaration;
import org.coursera.courier.psi.CourierTypeDeclaration;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.CourierTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CourierFindUsagesProvider implements FindUsagesProvider {
  private static final DefaultWordsScanner WORDS_SCANNER =
    new DefaultWordsScanner(new CourierLexerAdapter(),
      TokenSet.create(CourierTypes.IDENTIFIER),
      TokenSet.create(
        CourierTypes.LINE_COMMENT,
        CourierTypes.BLOCK_COMMENT_NON_EMPTY,
        CourierTypes.BLOCK_COMMENT_EMPTY,
        CourierTypes.COMMA),
      TokenSet.create(
        CourierTypes.JSON_STRING,
        CourierTypes.JSON_NUMBER,
        CourierTypes.JSON_BOOLEAN,
        CourierTypes.JSON_NULL));

  @Nullable
  @Override
  public WordsScanner getWordsScanner() {
    return WORDS_SCANNER;
  }

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
    return psiElement instanceof PsiNamedElement;
  }

  @Nullable
  @Override
  public String getHelpId(@NotNull PsiElement psiElement) {
    return null;
  }

  @NotNull
  @Override
  public String getType(@NotNull PsiElement element) {
    if (element instanceof CourierTypeNameDeclaration) {
      return "type"; // TODO: lookup the data schema type
    } else {
      return "";
    }
  }

  @NotNull
  @Override
  public String getDescriptiveName(@NotNull PsiElement element) {
    if (element instanceof CourierTypeNameDeclaration) {
      return ((CourierTypeNameDeclaration)element).getFullname().toString();
    } else {
      return "";
    }
  }

  @NotNull
  @Override
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    if (element instanceof CourierTypeNameDeclaration) {
      return ((CourierTypeNameDeclaration)element).getName();
    } else {
      return "";
    }
  }
}
