-export([x/0, y/0]).

-record(not_quoted, {field1}).
x() ->
  #not_quoted{ 'field1' = 1 }.


-record(quoted, {'field1'}).
y() ->
  #quoted{ field1 = 1}.
