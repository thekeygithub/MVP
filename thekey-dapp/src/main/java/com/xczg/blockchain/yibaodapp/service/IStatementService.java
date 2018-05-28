package com.xczg.blockchain.yibaodapp.service;

import java.util.List;

import com.xczg.blockchain.yibaodapp.bean.TblStatementKey;


public interface IStatementService  {

	public List<String>  getHashInTheDay();
	
	public void save( TblStatementKey entity) throws Exception ;
	
}
