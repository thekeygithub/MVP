package com.xczg.blockchain.yibaodapp.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;

import com.alibaba.fastjson.JSONObject;
import com.xczg.blockchain.yibaodapp.bean.TblInterfaceHis;
import com.xczg.blockchain.yibaodapp.bean.TblTranfer;
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;
import com.xczg.blockchain.yibaodapp.service.IInterfaceHisService;
import com.xczg.blockchain.yibaodapp.service.ITranferService;
import com.xczg.blockchain.yibaodapp.service.ITransactionInfoService;
import com.xczg.blockchain.yibaodapp.util.ChainUtil;
import com.xczg.blockchain.yibaodapp.util.DateUtil;
import com.xczg.blockchain.yibaodapp.util.IpfsUtil;
import com.xczg.blockchain.yibaodapp.util.RSAUtil;
import com.xczg.blockchain.yibaodapp.util.ReceiverEnum;
import com.xczg.blockchain.yibaodapp.util.ThirdInterfaceUtil;
import com.hollycrm.hollyuniproxy.test.HttpJsonBlockListClient;

/**
 * 业务异步执行验证业务<br>
 * 说明： 用于执行业务任务单元,由线程池负责调配<br>
 * 
 */
public class BussinessRunner implements Runnable {

	private static final Logger logger = Logger
			.getLogger(BussinessRunner.class);

	private Map<String, Object> inParam;
	private IInterfaceHisService interfaceHisService;
	private ITranferService tranferService;
	private Environment env;
	private ITransactionInfoService transactionInfoService;

	/**
	 * 执行状态 <li>0 = 执行结束 <li>1 = 正在执行中 <li>2 = 等待执行
	 */
	private String execStatus = "2";

	/**
	 * 执行状态 <li>0 = 执行结束 <li>1 = 正在执行中 <li>2 = 等待执行
	 */
	public String getExecStatus() {
		return execStatus;
	}

