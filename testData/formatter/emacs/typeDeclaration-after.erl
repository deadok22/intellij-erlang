-type paren() ::
        (ann2()).

-type t6() :: 1 | 2 | 3 |
              'foo' | 'bar'.

-type t14() :: [erl_scan:foo() |
                %% Should be highlighted
                term() |
                bool() |
                byte() |
                char() |
                non_neg_integer() | nonempty_list() |
                pos_integer() |
                neg_integer() |
                number() |
                list() |
                nonempty_improper_list() | nonempty_maybe_improper_list() |
                maybe_improper_list() | string() | iolist() | byte() |
                module() |
                mfa()   |
                node()  |
                timeout() |
                no_return() |
                %% Should not be highlighted
                nonempty_() | nonlist() |
                erl_scan:bar(34, 92) | t13() | m:f(integer() | <<_:_ * 16>>)].

-type t15() :: {binary(), <<>>, <<_:34>>, <<_:_ * 42>>,
                <<_:3, _:_ * 14>>, <<>>} | [<<>>|<<_:34>>|<<_:16>>|
                                        <<_:3, _:_ * 1472>>|<<_:19, _:_ * 14>>| <<_:34>>|
                                        <<_:34>>|<<_:34>>|<<_:34>>].

-type t19() :: fun((t18()) -> t16()) |
               fun((nonempty_maybe_improper_list('integer', any())|
                    1|2|3|a|b|<<_:3, _:_ * 14>>|integer()) ->
                          nonempty_maybe_improper_list('integer', any())|
                          1|2|3|a|b|<<_:3, _:_ * 14>>|integer()).

-type t25() :: #rec3{f123 :: [t24() |
                              1|2|3|4|a|b|c|d|
                              nonempty_maybe_improper_list(integer, any())]}.

-type t99() ::
        {t2(), t4(), t5(), t6(), t7(), t8(), t10(), t14(),
         t15(), t20(), t21(), t22(), t25()}.
