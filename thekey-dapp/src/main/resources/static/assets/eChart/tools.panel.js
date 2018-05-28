/**
 * Created by zhaihw on 2016/3/21.
 */
(function(factory){
    "use strict";//启用严格模式
    if (typeof define === 'function' && define.amd && define.amd.jQuery) {
        define(['jquery'], factory);
    }else if(typeof exports === 'object') {
        factory(require('jquery'));
    }else {
        factory(jQuery);
    }
}(function($,undefined) {
    "use strict";//启用严格模式
    //之前已经加载，则无需进行加载
    if ($.panel) {
        return
    }

    $.panel={
        defaults: {
            title: "",
            scene: "panel-default"
        }
    }
    /**
     * panel原始对象
     */
    $.panel.core=function(){

    }
    /**
     * 原型扩展
     * @type {{ init: Function}}
     */
    $.panel.core.prototype={
        //初始化函数
        init:function(el,options){
            var style ;
            if(options["style"])
                style = options["style"];
            this.setting = options;
            this.element = $(el).addClass("panel "+this.setting.scene) ;
            var html =  '<div class="panel-heading">'+
                         '<div class="panel-title">'+this.setting.title+'</div>'+
                        '</div>'+
                        '<div class="panel-body" ';
            if(style)
                html+='style="'+style+'"'
            html+="></div>"
            this.element.html(html);
        }
    }
    /**
     * 创建panel
     * @param el 选中的元素
     * @param options 选项
     * @returns {$.panel.core}
     */
    $.panel.create=function(el,options){
        options = $.extend({}, $.panel.defaults,options);
        var panel = new  $.panel.core();
        panel.init(el,options)
        return panel;
    }

    /**
     * 重新获取panel
     * @param p
     * @returns {*}
     */
    $.panel.reference = function(p){
        return $(p).hasClass("panel")?p:null;
    }
    /**
     * 产生一个panel
     * @param args
     * @returns {$.fn}
     */
    $.fn.panel=function(args){
        this.each(function(){
            var instance = $.panel.reference(this);
            if(!instance)
                $.panel.create(this,args);
        });
        return this;
    }

    /**
     * panel-body ,
     *  如果args有值 则获取将args填充到panel-body中,并返回当前面板
     *  如果没值，则返回panel-body元素
     * @param args
     * @returns {*|jQuery}
     */
    $.fn.panel_body=function(args){
        if(!!args){
            this.each(function(){
                var instance = $.panel.reference(this);
                if(!instance)
                    $.panel.create(this,args);
                $(this).find(".panel-body").html(args)
            });
            return this;
        }else{
            return $(this).find(".panel-body");
        }
    }
    /**
     * panel-title ,
     *  如果args有值 则获取将args填充到panel-title中，并返回当前面板
     *  如果没值，则返回panel-title元素
     * @param args
     * @returns {*|jQuery}
     */
    $.fn.panel_title=function(args){
        if(!!args){
            this.each(function(){
                var instance = $.panel.reference(this);
                if(!instance)
                    $.panel.create(this,args);
                $(this).find(".panel-title").html(args)
            });
            return this;
        }else{
            return $(this).find(".panel-title");
        }

    }
}));