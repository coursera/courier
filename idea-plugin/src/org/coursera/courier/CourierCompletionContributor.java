package org.coursera.courier;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.coursera.courier.psi.CourierTokenType;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.CourierTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourierCompletionContributor extends CompletionContributor {
  public CourierCompletionContributor() {
    extend(CompletionType.BASIC,
      PlatformPatterns.psiElement(CourierTypes.IDENTIFIER).withLanguage(CourierLanguage.INSTANCE),
      new CompletionProvider<CompletionParameters>() {
        public void addCompletions(@NotNull CompletionParameters parameters,
                                   ProcessingContext context,
                                   @NotNull CompletionResultSet resultSet) {
          List<CourierTypeNameDeclaration> decls = CourierResolver.findTypeDeclarations(parameters.getEditor().getProject());
          for (String primitiveType: CourierTokenType.PRIMITIVE_TYPES) {
            resultSet.addElement(LookupElementBuilder.create(primitiveType));
          }
          for (CourierTypeNameDeclaration decl: decls) {
            resultSet.addElement(LookupElementBuilder.create(decl));
          }
        }
      }
    );
  }
}
