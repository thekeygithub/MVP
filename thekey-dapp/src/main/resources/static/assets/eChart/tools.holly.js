/**
 * 公共组件工具类
 */



var viewer = new Viewer();

/**
 * 页面加载类，方便局部加载页面
 */
function Viewer() {

};

/**
 * 页面局部加载方法，需传入一个页面路径和页面执行的脚本，脚本以__init方法命名
 * dom: 被载入的jquery dom标签
 * url：等待载入的页面
 * js：页面载入成功后执行的脚本
 */
Viewer.prototype.load = function (dom, url, js, callback,noBlock,blockCallback) {
    var el = document.body;
    if(!noBlock)
        App.blockUI(el);
    if (!dom) {
        throw new Error("the dom element can not be null!");
    }
    if (!url) {
        throw new Error("been loaded page can not be null!");
    }
    var str=url.split("?");
    var param="";
    if(str.length>1){
        url=str[0];
        param=str[1];
    }
    $.ajax({
        url: url,
       type:"post",
       data:param,
        dataType: 'html',
        cache: false,
        //async: false,
        success: function (response) {
            dom.html(response)
            if(blockCallback)blockCallback();
            if (js)
                GET_SCRIPT(js, function (jsText) {
                    try {
                        __init(dom, callback);
                        if(!noBlock)
                            App.unblockUI(el);
                    } catch (e) {
                        toastr.error("页面加载异常,请重试...");
                        if(!noBlock)
                            App.unblockUI(el);
                    }
                });
        },
        error: function (jqxhr) {
            // alert(jqxhr["responseText"])
            toastr.error("页面加载异常,请重试...");
            if(!noBlock)
                App.unblockUI(el);
            if(blockCallback)blockCallback();
        }
    });
    /**
     * 获取脚本方法
     * @param js 脚本地址
     * @param callback 回调方法
     */
    function GET_SCRIPT(js, callback) {
        $.ajax({
            url: js,
            cache: false,
            success: function (response) {
                callback(response);
            },
            error: function (jqxhr) {
            }
        });
    }

};

/**
 * 无blockUI，用于情况是两个Viewer同时用时 先用viewer。easyLoad 再用用 viewer。load
 * 页面局部加载方法，需传入一个页面路径和页面执行的脚本，脚本以__init方法命名
 * dom: 被载入的jquery dom标签
 * url：等待载入的页面
 * js：页面载入成功后执行的脚本
 */
Viewer.prototype.easyLoad = function(dom,url,js,callback){
	 var el = document.body;
	if(!dom){
		throw new Error("the dom element can not be null!");
	}
	
	if(!url){
		throw new Error("been loaded page can not be null!");
	}
	
	$.ajax({
       url:url,
       dataType:'html',
       cache:false,
       success:function(response){
           dom.html(response);
           	//构造匿名对象
           	GET_SCRIPT(js,function(jsText){
           		try{
           			//var object = eval('({'+jsText+'})');
		                //执行初始化方法，并传入该dom域
                       /**
		                if(object.__init){
		                	if(object.__init instanceof Function){
		                		object.__init(dom);
		                    }
		                }**/
           	        __init(dom,callback);
           	      
		            }catch (e) {
						// TODO: handle exception
		            	//加载JS出错
		            	//throw new Error("load js error!");
		            	
					}
           	});
               
          
       },
       error:function(jqxhr){
       	 App.unblockUI(el);
       }
    });
	
	/**
	 * 获取脚本方法
	 * @param js 脚本地址
	 * @param callback 回调方法
	 */
	 function GET_SCRIPT(js,callback){
		 
		 $.ajax({
	        url:js,
	        cache:false,
	        success:function(response){
	           
	            callback(response);
	            
	        },
	        error:function(jqxhr){
	            
	        }
	     });
	 }
	
};

//Viewer.prototype.easyLoad = function (option) {
//    var opt={
//        url:"",
//        dataType:"html",
//        js:"",
//        callBack:null,
//        async:true,
//        mainDom:"",
//        pageDomId:"",
//        pageCountId:"",
//        method:""
//    }
//    opt= $.extend(opt,option);
//    if(!opt.url || !opt.mainDom)return;
//    $.ajax({
//        url: opt.url,
//        dataType: 'html',
//        cache: false,
//        async:opt.async,
//        success:function(response){
//            opt.mainDom.html(response);
//            if(opt.callBack)
//                window[opt.callBack]();
//            if(opt.js && opt.method)
//                if(typeof opt.js=="string" && typeof opt.method=="string")
//                    getScript(opt.js,function(){
//                        eval(opt.method+"()")
//                    })
//            if(opt.pageDomId && typeof opt.pageDomId=="string")
//                loadPaging(opt.pageDomId,$("#"+opt.pageCountId).val());
//        },
//        error:function(){
//            alert(1)
//        }
//    });
//    function loadPaging(id,totalCount){
//        var totalPage = totalCount%5==0?totalCount/5:parseInt(totalCount/5+1);
//        var currentPage=1;
//        //分页插件
//        $("#"+id).paginate({
//            count       : 5,        //每个页码组的页码数
//            start       : currentPage,        //开始页数
//            totalPage   : totalPage,//总的页数
//            showFirst   :true,  //显示首页
//            showLast    :true,  //显示末页
//            fixBottom   :true,      //固定在底部
//            mouse       :'press',
//            totalRow    :totalCount,
//            onChange    : function(page){
//                currentPage = page;
//                searchUserList();//回调事件
//            }
//        });
//    }
//    function getScript(js,callback){
//        $.ajax({
//            url: js,
//            cache: false,
//            success: function (response) {
//                callback(response);
//            },
//            error: function (jqxhr) {
//            }
//        });
//    }
//}
/**
 *  js 拼接字符串类
 */
function StringBuffer() {
    this.strings = new Array();
};
StringBuffer.prototype.append = function (str) {
    this.strings.push(str);
    return this;
};
StringBuffer.prototype.toString = function () {
    return this.strings.join("");
};


//toaster 全局参数
function initToastr() {
    //如果父窗口已经生成toastr通知，则直接调用父窗口通知
    //if (parent.toastr) {
        toastr = parent.toastr || toastr ;
    //}
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "3000",
        "extendedTimeOut": "1000",
        "showEasing": "linear",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };


}

/**
 * 过滤字符串函数
 **/
function filterStr(str) {
    return str;

    var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'\"。，、？%+_]");
    var specialStr = "";
    for (var i = 0; i < str.length; i++) {
        specialStr += str.substr(i, 1).replace(pattern, '');
    }
    return specialStr;
}
