/*
 * Copyright 2012-2015 Sergey Ignatov
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

package org.intellij.erlang.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.BaseScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.text.CaseInsensitiveStringHashingStrategy;
import gnu.trove.THashSet;
import org.intellij.erlang.icons.ErlangIcons;
import org.intellij.erlang.psi.*;
import org.intellij.erlang.psi.impl.ErlangVarProcessor;
import org.intellij.erlang.psi.impl.ResolveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

import static com.intellij.patterns.PlatformPatterns.instanceOf;
import static com.intellij.patterns.PlatformPatterns.psiElement;
import static org.intellij.erlang.psi.impl.ErlangPsiImplUtil.*;

public class ErlangVariableCompletionContributor extends CompletionContributor implements DumbAware {
  public ErlangVariableCompletionContributor() {
    extend(CompletionType.BASIC, psiElement().inFile(instanceOf(ErlangFile.class)), new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        PsiElement position = parameters.getPosition();
        Collection<String> vars = new THashSet<String>(CaseInsensitiveStringHashingStrategy.INSTANCE);
        PsiFile file = position.getContainingFile();
        if (!(position.getParent() instanceof ErlangRecordExpression)) {
          //noinspection unchecked
          PsiElement scopeOwner = PsiTreeUtil.getParentOfType(position,
            ErlangFunctionClause.class, ErlangMacrosDefinition.class, ErlangTypeDefinition.class, ErlangSpecification.class);
          ResolveUtil.treeWalkUp(position, new MyBaseScopeProcessor(vars, position, scopeOwner, false));

//          ErlangModule module = getErlangModule();
//          if (module != null) {
//            module.processDeclarations(new MyBaseScopeProcessor(scopeOwner, true, vars), ResolveState.initial(), module, module);
//          }

          for (String var : vars) {
            result.addElement(LookupElementBuilder.create(var).withIcon(ErlangIcons.VARIABLE));
          }
        }

        Map<String, ErlangQVar> erlangVarContext = file.getOriginalFile().getUserData(ErlangVarProcessor.ERLANG_VARIABLE_CONTEXT);
        if (erlangVarContext != null && PsiTreeUtil.getParentOfType(position, ErlangColonQualifiedExpression.class) == null) {
          for (String var : erlangVarContext.keySet()) {
            result.addElement(LookupElementBuilder.create(var).withIcon(ErlangIcons.VARIABLE));
          }
        }
      }
    });
  }

  public static void populateVariables(@NotNull Collection<ErlangQVar> result, @NotNull ErlangQVar variable, @Nullable PsiElement scopeOwner) {
    ResolveUtil.treeWalkUp(variable, new MyBaseScopeProcessor(result, variable, scopeOwner));
  }

  private static class MyBaseScopeProcessor extends BaseScopeProcessor {
    private final PsiElement myElement;
    private final PsiElement myScopeOwner;
    private final boolean myForce;
    private final Collection<String> myResult;
    @Nullable private Collection<ErlangQVar> myVars;

    public MyBaseScopeProcessor(@NotNull Collection<String> result, @NotNull PsiElement element, @Nullable PsiElement scopeOwner, boolean force) {
      myElement = element;
      myScopeOwner = scopeOwner;
      myForce = force;
      myResult = result;
    }

    @SuppressWarnings("NullableProblems")
    private MyBaseScopeProcessor(@NotNull Collection<ErlangQVar> result, @NotNull PsiElement element, @Nullable PsiElement scopeOwner) {
      this(ContainerUtil.<String>newArrayList(), element, scopeOwner, true);
      myVars = result;
    }

    @Override
    public boolean execute(@NotNull PsiElement psiElement, @NotNull ResolveState resolveState) {
      if (!psiElement.equals(myElement) && psiElement instanceof ErlangQVar && !psiElement.getText().equals("_") && !inColonQualified(myElement)) {
        boolean ancestor = PsiTreeUtil.isAncestor(myScopeOwner, psiElement, false);
        if ((ancestor || myForce) && (inArgumentDefinition(psiElement) || inLeftPartOfAssignment(psiElement) || inFunctionTypeArgument(psiElement))) {
          myResult.add(((ErlangQVar) psiElement).getName());
          if (myVars != null) myVars.add((ErlangQVar) psiElement);
        }
      }
      return true;
    }
  }
}
