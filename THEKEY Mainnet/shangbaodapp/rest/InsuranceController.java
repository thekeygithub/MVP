package com.xczg.blockchain.shangbaodapp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xczg.blockchain.yibaodapp.bean.*;
import com.xczg.blockchain.yibaodapp.rest.NoticeController;
import com.xczg.blockchain.yibaodapp.service.IApplyService;
import com.xczg.blockchain.yibaodapp.service.ITransactionInfoService;
import com.xczg.blockchain.yibaodapp.service.IUserCenterService;
import com.xczg.blockchain.yibaodapp.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("appController")
public class InsuranceController { 

	@Autowired
	private IApplyService service; 
	@Autowired 
	HttpServletRequest request;
	@Autowired
	private IUserCenterService userCenterServiceImpl; 
	@Autowired
	private Environment env; 
	@Autowired
	private ITransactionInfoService transactionInfoService;

	private final Logger logger = LoggerFactory.getLogger(NoticeController.class);

	private String ipfsUrl="http://222.128.14.106:2699/IPFS/api/putUserInformation";
	private String saveUrl ="http://192.168.70.27:9000";
	//private String Url ="http://222.128.14.106:3226";
	private String userUrl ="http://18.136.199.254:20332";
	private String shangbaoAddress="AKRX2qyZTikrJKHCGtF4gKEzXkEC16Sabt";
	/**
	 * APP 端用户点击选择一个保险产品，用户信息和保险名称上链
	 * @return AdkmqctuCEFAGHAGxbVyDjqgp2KGG1tL1E
	 */
	@ResponseBody
	@RequestMapping(value="/chooseProductInfo",method=RequestMethod.POST)
	public String chooseProductApply(
			@RequestParam(value = "product_id",required = true)   int product_id,
			@RequestParam(value = "product_name",required = false)   String product_name,
			@RequestParam(value = "apply_phone",required = true)   String apply_phone,
			@RequestParam(value = "jsid",required = true)   String jsid,
			@RequestParam(value = "mobile",required = false)   String mobile,
			@RequestParam(value = "token",required = false)   String token
			){
		boolean isOK=true;
		String msg="";
		// 登录令牌检查
		if ( !checkUserLogin(token,mobile) ) {
			return "NOT_LOGIN";
		}
		try {
			// 信息上链
			String jsonStr1="{\"jsid\":\""+jsid+"\",\"product_id\":\""+product_id+"\",\"product_name\":\""+product_name+"\",\"apply_phone\":\""+apply_phone+"\"}";
			String hash1=IpfsUtil.putInfoToIPFS(ipfsUrl,jsonStr1);
			String method1="savePayInfo";
			BlockChainResultEntity resultEntity1 =ChainUtil.saveBlock(saveUrl, hash1, method1);
			if (resultEntity1 == null){
				logger.error("调用区块链, 存储IPFS哈希失败");
			}else {
				logger.info("调用区块链, 存储IPFS哈希成功，validKey：{}", resultEntity1);
				String senderAddr="AcesbhiJs1orCB4ty5c4Pi2SBRiMUspvhL"; 
				String contactWalletAddr = env.getProperty("contact.wallet.addr");
				try {
					String serialNo = transactionInfoService.getSerialNo();
					TblTransactionInfo entity = new TblTransactionInfo(
							resultEntity1.getTxid(),"THEKEY", "验证合约", senderAddr, contactWalletAddr,
							"0", DateUtil.getNow(), resultEntity1.getValidKey(), serialNo, "商保投保");
					transactionInfoService.save(entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append("}");

		return reStr.toString();
	}
	/**
	 * APP 端用户获取点击选择的保险产品的明细信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUserChooseProductInfo",method=RequestMethod.POST)
	public String getUserChooseProductApply(
			@RequestParam(value = "product_id",required = true)   int product_id,
			@RequestParam(value = "chainKey",required = true)   String chainKey,
			@RequestParam(value = "mobile",required = false)   String mobile,
			@RequestParam(value = "token",required = false)   String token
			){
		boolean isOK=true;
		String msg="";
		String body="";
		// 登录令牌检查
		if ( !checkUserLogin(token,mobile) ) {
			return "NOT_LOGIN";
		}
		try {
			Map<String, Object> inParam = new HashMap<String, Object>();
			inParam.put("jsonrpc", "2.0");
			inParam.put("method", "invokefunction");
			inParam.put("id", "3");
			Object paramsArr[] = new Object[3];
			paramsArr[0] = "0x33ee36c712b37df8acfbda4a1beb165e100ed3e0";// verifyScriptHash;//区块hash
			paramsArr[1] = "get";
			JSONObject obj = new JSONObject();
			obj.put("type","String");
			obj.put("value",chainKey);
			Object subParamsArr[] = new Object[1];
			subParamsArr[0] = obj;
			paramsArr[2] = subParamsArr;
			inParam.put("params", paramsArr);
			String inJson = JSONObject.toJSONString(inParam);
			RestTemplate restTemplate = new RestTemplate();
			String response = restTemplate.postForObject(env.getProperty("neo.url"), inJson, String.class);
			JSONObject jsObj = JSONObject.parseObject(response);
			if(jsObj != null && jsObj.containsKey("result"))
			{
				JSONObject resObj = jsObj.getJSONObject("result");
				if(resObj!=null && resObj.containsKey("stack")) {
					JSONArray stackArr = resObj.getJSONArray("stack");
					if(stackArr != null && stackArr.size()>0) {
						JSONObject stackObj = stackArr.getJSONObject(0);
						if(stackObj!=null && stackObj.containsKey("value")) {
							String productid = stackObj.getString("value");
							System.out.println(productid);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isOK=false;
		}
		String result = ProductDrtail(product_id);
		body = result;
		// 返回产品明细
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append(",").append("\"body\":").append(body).append("}");
		return reStr.toString();
	}
	/**
	 * 商保投保
	 * 授权保险公司取个人健康档案信息。用户信息和授权信息上链
	 * @param product_name
	 * @param product_insured_age
	 * @param product_money
	 * @param product_insurance_period
	 * @param product_payment_time
	 * @param product_payment_method
	 * @param product_hesitation
	 * @param product_waiting_period
	 * @param product_insurance_area
	 * @param organize_id
	 * @param apply_phone
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertProductApply",method=RequestMethod.POST)
	public String insertProductApply(
			@RequestParam(value = "product_id",required = true)   int product_id,
			@RequestParam(value = "product_name",required = false)   String product_name,
			@RequestParam(value = "product_insured_age",required = false)   String product_insured_age,
			@RequestParam(value = "product_money",required = false)   String product_money,
			@RequestParam(value = "product_insurance_period",required = false)   String product_insurance_period,
			@RequestParam(value = "product_payment_time",required = false)   String product_payment_time,
			@RequestParam(value = "product_payment_method",required = false)   String product_payment_method,
			@RequestParam(value = "product_hesitation",required = false)   String product_hesitation,
			@RequestParam(value = "product_waiting_period",required = false)   String product_waiting_period,
			@RequestParam(value = "product_insurance_area",required = false)   String product_insurance_area,
			@RequestParam(value = "organize_id",required = false)   String organize_id,
			@RequestParam(value = "apply_phone",required = false)   String apply_phone,
			@RequestParam(value = "amount",required = false)   String amount,
			@RequestParam(value = "authCode",required = false)   String authCode,  // 个人健康档案信息授权码
			@RequestParam(value = "insured_name",required = false)   String insured_name,
			@RequestParam(value = "mobile",required = false)   String mobile,
			@RequestParam(value = "token",required = false)   String token
			){
		boolean isOK=true;
		String msg="";
		// 登录令牌检查
		if ( !checkUserLogin(token,mobile) ) {
			return "NOT_LOGIN";
		}
		ProductApply info = new ProductApply();
		info.setProduct_hesitation(product_hesitation);
		info.setProduct_name(product_name);
		info.setProduct_insured_age(product_insured_age);
		info.setProduct_money(product_money);
		info.setProduct_insurance_period(product_insurance_period);
		info.setProduct_payment_time(product_payment_time);
		info.setProduct_payment_method(product_payment_method);
		info.setProduct_insurance_area(product_insurance_area);
		info.setProduct_waiting_period(product_waiting_period);
		info.setApply_phone(apply_phone);
		info.setOrganize_id(organize_id);
		info.setPayment_status("0");
		info.setAmount(amount);
		info.setStatus("0");
		info.setPay_amount(50.0);
		info.setInsured_name(insured_name);
		int i = (int)(Math.random()*10000000);
		info.setContractNo(Integer.toString(i));
		CustInfo cust=userCenterServiceImpl.getCustInfoByMobile(apply_phone);
		if ( cust != null ) {
			info.setApply_user_name(cust.getCust_name());
			info.setApply_user_id(cust.getId());
			info.setApply_id_card(cust.getId_card());
		}
		try {
			long now=System.currentTimeMillis();
			info.setApply_time(now);
			info.setId(now);
			String chainKey="";
			String type="01";
			try {
				//授权保险公司取个人健康档案授权信息
				info.setAuthCode(authCode);
				// 用户信息和授权信息上链
				String jsonStr1=info.toString();
				String hash1=IpfsUtil.putInfoToIPFS(ipfsUrl,jsonStr1);
				String method1="savePayInfo";
				BlockChainResultEntity resultEntity1 =ChainUtil.saveBlock(saveUrl, hash1, method1);
				if (resultEntity1 == null){
					logger.error("调用区块链, 存储IPFS哈希失败");
				}else {
					logger.info("调用区块链, 存储IPFS哈希成功，validKey：{}", resultEntity1);
					String senderAddr="AcesbhiJs1orCB4ty5c4Pi2SBRiMUspvhL"; 
					String contactWalletAddr = env.getProperty("contact.wallet.addr");
					try {
						String serialNo = transactionInfoService.getSerialNo();
						TblTransactionInfo entity = new TblTransactionInfo(
								resultEntity1.getTxid(),"保险公司", "验证合约", senderAddr, contactWalletAddr,
								"0", DateUtil.getNow(), resultEntity1.getValidKey(), serialNo, "健康档案授权");
						transactionInfoService.save(entity);
						chainKey=resultEntity1.getValidKey();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}catch(Exception ee) {
			}
			service.insertProductApply(info);
			// 通知医保DAPP从链上获得用户信息和授权信息
			String noticeURL="http://222.128.14.106:2989/HollyBlockChain/applyController/getUserDocumentFromBigData?chainKey="+chainKey+"&id="+info.getId()+"&id_card="+info.getApply_id_card();
			try {
				HttpClientUtil.doGet(noticeURL);
			}catch(Exception e) {
			}
		}catch(Exception e) {
			e.printStackTrace();
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append("}");
		return reStr.toString();
	}
	// 医保DAPP从链上获得用户信息和授权信息，从双医大数据平台获得个人健康信息上链。
	@ResponseBody
	@RequestMapping(value="/getUserDocumentFromBigData",method=RequestMethod.GET)
	public String getUserDocumentFromBigData(
			@RequestParam(value = "id",required = true)  long id,
			@RequestParam(value = "chainKey",required = true)   String chainKey,
			@RequestParam(value = "id_card",required = false)   String id_card,
			@RequestParam(value = "token",required = false)   String token
			){
		boolean isOK=true;
		String msg="";
		try {
			String value="";
			//上链获取用户信息和授权信息
			Map<String, Object> inParam = new HashMap<String, Object>();
			inParam.put("jsonrpc", "2.0");
			inParam.put("method", "invokefunction");
			inParam.put("id", "3");
			Object paramsArr[] = new Object[3];
			paramsArr[0] = "0x33ee36c712b37df8acfbda4a1beb165e100ed3e0";// verifyScriptHash;//区块hash
			paramsArr[1] = "get";
			JSONObject obj = new JSONObject();
			obj.put("type","String");
			obj.put("value",chainKey);
			Object subParamsArr[] = new Object[1];
			subParamsArr[0] = obj;
			paramsArr[2] = subParamsArr;
			inParam.put("params", paramsArr);
			String inJson = JSONObject.toJSONString(inParam);
			RestTemplate restTemplate = new RestTemplate();
			String response = restTemplate.postForObject(env.getProperty("neo.url"), inJson, String.class);
			JSONObject jsObj = JSONObject.parseObject(response);
			if(jsObj != null && jsObj.containsKey("result"))
			{
				JSONObject resObj = jsObj.getJSONObject("result");
				if(resObj!=null && resObj.containsKey("stack")) {
					JSONArray stackArr = resObj.getJSONArray("stack");
					if(stackArr != null && stackArr.size()>0) {
						JSONObject stackObj = stackArr.getJSONObject(0);
						if(stackObj!=null && stackObj.containsKey("value")) {
							value = stackObj.getString("value");
						}
					}
				}
			}
			id_card="130105198800001111";
			// 从双医大数据平台获得个人健康信息
			String noticeURL="http://192.168.70.29:811/HealthInfo?userid="+id_card;
			String healthRecord="";
			try {
				String res=HttpClientUtil.doGet(noticeURL);
				healthRecord=JSONObject.parseObject(res).get("msg").toString();
				// healthRecord={"Name":"wangwu","Id":"140101199001010051","Gender":"Female","Cellphone":"18700000000","Company":"sinopec","Residence":"","Blood":"A","Education":"Master","Career":"Engineer","Marry":"true","Medical_Type":"city_employee","Allergies":{"Penicillin":"false","Sulfonamide":"false","Streptomycin":"false","Others":"none"},"Past_Edical_History":{"Disease":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false","BrainStroke":"false","Neuropathy":"false","Tuberculosis":"false","Hepatitis":"false","Others":"false"},"Surgery":{},"Trauma":{},"BloodTran":{}},"Family":{"Father":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false","BrainStroke":"false"},"Mother":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false","BrainStroke":"false"},"Brothers":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false","BrainStroke":"false"},"Children":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false","BrainStroke":"false"}}}
			}catch(Exception e) {
			}

			if ( !StringUtil.isEmpty(healthRecord) ) {
				// 健康档案上链
				String hash1=IpfsUtil.putInfoToIPFS(ipfsUrl,healthRecord);
				String method1="savePayInfo";
				BlockChainResultEntity resultEntity1 =ChainUtil.saveBlock(saveUrl, hash1, method1);
				if (resultEntity1 == null){
					logger.error("调用区块链, 存储IPFS哈希失败");
				}else {
					logger.info("调用区块链, 存储IPFS哈希成功，validKey：{}", resultEntity1);
					String senderAddr="AcesbhiJs1orCB4ty5c4Pi2SBRiMUspvhL"; 
					String contactWalletAddr = env.getProperty("contact.wallet.addr");
					try {
						String serialNo = transactionInfoService.getSerialNo();
						TblTransactionInfo entity = new TblTransactionInfo(
								resultEntity1.getTxid(),"THEKEY","验证合约", senderAddr, contactWalletAddr,
								"0", DateUtil.getNow(), resultEntity1.getValidKey(), serialNo, "健康档案");
						transactionInfoService.save(entity);

						chainKey=entity.getValidKey();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 通知保险DAPP从链上获得用户个人健康信息
				noticeURL="http://222.128.14.106:2989/HollyBlockChain/applyController/checkProductApply?chainKey="+chainKey+"&id="+id;
				try {
					HttpClientUtil.doGet(noticeURL);
				}catch(Exception e) {
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append("}");
		return reStr.toString();
	}
	// 保险DAPP获得个人健康信息。智能合约通过审核规则（哪些保险产品对应的投保人健康审核规则）进行审核。审核结果上链。
	@ResponseBody
	@RequestMapping(value="/checkProductApply",method=RequestMethod.GET)
	public String checkProductApply(
			@RequestParam(value = "id",required = true)  long id,
			@RequestParam(value = "chainKey",required = true)   String chainKey,
			@RequestParam(value = "token",required = false)   String token
			){
		boolean isOK=true;
		String msg="";
		ProductApply info = service.getProductApplyById(id);
		try {
			// 从链上取健康档案
			String healthRecord="";
			Map<String, Object> inParam = new HashMap<String, Object>();
			inParam.put("jsonrpc", "2.0");
			inParam.put("method", "invokefunction");
			inParam.put("id", "3");
			Object paramsArr[] = new Object[3];
			paramsArr[0] = "0x33ee36c712b37df8acfbda4a1beb165e100ed3e0";// verifyScriptHash;//区块hash
			paramsArr[1] = "get";
			JSONObject obj = new JSONObject();
			obj.put("type","String");
			obj.put("value",chainKey);
			Object subParamsArr[] = new Object[1];
			subParamsArr[0] = obj;
			paramsArr[2] = subParamsArr;
			inParam.put("params", paramsArr);
			String inJson = JSONObject.toJSONString(inParam);
			RestTemplate restTemplate = new RestTemplate();
			String response = restTemplate.postForObject(env.getProperty("neo.url"), inJson, String.class);
			JSONObject jsObj = JSONObject.parseObject(response);
			System.out.println("返回结果"+jsObj);
			if(jsObj != null && jsObj.containsKey("result"))
			{
				JSONObject resObj = jsObj.getJSONObject("result");
				if(resObj!=null && resObj.containsKey("stack")) {
					JSONArray stackArr = resObj.getJSONArray("stack");
					if(stackArr != null && stackArr.size()>0) {
						JSONObject stackObj = stackArr.getJSONObject(0);
						if(stackObj!=null && stackObj.containsKey("value")) {
							healthRecord = stackObj.getString("value");
						}
					}
				}
			}
			if ( StringUtil.isEmpty(healthRecord) ) {
				healthRecord="{\"Name\":\"wangwu\",\"Id\":\"140101199001010051\",\"Gender\":\"Female\",\"Cellphone\":\"18700000000\",\"Company\":\"sinopec\",\"Residence\":\"\",\"Blood\":\"A\",\"Education\":\"Master\",\"Career\":\"Engineer\",\"Marry\":\"true\",\"Medical_Type\":\"city_employee\",\"Allergies\":{\"Penicillin\":\"false\",\"Sulfonamide\":\"false\",\"Streptomycin\":\"false\",\"Others\":\"none\"},\"Past_Edical_History\":{\"Disease\":{\"Hypertension\":\"false\",\"Diabetes\":\"false\",\"HeartDisease\":\"false\",\"LungDisease\":\"false\",\"Cancer\":\"false\",\"BrainStroke\":\"false\",\"Neuropathy\":\"false\",\"Tuberculosis\":\"false\",\"Hepatitis\":\"false\",\"Others\":\"false\"},\"Surgery\":{},\"Trauma\":{},\"BloodTran\":{}},\"Family\":{\"Father\":{\"Hypertension\":\"false\",\"Diabetes\":\"false\",\"HeartDisease\":\"false\",\"LungDisease\":\"false\",\"Cancer\":\"false\",\"BrainStroke\":\"false\"},\"Mother\":{\"Hypertension\":\"false\",\"Diabetes\":\"false\",\"HeartDisease\":\"false\",\"LungDisease\":\"false\",\"Cancer\":\"false\",\"BrainStroke\":\"false\"},\"Brothers\":{\"Hypertension\":\"false\",\"Diabetes\":\"false\",\"HeartDisease\":\"false\",\"LungDisease\":\"false\",\"Cancer\":\"false\",\"BrainStroke\":\"false\"},\"Children\":{\"Hypertension\":\"false\",\"Diabetes\":\"false\",\"HeartDisease\":\"false\",\"LungDisease\":\"false\",\"Cancer\":\"false\",\"BrainStroke\":\"false\"}}}";
			}
			// 根据规则审核，返回结果
			String status=checkHealthRecord(healthRecord)?"1":"2"; 
			info.setId(id);
			info.setStatus(status);
			info.setCheck_time(System.currentTimeMillis());
			// 审核结果上链
			String jsonStr1=info.toString();
			String hash1=IpfsUtil.putInfoToIPFS(ipfsUrl,jsonStr1);
			String method1="savePayInfo";
			BlockChainResultEntity resultEntity1 =ChainUtil.saveBlock(saveUrl, hash1, method1);
			if (resultEntity1 == null){
				logger.error("调用区块链, 存储IPFS哈希失败");
			}else {
				logger.info("调用区块链, 存储IPFS哈希成功，validKey：{}", resultEntity1);
				String senderAddr="AcesbhiJs1orCB4ty5c4Pi2SBRiMUspvhL"; 
				String contactWalletAddr = env.getProperty("contact.wallet.addr");
				try {
					String serialNo = transactionInfoService.getSerialNo();
					TblTransactionInfo entity = new TblTransactionInfo(
							resultEntity1.getTxid(),"保险公司", "验证合约", senderAddr, contactWalletAddr,
							"0", DateUtil.getNow(), resultEntity1.getValidKey(), serialNo, "商保投保审核结果");
					transactionInfoService.save(entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			service.updateProductApply(info);
		}catch(Exception e) {
			e.printStackTrace();
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append("}");
		return reStr.toString();
	}
	/**
	 * 根据规则自动审核健康档案
	 * @param healthRecord
	 * @return
	 */
	private boolean checkHealthRecord(String healthRecord) {
		try {
			JSONObject obj=JSONObject.parseObject(healthRecord).getJSONObject("Past_Edical_History").getJSONObject("Disease");
			//  {"Name":"wangwu","Id":"140101199001010051","Gender":"Female","Cellphone":"18700000000","Company":"sinopec"
			// ,"Residence":"","Blood":"A","Education":"Master","Career":"Engineer","Marry":"true","Medical_Type":"city_employee"
			// ,"Allergies":{"Penicillin":"false","Sulfonamide":"false","Streptomycin":"false","Others":"none"}
			// ,"Past_Edical_History":{"Disease":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false"
			// ,"BrainStroke":"false","Neuropathy":"false","Tuberculosis":"false","Hepatitis":"false","Others":"false"},"Surgery":{},"Trauma":{},"BloodTran":{}}
			// ,"Family":{"Father":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false","BrainStroke":"false"}
			// ,"Mother":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false","BrainStroke":"false"}
			// ,"Brothers":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false","BrainStroke":"false"}
			// ,"Children":{"Hypertension":"false","Diabetes":"false","HeartDisease":"false","LungDisease":"false","Cancer":"false","BrainStroke":"false"}}}
			if ( "true".equals(obj.get("Hypertension")) )	return false;
			if ( "true".equals(obj.get("Diabetes")) )	return false;
			if ( "true".equals(obj.get("HeartDisease")) )	return false;
			if ( "true".equals(obj.get("LungDisease")) )	return false;
			if ( "true".equals(obj.get("Cancer")) )	return false;
			if ( "true".equals(obj.get("BrainStroke")) )	return false;
			if ( "true".equals(obj.get("Neuropathy")) )	return false;
			if ( "true".equals(obj.get("Tuberculosis")) )	return false;
			return true;
		}catch(Exception e) {
			return true;
		}
	}
	//  个人通过APP获得审核结果
	@ResponseBody
	@RequestMapping(value="/getProductApplyList")
	public String getProductApplyList(
			@RequestParam(value = "mobile",required = false)   String mobile,
			@RequestParam(value = "token",required = false)   String token){
		String sEcho = request.getParameter("sEcho");
		String time1 = request.getParameter("time1");
		String time2 = request.getParameter("time2");
		String apply_phone = request.getParameter("apply_phone");
		// 登录令牌检查
		if ( !checkUserLogin(token,mobile) ) {
			return "NOT_LOGIN";
		}
		long qtime1 = DateUtil.toTimestamp(time1,true);
		long qtime2 = DateUtil.toTimestamp(time2,false);
		ProductApply productApply = new ProductApply();
		productApply.setApply_phone(apply_phone);
		boolean isOK=true;
		String msg="";
		StringBuffer listStr = new StringBuffer();
		int total=service.getProductApplyListCount(productApply,qtime1,qtime2);
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append(",\"code\":1,\"sEcho\":").append(sEcho).append(",\"iTotalRecords\":").append(total);
		reStr.append(",\"iTotalDisplayRecords\":").append(total).append(",\"aaData\":[");
		reStr.append(listStr);
		reStr.append("]}");
		return reStr.toString();
	}
	// 个人给保险转账。APP上显示余额变化。结果上链。
	@ResponseBody
	@RequestMapping(value="/payProductFee",method=RequestMethod.POST)
	public String payProductFee(
			@RequestParam(value = "id",required = true) long id,
			@RequestParam(value = "mobile",required = false)   String mobile,
			@RequestParam(value = "token",required = false)   String token){
		boolean isOK=true;
		String msg="";
		try {
			// 登录令牌检查
			if ( !checkUserLogin(token,mobile) ) {
				return "NOT_LOGIN";
			}
			ProductApply info=service.getProductApplyById(id);
			//修改支付结果
			info.setPayment_status("1");
			String phone = info.getApply_phone();
			String result=ChainUtil.tranfer1(shangbaoAddress,"3",userUrl);
			String jsonStr1=info.toString()+"个人支付信息："+result;
			String hash1=IpfsUtil.putInfoToIPFS(ipfsUrl,jsonStr1);
			String method1="savePayInfo";
			BlockChainResultEntity resultEntity1 =ChainUtil.SaveBlock(saveUrl, hash1, method1);
			if (resultEntity1 == null){
				logger.error("调用区块链, 存储IPFS哈希失败");
			}else {
				logger.info("调用区块链, 存储IPFS哈希成功，validKey：{}", resultEntity1);
				String contactWalletAddr = env.getProperty("contact.wallet.addr");
				try {
					String serialNo = transactionInfoService.getSerialNo();
					TblTransactionInfo entity = new TblTransactionInfo(
							result,info.getApply_user_name(), "支付合约", shangbaoAddress, contactWalletAddr,
							info.getAmount(), DateUtil.getNow(), resultEntity1.getValidKey(), serialNo, "支付核保金额");
					transactionInfoService.save(entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//查账户余额
			//String tky_balance=ChainUtil.getBalace();
			CustInfo cust = userCenterServiceImpl.getCustInfoByMobile(phone);
			double balance = cust.getTky_balance();
			// 查询产品要支付的金额
			double amount = Double.parseDouble(info.getAmount());
			if(balance-amount>=0){
				service.updateProductApplyPayStatus(info);
				// 修改个人账户余额
				Map<String,Object> maps = new HashMap<>();
				maps.put("mobile",phone);
				maps.put("amount",balance-amount);//改动tky-balance
				userCenterServiceImpl.deductCost(maps);
			}else {
				isOK=false;
				msg="余额不足，请充值！";
			}
		}catch(Exception e) {
			e.printStackTrace();
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append("}");
		return reStr.toString();
	}
	//获取身份认证信息
	@ResponseBody
	@RequestMapping(value="/getUserInfoBymobile",method=RequestMethod.POST)
	public String getUserInfoBymobile(
			@RequestParam(value = "mobile",required = true) String mobile,
			@RequestParam(value = "token",required = false)   String token) {
		boolean isOK=true;
		String msg="";
		String body="";
		try {
			CustInfo cust = userCenterServiceImpl.getCustInfoByMobile(mobile);
			body=cust.toJson();
		}catch(Exception e) {
			e.printStackTrace();
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append(",\"body\":").append(body).append("}");
		return reStr.toString();
	}
	//通过id(created_time)获取购买的保险产品的信息
	@ResponseBody
	@RequestMapping(value="/getProductApplyById",method=RequestMethod.POST)
	public String getProductApplyById(
			@RequestParam(value = "id",required = true) long id,
			@RequestParam(value = "mobile",required = false)   String mobile,
			@RequestParam(value = "token",required = false)   String token
			){
		boolean isOK=true;
		String msg="";
		String body="";
		// 登录令牌检查
		if ( !checkUserLogin(token,mobile) ) {
			return "NOT_LOGIN";
		}
		ProductApply info = service.getProductApplyById(id);
		if ( info != null ) {
			body=info.toJson();
		} else {
			body="\"\"";
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append(",\"body\":").append(body).append("}");
		return reStr.toString();
	}
	//app个人中心实名认证接口（添加或者修改实名认证信息）
	@RequestMapping("addUserInfo")
	@ResponseBody
	public String addUserInfo(@RequestParam(value="name",required = true) String name,
			@RequestParam(value="sex",required = true) String sex,
			@RequestParam(value="id_card",required = true) String id_card,
			@RequestParam(value="social_id",required = true) String social_id,
			@RequestParam(value="phone",required = true) String phone,
			@RequestParam(value = "birthday",required = true)String birthday,
			@RequestParam(value = "insure_addr",required = true)String insure_addr,
			@RequestParam(value = "token",required = false)   String token){

		// 登录令牌检查
		if ( !checkUserLogin(token,phone) ) {
			return "NOT_LOGIN";
		}
		CustInfo userInfo = new CustInfo();
		userInfo.setId_card(id_card);
		userInfo.setCust_name(name);
		if(sex!=null) {
			userInfo.setSex(sex);
		}
		userInfo.setSocial_card_id(social_id);
		userInfo.setMobile(phone);
		userInfo.setBirth_date(Long.parseLong(birthday));
		userInfo.setAddr(insure_addr);
		//userInfo.setTky_balance(50);/改动/
		int result = service.addUserInfo(userInfo);
		boolean isOK=true;
		String msg="";
		String body="";
		if(userInfo!=null){
			body=Integer.toString(result);
		}else {
			body="\"\"";
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append(",\"body\":").append(body).append("}");
		return reStr.toString();
	}
	//获取用户信息
	@RequestMapping("getUserInfo")
	@ResponseBody
	public String getUserInfo( 
			@RequestParam(value="phone",required = false) String phone,
			@RequestParam(value = "mobile",required = false)   String mobile,
			@RequestParam(value = "token",required = false)   String token){

		// 登录令牌检查
		if ( !checkUserLogin(token,mobile) ) {
			return "NOT_LOGIN";
		}
		CustInfo userInfo = service.getUserInfo(phone);
		boolean isOK=true;
		String msg="";
		String body="";
		if(userInfo!=null){
			body=userInfo.toJson();
		}else {
			body="\"\"";
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append(",\"body\":").append(body).append("}");
		return reStr.toString();
	}

	//获取余额信息
	@ResponseBody
	@RequestMapping(value = "/getBalance",method = RequestMethod.POST)
	public String getBalance(
			@RequestParam(value = "mobile",required = true) String mobile,
			@RequestParam(value = "token",required = false)   String token
			){
		boolean isOK=true;
		String msg="";
		String body="";
		// 登录令牌检查
		if ( !checkUserLogin(token,mobile) ) {
			return "NOT_LOGIN";
		}
		double tky_balance=0;
		try {
			/*修改账户余额使账户余额与区块链tky同步
			Map<String,String> map = new HashMap<>();
			map.put("mobile",mobile);
			tky_balance=ChainUtil.getBalace();
			map.put("tky_balance",tky_balance);
			service.updateBalance(map);
			 */
			CustInfo cust = userCenterServiceImpl.getCustInfoByMobile(mobile);
			tky_balance = cust.getTky_balance();
		}catch(Exception e) {
			e.printStackTrace();
			isOK=false;
		}
		int statusCode=200;
		StringBuffer reStr = new StringBuffer();
		reStr.append("{\"status\":").append(isOK).append(",\"message\":\"").append(msg).append("\",\"statusCode\":").append(statusCode).append(",\"body\":").append("{").append("\"tky_balance\":").append(tky_balance).append("}").append("}");
		return reStr.toString();
	}
	/**
	 * 检查登录
	 * @return
	 */
	private boolean checkUserLogin(String token,String mobile) {
		try {
			String sessionUserId=(String)request.getSession().getAttribute("current_user_id");
			if ( StringUtil.isEmpty(sessionUserId) ) {
				if ( !StringUtil.isEmpty(token) ) {
					CustInfo ci=userCenterServiceImpl.getCustInfoByToken(token);
					if ( ci !=null && ci.getMobile().equals(mobile) ) {
						return true;
					}
				}
			} else {
				return true;
			}
		}catch(Exception e) {
			return false;
		}
		return false;
	}
	public String ProductDrtail(int id) {
		Map<String,String> product1 = new HashMap<>();
		product1.put("cpmc","抗癌卫士");
		product1.put("bbrnl","出生满28天-50周岁");
		product1.put("bzje","1万-40万");
		product1.put("bxqj","保终身");
		product1.put("jfnx","15年/20年");
		product1.put("jffs","月交/年交");
		product1.put("yyq","15天");
		product1.put("ddq","180天");
		product1.put("tbdq","广东、北京、上海、天津、江苏、浙江、四川、重庆、山东、湖南、湖北、辽宁、陕西、河南、江西");
		Map<String,String> product2 = new HashMap<>();
		product2.put("cpmc","e家保");
		product2.put("bbrnl","出生满28天-50周岁");
		product2.put("bzje","10万-50万");
		product2.put("bxqj","1年/可续保");
		product2.put("jfnx","1年");
		product2.put("jffs","年交");
		product2.put("yyq","15天");
		product2.put("ddq","30天");
		product2.put("tbdq","广东、北京、上海、天津、江苏、浙江、四川、重庆、山东、湖南、湖北、辽宁、陕西、河南、江西");
		Map<String,String> product3 = new HashMap<>();
		product3.put("cpmc","医保+成人");
		product3.put("bbrnl","出生满28天-50周岁");
		product3.put("bzje","10万-100万");
		product3.put("bxqj","10年/可续保");
		product3.put("jfnx","10年");
		product3.put("jffs","月交/年交");
		product3.put("yyq","30天");
		product3.put("ddq","150天");
		product3.put("tbdq","广东、北京、上海、天津、江苏、浙江、四川、重庆、山东、湖南、湖北、辽宁、陕西、河南、江西");
		if(id==1){
			StringBuffer str = new StringBuffer();
			str.append("{\"cpmc\":").append("\"").append(product1.get("cpmc")).append("\"").append(",")
			.append("\"bbrnl\":").append("\"").append(product1.get("bbrnl")).append("\"").append(",")
			.append("\"bzje\":").append("\"").append(product1.get("bzje")).append("\"").append(",")
			.append("\"bxqj\":").append("\"").append(product1.get("bxqj")).append("\"").append(",")
			.append("\"jfnx\":").append("\"").append(product1.get("jfnx")).append("\"").append(",")
			.append("\"jffs\":").append("\"").append(product1.get("jffs")).append("\"").append(",")
			.append("\"yyq\":").append("\"").append(product1.get("yyq")).append("\"").append(",")
			.append("\"ddq\":").append("\"").append(product1.get("ddq")).append("\"").append(",")
			.append("\"tbdq\":").append("\"").append(product1.get("tbdq")).append("\"").append("}");
			return str.toString();
		}
		else if(id==2){
			StringBuffer str = new StringBuffer();
			str.append("{\"cpmc\":").append("\"").append(product2.get("cpmc")).append("\"").append(",")
			.append("\"bbrnl\":").append("\"").append(product2.get("bbrnl")).append("\"").append(",")
			.append("\"bzje\":").append("\"").append(product2.get("bzje")).append("\"").append(",")
			.append("\"bxqj\":").append("\"").append(product2.get("bxqj")).append("\"").append(",")
			.append("\"jfnx\":").append("\"").append(product2.get("jfnx")).append("\"").append(",")
			.append("\"jffs\":").append("\"").append(product2.get("jffs")).append("\"").append(",")
			.append("\"yyq\":").append("\"").append(product2.get("yyq")).append("\"").append(",")
			.append("\"ddq\":").append("\"").append(product2.get("ddq")).append("\"").append(",")
			.append("\"tbdq\":").append("\"").append(product2.get("tbdq")).append("\"").append("}");
			return str.toString();
		}else if(id==3){
			StringBuffer str = new StringBuffer();
			str.append("{\"cpmc\":").append("\"").append(product3.get("cpmc")).append("\"").append(",")
			.append("\"bbrnl\":").append("\"").append(product3.get("bbrnl")).append("\"").append(",")
			.append("\"bzje\":").append("\"").append(product3.get("bzje")).append("\"").append(",")
			.append("\"bxqj\":").append("\"").append(product3.get("bxqj")).append("\"").append(",")
			.append("\"jfnx\":").append("\"").append(product3.get("jfnx")).append("\"").append(",")
			.append("\"jffs\":").append("\"").append(product3.get("jffs")).append("\"").append(",")
			.append("\"yyq\":").append("\"").append(product3.get("yyq")).append("\"").append(",")
			.append("\"ddq\":").append("\"").append(product3.get("ddq")).append("\"").append(",")
			.append("\"tbdq\":").append("\"").append(product3.get("tbdq")).append("\"").append("}");

			return str.toString();
		}
		return "";
	}


}
