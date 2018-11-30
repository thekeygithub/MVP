// JavaScript Document

function jsPrint()
{
	if (confirm('确定打印吗？'))
	{
		$('print').style.display="none";
		window.print();
	}
}
function jsPrintThisPage()
{
	var url = window.location.href;
	var arr = url.split('?');
	window.open('pri_print.php?'+arr[1]);
}
function listItemTask( id, task )
{
	var f = document.adminForm;
	f.CID.value = id;
	f.boxchecked.value = 1;
	submitbutton(task);
	return false;
}
function submitbutton(pressbutton) {
	submitform(pressbutton);
}
function submitform(pressbutton){

	if (pressbutton) {
		document.adminForm.act.value=pressbutton;
	}
	if (typeof document.adminForm.onsubmit == "function") {
		document.adminForm.onsubmit();
	}
	document.adminForm.submit();
}
String.prototype.trim = function()
{
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
function $(id){return document.getElementById(id)}
function addOptionGroup(selectId,optGroupString)
{
    var optGroup = optGroupString.split(",");
    var objSelectNow = $(selectId);
	if(objSelectNow.options==undefined)
	{
		try
		{
			$('#'+selectId).empty();
		}catch(e)
		{
			alert('无法获取数据');
		}
	}else
	{
    	objSelectNow.length = 0;
	}
	
    for (i=0; i<optGroup.length; i++)
    {
        if(optGroup[i]!="")
        {
            var opt = optGroup[i].split("|");
            addOption(objSelectNow, opt[0], opt[1],selectId);
        }
    }
}
function addOption(objSelectNow,txt,val,selectId)
{
    var objOption = document.createElement("option");
    objOption.text= txt;
    objOption.value=val;
	if(objSelectNow.options==undefined)
	{
		try
		{
			$('#'+selectId).append("<option value='"+val+"'>"+txt+"</option>");
		}catch(e)
		{
			alert('无法获取数据');
		}
	}else
	{
   	 objSelectNow.options.add(objOption);
	}
}
function uploadFile(mytype,myflagid)
{
	var theform = document.getElementById("frm");
	if(mytype == 1)
	{
		var msg = document.getElementById("divshowuploadmsg_p");
		var msgok = document.getElementById("divshowuploadmsgok_p");
	}else
	{
		var msg = document.getElementById("divshowuploadmsg_z");
		var msgok = document.getElementById("divshowuploadmsgok_z");
	}
	var oldAction = theform.action;
	var oldonSubmit = theform.onSubmit;
	msgok.style.display = 'none';
	msg.style.display = '';
	msg.innerHTML = "正在上传中，请您稍等......";
	theform.action = "attachment.php?mytype="+mytype+"&myflagid="+myflagid;
	theform.onSubmit = "";
	theform.target = "phpframe";
	theform.submit();
	theform.action = oldAction;
	theform.onSubmit = oldonSubmit;
	theform.target = "";
	return false;
}
function uploadFileEdit(mytype,pid)
{
	if(document.getElementById("pic").value == '' && document.getElementById("zhiwen").value == '')
	{
		return false;
	}
	var theform = document.getElementById("frm");
	if(mytype == 1)
	{
		var msg = document.getElementById("divshowuploadmsg_p");
		var msgok = document.getElementById("divshowuploadmsgok_p");
	}else
	{
		var msg = document.getElementById("divshowuploadmsg_z");
		var msgok = document.getElementById("divshowuploadmsgok_z");
	}
	var oldAction = theform.action;
	var oldonSubmit = theform.onSubmit;
	msgok.style.display = 'none';
	msg.style.display = '';
	msg.innerHTML = "正在上传中，请您稍等......";
	theform.action = "attachmentedit.php?mytype="+mytype+"&PID="+pid;
	theform.onSubmit = "";
	theform.target = "phpframe";
	theform.submit();
	theform.action = oldAction;
	theform.onSubmit = oldonSubmit;
	theform.target = "";
	return false;
}
function showHidden(id)
{
	if(document.getElementById(id).style.display=="")
	{
		document.getElementById(id).style.display="none";
	}else
	{
		document.getElementById(id).style.display="";
	}
}
//搜索校验
function check_search(frm)
{
	if(frm.keyes.value == '')
	{
		return false;
	}
	return true;
}
//关闭窗口
function win_close()
{
	window.opener=null;
	window.open('','_self');
	window.close();	
}

//检查日期格式
function isDate(datestr)
{
	var newPar=/^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])$/;
	return newPar.test(datestr);
}

