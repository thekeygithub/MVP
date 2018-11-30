
/*******************************************************************************
**  函数名称：IsEmail
**  功    能：是否为合格的E-mail地址
*   type 为0，不检查 ,type 为1，检查
**  引用函数：无
**  返 回 值：true false
*******************************************************************************/

 String.prototype.endWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
     return false;
  if(this.substring(this.length-s.length)==s)
     return true;
  else
     return false;
  return true;
 }

 String.prototype.startWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
   return false;
  if(this.substr(0,s.length)==s)
     return true;
  else
     return false;
  return true;
 }

function IsEmail(concole,type)
{
  var p1, p2;
	var s=concole.value;
	  if ((s==null) || (s.length<=0)){

		if(type=='1'){
//必须填写
  		return false;
 		}
	}else{
	if ((s==null) || (s.length<=5)){
	return false;
}
    p1 = s.indexOf("@");
    if (p1==-1)
    return false;
    if (s.indexOf("@", p1+1)!=-1)
    return false;
    p2 = s.indexOf(".", p1+1);
    if (p2==-1)
    return false;
    if ( (p1<=0) || ((p2-p1)<=1) || (p2>=s.length-1) )
    return false;

	}
return true;

}

/**
*
*
*/
function IsInArray(strInput,array){
    for(var j=0;j<array.length;j++){
			  if(array[j]==strInput){
				return true;
			  }
	}
	return false;

}
function checkPhone(strInput){

	var array = new Array("(",")","-");
      for (var i = 0; i < strInput.length; i++){
    	    if (IsDigit(strInput.charAt(i))==false){
      			if(!IsInArray(strInput.charAt(i),array)){
      				//alert(msg+"存在非法字符，只能包含数字和'（'、'）'和'-'");
      				return false;
      			}
    		  }
  	  }
    return true;
}

function chkNum(concole,length,msg){

        if(concole.value.length>length){
			alert(msg+"长度大于"+length);
			return false;
		}
		if(concole.value!=""){
             if(isNaN(concole.value)){
                alert(msg+"不是数字!");
                //concole.focus();
              return false;
            }
         }else{
          alert("请填写"+msg+"!");
          //concole.focus();
          return false;
         }
		 return true;

	}

	function checkString(val,isMust,length,msg){
        var str="";
		if( val && val != "" ) {
          if ( val.length > length ) {
		    str=msg+"长度不能大于"+length+"<br/>";
		  }
        } else {
          if ( isMust ) {
			str="请填写"+msg+"<br/>";
		  }
		}
		return str;
	}
	/*******************************************************************************
**  函数名称：IsFloat
**  功    能：是否为合格的浮点数
**  引用函数：IsDigit
**  返 回 值：true false
*******************************************************************************/
function IsFloat(s)
{
  var i, j;
  if ((s==null) || (s.length<=0))
    return false;
  if (s.charAt(0)=='-')
    j = 1;
  else
    j = 0;
  for(i=j; i<s.length; i++)
    if (IsDigit(s.charAt(i))==false)
    {
      if ((j<2) && (s.charAt(i)=="."))
        j = 2;
      else
        return false;
    }
  return true;
}

/*******************************************************************************
**  函数名称：IsDigit
**  功    能：是否为单一数字
**  引用函数：无
**  返 回 值：true false
*******************************************************************************/
function IsDigit(s)
{
  var AllDigits = '0123456789';
  if ((s==null) || (s.length!=1))
    return false;
  else if (AllDigits.indexOf(s)==-1)
    return false;
  else
    return true;
}
/*******************************************************************************
**  函数名称：IsNumber
**  功    能：是否为合格的正整数
**  引用函数：IsDigit
**  返 回 值：true false
*******************************************************************************/
function IsNumber(s)
{
  if ((s==null) || (s.length<=0))
    return false;
  else
    for(var i=0; i<s.length; i++)
      if (IsDigit(s.charAt(i))==false)
        return false;
  return true;
}
/*******************************************************************************
**  函数名称：IsDate
**  功    能：是否为合格的日期型（不允许输入参数为空字符串）
**  说    明：输入参数为空字符串时返回false
**  引用函数：IsNumber
**  返 回 值：true false
*******************************************************************************/
function IsDate(s)
{
  var Separator = '-';
  var SepCount = 0;
  var DaysInMonth;
  var y, m, d;
  var l;
  var p1, p2;
  if (s==null)
    return false;
  l = s.length;
  if ((l<8) || (l>10))
    return false;
  p1 = s.indexOf(Separator);
  if (p1==-1)
    return false;
  p2 = s.indexOf(Separator, p1+1);
  if (p2==-1)
    return false;
  if (s.indexOf(Separator, p2+1)!=-1)
    return false;
  y = s.substring(0, p1);
  m = s.substring(p1+1, p2);
  d = s.substring(p2+1);
  y = GetPureNumber(y);
  m = GetPureNumber(m);
  d = GetPureNumber(d);
  if (IsNumber(y)==false || IsNumber(m)==false || IsNumber(d)==false)
    return false;
  if (y.length!=4)
    return false;
  if ((parseInt(m)<1) || (parseInt(m)>12))
    return false;
  if (m==4||m==6||m==9||m==11)
   DaysInMonth = 30;
  else if (m==2)
  {
    if (y%4>0)
      DaysInMonth = 28;
    else if (y%100==0 && y%400>0)
      DaysInMonth = 28;
    else
      DaysInMonth = 29;
  }
  else
    DaysInMonth = 31;
  if ((parseInt(d)<1) || (parseInt(d)>DaysInMonth))
    return false;
  return true;
}

