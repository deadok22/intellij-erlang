-spec t1(FooBar :: t99()) -> t99();
(t2()) -> t2();
	(t4()) -> t4() when is_subtype(t4(), t24);
(t23()) -> t23() when is_subtype(t23(), atom()),
                      is_subtype(t23(), t14());
(t24()) -> t24() when is_subtype(t24(), atom()),
                      is_subtype(t24(), t14()),
	   is_subtype(t24(), t4()).

-spec over(I :: integer()) -> R1 :: foo:typen();
  (A :: atom()) -> R2 :: foo:atomen();
 (T :: tuple()) -> R3 :: bar:typen().

