/*
 * Copyright 2015 Coursera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.coursera.courier.formatter;

import com.intellij.lang.ASTNode;
import com.intellij.lang.CodeDocumentationAwareCommenterEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.coursera.courier.psi.CourierElementType;
import org.coursera.courier.psi.CourierTypes;
import org.coursera.courier.schemadoc.psi.PsiSchemadocElement;
import org.coursera.courier.schemadoc.psi.SchemadocTypes;
import org.jetbrains.annotations.Nullable;

public class CourierCommenter implements CodeDocumentationAwareCommenterEx {
  private static final Logger LOG = Logger.getInstance("#" + CourierCommenter.class.getName());

  public CourierCommenter() {
    LOG.info("CourierCommenter");
  }

  @Nullable
  @Override
  public String getLineCommentPrefix() {
    return "//";
  }

  @Nullable
  @Override
  public String getBlockCommentPrefix() {
    return "/*";
  }

  @Nullable
  @Override
  public String getBlockCommentSuffix() {
    return "*/";
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentPrefix() {
    return null;
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentSuffix() {
    return null;
  }

  @Nullable
  @Override
  public IElementType getLineCommentTokenType() {
    return CourierTypes.LINE_COMMENT;
  }

  @Nullable
  @Override
  public IElementType getBlockCommentTokenType() {
    return CourierTypes.BLOCK_COMMENT;
  }

  @Nullable
  @Override
  public IElementType getDocumentationCommentTokenType() {
    return CourierElementType.DOC_COMMENT;
  }

  @Nullable
  @Override
  public String getDocumentationCommentPrefix() {
    return "/**";
  }

  @Nullable
  @Override
  public String getDocumentationCommentLinePrefix() {
    return "*";
  }

  @Nullable
  @Override
  public String getDocumentationCommentSuffix() {
    return "*/";
  }

  @Override
  public boolean isDocumentationComment(PsiComment comment) {
    return (comment instanceof PsiSchemadocElement);
  }

  @Override
  public boolean isDocumentationCommentText(PsiElement element) {
    if (element == null) return false;
    final ASTNode node = element.getNode();
    return node != null && node.getElementType() == SchemadocTypes.DOC_COMMENT_CONTENT;
  }
}
