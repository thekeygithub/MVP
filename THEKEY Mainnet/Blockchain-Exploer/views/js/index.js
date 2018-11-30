//获取根路径
function getBasePath() {
	var obj = window.location;
	var contextPath = obj.pathname.split("/")[1];
	var basePath = obj.protocol + "//" + obj.host + "/" + contextPath;
	return basePath;
}
// 获取最新交易信息
var newestTra = {
	init : function(serialNo,type,txid, sender, receiver, sys_fee, time) {
		var lang = $('#language option:selected').val();
		$('#table2')
				.datagrid(
						{
							width : 'auto',
							singleSelect : true,
							method : 'get',
							border : false,
							fit : true,
							fitColumns : true,
							pagination : true,
							pageNumber : 0,
							scrollbarSize : 0,
							pageSize : 10,
							pageList : [ 5, 10, 15, 20, 50 ],
							rownumbers : false,
							url : getBasePath()
									+ '/pageFunction/getNewestTraInfo',
							columns : [ [
									{
										field : 'serialNo',
										title : serialNo,
										width : 80,
										align : 'center'
									},
									{
										field : 'txid',
										title : txid,
										width : 120,
										align : 'center',
										formatter : function(val, row, index) {
											return "<a href='pageFunction/toTraInfoPage?searchInfo="
													+ val
													+ "' > "
													+ val
													+ "</a>";
										}
									}, {
										field : 'sender',
										title : sender,
										width : 50,
										align : 'center'
									}, {
										field : 'receiver',
										title : receiver,
										width : 50,
										align : 'center'
									}, {
										field : 'fee',
										title : sys_fee,
										width : 40,
										align : 'center',
										formatter : function(val, row, index) {
											return val + " TKY";
										}
									}, {
										field : 'type',
										title : type,
										width : 50,
										align : 'center'
									}, {
										field : 'time',
										title : time,
										width : 60,
										align : 'center'
									} ] ],
							onLoadSuccess : function(data) {
								$("#traNum").text(data.total);
							},
						});
		if (lang == "en") {
			$('#table2').datagrid('getPager').pagination({// 分页栏下方文字显示
				afterPageText : 'Page    of {pages} ',
				displayMsg : 'Displaying {from} - {to} of {total} items',
			});
		} else {
			$('#table2').datagrid('getPager').pagination({// 分页栏下方文字显示
				afterPageText : '页    共 {pages} 页',
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
			});
		}

	}
}
// 展现交易信息，用于区块id查询界面
var index = {
	init : function(txid, sender, receiver, sys_fee, time) {
		var lang = $('#language option:selected').val();
		$('#table1').datagrid(
				{
					width : 'auto',
					singleSelect : true,
					method : 'get',
					border : false,
					fit : true,
					fitColumns : true,
					pagination : true,
					pageNumber : 0,
					scrollbarSize : 0,
					pageSize : 10,
					pageList : [ 5, 10, 15, 20, 50 ],
					rownumbers : false,
					columns : [ [
							{
								field : 'txid',
								title : txid,
								width : 200,
								align : 'center',
								formatter : function(val, row, index) {
									return "<a href='serchFunction?searchInfo="
											+ val + "' > " + val + "</a>";
								}
							}, {
								field : 'sender',
								title : sender,
								width : 50,
								align : 'center'
							}, {
								field : 'receiver',
								title : receiver,
								width : 50,
								align : 'center'
							}, {
								field : 'fee',
								title : sys_fee,
								width : 30,
								align : 'center',
								formatter : function(val, row, index) {
									return val + " TKY";
								}
							}, {
								field : 'time',
								title : time,
								width : 60,
								align : 'center'
							} ] ]
				});
		if (lang == "en") {
			$('#table1').datagrid('getPager').pagination({// 分页栏下方文字显示
				afterPageText : 'Page    of {pages} ',
				displayMsg : 'Displaying {from} - {to} of {total} items',
			});
		} else {
			$('#table1').datagrid('getPager').pagination({// 分页栏下方文字显示
				afterPageText : '页    共 {pages} 页',
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
			});
		}
	}
}
// 区块查询与交易查询时区块信息的展现
var loadSkills = function(url, searchInfo) {
	$.post(url, {
		searchInfo : searchInfo
	}, function(data) {
		if (data != null) {
			$("#info").text(searchInfo);
			$("#index").text(data.index);
			$("#hash").text(data.hash);
			$("#merkleroot").text(data.merkleroot);
			$("#hash").text(data.hash);
			var unixtime = data.time;
			var unixTimestamp = new Date(unixtime * 1000);
			commonTime = unixTimestamp.toLocaleString();
			$("#startTime").text(timestampToTime(data.time));
			$("#version").text("1.0");
			$("#size").text(data.size + "bit");
			$("#invocation").text(data.script.invocation);
			$("#verification").text(data.script.verification);
			$("#blockId").text(data.index);
			if (data.addInfo != undefined && data.addInfo != null) {
				var addInfo = data.addInfo;
				if (addInfo.length == 2) {
					$("#sender_addr1").text(addInfo[0].sender_addr);
					$("#receiver_addr1").text(addInfo[0].receiver_addr);
					$("#sender_addr2").text(addInfo[1].sender_addr);
					$("#receiver_addr2").text(addInfo[1].receiver_addr);
				} else {
					$("#sender_addr1").text(addInfo[0].sender_addr);
					$("#receiver_addr1").text(addInfo[0].receiver_addr);
				}
				var sender_addr = "";
				var receiver_addr = "";
				for (var i = 0; i < addInfo.length; i++) {
					sender_addr = sender_addr + addInfo[i].sender_addr + '\n';
					receiver_addr = receiver_addr + addInfo[i].receiver_addr
							+ '\n';
				}
				$("#sender_addr").text(sender_addr);
				$("#receiver_addr").text(receiver_addr);
			}
			var initData = data.tx;
			var txIds = "";
			for (var i = 0; i < initData.length; i++) {
				txIds += "'" + initData[i].txid + "',";
			}
			SearchTraInfo(txIds);

		}

	}, "json");
};
// 区块查询与交易查询时，根据id查询数据库，以此来确定区块的交易信息与交易数量
function SearchTraInfo(txid) {
	var url = getBasePath() + '/pageFunction/getTraInfoByID';
	$.post(url, {
		txid : txid
	}, function(data) {
		$("#traNum").text(data.length);
		$('#table1').datagrid('loadData', data);

	});
}

