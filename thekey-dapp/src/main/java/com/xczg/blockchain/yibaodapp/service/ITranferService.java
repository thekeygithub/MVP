package com.xczg.blockchain.yibaodapp.service;

import java.util.List;

import com.xczg.blockchain.yibaodapp.bean.TblTranfer;

public interface ITranferService {
	public void save(TblTranfer entity) throws Exception;
	
	public List<TblTranfer> getUndo();
	
	public void update(TblTranfer entity) throws Exception;
}
