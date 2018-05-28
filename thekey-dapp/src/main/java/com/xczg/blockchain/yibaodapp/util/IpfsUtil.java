package com.xczg.blockchain.yibaodapp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.xczg.blockchain.yibaodapp.bean.TblInterfaceHis;
import com.hollycrm.hollyuniproxy.test.HttpJsonBlockListClient;

public class IpfsUtil {

	private static Logger log = LoggerFactory.getLogger(IpfsUtil.class);

	/**
	 * IPFS取用户信息
	 * 
	 * @return
	 */
	public static Map<String, Object> getUserInformation(String url,
			String ipfsHash, String type) {
		JSONObject req = new JSONObject();
		req.put("hashId", ipfsHash);
		Map<String, Object> respMap = new HashMap<String, Object>();
		try {
			req.put("infoType", type);
			String resJson = HttpJsonBlockListClient.requestNeo(url,
					req.toJSONString());
			String rsJsonString = JSONObject.parseObject(resJson)
					.get("rsJsonString").toString();
			respMap = (Map<String, Object>) JSONObject
					.parseObject(rsJsonString);

		} catch (Exception e) {
			log.error("IPFS infoType：" + type + "获取信息失败 " + e.getMessage(), e);
		}
		return respMap;

	}

	/**
	 * IPFS存验证结果信息 IPFS反馈目录里要存请求ID、每个接口的反馈详情、每笔处方的分账单
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String putDmiResult(String url, String ipfsHash,
			String validID, List<Map<String, Object>> pointsBills,
			List<TblInterfaceHis> interfaceHisList, String resTotal) {

		Map<String, Object> req = new HashMap<String, Object>();
		req.put("hashId", ipfsHash);
		Map<String, Object> dmiResult = new HashMap<String, Object>();
		req.put("dmiResult", dmiResult);
		Map<String, Object> resultInfo = new HashMap<String, Object>();
		resultInfo.put("validID", validID);
		resultInfo.put("pointsBills", pointsBills);
		resultInfo.put("interfaceHisList", interfaceHisList);
		resultInfo.put("resTotal", resTotal);
		dmiResult.put("resultInfo", resultInfo);
		String reqJson = JSONObject.toJSONString(req).toString();
		log.info("Ipfs putDmiResult 请求：" + reqJson);
		String resJson = HttpJsonBlockListClient.requestNeo(url, reqJson);
		log.info("Ipfs putDmiResult 响应：" + resJson);
		Map respMap = (Map) JSONObject.parseObject(resJson);
		String newHash = respMap.get("newHash") == null ? "" : respMap.get(
				"newHash").toString();

		return newHash;

	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static String readFileByLines(String fileName) {
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				sb.append(new String(tempString.getBytes(), "UTF-8"));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}

}
