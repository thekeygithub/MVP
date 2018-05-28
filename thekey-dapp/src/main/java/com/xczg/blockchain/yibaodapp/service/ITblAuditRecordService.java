package com.xczg.blockchain.yibaodapp.service;

import java.util.List;

import com.xczg.blockchain.yibaodapp.bean.TblAuditRecord;

public interface ITblAuditRecordService {
	public List<Integer> saveList(List<TblAuditRecord> list)
			throws Exception;

	public void save(TblAuditRecord entity) throws Exception;
}
