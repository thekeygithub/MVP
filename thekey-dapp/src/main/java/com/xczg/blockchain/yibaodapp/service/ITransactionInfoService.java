package com.xczg.blockchain.yibaodapp.service;

import java.util.List;

import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;

public interface ITransactionInfoService {

	public List<Integer> saveList(List<TblTransactionInfo> list)
			throws Exception;

	public void save(TblTransactionInfo entity) throws Exception;

	public String getSerialNo() throws Exception;

}
