$(function () {
    var $play = $("#play");
    var $stop =$("#stop");
    var $line  = $("#line") ;

    word.initMenu();
    word.initText();

    // $("#voiceTransfer").trigger("click");


    $line.on("change",function () {
        $play.show();
        $stop.hide();
    });
         
    
    //绑定播放事件
    $play.on("click",function(){
        if(!$line.val||$line.val().trim()===""){
            layer.msg('请输入文本！',{icon: 7});
            return;
        }
        var URL = "/textminer/listonToRecording";
        var param = {
            line:$line.val().replace(/[\r\n]/g,"")
        };
        $.ajax({
            url:URL,
            data: param,
            type: "post",
            dataType: "json",
            success: function(data) {
                if(data&&data["success"]){
                    $play.hide();
                    $stop.show();
                }else{
                    layer.msg('播放失败！',{icon: 2});
                }
            }, 
            error:function(data){
                layer.msg('网络异常！',{icon: 2});
            }
        })
    });
    
    //绑定暂停事件
    $stop.on("click",function(){
        if(!$line.val||$line.val().trim()===""){
            layer.msg('请输入文本！',{icon: 7});
            return;
        }
       
        var URL = "/textminer/stopRecording";  
        var param = {
            beanName:$line.val().replace(/[\r\n]/g,"")
        }; 
        $.ajax({ 
            url:URL,
            data: param,
            type: "post",
            dataType: "json",
            success: function(data) {
                  if(data&&data["success"]){ 
                      $play.show(); 
                      $stop.hide();
                  }else{
                      layer.msg('暂停失败！',{icon: 2});
                  }
            },
            error:function(data){
                layer.msg('网络异常！',{icon: 2});
            }
        })
    });

    //新词学习 
    $("#newWordLearn").on("click",function(){
        word.activeLabel($("#newWordLearn"));
        $("#newWord_content").html("");
        if(!$line.val||$line.val().trim()===""){
            layer.msg('请输入文本！',{icon: 7});
            return;
        }
        $("#new_word_learn").mLoading("show");//显示loading组件
        $.ajax({
            url:"/textminer/getNewWord",
            data: {
                "line":$line.val().trim()
            },
            type: "post",
            dataType: "json",
            success: function(data) {
                if(data&&data["success"]){
                    word.addLine(data.content,1);
                    $("#new_word_learn").mLoading("hide");//显示loading组件
                }else{
                    layer.msg('新词学习失败！',{icon: 2});
                    $("#new_word_learn").mLoading("hide");//显示loading组件
                }
            } ,
            error:function(){
                $("#new_word_learn").mLoading("hide");//显示loading组件
                layer.msg('网络异常！',{icon: 2});
            }
        })
    });

    //词性分析-文本分词
    $("#wordAnalysis").on("click",function(){
        word.activeLabel($("#wordAnalysis"));
        $("#analysis_content").html("");
        $("#analysis_type").html("");
        if(!$line.val||$line.val().trim()===""){
            layer.msg('请输入文本！',{icon: 7});
            return;
        }
        $("#word_analysis").mLoading("show");//显示loading组件
        $.ajax({
            url: "/textminer/getSegment",
            data:{
                "line" :$line.val().replace(/[\r\n]/g,"")
            },
            type: "post",
            dataType: "json",
            success: function(data) {
                if(data&&data.success){
                    //集合中的内容格式为：会议/n
                    var list = data.content;
                    var result = word.format2Html(list);
                    var html = result["html"];
                    var typeHtml = result["typeHtml"];
                    word.show(html,typeHtml);
                    $("#word_analysis").mLoading("hide");//显示loading组件
                }else{
                    layer.msg('文本分词失败！',{icon: 2});
                    $("#word_analysis").mLoading("hide");//显示loading组件
                }
            },
            error:function(){
                layer.msg('网络异常！',{icon: 2});
                $("#word_analysis").mLoading("hide");//显示loading组件
            }
        })
    });

    //摘要
    $("#summaryAbstract").on("click",function () {
        word.activeLabel($("#summaryAbstract"));
        $("#summary_content").html("");
        if(!$line.val||$line.val().trim()===""){
            layer.msg('请输入文本！',{icon: 7});
            return;
        }
        $("#summary_abstract").mLoading("show");//显示loading组件
        $.ajax({
            url: "/textminer/getSummary",
            data:{
                "line" :$line .val().replace(/[\r\n]/g,"")
            },
            type: "post",
            dataType: "json",
            success: function(data) {
                // var summaryAbs = $("#summary_abstract");
                // summaryAbs.find("p").remove();
                if(data&&data.success){
                    if(!data.content.length){
                        layer.msg('摘要结果为空！',{icon: 7});
                    }
                    $("#summary_content").html('<p style="padding-top: 5px;">'+data.content+'</p>');
                    // summaryAbs.find("h4").after().append('<p style="padding-top: 5px;">'+data.content+'</p>');
                    $("#summary_abstract").mLoading("hide");//显示loading组件
                }else{
                    layer.msg('获取摘要出错！',{icon: 2});
                    $("#summary_abstract").mLoading("hide");//显示loading组件
                }
            } ,
            error:function(){
                layer.msg('网络异常！',{icon: 2});
                $("#summary_abstract").mLoading("hide");//显示loading组件
            }
        })
    });

    //关键词
    $("#keyWord").on("click",function(){
        word.activeLabel($("#keyWord"));
        $("#keyWord_content").html("");
        if(!$line.val||$line.val().trim()===""){
            layer.msg('请输入文本！',{icon: 7});
            return;
        }
        $("#key_word").mLoading("show");//显示loading组件
        var line  =$line.val().replace(/[\r\n]/g,"");
        $.ajax({
            url:"/textminer/getKeyWords",
            data: {
                "line":line
            },
            type: "post",
            dataType: "json",
            success: function(data) {
                if(data&&data["success"]){
                    word.addLine(data.content,2);
                }else{
                    layer.msg('获取关键词出错！',{icon: 2});
                }
                $("#key_word").mLoading("hide");//显示loading组件
            } ,
            error:function(){
                $("#key_word").mLoading("hide");//显示loading组件
                layer.msg('网络异常！',{icon: 2});
            }
        })
    });

    //词向量 - 相关词语
    $("#wordVector").on("click",function () {
        word.activeLabel($("#wordVector"));
        $("#anaWord").bind('click',word.loadChartWord);

    });

    //上传MP3文件进行语音转写
    $("#transfer").on("click",function () {
        var $voice_text =  $("#voice_text");
        var file = $("#dataFile").val();
        if(!file){
            layer.msg('请选择MP3文件！',{icon: 7});
            return;
        }
        $voice_text.hide();
        $("#voice_transfer").mLoading("show");//显示loading组件
        $.ajax({
            url: "/textminer/voice2Text",
            type: 'POST',
            cache: false,
            data: new FormData($('#upload_file_div')[0]),
            processData: false,
            contentType: false,
            success: function (data) { 
                $voice_text.show();
                if(data&&data.success){ 
                    $line.val(data.content);
                }else{
                    layer.msg('语音转写出错！',{icon: 2});
                }
                $("#voice_transfer").mLoading("hide");//显示loading组件
            }
        })
    });

    
    $("#voiceTransfer").on("click",function () {
        word.activeLabel($("#voiceTransfer"));
    }); 
});