//检查出生日期是否成年
function isAdult(datestr)
{	
	var birth = datestr.split("-");
	var year=parseInt(birth[0])+parseInt("18");
	year=year+birth[1]+birth[2];
	/*var s="";
    var theDate=new Date();
    s += theDate.getFullYear(); // 获取年份。
	var Month= parseInt(theDate.getMonth())+parseInt("1");
    var Day = theDate.getDate();
	if (Month >= 10 )
	{
    	 s+= Month;
    }
    else
	{
     	s+="0"+ Month;
    }
    if (Day >= 10 ){
     s += Day ;
    }
    else{
     s +=0+ Day ;
    }*/
	var day = new Date();
    var Year = 0;
    var Month = 0;
    var Day = 0;
    var CurrentDate = "";
    Year= day.getFullYear();//支持IE和火狐浏览器.
    Month= day.getMonth()+1;
    Day = day.getDate();
    CurrentDate += Year;
    if (Month >= 10 ){
     CurrentDate += Month;
    }
    else{
     CurrentDate += "0" + Month;
    }
    if (Day >= 10 ){
     CurrentDate += Day ;
    }
    else{
     CurrentDate += "0" + Day ;
    }

	if(year>CurrentDate)
	{
		return false;
	}
	return true;
}

function checkinfo(str)
{
	var pattern=/^[a-z0-9A-Z]+([a-z0-9A-Z_-]+){0,16}$/;
	return pattern.test(str);	
}

//检查是否为任意数（实数）
function isNumeric(strNumber)
{
	var newPar=/^(-|\+)?\d+(\.\d+)?$/;
	return newPar.test(strNumber);
}

//检查是否为正数
function isUnsignedNumeric(strNumber)
{
	var newPar=/^\d+(\.\d+)?$/;
	return newPar.test(strNumber);
}

//检查是否为整数
function isInteger(strInteger)
{
	var newPar=/^(-|\+)?\d+$/;
	return newPar.test(strInteger);
}

//检查是否为正整数
function isUnsignedInteger(strInteger)
{
	var newPar=/^\d+$/;
	return newPar.test(strInteger);
}


var sUserAgent = navigator.userAgent;
var isOpera = sUserAgent.indexOf("Opera") > -1;
var isIE = sUserAgent.indexOf("compatible") > -1 && sUserAgent.indexOf("MSIE") > -1 && !isOpera;

function setPointer(theLenth, theRowNum, theAction, theDefaultColor, thePointerColor, theMarkColor, theTr)
{
	var theDefaultColor='#FFFFFF';
	var thePointerColor='#FFCC99';
	var theMarkColor='#fff2e7';
	if(theTr==undefined)
	{
		var theRow = document.getElementById('tr_'+theRowNum);
	}else
	{
		var theRow = document.getElementById(theTr+theRowNum);
	}
	var theCells = theRow.getElementsByTagName('td');
    var rowCellsCnt  = theCells.length;
	
    var currentColor = null;
	currentColor = theCells[0].style.backgroundColor;
    if (currentColor.indexOf("rgb") >= 0) 
    {
        var rgbStr = currentColor.slice(currentColor.indexOf('(') + 1,
                                     currentColor.indexOf(')'));
        var rgbValues = rgbStr.split(",");
        currentColor = "#";
        var hexChars = "0123456789ABCDEF";
        for (var i = 0; i < 3; i++)
        {
            var v = rgbValues[i].valueOf();
            currentColor += hexChars.charAt(v/16) + hexChars.charAt(v%16);
        }
    }
	
	if(theAction=='over')
	{
		for (var c = 0; c < rowCellsCnt; c++)
		{
			if(currentColor.toLowerCase()!=theMarkColor.toLowerCase())
			{
				theCells[c].style.backgroundColor = thePointerColor;
			}
		}
	}
	if(theAction=='out')
	{
		for (var c = 0; c < rowCellsCnt; c++)
		{
			if(currentColor.toLowerCase()==thePointerColor.toLowerCase())
			{
				theCells[c].style.backgroundColor = theDefaultColor;
			}
		}
	}
	if(theAction=='click')
	{
		for(var i=1;i<=theLenth;i++)
		{
			if(i!=parseInt(theRowNum))
			{
				if(theTr==undefined)
				{
					var nRow = document.getElementById('tr_'+i);
				}else
				{
					var nRow = document.getElementById(theTr+i);
				}
				var nCells = nRow.getElementsByTagName('td');
				var nCellsCnt  = nCells.length;
				for (var c = 0; c < nCellsCnt; c++)
				{
					nCells[c].style.backgroundColor = theDefaultColor;
				}
			}
		}
		for (var c = 0; c < rowCellsCnt; c++)
		{
			if(currentColor.toLowerCase()==theMarkColor.toLowerCase())
			{
				theCells[c].style.backgroundColor = theDefaultColor;
			}else
			{
				theCells[c].style.backgroundColor = theMarkColor;
			}
		}//end_for
	}//end_if
}//end_func
