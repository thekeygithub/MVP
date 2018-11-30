package com.xczg.blockchain.yibaodapp.bean;

import javax.persistence.Entity;

@Entity
public class BlockChainResultEntity {
	
	 private String validKey;
	 private String txid;
	 private String balance;
	public String getValidKey() {
		return validKey;
	}
	public void setValidKey(String validKey) {
		this.validKey = validKey;
	}
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	 
}
