package com.xczg.blockchain.yibaodapp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xczg.blockchain.common.dao.BaseDao;
import com.xczg.blockchain.yibaodapp.bean.TblInterfaceHis;
import com.xczg.blockchain.yibaodapp.service.IInterfaceHisService;

@Service("interfaceHisService")
public class InterfaceHisServiceImpl implements IInterfaceHisService {

	@Resource
	private BaseDao baseDao;

	public List<Integer> saveList(List<TblInterfaceHis> list)
			throws Exception {
		return baseDao.save(list);
	}

	public void save(TblInterfaceHis entity) throws Exception {
		baseDao.save(entity);
	}

}
