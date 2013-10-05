-export([x/0, y/0]).

-record(not_quoted, {}).
x() ->
  #not_quoted{},
  #'not_quoted'{}.


-record('quoted', {}).
y() ->
  #quoted{},
  #'quoted'{}.