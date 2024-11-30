package org.coursera.courier;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.CourierTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CourierFindUsagesProvider implements FindUsagesProvider {

  private static final TokenSet identifiers =
    TokenSet.create(CourierTypes.IDENTIFIER);

  private static final TokenSet comments = TokenSet.create(
    CourierTypes.LINE_COMMENT,
    CourierTypes.BLOCK_COMMENT_NON_EMPTY,
    CourierTypes.BLOCK_COMMENT_EMPTY,
    CourierTypes.COMMA);

  private static final TokenSet literals = TokenSet.create(
    CourierTypes.JSON_STRING,
    CourierTypes.JSON_NUMBER,
    CourierTypes.JSON_BOOLEAN,
    CourierTypes.JSON_NULL);

  @Nullable
  @Override
  public WordsScanner getWordsScanner() {
    // See https://devnet.jetbrains.com/message/5537369
    return new DefaultWordsScanner(new CourierLexerAdapter(), identifiers, comments, literals);
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
      CourierTypeNameDeclaration declaration = (CourierTypeNameDeclaration) element;
      String name = declaration.getName();
      if (name != null) {
        return name;
      } else {
        return "";
      }
    } else {
      return "";
    }
  }
}
