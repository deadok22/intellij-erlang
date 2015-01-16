/*
 * Copyright 2012-2014 Sergey Ignatov
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

package org.intellij.erlang.stubs.types;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.DefaultStubBuilder;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.IStubFileElementType;
import org.intellij.erlang.ErlangLanguage;
import org.intellij.erlang.context.ErlangCompileContext;
import org.intellij.erlang.context.ErlangCompileContextManager;
import org.intellij.erlang.lexer.ErlangMacroSubstitutingLexer;
import org.intellij.erlang.parser.ErlangParser;
import org.intellij.erlang.psi.ErlangFile;
import org.intellij.erlang.stubs.ErlangFileStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ErlangFileElementType extends IStubFileElementType<ErlangFileStub> {
  public static final int VERSION = 1;
  public static final IStubFileElementType INSTANCE = new ErlangFileElementType();

  public ErlangFileElementType() {
    super("ERL_FILE", ErlangLanguage.INSTANCE);
  }

  @Override
  public StubBuilder getBuilder() {
    return new DefaultStubBuilder() {
      @Override
      protected StubElement createStubForFile(@NotNull PsiFile file) {
        if (file instanceof ErlangFile) {
          return new ErlangFileStub((ErlangFile) file);
        }
        return super.createStubForFile(file);
      }
    };
  }

  @Override
  public int getStubVersion() {
    return VERSION;
  }

  @Override
  public void serialize(@NotNull ErlangFileStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeBoolean(stub.isExportAll());
    dataStream.writeName(stub.getParseTransforms());
  }

  @NotNull
  @Override
  public ErlangFileStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new ErlangFileStub(null, dataStream.readBoolean(), dataStream.readName());
  }

  @Override
  protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
    PsiFile psiFile = psi.getContainingFile();
    VirtualFile virtualFile = psiFile != null ? psiFile.getVirtualFile() : null;
    ErlangCompileContext compileContext = ErlangCompileContextManager.getInstance(psi.getProject()).getContext(psi.getProject(), virtualFile);
    //TODO should we use the preprocessing lexer here?
    //TODO pass current file to lexer
    ErlangMacroSubstitutingLexer lexer = new ErlangMacroSubstitutingLexer(compileContext);
    PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(psi.getProject(), chameleon, lexer, ErlangLanguage.INSTANCE, chameleon.getChars());
    return new ErlangParser().parse(chameleon.getElementType(), builder).getFirstChildNode();
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "erlang.FILE";
  }
}
