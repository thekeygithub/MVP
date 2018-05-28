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
	<link rel="stylesheet" href="views/css/hollybeaconui.css">
	<link rel="stylesheet" href="views/css/style.css">
	<link rel="stylesheet" href="views/css/tab.css">
	<script type="text/javascript" src="views/js/jquery.min.js" ></script>
	<script type="text/javascript" src="views/js/jquery.hollybeaconui.js" ></script>
	<script src="views/js/index.js"></script>
	<title>审计查询</title>
</head>
<script type="text/javascript">
function ww4(date){  
    var y = date.getFullYear();  
    var m = date.getMonth()+1;  
    var d = date.getDate();  
    var h = date.getHours(); 
    var ms = date.getMinutes();
    var mm = date.getSeconds(); 
    return  y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(ms<10?('0'+ms):ms)+':'+(mm<10?('0'+mm):mm);  
      
}  
function w4(s){  
    var ss = (s.split('-'));  
    var y = parseInt(ss[0]);  
    var m = parseInt(ss[1]);  
    var d = parseInt(ss[2]);
    var ss2 = (s.split(' '));
	var h = parseInt(ss2[1]);
    var sss = (s.split(':'));
    var ms = parseInt(sss[1]);
    var mm = parseInt(sss[2]);  
    if (!isNaN(y) && !isNaN(m) && !isNaN(d) && !isNaN(h) && !isNaN(ms) && !isNaN(mm)){  
        return new Date(y,m-1,d,h,ms,mm);  
    } else {  
        return new Date();  
    }  
}  

</script>
<body style="margin:0px 10%;">
	<div style="padding:10px 20px; height:90px;">
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
	<div class="header" style="padding:10px 0px;">
		<div class="qukuaitit" ></div>
		<div class="qukuaitxt" id="info"></div>
		<div class="clearfix">
			<div class="clearfix" style="width: 95%;float: left;">
				<form action="" method="post" id="searchForm">
					<div class="formitem">
						<label class="formtit"><spring:message code="visitTime"></spring:message>:</label>
						<div class="formbox">
							<input style="width:100%;" type="text" class="easyui-datetimebox" id = "startTime" name = "startTime" data-options="formatter:ww4,parser:w4">
						</div>
					</div>
					<div class="formitem">
						<label class="formtit"><spring:message code="to"></spring:message></label>
						<div class="formbox">
							<input style="width:100%;" type="text" class="easyui-datetimebox" id="endTime" name="endTime" data-options="formatter:ww4,parser:w4">
						</div>
					</div>
					<div class="formitem">
						<label class="formtit"><spring:message code="medicalInstitution"></spring:message>:</label>
						<div class="formbox">
							<!-- <input id="comboorg" style="width:100%;" class="easyui-combobox" /> -->
							 <select name="medicalInstitution" style="width:100%;" class="easyui-combobox">
								<option value="">全部</option>
								<option >人民医院</option>
								<option >积水潭医院</option>
							</select>  
						</div>
					</div>
					<div class="formitem">
						<label class="formtit"><spring:message code="illegalType"></spring:message>:</label>
						<div class="formbox">
							<select id="comborule" name="illegalType" style="width:100%;" class="easyui-combobox">
						    	<option value="">全部</option>
							  	<option >限女性使用</option>
								<option >限男性使用</option>
								<option >限儿童使用</option>
								<option >限新生儿使用</option>
								<option >二级以上医院使用使用</option>
								<option >三级医院使用</option>
								<option >限三级综合，肿瘤专科二线医院用药</option>
							</select> 
						</div>
					</div>
					<div class="formitem">
						<label class="formtit"><spring:message code="InsuredPerson"></spring:message>:</label>
						<div class="formbox">
							<input style="width:100%;" class="easyui-textbox" type="text" name="name">
						</div>
					</div>
					<div class="formitem"s>
						<label class="formtit"><spring:message code="sex"></spring:message></label>
						<div class="formbox">
							<!-- <input id="combosex" style="width:100%;" class="easyui-combobox" /> -->
							 <select name="sex" style="width:100%;" class="easyui-combobox">
								<option value="">全部</option>
							  	<option >男</option>
								<option >女</option>
						  	</select> 
						</div>
					</div> 
				</form>
			</div>
			<div class="clearfix" style="padding-left: 95%; text-align: right;">
				<span class="searchbtn" type="button" value="检索" onclick="searchAudit()"></span>
			</div>
		</div>
		
		<div class="content clearfix" style="padding:10px 0px;">
			<div style="height:300px;">
				<table id=datagrid>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	var medicalInstitution = "<spring:message code="medicalInstitution"></spring:message>";
	var visitType = "<spring:message code="visitType"></spring:message>";
	var illegalType = "<spring:message code="illegalType"></spring:message>";
	var illegalDrug = "<spring:message code="illegalDrug"></spring:message>";
	var Diagnosis = "<spring:message code="Diagnosis"></spring:message>";
	var Treatment = "<spring:message code="Treatment"></spring:message>";
	var visitTime = "<spring:message code="visitTime"></spring:message>";
	var sex = "<spring:message code="sex"></spring:message>";
	var name = "<spring:message code="name"></spring:message>";
	var sfzh = "<spring:message code="sfzh"></spring:message>";
	var project = "<spring:message code="project"></spring:message>";
	auditRecord.init(medicalInstitution,visitType,illegalType,illegalDrug,Diagnosis,Treatment,visitTime,sex,name,sfzh,project);
	</script>
</body>
</html>