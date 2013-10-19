package org.intellij.erlang.parser.preprocessor;

import java.util.List;

/**
 * @author savenko
 */
public class ErlangMacroDefinition {
  private final List<String> myParameters;
  private final String myText;

  public ErlangMacroDefinition(List<String> parameters, String text) {
    myParameters = parameters;
    myText = text;
  }

  public String substitute(List<String> arguments) {
    //TODO implmement me
    return myText;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ErlangMacroDefinition that = (ErlangMacroDefinition) o;

    if (!myParameters.equals(that.myParameters)) return false;
    if (!myText.equals(that.myText)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = myParameters.hashCode();
    result = 31 * result + myText.hashCode();
    return result;
  }
}
