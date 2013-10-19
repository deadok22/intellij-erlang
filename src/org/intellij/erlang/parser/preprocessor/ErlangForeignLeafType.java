package org.intellij.erlang.parser.preprocessor;

import com.intellij.lang.ForeignLeafType;
import com.intellij.psi.tree.IElementType;

/**
 * @author savenko
 */
public class ErlangForeignLeafType extends ForeignLeafType {
  public ErlangForeignLeafType(IElementType delegate, CharSequence value) {
    super(delegate, value);
  }
}
