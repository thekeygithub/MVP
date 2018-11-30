var loginMode="";
var caseParam=null;
var provinceList=["北京市","天津市","上海市","重庆市","河北省","山西省","内蒙古","辽宁省","吉林省","黑龙江省","江苏省","浙江省","安徽省","福建省","江西省","山东省","河南省","湖北省","湖南省","广东省","广西省","海南省","四川省","贵州省","云南省","西藏","陕西省","甘肃省","青海省","宁夏","新疆","香港","澳门","台湾"];

var indexdata = [];

function index_init(){}

/**
 * 初始化项目个性化全局缓存数据
 */
function proInitGlobalData(){
	g_dictionary.put("EDIT_DIC_TYPEDEPT_TYPE", new Code("DEPT_TYPE","EDIT_DIC_TYPE","部门属性","1"));
	g_dictionary.put("EDIT_DIC_TYPEJOB_LEVEL", new Code("JOB_LEVEL","EDIT_DIC_TYPE","职级","1"));
	g_dictionary.put("EDIT_DIC_TYPESYS_POSITION", new Code("SYS_POSITION","EDIT_DIC_TYPE","职位","1"));
	g_dictionary.put("EDIT_DIC_TYPECHECK_RESULT", new Code("CHECK_RESULT","EDIT_DIC_TYPE","审核结果","1"));
	g_dictionary.put("EDIT_DIC_TYPEREMIND_TYPE", new Code("REMIND_TYPE","EDIT_DIC_TYPE","工作提醒类型","1"));
	g_dictionary.put("EDIT_DIC_TYPESEX", new Code("SEX","EDIT_DIC_TYPE","性别","1"));
	g_dictionary.put("EDIT_DIC_TYPEIS_HAD", new Code("IS_HAD","EDIT_DIC_TYPE","有无","1"));
	g_dictionary.put("EDIT_DIC_TYPEPROJECT_FILE_TYPE", new Code("PROJECT_FILE_TYPE","EDIT_DIC_TYPE","附件类别","1"));
    g_dictionary.put("EDIT_DIC_TYPEEDUCATION", new Code("EDUCATION","EDIT_DIC_TYPE","学历","1"));
	g_dictionary.put("EDIT_DIC_TYPEAPPLY_STATUS", new Code("APPLY_STATUS","EDIT_DIC_TYPE","受理状态","1"));
	g_dictionary.put("EDIT_DIC_TYPERETURN_STATUS", new Code("RETURN_STATUS","EDIT_DIC_TYPE","退费到账状态","1"));
	g_dictionary.put("EDIT_DIC_TYPEVERIFICATION_STATUS", new Code("VERIFICATION_STATUS","EDIT_DIC_TYPE","核销状态","1"));
	g_dictionary.put("EDIT_DIC_TYPECLOSE_TYPE", new Code("CLOSE_TYPE","EDIT_DIC_TYPE","结案方式","1"));
	g_dictionary.put("EDIT_DIC_TYPERETURN_APPLY_STATUS", new Code("RETURN_APPLY_STATUS","EDIT_DIC_TYPE","退费受理状态","1"));
	g_dictionary.put("EDIT_DIC_TYPEPROGRESS", new Code("PROGRESS","EDIT_DIC_TYPE","进度","1"));
	g_dictionary.put("EDIT_DIC_TYPEMERCHANT_TYPE", new Code("MERCHANT_TYPE","EDIT_DIC_TYPE","商家类型","1"));
	g_dictionary.put("EDIT_DIC_TYPECONFIRM_MERCHANT_TYPE", new Code("CONFIRM_MERCHANT_TYPE","EDIT_DIC_TYPE","小米确认的商家类型","1"));
	g_dictionary.put("EDIT_DIC_TYPEUNDERTAKEINFO_STATUS", new Code("UNDERTAKEINFO_STATUS","EDIT_DIC_TYPE","服务商状态","1"));
	g_dictionary.put("EDIT_DIC_TYPESURVEY_RESULT", new Code("SURVEY_RESULT","EDIT_DIC_TYPE","调查情况","1","03"));
	g_dictionary.put("EDIT_DIC_TYPETORT_TYPE", new Code("TORT_TYPE","EDIT_DIC_TYPE","侵权方式","1","03"));
	g_dictionary.put("EDIT_DIC_TYPELAWYER_STATUS", new Code("LAWYER_STATUS","EDIT_DIC_TYPE","律师状态","1","03"));
	g_dictionary.put("EDIT_DIC_TYPEMATERIAL_TYPE", new Code("MATERIAL_TYPE","EDIT_DIC_TYPE","盖章材料类型","1"));
	g_dictionary.put("EDIT_DIC_TYPEMERCHANT_LEVEL", new Code("MERCHANT_LEVEL","EDIT_DIC_TYPE","商户级别","1"));
	g_dictionary.put("EDIT_DIC_TYPELETTER_STATUS", new Code("LETTER_STATUS","EDIT_DIC_TYPE","发函状态","1"));
	g_dictionary.put("EDIT_DIC_TYPECLEARING_STATUS", new Code("CLEARING_STATUS","EDIT_DIC_TYPE","结算状态","1"));
	g_dictionary.put("EDIT_DIC_TYPEIS_REPETITION", new Code("IS_REPETITION","EDIT_DIC_TYPE","是否重复","1"));
	g_dictionary.put("EDIT_DIC_TYPEFEE_OBJECT_TYPE", new Code("FEE_OBJECT_TYPE","EDIT_DIC_TYPE","结算方","1"));
	g_dictionary.put("EDIT_DIC_TYPEFEE_TYPE", new Code("FEE_TYPE","EDIT_DIC_TYPE","结算费类型","1"));
    g_dictionary.put("SEX1",new Code("SEX","男 "));
    g_dictionary.put("SEX0",new Code("SEX","女 "));
	if ( loginType == 'undertake' ) {
		    try{
			  loadDeptTree();
			}catch(e){}
		    loadCodes();
	}
}

  function getDeptListByType(sid,type,hasEmpty){
	 var ds=getDeptData();
	 var ht='<select id="'+sid+'">';
	 if ( hasEmpty ) {
		 ht+='<option value=""></option>';
	 }
	 if ( ds ) {
		 for ( var i=0;i<ds.length;i++ ) {
			 if ( ds[i].dept_type == type ) {
				   ht+='<option value="'+ds[i].deptId+'">'+ds[i].name+'</option>';
			 }
		 }
	 }
	 ht+='</select>';
	 return ht;
 }

 function getProvinceList(sid,initValue,onchange){
    var ht='<select id="'+sid+'" name="'+sid+'" ';
	if ( onchange != '' ) {
		ht+=' onchange="'+onchange+'" ';
	}
    ht+='><option value=""></option>';
	for (var i=0;i<provinceList.length;i++) {
	   ht+='<option value="'+provinceList[i]+'" ';
	   if ( provinceList[i] == initValue ) ht+=' selected ';
	   ht+='>'+provinceList[i]+'</option>';
	}
	ht+='</select>';
	return ht;
 }
 
 function setCaseQueryParam(ao){
	caseParam=ao;
}

function getCaseQueryParam(){
	return caseParam;
}