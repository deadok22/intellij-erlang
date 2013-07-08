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

package org.intellij.erlang.formatter.settings;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.PredefinedCodeStyle;
import org.intellij.erlang.ErlangFileType;
import org.intellij.erlang.ErlangLanguage;

/**
 * @author savenko
 */
public class ErlangEmacsCodeStyle extends PredefinedCodeStyle {
  private static final String CODE_STYLE_NAME = "Emacs-like";

  public ErlangEmacsCodeStyle() {
    super(CODE_STYLE_NAME, ErlangLanguage.INSTANCE);
  }

  @Override
  public void apply(CodeStyleSettings settings) {
    applyCommonSettings(settings.getCommonSettings(ErlangLanguage.INSTANCE));
    applyIndentOptions(settings.getIndentOptions(ErlangFileType.MODULE));
    applyErlangCodeStyleSettings(settings.getCustomSettings(ErlangCodeStyleSettings.class));
  }

  private static void applyCommonSettings(CommonCodeStyleSettings settings) {
    settings.LINE_COMMENT_AT_FIRST_COLUMN = true;
  }

  private static void applyIndentOptions(CommonCodeStyleSettings.IndentOptions indentOptions) {
    indentOptions.CONTINUATION_INDENT_SIZE = 1;
    indentOptions.INDENT_SIZE = 4;
    indentOptions.TAB_SIZE = 8;
  }

  private static void applyErlangCodeStyleSettings(ErlangCodeStyleSettings settings) {
    settings.INDENT_RELATIVE = true;
    settings.ALIGN_FUNCTION_CLAUSES = true;
    settings.ALIGN_MULTILINE_BLOCK = true;
    settings.ENABLE_EMACS_INDENTATION_TWEAKS = true;
    settings.ALIGN_FUN_EXPRESSION_CLAUSE_BODY = true;
  }
}
