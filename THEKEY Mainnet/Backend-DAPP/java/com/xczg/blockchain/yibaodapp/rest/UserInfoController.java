package com.xczg.blockchain.yibaodapp.rest;

import com.google.gson.Gson;
import com.xczg.blockchain.yibaodapp.bean.CustInfo;
import com.xczg.blockchain.yibaodapp.bean.CustOfQueryCodition;
import com.xczg.blockchain.yibaodapp.bean.EnterPriseOfQueryCodition;
import com.xczg.blockchain.yibaodapp.bean.EnterpriseInfo;
import com.xczg.blockchain.yibaodapp.bean.PageList;
import com.xczg.blockchain.yibaodapp.bean.TblSysUser;
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;
import com.xczg.blockchain.yibaodapp.service.ITransactionInfoService;
import com.xczg.blockchain.yibaodapp.service.IUserCenterService;
import com.xczg.blockchain.yibaodapp.util.ChainUtil;
import com.xczg.blockchain.yibaodapp.util.DateUtil;
import com.xczg.blockchain.yibaodapp.util.EncryptUtil;
import com.xczg.blockchain.yibaodapp.util.StringUtil;
import com.xczg.blockchain.yibaodapp.util.UUIDUtil;


import java.util.HashMap;

import java.util.List;
import java.util.Map;  


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("InfoController")
public class UserInfoController {
	
	@Autowired
	HttpServletRequest request;
	private final Logger logger = LoggerFactory.getLogger(NoticeController.class);
	@Autowired
	private IUserCenterService userCenterServiceImpl;
	private String useraddress="APycLdMX4Jjfcmg9Bj925TJsn7ExUFuPvb";
	private String url="http://222.128.14.106:3226";
	@Autowired
	private ITransactionInfoService transactionInfoService;
	/**
	 * 从数据库获取一个custInfo对象
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCustInfoById",method=RequestMethod.POST)
	public String getCustInfoById(
			@RequestParam(value = "id",required = true) String id,
			@RequestParam(value = "token",required = false)   String token){
		CustInfo cust=new CustInfo();
		cust=userCenterServiceImpl.getCustInfoById(id);
		Gson gson=new Gson();
		String json=gson.toJson(cust);
		return json;
	}

	/**
	 * 登录
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(
			@RequestParam(value = "mobile",required = true) String mobile,
			@RequestParam(value = "authCode",required = true) String authCode
			){
		boolean isOK=true;
		String msg="";
		String token="";
		String userId="";
		// 验证验证码
		if ( "8888".equals(authCode) ) {
			CustInfo cust=userCenterServiceImpl.getCustInfoByMobile(mobile);
			if ( cust == null ) {
				isOK = false;
				msg="账号不存在";
			} else {
				request.getSession().setAttribute("current_user_id", cust.getId());
				request.getSession().setAttribute("current_user_name", cust.getCust_name());
				request.getSession().setAttribute("current_user_mobile", cust.getMobile());
			   //  返回sessionID == token 
				token=request.getSession().getId();
				userId=cust.getId();
			}
		} else {
			isOK = false;
			msg="验证码错误";
		}
		
		// 将Token更新到用户表
		if ( isOK ) {
			userCenterServiceImpl.updateCustInfoLoginToken(token, userId);
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append(",\"token\":\"").append(token).append("\"}");
		return reStr.toString();
	}
	
	/**
	 * 登录验证
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/enterpriseLogin")
	public Map<String, String> enterpriseLogin(HttpServletRequest request, HttpSession httpSession) throws Exception 
	{
		String logincode = request.getParameter("account");
		String enterprisetype = request.getParameter("enterprisetype");
		String password = request.getParameter("password");
		EnterpriseInfo user = userCenterServiceImpl.getEnterpriseInfofoByLoginCodeAndType(logincode, enterprisetype);
		Map<String,String> result = new HashMap<String,String>();
		if(user !=null && password.equals(user.getPassword())) {
			result.put("status", "true");
			result.put("msg", "登陆成功");
			result.put("type",enterprisetype);
			request.getSession().setAttribute("current_user_id", user.getId());
			request.getSession().setAttribute("current_user_name", user.getEnterprise_name());
			return result;
		}else {
			result.put("status", "false");
			result.put("msg", "密码错误！");
			return result;
		}	
	}

	/**
	 * 从数据库获取所有custInfo对象
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCustInfoAll")
	public String getCustInfoAll(
			@RequestParam(value = "token",required = false)   String token){
		List<CustInfo> date = userCenterServiceImpl.getCustInfoAll();
		StringBuffer cuStr=new StringBuffer();
		int x=0;
		for(CustInfo cust:date){
			if(x>0)cuStr.append(",");
			cuStr.append(cust.toJson());
			x++;
		}
		int count = userCenterServiceImpl.getCustInfoListCount();
		StringBuffer lcuStrl = new StringBuffer();
		lcuStrl.append("{\"length\":").append(count).append(",").append("\"data\":[").append(cuStr).append("]}");
		return lcuStrl.toString();
	}

	/**
	 * 条件查询用户信息
	 */
	@ResponseBody
	@RequestMapping("/queryCustInfoOfCodition")
	public String queryCustInfoOfCodition(HttpServletRequest request) {
		String sEcho = request.getParameter("sEcho");
		String DisplayStart = request.getParameter("iDisplayStart");
		int iDisplayStart =Integer.parseInt(DisplayStart);
		String DisplayLength = request.getParameter("iDisplayLength");
		int iDisplayLength =Integer.parseInt(DisplayLength);
		String orderAz=getOrderColAZ(request);		
		String OrderName= getOrderColName(request);
		String qoperatorname = request.getParameter("q_operatorname");
		String qoperatoridcard = request.getParameter("q_operatoridcard");
		String qoperatormobile = request.getParameter("q_operatormobile");
		String qbankaccount = request.getParameter("q_bankaccount");
		String qlicenseno = request.getParameter("q_licenseno");
		String qstatus = request.getParameter("q_status");

		CustOfQueryCodition Codition = new  CustOfQueryCodition(sEcho,iDisplayStart,iDisplayLength,orderAz,OrderName,qoperatorname,qoperatoridcard,qoperatormobile,qbankaccount,qlicenseno,qstatus);
		int total=userCenterServiceImpl.getCustInfoPageListOfCount(Codition);
		List<CustInfo> date=userCenterServiceImpl.getCustInfoPageListOfQuery(Codition);

		StringBuffer cuStr=new StringBuffer();
		int x=0;
		for(CustInfo cust:date){
			if(x>0)cuStr.append(",");
			cuStr.append(cust.toJson());
			x++;
		}
		StringBuffer reStr=new StringBuffer();
		reStr.append("{\"code\":1,\"sEcho\":").append(sEcho).append(",\"iTotalRecords\":").append(total);
		reStr.append(",\"iTotalDisplayRecords\":").append(total).append(",\"aaData\":[");
		reStr.append(cuStr);
		reStr.append("]}");
		return reStr.toString();	
	}

