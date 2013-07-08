-record(record1, {a, 
 b, 
 c
}).
-record(record2, {
  a,
  b
 }).
	  
-record(record3, {a = 8#42423 bor
    8#4234,
  b = 8#5432 
		  bor 2#1010101,
  c = 123 +
234,
		  d}).
