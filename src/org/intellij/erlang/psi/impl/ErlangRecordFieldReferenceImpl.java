/*
 * Copyright 2012-2013 Sergey Ignatov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.intellij.erlang.psi.impl;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayUtil;
import org.intellij.erlang.psi.ErlangQAtom;
import org.intellij.erlang.psi.ErlangTypedExpr;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author savenko
 */
public class ErlangRecordFieldReferenceImpl<T extends ErlangQAtom> extends ErlangAtomBasedReferenceImpl<T> {
  public ErlangRecordFieldReferenceImpl(T element, TextRange range, String name) {
    super(element, range, name);
  }

  @Override
  public PsiElement resolve() {
    Pair<List<ErlangTypedExpr>, List<ErlangQAtom>> recordFields = ErlangPsiImplUtil.getRecordFields(myElement);
    for (ErlangTypedExpr field : recordFields.first) {
      if (ErlangPsiImplUtil.equalAsAtoms(myReferenceName, field.getName())) return field;
    }
    for (ErlangQAtom qAtom : recordFields.second) {
      PsiElement aa = qAtom.getAtom();
      if (aa != null) {
        if (ErlangPsiImplUtil.equalAsAtoms(myReferenceName, aa.getText())) return qAtom;
      }
    }
    return null;
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    return ArrayUtil.EMPTY_OBJECT_ARRAY;
  }
}
