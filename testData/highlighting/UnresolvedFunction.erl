-export([bar/0, call_bar/0, call_unresolved/0, call_qualified_bar/0, call_qualified_unresolved/0]).

-callback(foo(atom()) -> ok).

bar() ->
  <warning>foo</warning>().

-define(CALL0(F), F()).

call_bar() ->
  ?CALL0(bar).

call_unresolved() ->
  <warning>?CALL0(unresolved)</warning>.

call_qualified_bar() ->
  ?CALL0('UnresolvedFunction':bar).

call_qualified_unresolved() ->
  <warning>?CALL0('UnresolvedFunction':unresolved)</warning>.