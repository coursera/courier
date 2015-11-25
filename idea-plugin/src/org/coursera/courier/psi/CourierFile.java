package org.coursera.courier.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.coursera.courier.CourierFileType;
import org.coursera.courier.CourierLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CourierFile extends PsiFileBase {
  public CourierFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, CourierLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return CourierFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "Courier File";
  }

  @Override
  public Icon getIcon(int flags) {
    return super.getIcon(flags);
  }

  public CourierTypeNameDeclaration getPrimaryTypeDeclaration() {
    return PsiTreeUtil.findChildOfType(this, CourierTypeNameDeclaration.class);
  }

  public Collection<CourierTypeReference> getTypeReferences() {
    return PsiTreeUtil.findChildrenOfType(this, CourierTypeReference.class);
  }

  public CourierNamespace getNamespace() {
    return PsiTreeUtil.findChildOfType(this, CourierNamespace.class);
  }

  public void addImport(CourierImportDeclaration importDecl) {
    CourierTopLevel root = getRoot();
    CourierImportDeclarations importDecls = root.getImportDeclarations();
    if (importDecls.getImportDeclarationList().size() == 0) {
      importDecls = addFirstImport(importDecl);
    } else {
      importDecls = addNthImport(importDecl);
    }
    CodeStyleManager.getInstance(importDecls.getProject()).reformat(importDecls);
  }

  public CourierTopLevel getRoot() {
    return PsiTreeUtil.findChildOfType(this, CourierTopLevel.class);
  }

  /**
   * Position imports correctly when adding the first import.
   */
  private CourierImportDeclarations addFirstImport(CourierImportDeclaration importDecl) {
    TypeName first = importDecl.getFullname();
    CourierTopLevel root = getRoot();
    CourierImportDeclarations importDecls = root.getImportDeclarations();
    if (importDecls.getImportDeclarationList().size() == 0) {
      importDecls.delete();
      CourierImportDeclarations emptyImports =
        CourierElementFactory.createImports(getProject(), Collections.singleton(first));

      CourierNamespaceDeclaration namespaceDecl = root.getNamespaceDeclaration();
      if (namespaceDecl != null) {
        root.addAfter(emptyImports, namespaceDecl);
      } else {
        PsiElement firstChild = root.getFirstChild();
        if (firstChild != null) {
          root.addBefore(emptyImports, firstChild);
        } else {
          root.add(emptyImports);
        }
      }
      return emptyImports;
    } else {
      return importDecls;
    }
  }

  /**
   * Add an import to an existing import list, attempting to keep the list sorted.
   *
   * Note: this does not change the sort order of the existing import list.  It only attempts
   * to keep the list sorted if it already is by adding the import at the correct position.
   *
   */
  private CourierImportDeclarations addNthImport(CourierImportDeclaration importDecl) {
    CourierTopLevel root = getRoot();
    CourierImportDeclarations importDecls = root.getImportDeclarations();

    boolean added = false;
    for (CourierImportDeclaration existing : importDecls.getImportDeclarationList()) {
      if (importDecl.getFullname().toString().compareTo(existing.getFullname().toString()) < 0) {
        importDecls.addBefore(importDecl, existing);
        added = true;
        break;
      }
    }
    if (!added) {
      importDecls.add(importDecl);
    }
    return importDecls;
  }

  public Collection<CourierImportDeclaration> getImports() {
    return PsiTreeUtil.findChildrenOfType(getContainingFile(), CourierImportDeclaration.class);
  }

  private boolean isUsedImport(CourierImportDeclaration importDecl) {
    boolean found = false;
    for (CourierTypeReference ref: importDecl.getCourierFile().getTypeReferences()) {
      if (ref.getFullname().equals(importDecl.getFullname())) {
        found = true;
        break;
      }
    }
    return found;
  }

  public void optimizeImports() {
    CourierImportDeclarations importGroup = PsiTreeUtil.findChildOfType(getContainingFile(), CourierImportDeclarations.class);
    if (importGroup != null) {
      List<CourierImportDeclaration> importDecls = importGroup.getImportDeclarationList();
      if (importDecls.size() > 0) {
        for (CourierImportDeclaration importDecl : importDecls) {
          if (!importDecl.isUsed()) {
            importDecl.delete();
          }
        }

        List<CourierImportDeclaration> remaining = importGroup.getImportDeclarationList();
        if (remaining.size() > 0) {
          List<TypeName> typeNames = new ArrayList<TypeName>();
          for (CourierImportDeclaration importDecl: remaining) {
            typeNames.add(importDecl.getFullname());
          }
          Collections.sort(typeNames);
          importGroup.replace(CourierElementFactory.createImports(getProject(), typeNames));
        }
      }
    }
  }

  public TypeName lookupImport(String name) {
    Collection<CourierImportDeclaration> importDecls = getImports();
    for (CourierImportDeclaration importDecl : importDecls) {
      String importName = importDecl.getName();
      if (importName != null && importName.equals(name)) {
        return importDecl.getFullname();
      }
    }
    return null;
  }
}
