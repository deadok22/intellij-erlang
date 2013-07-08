indent_try_catch() ->
    try
        io:format(stdout, "Parsing file ~s, ",
                  [St0#leex.xfile]),
        {ok, Line3, REAs, Actions, St3} =
            parse_rules(Xfile, Line2, Macs, St2)
    catch
        exit:{badarg, R} ->
            foo(R),
            io:format(stdout,
                      "ERROR reason ~p~n",
                      R);
        error:R
          when R =:= 42 ->
            foo(R);
        error:R
          when
              R =:= 42 ->
            foo(R);
        error:R when
              R =:= foo ->
            foo(R);
        error:R ->
            foo(R),
            io:format(stdout,
                      "ERROR reason ~p~n",
                      R)
    after
        foo('after'),
        file:close(Xfile)
    end;
indent_try_catch() ->
    try
        foo(bar)
    of
        X when true andalso
               kalle ->
            io:format(stdout, "Parsing file ~s, ",
                      [St0#leex.xfile]),
            {ok, Line3, REAs, Actions, St3} =
                parse_rules(Xfile, Line2, Macs, St2);
        X
          when false andalso
               bengt ->
            gurka();
        X when
              false andalso
              not bengt ->
            gurka();
        X ->
            io:format(stdout, "Parsing file ~s, ",
                      [St0#leex.xfile]),
            {ok, Line3, REAs, Actions, St3} =
                parse_rules(Xfile, Line2, Macs, St2)
    catch
        exit:{badarg, R} ->
            foo(R),
            io:format(stdout,
                      "ERROR reason ~p~n",
                      R);
        error:R ->
            foo(R),
            io:format(stdout,
                      "ERROR reason ~p~n",
                      R)
    after
        foo('after'),
        file:close(Xfile),
        bar(with_long_arg,
            with_second_arg)
    end;
indent_try_catch() ->
    try foo()
    after
        foo(),
        bar(with_long_arg,
            with_second_arg)
    end.
