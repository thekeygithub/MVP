package com.xczg.blockchain.yibaodapp.job;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xczg.blockchain.yibaodapp.bean.TblTranfer;
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;
import com.xczg.blockchain.yibaodapp.service.ITranferService;
import com.xczg.blockchain.yibaodapp.service.ITransactionInfoService;
import com.xczg.blockchain.yibaodapp.util.ChainUtil;
import com.xczg.blockchain.yibaodapp.util.DateUtil;

@Component
public class TxTask {

	private static Logger log = LoggerFactory.getLogger(TxTask.class);

	@Autowired
	private ITranferService tranferService;

	@Autowired
	private ITransactionInfoService transactionInfoService;

	@Autowired
	private Environment env;

	@Scheduled(cron = "*/20 * * * * ?")
	// 每隔20s
	public void getTxid() throws InterruptedException {
		log.info("getTxid " + DateUtil.getNow());
		List<TblTransactionInfo> tblTransactionInfoList = null;
		List<TblTranfer> entrys = tranferService.getUndo();
		String neoUrl = env.getProperty("neo.url");// 区块链调用地址
		String allotTKYUrl = env.getProperty("allotTKY.url");
		String getZZHash = env.getProperty("getZZ.hash");
		if (null != entrys && entrys.size() > 0) {
			for (TblTranfer enrty : entrys) {
				tblTransactionInfoList = new ArrayList<TblTransactionInfo>();// 交易记录
				// 验证完成，调用转账合约,数据库记录交易信息
				ChainUtil.tranfer(getZZHash, neoUrl, allotTKYUrl,
						JSONObject.parseObject(enrty.getTypeCount()),
						tblTransactionInfoList, enrty.getValidKey(),
						enrty.getSerialNo());
				try {
					transactionInfoService.saveList(tblTransactionInfoList);
					enrty.setState("1");
					tranferService.update(enrty);
				} catch (Exception e) {
					log.error("保存交易记录失败 " + e.getMessage(), e);
				}

			}
		}

	}
}
