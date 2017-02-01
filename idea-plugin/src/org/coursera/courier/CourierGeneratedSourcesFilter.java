package org.coursera.courier;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.roots.GeneratedSourcesFilter;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.TypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.scala.lang.psi.api.base.ScLiteral;
import org.jetbrains.plugins.scala.lang.psi.api.expr.*;
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.ScClass;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import scala.collection.JavaConversions;

/**
 * TODOs:
 * More proper identification of generated files.
 * More proper finding of the corresponding courier PsiElement.
 * The ability to jump from courier file to generated scala file.
 */

public class CourierGeneratedSourcesFilter extends GeneratedSourcesFilter {

    private static final Logger log = Logger.getInstance(CourierGeneratedSourcesFilter.class);

    @Override
    public boolean isGeneratedSource(@NotNull VirtualFile file, @NotNull Project project) {
        // This function determines a file to be generated when it contains at least one class with
        // a `@Generated` annotation with `comments = "Courier Data Template."`.

        String canonicalPath = file.getCanonicalPath();
        log.info("isGeneratedSource: " + canonicalPath);

        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile == null) {
            return false;
        }

        ScClass generatedClass = findGeneratedClassDescendant(psiFile);
        if (generatedClass == null) {
            log.info("isGeneratedSource generatedClass: null");
            return false;
        }
        log.info("isGeneratedSource generatedClass: " + generatedClass.getName());
        return generatedClass != null;
    }

    @NotNull
    @Override
    public List<PsiElement> getOriginalElements(@NotNull PsiElement element) {
        Project project = element.getProject();
        String canonicalPath = element.getContainingFile().getVirtualFile().getCanonicalPath();
        log.info("getOriginalElements: " + canonicalPath);

        ScClass generatedClass = findGeneratedClassAncestor(element);
        if (generatedClass == null) {
            return Collections.emptyList();
        }

        log.info("getOriginalElements class: " + generatedClass.getText());

        PsiReference generatedClassReference = generatedClass.getReference();
        if (generatedClassReference == null) {
            return Collections.emptyList();
        }

        log.info("getOriginalElements generatedClassReference: " + generatedClassReference.getCanonicalText());

        TypeName typeName = new TypeName(generatedClassReference.getCanonicalText());
        CourierTypeNameDeclaration declaration =
                CourierResolver.findTypeDeclaration(project, typeName);
        if (declaration == null) {
            return Collections.emptyList();
        }

        log.info("getOriginalElements declaration: " + declaration.getText());

        return Collections.singletonList((PsiElement) declaration);

        /*
        if (canonicalPath != null && canonicalPath.contains("src_managed")) {
            Project project = element.getProject();

            String originalElementFilePath = canonicalPath.replace(
                    "target/scala-2.11/src_managed/main/courier",
                    "src/main/pegasus").replace("scala", "courier");
            File originalElementFile = new File(originalElementFilePath);

            log.info("originalElementFilePath: " + originalElementFilePath);

            VirtualFile originalElementVirtualFile =
                    LocalFileSystem.getInstance().findFileByIoFile(originalElementFile);
            if (originalElementVirtualFile == null) {
                return Collections.emptyList();
            }

            PsiFile originalElementPsiFile =
                    PsiManager.getInstance(project).findFile(originalElementVirtualFile);
            if (originalElementPsiFile == null) {
                return Collections.emptyList();
            }

            for (PsiElement child : originalElementPsiFile.getChildren()) {
                log.info("child: " + child.toString());
                if (child instanceof CourierTopLevel) {
                    return Collections.singletonList(child);
                }
            }
        }*/
    }

    @Nullable
    private ScClass findGeneratedClassAncestor(@NotNull PsiElement element) {
        if (element instanceof ScClass && isGeneratedClass((ScClass) element)) {
            return (ScClass) element;
        } else if (element.getParent() != null) {
            return findGeneratedClassAncestor(element.getParent());
        } else {
            return null;
        }
    }

    @Nullable
    private ScClass findGeneratedClassDescendant(@NotNull PsiElement element) {
        log.info("findGeneratedClassDescendant: " + element.toString());
        if (element instanceof ScClass && isGeneratedClass((ScClass) element)) {
            return (ScClass) element;
        }
        for (PsiElement child : element.getChildren()) {
            ScClass generatedClass = findGeneratedClassDescendant(child);
            if (generatedClass != null) {
                return generatedClass;
            }
        }
        return null;
    }

    private boolean isGeneratedClass(ScClass classElement) {
        Collection<ScAnnotation> annotations =
                JavaConversions.asJavaCollection(classElement.allMatchingAnnotations("javax.annotation.Generated"));
        log.info("isGeneratedClass annotations: " + annotations.size());

        for (String annotationName : JavaConversions.asJavaCollection(classElement.annotationNames())) {
            log.info("isGeneratedClass annotationName: " + annotationName);
        }

        for (ScAnnotation annotation : annotations) {
            log.info("isGeneratedClass annotation:" + annotation.getText());

            ScArgumentExprList argumentExprList = annotation.annotationExpr().constr().args().getOrElse(null);
            if (argumentExprList == null) {
                continue;
            }

            for (ScExpression argumentExpr : argumentExprList.exprsArray()) {
                log.info("isGeneratedClass argumentExpr: " + argumentExpr.getText());

                if (!(argumentExpr instanceof ScAssignStmt)) {
                    continue;
                }
                ScAssignStmt assignStmt = (ScAssignStmt) argumentExpr;

                log.info("isGeneratedClass assignStmt: " + assignStmt.getText());

                if (!(assignStmt.getLExpression() instanceof ScReferenceExpression)) {
                    continue;
                }
                ScReferenceExpression lExpression = (ScReferenceExpression) assignStmt.getLExpression();
                log.info("isGeneratedClass lExpression: " + lExpression.getText());
                if (!lExpression.getCanonicalText().equals("comments")) {
                    continue;
                }

                if (!(assignStmt.getRExpression().getOrElse(null) instanceof ScLiteral) || assignStmt.getRExpression().getOrElse(null) == null) {
                    continue;
                }
                ScLiteral rExpression = (ScLiteral) assignStmt.getRExpression().getOrElse(null);
                log.info("isGeneratedClass rExpression: " + rExpression.getText());
                if (!rExpression.getText().equals("Courier Data Template.")) {
                    continue;
                }

                return true;
            }
        }

        return false;

    }

}
