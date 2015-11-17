package org.coursera.courier;

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.ContainerUtil;
import org.coursera.courier.psi.CourierTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static org.coursera.courier.psi.CourierTypes.ARRAY_TYPE_ASSIGNMENTS;
import static org.coursera.courier.psi.CourierTypes.BLOCK_COMMENT;
import static org.coursera.courier.psi.CourierTypes.CLOSE_BRACE;
import static org.coursera.courier.psi.CourierTypes.CLOSE_BRACKET;
import static org.coursera.courier.psi.CourierTypes.CLOSE_PAREN;
import static org.coursera.courier.psi.CourierTypes.COLON;
import static org.coursera.courier.psi.CourierTypes.COMMA;
import static org.coursera.courier.psi.CourierTypes.DOT;
import static org.coursera.courier.psi.CourierTypes.ENUM_SYMBOL_DECLARATION;
import static org.coursera.courier.psi.CourierTypes.ENUM_SYMBOL_DECLARATIONS;
import static org.coursera.courier.psi.CourierTypes.EQUALS;
import static org.coursera.courier.psi.CourierTypes.FIELD_SELECTION;
import static org.coursera.courier.psi.CourierTypes.FIELD_SELECTION_ELEMENT;
import static org.coursera.courier.psi.CourierTypes.IMPORT_DECLARATION;
import static org.coursera.courier.psi.CourierTypes.IMPORT_DECLARATIONS;
import static org.coursera.courier.psi.CourierTypes.IMPORT_KEYWORD;
import static org.coursera.courier.psi.CourierTypes.JSON_ARRAY;
import static org.coursera.courier.psi.CourierTypes.JSON_OBJECT;
import static org.coursera.courier.psi.CourierTypes.LINE_COMMENT;
import static org.coursera.courier.psi.CourierTypes.MAP_TYPE_ASSIGNMENTS;
import static org.coursera.courier.psi.CourierTypes.NAMESPACE_DECLARATION;
import static org.coursera.courier.psi.CourierTypes.NAMESPACE_KEYWORD;
import static org.coursera.courier.psi.CourierTypes.OPEN_BRACE;
import static org.coursera.courier.psi.CourierTypes.OPEN_BRACKET;
import static org.coursera.courier.psi.CourierTypes.OPEN_PAREN;
import static org.coursera.courier.psi.CourierTypes.PROP_JSON_VALUE;
import static org.coursera.courier.psi.CourierTypes.QUESTION_MARK;
import static org.coursera.courier.psi.CourierTypes.SCHEMADOC;
import static org.coursera.courier.psi.CourierTypes.SCHEMADOC_CONTENT;
import static org.coursera.courier.psi.CourierTypes.SCHEMADOC_END;
import static org.coursera.courier.psi.CourierTypes.SCHEMADOC_START;
import static org.coursera.courier.psi.CourierTypes.TOP_LEVEL;
import static org.coursera.courier.psi.CourierTypes.TYPE_DECLARATION;
import static org.coursera.courier.psi.CourierTypes.UNION_TYPE_ASSIGNMENTS;

public class CourierFormattingModelBuilder implements FormattingModelBuilder {
  @NotNull
  @Override
  public FormattingModel createModel(@NotNull PsiElement element, @NotNull CodeStyleSettings settings) {
    Block block = new CourierFormattingBlock(element.getNode(), null, Indent.getNoneIndent(), null, settings, createSpacingBuilder(settings));
    return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
  }

  @NotNull
  private static SpacingBuilder createSpacingBuilder(@NotNull CodeStyleSettings settings) {
    // TODO: all the below hard coded values can potentially be replaced with code style config
    // variables.
    return new SpacingBuilder(settings, CourierLanguage.INSTANCE)
      .before(COMMA).spaceIf(false)
      .after(COMMA).spaceIf(true)
      .before(COLON).spaceIf(false)
      .after(COLON).spaceIf(true)
      .before(EQUALS).spaceIf(true)
      .after(EQUALS).spaceIf(true)
      .before(QUESTION_MARK).spaceIf(false)
      .after(QUESTION_MARK).spaceIf(true)
      .around(DOT).none()

      .before(NAMESPACE_DECLARATION).blankLines(0)
      .after(NAMESPACE_DECLARATION).blankLines(1)
      .after(NAMESPACE_KEYWORD).spaceIf(true)

      .around(IMPORT_DECLARATIONS).blankLines(1)
      .around(IMPORT_DECLARATION).lineBreakInCode()
      .after(IMPORT_KEYWORD).spaceIf(true)

      .before(TYPE_DECLARATION).lineBreakInCode()
      .after(TYPE_DECLARATION).lineBreakInCode()
      .after(FIELD_SELECTION_ELEMENT).lineBreakInCode()
      .after(ENUM_SYMBOL_DECLARATION).lineBreakInCode()

      .before(SCHEMADOC).blankLines(1)
      .after(SCHEMADOC).lineBreakInCode()
      .after(LINE_COMMENT).lineBreakInCode()
      .after(BLOCK_COMMENT).lineBreakInCode()
      ;
  }

