package com.xczg.blockchain.yibaodapp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xczg.blockchain.common.dao.BaseDao;
import com.xczg.blockchain.yibaodapp.bean.TblAuditRecord;
import com.xczg.blockchain.yibaodapp.service.ITblAuditRecordService;

@Service("auditRecordService")
public class AuditRecordServiceImpl implements ITblAuditRecordService {

	@Resource
	private BaseDao baseDao;

	@Override
	public List<Integer> saveList(List<TblAuditRecord> list) throws Exception {
		return baseDao.save(list);
	}

	@Override
	public void save(TblAuditRecord entity) throws Exception {
		baseDao.save(entity);
	}

}
