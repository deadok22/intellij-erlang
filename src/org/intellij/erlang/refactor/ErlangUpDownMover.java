package org.intellij.erlang.refactor;

import com.intellij.codeInsight.editorActions.moveUpDown.LineMover;
import com.intellij.codeInsight.editorActions.moveUpDown.LineRange;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.intellij.erlang.psi.ErlangFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author savenko
 */
public class ErlangUpDownMover extends LineMover {
  @Override
  public boolean checkAvailable(@NotNull Editor editor, @NotNull PsiFile file, @NotNull MoveInfo info, boolean down) {
    if (!(file instanceof ErlangFile)) return info.prohibitMove();
    LineRange moveSourceRange = getLineRangeFromSelection(editor);
    LineRange moveDestinationRange = getMoveDestinationRange(moveSourceRange, editor, file, down);
    if (moveDestinationRange != null) {
      info.toMove = moveSourceRange;
      info.toMove2 = moveDestinationRange;
      return true;
    }

    //TODO process cases like below correctly:
    //foo(1, 2) -> onetwo;
    //foo(A, B) ->
    //ok.
    //TODO also process different combinations of dots, commas and semicolons.
    //TODO see if formatting is done right.

    return info.prohibitMove();
  }

  @Nullable
  private static LineRange getMoveDestinationRange(@Nullable LineRange moveSourceRange, @NotNull Editor editor, @NotNull PsiFile file, boolean down) {
    if (moveSourceRange == null) return null;
    Pair<PsiElement, PsiElement> moveSrcElementRange = getMoveElementsRange(editor, file, moveSourceRange);
    if (moveSrcElementRange == null || !isRangeOfSiblings(moveSrcElementRange)) return null;

    PsiElement moveDstStartElement = down ? moveSrcElementRange.second.getNextSibling() : moveSrcElementRange.first.getPrevSibling();
    moveDstStartElement = moveDstStartElement != null ? firstNonWhiteElement(moveDstStartElement, down) : null;
    LineRange moveDestinationRange = moveDstStartElement != null ? new LineRange(moveDstStartElement) : null;
    if (moveDestinationRange == null) return null;

    Pair<PsiElement, PsiElement> moveDstElementRange = getMoveElementsRange(editor, file, moveDestinationRange);
    if (!isRangeOfSiblings(moveDstElementRange)) return null;

    return moveDestinationRange;
  }

  @Nullable
  private static Pair<PsiElement, PsiElement> getMoveElementsRange(@NotNull Editor editor, @NotNull PsiFile file, @NotNull LineRange range) {
    final int startOffset = editor.getDocument().getLineStartOffset(range.startLine);
    final int endOffset = editor.getDocument().getLineEndOffset(range.endLine - 1);
    TextRange maxRange = new TextRange(startOffset, endOffset);

    Pair<PsiElement, PsiElement> elementRange = getElementRange(editor, file, range);
    if (elementRange != null) {
      PsiElement firstElement = getLeftRangeBorder(elementRange.first, maxRange);
      PsiElement lastElement = getRightRangeBorder(elementRange.second, maxRange);
      return new Pair<PsiElement, PsiElement>(firstElement, lastElement);
    }
    return null;
  }

  private static PsiElement getLeftRangeBorder(PsiElement element, TextRange maxRange) {
    return getRangeBorder(element, maxRange, true);
  }

  private static PsiElement getRightRangeBorder(PsiElement element, TextRange maxRange) {
    return getRangeBorder(element, maxRange, false);
  }

  private static PsiElement getRangeBorder(PsiElement element, TextRange maxRange, boolean leftBorder) {
    while (!maxRange.contains(element.getTextRange())) {
      element = leftBorder ? element.getFirstChild() : element.getLastChild();
    }
    return getTopmostAncestorFromRange(element, maxRange);
  }

  private static PsiElement getTopmostAncestorFromRange(PsiElement element, TextRange range) {
    PsiElement parent = element.getParent();
    while (parent != null && range.contains(parent.getTextRange())) {
      element = parent;
      parent = element.getParent();
    }
    return element;
  }

  private static boolean isRangeOfSiblings(@Nullable Pair<PsiElement, PsiElement> rangeBounds) {
    if (rangeBounds == null) return false;
    PsiElement currentElement = rangeBounds.first;
    while (currentElement != null && currentElement != rangeBounds.second) {
      currentElement = currentElement.getNextSibling();
    }
    return currentElement != null;
  }
}
