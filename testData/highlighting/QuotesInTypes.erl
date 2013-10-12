-export([foo/1, bar/1]).
-export_type([quoted/0, 'not_quoted'/0]).

-type not_quoted() :: atom().

-spec foo('not_quoted'()) -> not_quoted().
foo(X) -> X.

-type 'quoted'() :: atom().

-spec bar(quoted()) -> 'quoted'().
bar(X) -> X.