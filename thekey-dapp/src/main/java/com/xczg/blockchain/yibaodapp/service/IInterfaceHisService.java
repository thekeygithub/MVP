package com.xczg.blockchain.yibaodapp.service;

import java.util.List;

import com.xczg.blockchain.yibaodapp.bean.TblInterfaceHis;

public interface IInterfaceHisService {

	public List<Integer> saveList(List<TblInterfaceHis> list)
			throws Exception;

	public void save(TblInterfaceHis entity) throws Exception;

}
