package com.xczg.blockchain.yibaodapp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.xczg.blockchain.yibaodapp.bean.BlockChainResultEntity;
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;
import com.xczg.blockchain.yibaodapp.service.ITransactionInfoService;
import com.hollycrm.hollyuniproxy.test.HttpJsonBlockListClient;

public class ChainUtil {
	@Autowired
	private ITransactionInfoService transactionInfoService;
	private static Environment env;
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
		//list.add("SBKJBXX,SBKTXXX,SBKZTXX,RLSB,GPS,SWPD,RXPD,ZTPW,CPAB,TSZD,TSRY");
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
				Map<String, Object> respMap1 = (Map<String, Object>) respMap.get("result");
				List<Map<String, Object>> respList2 = (List<Map<String, Object>>) respMap1.get("stack");
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
			// getZZ(hash, neoUrl, key);

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
	//转账给address
	public static String tranfer1(String address,String count,String url) {
		Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "sendtoaddress");
        inParam.put("id", "1");
        Object paramsArr[] = new Object[3];
        paramsArr[0] = "0x132947096727c84c7f9e076c90f08fec3bc17f18";
        paramsArr[1] = address;
        paramsArr[2] = count;
        inParam.put("params",paramsArr);
        try {
            String inJson = JSONObject.toJSONString(inParam);
            //String response = HttpUtils.sendPOST(applicationConfig.getNeoCliUrl(), inJson);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(url, inJson, String.class);
            JSONObject jsObj = JSONObject.parseObject(response);
            if(jsObj != null && jsObj.containsKey("result")) {
                JSONObject resObj = jsObj.getJSONObject("result");
                if(resObj.containsKey("txid") && !resObj.getString("txid").equals("")) {
                	log.info(address+"zzzzz交易成功，交易id："+resObj.getString("txid"));
                	return resObj.getString("txid");
                }
            }
        }catch(Exception e){
        	log.info(address+"交易失败"+e);
        }
        return inParam.toString();
 }
	public static String tranfer2(String address,String url) {
		Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "sendtoaddress");
        inParam.put("id", "1");
        Object paramsArr[] = new Object[3];
        paramsArr[0] = "0x132947096727c84c7f9e076c90f08fec3bc17f18";
        paramsArr[1] = address;
        paramsArr[2] = "3";
        inParam.put("params",paramsArr);
        try {
            String inJson = JSONObject.toJSONString(inParam);
            //String response = HttpUtils.sendPOST(applicationConfig.getNeoCliUrl(), inJson);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(url, inJson, String.class);
            JSONObject jsObj = JSONObject.parseObject(response);
            if(jsObj != null && jsObj.containsKey("result")) {
                JSONObject resObj = jsObj.getJSONObject("result");
                if(resObj.containsKey("txid") && !resObj.getString("txid").equals("")) {
                	log.info("idv交易成功，交易id："+resObj.getString("txid"));
                	return resObj.getString("txid");
                }
            }
        }catch(Exception e){
           
        }
        return inParam.toString();
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

		return "";
		//
		//		StringBuffer sb = new StringBuffer();
		//
		//		sb.append("method:allotTKY")
		//				.append(",yb:")
		//				.append(json.get(ReceiverEnum.YB.getCode()) == null ? 0 : json
		//						.get(ReceiverEnum.YB.getCode()))
		//				.append(",yys:")
		//				.append(json.get(ReceiverEnum.YYS.getCode()) == null ? 0 : json
		//						.get(ReceiverEnum.YYS.getCode()));
		//
		//		String chainKey = "";
		//		String txid = "";
		//		try {
		//			log.info("tranfer inJson :" + sb.toString());
		//			String respMsg = HttpJsonBlockListClient.requestNeo(gbUrl,
		//					sb.toString());
		//			int time = 1;
		//			while (respMsg == null || respMsg.isEmpty()) {
		//				respMsg = HttpJsonBlockListClient.requestNeo(gbUrl,
		//						sb.toString());
		//				if (respMsg == null || respMsg.isEmpty()) {
		//					Thread.sleep(1000);
		//				}
		//				time++;
		//				if (time >= 60) {
		//					log.info("tranfer 超时30s 失败，终止调用");
		//					break;
		//				}
		//			}
		//			log.info("tranfer outJson :" + respMsg);
		//
		//			chainKey = respMsg.substring(respMsg.indexOf("chainKey:") + 9,
		//					respMsg.indexOf(",txid:"));
		//			txid = respMsg.substring(respMsg.indexOf("txid:") + 5,
		//					respMsg.length());
		//
		//			String value = getZZ(hash, neoUrl, chainKey);
		//
		//			// 记录交易信息
		//			JSONArray items = JSONObject.parseArray(value);
		//			TblTransactionInfo tblTransactionInfo = null;
		//			JSONObject item = null;
		//			if (null != items && items.size() > 0) {
		//				for (int i = 0; i < items.size(); i++) {
		//					item = (JSONObject) items.get(i);
		//					tblTransactionInfo = new TblTransactionInfo(txid, "支付合约",
		//							ReceiverEnum.getName(item.getString("type")),
		//							item.getString("from"), item.getString("to"), item
		//									.getString("Fee").trim(),
		//							DateUtil.getNow(), validKey, serialNo, "积分流转");
		//					tblTransactionInfoList.add(tblTransactionInfo);
		//				}
		//			}
		//		} catch (Exception e) {
		//			log.error("调取支付合约接口 error " + e.getMessage(), e);
		//		}
		//
		//		return chainKey;
		//
	}

	/**
	 * 将验证结果上链
	 * 
	 * @param key
	 * @return
	 */
	public static Map saveResult(String gbUrl, String validKey, String result) {
		//		String privateKey=env.getProperty("privateKey");
		//		String sciptHash=env.getProperty("sciptHash");
		String sciptHash="0x6bd3f062a37ac2b19d6f06a819f9b1f2464bbca4";
		String privateKey="KysSjeqG1Q8AXWxJGKFKzMvdAkjubLBw8MS3kCAcoto89DAY4GYu";
		//		if ( sciptHash != null && sciptHash.indexOf("(") != -1 ) {
		//			sciptHash=sciptHash.substring(1, sciptHash.length()-1);
		//		}
		StringBuffer sb = new StringBuffer();
		sb.append("method:saveResult,key:").append(validKey).append(",value:")
		.append(result).append(",privateKey:").append(privateKey).append(",sciptHash:").append(sciptHash);
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
	public static Map<String, Object> getBlockMessage(String neoUrl, String jsonStr) {
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
	//通用信息上链
	public static BlockChainResultEntity saveToBlockchain(String url,String result,String method) {
			try {
			String jsonStr = "method:"+method+",value:" + result;
			log.info("调用区块链, 存储IPFS哈希, 调用参数:{}",jsonStr);
			RestTemplate restTemplate = new RestTemplate();
	        String respMsg = restTemplate.postForObject(url, jsonStr, String.class);
			int time = 1;
			while (respMsg == null || respMsg.isEmpty()) {
				respMsg = HttpJsonBlockListClient.requestNeo(url,
						jsonStr);
				if (respMsg == null) {
					Thread.sleep(1000);
				}
				time++;
				if (time >= 40) {
					log.info("saveResult 超时30s 失败,终止调用");
					break;
				}
			}
			if(respMsg.isEmpty()) {
				log.error("saveResult outJson为空，存储失败:" + respMsg);
			}
			log.info("saveResult outJson:" + respMsg);
			BlockChainResultEntity resultEntity=new BlockChainResultEntity();
			String chainKey = respMsg.substring(respMsg.indexOf("chainKey:") + 9,
					respMsg.indexOf(",txid:"));
			String txid = respMsg.substring(respMsg.indexOf("txid:") + 5,
					respMsg.length());
			resultEntity.setValidKey(chainKey);
			resultEntity.setTxid(txid);
			return resultEntity;
		} catch (Exception e) {
			log.error("saveResult outJson为空，存储失败");
			return null;
		}
	}
	 
//	public static BlockChainResultEntity saveBlock(String url, String hash,String type)
//	{
//		BlockChainResultEntity entity=new BlockChainResultEntity();
//		String nep5assetid="0x132947096727c84c7f9e076c90f08fec3bc17f18";
//		Map<String, Object> inParam = new HashMap<String, Object>();
//		inParam.put("jsonrpc", "2.0");
//		inParam.put("method", "sendtoaddress"); 
//		inParam.put("id", "1");
//		Object paramsArr[] = new Object[3];
//		//paramsArr[0] = applicationConfig.getTkyAssetId();
//		paramsArr[0] = nep5assetid;
//		paramsArr[1] = "AQPidazCXeFnRNh7xgGeqH7BhMKLjU3MWM";
//		paramsArr[2] = "5";
//		inParam.put("params",paramsArr);
//		try {
//			String inJson = JSONObject.toJSONString(inParam);
//			//String response = HttpUtils.sendPOST(applicationConfig.getNeoCliUrl(), inJson);
//			RestTemplate restTemplate = new RestTemplate();
//			String response = restTemplate.postForObject(url, inJson, String.class);
//			JSONObject jsObj = JSONObject.parseObject(response);
//			if(jsObj != null && jsObj.containsKey("result")) {
//				JSONObject resObj = jsObj.getJSONObject("result");
//				if(resObj.containsKey("txid") && !resObj.getString("txid").equals("")) {
//					//return resObj.get("txid").toString();
//					entity.setTxid(resObj.get("txid").toString());
//					entity.setValidKey(getValiyKey());
//					log.info("slslsl交易成功，，交易ID为："+resObj.get("txid").toString());
//					return entity;
//				}
//				else {
//					//余额不足的时候会有exception,需测试结果json的内容
//					return null;
//				}
//			}
//		}catch(Exception e){
//			log.error("transfer:消息请求失败！",e);
//		}
//		return null;
//	}

	public static BlockChainResultEntity SaveBlock(String url, String hash,String type)
	{
		BlockChainResultEntity entity=new BlockChainResultEntity();
		try {
			entity.setTxid(getValiyKey());
			entity.setValidKey(getValiyKey());
			return entity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

public static String bytesToHex(byte[] bytes) {  
    StringBuffer sb = new StringBuffer();  
    for(int i = 0; i < bytes.length; i++) {  
        String hex = Integer.toHexString(bytes[i] & 0xFF);  
        if(hex.length() < 2){  
            sb.append(0);  
        }  
        sb.append(hex);  
    }  
    return sb.toString();  
}

public static String binaryString2hexString(String binaryString) {
    String hexString = "";
    int bit = 0;
    for (int i = 0; i < binaryString.length(); i += 4) {
        bit = 0;
        for (int j = 0; j < 4; j++) {
            String x = binaryString.substring(i + j, i + j + 1);
            bit += Integer.parseInt(x) << (4 - j - 1);
        }
        hexString += Integer.toHexString(bit);
    }
    return hexString.toString();
}

//用validkey到链上取加密的IPFS哈希
public static String getHashFromBlockChain(String validKey)
{
	String url="http://222.128.14.106:3226";
    try {
        Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "invokefunction");
        inParam.put("id", "3");
        Object paramsArr[] = new Object[3];
        paramsArr[0] = "0x6bd3f062a37ac2b19d6f06a819f9b1f2464bbca4";//applicationConfig.getVerifyScriptHash();// verifyScriptHash;//区块hash
        paramsArr[1] = "get";
        JSONObject obj = new JSONObject();
        obj.put("type","String");
        obj.put("value",validKey);
        Object subParamsArr[] = new Object[1];
        subParamsArr[0] = obj;
        paramsArr[2] = subParamsArr;
        inParam.put("params", paramsArr);
        String inJson = JSONObject.toJSONString(inParam);
        System.out.println("下链时传递的参数："+inJson);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url, inJson, String.class);

        JSONObject jsObj = JSONObject.parseObject(response);
        
        System.out.println("response="+response);
        
        if(jsObj != null && jsObj.containsKey("result"))
        {
            JSONObject resObj = jsObj.getJSONObject("result");
            if(resObj!=null && resObj.containsKey("stack")) {
                JSONArray stackArr = resObj.getJSONArray("stack");
                if(stackArr != null && stackArr.size()>0) {
                    JSONObject stackObj = stackArr.getJSONObject(0);
                    if(stackObj!=null && stackObj.containsKey("value")) {
                        String value = stackObj.getString("value");
                        String resultStr = StringUtil.hexStringToString(value);
                        System.out.println("从区块链上获取IPFS哈希：" + resultStr);
                        return resultStr;
                    }
                }
            }
        } else {
        	System.out.println("result is null");
        }
    } catch (Exception e) {
    	 System.out.println("调用区块链,通过validKey获取ipfsHash 失败 " + e.getMessage());
    }
    return "";
}
private static String getBalance()
{
    Map<String, Object> inParam = new HashMap<String, Object>();
    inParam.put("jsonrpc", "2.0");
    inParam.put("method", "getbalance");
    inParam.put("id", "1");
    Object paramsArr[] = new Object[1];
    //paramsArr[0] = applicationConfig.getTkyAssetId();
    paramsArr[0] = "602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7";
    inParam.put("params",paramsArr);

    try {
        String inJson = JSONObject.toJSONString(inParam);
        //String response = HttpUtils.sendPOST(applicationConfig.getNeoCliUrl(), inJson);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject("http://222.128.14.106:3226", inJson, String.class);
        Map respMap = JsonUtils.parseMap(response);
        JSONObject jsObj = JSONObject.parseObject(response);
        if(jsObj != null && jsObj.containsKey("result")) {
            JSONObject resObj = jsObj.getJSONObject("result");
            if (resObj.containsKey("balance")) {
                String balance = resObj.getString("balance");
                log.info("账户余额"+balance);
                return balance;
            }
        }
    }catch(Exception e){
        log.error("getBalance:消息请求失败！",e);
    }
    return "";
}
	public static  String getValiyKey() throws Exception {
		
		return  UUIDUtil.getUUID();
	}
	/**
	 * 上链存储数据
	 * @param url
	 * @param hash
	 * @param type
	 * @return
	 */
	public static BlockChainResultEntity saveBlock(String url, String hash,String type)
	{
		try {
			String sciptHash=env.getProperty("get.hash");
	        String jsonStr = "method:"+type+",value:" + hash+",sciptHash:"+sciptHash;
	        log.info("调用区块链, 存储IPFS哈希, 调用参数:{}", jsonStr);
	        //System.out.println("调用接口发送数据：" + jsonStr);
	        //String url = "http://192.168.99.54:20334";
	        String Url =env.getProperty("saveResult.url");
	        String respMsg = HttpJsonBlockListClient.requestNeo(Url,
	        		jsonStr);

			int time = 1;
			while (respMsg == null || respMsg.isEmpty()) {
				respMsg = HttpJsonBlockListClient.requestNeo(Url,
						jsonStr);
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

			String validKey = respMsg.substring(respMsg.indexOf("chainKey:") + 9,
					respMsg.indexOf(",txid:"));
			String txid = respMsg.substring(respMsg.indexOf("txid:") + 5,
					respMsg.length());
	        BlockChainResultEntity resultEntity = new BlockChainResultEntity();
	        resultEntity.setValidKey(validKey);
	        resultEntity.setTxid(txid);

	        log.debug("调用区块链, 存储IPFS哈希成功，validKey={}, hash={}",validKey, jsonStr );
	        return resultEntity;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
   }
	public static String getBalace() {
		Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "getbalance");
        inParam.put("id", "1");
        Object paramsArr[] = new Object[1];
        paramsArr[0] = "0x132947096727c84c7f9e076c90f08fec3bc17f18";
        //paramsArr[0] = "0x602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7";
        inParam.put("params",paramsArr);  
        
        // 节点地址  AQPidazCXeFnRNh7xgGeqH7BhMKLjU3MWM

        try {
            String inJson = JSONObject.toJSONString(inParam);
            //String response = HttpUtils.sendPOST(applicationConfig.getNeoCliUrl(), inJson);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject("http://222.128.14.106:3226", inJson, String.class);
            System.out.println("返回结果："+response);
            Map respMap = JsonUtils.parseMap(response);
            JSONObject jsObj = JSONObject.parseObject(response);
            if(jsObj != null && jsObj.containsKey("result")) {
                JSONObject resObj = jsObj.getJSONObject("result");
                if (resObj.containsKey("balance")) {
                    String balance = resObj.getString("balance");
                    return balance;
                }
            }
        }catch(Exception e){
            return null;
        }
		return null;
	}
}



