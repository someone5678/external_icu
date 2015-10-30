/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.currysrc.transformers;

import com.google.common.collect.Lists;
import com.google.currysrc.api.transform.DocumentTransformer;

import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import java.util.List;

/**
 * A base-class for general comment scanners / transformers. All comments of all types in a
 * {@link CompilationUnit} are considered. Subclasses determine whether to make a complete comment
 * replacement.
 */
public abstract class BaseModifyCommentScanner implements DocumentTransformer {

  @Override
  public final void transform(CompilationUnit cu, Document document) {
    List<Comment> comments = cu.getCommentList();
    try {
      for (Comment comment : Lists.reverse(comments)) {
        String commentText = document.get(comment.getStartPosition(), comment.getLength());
        String newCommentText = generateReplacementText(comment, commentText);
        if (newCommentText != null) {
          document.replace(comment.getStartPosition(), comment.getLength(), newCommentText);
        }
      }
    } catch (BadLocationException e) {
      throw new AssertionError(e);
    }
  }

  /** Generates new text for the comment, or returns {@code null} if there is nothing to do. */
  protected abstract String generateReplacementText(Comment commentNode, String commentText);
}