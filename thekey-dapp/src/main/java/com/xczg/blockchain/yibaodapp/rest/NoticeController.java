package com.xczg.blockchain.yibaodapp.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xczg.blockchain.yibaodapp.bean.TblStatementKey;
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;
import com.xczg.blockchain.yibaodapp.service.IInterfaceHisService;
import com.xczg.blockchain.yibaodapp.service.IStatementService;
import com.xczg.blockchain.yibaodapp.service.ITranferService;
import com.xczg.blockchain.yibaodapp.service.ITransactionInfoService;
import com.xczg.blockchain.yibaodapp.thread.BussinessRunner;
import com.xczg.blockchain.yibaodapp.thread.BussinessRunner2;
import com.xczg.blockchain.yibaodapp.thread.ExecutorManager;
import com.xczg.blockchain.yibaodapp.util.ChainUtil;
import com.xczg.blockchain.yibaodapp.util.RSAUtil;

@Controller
@RequestMapping("notice")
public class NoticeController {

	private final Logger logger = LoggerFactory
			.getLogger(NoticeController.class);

	@Autowired
	private ITransactionInfoService transactionInfoService;

	@Autowired
	private IInterfaceHisService interfaceHisService;

	@Autowired
	private IStatementService statementService;

	@Autowired
	private Environment env;

	@Autowired
	private ITranferService tranferService;