	/**
	 * 条件查询企业信息
	 */
	@ResponseBody
	@RequestMapping("/queryEnterprisementInfoOfCodition")
	public String queryEnterprisementInfoOfCodition(HttpServletRequest request){
		String sEcho = request.getParameter("sEcho");
		String DisplayStart = request.getParameter("iDisplayStart");
		int iDisplayStart =Integer.parseInt(DisplayStart);
		String DisplayLength = request.getParameter("iDisplayLength");
		int iDisplayLength =Integer.parseInt(DisplayLength);
		String orderAz=getOrderColAZ(request);		
		String OrderName= getOrderColName(request);
		String qenterprisename = request.getParameter("enterprisename");
		String qoperatorname = request.getParameter("operatorname");
		String qoperatoridcard = request.getParameter("operatoridcard");
		String qoperatormobile = request.getParameter("operatormobile");
		String qlicenseno = request.getParameter("licenseno");
		String qstatus = request.getParameter("status");

		EnterPriseOfQueryCodition Codition =new EnterPriseOfQueryCodition(sEcho,iDisplayStart,iDisplayLength,orderAz,OrderName,qenterprisename, qoperatorname,qoperatoridcard,qoperatormobile,qlicenseno,qstatus) ;

		int total=userCenterServiceImpl.getEnterpriseInfoPageListOfCount(Codition);
		List<EnterpriseInfo> date=userCenterServiceImpl.getEnterpriseInfoPageListOfQuery(Codition);

		StringBuffer cuStr=new StringBuffer();
		int x=0;
		for(EnterpriseInfo cust:date){
			if(x>0)cuStr.append(",");
			cuStr.append(cust.toJson());
			x++;
		}
		StringBuffer reStr=new StringBuffer();
		reStr.append("{\"code\":1,\"sEcho\":").append(sEcho).append(",\"iTotalRecords\":").append(total);
		reStr.append(",\"iTotalDisplayRecords\":").append(total).append(",\"aaData\":[");
		reStr.append(cuStr);
		reStr.append("]}");
		return reStr.toString();			

	}

