-export([var_as_record_ref/1, var_as_record_field/1]).

-record(rec, {a}).

var_as_record_ref(A) ->
  B = rec,
  A#B.a.

var_as_record_field(A) ->
  B = a,
  A#rec.B.

stringify_macro_arg(A) ->
  ??A.