package com.xczg.blockchain.yibaodapp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;
import com.hollycrm.hollyuniproxy.test.HttpJsonBlockListClient;

public class ChainUtil {

	private static Logger log = LoggerFactory.getLogger(ChainUtil.class);

	/**
	 * 验证合约 获取行业预判接口名集合
	 * 
	 * @param industry
	 * @param industryCode行业编码
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> getindustryInterfaces(String hash,
			String neoUrl, String industryCode) {
		List<String> list = new ArrayList<String>();
		try {
			Map<String, Object> inParam = new HashMap<String, Object>();
			inParam.put("jsonrpc", "2.0");
			inParam.put("method", "invokefunction");
			inParam.put("id", "3");
			Object paramsArr[] = new Object[3];
			paramsArr[0] = hash;
			// 测试链
			// paramsArr[0] = "0x33ee36c712b37df8acfbda4a1beb165e100ed3e0";
			// 私链
			// paramsArr[0] = "0x6bd3f062a37ac2b19d6f06a819f9b1f2464bbca4";
			paramsArr[1] = "getRule";
			Map<String, Object> inParam3 = new HashMap<String, Object>();
			inParam3.put("type", "String");
			inParam3.put("value", industryCode);
			List<Map<String, Object>> inList = new ArrayList<Map<String, Object>>();
			inList.add(inParam3);
			paramsArr[2] = inList;
			inParam.put("params", paramsArr);

			// 调用合约接口，获取数据
			String inJson = JSONObject.toJSONString(inParam).toString();
			log.info("获取行业预判接口请求" + inJson);
			String outJson = HttpJsonBlockListClient.requestNeo(neoUrl, inJson);
			log.info("获取行业预判接口响应" + outJson);
			Map respMap = (Map) JSONObject.parseObject(outJson);
			Map<String, Object> respMap1 = (Map<String, Object>) respMap
					.get("result");
			List<Map<String, Object>> respList2 = (List<Map<String, Object>>) respMap1
					.get("stack");
			if (respList2 != null && respList2.size() > 0) {
				Map<String, Object> resultMap = respList2.get(0);
				String value = StringUtil.hexStringToString(resultMap.get(
						"value").toString());
				Map<String, Object> value1 = (Map<String, Object>) JSON
						.parse(value);
				String InterfaceOrder = value1.get("InterfaceOrder") == null ? ""
						: value1.get("InterfaceOrder").toString();
				String[] InterfaceOrders = InterfaceOrder.split(",");
				list = Arrays.asList(InterfaceOrders);
			}

		} catch (Exception e) {
			log.error("获取行业预判接口名集合 error" + e.getMessage(), e);
			return null;
		}
		return list;
	}

	/**
	 * 调用区块链,通过validKey获取ipfsHash
	 * 
	 * @param key
	 * @return
	 */
	public static String save(String gbUrl, String value) {
		StringBuffer sb = new StringBuffer();
		sb.append("method:save").append(",value:").append(value);
		String chainKey = "";
		try {
			String respMsg = HttpJsonBlockListClient.requestNeo(gbUrl,
					sb.toString());
			chainKey = respMsg.substring(respMsg.indexOf("chainKey:") + 9,
					respMsg.length());
		} catch (Exception e) {
			log.error("调取区块链接口 error" + e.getMessage(), e);
		}
		return chainKey;
	}