	/**
	 * 从数据库获取一个EnterpriseInfo对象
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getEnterpriseInfoById")
	public EnterpriseInfo getEnterpriseInfoById(@RequestParam(value = "id",required = true) String id){
		EnterpriseInfo enter =new EnterpriseInfo();
		enter=userCenterServiceImpl.getEnterpriseInfofoById(id);
		return enter;
	}

	/**
	 * 分页查询个人客户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCustInfoPageList")
	@ResponseBody
	public String getCustInfoPageList(HttpServletRequest request){
		PageList page=new PageList();
		String sEcho = request.getParameter("sEcho");
		String iDisplayStart = request.getParameter("iDisplayStart");
		String iDisplayLength = request.getParameter("iDisplayLength");	
		String orderAz=getOrderColAZ(request);		
		String OrderName= getOrderColName(request);
		page.setiDisplayStart(Integer.parseInt(iDisplayStart));
		page.setiDisplayLength(Integer.parseInt(iDisplayLength));
		page.setOrderName(OrderName);
		page.setOrderAZ(orderAz);
		int total=userCenterServiceImpl.getCustInfoListCount();
		List<CustInfo> date=userCenterServiceImpl.getCustInfoPageList(page);
		StringBuffer cuStr=new StringBuffer();
		int x=0;
		for(CustInfo cust:date){
			if(x>0)cuStr.append(",");
			cuStr.append(cust.toJson());
			x++;
		}
		StringBuffer reStr=new StringBuffer();
		reStr.append("{\"code\":1,\"sEcho\":").append(sEcho).append(",\"iTotalRecords\":").append(total);
		reStr.append(",\"iTotalDisplayRecords\":").append(total).append(",\"aaData\":[");
		reStr.append(cuStr);
		reStr.append("]}");
		System.out.println(reStr.toString());
		return reStr.toString();
	}

	/**
	 * 分页查询企业信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/getEnterpriseInfoPageList")
	@ResponseBody
	public String getEnterpriseInfoPageList(HttpServletRequest request) {
		PageList page=new PageList();
		String sEcho = request.getParameter("sEcho");
		String iDisplayStart = request.getParameter("iDisplayStart");
		String iDisplayLength = request.getParameter("iDisplayLength");	
		String orderAz=getOrderColAZ(request);		
		String OrderName= getOrderColName(request);
		page.setiDisplayStart(Integer.parseInt(iDisplayStart));
		page.setiDisplayLength(Integer.parseInt(iDisplayLength));
		page.setOrderName(OrderName);
		page.setOrderAZ(orderAz);
		int total=userCenterServiceImpl.getEnterpriseInfoListCount();
		List<EnterpriseInfo> date=userCenterServiceImpl.getEnterpriseInfoPageList(page);
		StringBuffer enStr=new StringBuffer();
		int x=0;
		for(EnterpriseInfo enter:date){
			if(x>0)enStr.append(",");
			enStr.append(enter.toJson());
			x++;
		}
		StringBuffer reStr=new StringBuffer();
		reStr.append("{\"code\":1,\"sEcho\":").append(sEcho).append(",\"iTotalRecords\":").append(total);
		reStr.append(",\"iTotalDisplayRecords\":").append(total).append(",\"aaData\":[");
		reStr.append(enStr);
		reStr.append("]}");
		System.out.println(reStr.toString());
		return reStr.toString();
	}

	/**
	 * 查询全部企业信息
	 *
	 * @return
	 */
	@RequestMapping("/getEnterpriseInfoAll")
	@ResponseBody
	public String getEnterpriseInfoAll() {

		List<EnterpriseInfo> date=userCenterServiceImpl.getEnterpriseInfoAll();
		int count = userCenterServiceImpl.getEnterpriseInfoListCount();
		StringBuffer enStr=new StringBuffer();
		int x=0;
		for(EnterpriseInfo enter:date){
			if(x>0)enStr.append(",");
			enStr.append(enter.toJson());
			x++;
		}
		StringBuffer leuStrl = new StringBuffer();
		leuStrl.append("{\"length\":").append(count).append(",").append("\"data\":[").append(enStr).append("]}");
		return leuStrl.toString();

	}

