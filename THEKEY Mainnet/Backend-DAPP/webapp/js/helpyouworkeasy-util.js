  
  function getDownloadFileName(name){
	  if ( !name ) return "";
	  var i = name.indexOf("_");
	  if ( i != -1 ) {
		 name = name.substring(i+1);
	  }
	  i = name.indexOf("/");
	  if ( i != -1 ) {
		 name = name.substring(i+1);
	  }
	  return name;
    }
//// 通用工具类函数  /////////////////////////////////
	/**
	 * 获取URL参数值
	 */
	function getUrlParam(xurl,name){
	    var para="";
	    if(xurl.lastIndexOf("?")>0){
	        para=xurl.substring(xurl.lastIndexOf("?")+1,xurl.length);
			var arr=para.split("&");
			para="";
			for(var i=0;i<arr.length;i++){
			   if(arr[i].split("=")[0]==name) return arr[i].split("=")[1];
			}
			return "";
	   }else{
	        return "";
	   }
	}
	/**
	 * 全选或全不选
	 * @param isCheck
	 * @param sub_boxes
	 */
	function checkAll(isCheck, sub_boxes){
		for (var i = 0; i < sub_boxes.length; i++) {
            sub_boxes[i].checked = isCheck;
		}
	}
    /**
     * 获取所有选中的复选框的值
     * @param boxes
     * @returns {String}
     */
	function getAllCheckedValue(boxes) {
		var values = "";
		for (var i = 0; i < boxes.length; i++) {
			if (boxes[i].checked) {
				values += boxes[i].value + ",";
			}
		}
		if (values.length > 0) {
			values = values.substring(0, values.length - 1);
		}
		return values;
	}
	/** 
	  获取单选项的值
	*/
	function getRadioValue(ds){
	  if ( !ds ) return "";
	  for ( var i=0;i<ds.length;i++ ) {
			if ( ds[i].checked ) {
				return ds[i].value;
			}
	  }
	  return "";
	}
	/** 初始化单选项的值 */
	function initRadioValue(boxes,v){
	  for (var i = 0; i < boxes.length; i++) {
		if ( boxes[i].value == v ) {
		  boxes[i].checked = true;
		  break ;
		}
	  }
	}
	
	/**
	 * 返回最近5年年份下拉选项
	 */
	function get5YearSelectHtml(name,hasEmpty,initValue, onchangeFn){
		if ( !initValue ) initValue = -1;
		var ht="<select id='"+name+"'>";
		if ( onchangeFn ) ht="<select id='"+name+"' onchange='"+onchangeFn+"'>";
		if ( hasEmpty ) {
			ht+="<option value=''>&nbsp;&nbsp;</option>";
		}
		var d=new Date();
		var year = Number( d.getFullYear() );
		for(var i=year;i>year-5;i-- ){
		   ht+="<option value='"+ i +"' ";
		   if ( i == initValue ) ht+=" selected ";
		   ht+=">"+i+"</option>";
		}
		ht+="</select>";
		return ht;
	}
	
	/**
	 * 返回12个月份下拉选项
	 */
	function get12MonthSelectHtml(name,hasEmpty,initValue, onchangeFn){
		if ( !initValue ) initValue = -1;
		var ht="<select id='"+name+"'>";
		if ( onchangeFn ) ht="<select id='"+name+"' onchange='"+onchangeFn+"'>";
		if ( hasEmpty ) {
			ht+="<option value=''>&nbsp;&nbsp;</option>";
		}
		var temp = "";
		for(var i=1;i<=12;i++ ){
		   temp = i<10?("0"+i):(i+"");
		   ht+="<option value='"+ i +"' ";
		   if ( i == initValue ) ht+=" selected ";
		   ht+=">"+temp+"</option>";
		}
		ht+="</select>";
		return ht;
	}
	
	/**
	 * 返回24小时下拉选项
	 */
	function get24HourSelectHtml(name,hasEmpty,initValue, onchangeFn){
		if ( !initValue ) initValue = -1;
		var ht="<select id='"+name+"'>";
		if ( onchangeFn ) ht="<select id='"+name+"' onchange='"+onchangeFn+"'>";
		if ( hasEmpty ) {
			ht+="<option value=''>&nbsp;&nbsp;</option>";
		}
		var temp = "";
		for(var i=0;i<24;i++ ){
		   temp = i<10?("0"+i):(i+"");
		   ht+="<option value='"+ i +"' ";
		   if ( i == initValue ) ht+=" selected ";
		   ht+=">"+temp+"</option>";
		}
		ht+="</select>";
		return ht;
	}
	
	/**
	返回分钟下拉选项（间隔5分钟）
	 */
	 
	 function get5MinuteSelectHtml(name,hasEmpty,initValue, onchangeFn){
		if ( !initValue ) initValue = -1;
		var ht="<select id='"+name+"'>";
		if ( onchangeFn ) ht="<select id='"+name+"' onchange='"+onchangeFn+"'>";
		if ( hasEmpty ) {
			ht+="<option value=''>&nbsp;&nbsp;</option>";
		}
		var temp = "";
		for(var i=0;i<60;i=i+5 ){
		   temp = i<10?("0"+i):(i+"");
		   ht+="<option value='"+ i +"' ";
		   if ( i == initValue ) ht+=" selected ";
		   ht+=">"+temp+"</option>";
		}
		ht+="</select>";
		return ht;
	}
	
	/**
     * 初始化选中复选框
     */
	function initCheckedValue(boxes,ids) {
		ids = ids+",";
		for (var i = 0; i < boxes.length; i++) {
			 boxes[i].checked = ( ids.indexOf(boxes[i].value + ",") != -1 );
		}
	}
	/**
	 * 转换时间字符串格式 (YYYY-MM-DD hh:mm:ss)
	 */
	function getTimeStr(t,isDateStr){
	    if ( !isDateStr ) isDateStr = false;
		if ( !t ) return "";
		if ( t == 0 ) return "";
		if ( t == -1 ) return "";
		try{
			var d=new Date();
		    d.setTime( t );
			var str = d.getFullYear()+"-"+getTwoNumberStr(d.getMonth()+1)+"-"+getTwoNumberStr(d.getDate());
			if ( !isDateStr ) str +=" "+getTwoNumberStr(d.getHours())+":"+getTwoNumberStr(d.getMinutes())
			       +":"+getTwoNumberStr(d.getSeconds());
		    return str;
		}catch(e){
			return "";
		}
	}
	/** 获取客户端系统当前时间(YYYY-MM-DD hh:mm:ss) */
	function getCurrentClientTime(){
	    var d=new Date();
		var str = d.getFullYear()+"-"+getTwoNumberStr(d.getMonth()+1)+"-"+getTwoNumberStr(d.getDate());
			str +=" "+getTwoNumberStr(d.getHours())+":"+getTwoNumberStr(d.getMinutes())+":"+getTwoNumberStr(d.getSeconds());
	    return str;
	}
	/** 对于个位数的数字，在前面补零  */
	function getTwoNumberStr(n){
		return ( n < 10 ) ? ("0"+n):n; 
	}
	/** 截取字符串长度 */
	function ellipsis(len,str){
	    if ( str == null ) return "";
        if ( str.length > len ) return str.substring(0,len-3)+"...";
        return str;	
	}
	/** 将日期字符串(YYYY-MM-DD hh:mm:ss)转换为时间戳 */
	function toTimestamp(str){
	    try{
	      var d=new Date();
		  d.setFullYear( Number(str.substring(0,4)) );
		  d.setMonth( Number(str.substring(5,7)) - 1 ); // 0 - 11
		  d.setDate( Number(str.substring(8,10)) );
		  d.setHours( Number(str.substring(11,13)) );
		  d.setMinutes( Number(str.substring(14,16)) );
		  d.setSeconds(0);
		  return d.getTime();
		}catch(e){
		  return 0;
		}
	}
	/** 将国际时转换为北京时(YYYY-MM-DD hh) */
	function toBeijingTime(y,m,day,h){
	    try{
	      var d=new Date();
		  d.setFullYear( Number(y) );
		  d.setMonth( Number(m) - 1 ); // 0 - 11
		  d.setDate( Number(day) );
		  d.setHours( Number(h) );
		  d.setMinutes(0);
		  d.setSeconds(0);
		  d.setTime( d.getTime() + 8*60*60000 );
		  var str = d.getFullYear()+"-"+getTwoNumberStr(d.getMonth()+1)+"-"+getTwoNumberStr(d.getDate()) +" "+getTwoNumberStr(d.getHours());
		  return str;
		}catch(e){
		  return "";
		}
	}
   