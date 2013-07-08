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

package org.intellij.erlang.formatting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import junit.framework.Assert;
import org.intellij.erlang.formatter.settings.ErlangEmacsCodeStyle;
import org.intellij.erlang.utils.ErlangLightPlatformCodeInsightFixtureTestCase;

/**
 * @author savenko
 */
public class ErlangEmacsLikeFormattingTest extends ErlangLightPlatformCodeInsightFixtureTestCase {

  private CodeStyleSettings myTempSettings;

  @Override
  protected void setUp() throws Exception {
    System.setProperty("idea.platform.prefix", "Idea");
    super.setUp();

    CodeStyleSettings currentSettings = CodeStyleSettingsManager.getSettings(getProject());
    Assert.assertNotNull(currentSettings);

    myTempSettings = currentSettings.clone();
    new ErlangEmacsCodeStyle().apply(myTempSettings);

    CodeStyleSettingsManager.getInstance(getProject()).setTemporarySettings(myTempSettings);
  }

  @Override
  protected void tearDown() throws Exception {
    CodeStyleSettingsManager.getInstance(getProject()).dropTemporarySettings();
    super.tearDown();
  }

  @Override
  protected String getTestDataPath() {
    return "testData/formatter/emacs/";
  }

  private void doTest() throws Throwable {
    myFixture.configureByFile(getTestName(true) + ".erl");
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        CodeStyleManager.getInstance(getProject()).reformat(myFixture.getFile());
      }
    });
    myFixture.checkResultByFile(getTestName(true) + "-after.erl");
  }

  public void testCaseExpression()              throws Throwable { doTest(); }
  public void testComprehensions()              throws Throwable { doTest(); }
  public void testFunctionArgumentDefinitions() throws Throwable { doTest(); }
  public void testFunctionGuards()              throws Throwable { doTest(); }
  public void testFunExpression()               throws Throwable { doTest(); }
  public void testIfExpression()                throws Throwable { doTest(); }
  public void testListExpression()              throws Throwable { doTest(); }
  public void testRecordDefinition()            throws Throwable { doTest(); }
  public void testRecordExpression()            throws Throwable { doTest(); }
  public void testSpecDeclaration()             throws Throwable { doTest(); }
  public void testTryExpression()               throws Throwable { doTest(); }
  public void testTypeDeclaration()             throws Throwable { doTest(); }
}
