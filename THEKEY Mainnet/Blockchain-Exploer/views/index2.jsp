<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>   
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" href="views/css/hollybeaconui.css">
	<script type="text/javascript" src="views/js/jquery.min.js" ></script>
	<script type="text/javascript" src="views/js/jquery.hollybeaconui.js" ></script>
	<script type="text/javascript" src="views/js/index.js"></script>
	<link rel="stylesheet" href="views/css/style.css">
	<link rel="stylesheet" href="views/css/tab.css">
	<title>index</title>
</head>
<script type="text/javascript">
var timeOutObj;
initAgentData();
 function initAgentData() {
    var indata = new Object();
    $.ajax({
        type: "post",
        timeout: 10000,
        dataType: "json",
        data: indata,
        url: '<%=basePath%>pageFunction/getNewstInfo',
        success: function (data, status, xhr) {
        	$("#startTime").text(data.startTime);
        	$("#startDays").text(data.start_days+" <spring:message code="days"></spring:message>");
        	$("#BlkCount").text(data.BlkCount);
        	$("#traNum").text(data.traNum);
        	$("#UserNum").text(parseInt(data.user_num)+200);
        },
        error: function (xhr, status, err) {
            return false;
        },
        complete: function (xhr, status) {
            xhr.abort();
            xhr = null;
        }
    }); 

 }
 $(function () {
     if (!timeOutObj) {
         timeOutObj = setInterval(initAgentData, 10000);//每隔10秒钟执行一次getSftData
     }

     initAgentData();
 });
    function search(){
    	var searchInfo = $("#searchVal").val();
    	if(searchInfo!= null&&searchInfo!=""){
   	    	window.location.href = "<%=basePath%>pageFunction/serchFunction?searchInfo="+searchInfo;
    	}
   }

</script>
<body class="easyui-layout" style="margin:0px 10%;">
	<div data-options="region:'north',border:false" style="padding:10px 20px; height:90px; margin-top:20px;">
		<div class="logo" style="width: 20%;height: 100%">
			<img src="views/images/logo.png">
		</div>
		<div class="topbox">
			<a href="javascript:toNewstInfoJsp();" class="toplink"><spring:message code="news"></spring:message></a>
			<a href="javascript:toNewstTraJsp();" class="toplink"><spring:message code="latestTransaction"></spring:message></a>
 			<a href="javascript:toAuditRecord();" class="toplink"><spring:message code="CPAPostAudit"></spring:message></a>
			<label class="toplink"><spring:message code="language"></spring:message></label>
			<select id="language" onchange="changeLanguage()">
				<option value=<spring:message code="language1"></spring:message> id="language1" ><spring:message code="chinese"></spring:message></option>
				<option value=<spring:message code="language2"></spring:message> id="language2" ><spring:message code="english"></spring:message></option>
			</select>
		</div>
	</div>
	<div data-options="region:'center',border:false" style="padding:10px;">
		<div class="header clearfix" style="margin: 0px 10%;">
			<input placeholder="<spring:message code="placeholder"></spring:message>" id="searchVal" class="inpsearch1" type="text" >
			<span class="inpsearchbtn" onclick="search()"></span>
		</div>
		<div class="content clearfix">
			<div class="infomationitem">
				<div class="itemtop" id = "startTime"></div>
				<div class="itembottom"><spring:message code="startTime"></spring:message></div>
			</div>
			<div class="infomationitem">
				<div class="itemtop" id = "startDays"></div>
				<div class="itembottom"><spring:message code="runTime"></spring:message></div>
			</div>
			<div class="infomationitem">
				<div class="itemtop" id="BlkCount"></div>
				<div class="itembottom"><spring:message code="totalBlocks"></spring:message></div>
			</div>
			<div class="infomationitem">
				<div class="itemtop" id="traNum"></div>
				<div class="itembottom"><spring:message code="totalTransactions"></spring:message></div>
			</div>
			<div class="infomationitem">
				<div class="itemtop" id="UserNum"></div>
				<div class="itembottom"><spring:message code="totalUsers"></spring:message></div>
			</div>
		</div>
	</div>
</body>
</html>