// 将unix时间戳转化为标准时间
function timestampToTime(timestamp) {
	var date = new Date(timestamp * 1000);// 时间戳为10位需*1000，时间戳为13位的话不需乘1000
	Y = date.getFullYear() + '-';
	M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date
			.getMonth() + 1)
			+ '-';
	D = date.getDate() + ' ';
	h = date.getHours() + ':';
	m = date.getMinutes() + ':';
	if (date.getMinutes() < 10) {
		m = '0' + m;
	}
	s = date.getSeconds();
	if (date.getSeconds() < 10) {
		s = '0' + s;
	}
	return Y + M + D + h + m + s;
}
// 跳转到最新交易页面
function toNewstInfoJsp() {
	window.location.href = getBasePath();
}
// 跳转到最新交易页面，由于有些页面的路径不同所以对应的跳转页面写了两个方法
function toNewstTraJsp() {
	window.location.href = getBasePath() + '/NewestTra';
}
// 跳转到审计页面
function toAuditRecord() {
	window.location.href = getBasePath() + '/auditRecord';
}

// 更换语言
function changeLanguage() {
	// 获取查询的值
	var lang = $('#language option:selected').val();
	$.post(getBasePath() + '/changeSessionLanauage', {
		lang : lang
	}, function() {
		window.location.reload();
	});

}
$.fn.serializeObject = function() {
	var object = {};
	var array = this.serializeArray();
	$.each(array, function() {
		if (object[this.name]) {
			if (!object[this.name].push) {
				object[this.name] = [ object[this.name] ];
			}
			object[this.name].push(this.value || '');
		} else {
			object[this.name] = this.value || '';
		}
	});
	return object;
}
// 审计页面查询功能
var auditRecord = {
	init : function(medicalInstitution, visitType, illegalType, illegalDrug,
			Diagnosis, Treatment, visitTime, sex, name, sfzh, project) {
		var lang = $('#language option:selected').val();
		$('#datagrid').datagrid({
			width : 'auto',
			singleSelect : true,
			method : 'post',
			border : false,
			fit : true,
			fitColumns : true,
			pagination : true,
			pageNumber : 0,
			scrollbarSize : 0,
			pageSize : 10,
			pageList : [ 5, 10, 15, 20, 50 ],
			rownumbers : false,
			url : getBasePath() + '/pageFunction/getAuditRecord',
			onLoadSuccess : function(data) {
				if (data = null) {
					return;
				}
			},
			columns : [ [ {
				field : 'medicalInstitution',
				title : medicalInstitution,
				width : 10,
				align : 'center'
			}, {
				field : 'treatType',
				title : visitType + "/" + project,
				width : 10,
				align : 'center'
			}, {
				field : 'illegalDrugs',
				title : illegalDrug,
				width : 15,
				align : 'center',
				formatter : function(val, row, index) {
					return row.illegalDrugs;
				}
			}, {
				field : 'illegalType',
				title : illegalType,
				width : 10,
				align : 'center'
			}, {
				field : 'sfzh',
				title : sfzh,
				width : 15,
				align : 'center',
				formatter : function(val, row, index) {
					var sub = val.substring(6, 14);
					val = val.replace(sub, "********");
					return val;
				}
			}, {
				field : 'sex',
				title : sex,
				width : 5,
				align : 'center'
			}, {
				field : 'name',
				title : name,
				width : 5,
				align : 'center'
			}, {
				field : 'diagnosis',
				title : Diagnosis + "/" + Treatment,
				width : 10,
				align : 'center'
			}, {
				field : 'treatmentDate',
				title : visitTime,
				width : 15,
				align : 'center'
			}] ]
		});

		if (lang == "en") {
			$('#datagrid').datagrid('getPager').pagination({// 分页栏下方文字显示
				afterPageText : 'Page    of {pages} ',
				displayMsg : 'Displaying {from} - {to} of {total} items',
			});
		} else {
			$('#datagrid').datagrid('getPager').pagination({// 分页栏下方文字显示
				afterPageText : '页    共 {pages} 页',
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
			});
		}
	}

}
formatterDate = function(date) {
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
			+ (date.getMonth() + 1);
	var hor = date.getHours();
	var min = date.getMinutes();
	var sec = date.getSeconds();
	return date.getFullYear() + '-' + month + '-' + day + " " + hor + ":" + min
			+ ":" + sec;
};

$('#startTime1').datetimebox('setValue', formatterDate(new Date()));
function searchAudit() {
	if ($('#searchForm').form('validate')) {
		var startTime = $('#startTime').datetimebox("getValue");
		var endTime = $('#endTime').datetimebox("getValue");
		if (endTime < startTime) {
			$.messager.alert("操作提示", "结束时间不能小于开始时间", error);
			return;
		}

		$('#datagrid').datagrid("load", $('#searchForm').serializeObject());
	}

}