  @Nullable
  @Override
  public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
    return null;
  }

  public static class CourierFormattingBlock extends UserDataHolderBase implements ASTBlock {
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
        CLOSE_PAREN,
        SCHEMADOC_START,
        SCHEMADOC_END
    );

    @NotNull private final ASTNode myNode;
    @Nullable private final Alignment myAlignment;
    @Nullable private final Indent myIndent;
    @Nullable private final Wrap myWrap;
    @NotNull private final CodeStyleSettings mySettings;
    @NotNull private final SpacingBuilder mySpacingBuilder;
    @Nullable private List<Block> mySubBlocks;

    public CourierFormattingBlock(@NotNull ASTNode node,
                                  @Nullable Alignment alignment,
                                  @Nullable Indent indent,
                                  @Nullable Wrap wrap,
                                  @NotNull CodeStyleSettings settings,
                                  @NotNull SpacingBuilder spacingBuilder) {
      myNode = node;
      myAlignment = alignment;
      myIndent = indent;
      myWrap = wrap;
      mySettings = settings;
      mySpacingBuilder = spacingBuilder;
    }

    @NotNull
    @Override
    public ASTNode getNode() {
      return myNode;
    }

    @NotNull
    @Override
    public TextRange getTextRange() {
      return myNode.getTextRange();
    }

    @Nullable
    @Override
    public Wrap getWrap() {
      return myWrap;
    }

    @Nullable
    @Override
    public Indent getIndent() {
      return myIndent;
    }

    @Nullable
    @Override
    public Alignment getAlignment() {
      return myAlignment;
    }

    @NotNull
    @Override
    public List<Block> getSubBlocks() {
      if (mySubBlocks == null) {
        mySubBlocks = buildSubBlocks();
      }
      return ContainerUtil.newArrayList(mySubBlocks);
    }

    @NotNull
    private List<Block> buildSubBlocks() {
      List<Block> blocks = ContainerUtil.newArrayList();
      for (ASTNode child = myNode.getFirstChildNode(); child != null; child = child.getTreeNext()) {
        IElementType childType = child.getElementType();
        if (child.getTextRange().getLength() == 0) continue;
        if (childType == TokenType.WHITE_SPACE) continue;
        CourierFormattingBlock e = buildSubBlock(child, null);
        blocks.add(e);
      }
      return Collections.unmodifiableList(blocks);
    }

    @NotNull
    private CourierFormattingBlock buildSubBlock(@NotNull ASTNode child, @Nullable Alignment alignment) {
      Indent indent = calcIndent(child);
      return new CourierFormattingBlock(child, alignment, indent, null, mySettings, mySpacingBuilder);
    }

    @NotNull
    private Indent calcIndent(@NotNull ASTNode child) {
      IElementType parentType = myNode.getElementType();
      IElementType type = child.getElementType();
      if (type == TOP_LEVEL) return Indent.getAbsoluteNoneIndent();
      if (type == SCHEMADOC_CONTENT || type == SCHEMADOC_END) {
        return Indent.getIndent(Indent.Type.SPACES, 1, true, true);
      }
      if (BLOCKS_TOKEN_SET.contains(parentType)) return indentIfNotBrace(child);
      return Indent.getNoneIndent();
    }

    @NotNull
    private static Indent indentIfNotBrace(@NotNull ASTNode child) {
      return BRACES_TOKEN_SET.contains(child.getElementType()) ? Indent.getNoneIndent() : Indent.getNormalIndent();
    }

    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
      return mySpacingBuilder.getSpacing(this, child1, child2);
    }

    @NotNull
    @Override
    public ChildAttributes getChildAttributes(int newChildIndex) {
      Indent childIndent = Indent.getNoneIndent();
      IElementType parentType = myNode.getElementType(); // always the parent since isIncomplete is false
      if (BLOCKS_TOKEN_SET.contains(parentType)) {
        childIndent = Indent.getNormalIndent();
      } else if (parentType == SCHEMADOC) {
        childIndent = Indent.getIndent(Indent.Type.SPACES, 1, true, true);
      }
      return new ChildAttributes(childIndent, null);
    }

    @Override
    public boolean isIncomplete() {
      return false;
    }

    @Override
    public boolean isLeaf() {
      return myNode.getFirstChildNode() == null;
    }
  }
}
