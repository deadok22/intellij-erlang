indent_comprehensions() ->
    Result1 = [X ||
                  #record{a = X} <- lists:seq(1, 10),
                  true = (X rem 2)
              ],
    Result2 = [X || <<X:32, _:32>> <= <<0:512>>,
                    true = (X rem 2)
              ],

    Binary1 = <<<<X:8>> ||
                  #record{a = X} <- lists:seq(1, 10),
                  true = (X rem 2)
              >>,

    Binary2 = <<<<X:8>> || <<X:32, _:32>> <= <<0:512>>,
                           true = (X rem 2)
              >>,
    ok.
