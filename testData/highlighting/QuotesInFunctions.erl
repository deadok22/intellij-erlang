-export(['call_functions'/0]).

'quoted'() -> ok.
unquoted() -> ok.

call_functions() ->
  quoted(),
  'quoted'(),
  unquoted(),
  'unquoted'().