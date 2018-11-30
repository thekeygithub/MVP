package com.xczg.blockchain.yibaodapp.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.xczg.blockchain.yibaodapp.bean.CustInfo;
import com.xczg.blockchain.yibaodapp.bean.LoginUser;
import com.xczg.blockchain.yibaodapp.bean.PageList;
import com.xczg.blockchain.yibaodapp.bean.SysUserOfCodition;
import com.xczg.blockchain.yibaodapp.bean.TblSysUser;
import com.xczg.blockchain.yibaodapp.service.ITblSysUserService;
import com.xczg.blockchain.yibaodapp.util.MD5Util;


@Controller
@RequestMapping("sys")
public class SysUserController {
	@Autowired
	private ITblSysUserService sysUserService;
	
	
	/**
	 * 主页登录验证
	 */
	@ResponseBody
	@RequestMapping("/isLogin")
	public Map<String,String> isLogin( HttpServletRequest request, HttpSession httpSession) throws Exception{
		String user = (String)httpSession.getAttribute("current_user_name");
		System.out.println("当前登陆的用户："+user);
		Map<String,String> map =new HashMap<String, String>();
		if(user== null) {
			map.put("responseResult", "false");
		}else {
			map.put("responseResult", "true");
			map.put("account", user);
		}
		return map;
	}
	
