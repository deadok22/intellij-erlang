foo() ->
 case {Z, foo, bar} of
  {Z, _, _} ->
  X = 43 div 4,
  foo(X);
  {Z, _, _}
  when Z < 10 ->
  X = 43 div 4,
  foo(X);
  {Z, _, _}
  when
  Z < 10
  andalso
  true ->
  X = 43 div 4,
  foo(X)
 end.
