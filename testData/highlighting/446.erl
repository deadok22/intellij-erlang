-export([tree/0]).

-record(node, {children = []}).

-define(APPEND_NEW_CHILD_REC(NODE, PARENTREC, CHILDREC, FIELD),
  NODE#PARENTREC{FIELD = [#CHILDREC{}|NODE#PARENTREC.FIELD]}
).

tree() ->
  Root = #node{},
  ?APPEND_NEW_CHILD_REC(Root, node, node, children).