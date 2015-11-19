package org.coursera.courier;

import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.CreateTemplateInPackageAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.ArrayFactory;
import com.intellij.util.IncorrectOperationException;
import org.coursera.courier.psi.CourierEnumDeclaration;
import org.coursera.courier.psi.CourierFile;
import org.coursera.courier.psi.CourierNamedTypeDeclaration;
import org.coursera.courier.psi.CourierRecordDeclaration;
import org.coursera.courier.psi.CourierTopLevel;
import org.coursera.courier.psi.CourierTypeAssignment;
import org.coursera.courier.psi.CourierTypeDeclaration;
import org.coursera.courier.psi.CourierTypeNameDeclaration;
import org.coursera.courier.psi.CourierTypeReference;
import org.coursera.courier.psi.CourierTyperefDeclaration;
import org.coursera.courier.psi.CourierTypes;
import org.coursera.courier.psi.CourierUnionDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class NewCourierTypeAction extends CreateTemplateInPackageAction<CourierTopLevel> implements DumbAware {

  public NewCourierTypeAction() {
    super(
      "New Courier Type",
      "Create a new courier schema",
      CourierIcons.FILE,
      Collections.singleton(JavaSourceRootType.SOURCE));
  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder
      .setTitle("New Courier Type")
      .addKind("Record", CourierIcons.FILE, "Record.courier")
      .addKind("Enum", CourierIcons.FILE, "Enum.courier")
      .addKind("Union", CourierIcons.FILE, "Union.courier")
      .addKind("Typeref", CourierIcons.FILE, "Typeref.courier");
    for (FileTemplate template : FileTemplateManager.getInstance(project).getAllTemplates()) {
      FileType fileType = FileTypeManagerEx.getInstanceEx().getFileTypeByExtension(template.getExtension());
      if (fileType.equals(CourierFileType.INSTANCE) && JavaDirectoryService.getInstance().getPackage(directory) != null) {
        builder.addKind(template.getName(), CourierIcons.FILE, template.getName());
      }
    }
  }

  @Override
  protected boolean checkPackageExists(PsiDirectory directory) {
    return JavaDirectoryService.getInstance().getPackage(directory) != null;
  }

  @Override
  protected String getActionName(PsiDirectory directory, String newName, String templateName) {
    return "New Courier Record";
  }

  @Override
  protected PsiElement getNavigationElement(@NotNull CourierTopLevel createdElement) {
    return createdElement;
  }

  @Override
  protected final CourierTopLevel doCreate(
      PsiDirectory dir, String className, String templateName)
      throws IncorrectOperationException {
    try {
      final String fileName = className + ".courier";
      Project project = dir.getProject();

      FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance(project);
      final FileTemplate fileTemplate = fileTemplateManager.getInternalTemplate(templateName);
      final PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);

      Properties properties = new Properties(fileTemplateManager.getDefaultProperties());
      properties.setProperty("PACKAGE_NAME", getNamespace(dir));
      properties.setProperty("NAME", className);

      PsiFile file =
        psiFileFactory.createFileFromText(
          fileName, CourierFileType.INSTANCE, fileTemplate.getText(properties));
      file = (PsiFile) dir.add(file);

      if (file instanceof CourierFile) {
        CourierFile courierFile = (CourierFile) file;
        CodeStyleManager.getInstance(file.getManager()).reformat(file);
        return courierFile
          .calcTreeElement()
          .getChildrenAsPsiElements(CourierTypes.TOP_LEVEL, ARRAY_FACTORY)[0];
      }
      throw new IncorrectOperationException(".courier extension is not mapped to Courier file type: " + fileTemplate.getClass());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getNamespace(@NotNull PsiDirectory directory) {
    PsiPackage ns = JavaDirectoryService.getInstance().getPackage(directory);
    return ns != null ? ns.getQualifiedName() : "UNKNOWN";
  }

  @Override
  protected void postProcess(CourierTopLevel createdElement, String templateName, Map<String, String> customProperties) {
    super.postProcess(createdElement, templateName, customProperties);

    final Project project = createdElement.getProject();
    final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    if (editor != null) {
      CourierNamedTypeDeclaration decl = createdElement.getNamedTypeDeclaration();
      if (decl.getRecordDeclaration() != null) {
        CourierRecordDeclaration record = decl.getRecordDeclaration();
        PsiElement offset = record.getFieldSelection().getFirstChild();
        editor.getCaretModel().moveToOffset(offset.getTextRange().getEndOffset());
      } else if (decl.getEnumDeclaration() != null) {
        CourierEnumDeclaration enumeration = decl.getEnumDeclaration();
        PsiElement offset = enumeration.getEnumSymbolDeclarations().getFirstChild();
        editor.getCaretModel().moveToOffset(offset.getTextRange().getEndOffset());
      } else if (decl.getTyperefDeclaration() != null) {
        CourierTyperefDeclaration typeref = decl.getTyperefDeclaration();
        CourierTypeAssignment assignment = typeref.getTypeAssignment();
        if (assignment.getTypeDeclaration() != null) {
          CourierTypeDeclaration refDecl = assignment.getTypeDeclaration();
          if (refDecl.getAnonymousTypeDeclaration() != null) {
            CourierUnionDeclaration unionDecl = refDecl.getAnonymousTypeDeclaration().getUnionDeclaration();
            if (unionDecl != null) {
              PsiElement offset = unionDecl.getUnionTypeAssignments().getFirstChild();
              editor.getCaretModel().moveToOffset(offset.getTextRange().getEndOffset());
            }
          }
        } else if (assignment.getTypeReference() != null) {
          CourierTypeReference refType = assignment.getTypeReference();
          TextRange range = refType.getTextRange();
          editor.getCaretModel().moveToOffset(range.getStartOffset());
          editor.getSelectionModel().setSelection(range.getStartOffset(), range.getEndOffset());
        }
      }
    }
  }

  private static ArrayFactory<CourierTopLevel> ARRAY_FACTORY = new ArrayFactory<CourierTopLevel>() {
    @NotNull
    @Override
    public CourierTopLevel[] create(int count) {
      return new CourierTopLevel[count];
    }
  };
}