	/**
	 * 条件查询管理人员信息
	 */
	@ResponseBody
	@RequestMapping("/querysysuserInfoOfCodition")
	public String querysysuserInfoOfCodition(HttpServletRequest request){
		String sEcho = request.getParameter("sEcho");
		String DisplayStart = request.getParameter("iDisplayStart");
		int iDisplayStart =Integer.parseInt(DisplayStart);
		String DisplayLength = request.getParameter("iDisplayLength");
		int iDisplayLength =Integer.parseInt(DisplayLength);
		String orderAz=getOrderColAZ(request);
		String OrderName= getOrderColName(request);
		String qusername = request.getParameter("q_username");
		String qsex = request.getParameter("q_sex");
		String qemail = request.getParameter("q_email");
		String qmobile = request.getParameter("q_mobile");
		String qjobtitle = request.getParameter("q_jobtitle");
		String qofficephone = request.getParameter("q_officephone");
		String qjoblever = request.getParameter("q_joblever");
		String qstatus = request.getParameter("q_status");
        
		SysUserOfCodition Codition = new SysUserOfCodition(sEcho,iDisplayStart,iDisplayLength,orderAz,OrderName, qusername,qsex,qemail,qmobile,qjobtitle,qofficephone,qjoblever,qstatus);
		System.out.println(Codition.getOrderAz());
		int total = sysUserService.getTblSysUserPageListOfCount(Codition);
		List<TblSysUser> date = sysUserService.getTblSysUserPageListOfQuery(Codition);
		System.out.println(date.size());
		StringBuffer  cuStr =new StringBuffer();
		int x=0;
		for(TblSysUser cust:date){
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
	 * 用户退出
	 */
	@ResponseBody
	@RequestMapping("/isLogout")
	public String isLogout( HttpServletRequest request, HttpSession httpSession) throws Exception{
		HttpSession sessionId = request.getSession();
		sessionId.setAttribute("current_user_name", null);
		return "isok";
	}
	
	/**
	 * 登录验证
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/checkLogin")
	public Map<String, String> login(HttpServletRequest request, HttpSession httpSession) throws Exception 
	{
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		TblSysUser user = sysUserService.getTblSysUserByAccount(account);
		HttpSession sessionId = request.getSession();
		sessionId.setAttribute("account", account);
		Map<String,String> result = new HashMap<String,String>();
		if(user !=null && password.equals(user.getPassword())) {
			result.put("status", "true");
			result.put("msg", "登陆成功");
			result.put("type",Integer.toString(user.getSys_type()));
			request.getSession().setAttribute("current_user_id", account);
			request.getSession().setAttribute("current_user_name", user.getUsername());
			request.getSession().setAttribute("current_user_mobile", user.getMobile());
			return result;
		}else {
			result.put("status", "false");
			result.put("msg", "密码错误！");
			return result;
		}	
	}
	
	

	/**
	 * 修改密码
	 * @param request
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/changePwd")
	public Map<String, Boolean> changePwd(HttpServletRequest request, HttpSession httpSession) throws Exception{
		String sessionId = httpSession.getId();
		String account = (String)httpSession.getAttribute("account");	
		Map<String,Boolean> res = new HashMap<>();
		 String password = request.getParameter("user.password");
		 String newpass = request.getParameter("newpass");
		int checkCount = sysUserService.checkPwd(new LoginUser(account,password));
		if(checkCount==1) {
			Map<String,String> newMaps= new HashMap<>();
			newMaps.put("account",account);
			newMaps.put("newPwd",newpass);
			int changeResult = sysUserService.changePwd(newMaps);
			if(changeResult==1) {
				res.put("status",true);
				return res;
			}else {
				res.put("status",false);
				return res;
			}
		}else {
			res.put("status",false);
			return res;
		}
		
		
	}
	/**
	 * 分页查询系统管理员信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUserInfoPageList")
 	@ResponseBody
	public String getSysUserInfoPageList(HttpServletRequest request) {
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
 		int total=sysUserService.getTblSysUserCount();
 		List<TblSysUser>list=sysUserService.getTblSysUserPageList(page);
 		StringBuffer cuStr=new StringBuffer();
 		int x=0;
 		for(TblSysUser user:list){
 			if(x>0)cuStr.append(",");
 			cuStr.append(user.toJson());
 			x++;
 		}
 		StringBuffer reStr=new StringBuffer();
		reStr.append("{\"code\":1,\"sEcho\":").append(sEcho).append(",\"iTotalRecords\":").append(total);
		reStr.append(",\"iTotalDisplayRecords\":").append(total).append(",\"aaData\":[");
		reStr.append(cuStr);
		reStr.append("]}");
		System.out.println(reStr);
		return reStr.toString();
	}
	/**
	 * 添加管理员信息
	 * @param account
	 * @param username
	 * @param password
	 * @param status
	 * @param lastlogin
	 * @param email
	 * @param mobile
	 * @param officephone
	 * @param entrydate
	 * @param jobtitle
	 * @param sex
	 * @param joblevel
	 * @param memo
	 * @param birthday
	 * @param avatarpath
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/insertTblSysUser")
 	@ResponseBody
	public Map<String, Boolean> insertTblSysUser(
			@RequestParam(value="account",required = true) String account ,
			@RequestParam(value="username",required = true) String username ,
			@RequestParam(value="password",required = true) String password ,
			@RequestParam(value="status",required = true) String status ,
			@RequestParam(value="lastlogin",required = false) String lastlogin ,
			@RequestParam(value="email",required = true) String email ,
			@RequestParam(value="mobile",required = true) String mobile ,
			@RequestParam(value="officephone",required = true) String officephone ,
			@RequestParam(value="entrydate",required = false) String entrydate ,
			@RequestParam(value="jobtitle",required = true) String jobtitle ,
			@RequestParam(value="sex",required = true) String sex ,
			@RequestParam(value="joblevel",required = true) String joblevel ,
			@RequestParam(value="memo",required = true) String memo ,
			@RequestParam(value="birthday",required = true) String birthday ,
			@RequestParam(value="avatarpath",required = false) String avatarpath 
			) throws Exception 
	{
		TblSysUser user=new TblSysUser(account,username,password,status,lastlogin,email,mobile,officephone, entrydate,jobtitle,sex,  joblevel,
				 memo,  birthday,  avatarpath);
		int i=sysUserService.insertTblSysUser(user);
		Map<String, Boolean>map=new HashMap<>();
		if(i>0) {
			map.put("status", true);
		}
		return map;
	}
	/**
	 * 删除管理员账户
	 * @param account
	 * @return
	 */
	@RequestMapping("/deleteTblSysUser")
 	@ResponseBody
	public Map<String, Boolean> deleteTblSysUser(@RequestParam(value="account",required = true) String account){
		int i=sysUserService.deleteTblSysUser(account);
		Map<String, Boolean>map=new HashMap<>();
		if(i>0) {
			map.put("status", true);
		}
		return map;
	}
	/**
	 * 修改信息
	 * @param account
	 * @param username
	 * @param status
	 * @param lastlogin
	 * @param email
	 * @param mobile
	 * @param officephone
	 * @param entrydate
	 * @param jobtitle
	 * @param sex
	 * @param joblevel
	 * @param memo
	 * @param birthday
	 * @param avatarpath
	 * @return
	 */
	@RequestMapping("/updateTblSysUser")
 	@ResponseBody
	public Map<String, Boolean> updateTblSysUser(
			@RequestParam(value="account",required = false) String account ,
			@RequestParam(value="username",required = false) String username ,
			@RequestParam(value="status",required = false) String status ,
			@RequestParam(value="lastlogin",required = false) String lastlogin ,
			@RequestParam(value="email",required = false) String email ,
			@RequestParam(value="mobile",required = false) String mobile ,
			@RequestParam(value="officephone",required = false) String officephone ,
			@RequestParam(value="entrydate",required = false) String entrydate ,
			@RequestParam(value="jobtitle",required = false) String jobtitle ,
			@RequestParam(value="sex",required = false) String sex ,
			@RequestParam(value="joblever",required = false) String joblever ,
			@RequestParam(value="memo",required = false) String memo ,
			@RequestParam(value="birthday",required = false) String birthday ,
			@RequestParam(value="avatarpath",required = false) String avatarpath 
			){
		TblSysUser user=new TblSysUser();
		user.setAccount(account);
		user.setAvatarpath(avatarpath);
		user.setBirthday(birthday);
		user.setEmail(email);
		user.setEntrydate(entrydate);
		user.setJoblevel(joblever);
		user.setJobtitle(jobtitle);
		user.setLastlogin(lastlogin);
		user.setMemo(memo);
		user.setMobile(mobile);
		user.setOfficephone(officephone);
		user.setSex(sex);
		user.setStatus(status);
		user.setUsername(username);
		int i=sysUserService.updateTblSysUser(user);
		Map<String, Boolean>map=new HashMap<>();
		if(i>0) {
			map.put("status", true);
		}
		return map;
	}
	/**
	 * 修改密码
	 * @param account
	 * @param password
	 * @return
	 */
	@RequestMapping("/updatePassword")
 	@ResponseBody
	public Map<String, Boolean> updatePassword(
			@RequestParam(value="account",required = true) String account ,
			@RequestParam(value="password",required = true) String password 
			){
		TblSysUser user=new TblSysUser();
		user.setAccount(account);
		user.setPassword(password);
		int i=sysUserService.updatePassword(user);
		Map<String, Boolean>map=new HashMap<>();
		if(i>0) {
			map.put("status", true);
		}
		return map;
	}
	/**
	 * 修改头像保存路径
	 * @param account
	 * @param avatarpath
	 * @return
	 */
	@RequestMapping("/updateAvatarPath")
 	@ResponseBody
	public Map<String, Boolean> updateAvatarPath(
			@RequestParam(value="account",required = true) String account ,
			@RequestParam(value="avatarpath",required = true) String avatarpath 
			){
		TblSysUser user=new TblSysUser();
		user.setAccount(account);
		user.setAvatarpath(avatarpath);
		int i=sysUserService.updateAvatarPath(user);
		Map<String, Boolean>map=new HashMap<>();
		if(i>0) {
			map.put("status", true);
		}
		return map;
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
