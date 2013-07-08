indent_icr(Z) -> 				% icr = if case receive
 %% If
 if Z >= 0 ->
	 X = 43 div 4,
	 foo(X);
    Z =< 10 ->
	 X = 43 div 4,
	 foo(X);
    Z == 5 orelse
    Z == 7 ->
	 X = 43 div 4,
	 foo(X);
    true ->
	 if_works
  end.
