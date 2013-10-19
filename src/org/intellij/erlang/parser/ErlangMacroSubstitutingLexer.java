package org.intellij.erlang.parser;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.LookAheadLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.MultiMap;
import org.intellij.erlang.ErlangTypes;
import org.intellij.erlang.parser.preprocessor.ErlangForeignLeafType;
import org.intellij.erlang.parser.preprocessor.ErlangMacroDefinition;
import org.intellij.erlang.parser.preprocessor.ErlangMacroDefinitionBuilder;

import java.util.Collection;

/**
 * @author savenko
 */
public class ErlangMacroSubstitutingLexer extends LookAheadLexer {
  private final MultiMap<String, ErlangMacroDefinition> myMacroMap = new MultiMap<String, ErlangMacroDefinition>();

  public ErlangMacroSubstitutingLexer() {
    super(new ErlangLexer());
  }

  public ErlangMacroSubstitutingLexer(MultiMap<String, ErlangMacroDefinition> macroMap) {
    super(new ErlangLexer());
    myMacroMap.putAllValues(macroMap);
  }

  @Override
  protected void lookAhead(Lexer baseLexer) {
    if (myMacroDefState == null && baseLexer.getTokenType() == ErlangTypes.ERL_OP_MINUS) {
      myMacroDefState = MacroDefinitionParsingState.MINUS;
    }
    else if (myMacroDefState != null && !continueMacroDefParsing(baseLexer)) {
      myMacroDefState = null;
    } else if (myMacroDefState == null && baseLexer.getTokenType() == ErlangTypes.ERL_QMARK) {
      //TODO clean this mess up
      addToken(baseLexer.getTokenType()); //add question mark
      baseLexer.advance();
      IElementType varOrAtom = baseLexer.getTokenType();
      addToken(varOrAtom); //add macro var
      int macroCallEnd = baseLexer.getTokenEnd();
      String macroName = baseLexer.getTokenText();
      baseLexer.advance();

      if (varOrAtom == ErlangTypes.ERL_VAR || varOrAtom == ErlangTypes.ERL_ATOM) {
        //TODO get args and use them in substitution
        Collection<ErlangMacroDefinition> macroDefs = myMacroMap.get(macroName);
        if (!macroDefs.isEmpty()) {
          String substitution = macroDefs.iterator().next().substitute(ContainerUtil.<String>emptyList());

          Lexer substitutionLexer = new ErlangMacroSubstitutingLexer(myMacroMap);
          substitutionLexer.start(substitution, 0, substitution.length(), 0);

          while (substitutionLexer.getTokenType() != null) {
            IElementType tokenType = substitutionLexer.getTokenType();
            addToken(macroCallEnd, new ErlangForeignLeafType(tokenType, substitutionLexer.getTokenText()));
            substitutionLexer.advance();
          }

          return;
        }
      }

    }
    super.lookAhead(baseLexer);
  }

  private boolean continueMacroDefParsing(Lexer baseLexer) {
    IElementType tt = baseLexer.getTokenType();
    if (tt == TokenType.WHITE_SPACE) {
      return true;
    }
    //TODO fail if nothing inside case matched
    switch (myMacroDefState) {
      case MINUS: {
        if (tt == ErlangTypes.ERL_ATOM && "define".equals(baseLexer.getTokenText())) {
          myMacroDefState = MacroDefinitionParsingState.DEFINE;
          return true;
        }
        break;
      }
      case DEFINE: {
        if (tt == ErlangTypes.ERL_PAR_LEFT) {
          return true;
        }
        if (tt == ErlangTypes.ERL_ATOM || tt == ErlangTypes.ERL_VAR) {
          myMacroDefBuilder.setName(baseLexer.getTokenText());
          myMacroDefState = MacroDefinitionParsingState.NAME;
          return true;
        }
        break;
      }
      case NAME: {
        if (tt == ErlangTypes.ERL_COMMA) {
          myMacroDefState = MacroDefinitionParsingState.BODY;
          return true;
        }
        if (tt == ErlangTypes.ERL_PAR_LEFT) {
          myMacroDefState = MacroDefinitionParsingState.ARGS_LIST;
          return true;
        }
        break;
      }
      case ARGS_LIST: {
        if (tt == ErlangTypes.ERL_VAR) {
          myMacroDefBuilder.addParameter(baseLexer.getTokenText());
          return true;
        }
        if (tt == ErlangTypes.ERL_COMMA) {
          return true;
        }
        if (tt == ErlangTypes.ERL_PAR_RIGHT) {
          myMacroDefState = MacroDefinitionParsingState.COMMA;
          return true;
        }
        break;
      }
      case COMMA: {
        if (tt == ErlangTypes.ERL_COMMA) {
          myMacroDefState = MacroDefinitionParsingState.BODY;
          return true;
        }
        break;
      }
      case BODY: {
        if (tt == ErlangTypes.ERL_DOT) {
          myMacroDefState = null;
          String name = myMacroDefBuilder.getName();
          myMacroDefBuilder.trimLastBrace();
          ErlangMacroDefinition macroDef = myMacroDefBuilder.build();
          myMacroMap.putValue(name, macroDef);
          return true;
        }
        myMacroDefBuilder.addBody(baseLexer.getTokenText());
        return true;
      }
    }
    return false;
  }

  private MacroDefinitionParsingState myMacroDefState = null;
  private ErlangMacroDefinitionBuilder myMacroDefBuilder = new ErlangMacroDefinitionBuilder();

  private enum MacroDefinitionParsingState {
    MINUS,
    DEFINE,
    NAME,
    ARGS_LIST,
    COMMA,
    BODY
  }
}
