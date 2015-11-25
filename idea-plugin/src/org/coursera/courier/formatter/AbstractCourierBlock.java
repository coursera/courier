package org.coursera.courier.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.alignment.AlignmentStrategy;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.schemadoc.psi.PsiSchemadocElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCourierBlock extends AbstractBlock {

  @NotNull protected final Indent indent;
  @NotNull protected final CodeStyleSettings settings;
  @NotNull protected final SpacingBuilder spacingBuilder;

  public AbstractCourierBlock(ASTNode node,
        Wrap wrap,
        Alignment alignment,
        Indent indent,
        CodeStyleSettings settings,
        SpacingBuilder spacingBuilder) {
    super(node, wrap, alignment);
    this.indent = indent;
    this.settings = settings;
    this.spacingBuilder = spacingBuilder;
  }

  @NotNull
  public static Block createBlock(@NotNull ASTNode child,
        @Nullable Indent indent,
        Wrap wrap,
        @NotNull AlignmentStrategy alignmentStrategy,
        CodeStyleSettings settings,
        SpacingBuilder spacingBuilder) {
    final IElementType elementType = child.getElementType();
    Alignment alignment = alignmentStrategy.getAlignment(elementType);

    if (child.getPsi() instanceof PsiSchemadocElement) {
      return new SchemadocBlock(child, wrap, alignment, indent, settings, spacingBuilder);
    }
    else {
      return new SimpleCourierBlock(child, wrap, alignment, indent, settings, spacingBuilder);
    }
  }

  @Override
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return spacingBuilder.getSpacing(this, child1, child2);
  }

  @Override
  public Indent getIndent() {
    return indent;
  }

  @Override
  public boolean isLeaf() {
    return myNode.getFirstChildNode() == null;
  }
}
