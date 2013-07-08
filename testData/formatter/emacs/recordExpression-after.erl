%% Record indentation
some_function_with_a_very_long_name() ->
    #'a-long-record-name-like-it-sometimes-is-with-asn.1-records'{
                                     field1 = a,
                                     field2 = b},
    case dummy_function_with_a_very_very_long_name(x) of
        #'a-long-record-name-like-it-sometimes-is-with-asn.1-records'{
                                                        field1 = a,
                                                        field2 = b} ->
            ok;
        Var = #'a-long-record-name-like-it-sometimes-is-with-asn.1-records'{
          field1 = a,
          field2 = b} ->
            Var#'a-long-record-name-like-it-sometimes-is-with-asn.1-records'{
              field1 = a,
              field2 = b};
        #xyz{
              a = 1,
              b = 2} ->
            ok
    end.

another_function_with_a_very_very_long_name() ->
    #rec{
          field1 = 1,
          field2 = 1}.

some_function_name_xyz(xyzzy, #some_record{
                         field1 = Field1,
                         field2 = Field2}) ->
    SomeVariable = f(#'Some-long-record-name'{
                        field_a = 1,
                        'inter-xyz-parameters' =
                            #'Some-other-very-long-record-name'{
                          field2 = Field1,
                          field2 = Field2}}),
    {ok, SomeVariable}.
