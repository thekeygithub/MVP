/**
 * Created by zhaihw on 2016/3/22.
 */
/**
 * url[,data],dom,parts[,click,error,callback,optionCallback]
 * parts @see this.chart
 * data 提交到后台的数据 || 当URL为空时使用Data构造图表
 * callback 回调，与图表无关，入参为后台返回的数据
 * optionCallback 回调，改变全局option，入参为全option
 *
 */
function $ECharts(params) {
    this.instance = null;
    var tip = layer;
    if (!params || !params['parts'] || !params['dom']) {
        if (tip)
            tip.msg('缺少必要的属性，不能渲染图表！',{icon: 7});
        else
            // tip.msg('缺少必要的属性，不能渲染图表！',{icon: 7});
            alert("缺少必要的属性，不能渲染图表");
        return;
    }
    params = $.extend({}, this.defaults, params);
    var parts = this.chart.parts[params['parts']], optMethod = this.chart[params['parts']], data = "";
    if (params["data"] != undefined)
        data = params["data"];
    var that = this;
    if(params.url) {
        $.ajax({
            url: params.url,
            type: "post",
            dataType: "json",
            data: data,
            success: function (result) {
                if (typeof params['callback'] == 'function') {
                    params.callback(result);
                }
                if (result.success) {
                    var option = optMethod(result.content);//获取options
                    if (typeof params.optionCallback == 'function') {
                        params.optionCallback(option);
                    }
                    that.init(params.dom, parts, option, params.notMerge, params['click']);//初始echarts
                } else {
                    if (tip)
                        tip.msg(result.errorMessage,{icon: 2});
                    else
                        alert(result.errorMessage)
                }
            }, error: function (e) {
                if (tip)
                    tip.msg('网络异常，请重新登录再试！',{icon: 2});
                else
                    alert("网络异常，请重新登录再试");
                if (params["error"] != undefined && typeof params["error"] == 'function') {
                    params.error(e);
                }
            }
        });
    }else{ // 无URL请求则使用data的数据生成图表
        if(data){
            var option = optMethod(data);//获取options
            if (typeof params.optionCallback == 'function') {
                params.optionCallback(option);
            }
            that.init(params.dom, parts, option, params.notMerge, params['click']);//初始echarts
        }else{
            if (tip)
                tip.msg('缺少必须数据无法展现图表！',{icon: 7});
            else
                alert("缺少必须数据无法展现图表");
        }
    }

    /**
     * 获取echarts实例，如果不存在则为null
     * @returns {null|*}
     */
    this.getEChartsInstance = function () {
        return this.instance;
    }
}

// /**
//  * 默认参数
//  * @type {{notMerge: boolean, downloadUrl: string}}
//  */
// $ECharts.prototype.defaults = {
//     notMerge: false,
//     //mask:true,
//     downloadUrl: contextPath + "/download/downloadImg.do"//图片下载地址
// };
/**
 * 初始化
 * @param dom dom元素
 * @param parts echarts依赖
 * @param options 选项
 * @param merge 是否合并
 * @param click 图表单击事件
 */
$ECharts.prototype.init = function (dom, parts, options, merge, click) {
    var that = this;
    require(parts, function (e) {
        if (merge)
            that.instance = e.init(dom).setOption(options, merge);
        else
            that.instance = e.init(dom).setOption(options);
        if (click && typeof click == 'function') {
            var ecConfig = require('echarts/config');
            that.instance.on(ecConfig.EVENT.CLICK, function (param) {
                click(param)
            });
        }
    });
};
/**
 * 图片导出
 * @param callback 回调，会接收两个参数，第一个为导出的文件名，第二个为传入的params
 * @param params 期望回调函数接受的参数
 */
$ECharts.prototype.exportImg = function (callback, params) {
    var img = this.getEChartsInstance().getImage();
    var data = img.href ? img.href : img.src, url = this.defaults.downloadUrl;
    $.ajax({
        type: "post",
        data: "data=" + encodeURIComponent(data),
        dataType: "text",
        url: url,
        async: false,
        success: function (d) {
            if (d && d != 'error') {
                if (typeof callback == 'function')
                    callback(d, params);
            } else {
                layer.msg('下载图片出错！',{icon: 2});
            }
        }, error: function (e) {
            layer.msg('下载图片出错！',{icon: 2});
        }
    });
};

