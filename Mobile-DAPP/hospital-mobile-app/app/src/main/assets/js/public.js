var url="http://222.128.14.106:2989/HollyBlockChain";
function getUrlBase(){
	return url;
}
function toTimestamp(str){
	if ( str == '' ) return 0;
	try{
	  var d=new Date(2013,1,1);
	  d.setFullYear( Number(str.substring(0,4)) );
	  d.setMonth( Number(str.substring(5,7)) - 1 ); // 0 - 11
	  d.setDate( Number(str.substring(8,10)) );
	  d.setSeconds(0);
	  if ( str.length > 15 ) {
	   d.setHours( Number(str.substring(11,13)) );
	   d.setMinutes( Number(str.substring(14,16)) );
	   if ( str.length > 17 ) d.setSeconds( Number(str.substring(17,19)) );
	  } else {
	   d.setHours(0);
	   d.setMinutes(0);
	   d.setSeconds(0);
	  }
	  return d.getTime();
	}catch(e){
	  return 0;
	}
}