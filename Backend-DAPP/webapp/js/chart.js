   function getMStep(d){
      if ( d > 100000 ) return 100000;
      if ( d > 10000 ) return 10000;
      if ( d > 1000 ) return 1000;
      if ( d > 100 ) return 100;
      if ( d > 50 ) return 10;
	  if ( d > 10 ) return 5;
	  if ( d > 1 ) return 1;
	  if ( d > 0.1 ) return 0.1;
	  if ( d > 0.01 ) return 0.01;
	  if ( d > 0.005 ) return 0.005;
	  return 0.0005;
   }
   
   function getMMin(m){
      if ( m == 0 ) return 0;
	  var ret=m;
	  if ( m > 10 ) {
	    ret = 10;
	  }
	  if ( m > 100 ) {
	    ret = 100;
	  } 
	  if ( m > 1000 ) {
	    ret = 1000;
	  } 
	  if ( m > 10000 ) {
	    ret = 10000;
	  } 
	  if ( m > 100000 ) {
	    ret = 100000;
	  } 
	  if ( m > 1000000 ) {
	    ret = 1000000;
	  }
	  if ( m > 10000000 ) {
	    ret = 10000000;
	  }
	  return ret;
   }
   
   function getMMax(m){
      if ( m == 0 ) return 10;
	  var ret=m;
      if ( m < 100000000 ) {
	    ret=100000000;
	  }
      if ( m < 50000000 ) {
	    ret=50000000;
	  }
      if ( m < 20000000 ) {
	    ret=20000000;
	  }	  
	  if ( m < 10000000 ) {
	    ret=10000000;
	  }
      if ( m < 5000000 ) {
	    ret=5000000;
	  }
      if ( m < 2000000 ) {
	    ret=2000000;
	  }	  
	  if ( m < 1000000 ) {
	    ret=1000000;
	  }
      if ( m < 500000 ) {
	    ret=500000;
	  }	  
	  if ( m < 100000 ) {
	    ret=100000;
	  } 
	  if ( m < 50000 ) {
	    ret=50000;
	  }
	  if ( m < 10000 ) {
	    ret=10000;
	  } 
	  if ( m < 5000 ) {
	    ret=5000;
	  }
	  if ( m < 1000 ) {
	    ret=1000;
	  } 
	  if ( m < 100 ) {
	    ret=100;
	  } 
	  if ( m < 10 ) {
	    ret=10;
	  }
	  return ret;
   }