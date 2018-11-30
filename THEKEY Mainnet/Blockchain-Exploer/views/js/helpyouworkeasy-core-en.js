var g_systemVersion = "v1.0.8";
var tempValue="";//用于不同子页面间传递数据的临时数据
var deptData=[];//部门树数据
var g_dictionary=new SimpleMap();
var g_currentUser=null;
var g_userRights="";
var g_UrlBase = "";
var g_FileResUrl="/fileRes/";
var g_DownloadServerUrl = "";
var g_cacheMap=new SimpleMap();//页面缓存
var winInterId=0;
var isRequesting=false;
var requestHeadNum=0;
var requestTailNum=0;
var waittingRequests=[];
var is_use_detail_user_mode=true;// 是否使用详细版本的用户信息
var is_dept_limit=false;// 是否限制部门查看权限
var isShowErrorMsg=true;

function getIsUseDetailUserMode(){
   return is_use_detail_user_mode;
}

function setIsUseDetailUserMode(t){
    is_use_detail_user_mode=t;
}

function setIsDeptLimit(t){
    is_dept_limit=t;
}

function getIsDeptLimit(){
	return is_dept_limit;
}

function WRequest(x,p,f,n,t){
   this.xurl=x;
   this.param=p;
   this.fnCallback=f;
   this.notShowError=n;
   this.to=t;
}

	function Code(c, t, n, s){
		this.code=c;
		this.type=t;
		this.name=n;
		this.isSys=s;
	}
	
	function setCacheMap(key,obj){
	    g_cacheMap.put(key, obj);
	}
	
	function getCacheMap(key){
		var obj=g_cacheMap.get(key);
		if ( !obj ) {
		 return null;
		}
		return obj;
	}
	
	function getFileResUrl(){
	    return g_FileResUrl;
	}
	
	function getDownloadServerUrl(){
    	return g_DownloadServerUrl;
    }
 
	function getUrlBase(){
		return g_UrlBase;
	}
	
	function setTempValue(v){
		tempValue = v;
	}
	
	function getTempValue(){
		return tempValue;
	}
	
	function g_login(){
		var xurl="pages/login.html";
	    openDialog("User Login",xurl,true,380,280,null);
	}
	
	function showHelpWin(w,h,xurl){
	   if ( !w ) w=500;
	   if ( !h ) h=500;
	   if ( !xurl ) xurl="pages/system/help.html";
	   openDialog("Help",xurl,true,w,h,null);
	}
	
	function getCurrentUser(){
	   return g_currentUser;
	}
		
	function setCurrentUser(user, rights){
	  g_currentUser=user;
	  g_userRights=rights;
	  try{
	  	var ht="Welcome:&nbsp;"+g_currentUser.userName;
	    document.getElementById("currentUserInfo").innerHTML = ht;
	  }catch(e){
	  }
	        // init menu
			try{
				var menus=document.getElementsByName("menu");
				var rightId="";
				for ( var i=0;i<menus.length;i++ ) {
					rightId=$("#"+menus[i].id).attr("rightId");
					if ( rightId != '' ) {
						if ( g_isPermit( rightId ) ) {
						  $("#"+menus[i].id).show();
						} else {
						  $("#"+menus[i].id).hide();
						}
					}
				}
			}catch(e){
			}
	}
	
	function isLogout(){
	  showConfirm("Are you sure to quit the system ?", logout);
	}
	
	// 退出系统
	function logout(){
		sendAjaxRequest("/actions/System.action?signoff",[],logoutCallBack);
	}
	
	// 退出系统回调函数
	function logoutCallBack(obj){
		if ( obj.status == true ){
		  g_currentUser=null;
		  g_userRights="";
		  try{
		    document.getElementById("currentUserInfo").innerHTML = "";
		    logoutSuccess();
		  }catch(e){
		  }
	  }
	}
	// 注册账号
	function g_register(){
	  var xurl="pages/organization/userInfo.html?opt=register";
		openDialog("注册账号",xurl,true,400,400,null);
	}
	
	// 修改密码
	function g_changepass(){
	  var xurl="pages/organization/changepass_en.html";
	  openDialog("Change Password",xurl,true,540,240,null);
	}
 
	
	// 判断当前用户是否具有某一权限
	function g_isPermit(rightId){
	  if ( g_userRights == '' ) return false;
	  return ( g_userRights.indexOf(rightId+",") != -1 || g_userRights.indexOf(","+rightId) != -1 );
	}
	/**
	 * 初始化加载全局缓存数据（如部门树，常用代码等）
	 */
	function initGlobalData(isLoadDBdata){
	    var ind=document.URL.indexOf("/index_");
		if ( ind == -1 ) ind = document.URL.indexOf("/index.html");
	    if ( ind != -1 ) {
		  g_UrlBase = document.URL.substring(0,ind);
		} else g_UrlBase = document.URL;
		try{
			proInitGlobalData();
		}catch(e){
		}
		if ( !isLoadDBdata ) return ;
		sendAjaxRequest("/actions/System.action?getCurrentUserInfo",[],optGetCurrentUserCallback);
	}
	/**
	  检查当前登录用户信息
	*/
	function optGetCurrentUserCallback(obj){
	   try{
	    if ( obj.status == true ){
	    	setCurrentUser(obj.body,obj.msg);
			try{
			  loginSuccess();
			}catch(e){}
			try{
			  loadDeptTree();
			}catch(e){}
		    loadCodes();
	    } else loginFail();
	   }catch(e){
	   }
   }
   
   function getUserConfig(){
       return g_currentUser.config;
   }
   
   function updateUserConfig(config){
       g_currentUser.config = config;
   }
   
   /** 选择标签页 */
   function v_selectTag(v_currentTabIndex){
	  // 操作标签
	  var nTds = document.getElementById("v_tab_tr").cells;
	  var taglength = nTds.length;
	  var j;
	  var indexC=0;
	  var d="";
	  for(var i=0; i<taglength; i++){
		d=nTds[i].id;
		indexC=d.substring(10,d.length-2);
		if(v_currentTabIndex!=indexC){
		 try{
		  document.getElementById('tagContent'+indexC+'ImgL').src="img/a4.jpg";
		  $('#tagContent'+indexC+'TD').css({"background-image":"url(img/a6.jpg)"});
		  document.getElementById('tagContent'+indexC+'ImgR').src="img/a5.jpg";
		  document.getElementById('tagContent'+indexC+'ImgR').style.height=21;
		  document.getElementById('tagContent'+indexC+'ImgL').style.height=21;
		  document.getElementById('tagContent'+indexC+'TD').style="display:block;padding-bottom:0px;height: 21px;background-position: center bottom;text-align: center;vertical-align: bottom;";
		 }catch(e){
		 } 
		}
	  }
	  try{
		  document.getElementById('tagContent'+v_currentTabIndex+'ImgL').src="img/a7.jpg";
		  $('#tagContent'+v_currentTabIndex+'TD').css({"background-image":"url(img/a9.jpg)"});
		  document.getElementById('tagContent'+v_currentTabIndex+'ImgR').src="img/a8.jpg";
		  document.getElementById('tagContent'+v_currentTabIndex+'ImgR').style.height=21;
		  document.getElementById('tagContent'+v_currentTabIndex+'ImgL').style.height=21;
	  }catch(e){
	  }
	  
	  // 操作内容
	  for(var i=0;i<=4; i++){
		if ( i == v_currentTabIndex ) {
		 if ( $("#tagContent"+i) ) $("#tagContent"+i).show();
		}else{
		 if ( $("#tagContent"+i) ) $("#tagContent"+i).hide();
		}
	  }
	}
   
	/**
	 * 加载部门树数据
	 * @param obj
	 */
	function loadDeptTree(){
		sendAjaxRequest("/actions/Dept.action?getDeptList",[],loadDeptTreeCallback,true);
	}
	/**
	 * 加载部门树数据 回调函数
	 * @param obj
	 */
	function loadDeptTreeCallback(obj){
       setDeptData( obj.aaData );
	}
	
	/**
	 * 加载常用编码
	 * @param obj
	 */
	function loadCodes(){
		sendAjaxRequest("/actions/System.action?getCodeList",[],loadCodesCallback,true);
	}
	/**
	 * 加载常用编码 回调函数
	 * @param obj
	 */
	function loadCodesCallback(obj){
        if ( obj ) {
        	for ( var i=0;i<obj.aaData.length;i++ ){
        		g_dictionary.put(obj.aaData[i].type+obj.aaData[i].code, 
        				new Code(obj.aaData[i].code, obj.aaData[i].type, obj.aaData[i].name, obj.aaData[i].isSys));
        	}
        }
		sendAjaxRequest("/actions/System.action?getWebParameterList",[],loadParameterCallback,true);
	}
	
	/**
	 * 加载WEB使用到的系统配置 回调函数
	 * @param obj
	 */
	function loadParameterCallback(obj){
        if ( obj ) {
        	for ( var i=0;i<obj.aaData.length;i++ ){
        		if (obj.aaData[i].code == 'DOWNLOAD_SERVER_URL' ) {
				   g_DownloadServerUrl = obj.aaData[i].value;
				} else if (obj.aaData[i].code == 'FILE_RES_URL' ) {
				   g_FileResUrl = obj.aaData[i].value;
				}
        	}
        }
	}
	
	/**
	 * 关闭弹出页面窗口，可被嵌入的子页面调用关闭自己
	 */
	function closeDialog(){
		$( "#g_dialog" ).dialog( "close" );
	}
	
	/**
	 * 关闭弹出选择页面窗口，可被嵌入的子页面调用关闭自己
	 */
	function closeSelectDialog(){
		$( "#g_selectDialog" ).dialog( "close" );
	}
	
	/**
	 * 关闭第三层弹出选择页面
	 */
	function closeSelectDialog2(){
		$( "#g_selectDialog2" ).dialog( "close" );
	}
	
	/**
	 * 弹出页面窗口
	 * @param t
	 * @param xurl
	 * @param isModal
	 * @param w
	 * @param h
	 * @param callback
	 */
	function openDialog(t,xurl,isModal,w,h,callback){
		if ( isModal == undefined ) isModal=true;
		if ( !h ) h=200;
		if ( !w ) w=400;
		$( "#g_dialog" ).append('<iframe id="g_dialog_iframe" src="about:blank" width="100%" height="'+(h-60)+'px" frameborder="0"></iframe>');
		$( "#g_dialog" ).dialog({
			title: t,
			height: h,
			width: w,
			modal: isModal,
			resizable: false,
            beforeClose: function(event, ui) {
            	try{
				  try{
            	    document.getElementById("g_dialog_iframe").contentWindow.doBeforeClose();
				  }catch(e){}
            	  if ( callback ) callback.apply();
            	}catch(e){
            	}
				$("#g_dialog_iframe").remove();
            	return true;
            }
		});
		document.getElementById("g_dialog_iframe").src = xurl;
	}
	
	/**
	 * 弹出第二层选择页面窗口
	 * @param t
	 * @param xurl
	 * @param isModal
	 * @param w
	 * @param h
	 * @param callback
	 */
	function openSelectDialog(t,xurl,w,h,callback){
		if ( !h ) h=200;
		if ( !w ) w=400;
		$( "#g_selectDialog" ).append('<iframe id="g_selectDialog_iframe" src="about:blank" width="100%" height="'+(h-45)+'px" frameborder="0"></iframe>');
		$( "#g_selectDialog" ).dialog({
			title: t,
			height: h,
			width: w,
			modal: true,
			resizable: false,
            beforeClose: function(event, ui) {
            	try{
            	  document.getElementById("g_selectDialog_iframe").contentWindow.doBeforeClose();
            	  if ( callback ) callback.apply();
            	}catch(e){
            	}
				$("#g_selectDialog_iframe").remove();
            	return true;
            }
		});
		document.getElementById("g_selectDialog_iframe").src = xurl;
	}
	
	/**
	 * 弹出第三层选择页面窗口
	 * @param t
	 * @param xurl
	 * @param isModal
	 * @param w
	 * @param h
	 * @param callback
	 */
	function openSelectDialog2(t,xurl,w,h,callback){
		if ( !h ) h=200;
		if ( !w ) w=400;
		$( "#g_selectDialog2" ).append('<iframe id="g_selectDialog_iframe2" src="about:blank" width="100%" height="'+(h-45)+'px" frameborder="0"></iframe>');
		$( "#g_selectDialog2" ).dialog({
			title: t,
			height: h,
			width: w,
			modal: true,
			resizable: false,
            beforeClose: function(event, ui) {
            	try{
            	  document.getElementById("g_selectDialog_iframe2").contentWindow.doBeforeClose();
            	  if ( callback ) callback.apply();
            	}catch(e){
            	}
				$("#g_selectDialog_iframe2").remove();
            	return true;
            }
		});
		document.getElementById("g_selectDialog_iframe2").src = xurl;
	}
	
	function popUpModalDialog(xURL,width,height) {
	    xURL = g_UrlBase+xURL;
	    var showx=($(window).width() - width)/2; 
	    var showy=($(window).height() - height)/2; 
	    return window.open(xURL,xURL,"scrollbars=1,height="+height+",width="+width+",top="+showy+",left="+showx+",status=no,toolbar=no,menubar=no,location=no","false");
	}
	/**
	 * 弹出消息提示框
	 * @param msg
	 */
	function showInfoWin(msg,imgType){
	    if ( imgType == 'ok' ){
		  alertify.success(msg);
		} else if ( imgType == 'warn' ){
		  alertify.error(msg);
		} else if ( imgType == 'error' ){
		  alertify.error(msg);
		}
	
	    /**if ( !imgType ) {
		  $( "#info_msg_td" ).hide();
		} else if ( imgType == 'ok' ){
		  $( "#info_msg_td" ).show();
		  $( "#info_msg_img" ).attr("src", "img/msginfo.gif");
		} else if ( imgType == 'warn' ){
		  $( "#info_msg_td" ).show();
		  $( "#info_msg_img" ).attr("src", "img/msgwarning.gif");
		} else if ( imgType == 'error' ){
		  $( "#info_msg_td" ).show();
		  $( "#info_msg_img" ).attr("src", "img/msgerror.gif");
		}
		$( "#info_msg" ).html(msg);
		$( "#dialog-info" ).dialog({
			resizable: false,
			height:190,
			modal: true,
			buttons: {
				"确定": function() {
					$( this ).dialog( "close" );
				}
			}
		}); */
	}
	function showInfoWinOK(msg,isAutoHide){
	    showInfoWin(msg,"ok");
		/**if ( isAutoHide ) {
		  winInterId = setInterval("gHideInfoWin()",2000);
		}*/
	}
	
	function gHideInfoWin(){
	    clearInterval( winInterId );
		$("#dialog-info").dialog( "close" );
	}
	
	function showInfoWinWarn(msg){
	    showInfoWin(msg,"warn");
	}
	
	function showInfoWinError(msg){
		if ( !isShowErrorMsg ) return ;
	    showInfoWin(msg,"error");
	}
	/**
	  显示或隐藏正在操作窗口
	*/
	function showLoadingInfo(isOpen){
		if ( !isOpen ) {
		    $( "#dialog-loading" ).dialog( "close" );
		    return ;
		}
		$( "#dialog-loading" ).dialog({
			title: "提示信息",
			resizable: false,
			height:120,
			modal: true
		});
	}
	/**
	 * 弹出操作确认框
	 * @param msg
	 * @param okFn
	 */
	function showConfirm(msg,okFn,cancelFn){
	    alertify.confirm("<font size='5px'>"+msg+"</font><br/><br/>", function (e) {
				if (e) {
					if ( okFn ) okFn.apply();
				} else {
					if ( cancelFn ) cancelFn.apply();
				}
			});
	
		/**$( "#confirm_msg" ).html(msg);
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:190,
			width: 300,
			modal: true,
			buttons: {
				"确定": function() {
					if ( okFn ) okFn.apply();
					$( this ).dialog( "close" );
				},
				"取消": function() {
				    if ( okFn ) cancelFn.apply();
					$( this ).dialog( "close" );
				}
			}
		}); */
	}
	/**
	 * 弹出输入对话框
	 * @param t
	 * @param msg
	 * @param okFn
	 * @param v
	 */
	function showInputDialog(t,msg,okFn,v){
		if ( !v ) v="";
		document.getElementById("dialog_input_value").value=v;
		$( "#input_msg" ).html(msg);
		$( "#dialog-input" ).dialog({
			title: t,
			resizable: false,
			height:200,
			modal: true,
			buttons: {
				"确定": function() {
					var v= document.getElementById("dialog_input_value").value;
					if ( v == '' ) return ; 
					if ( okFn ) okFn( v );
					$( this ).dialog( "close" );
				},
				"取消": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}
	/**
	 * 发送AJAX请求
	 * @param xurl
	 * @param param
	 * @param fnCallback
	 */
	function sendAjaxRequest(xurl,param,fnCallback,notShowError,to){
	    if ( to == undefined ) to = 50000;
	    if ( isRequesting ) {
            requestTailNum = requestTailNum +1;
			var so=new WRequest(xurl,param,fnCallback,notShowError,to);
            waittingRequests[requestTailNum] = so;
        } else {
            doSendAjaxRequest( xurl,param,fnCallback,notShowError,to );
        }
	}
	
	function doSendAjaxRequest(xurl,param,fnCallback,notShowError,to){
	    isRequesting = true;
	    if ( !notShowError ) notShowError = false;
	    if ( xurl.indexOf('http://') == -1 ) xurl = g_UrlBase+xurl;

		$.ajax( {
			"url":  xurl,
			"data": param,
			"timeout" : to,
			"contentType" : "application/x-www-form-urlencoded;charset=utf-8",
			"success": function (json) {
				checkwaittingRequests();
				try{
					try{
                       if ( json.msg == 'NOT_LOGIN' ) {
						   loginFail();
						   return ;
					   }
					}catch(e){
					}
				    fnCallback( json );
				}catch(e){
					//alert(e.message);
				}
			},
			"dataType": "json",
			"cache": false,
			"type": "POST",
			"error": function (xhr, error, thrown) {
				try{
			      checkwaittingRequests();
				  if ( !notShowError ) showInfoWinError("操作异常："+xurl);
				  showLoadingInfo(false);
				}catch(e){
				}
			}
		} );
	}
	
	function checkwaittingRequests(){
	    if ( waittingRequests[requestHeadNum] ) {
            waittingRequests[requestHeadNum] = null;
        }
        if ( requestHeadNum == requestTailNum ) {
            if ( requestTailNum != 0 ) {
                requestHeadNum = 0;
                requestTailNum = 0;
            }
            isRequesting = false;
        } else {
            requestHeadNum = requestHeadNum + 1;
			try {
             var so = waittingRequests[requestHeadNum];
             doSendAjaxRequest( so.xurl,so.param,so.fnCallback,so.notShowError,so.to );
			} catch(e){}
        }
	}
	
	/** 初始化拖拽功能 */
    function toDrag(boxId){
	    $( "#"+boxId ).draggable();
    }
	
    /**
     * 刷新部门数据
     * @param d
     */
	function setDeptData(d){
		deptData = d;
	}
	
	function getDeptData(){
		return deptData;
	}
	/**
	 * 查找部门对象
	 * @param deptId
	 * @returns
	 */
	function getDeptById(deptId) {
		 for (var i=0; i<deptData.length; i++) {
			if (deptData[i].deptId == deptId) {
				return deptData[i];
			}
		 }
		return null;
	}
    /**
     * 根据部门ID查询部门路径
     * @param deptId
     * @returns {String}
     */
	function getDeptPath(deptId) {
		var str = '';
		var n=0;
		var pid="";
		var node=getDeptById(deptId);
		if ( !node ) return "";
		str=node.name;
		if ( deptId == 0 ) return str;
		while( node && node.parentId!=0 && n < 1){
	     n++;
	     node=getDeptById(node.parentId);
		 if ( node ) str = node.name + " >> " + str;
		}
		return str;
	}
	/**
     * 判断是否当前登录账号的父部门
     * @param deptId
     * @returns {String}
     */
	function checkIsParentDept(deptId) {
		var str = '';
		var n=0;
		var pid="";
		try{
			var node=getDeptById( getCurrentUser().deptId );
			if ( !node ) return false;
			while( node && n < 20){
				 if ( node.parentId == deptId ) return true;
			     n++;
			     node=getDeptById(node.parentId);
			}
		}catch(e){
			return false;
		}
		return false;
	}
	/**
     * 根据部门ID查询部门名称
     * @param deptId
     * @returns {String}
     */
	function getDeptName(deptId) {
		var node=getDeptById(deptId);
		if ( !node ) return "";
		return node.name;
	}
	/**
	 * 翻译常用代码
	 * @param type
	 * @param code
	 * @returns {String}
	 */
	function getCodeName(type, code, defaultValue){
		var obj=g_dictionary.get(type+code);
		if ( !obj ) {
		 if ( defaultValue ) return defaultValue;
		 return code;
		}
		return obj.name;
	}
	
	/**
	 * 返回常用代码下拉选项
	 * @param type
	 * @returns {String}
	 */
	function getCodeSelectHtml(type,name,hasEmpty,initValue, onchangeFn, allName, ww){
		if ( initValue == undefined ) initValue = "";
		if ( !allName ) allName="&nbsp;&nbsp;";
		if ( !ww ) ww="120px";
		var ht="<select id='"+name+"' style='width:"+ww+"'>";
		if ( onchangeFn ) ht="<select id='"+name+"' onchange='"+onchangeFn+"' style='width:"+ww+"'>";
		if ( hasEmpty ) {
			ht+="<option value=''>"+allName+"</option>";
		}
		var keys=g_dictionary.keySet();
		var code=null;
		for(var i=0;i<keys.length;i++ ){
			code = g_dictionary.get(keys[i]);
			if ( type == code.type ) {
				ht+="<option value='"+code.code+"' ";
				if ( code.code == initValue ) ht+=" selected ";
				ht+=">"+code.name+"</option>";
			}
		}
		ht+="</select>";
		return ht;
	}
	/**
	 * 返回某一类型的字典列表
	*/
	function getCodeListByType(type){
		var keys=g_dictionary.keySet();
		var code=null;
		var reList=[];
		for(var i=0;i<keys.length;i++ ){
			code = g_dictionary.get(keys[i]);
			if ( type == code.type ) {
				reList[reList.length]=code;
			}
		}
		return reList;
	}

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
	// 弹出页面头部的按钮
	function getDialogButtonHtml(buttonName,buttonFnIndex,imgSrc){
	  if ( !buttonFnIndex ) buttonFnIndex = -1;
	  if ( !imgSrc ) imgSrc='img/page_excel.png';
	  var ht ="&nbsp;&nbsp;&nbsp;&nbsp;<span onclick='javascript:headButtonFun("+buttonFnIndex+")' style='cursor:pointer;background-color:#D5E4FD;padding:2px'><img src='"+imgSrc+"' valign='middle' />&nbsp;<font color='#65A7D1'><b>"+buttonName+"</b></span>";
	  return ht;
	}
	// 点击弹出页面头部按钮后的响应事件
	function headButtonFun(n){
	  if ( !n ) return ;
	  if ( n == -1 ) return ;
	  try{
	    if ( n == 1 ) {
	     document.getElementById("g_dialog_iframe").contentWindow.headButtonFun1();
	    } else if ( n == 2 ) {
	     document.getElementById("g_dialog_iframe").contentWindow.headButtonFun2();
	    } else if ( n == 3 ) {
	     document.getElementById("g_dialog_iframe").contentWindow.headButtonFun3();
	    } else if ( n == 4 ) {
	     document.getElementById("g_dialog_iframe").contentWindow.headButtonFun4();
	    } else if ( n == 5 ) {
	     document.getElementById("g_dialog_iframe").contentWindow.headButtonFun5();
	    } else if ( n == 6 ) {
	     document.getElementById("g_dialog_iframe").contentWindow.headButtonFun6();
	    }
	  }catch(e){
	  }
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
	 * 返回12个月份和四个季度下拉选项
	 */
	function get12MonthSelectHtml(name,hasEmpty,initValue, onchangeFn, isNeedSeason){
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
		if ( isNeedSeason ) {
		 ht+="<option value='21' >第一季度</option>";
		 ht+="<option value='22' >第二季度</option>";
		 ht+="<option value='23' >第三季度</option>";
		 ht+="<option value='24' >第四季度</option>";
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
		if ( t > -2 && t < 10 ) return "";
		try{
			var d=new Date(2013,1,1);
		    d.setTime( t );
			var str = d.getFullYear()+"-"+getTwoNumberStr(d.getMonth()+1)+"-"+getTwoNumberStr(d.getDate());
			if ( !isDateStr ) str +=" "+getTwoNumberStr(d.getHours())+":"+getTwoNumberStr(d.getMinutes())
			       +":"+getTwoNumberStr(d.getSeconds());
		    return str;
		}catch(e){
			return "";
		}
	}
	
    function getTimeStrEn(t,isDateStr){
	    if ( !isDateStr ) isDateStr = false;
		if ( !t ) return "";
		if ( t > -2 && t < 10 ) return "";
		try{
			var d=new Date(2013,1,1);
		    d.setTime( t );
			var str = getTwoNumberStr(d.getDate())+"/"+getTwoNumberStr(d.getMonth()+1)+"/"+d.getFullYear();
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
	function getCurrentClientDate(){
	    var d=new Date();
		var str = d.getFullYear()+"-"+getTwoNumberStr(d.getMonth()+1)+"-"+getTwoNumberStr(d.getDate());
	    return str;
	}
	function getCurrentClientDateEn(){
	    var d=new Date();
		var str = getTwoNumberStr(d.getDate())+"/"+getTwoNumberStr(d.getMonth()+1)+"/"+d.getFullYear();
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
	    if ( str == '' ) return 0;
	    try{
	      var d=new Date(2013,1,1);
		  d.setFullYear( Number(str.substring(0,4)) );
		  d.setMonth( Number(str.substring(5,7)) - 1 ); // 0 - 11
		  d.setDate( Number(str.substring(8,10)) );
		  if ( str.length > 15 ) {
		   d.setHours( Number(str.substring(11,13)) );
		   d.setMinutes( Number(str.substring(14,16)) );
		  } else {
		   d.setHours(0);
		   d.setMinutes(0);
		  }
		  d.setSeconds(0,0);
		  return d.getTime();
		}catch(e){
		  return 0;
		}
	}
	
	function toTimestampEn(str){
	    if ( str == '' ) return 0;
	    try{
	      var d=new Date(2013,1,1);
		  d.setFullYear( Number(str.substring(6,10)) );
		  d.setMonth( Number(str.substring(3,5)) - 1 ); // 0 - 11
		  d.setDate( Number(str.substring(0,2)) );
		  if ( str.length > 15 ) {
		   d.setHours( Number(str.substring(11,13)) );
		   d.setMinutes( Number(str.substring(14,16)) );
		  } else {
		   d.setHours(0);
		   d.setMinutes(0);
		  }
		  d.setSeconds(0,0);
		  return d.getTime();
		}catch(e){
		  return 0;
		}
	}
	
	function g_GetNumValue(v,defaultValue){
	    if ( !defaultValue ) defaultValue=0;
	    if ( !v ) return defaultValue;
		if ( v == '' ) return defaultValue;
		if ( !IsNumber( v ) ) return defaultValue;
		return v;
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

	/** 返回世界时 */
	function toWorldTimestamp(str){
	    try{
	      var d=new Date(2013,1,1);
		  d.setFullYear( Number(str.substring(0,4)) );
		  d.setMonth( Number(str.substring(5,7)) - 1 ); // 0 - 11
		  d.setDate( Number(str.substring(8,10)) );
		  d.setHours( Number(str.substring(11,13)) );
		  d.setMinutes( Number(str.substring(14,16)) );
		  d.setSeconds(0);
		  d.setTime(d.getTime() - 8*60*60000);
		  return d.getTime();
		}catch(e){
		  return 0;
		}
	}
	/** 将国际时转换为北京时(YYYY-MM-DD hh) */
	function toBeijingTime(y,m,day,h){
	    try{
	      var d=new Date(2013,1,1);
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
	/** 将YYYY-MM-DD 转换为 年-月-日 */
	function toLongTimeFormat(str){
	    if ( str == '' ) return "";
	    try{
		  var str = str.substring(0,4)+"年"+str.substring(5,7)+"月"+str.substring(8,10)+"日";
		  return str;
		}catch(e){
		  return "";
		}
	}
	/** 返回传入日期的上一天 */
	function getLastDate(str){
	    if ( str == '' ) return "";
	    try{
	      var d=new Date(2013,1,1);
		  d.setFullYear( Number(str.substring(0,4)) );
		  d.setMonth( Number(str.substring(5,7)) - 1 );
		  d.setDate( Number(str.substring(8,10)) );
		  d.setHours( 1 );
		  d.setMinutes(0);
		  d.setSeconds(0);
		  d.setTime( d.getTime() - 24*60*60000 );
		  var str = d.getFullYear()+"-"+getTwoNumberStr(d.getMonth()+1)+"-"+getTwoNumberStr(d.getDate());
		  return str;
		}catch(e){
		  return "";
		}
	}
	/** 翻译URL参数路径  */
	function decodeParamURL(path){
	    path = path.replace(new RegExp("IWHATI","gm"), "?");
		path = path.replace(new RegExp("IANDI","gm"), "&");
		path = path.replace(new RegExp("IEQUALI","gm"), "=");
		path = path.replace(new RegExp("IQUOTEI","gm"), "'");
		path = path.replace(new RegExp("IBRI","gm"), "\n");
		path = path.replace(new RegExp("IDOWNLOAD_SERVER_URLI","gm"), getDownloadServerUrl());
		return path;
	}
	
	/** 翻译成URL参数路径  */
	function encodeParamURL(path){
		path = path.replace(new RegExp("&","gm"), "IANDI");
		path = path.replace(new RegExp("=","gm"), "IEQUALI");
		path = path.replace(new RegExp("'","gm"), "IQUOTEI");
		path = path.replace(new RegExp("\n","gm"), "IBRI");
		return path;
	}
	
	function toHTMLcode(str){
	    if ( str == null ) return "";
		str = str.replace(new RegExp("\n","gm"), "<BR/>");
		return str;
	}
	
	 // 复选框组件
 function checkboxStyleHtml(label,sid,isChecked,isEditable){
    var ht='<span id="'+sid+'_span" style="cursor:pointer" ';
	if ( isEditable ) {
	  ht+='onclick="selectedCheckbox(\''+sid+'\')"';
	}
	ht+='><input id="'+sid+'" type="hidden" checked="'+(isChecked?"true":"false")+'" value="'+(isChecked==true?"1":"0")+'"></input>';
    if ( isChecked ) {
	 ht+='<img src="../../img/icon_success.png" id="'+sid+'_img" width="24px" height="24px"></img>';
    } else {
	 ht+='<img src="../../img/tab_iconrig3.png" id="'+sid+'_img" width="24px" height="24px"></img>';
	}
	ht+=label+'</span>';
	return ht;
 }
	
function writeCommonDiv(){
    var ht='<div id="g_dialog" title="" style="display:none;padding: 0 0 0 0;margin: 0 0 0 0;z-index:10000;overflow:auto">';
    ht+='</div>';
    ht+='<div id="g_selectDialog" title="" style="display:none;padding: 0 0 0 0;margin: 0 0 0 0;overflow:hidden">';
    ht+='</div>';
    ht+='<div id="g_selectDialog2" title="" style="display:none;padding: 0 0 0 0;margin: 0 0 0 0;overflow:hidden">';
    ht+='</div>';
    ht+='<div id="dialog-confirm" title="操作确认" style="display:none;z-index:10000">';
    ht+='<table width="100%" height="100%"><tr><td width="40px" align="center"><img src="img/icon-question.gif" /></td>';
	ht+='<td width="150px" height="100%" align="left" valign="middle"><span id="confirm_msg"></span>';
	ht+='</td></tr></table>';
    ht+='</div>';
    ht+='<div id="dialog-info" title="操作提示信息" style="display:none;z-index:10000">';
    ht+='<table width="100%" height="100%">';
	ht+='<tr>';
	ht+='<td width="40px" align="center" id="info_msg_td"><img id="info_msg_img" src="img/msginfo.gif" /></td>';
	ht+='<td width="150px" align="left" valign="middle"><span id="info_msg"></span></td>';
	ht+='</tr>';
	ht+='</table>';
    ht+='</div>';
    ht+='<div id="dialog-loading" title="" style="display:none;z-index:10000">';
    ht+='<p><span  style="float:left; margin:5px 7px 20px 0;"></span><span style="float:left; margin:1px 7px 20px 0;">操作中,请稍候...<img src="img/loading.gif" /></span></p>';
    ht+='</div>';
    ht+='<div id="dialog-input" title="输入信息" style="display:none;z-index:10000">';
    ht+='<table width="100%" height="100%"><tr><td width="100%" height="100%" align="center" valign="middle">';
	ht+='<span id="input_msg"></span>';
    ht+='<br><input type="text" id="dialog_input_value" value="" size="20" /></td></tr></table>';
    ht+='</div>';
	document.write( ht );
}
   