	/**
	 * 调用支付合约,通过validKey获取ipfsHash
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getZZ(String hash, String neoUrl, String key) {
		Object value = null;
		int time = 0;
		try {
			Map<String, Object> inParam = new HashMap<String, Object>();
			inParam.put("jsonrpc", "2.0");
			inParam.put("method", "invokefunction");
			inParam.put("id", "3");
			Object paramsArr[] = new Object[3];
			paramsArr[0] = hash;
			// 私链
			// paramsArr[0] = "0xcf5b19f1fba56d890742840b29b5203b33faedfc";
			// 测试链
			// paramsArr[0] = "0x2e05dc2c1d5780b3b7698944d0a59b3dc51efde3";
			paramsArr[1] = "get";
			Map<String, Object> map_a1 = new HashMap<String, Object>();
			map_a1.put("type", "String");
			map_a1.put("value", key);
			List<Map<String, Object>> list_b1 = new ArrayList<Map<String, Object>>();
			list_b1.add(map_a1);
			paramsArr[2] = list_b1;
			inParam.put("params", paramsArr);
			String inJson = JSONObject.toJSONString(inParam).toString();
			long startTime = new Date().getTime();
			log.info("getZZ inJson (" + startTime + ")" + inJson);
			long endTime = 0;
			String outJson = "";
			while (value == null || value.toString().isEmpty()) {
				outJson = HttpJsonBlockListClient.requestNeo(neoUrl, inJson);

				Map respMap = (Map) JSONObject.parseObject(outJson);
				Map<String, Object> respMap1 = (Map<String, Object>) respMap
						.get("result");
				List<Map<String, Object>> respList2 = (List<Map<String, Object>>) respMap1
						.get("stack");
				if (respList2 != null && respList2.size() > 0) {
					Map<String, Object> resultMap = respList2.get(0);
					value = resultMap.get("value");

					if (value == null || value.toString().isEmpty()) {
						Thread.sleep(1000);
						time++;
						if (time >= 60) {
							break;
						}
					} else {
						endTime = new Date().getTime();
					}
				}
			}
			log.info("getZZ outJson (" + (endTime - startTime) + ")" + inJson);

		} catch (Exception e) {
			log.error("调用区块链,通过validKey获取ipfsHash 失败 " + e.getMessage(), e);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			getZZ(hash, neoUrl, key);

		}
		return StringUtil.hexStringToString(value == null ? "" : value
				.toString());
	}

	/**
	 * 调用区块链,通过validKey获取ipfsHash
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String get(String hash, String neoUrl, String key) {
		Object value = null;
		int time = 1;

		Map<String, Object> inParam = new HashMap<String, Object>();
		inParam.put("jsonrpc", "2.0");
		inParam.put("method", "invokefunction");
		inParam.put("id", "3");
		Object paramsArr[] = new Object[3];
		paramsArr[0] = hash;
		// 测试链
		// paramsArr[0] = "0x33ee36c712b37df8acfbda4a1beb165e100ed3e0";
		// 私链
		// paramsArr[0] = "0x6bd3f062a37ac2b19d6f06a819f9b1f2464bbca4";
		paramsArr[1] = "get";
		Map<String, Object> map_a1 = new HashMap<String, Object>();
		map_a1.put("type", "String");
		map_a1.put("value", key);
		List<Map<String, Object>> list_b1 = new ArrayList<Map<String, Object>>();
		list_b1.add(map_a1);
		paramsArr[2] = list_b1;
		inParam.put("params", paramsArr);
		String inJson = JSONObject.toJSONString(inParam).toString();
		log.info("get inJson :" + inJson);

		String outJson = "";
		while (value == null || value.toString().isEmpty()) {
			try {
				outJson = HttpJsonBlockListClient.requestNeo(neoUrl, inJson);
			} catch (Exception e) {
				log.error("调用区块链,通过validKey获取ipfsHash 失败 " + e.getMessage(), e);

			}
			Map respMap = (Map) JSONObject.parseObject(outJson);
			Map<String, Object> respMap1 = (Map<String, Object>) respMap
					.get("result");
			List<Map<String, Object>> respList2 = (List<Map<String, Object>>) respMap1
					.get("stack");
			if (respList2 != null && respList2.size() > 0) {
				Map<String, Object> resultMap = respList2.get(0);
				value = resultMap.get("value");

				if (value == null || value.toString().isEmpty()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.error(
								"调用区块链,通过validKey获取ipfsHash  sleep失败 "
										+ e.getMessage(), e);
					}
					time++;
					if (time >= 60) {
						break;
					}
				}
			}
		}
		log.info("get outJson (" + time + "s后):" + outJson);

		return StringUtil.hexStringToString(value == null ? "" : value
				.toString());
	}

	/**
	 * 调用支付合约 {"RS"："2","GA":"1","YYS":"1"}
	 * 
	 * @param key
	 * @return
	 */
	public static String tranfer(String hash, String neoUrl, String gbUrl,
			JSONObject json, List<TblTransactionInfo> tblTransactionInfoList,
			String validKey, String serialNo) {

		StringBuffer sb = new StringBuffer();

		sb.append("method:allotTKY")
				.append(",yb:")
				.append(json.get(ReceiverEnum.YB.getCode()) == null ? 0 : json
						.get(ReceiverEnum.YB.getCode()))
				.append(",yys:")
				.append(json.get(ReceiverEnum.YYS.getCode()) == null ? 0 : json
						.get(ReceiverEnum.YYS.getCode()));

		String chainKey = "";
		String txid = "";
		try {
			log.info("tranfer inJson :" + sb.toString());
			String respMsg = HttpJsonBlockListClient.requestNeo(gbUrl,
					sb.toString());
			int time = 1;
			while (respMsg == null || respMsg.isEmpty()) {
				respMsg = HttpJsonBlockListClient.requestNeo(gbUrl,
						sb.toString());
				if (respMsg == null || respMsg.isEmpty()) {
					Thread.sleep(1000);
				}
				time++;
				if (time >= 60) {
					log.info("tranfer 超时30s 失败，终止调用");
					break;
				}
			}
			log.info("tranfer outJson :" + respMsg);

			chainKey = respMsg.substring(respMsg.indexOf("chainKey:") + 9,
					respMsg.indexOf(",txid:"));
			txid = respMsg.substring(respMsg.indexOf("txid:") + 5,
					respMsg.length());

			String value = getZZ(hash, neoUrl, chainKey);

			// 记录交易信息
			JSONArray items = JSONObject.parseArray(value);
			TblTransactionInfo tblTransactionInfo = null;
			JSONObject item = null;
			if (null != items && items.size() > 0) {
				for (int i = 0; i < items.size(); i++) {
					item = (JSONObject) items.get(i);
					tblTransactionInfo = new TblTransactionInfo(txid, "支付合约",
							ReceiverEnum.getName(item.getString("type")),
							item.getString("from"), item.getString("to"), item
									.getString("Fee").trim(),
							DateUtil.getNow(), validKey, serialNo, "积分流转");
					tblTransactionInfoList.add(tblTransactionInfo);
				}
			}
		} catch (Exception e) {
			log.error("调取支付合约接口 error " + e.getMessage(), e);
		}

		return chainKey;

	}

