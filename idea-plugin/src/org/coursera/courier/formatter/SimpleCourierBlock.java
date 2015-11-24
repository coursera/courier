package org.coursera.courier.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.alignment.AlignmentStrategy;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.coursera.courier.psi.CourierTypes.ARRAY_TYPE_ASSIGNMENTS;
import static org.coursera.courier.psi.CourierTypes.CLOSE_BRACE;
import static org.coursera.courier.psi.CourierTypes.CLOSE_BRACKET;
import static org.coursera.courier.psi.CourierTypes.CLOSE_PAREN;
import static org.coursera.courier.psi.CourierTypes.ENUM_SYMBOL_DECLARATIONS;
import static org.coursera.courier.psi.CourierTypes.FIELD_SELECTION;
import static org.coursera.courier.psi.CourierTypes.JSON_ARRAY;
import static org.coursera.courier.psi.CourierTypes.JSON_OBJECT;
import static org.coursera.courier.psi.CourierTypes.MAP_TYPE_ASSIGNMENTS;
import static org.coursera.courier.psi.CourierTypes.OPEN_BRACE;
import static org.coursera.courier.psi.CourierTypes.OPEN_BRACKET;
import static org.coursera.courier.psi.CourierTypes.OPEN_PAREN;
import static org.coursera.courier.psi.CourierTypes.PROP_JSON_VALUE;
import static org.coursera.courier.psi.CourierTypes.TOP_LEVEL;
import static org.coursera.courier.psi.CourierTypes.UNION_TYPE_ASSIGNMENTS;

public class SimpleCourierBlock extends AbstractCourierBlock {
  public SimpleCourierBlock(ASTNode node,
      Wrap wrap,
      Alignment alignment,
      Indent indent,
      CodeStyleSettings settings,
      SpacingBuilder spacingBuilder) {
    super(node, wrap, alignment, indent, settings, spacingBuilder);
  }

  public static final TokenSet BLOCKS_TOKEN_SET = TokenSet.create(
    FIELD_SELECTION,
    ENUM_SYMBOL_DECLARATIONS,
    UNION_TYPE_ASSIGNMENTS,
    PROP_JSON_VALUE,
    ARRAY_TYPE_ASSIGNMENTS,
    MAP_TYPE_ASSIGNMENTS,
    JSON_OBJECT,
    JSON_ARRAY
  );

  public static final TokenSet BRACES_TOKEN_SET = TokenSet.create(
    OPEN_BRACE,
    CLOSE_BRACE,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    OPEN_PAREN,
    CLOSE_PAREN
  );

  @Override
  protected List<Block> buildChildren() {
    final ArrayList<Block> result = new ArrayList<Block>();

    ASTNode child = getNode().getFirstChildNode();
    while (child != null) {
      IElementType childType = child.getElementType();
      if (childType != TokenType.WHITE_SPACE && !FormatterUtil.containsWhiteSpacesOnly(child) && !child.getText().trim().isEmpty()) {
        result.add(
          createBlock(child, calcIndent(child), null, AlignmentStrategy.getNullStrategy(), settings, spacingBuilder));
      }

      child = child.getTreeNext();
    }
    return result;
  }

  @NotNull
  private Indent calcIndent(@NotNull ASTNode child) {
    IElementType parentType = myNode.getElementType();
    if (parentType == TOP_LEVEL) return Indent.getAbsoluteNoneIndent();
    if (BLOCKS_TOKEN_SET.contains(parentType)) {
      return indentIfNotBrace(child);
    } else {
      return Indent.getNoneIndent();
    }
  }

  @NotNull
  private static Indent indentIfNotBrace(@NotNull ASTNode child) {
    if (BRACES_TOKEN_SET.contains(child.getElementType())) {
      return Indent.getNoneIndent();
    } else {
      return Indent.getNormalIndent();
    }
  }

  @Nullable
  protected Indent getChildIndent() {
    IElementType parentType = myNode.getElementType(); // always the parent since isIncomplete is false

    if (BLOCKS_TOKEN_SET.contains(parentType)) {
      return Indent.getNormalIndent();
    } else {
      return Indent.getNoneIndent();
    }
  }
}
