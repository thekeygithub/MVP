package com.xczg.blockchain.yibaodapp.bean;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xczg.blockchain.common.annotation.WhereSQL;
import com.xczg.blockchain.yibaodapp.util.DateUtil;

@Entity
@Table(name = "TBL_STATEMENT_KEY")
public class TblStatementKey {
	/**
	 * 主键
	 */
	private String id;
	/**
	 * IPFS上结算单的ipfsHash
	 */
	private String ipfsHash;

	/**
	 * 区块链上结算单的chainKey
	 */
	private String chainKey;
	/**
	 * 创建时间
	 */
	private String createTime;

	public TblStatementKey() {
	}

	public TblStatementKey(String ipfsHash,String chainKey) {
		super();
		this.id = UUID.randomUUID().toString();
		this.ipfsHash = ipfsHash;
		this.chainKey=chainKey;
		this.createTime = DateUtil.getNow();
	}

	@Id
	@Column(name = "ID")
	@WhereSQL(sql = "ID=:id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "IPFS_HASH")
	@WhereSQL(sql = "IPFS_HASH=:ipfsHash")
	public String getIpfsHash() {
		return ipfsHash;
	}

	public void setIpfsHash(String ipfsHash) {
		this.ipfsHash = ipfsHash;
	}

	@Column(name = "CREATE_TIME")
	@WhereSQL(sql = "CREATE_TIME=:createTime")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CHAIN_KEY")
	@WhereSQL(sql = "CHAIN_KEY=:chainKey")
	public String getChainKey() {
		return chainKey;
	}

	public void setChainKey(String chainKey) {
		this.chainKey = chainKey;
	}

}
