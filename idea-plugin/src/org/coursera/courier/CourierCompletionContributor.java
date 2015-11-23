package org.coursera.courier;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.navigation.ItemPresentation;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.coursera.courier.psi.CourierElementFactory;
import org.coursera.courier.psi.CourierFile;
import org.coursera.courier.psi.CourierImportDeclaration;
import org.coursera.courier.psi.CourierNamedElementReferenceBase;
import org.coursera.courier.psi.CourierTokenType;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.CourierTypes;
import org.coursera.courier.psi.TypeName;
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
          List<CourierTypeNameDeclaration> decls =
            CourierResolver.findTypeDeclarations(parameters.getEditor().getProject());
          for (String primitiveType: CourierTokenType.PRIMITIVE_TYPES) {
            resultSet.addElement(LookupElementBuilder.create(primitiveType));
          }
          for (CourierTypeNameDeclaration decl: decls) {
            LookupElementBuilder builder = LookupElementBuilder.create(decl);
            ItemPresentation presentation = decl.getPresentation();
            if (presentation != null && presentation.getPresentableText() != null) {
              builder = builder
                .withTailText(" " + presentation.getLocationString())
                .withIcon(CourierIcons.FILE);
            }
            builder = builder.withInsertHandler(addImportHandler);
            resultSet.addElement(builder);
          }
        }
      }
    );
  }

  private static AddImportHandler addImportHandler = new AddImportHandler();

  /**
   * Add import to referenced type if it is in a different namespace and is not already imported.
   */
  private static class AddImportHandler implements InsertHandler<LookupElement> {
    @Override
    public void handleInsert(InsertionContext context, LookupElement declaration) {
      PsiElement declarationElement = declaration.getPsiElement();
      if (declarationElement != null) {
        PsiFile referenceFile = context.getFile();
        PsiFile declarationFile = declarationElement.getContainingFile();
        if (declarationFile instanceof CourierFile && referenceFile instanceof CourierFile) {
          CourierFile referenceCourierFile = (CourierFile) referenceFile;
          CourierFile declarationCourierFile = (CourierFile) declarationFile;
          TypeName declarationFullname =
            CourierNamedElementReferenceBase.toFullname(
              declarationCourierFile, declaration.getLookupString());
          if (declarationFullname != null) {
            addImport(referenceCourierFile, declarationFullname);
          }
        }
      }
    }

    public void addImport(CourierFile referenceCourierFile, TypeName declarationFullname) {
      String declNamespace = declarationFullname.getNamespace();
      String referenceFileNamespace = TypeName.unescape(referenceCourierFile.getNamespace().getText());
      if (!declNamespace.equals(referenceFileNamespace)) {
        CourierImportDeclaration importDecl =
          CourierElementFactory.createImport(referenceCourierFile.getProject(), declarationFullname);
        if (referenceCourierFile.lookupImport(declarationFullname.getName()) == null) {
          referenceCourierFile.addImport(importDecl);
        }
      }
    }
  }
}
