<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="views/css/hollybeaconui.css">
	<link rel="stylesheet" href="views/css/style.css">
	<link rel="stylesheet" href="views/css/tab.css">
	<script type="text/javascript" src="views/js/jquery.min.js" ></script>
	<script type="text/javascript" src="views/js/jquery.hollybeaconui.js" ></script>
	<script src="views/js/index.js"></script>
	<title>最新交易</title>
</head>
<body class="easyui-layout" style="margin:0px 10%;">
	<div data-options="region:'north',border:false" style="padding:10px 20px; height:90px;">
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
		<div class="easyui-layout" data-options="fit:true">
			<div class="header" data-options="region:'north',border:false" style="height:50px; padding:10px 0px;">
				<div class="qukuaitit" style="float:left;" ><spring:message code="totalTransactions"></spring:message>：<span id="traNum">10</span></div>
				<!-- <div class="head_right">
					<select>
						<option value="any">any</option>
						<option value="con">con</option>
					</select>
				</div> -->
			</div>
			<div data-options="region:'center',border:false" class="content clearfix" style="padding:5px 0px; margin-top:0px;">
				<table id="table2">
				</table>
			</div>
		</div>
	</div>
<script>
	$(function(){
		var serialNo = "<spring:message code="serialNo"></spring:message>";
		var txType = "<spring:message code="txType"></spring:message>";
		var txid = "<spring:message code="txid"></spring:message>";
		var sender = "<spring:message code="sender"></spring:message>";
		var receiver = "<spring:message code="receiver"></spring:message>";
		var sys_fee = "<spring:message code="sys_fee"></spring:message>";
		var time = "<spring:message code="time"></spring:message>";
		newestTra.init(serialNo,txType,txid,sender,receiver,sys_fee,time);
	});
</script>
</body>
</html>