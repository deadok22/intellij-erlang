indent_nested() ->
    [
     {foo, 2, "string"},
     {bar, 3, "another string"}
    ].

%% This causes an error in earlier erlang-mode versions.
foo() ->
    [#foo{
        foo = foo}].
