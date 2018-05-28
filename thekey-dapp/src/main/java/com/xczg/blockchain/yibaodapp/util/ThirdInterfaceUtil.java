package com.xczg.blockchain.yibaodapp.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xczg.blockchain.yibaodapp.bean.TblInterfaceHis;
import com.hollycrm.hollyuniproxy.test.HttpJsonBlockListClient;

public class ThirdInterfaceUtil {

	private static Logger log = LoggerFactory
			.getLogger(ThirdInterfaceUtil.class);

	/**
	 * 社保人员头像信息接口 /社保卡状态信息接口/ 行业规则接口 /死亡判断接口 /入刑判断接口/ 追讨判断接口 /国家机密信息-特殊诊断接口
	 * 国家机密信息-特殊人员接口
	 * 
	 * @param interCode
	 *            接口编码
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Integer judge(String interCode, Map<String, Object> inParam,
			List<TblInterfaceHis> interfaceHisList, Environment env,
			JSONObject type_count, StringBuffer resSb) {
		JSONObject req = new JSONObject();
		Integer res = 2;// 0-验证通过1-验证失败2-未经过验证3-接口调用失败(其他错误)

		String type = inParam.get("type") == null ? null : inParam.get("type")
				.toString();// 行业
		String id_no = "";

		String validKey = inParam.get("validKey") == null ? "" : inParam.get(
				"validKey").toString();
		Map respMap = new HashMap<String, Object>();
		Integer index = 1;
		try {
			Map<String, Object> userInfoMap = (Map<String, Object>) ((Map<String, Object>) inParam
					.get("userInfoMap")).get("userInfo");// IPFS用户基本信息
			Map<String, Object> imageMap = (Map<String, Object>) inParam
					.get("imageMap");// IPFS照片信息
			String realName = userInfoMap.get("name") == null ? null
					: userInfoMap.get("name").toString();// 姓名
			id_no = userInfoMap.get("id_no") == null ? null : userInfoMap.get(
					"id_no").toString();// 证件号码
			String photo1 = imageMap.get("image") == null ? null : imageMap
					.get("image").toString();

			String url = "";// 接口调用地址

			// 社保卡基本信息接口 2 RS（人社） SBKJBXX
			if (interCode.equals(InterfaceEnum.SBKJBXX.getCode())) {
				url = env.getProperty("SBKJBXX.url");
				req.put("certNo", id_no);
				req.put("realName", realName);
				String resJson = HttpUtil.sendPost(url, req.toJSONString());
				log.info(validKey + " SBKJBXX 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				inParam.put("SBKJBXX", respMap);
				Integer code = respMap.get("code") == null ? 0 : Integer
						.valueOf(respMap.get("code").toString());
				if (code == 0) {
					res = 0;
					if (Integer.valueOf(type_count.get(
							ReceiverEnum.RS.getCode()).toString()) <= 0) {
						type_count.put(ReceiverEnum.RS.getCode(), 1);
					}// 所有人社接口调用成功给人社一个币，其中一个人社接口失败，则不给人社币
				} else {
					res = 1;
				}
				index = 1;
			}// 社保卡头像信息像接口 3 RS（人社） SBKTXXX
			else if (interCode.equals(InterfaceEnum.SBKTXXX.getCode())) {
				url = env.getProperty("SBKTXXX.url");
				req.put("certNo", id_no);
				req.put("realName", realName);
				String resJson = HttpUtil.sendPost(url, req.toJSONString());
				respMap = (Map) JSONObject.parseObject(resJson);
				log.info(validKey + " SBKTXXX 接口响应：" + resJson);
				String image = respMap.get("image") == null ? "" : respMap.get(
						"image").toString();
				userInfoMap.put("Photo2", image);
				Integer code = respMap.get("code") == null ? 0 : Integer
						.valueOf(respMap.get("code").toString());
				if (code == 0) {
					res = 0;
					if (Integer.valueOf(type_count.get(
							ReceiverEnum.RS.getCode()).toString()) <= 0) {
						type_count.put(ReceiverEnum.RS.getCode(), 1);
					}// 所有人社接口调用成功给人社一个币，其中一个人社接口失败，则不给人社币
				} else {
					res = 1;
				}
				index = 2;
			}// 公安头像信息接口 4 GA（公安） GATXXX
			else if (interCode.equals(InterfaceEnum.GATXXX.getCode())) {
				url = env.getProperty("GATXXX.url");
				req.put("certNo", id_no);
				req.put("realName", realName);
				String resJson = HttpUtil.sendPost(url, req.toJSONString());
				log.info(validKey + " GATXXX 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				String image = respMap.get("image") == null ? "" : respMap.get(
						"image").toString();
				userInfoMap.put("Photo2", image);
				Integer code = respMap.get("code") == null ? 0 : Integer
						.valueOf(respMap.get("code").toString());
				if (code == 0) {
					res = 0;
					if (Integer.valueOf(type_count.get(
							ReceiverEnum.GA.getCode()).toString()) <= 0) {
						type_count.put(ReceiverEnum.GA.getCode(), 1);
					}// 所有公安接口调用成功给人社一个币，其中一个公安接口失败，则不给公安币
				} else {
					res = 1;
				}
				index = 3;
			} // 人脸识别接口 5 易保 RLSB
			else if (interCode.equals(InterfaceEnum.RLSB.getCode())) {
				url = env.getProperty("RLSB.url");

				String app_secret = "2RLYsN1SK03OsQD4EPWWrDOW";
				String app_key = "1208661ca8c342d4a1b02ca34baf6640";
				String client_time = new SimpleDateFormat("yyyyMMddhhmm")
						.format(new Date());
				String transaction_no = UUID.randomUUID().toString()
						.replaceAll("-", "");
				req.put("app_key", app_key);
				req.put("photo1", photo1);// IPFS头像
				log.info("ipfs 头像："+photo1);
				req.put("photo2", userInfoMap.get("Photo2"));// 社保卡头像/公安头像
				log.info("社保卡头像/公安头像："+userInfoMap.get("Photo2"));
				req.put("client_time", client_time);
				req.put("transaction_no", transaction_no);
				Map<String, String> signParameters = new HashMap<String, String>();
				signParameters.put("app_key", app_key);
				signParameters.put("client_time", client_time);
				signParameters.put("transaction_no", transaction_no);
				String sign = "";
				try {
					sign = MD5Util.sign(signParameters, app_secret);
				} catch (Exception e2) {
					sign = "";
					log.error("人脸识别接口MD5加密 error " + e2.getMessage(), e2);
				}
				req.put("sign", sign);
				/*
				String resJson = HttpJsonBlockListClient.requestNeo(url,
						req.toJSONString());
				log.info(validKey + " RLSB 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				Integer msg = respMap.get("score") == null ? 0 : Integer
						.valueOf(respMap.get("score").toString());
				if (msg >= 60) {
					res = 0;
				} else {
					res = 1;
				}*/
				res=0;
				index = 4;
			} // 社保卡状态信息接口 6 RS（人社） SBKZTXX
			else if (interCode.equals(InterfaceEnum.SBKZTXX.getCode())) {
				url = env.getProperty("SBKZTXX.url");
				req.put("certNo", id_no);
				req.put("realName", realName);
				String resJson = HttpUtil.sendPost(url, req.toJSONString());
				log.info(validKey + " SBKZTXX 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				inParam.put("SBKZTXX", respMap);
				Integer code = respMap.get("code") == null ? 0 : Integer
						.valueOf(respMap.get("code").toString());
				if (code == 0) {
					res = 0;
					if (Integer.valueOf(type_count.get(
							ReceiverEnum.RS.getCode()).toString()) <= 0) {
						type_count.put(ReceiverEnum.RS.getCode(), 1);
					}// 所有人社接口调用成功给人社一个币，其中一个人社接口失败，则不给人社币
				} else {
					res = 1;
				}
				index = 5;
			}// 死亡判断接口 7 GA（公安） SWPD
			else if (interCode.equals(InterfaceEnum.SWPD.getCode())) {
				url = env.getProperty("SWPD.url");
				req.put("sfzh", id_no);
				String resJson = HttpJsonBlockListClient.requestNeo(url,
						req.toJSONString());
				log.info(validKey + " SWPD 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				Integer yhzt = respMap.get("yhzt") == null ? 0 : Integer
						.valueOf(respMap.get("yhzt").toString());
				if (yhzt == 0) {// 0：未死亡 1：已死亡
					res = 0;
					if (Integer.valueOf(type_count.get(
							ReceiverEnum.GA.getCode()).toString()) <= 0) {
						type_count.put(ReceiverEnum.GA.getCode(), 1);
					}
				} else {
					res = 1;
				}
				index = 6;
			} // 入刑判断接口 8 GA（公安） RXPD
			else if (interCode.equals(InterfaceEnum.RXPD.getCode())) {
				url = env.getProperty("RXPD.url");
				req.put("sfzh", id_no);
				String resJson = HttpJsonBlockListClient.requestNeo(url,
						req.toJSONString());
				log.info(validKey + " RXPD 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				Integer rxzt = respMap.get("rxzt") == null ? 0 : Integer
						.valueOf(respMap.get("rxzt").toString());
				if (rxzt == 0) {// 0：未入刑 1：有入刑
					res = 0;
					if (Integer.valueOf(type_count.get(
							ReceiverEnum.GA.getCode()).toString()) <= 0) {
						type_count.put(ReceiverEnum.GA.getCode(), 1);
					}
				} else {
					res = 1;
				}
				index = 7;
			} // 追逃判断接口 9 GA（公安） ZTPW
			else if (interCode.equals(InterfaceEnum.ZTPW.getCode())) {
				url = env.getProperty("ZTPW.url");
				req.put("sfzh", id_no);
				String resJson = HttpJsonBlockListClient.requestNeo(url,
						req.toJSONString());
				log.info(validKey + " ZTPW 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				Integer ztzt = respMap.get("ztzt") == null ? 0 : Integer
						.valueOf(respMap.get("ztzt").toString());
				if (ztzt == 0) {// 0：无追讨 1：有追讨
					res = 0;
					if (Integer.valueOf(type_count.get(
							ReceiverEnum.GA.getCode()).toString()) <= 0) {
						type_count.put(ReceiverEnum.GA.getCode(), 1);
					}
				} else {
					res = 1;
				}
				index = 8;
			} // 国家机密信息-特殊诊断接口 10 易保 TSZD
			else if (interCode.equals(InterfaceEnum.TSZD.getCode())) {
				url = env.getProperty("TSZD.url");
				req.put("icd_code", id_no);
				String resJson = HttpJsonBlockListClient.requestNeo(url,
						req.toJSONString());
				log.info(validKey + " TSZD 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				Integer jmbs = respMap.get("jmbs") == null ? 0 : Integer
						.valueOf(respMap.get("jmbs").toString());
				if (jmbs == 0) {// 0：非国家机密 1：国家机密
					res = 0;
				} else {
					res = 1;
				}
				index = 9;
			} // 国家机密信息-特殊人员接口 11 易保 TSRY
			else if (interCode.equals(InterfaceEnum.TSRY.getCode())) {
				url = env.getProperty("TSRY.url");
				req.put("sfzh", id_no);
				String resJson = HttpJsonBlockListClient.requestNeo(url,
						req.toJSONString());
				log.info(validKey + " TSRY 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				Integer jmbs = respMap.get("jmbs") == null ? 0 : Integer
						.valueOf(respMap.get("jmbs").toString());
				if (jmbs == 0) {// 0：非国家机密 1：国家机密
					res = 0;
				} else {
					res = 1;
				}
				index = 10;
			} // 位置信息接口 12 YYS（运营商） GPS
			else if (interCode.equals(InterfaceEnum.GPS.getCode())) {
				url = env.getProperty("GPS.url");
				req.put("telNo", "15927270420");// 手机号
				req.put("longitude", "116.374948");// 经度
				req.put("latitude", "39.924232");// 纬度
				String resJson = HttpJsonBlockListClient.requestNeo(url,
						req.toJSONString());
				log.info(validKey + " GPS 接口响应：" + resJson);
				respMap = (Map) JSONObject.parseObject(resJson);
				Integer rsCode = respMap.get("rsCode") == null ? 0 : Integer
						.valueOf(respMap.get("rsCode").toString());
				if (rsCode == 0) {
					res = 0;
					if (Integer.valueOf(type_count.get(
							ReceiverEnum.YYS.getCode()).toString()) <= 0) {
						type_count.put(ReceiverEnum.YYS.getCode(), 1);
					}
				} else {
					res = 1;
				}
				index = 11;
			}// CPAB事前接口 13 RS（人社） CPAB
			else if (interCode.equals(InterfaceEnum.CPAB.getCode())) {
				url = env.getProperty("CPAB.url");
				req.put("perCardID", id_no);// 身份证号 perCardID
				Map<String, Object> sbkjbxx = (Map<String, Object>) inParam
						.get("SBKJBXX");// 社保卡基本信息
				req.put("siCardNo", sbkjbxx.get("ei_id"));// 社保卡号 siCardNo
				req.put("personID", sbkjbxx.get("person_id"));// 个人编号 personID
				req.put("perName", userInfoMap.get("name"));// 姓名 perName
				req.put("sex", userInfoMap.get("gender"));// 性别 sex
				req.put("age", userInfoMap.get("age"));// 年龄 age
				Map<String, Object> sbkztxx = (Map<String, Object>) inParam
						.get("SBKZTXX");// 社保卡状态信息
				req.put("zhsybz", sbkztxx.get("cardStatus"));// 账户使用标志 zhsybz
				Map<String, Object> groupListMap = (Map<String, Object>) inParam
						.get("groupListMap");// IPFS处方信息
				List<Map<String, Object>> groupList = (List<Map<String, Object>>) groupListMap
						.get("groupList");// 处方信息
				res = 0;
				if (null != groupList && groupList.size() > 0) {
					JSONObject orderExtendInfo = null;
					List<Map<String, Object>> mxList = null;
					for (Map<String, Object> group : groupList) {
						orderExtendInfo = new JSONObject();
						Double totalPayMoney = 0.00;
						orderExtendInfo.put("doctorFlowId", group.get("group"));// 门诊流水号doctorFlowId
						orderExtendInfo.put("fixedHospCode",
								group.get("hosp_id"));// 定点医院编码 fixedHospCode
						orderExtendInfo.put("visDate",
								group.get("treatment_date"));// 就诊日期 visDate
						orderExtendInfo.put(
								"ylType",
								group.get("med_type") == null ? "" : group.get(
										"med_type").toString());// 医疗类别ylType
						orderExtendInfo.put(
								"mxbID",
								group.get("dis_code") == null ? "" : group.get(
										"dis_code").toString());// 慢性病编码
						// mxbID
						orderExtendInfo.put("jhsyssType", group.get("op_type"));// 计生手术类别
																				// jhsyssType
						mxList = (List<Map<String, Object>>) group.get("mx");// 处方详细内容

						List<JSONObject> busiOrderDetailInfoList = new ArrayList<JSONObject>();
						if (null != mxList && mxList.size() > 0) {
							JSONObject busiOrderDetailInfo = null;

							for (Map<String, Object> mx : mxList) {
								orderExtendInfo.put("jdjbID",
										mx.get("prediagicd"));// 诊断疾病编号 jdjbID
								orderExtendInfo.put("jdjbName",
										mx.get("prediagname"));// 诊断疾病名称jdjbName
								orderExtendInfo.put("officeName",
										mx.get("deptname"));// 科室名称officeName
								orderExtendInfo.put("doctorFlowId",
										mx.get("doctorid"));// 医生编号 doctorID
								orderExtendInfo.put("doctorName",
										mx.get("doctorname"));// 医生姓名doctorName
								orderExtendInfo.put("treatmentCostType",
										mx.get("treatmentcosttype"));// 类型treatmentCostType

								busiOrderDetailInfo = new JSONObject();
								busiOrderDetailInfo.put("cfID", mx.get("id"));// 处方IDfID
								busiOrderDetailInfo.put("cfDate",
										mx.get("treatmentdate"));// 处方日期 cfDate
								busiOrderDetailInfo.put("hospSfType",
										mx.get("itemtype"));// 医院收费类别hospSfType
								busiOrderDetailInfo.put("mediSfType",
										mx.get("siitemtype"));// 医保收费类别mediSfType
								busiOrderDetailInfo.put("hospProjCode",
										mx.get("hospitemid"));// 医院项目编码
																// hospProjCode
								busiOrderDetailInfo.put("mediProjCode",
										mx.get("siitemid"));// 医保项目编码
															// mediProjCode
								busiOrderDetailInfo.put("projectName",
										mx.get("itemname"));// 项目名称 projectName
								busiOrderDetailInfo.put("price",
										mx.get("price"));// 单价 price
								busiOrderDetailInfo.put("num", mx.get("num"));// 数量
																				// num
								busiOrderDetailInfo
										.put("sum", mx.get("amount"));// 金额 sum
								totalPayMoney = totalPayMoney
										+ Double.valueOf(mx.get("amount")
												.toString());
								busiOrderDetailInfo.put("unit",
										mx.get("measunit"));// 单位 unit
								busiOrderDetailInfo
										.put("norms", mx.get("spec"));// 规格
																		// norms
								busiOrderDetailInfo.put("dosage",
										mx.get("dosageforms"));// 剂型 dosage
								busiOrderDetailInfo.put("dose", mx.get("dose"));// 每次用量
																				// dose
								busiOrderDetailInfo.put("frequency",
										mx.get("usefreq"));// 执行频次 frequency
								busiOrderDetailInfoList
										.add(busiOrderDetailInfo);
							}
						}
						orderExtendInfo.put("busiOrderDetailInfo",
								busiOrderDetailInfoList);
						req.put("orderExtendInfo", orderExtendInfo);// 订单扩展信息
						req.put("totalPayMoney", totalPayMoney);
						// orderExtendInfo

						String resJson = HttpJsonBlockListClient.requestNeo(
								url, req.toJSONString());
						log.info(validKey + " CPAB 接口响应：" + resJson);
						respMap = (Map) JSONObject.parseObject(resJson);
						Integer code = respMap.get("code") == null ? 0
								: Integer.valueOf(respMap.get("code")
										.toString());

						if (code == 0) {
							res = 0;
							if (Integer.valueOf(type_count.get(
									ReceiverEnum.RS.getCode()).toString()) <= 0) {
								type_count.put(ReceiverEnum.RS.getCode(), 1);
							}
							continue;
						} else {
							res = 1;
							break;
						}
					}
				}
				index = 12;
			}
			resSb = resSb.replace(index - 1, index, String.valueOf(res));
		} catch (Exception e) {

			TblInterfaceHis interfaceHis = new TblInterfaceHis(type, id_no,
					interCode, "", DateUtil.getNow(),
					inParam.get("validKey") == null ? "" : inParam.get(
							"validKey").toString());
			interfaceHisList.add(interfaceHis);

			log.error(
					validKey + " 调用 judge " + interCode
							+ InterfaceEnum.getName(interCode) + " error "
							+ e.getMessage(), e);

			resSb = resSb.replace(index - 1, index, "3");
			return 3;
		}

		TblInterfaceHis interfaceHis = new TblInterfaceHis(type, id_no,
				interCode, JSONObject.toJSONString(respMap).toString(),
				DateUtil.getNow(), inParam.get("validKey") == null ? ""
						: inParam.get("validKey").toString());
		interfaceHisList.add(interfaceHis);

		return res;
	}

	/**
	 * 获取分账单
	 * 
	 * @param interCode
	 * @param inParam
	 * @param interfaceHisList
	 * @param env
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map<String, Object>> getPointsBills(
			Map<String, Object> inParam, Environment env,
			List<TblInterfaceHis> interfaceHisList) {
		String url = env.getProperty("YBYJS.url");// 医保预结算接口

		String type = inParam.get("type") == null ? null : inParam.get("type")
				.toString();// 行业
		String id_no = inParam.get("ID") == null ? null : inParam.get("ID")
				.toString();
		String validKey = inParam.get("validKey") == null ? "" : inParam.get(
				"validKey").toString();

		Map<String, Object> groupListMap = (Map<String, Object>) inParam
				.get("groupListMap");// IPFS处方信息
		List<Map<String, Object>> groupList = (List<Map<String, Object>>) groupListMap
				.get("groupList");// 处方信息

		List<Map<String, Object>> pointsBills = new ArrayList<Map<String, Object>>();// 分账单
		TblInterfaceHis interfaceHis = null;
		if (null != groupList && groupList.size() > 0) {
			JSONObject orderExtendInfo = null;
			JSONObject req = null;
			List<Map<String, Object>> mxList = null;

			Map<String, Object> sbkjbxx = (Map<String, Object>) inParam
					.get("SBKJBXX");// 社保卡基本信息
			Map<String, Object> userInfoMap = (Map<String, Object>) ((Map<String, Object>) inParam
					.get("userInfoMap")).get("userInfo");// IPFS用户基本信息
			Map<String, Object> sbkztxx = (Map<String, Object>) inParam
					.get("SBKZTXX");// 社保卡状态信息

			String presgroupid = "";// 处方ID
			for (Map<String, Object> group : groupList) {
				presgroupid = group.get("presgroupid") == null ? "" : group
						.get("presgroupid").toString();

				req = new JSONObject();
				req.put("name", userInfoMap.get("name"));// 姓名 name
				req.put("perCardID", userInfoMap.get("id_no"));// 身份证号 perCardID
				req.put("cardID", sbkjbxx.get("ei_id"));// 社保卡号 cardID
				req.put("zhsybz", sbkztxx.get("cardStatus"));// 账户使用标志 zhsybz

				orderExtendInfo = new JSONObject();
				Double totalMoney = 0.00;
				orderExtendInfo.put("doctorFlowId", group.get("group"));// 门诊流水号doctorFlowId
				orderExtendInfo.put("fixedHospCode", group.get("hosp_id"));// 定点医院编码fixedHospCode
				orderExtendInfo.put("visDate", group.get("treatment_date"));// 就诊日期visDate
				orderExtendInfo.put("ylType", group.get("med_type"));// 医疗类别ylType
				orderExtendInfo.put("mxbID", group.get("dis_code"));// 慢性病编码
																	// mxbID
				orderExtendInfo.put("jhsyssType", group.get("op_type"));// 计生手术类别jhsyssType

				mxList = (List<Map<String, Object>>) group.get("mx");// 处方详细内容
				if (null != mxList && mxList.size() > 0) {
					List<JSONObject> busiOrderDetailInfoList = new ArrayList<JSONObject>();
					JSONObject busiOrderDetailInfo = null;
					for (Map<String, Object> mx : mxList) {

						orderExtendInfo.put("jdjbID", mx.get("prediagicd"));// 诊断疾病编号
																			// jdjbID
						orderExtendInfo.put("jdjbName", mx.get("prediagname"));// 诊断疾病名称
																				// jdjbName
						orderExtendInfo.put("officeName", mx.get("deptname"));// 科室名称
																				// officeName
						orderExtendInfo.put("doctorFlowId", mx.get("doctorid"));// 医生编号
																				// doctorID
						orderExtendInfo.put("doctorName", mx.get("doctorname"));// 医生姓名
																				// doctorName
						orderExtendInfo.put("treatmentCostType",
								mx.get("treatmentcosttype"));// 类型
																// treatmentCostType

						busiOrderDetailInfo = new JSONObject();
						busiOrderDetailInfo
								.put("cfID", mx.get("id").toString());// 处方IDcfID
						busiOrderDetailInfo.put("cfDate",
								mx.get("treatmentdate"));// 处方日期 cfDate
						busiOrderDetailInfo.put("hospSfType",
								mx.get("itemtype"));// 医院收费类别hospSfType
						busiOrderDetailInfo.put("mediSfType",
								mx.get("siitemtype"));// 医保收费类别mediSfType
						busiOrderDetailInfo.put("hospProjCode",
								mx.get("hospitemid"));// 医院项目编码 hospProjCode
						busiOrderDetailInfo.put("mediProjCode",
								mx.get("siitemid"));// 医保项目编码 mediProjCode
						busiOrderDetailInfo.put("projectName",
								mx.get("itemname"));// 项目名称 projectName
						busiOrderDetailInfo.put("price", mx.get("price"));// 单价price
						busiOrderDetailInfo.put("num", mx.get("num"));// 数量num
						busiOrderDetailInfo.put("sum", mx.get("amount"));// 金额sum
						Double amount = Double.valueOf(mx.get("amount")
								.toString());
						totalMoney = totalMoney + amount;
						busiOrderDetailInfo.put("unit", mx.get("measunit"));// 单位unit
						busiOrderDetailInfo.put("norms", mx.get("spec"));// 规格norms
						busiOrderDetailInfo
								.put("dosage", mx.get("dosageforms"));// 剂型dosage
						busiOrderDetailInfo.put("dose", mx.get("dose"));// 每次用量dose
						busiOrderDetailInfo.put("frequency", mx.get("usefreq"));// 执行频次frequency
						busiOrderDetailInfoList.add(busiOrderDetailInfo);
					}
					orderExtendInfo.put("busiOrderDetailInfo", JSONObject
							.parseArray(JSON
									.toJSONString(busiOrderDetailInfoList)));
				}
				req.put("totalPayMoney", totalMoney.toString());
				req.put("orderExtendInfo", orderExtendInfo);
				log.info("YBYJS 接口请求：" + req.toJSONString());
				String resJson = HttpJsonBlockListClient.requestNeo(url,
						req.toJSONString());
				log.info(validKey + " YBYJS 接口响应：" + resJson);
				Map respMap = (Map) JSONObject.parseObject(resJson);
				respMap.put("presgroupid", presgroupid);
				pointsBills.add(respMap);

				interfaceHis = new TblInterfaceHis(type, id_no, "YBYJS",
						JSONObject.toJSONString(respMap).toString(),
						DateUtil.getNow(), inParam.get("validKey") == null ? ""
								: inParam.get("validKey").toString());
				interfaceHisList.add(interfaceHis);
			}
		}

		return pointsBills;
	}

	public static void main(String[] args) {
		String url = "http://113.6.246.26:49967/ebaoAPI/api/insurance/getUserImageSB.do";
		JSONObject req = new JSONObject();
		req.put("certNo", "410205197204080024");
		req.put("realName", "常爱梅");
		String resJson = HttpUtil.sendPost(url, req.toJSONString());
		System.out.println(resJson);
	}

}