/*******************************************************************************
**  函数名称：GetPureNumber
**  功    能：去除正整数字符串的前置0
**  引用函数：IsNumber
**  返 回 值：去除前置0的正整数字符串
*******************************************************************************/
function GetPureNumber(s)
{
  var l=0, i;
  var ss;
  if ((s==null) || (s.length<=0))
    return "";
  ss = new String(s);
  if (!IsNumber(ss))
    return ss;
  l = ss.length;
  for(i=0; i<l; i++)
  {
    if (ss.charAt(i)!='0')
      break;
  }
  return (ss.substring(i));
}
/*******************************************************************************
**  函数名称：BetweenFloats
**  功    能：浮点数是否在范围之内
**  参    数：
**       v:要检查的浮点数  s1:浮点数范围下界  s2:浮点数范围上界
**  说    明：参数是否为合法浮点型需要自行判断
**  引用函数：IsFloat
**  返 回 值：true false
*******************************************************************************/
function BetweenFloats(v, l, h)
{
  var vv, vl, vh;
  if (IsFloat(v)==false || IsFloat(l)==false || IsFloat(h)==false)
    return (false);
  vv = parseFloat(v);
  vl = parseFloat(l);
  vh = parseFloat(h);
  if ((vl>vv) || (vv>vh))
    return false;
  else
    return true;
}

/*******************************************************************************
**  函数名称：chkDate
**  功    能：检查是否为日期类型
**  参    数：
**  type="1" 不能为空 type="0" 可以为空
**       concole:要检查的文本框
**  说    明：参数是否为合法浮点型需要自行判断
**  引用函数：
**  返 回 值：true false
*******************************************************************************/
function chkDate(concole,type,msg){
	if(concole.value!=""){
	 if(IsDate(concole.value)==false){
		alert(msg+"不是日期类型，请按 YYYY-MM-DD 格式录入日期!");
		concole.focus();
	  return false;
	}
	 }else{
	  alert("请填写"+msg+"!");
	  concole.focus();
	  return false;
	 }
    return true;


}

/*******************************************************************************
**  函数名称：chkFloat
**  功    能：检查浮点型数的格式和大小
**  参    数：
**  type="1" 不能为空 type="2" 可以为空     len1 小数点前的位数
**       concole:要检查的浮点数
**  说    明：参数是否为合法浮点型需要自行判断
**  引用函数：IsFloat
**  返 回 值：true false
*******************************************************************************/
function chkFloat(concole,type,len1,len2,msg){
    s=concole.value;
    if ((s==null) || (s.length<=0)){
        if(type=="1"){
            alert(msg+"不能为空！");
			concole.focus();
            return false;
        }
    }else{         //else begin

   if(IsFloat(s)==false){
            alert(msg+"不是浮点型数据！");
			concole.focus();
            return false;
   }else{
           p1 = s.indexOf(".");
		   if(p1<0){
			if(s.length>parseInt(len1)){
				alert(msg+"长度大于"+len1+"！");
				concole.focus();
				return false;
			}
		   }else {
	           bNum= s.substring(0, p1);

			   if(parseInt(len1)<bNum.length){
				   alert(msg+"浮点型数据精度超过要求！");
				   concole.focus();
					return false;
			   }
		   }
		   //fNum=s.substring(p1+1,s.length);
		   //alert(fNum);
		   return true;

   }

   }//else end
}

function trim(s){
	var begin ,end;
	var i;
	if(s == null)
		return null;
	if(s.length = 0)
		return null;

	begin = 0;
	end = s.length-1;
	while(begin < s.length && s.charAt(begin) == ' '){
		begin++;
	}
	while(end >= 0 && s.charAt(end) == ' '){
		end--;
	}

	if(begin <= end)
		return s.substring(begin,end);
	else
		return null;
}

function _isMail(mail){
	var regExp = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	return (mail.match(regExp) != null);
}

function isMail(mail){
	var sepRegExp = /\s*[,|;]\s*/g;
	var mails = mail.split(sepRegExp);

	if(mails == null){
		return false;
	}

	for(var i = 0; i < mails.length; i++){
		if(!_isMail(mails[i])){
			return false;
		}

	}
	return true;
}