	/***
	 * 
	 * @param ID
	 *            身份证号
	 * @param validKey
	 *            用户数据区块链Key
	 * @param transferSerial
	 *            医院转账交易流水号
	 * @param transferTime
	 *            医院转账交易时间
	 * @param publicKey
	 *            医院公钥
	 * @param hospitalId
	 *            医院ID
	 * @param senderName
	 *            医院名称
	 * @param senderAddr
	 *            医院钱包地址
	 * @param type
	 *            行业类型
	 * @param fee
	 *            转账金额
	 * @param validTxid
	 *            医院验证合约交易ID
	 * @param validTxTime
	 *            医院验证合约交易时间
	 * @param jzlsno
	 *            就诊流水号
	 * @return
	 */
	@RequestMapping("/validation")
	public @ResponseBody String validation(
			@RequestParam(value = "ID", required = true) String ID,
			@RequestParam(value = "validKey", required = true) String validKey,
			@RequestParam(value = "transferSerial", required = true) String transferSerial,
			@RequestParam(value = "transferTime", required = true) String transferTime,
			@RequestParam(value = "publicKey", required = true) String publicKey,
			@RequestParam(value = "hospitalId", required = true) String hospitalId,
			@RequestParam(value = "senderName", required = true) String senderName,
			@RequestParam(value = "senderAddr", required = true) String senderAddr,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "fee", required = true) String fee,
			@RequestParam(value = "validTxId", required = true) String validTxId,
			@RequestParam(value = "validTxTime", required = true) String validTxTime,
			@RequestParam(value = "jzlsno", required = true) String jzlsno) {

		String serialNo = "";
		try {
			serialNo = transactionInfoService.getSerialNo();
			logger.info(" 收到验证发起通知，请求参数：" + "ID:" + ID + "|" + "validKey:"
					+ validKey + "|" + "transferSerial:" + transferSerial + "|"
					+ "transferTime:" + transferTime + "publicKey:" + publicKey
					+ "|" + "senderName" + "validTxid:" + validTxId
					+ "validTxTime:" + validTxTime
					+ new String(senderName.getBytes("ISO-8859-1"), "utf-8")
					+ "|" + "hospitalId" + hospitalId + "|" + "senderAddr"
					+ senderAddr + "|" + "type:" + type + "|" + "fee:" + fee);

			// 记录一笔医院给支付合约的交易记录
			String contactWalletAddr = env.getProperty("contact.wallet.addr");
			TblTransactionInfo tblTransactionInfo = new TblTransactionInfo(
					transferSerial, new String(
							senderName.getBytes("ISO-8859-1"), "utf-8"),
					"支付合约", senderAddr, contactWalletAddr, fee, transferTime,
					validKey, serialNo, "积分流转");
			transactionInfoService.save(tblTransactionInfo);
			logger.info(validKey + " 记录一笔医院调用支付合约记录：" + tblTransactionInfo);

			// 记录一笔医院给验证合约的交易记录
			TblTransactionInfo tblTransactionInfo2 = new TblTransactionInfo(
					validTxId, new String(senderName.getBytes("ISO-8859-1"),
							"utf-8"), "验证合约", senderAddr, contactWalletAddr,
					"0", validTxTime, validKey, serialNo, "验证申请");
			transactionInfoService.save(tblTransactionInfo2);
			logger.info(validKey + " 记录一笔医院调用验证合约记录：" + tblTransactionInfo2);

			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("ID", ID);
			inParamMap.put("validKey", validKey);
			inParamMap.put("transferSerial", transferSerial);
			inParamMap.put("publicKey", publicKey);
			inParamMap.put("hospitalId", hospitalId);
			inParamMap.put("senderName",
					new String(senderName.getBytes("ISO-8859-1"), "utf-8"));
			inParamMap.put("senderAddr", senderAddr);
			inParamMap.put("type", type);
			inParamMap.put("fee", fee);
			inParamMap.put("jzlsno", jzlsno);
			inParamMap.put("serialNo", serialNo);

			logger.info(validKey + " 开始多维验证");
			List<BussinessRunner> bussRunners = new ArrayList<BussinessRunner>();
			BussinessRunner tmpRunner = new BussinessRunner(inParamMap,
					interfaceHisService, env, tranferService,
					transactionInfoService);
			bussRunners.add(tmpRunner);
			ExecutorManager.getInstance().doExecute(tmpRunner);
		} catch (Exception e) {
			logger.info(validKey + " 验证error " + e.getMessage(), e);
		}

		logger.info(validKey + " 收到验证发起通知返回0");
		Map<String, Object> outParamMap = new HashMap<String, Object>();
		outParamMap.put("rsCode", "0");
		outParamMap.put("serialNo", serialNo);
		return JSONObject.toJSONString(outParamMap).toString();
	}

	/**
	 * 医院结算完成发起通知
	 * 
	 * @param validKey
	 *            结算单上链的validKey
	 * @param validKey
	 *            结算单上链的ipfsHash
	 * @return
	 */
	@RequestMapping("/payComplete")
	public @ResponseBody String payComplete(
			@RequestParam(value = "validKey", required = true) String validKey,
			@RequestParam(value = "ipfsHash", required = false) String ipfsHash) {
		logger.info(validKey + " 收到支付完成发起通知，请求参数：" + "validKey:" + validKey
				+ "ipfsHash:" + ipfsHash);
		try {
			if (null == ipfsHash || ipfsHash.isEmpty()) {
				String neoUrl = env.getProperty("neo.url");// 区块链调用地址
				String getHash = env.getProperty("get.hash");// 区块链调用地址
				String ipfsHash2 = ChainUtil.get(getHash, neoUrl, validKey);
				String yibaoPrivateKey = env.getProperty("yibao.privateKey");
				ipfsHash = RSAUtil.decryption(yibaoPrivateKey, ipfsHash2);
			}
			statementService.save(new TblStatementKey(ipfsHash, validKey));
		} catch (Exception e) {
			logger.info(validKey + " 支付完成通知error " + e.getMessage(), e);

			logger.info(validKey + " 收到支付发起通知 error，返回1");
			Map<String, Object> outParamMap = new HashMap<String, Object>();
			outParamMap.put("rsCode", "1");
			return JSONObject.toJSONString(outParamMap).toString();
		}

		logger.info(validKey + " 收到支付发起通知，返回0");
		Map<String, Object> outParamMap = new HashMap<String, Object>();
		outParamMap.put("rsCode", "0");
		return JSONObject.toJSONString(outParamMap).toString();
	}

	/**
	 * 医院发起验证通知
	 */
	@RequestMapping("/validation2")
	public @ResponseBody String validation2(
			@RequestParam(value = "ID", required = true) String ID,
			@RequestParam(value = "validKey", required = true) String validKey,
			@RequestParam(value = "transferSerial", required = true) String transferSerial,
			@RequestParam(value = "transferTime", required = true) String transferTime,
			@RequestParam(value = "publicKey", required = true) String publicKey,
			@RequestParam(value = "hospitalId", required = true) String hospitalId,
			@RequestParam(value = "senderName", required = true) String senderName,
			@RequestParam(value = "senderAddr", required = true) String senderAddr,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "fee", required = true) String fee,
			@RequestParam(value = "validTxId", required = true) String validTxId,
			@RequestParam(value = "validTxTime", required = true) String validTxTime,
			@RequestParam(value = "jzlsno", required = true) String jzlsno,
			@RequestParam(value = "ipfsHash", required = true) String ipfsHash) {
		String serialNo = "";
		try {
			serialNo = transactionInfoService.getSerialNo();
			logger.info(" 收到验证发起通知，请求参数：" + "ID:" + ID + "|" + "validKey:"
					+ validKey + "|" + "transferSerial:" + transferSerial + "|"
					+ "transferTime:" + transferTime + "publicKey:" + publicKey
					+ "|" + "senderName" + "validTxid:" + validTxId
					+ "validTxTime:" + validTxTime
					+ new String(senderName.getBytes("ISO-8859-1"), "utf-8")
					+ "|" + "hospitalId" + hospitalId + "|" + "senderAddr"
					+ senderAddr + "|" + "type:" + type + "|" + "fee:" + fee);

			// 记录一笔医院给易保合约的交易记录
			String contactWalletAddr = env.getProperty("contact.wallet.addr");
			TblTransactionInfo tblTransactionInfo = new TblTransactionInfo(
					transferSerial, new String(
							senderName.getBytes("ISO-8859-1"), "utf-8"),
					"支付合约", senderAddr, contactWalletAddr, fee, transferTime,
					validKey, serialNo, "支付合约");
			transactionInfoService.save(tblTransactionInfo);
			logger.info(validKey + " 记录一笔医院调用支付合约记录：" + tblTransactionInfo);

			// 记录一笔医院给验证合约的交易记录
			TblTransactionInfo tblTransactionInfo2 = new TblTransactionInfo(
					validTxId, new String(senderName.getBytes("ISO-8859-1"),
							"utf-8"), "验证合约", senderAddr, contactWalletAddr,
					fee, validTxTime, validKey, serialNo, "验证申请");
			transactionInfoService.save(tblTransactionInfo2);
			logger.info(validKey + " 记录一笔医院调用验证合约记录：" + tblTransactionInfo2);

			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("ID", ID);
			inParamMap.put("validKey", validKey);
			inParamMap.put("transferSerial", transferSerial);
			inParamMap.put("publicKey", publicKey);
			inParamMap.put("hospitalId", hospitalId);
			inParamMap.put("senderName",
					new String(senderName.getBytes("ISO-8859-1"), "utf-8"));
			inParamMap.put("senderAddr", senderAddr);
			inParamMap.put("type", type);
			inParamMap.put("fee", fee);
			inParamMap.put("jzlsno", jzlsno);
			inParamMap.put("ipfsHash", ipfsHash);
			inParamMap.put("serialNo", serialNo);

			logger.info(validKey + " 开始多维验证");
			List<BussinessRunner2> bussRunners = new ArrayList<BussinessRunner2>();
			BussinessRunner2 tmpRunner = new BussinessRunner2(inParamMap,
					interfaceHisService, env, tranferService,
					transactionInfoService);
			bussRunners.add(tmpRunner);
			ExecutorManager.getInstance().doExecute2(tmpRunner);
		} catch (Exception e) {
			logger.info(validKey + " 验证error " + e.getMessage(), e);
		}

		logger.info(validKey + " 收到验证发起通知返回0");
		Map<String, Object> outParamMap = new HashMap<String, Object>();
		outParamMap.put("rsCode", "0");
		outParamMap.put("serialNo", serialNo);
		return JSONObject.toJSONString(outParamMap).toString();
	}

	/**
	 * 
	 * @param txId
	 *            区块链返回的交易ID
	 * @param senderName
	 *            发起方,例如：人民医院
	 * @param receiverName
	 *            接收方，例如：验证合约
	 * @param senderAddr
	 *            发起方钱包地址
	 * @param receiverAddr
	 *            接收方钱包地址
	 * @param fee
	 *            转账金额
	 * @param time
	 *            交易发起时间
	 * @param validKey
	 *            区块链返回Key
	 * @param serialNo
	 *            交易业务标识， 一次业务，交易业务标识唯一
	 * @param type
	 *            交易类型：支付合约，验证申请，验证反馈等
	 * @return
	 */
	@RequestMapping("/saveTx")
	public @ResponseBody String saveTx(
			@RequestParam(value = "txId", required = true) String txId,
			@RequestParam(value = "senderName", required = true) String senderName,
			@RequestParam(value = "receiverName", required = true) String receiverName,
			@RequestParam(value = "senderAddr", required = true) String senderAddr,
			@RequestParam(value = "receiverAddr", required = true) String receiverAddr,
			@RequestParam(value = "fee", required = true) String fee,
			@RequestParam(value = "time", required = true) String time,
			@RequestParam(value = "validKey", required = true) String validKey,
			@RequestParam(value = "serialNo", required = true) String serialNo,
			@RequestParam(value = "type", required = true) String type) {

		TblTransactionInfo tblTransactionInfo;
		try {
			tblTransactionInfo = new TblTransactionInfo(txId, new String(
					senderName.getBytes("ISO-8859-1"), "utf-8"), new String(
					receiverName.getBytes("ISO-8859-1"), "utf-8"), senderAddr,
					receiverAddr, fee, time, validKey, serialNo, new String(
							type.getBytes("ISO-8859-1"), "utf-8"));
			transactionInfoService.save(tblTransactionInfo);

			logger.info(validKey + " 记录一笔医院交易：" + tblTransactionInfo);
		} catch (Exception e) {
			logger.info("记录交易error " + e.getMessage(), e);
			Map<String, Object> outParamMap = new HashMap<String, Object>();
			outParamMap.put("rsCode", "1");
			return JSONObject.toJSONString(outParamMap).toString();
		}

		Map<String, Object> outParamMap = new HashMap<String, Object>();
		outParamMap.put("rsCode", "0");
		return JSONObject.toJSONString(outParamMap).toString();
	}
}