var word = {

  //词向量的力导图显示  - 相关词语
    loadChartWord: function () {
        var word = $("#word").val();
        if(!word) {
            layer.msg('请输入术语！',{icon: 7});
            return;
        }
        $("#word_vector").mLoading("show");//显示loading组件
// 获取图标数据的路径
    var chartsUrl = '/textminer/jsonChartWord';
// 话务量地图
    var wordDom = $("#wordLinkDiv");
    var areaChart = new $ECharts({
        url: chartsUrl,
        data: {"word": word},
        dom: wordDom[0],
        parts: 'force',
        callback: function () {
            $("#word_vector").mLoading("hide");//显示loading组件
        }
    })},
    //关键词/新词数据添加（关键词名称与权重）
    addLine:function(content,wordType){
        var html;
        if(wordType===1){//新词 
            html = $("#new_word_learn").find(".tbl");
        }else{//关键词  
            html = $("#key_word").find(".tbl");
        }

        html.html("");
 
        $.each(content, function(i, item) {
            word.line(i,item,wordType,html);
        });
        
        if(wordType===1){//新词
            if(!content.length){
                layer.msg('新词学习结果为空！',{icon: 7});
            }
        }else{//关键词 
            if(!content.length){
                layer.msg('关键词提前结果为空！',{icon: 7});
            }
            $("#type").show();
        }
    },

    //关键词、新词加行
    line:function (i,item,wordType,html) {
        var type = "权重";
        var name;
        var weight ;
        if(wordType===1){//新词 
            // type = "权重";
            var wordRate = item.split("/");
            name = wordRate[0];
            weight = wordRate[1];
        }else{//关键词
            // type = "权重";
            name = item["keyWord"];
            weight = item["weight"];
        }
        if(i%5===0){
            html.append('<div class="col-xs-4 col-lg-4">'+ 
                '<table class="table table-bordered">'+
                '<thead>'+
                '<tr>'+  
                '<th style="width: 50%">名称</th>'+
                '<th style="width: 50%">'+type+'</th>'+
                '</tr>'+
                '</thead>'+
                '<tbody>'+
                '<tr>'+
                '<td>'+name+'</td>'+
                '<td>'+weight+'</td>'+
                '</tr>'+
                '</tbody>'+
                '</table>'+
                '</div>');
        }else{
            $(".table-bordered").find("tbody").last().append('<tr>'+
                '<td>'+name+'</td>'+
                '<td>'+weight+'</td>'+
                '</tr>');
        }
    },
    
    //词分析显示
    show:function (html,typeHtml) { 
        var $wordAnalysis =  $("#word_analysis");
        $wordAnalysis.find("#analysis").find("div").eq(1).hide();
        if(html.length){ 
            $wordAnalysis.find("#analysis").find("div").eq(0).html(html);
            $wordAnalysis.find("#analysis").find("div").eq(1).find("div").html(typeHtml);
            $wordAnalysis.find("#analysis").find("div").eq(1).show();
        }else{
            layer.msg('文本分词结果为空！',{icon: 7});
        }
    },
    
    initMenu:function () {
        //同步执行
        $.ajaxSettings.async = false;
        $.getJSON("/ui/json/menuList.json",function (data) {
            var html = ''; 
            $.each(data["menu"], function(i, item) {
                console.log(item.name+","+item.id+"  ");
                html+= '<a href="#'+item.href+'" id="'+item.id+'" class="list-group-item" data-toggle="tab">'+item.name+'</a> ';
            }); 
            $("#index_menu").html(html);
            //改为异步执行
            $.ajaxSettings.async = true;
        })
    },

    //初始化文本域内容
    initText:function () {
        var text = "会议认为，今年以来，面对复杂多变的国内外形势，各地区各部门按照中央经济工作会议部署，坚持稳中求进工作总基调，贯彻落实新发展理念，以推进供给侧结构性改革为主线，有效推进各项工作，保持了经济发展稳中向好态势。上半年经济运行在合理区间，主要指标好于预期，城镇就业平稳增加，财政收入、企业利润和居民收入较快增长，质量效益回升。物价总体稳定。经济结构调整不断深化，消费需求对经济增长的拉动作用保持强劲，产业结构调整加快，过剩产能继续化解，适应消费升级的行业和战略性新兴产业快速发展，各产业内部组织结构改善。区域协同联动效应初步显现，“一带一路”建设、京津冀协同发展、长江经济带发展三大战略深入实施，脱贫攻坚战成效明显，生态保护、环境治理取得新进展。新发展理念和供给侧结构性改革决策部署日益深入人心，政府和企业行为正在发生积极变化，促进供求关系发生变化，推动了市场信心逐步好转。";
        $("#line").text(text);
    },

    //点击标签时激活标签，添加样式
    activeLabel:function (e) {
        var labels = $("#index_menu").find("a");
        labels.removeClass("active");
        $(e).addClass("active");
    },

    //将分词结果即list集合转化为HTML
    format2Html:function (data) {
        var json = word.initJson("/ui/json/wordPart.json");
        var wordPart = json["wordPart"];

        var html = "";
        var types = new Set();
        $.each(data,function (i,text) {
            var words = text.split("/");
            var word = words[0];
            var type = words[1];

            var fullName = wordPart[type];
            if(!fullName){
                fullName = wordPart["other"];
            }
            var wordTypeClass = fullName.split("|")[1];
            types.add(type);
            var typename= fullName.split('|')[0]
            html += "<span class='wordSpan "+wordTypeClass+"' title='"+typename+"'>"+word+"</span>";
        });

        var typeHtml = "";
        var arrayTypes = Array.from(types);
        var hasOther = false;
        $.each(arrayTypes,function (j,wordType) {
            var fullName = wordPart[wordType];
            if(!fullName){
                fullName = wordPart["other"];
            }
            var wordTypeName = fullName.split("|")[0];
            if((!hasOther && "其他"===wordTypeName)||("其他"!==wordTypeName)){
                if(!hasOther && "其他"===wordTypeName){
                    hasOther = true;
                    typeHtml += "<span class=' wordSpan segment_other'>"+wordTypeName+"</span>";
                }else {
                typeHtml += "<span class='wordSpan segment_"+wordType+"'>"+wordTypeName+"</span>";
                }
            }
        });
        
        return {
            html:html,
            typeHtml:typeHtml
        };
    },

    //获取json内容
    initJson:function (url) {
        var json;
        $.ajax({
            url:url,
            type:"get",
            dataType:"json",
            async:false,
            success:function (data) {
                json = data;
            }
        });
        return json;
    },
    
    //输入文本改变时的清空
    clean:function () {
        
    }

};