	/**
	 * 将验证结果上链
	 * 
	 * @param key
	 * @return
	 */
	public static Map saveResult(String gbUrl, String validKey, String result) {
		StringBuffer sb = new StringBuffer();
		sb.append("method:saveResult,key:").append(validKey).append(",value:")
				.append(result);
		String chainKey = "";
		String txid = "";
		Map<String, Object> resp = new HashMap<String, Object>();
		try {
			log.info("saveResult inJson:" + sb.toString());
			String respMsg = HttpJsonBlockListClient.requestNeo(gbUrl,
					sb.toString());

			int time = 1;
			while (respMsg == null || respMsg.isEmpty()) {
				respMsg = HttpJsonBlockListClient.requestNeo(gbUrl,
						sb.toString());
				if (respMsg == null) {
					Thread.sleep(1000);
				}
				time++;
				if (time >= 40) {
					log.info("saveResult 超时30s 失败,终止调用");
					break;
				}
			}
			log.info("saveResult outJson:" + respMsg);

			chainKey = respMsg.substring(respMsg.indexOf("chainKey:") + 9,
					respMsg.indexOf(",txid:"));
			txid = respMsg.substring(respMsg.indexOf("txid:") + 5,
					respMsg.length());

			resp.put("chainKey", chainKey);
			resp.put("txid", txid);
		} catch (Exception e) {
			log.error("调取区块链接口 error" + e.getMessage(), e);
		}
		return resp;
	}

	// 根据地址和参数查询区块信息
	public Map<String, Object> getBlockMessage(String neoUrl, String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String respMsg = HttpJsonBlockListClient
					.requestNeo(neoUrl, jsonStr);
			Map<String, Object> respMap = JSONObject.parseObject(respMsg);
			return respMap;
		} catch (Exception e) {
			log.error("调取区块链接口 error" + e.getMessage(), e);
			return map;
		}
	}

}