	/**
	 * 从前台获得一个CustInfo对象信息并添加到数据库
	 * @param cust_name
	 * @param id_card
	 * @param social_card_id
	 * @param mobile
	 * @param sex
	 * @param pass_word
	 * @param avatar_img
	 * @param id_card_postive
	 * @param id_card_negative
	 * @param status
	 * @param memo
	 * @param birth_date
	 * @return
	 */
	@RequestMapping("/addCustInfo")
	@ResponseBody
	public String addCustInfo(
			@RequestParam(value="custname",required = false) String cust_name ,
			@RequestParam(value = "idcard",required = false) String id_card,
			@RequestParam(value = "socialcardid",required = false) String social_card_id,
			@RequestParam(value = "mobile",required = true) String mobile,
			@RequestParam(value = "sex",required = false) String sex,
			@RequestParam(value = "password",required = true) String pass_word,
			@RequestParam(value = "avatarimg",required = false) String avatar_img,
			@RequestParam(value = "idcardpostive",required = false) String id_card_postive,
			@RequestParam(value = "idcardnegative",required = false) String id_card_negative,
			@RequestParam(value = "status",required = false) String status,
			@RequestParam(value = "memo",required = false) String memo,
			@RequestParam(value = "birthdate",required = false) String birth_date
			){
		
		StringBuffer reStr = new StringBuffer();
		CustInfo cust=userCenterServiceImpl.getCustInfoByMobile(mobile);
		if ( cust != null ) {
			reStr.append("{\"status\":").append(false).append(",\"msg\":\"该账号已经注册\"}");
			return reStr.toString();
		}
		
		CustInfo custInfo = new CustInfo();
		String id=UUIDUtil.getUUID();
		custInfo.setId(id);
		custInfo.setCust_name(cust_name);
		custInfo.setId_card(id_card);
		custInfo.setSocial_card_id(social_card_id);
		custInfo.setMobile(mobile);
		custInfo.setSex(sex);
		custInfo.setPass_word(pass_word);
		custInfo.setAvatar_img(avatar_img);
		custInfo.setId_card_positive((id_card_postive==null||"".equals(id_card_postive))?" ":id_card_postive);
		custInfo.setId_card_negative((id_card_negative==null||"".equals(id_card_negative))?" ":id_card_negative);
		custInfo.setStatus("1");
		custInfo.setMemo(memo);
		custInfo.setBirth_date( DateUtil.toTimestamp(birth_date));
		custInfo.setIs_deleted(0);
		custInfo.setChain_key(UUIDUtil.getUUID());
		custInfo.setTky_balance(50);// 默认50 TKY
		boolean isOK=true;
		try {
			userCenterServiceImpl.insertCustInfo(custInfo);
			String result=ChainUtil.tranfer1(useraddress, "50", url);
			
				String serialNo = transactionInfoService.getSerialNo();
				TblTransactionInfo entity = new TblTransactionInfo(
						result,"THEKEY", "转账合约", useraddress, useraddress,
						"50", DateUtil.getNow(), DateUtil.getNow(), serialNo, "用户余额");
				transactionInfoService.save(entity);
		}catch(Exception e) {
			e.printStackTrace();
			isOK=false;
		}
		reStr.append("{\"status\":").append(isOK).append(",\"msg\":\"\"}");
		return reStr.toString();
	}

	/**
	 * 删除一个CustInfo对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deleteCustInfo",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Boolean> deleteCustInfo(@RequestParam(value = "id" ,required = true)String id){
		userCenterServiceImpl.deleteCustInfo(id);
		Map<String, Boolean>map=new HashMap<String, Boolean>();
		map.put("status", true);
		return map;
	}

	/**
	 * 更新一个CustInfo对象
	 * @param id
	 * @param cust_name
	 * @param id_card
	 * @param social_card_id
	 * @param mobile
	 * @param sex
	 * @param pass_word
	 * @param avatar_img
	 * @param id_card_postive
	 * @param id_card_negative
	 * @param status
	 * @param memo
	 * @param birth_date
	 * @return
	 */
	@RequestMapping("/updateCustInfo")
	public String updateCustInfo(
			@RequestParam(value = "id",required = true) String id,
			@RequestParam(value="custname",required = true) String cust_name ,
			@RequestParam(value = "idcard",required = true) String id_card,
			@RequestParam(value = "socialcardid",required = true) String social_card_id,
			@RequestParam(value = "mobile",required = true) String mobile,
			@RequestParam(value = "sex",required = true) String sex,
			@RequestParam(value = "avatarimg",required = false) String avatar_img,
			@RequestParam(value = "idcardpostive",required = false) String id_card_postive,
			@RequestParam(value = "idcardnegative",required = false) String id_card_negative,
			@RequestParam(value = "status",required = true) String status,
			@RequestParam(value = "memo",required = true) String memo,
			@RequestParam(value = "birthdate",required = false) String birth_date
			){
		CustInfo custInfo = new CustInfo();
		custInfo.setId(id);
		custInfo.setCust_name(cust_name);
		custInfo.setId_card(id_card);
		custInfo.setSocial_card_id(social_card_id);
		custInfo.setMobile(mobile);
		custInfo.setSex(sex);
		custInfo.setAvatar_img(avatar_img);
		custInfo.setId_card_positive(id_card_postive);
		custInfo.setId_card_negative(id_card_negative);
		custInfo.setStatus(status);
		custInfo.setMemo(memo);
		custInfo.setBirth_date( DateUtil.toTimestamp(birth_date) );
		userCenterServiceImpl.updateCustInfo(custInfo);
		return "";
	}

