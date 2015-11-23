package org.coursera.courier;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;

public class CourierFileTemplateGroupFactory implements FileTemplateGroupDescriptorFactory {

  @Override
  public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
    FileTemplateGroupDescriptor group =
      new FileTemplateGroupDescriptor("Courier", CourierIcons.FILE);
    group.addTemplate(
      new FileTemplateDescriptor("Record.courier", CourierIcons.FILE));
    group.addTemplate(
      new FileTemplateDescriptor("Enum.courier", CourierIcons.FILE));
    group.addTemplate(
      new FileTemplateDescriptor("Typeref.courier", CourierIcons.FILE));
    group.addTemplate(
      new FileTemplateDescriptor("Union.courier", CourierIcons.FILE));
    return group;
  }
}
