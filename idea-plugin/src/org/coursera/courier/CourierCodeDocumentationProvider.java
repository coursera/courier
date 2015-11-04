package org.coursera.courier;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.lang.documentation.CodeDocumentationProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CourierCodeDocumentationProvider extends AbstractDocumentationProvider implements CodeDocumentationProvider {
  private static final Logger LOG = Logger.getInstance("#" + CourierCodeDocumentationProvider.class.getName());

  public CourierCodeDocumentationProvider() {
    super();
  }
  /**
   * Finds primary documentation comment within given context.
   * @param contextElement candidate psi comment
   * @return contextElement if no other applicable
   */
  @Nullable
  @Override
  public PsiComment findExistingDocComment(PsiComment contextElement) {
    return contextElement; // TODO: implement
  }

  /**
   * Examines PSI hierarchy identified by the given 'start' element trying to find element which can be documented
   * and it's doc comment (if any).
   * <p/>
   * Example:
   * <pre>
   *   int test() {
   *     return [caret] 1;
   *   }
   * </pre>
   * PSI element at the caret (return element) is an entry point. This method is expected to return PSI method element
   * and <code>'null'</code> as the existing doc comment then.
   *
   * @param startPoint  start traversal point
   * @return            comment anchor which is a given element or its anchor if the one is found and its doc comment (if existing).
   *                    This method may return <code>'null'</code> as an indication that no doc comment anchor and existing comment
   *                    is available;
   *                    returned pair must have non-null PSI element and nullable existing comment references then
   */
  @Nullable
  @Override
  public Pair<PsiElement, PsiComment> parseContext(@NotNull PsiElement startPoint) {
    return Pair.create(startPoint, null); // TODO: implement
  }

  /**
   * Generate documentation comment content for given context.
   * @param contextComment context psi comment
   * @return documentation content for given context if any
   */
  @Nullable
  @Override
  public String generateDocumentationContentStub(PsiComment contextComment) {
    return "";
  }
}
