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
	<meta charset="UTF-8">
	<link rel="stylesheet" href="../views/css/hollybeaconui.css">
	<link rel="stylesheet" href="../views/css/style.css">
	<link rel="stylesheet" href="../views/css/tab.css">
	<script type="text/javascript" src="../views/js/jquery.min.js" ></script>
	<script type="text/javascript" src="../views/js/jquery.hollybeaconui.js" ></script>
	<script src="../views/js/index.js"></script>
	<title>区块查询</title>
</head>
<script type="text/javascript">
	var searchInfo = "<%=request.getParameter("searchInfo")%>";
	var url = "<%=basePath%>pageFunction/searchBlockInfo";
	
	loadSkills(url,searchInfo);
</script>
<body style="margin:0px 10%;">
	<div style="padding:10px 20px; height:90px;">
		<div class="logo" style="width: 20%;height: 100%">
			<img src="../views/images/logo.png">
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
	<div>
		<div class="header" style="padding:10px 0px;">
			<div class="qukuaitit" ><spring:message code="blockInfo"></spring:message></div>
			<div class="qukuaitxt" id="info"></div>
			<div class="clearfix">
				<div class="moditem">
					<div class="moditem_top"><spring:message code="index"></spring:message></div>
					<div class="moditem_box" id="index"></div>
				</div>
				<div class="moditem">
					<div class="moditem_top"><spring:message code="totalTransactions"></spring:message></div>
					<div class="moditem_box " id="traNum"></div>
				</div>
				<div class="moditem">
					<div class="moditem_top"><spring:message code="hash"></spring:message></div>
					<div class="moditem_box " id="hash"></div>
				</div>
				<div class="moditem">
					<div class="moditem_top"><spring:message code="merkleroot"></spring:message></div>
					<div class="moditem_box " id="merkleroot"></div>
				</div>
				<div class="moditem">
					<div class="moditem_top"><spring:message code="time"></spring:message></div>
					<div class="moditem_box " id="startTime"></div>
				</div>
				<div class="moditem">
					<div class="moditem_top"><spring:message code="version"></spring:message></div>
					<div class="moditem_box " id="version"></div>
				</div>
				<div class="moditem">
					<div class="moditem_top "><spring:message code="size"></spring:message></div>
					<div class="moditem_box " id="size"></div>
				</div>
				<div class="moditem">
					<div class="moditem_top "><spring:message code="blockId"></spring:message></div>
					<div class="moditem_box " id="blockId"></div>
				</div>
			</div>
		</div>
		<div class="content clearfix" style="padding:10px 0px;">
			<div style="height:320px;">
				<table id="table1">
				</table>
			</div>
			
			<div class="scriptbox">
				<div class="scriptboxtop"><spring:message code="invocationScript"></spring:message></div>
				<div id="invocation">
				</div>
			</div>
			<div class="scriptbox">
				<div class="scriptboxtop"><spring:message code="verificationScript"></spring:message></div>
				<div id="verification">
				</div>
			</div>
		</div>
	</div>
<script>
	$(function(){
		var txid = "<spring:message code="txid"></spring:message>";
		var sender = "<spring:message code="sender"></spring:message>";
		var receiver = "<spring:message code="receiver"></spring:message>";
		var sys_fee = "<spring:message code="sys_fee"></spring:message>";
		var time = "<spring:message code="time"></spring:message>";
		index.init(txid,sender,receiver,sys_fee,time);
	});
</script>
</body>
</html>