	/**
	 * 增加一个EnterpriseInfo对象
	 * @param enterprise_name
	 * @param license_no
	 * @param operator_name
	 * @param operator_id_card
	 * @param operator_mobile
	 * @param bank_account
	 * @param id_card_positive
	 * @param id_card_negative
	 * @param license_img
	 * @param enterprise_type
	 * @param addr
	 * @param memo
	 * @param privince
	 * @param city
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/addEnterpriseInfo")
	@ResponseBody
	public String addEnterpriseInfo(
			@RequestParam(value = "enterprisename",required =true )String enterpriseName ,
			@RequestParam(value = "licenseno",required = true)String licenseNo,
			@RequestParam(value = "operatorname",required = true)String operatorName,
			@RequestParam(value = "operatoridcard",required = false)String operatorIdcard ,
			@RequestParam(value = "operatormobile",required = false)String operatorMobile,
			@RequestParam(value = "bankaccount",required =false )String bankAccount,
			@RequestParam(value = "idcardpositive",required = false)String idcardPositive,
			@RequestParam(value = "idcardnegative",required =false )String idcardNegative,
			@RequestParam(value = "licenseimg",required = false)String licenseImg,
			@RequestParam(value = "enterprisetype",required = true)String enterpriseType,
			@RequestParam(value = "addr",required = false)String addr,
			@RequestParam(value = "memo",required =false )String memo,
			@RequestParam(value = "privince",required = false)String privince,
			@RequestParam(value = "city",required = false)String city,
			@RequestParam(value = "status",required = true)String status,
			@RequestParam(value = "logincode",required = true)String logincode,
			@RequestParam(value = "password",required = true)String password,
			RedirectAttributes ra){
		//fix me
		EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
		enterpriseInfo.setEnterprise_name(enterpriseName);
		enterpriseInfo.setLicense_no(licenseNo);
		enterpriseInfo.setOperator_name(operatorName);
		enterpriseInfo.setOperator_id_card(operatorIdcard);
		enterpriseInfo.setOperator_mobile(operatorMobile);
		enterpriseInfo.setBank_account(bankAccount);
		enterpriseInfo.setId_card_positive(idcardPositive);
		enterpriseInfo.setId_card_negative(idcardNegative);
		enterpriseInfo.setLicense_img(licenseImg);
		enterpriseInfo.setEnterprise_type(enterpriseType);
		enterpriseInfo.setAddr(addr);
		enterpriseInfo.setMemo(memo);
		enterpriseInfo.setPrivince(privince);
		enterpriseInfo.setCity(city);
		enterpriseInfo.setStatus(status);
		enterpriseInfo.setLogincode(logincode);
		enterpriseInfo.setPassword(password);
		if (  !StringUtil.isEmpty(password) ) {
			enterpriseInfo.setPassword( EncryptUtil.md5(password) );
		}
		//进行多维验证
		userCenterServiceImpl.insertEnterpriseInfo(enterpriseInfo);
		ra.addFlashAttribute("msg", "信息添加成功！！！");
		return "redirect:enterPriseInfo";
	}

	/**
	 * 删除一个EnterpriseInfo对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteEnterPriseInfo")
	@ResponseBody
	public Map<String,Boolean> deleEnterPriseInfo(@RequestParam(value = "id") String id,RedirectAttributes ra){
		userCenterServiceImpl.deleteEnterpriseInfo(id);
		Map<String,Boolean> map =new HashMap<>();
		map.put("status",true);
		return map;
	}

	/**
	 * 更改一个EnterpriseInfo对象
	 * @param id
	 * @param enterprise_name
	 * @param license_no
	 * @param operator_name
	 * @param operator_id_card
	 * @param operator_mobile
	 * @param bank_account
	 * @param id_card_positive
	 * @param id_card_negative
	 * @param license_img
	 * @param enterprise_type
	 * @param addr
	 * @param memo
	 * @param privince
	 * @param city
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateEnterPriseInfo")
	@ResponseBody
	public String updateEnterPriseInfo(
			@RequestParam(value = "id",required = true) String id,
			@RequestParam(value = "enterprisename",required =false )String enterprisename ,
			@RequestParam(value = "licenseno",required = false)String licenseno,
			@RequestParam(value = "operatorname",required = false)String operatorname,
			@RequestParam(value = "operatoridcard",required = false)String operatoridcard ,
			@RequestParam(value = "operatormobile",required = false)String operatormobile,
			@RequestParam(value = "bankaccount",required =false )String bankaccount,
			@RequestParam(value = "idcardpositive",required = false)String idcardpositive,
			@RequestParam(value = "idcardnegative",required =false )String idcardnegative,
			@RequestParam(value = "licenseimg",required = false)String licenseimg,
			@RequestParam(value = "enterprisetype",required = false)String enterprisetype,
			@RequestParam(value = "addr",required = false)String addr,
			@RequestParam(value = "memo",required =false )String memo,
			@RequestParam(value = "privince",required = false)String privince,
			@RequestParam(value = "city",required = false)String city,
			@RequestParam(value = "status",required = false)String status,
			@RequestParam(value = "logincode",required = true)String logincode,
			@RequestParam(value = "password",required = true)String password,
			RedirectAttributes ra){
		EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
		enterpriseInfo.setId(id);
		enterpriseInfo.setEnterprise_name(enterprisename);
		enterpriseInfo.setLicense_no(licenseno);
		enterpriseInfo.setOperator_name(operatorname);
		enterpriseInfo.setOperator_id_card(operatoridcard);
		enterpriseInfo.setOperator_mobile(operatormobile);
		enterpriseInfo.setBank_account(bankaccount);
		enterpriseInfo.setId_card_positive(idcardpositive);
		enterpriseInfo.setId_card_negative(idcardnegative);
		enterpriseInfo.setLicense_img(licenseimg);
		enterpriseInfo.setEnterprise_type(enterprisetype);
		enterpriseInfo.setAddr(addr);
		enterpriseInfo.setMemo(memo);
		enterpriseInfo.setPrivince(privince);
		enterpriseInfo.setCity(city);
		enterpriseInfo.setStatus(status);
		enterpriseInfo.setLogincode(logincode);
		if (  !StringUtil.isEmpty(password) ) {
			enterpriseInfo.setPassword( EncryptUtil.md5(password) );
		}
		userCenterServiceImpl.updateEnterpriseInfo(enterpriseInfo);
		ra.addFlashAttribute("msg", "信息更改成功！！！");
		return "redirect:enterPriseInfo";

	}
	/**
	 * 获取排序字段编码
	 * @param req
	 * @return
	 */
	public String getOrderColName(HttpServletRequest req) {
		String sOrder="";
		Map<String, String[]> map=req.getParameterMap();
		int sortIndex=0;
		try{
			if (map.containsKey("iSortCol_0")){
				sortIndex=Integer.parseInt( map.get("iSortCol_0")[0] );
				if ("false".equals(map.get("bSortable_"+sortIndex)[0])) return "";
				sOrder=map.get("mDataProp_"+sortIndex)[0];
				if (sOrder.toUpperCase().startsWith("ORDER BY"))  {
					sOrder = "";
				}else{
					sOrder=sOrder.replaceAll(" ","");
				}
			}
			if(sOrder.length()>25) sOrder="";
			map.remove("iSortCol_0");
		}catch(Exception e){
		}
		return sOrder;
	}

	/**
	 * 获取排序属性
	 * @param req
	 * @return
	 */
	public String getOrderColAZ(HttpServletRequest req){
		String sSortDir_0=req.getParameter("sSortDir_0");
		if (sSortDir_0==null) {
			sSortDir_0="";
		}else{
			sSortDir_0=sSortDir_0.replaceAll(" ","");
		}
		if (sSortDir_0.length()>10) {
			sSortDir_0="";
		}
		return sSortDir_0;
	}

}
