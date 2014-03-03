// This is a generated file. Not intended for manual editing.
package org.intellij.erlang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.intellij.erlang.ErlangTypes.*;
import org.intellij.erlang.psi.*;
import com.intellij.psi.PsiReference;

public class ErlangMacrosImpl extends ErlangCompositeElementImpl implements ErlangMacros {

  public ErlangMacrosImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ErlangVisitor) ((ErlangVisitor)visitor).visitMacros(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ErlangMacroCallArgumentList getMacroCallArgumentList() {
    return findChildByClass(ErlangMacroCallArgumentList.class);
  }

  @Override
  @Nullable
  public ErlangMacrosName getMacrosName() {
    return findChildByClass(ErlangMacrosName.class);
  }

  @Override
  @NotNull
  public PsiElement getQmark() {
    return findNotNullChildByType(ERL_QMARK);
  }

  @Nullable
  public PsiReference getReference() {
    return ErlangPsiImplUtil.getReference(this);
  }

}
