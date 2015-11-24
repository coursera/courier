package org.coursera.courier.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.alignment.AlignmentStrategy;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.schemadoc.psi.SchemadocTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SchemadocBlock extends AbstractCourierBlock {
  public SchemadocBlock(ASTNode node,
                        Wrap wrap,
                        Alignment alignment,
                        Indent indent,
                        CodeStyleSettings settings,
                        SpacingBuilder spacingBuilder) {
    super(node, wrap, alignment, indent, settings, spacingBuilder);
  }

  @Override
  protected List<Block> buildChildren() {
    final ArrayList<Block> result = new ArrayList<Block>();

    ASTNode child = getNode().getFirstChildNode();
    while (child != null) {
      IElementType childType = child.getElementType();
      if (childType != TokenType.WHITE_SPACE && !FormatterUtil.containsWhiteSpacesOnly(child) && !child.getText().trim().isEmpty()) {
        Indent childIndent;
        if (child.getElementType() == SchemadocTypes.DOC_COMMENT_START) {
          childIndent = Indent.getNoneIndent();
        } else {
          childIndent = Indent.getSpaceIndent(1);
        }
        result.add(createBlock(child, childIndent, null, AlignmentStrategy.getNullStrategy(), settings, spacingBuilder));
      }

      child = child.getTreeNext();
    }
    return result;
  }

  @Nullable
  protected Indent getChildIndent() {
    return Indent.getSpaceIndent(1);
  }
}