function checkIdcard(idcard){ 
		var errors=[
				"", 
				"身份证号码位数不对", 
				"身份证号码出生日期超出范围或含有非法字符", 
				"身份证号码校验错误"
		]; 
		var idcard,y,jym; 
		var s,m; 
		var idcard_array = []; 
		idcard_array = idcard.split("");
		switch(idcard.length){ 
			case 15:
					if ( (Number(idcard.substr(6,2))+1900) % 4 == 0 || ((Number(idcard.substr(6,2))+1900) % 100 == 0 && (Number(idcard.substr(6,2))+1900) % 4 == 0 )){ 
						ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; 
					}else { 
						ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;
					}
					if(ereg.test(idcard)) {
						return errors[0]; 
					}else {
						return errors[2]; 
					}
			     break; 
			 case 18: 
					if ( Number(idcard.substr(6,4)) % 4 == 0 || (Number(idcard.substr(6,4)) % 100 == 0 && Number(idcard.substr(6,4))%4 == 0 )){ 
						ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9xx]$/;
					}else { 
						ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9xx]$/;
					} 
					if(ereg.test(idcard)){
						s = (Number(idcard_array[0]) + Number(idcard_array[10])) * 7 
						+ (Number(idcard_array[1]) + Number(idcard_array[11])) * 9 
						+ (Number(idcard_array[2]) + Number(idcard_array[12])) * 10 
						+ (Number(idcard_array[3]) + Number(idcard_array[13])) * 5 
						+ (Number(idcard_array[4]) + Number(idcard_array[14])) * 8 
						+ (Number(idcard_array[5]) + Number(idcard_array[15])) * 4 
						+ (Number(idcard_array[6]) + Number(idcard_array[16])) * 2 
						+ Number(idcard_array[7]) * 1 
						+ Number(idcard_array[8]) * 6 
						+ Number(idcard_array[9]) * 3 ; 
						y = s % 11; 
						m = "f"; 
						jym = "10x98765432"; 
						m = jym.substr(y,1);
						if(m == idcard_array[17]) {
							return errors[0];
						}else{
							return errors[3]; 
						}
					} else{
							return errors[2]; 
					}
				   break; 
				default: 
				   return errors[1]; 
				break; 
			}
}

 function selectedCheckbox(sid){
	initYesNoCheckbox(sid,!document.getElementById(sid).checked);
 }
 
  function initYesNoCheckbox(sid,isChecked){
    try{
	  if ( isChecked ) {
		$("#"+sid+"_img").attr("src","../../img/icon_success.png");
		document.getElementById(sid).checked=true;
		$("#"+sid).val("1");
	  } else {
	    $("#"+sid+"_img").attr("src","../../img/tab_iconrig3.png");
		document.getElementById(sid).checked=false;
		$("#"+sid).val("0");
	  }
	}catch(e){
	}
 }
 
 //代码如下所示：
function convertCurrency(money) {
  //汉字的数字
  var cnNums = new Array('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖');
  //基本单位
  var cnIntRadice = new Array('', '拾', '佰', '仟');
  //对应整数部分扩展单位
  var cnIntUnits = new Array('', '万', '亿', '兆');
  //对应小数部分单位
  var cnDecUnits = new Array('角', '分', '毫', '厘');
  //整数金额时后面跟的字符
  var cnInteger = '整';
  //整型完以后的单位
  var cnIntLast = '元';
  //最大处理的数字
  var maxNum = 999999999999999.9999;
  //金额整数部分
  var integerNum;
  //金额小数部分
  var decimalNum;
  //输出的中文金额字符串
  var chineseStr = '';
  //分离金额后用的数组，预定义
  var parts;
  if (money == '') { return ''; }
  money = parseFloat(money);
  if (money >= maxNum) {
    //超出最大处理数字
    return '';
  }
  if (money == 0) {
    chineseStr = cnNums[0] + cnIntLast + cnInteger;
    return chineseStr;
  }
  //转换为字符串
  money = money.toString();
  if (money.indexOf('.') == -1) {
    integerNum = money;
    decimalNum = '';
  } else {
    parts = money.split('.');
    integerNum = parts[0];
    decimalNum = parts[1].substr(0, 4);
  }
  //获取整型部分转换
  if (parseInt(integerNum, 10) > 0) {
    var zeroCount = 0;
    var IntLen = integerNum.length;
    for (var i = 0; i < IntLen; i++) {
      var n = integerNum.substr(i, 1);
      var p = IntLen - i - 1;
      var q = p / 4;
      var m = p % 4;
      if (n == '0') {
        zeroCount++;
      } else {
        if (zeroCount > 0) {
          chineseStr += cnNums[0];
        }
        //归零
        zeroCount = 0;
        chineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
      }
      if (m == 0 && zeroCount < 4) {
        chineseStr += cnIntUnits[q];
      }
    }
    chineseStr += cnIntLast;
  }
  //小数部分
  if (decimalNum != '') {
    var decLen = decimalNum.length;
    for (var i = 0; i < decLen; i++) {
      var n = decimalNum.substr(i, 1);
      if (n != '0') {
        chineseStr += cnNums[Number(n)] + cnDecUnits[i];
      }
    }
  }
  if (chineseStr == '') {
    chineseStr += cnNums[0] + cnIntLast + cnInteger;
  } else if (decimalNum == '') {
    chineseStr += cnInteger;
  }
  return chineseStr;
}