/**
 * 支持的图表列表
 *  如需扩展，只需增加图表依赖，并提供其产生option的方法即可
 */
$ECharts.prototype.chart = {
    parts: {
        line: ['echarts', 'echarts/chart/bar', 'echarts/chart/line'],
        bar: ['echarts', 'echarts/chart/bar', 'echarts/chart/line'],
        pie: ['echarts', 'echarts/chart/pie', 'echarts/chart/funnel'],
        funnel: ['echarts', 'echarts/chart/pie', 'echarts/chart/funnel'],
        map: ['echarts', 'echarts/chart/map'],
        gauge: ['echarts', 'echarts/chart/gauge'],
        eventRiver: ['echarts', 'echarts/chart/eventRiver'],
        force: ['echarts', 'echarts/chart/force']
    },
    line: barOrLineOption,
    bar: barOrLineOption,
    pie: pieOption,
    funnel: pieOption,
    map: mapOption,
    gauge: gaugeOption,
    eventRiver: eventRiverOption,
    force: getForceOptions
};
function mapOption(result) {
    var title = "", data = [], minV = 0, maxV = 0, mapType = "china";
    if (result["title"] != undefined)
        title = result["title"];
    if (result["data"] != undefined) {
        data = result["data"];
        if (data instanceof Array && data.length > 0) {
            minV = 0; maxV = data[0].value;
            for (var i = 0; i < data.length; i++) {
                //if (minV > data[i].value)minV = data[i].value;
                if (maxV < data[i].value)maxV = data[i].value;
            }
        }
    }
    if (result["mapType"] != undefined)
        mapType = result["mapType"];//默认为中国地图

    return {
        title: {
            text: title,
            x: 'center'
        },
        tooltip: {
            trigger: 'item'
        }, dataRange: {
            min: minV,
            max: maxV,
            x: 'left',
            y: 'bottom',
            text: ['高', '低'],           // 文本，默认为数值文本
            calculable: true,
            color: ['#ee2c2c', '#F5DEB3']
        },
        roamController: {
            show: true,
            x: 'right',
            mapTypeControl: {
                'china': true
            }
        },
        series: [
            {
                name: '通话量',
                type: 'map',
                mapType: mapType,
                roam: false,
                itemStyle: {
                    normal: {label: {show: true}},
                    emphasis: {label: {show: true}}
                },
                data: data
            }
        ]
    };
};
function pieOption(result) {
    var legend = new Array(), title = "", data = [], maxV = 0;
    if (result["title"] != undefined)
        title = result["title"];
    if (result["data"] != undefined) {
        data = result["data"];
        for (var x in data) {
            //legend.push(data[x].name)//获取图例
            if (data[x].value > maxV)maxV = data[x].value
        }
    }
    return {
        title: {
            text: title,
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} :{c} ({d}%)"
                /*(function (params) {
                //原是字符串，如："{a} <br/>{b} :{c} ({d}%)";因漏斗图无{d}参数，则改为返回函数
                 return params[0] + " <br/>" + params[1] + " :" + params[2] + (params[3] ? " (" + params[3] + "%)" : "");
            })*/
        },
        //legend: {
        //    orient: 'vertical',
        //    x: 'left',
        //    data: legend
        //},
        toolbox: {
            y: "right",
            show: true,
            feature: {
                //mark : {show: true},
                //dataView : {show: true, readOnly: false},
                magicType: {
                    show: true,
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            x: '25%',
                            width: '50%',
                            funnelAlign: 'left',
                            max: maxV
                        }
                    }
                },
                restore: {show: true}
            }
        },
        calculable: true,
        series: [
            {
                name: "话务量",
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: data,
             /*   itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            formatter: '{b} : {c} ({d}%)'
                        },
                        labelLine: {show: true}
                    }
                }*/

            }
        ]
    };
};
function gaugeOption(result) {
    var title = "", data = [];
    if (result["title"] != undefined)
        title = result["title"];
    if (result["data"] != undefined)
        data = result["data"];
    return {
        title: {
            text: title,
            x: 'center'
        },
        tooltip: {
            formatter: "{a} <br/>{b} : {c}%"
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        series: [
            {
                name: title,
                type: 'gauge',
                detail: {formatter: '{value}'},
                data: data
            }
        ]
    };
};
function barOrLineOption(result) {
    var title = "", data = [], xAxis = [], type = 'bar',
        legend = new Array(), series = new Array();
    if (result["title"] != undefined)
        title = result["title"];
    if (result["data"] != undefined)
        data = result["data"];
    if (result["xAxis"] != undefined)
        xAxis = result["xAxis"];
    if (result["type"] != undefined)
        type = result["type"];
    //数据为空时，显示为暂无数据
    if (xAxis == "") {
        series.push({});
    } else {
        for (var x in data) {
            legend.push(x)
            series.push({
                name: x,
                type: type,
                data:data[x] ,
                itemStyle: {normal: {label: {show: true}}},
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
                //markPoint: {
                //    data: [
                //        {type: 'max', name: '最大值'},
                //        {type: 'min', name: '最小值'}
                //    ]
                //}
            });
        }
    }
    var basic = {
        grid: {
            y: 80,
            y2: 35
        },
        title: {
            text: title
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: legend,
            x: "center"
            //y: "center"
            //orient: 'vertical'
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true}
            }
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                data: xAxis
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ]
    };
    return $.extend(basic, {series: series});
};
//力导向图词语关系图
function getForceOptions(result) {
    return {
        title: {
            text: '',
            subtext: '',
            x: 'right',
            y: 'bottom'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} : {b}'
        },
        toolbox: {
            show: true,
            feature: {
                restore: {show: true},

                saveAsImage: {show: true}
            }
        },
        legend: {
            x: 'left',
            data: ['相关词', '相关词的相关词']
        },
        series: [{
            type: 'force',
            name: "相关性",
            ribbonType: false,
            categories: [
                {
                    name: '关键词'
                },
                {
                    name: '相关词'
                },
                {
                    name: '相关词的相关词'
                }
            ],
            itemStyle: {
                normal: {
                    label: {
                        show: true,
                        textStyle: {
                            color: '#333'
                        }
                    },
                    nodeStyle: {
                        brushType: 'both',
                        borderColor: 'rgba(255,215,0,0.4)',
                        borderWidth: 1
                    },
                    linkStyle: {
                        type: 'curve'
                    }
                },
                emphasis: {
                    label: {show: false},
                    nodeStyle: {},
                    linkStyle: {}
                }
            },
            useWorker: false,
            minRadius: 15,
            maxRadius: 25,
            gravity: 1.1,
            scaling: 1.1,
            roam: 'move',
            nodes: result.nodes,
            links: result.links
        }]
    };

}
/**
 * 设置河流图参数
 * @param result
 * @returns {option}
 */
function eventRiverOption(result) {
    var title = "";
    if (result["title"] != undefined)
        title = result["title"];
    var basic = {
        title: {
            text: title,
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            enterable: true
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        xAxis: [
            {
                type: 'time',
                boundaryGap: [0, 0] //不需要额外比例扩展，完全按照实际数据显示
            }
        ],
        series: [
            {
                name: "",
                type: "eventRiver",
                tooltip: {//不显示提示框
                    show: false
                },
                itemStyle: {//设置标签字体样式
                    normal: {
                        label: {
                            textStyle: {
                                color: "#000",
                                fontWeight: "bolder"
                            }
                        }
                    }
                },
                data: []
            }
        ]
    };
    var data = [];

    for (var i = 0; i < result['topics'].length; i++) {
        var rTopic = result['topics'][i];
        var topic = {
            name: rTopic['topicWords'],
            weight: rTopic['topicCount'],
            evolution: []
        }

        var details = rTopic['evolution'];
        for (var j = 0; j < details.length; j++) {
            var detail = details[j];
            for (var day in detail) {
                topic.evolution.push({
                    time: day,
                    value: detail[day]
                });
            }
        }


        data.push(topic);
    }

    basic.series[0].data = data;

    return basic;
}