package org.intellij.erlang.parser.preprocessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author savenko
 */
public class ErlangMacroDefinitionBuilder {
  private String myName = null;
  private List<String> myParameters = new ArrayList<String>();
  private StringBuilder myText = new StringBuilder();

  public void setName(String name) {
    myName = name;
  }

  public void addParameter(String parameterName) {
    myParameters.add(parameterName);
  }

  public void addBody(String bodyPart) {
    myText.append(bodyPart);
  }

  public String getName() {
    return myName;
  }

  public void trimLastBrace() {
    int i = myText.lastIndexOf(")");
    if (i >= 0) {
      myText.setLength(i);
    }
  }

  public ErlangMacroDefinition build() {
    ErlangMacroDefinition result = new ErlangMacroDefinition(myParameters, myText.toString());
    myName = null;
    myParameters = new ArrayList<String>();
    myText.setLength(0);
    return result;
  }
}