	/**
	 * 执行状态 <li>0 = 执行结束 <li>1 = 正在执行中 <li>2 = 等待执行
	 */
	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}

	public BussinessRunner(Map<String, Object> inParam,
			IInterfaceHisService interfaceHisService, Environment env,
			ITranferService tranferService,
			ITransactionInfoService transactionInfoService) {
		this.inParam = inParam;
		this.interfaceHisService = interfaceHisService;
		this.env = env;
		this.tranferService = tranferService;
		this.transactionInfoService = transactionInfoService;
	}

	@Override
	public void run() {

		execStatus = "1";// 标记为执行中

		String validKey = inParam.get("validKey") == null ? "" : inParam.get(
				"validKey").toString();
		try {
			String resTotal = "2222222222222";// 验证总结果(13位)

			List<TblInterfaceHis> interfaceHisList = new ArrayList<TblInterfaceHis>();// 接口调用记录
			List<Map<String, Object>> pointsBills = new ArrayList<Map<String, Object>>();// 分账单

			JSONObject type_count = new JSONObject();

			String saveResultUrl = env.getProperty("saveResult.url");
			String neoUrl = env.getProperty("neo.url");// 区块链调用地址

			// 1．调用区块链,通过validKey获取IpfsHash
			logger.info(validKey + " 区块链validKey：" + validKey);
			String getHash = env.getProperty("get.hash");// 区块链调用地址
			logger.info(validKey + " 区块链getHash：" + getHash);
			String ipfsHash = ChainUtil.get(getHash, neoUrl, validKey);
			logger.info(validKey + " 区块链取到的ipfsHash：" + ipfsHash);
			String ipfsHash2 = "";
			if (null == ipfsHash) {
				resTotal = "3222222222222";// 验证总结果
			} else {
				// 2. 用易保dapp私钥解密ipfsHash
				String yibaoPrivateKey = env.getProperty("yibao.privateKey");
				logger.info(validKey + " 易保私钥yibaoPrivateKey："
						+ yibaoPrivateKey);
				ipfsHash2 = RSAUtil.decryption(yibaoPrivateKey, ipfsHash);
				logger.info(validKey + " 易保私钥解密后的ipfsHash2：" + ipfsHash2);
				if (null == ipfsHash2) {
					resTotal = "3222222222222";// 验证总结果
				} else {
					// 3.调用ipfs接口，通过ipfsHash2获取:1.就诊用户信息，2.处方信息，3.正面照片
					String getUserInformationUrl = env
							.getProperty("ipfs.getUserInformation.url");
					Map<String, Object> userInfoMap = IpfsUtil
							.getUserInformation(getUserInformationUrl,
									ipfsHash2, "1");
					this.inParam.put("userInfoMap", userInfoMap);// IPFS用户基本信息
					logger.info(validKey + " IPFS上取到的用户信息：" + userInfoMap);
					Map<String, Object> groupListMap = IpfsUtil
							.getUserInformation(getUserInformationUrl,
									ipfsHash2, "2");// 处方信息
					this.inParam.put("groupListMap", groupListMap);
					logger.info(validKey + " IPFS上取到的处方信息：" + groupListMap);
					Map<String, Object> imageMap = IpfsUtil.getUserInformation(
							getUserInformationUrl, ipfsHash2, "3");
					this.inParam.put("imageMap", imageMap);
					logger.info(validKey + " IPFS上取到的图片信息：" + imageMap);

					if (null == userInfoMap) {
						resTotal = "3222222222222";// 验证总结果
					} else {
						// 4.调用合约接口，获取该行业需要检测的接口名
						String type = inParam.get("type") == null ? ""
								: inParam.get("type").toString();
						List<String> list = ChainUtil.getindustryInterfaces(
								getHash, neoUrl, type);
						logger.info(validKey + " 行业规则：" + list);
						if (null == list) {
							resTotal = "3222222222222";// 验证总结
						} else {
							// 5. 依次调用验证接口
							// 0-验证通过1-验证失败2-未经过验证3-接口调用失败(其他错误)
							int i = 2;

							type_count.put(ReceiverEnum.RS.getCode(), 0);
							type_count.put(ReceiverEnum.GA.getCode(), 0);
							type_count.put(ReceiverEnum.YYS.getCode(), 0);

							StringBuffer res = new StringBuffer("222222222222");// 接口依次调用结果
							StringBuffer result = new StringBuffer("0");// 首位结果

							if (null != list && list.size() > 0) {
								for (String interCode : list) {// 接口编码/名称
									i = ThirdInterfaceUtil.judge(interCode,
											inParam, interfaceHisList, env,
											type_count, res);
									if (i != 0) {// 首位表示总体验证结果
										result = result.replace(0, 1,
												String.valueOf(i));
										break;
									}
								}
								resTotal = result.append(res).toString();
								logger.info(validKey + " 多维验证最后结果：" + resTotal);
							}
						}
					}

					try {
						// 获取分账单
						pointsBills = ThirdInterfaceUtil.getPointsBills(
								inParam, env, interfaceHisList);
						logger.info(validKey + " 分账单：" + pointsBills);
					} catch (Exception e2) {
						logger.error(
								validKey + " 获取分账单 error " + e2.getMessage(),
								e2);
					}

					// 接口调用信息存数据库
					interfaceHisService.saveList(interfaceHisList);
					logger.info(validKey + " 接口调用历史：" + interfaceHisList);

				}
			}

			// 请求ID，分账单，接口调用明细，存IPFS
			String putDmiResultUrl = env.getProperty("ipfs.putDmiResult.url");
			String newHash = IpfsUtil.putDmiResult(putDmiResultUrl, ipfsHash2,
					validKey, pointsBills, interfaceHisList, resTotal);
			logger.info(validKey + " 请求ID，分账单，接口调用明细，存IPFS：" + newHash);

			// 调用结果（无详情）和反馈目录的哈希一起加密（医院公钥加密）
			String publicKey = inParam.get("publicKey") == null ? "" : inParam
					.get("publicKey").toString();
			JSONObject req = new JSONObject();
			req.put("newHash", newHash);
			req.put("resTotal", resTotal);
			logger.info(validKey + " 加密前req：" + req.toJSONString());
			String encryptResult = RSAUtil.encryption(publicKey,
					req.toJSONString());
			logger.info(validKey + " 调用结果（无详情）和反馈目录的哈希一起加密：" + encryptResult);

			// 将加密结果上链
			Map resp = ChainUtil.saveResult(
					saveResultUrl,
					inParam.get("validKey") == null ? "" : inParam.get(
							"validKey").toString(), encryptResult);
			String rsKey = resp.get("chainKey") == null ? "" : resp.get(
					"chainKey").toString();
			String txid = resp.get("txid") == null ? "" : resp.get("txid")
					.toString();
			TblTransactionInfo tblTransactionInfo = new TblTransactionInfo(
					txid, "THEKEY", "验证合约", "", "", "0", DateUtil.getNow(),
					validKey, inParam.get("serialNo") == null ? "" : inParam
							.get("serialNo").toString(), "验证反馈");
			transactionInfoService.save(tblTransactionInfo);

			/*
			 * // 验证完成，调用转账合约,数据库记录交易信息 ChainUtil.tranfer(neoUrl, allotTKYUrl,
			 * type_count, tblTransactionInfoList, validKey);
			 * transactionInfoService.saveList(tblTransactionInfoList);
			 * logger.info(validKey + " 验证完成，调用转账合约,数据库记录交易信息：" +
			 * tblTransactionInfoList);
			 */

			if (type_count.size() > 0) {
				type_count.put(ReceiverEnum.YB.getCode(), 2);
			}
			tranferService.save(new TblTranfer(validKey, type_count
					.toJSONString(), "0", inParam.get("serialNo") == null ? ""
					: inParam.get("serialNo").toString()));

			// 调用医院通知完成接口
			String hospitalUrl = env.getProperty("hospital.url");
			JSONObject result = new JSONObject();
			result.put("serialNo", inParam.get("serialNo"));
			result.put("blockKey", rsKey);
			result.put("hospitalId", this.inParam.get("hospitalId"));
			result.put("jzlsno", this.inParam.get("jzlsno"));
			try {
				HttpJsonBlockListClient.requestNeo(hospitalUrl,
						result.toJSONString());
				logger.info(validKey + " 调用医院通知完成接口：" + result.toJSONString());
			} catch (Exception e2) {
				logger.error(validKey + " 接收医院通知完成返回超时");
			}

		} catch (Exception e) {
			logger.error(
					validKey + " BussinessRunner.run error " + e.getMessage(),
					e);
		} finally {
			execStatus = "0";// 标记为执行结束
		}

	}

}
