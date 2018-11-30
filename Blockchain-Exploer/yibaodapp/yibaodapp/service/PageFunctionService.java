package com.xczg.blockchain.yibaodapp.service;

import java.util.List;
import java.util.Map;

import com.xczg.blockchain.common.model.PageResult;
import com.xczg.blockchain.yibaodapp.bean.TblAuditRecord;
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;

public interface PageFunctionService {

	public Map<String, Object> getNewstInfo();

	public Map<String, Object> searchBlockInfo(int blockIndex);

	public Map<String, Object> searchtraInfo(String searchInfo);

	public PageResult<TblTransactionInfo> findPageBySql(
			PageResult<TblTransactionInfo> pageResult);

	public PageResult<TblAuditRecord> queryPageByEntity(
			TblAuditRecord auditRecord, PageResult<TblAuditRecord> pageResult);

	public List<TblTransactionInfo> getTraInfoByID(String txid);

}
