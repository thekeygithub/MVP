package com.xczg.blockchain.yibaodapp.bean;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xczg.blockchain.common.annotation.WhereSQL;

@Entity
@Table(name = "TBL_TRANSACTION_INFO")
public class TblTransactionInfo {
	/** 
	 * 主键
	 */
	private String id;
	
	/**
	 * 业务交易流水号
	 */
	private String serialNo;
	
	/**
	 * 交易ID
	 */
	private String txid;
	/**
	 * 发起方
	 */
	private String sender;
	/**
	 * 接收方
	 */
	private String receiver;
	/**
	 * 费用
	 */
	private String fee;
	/**
	 * 交易时间
	 */
	private String time;
	/**
	 * 发起方钱包地址
	 */
	private String senderAddr;
	/**
	 * 接受方钱包地址
	 */
	private String receiverAddr;
	/**
	 * 医院发起通知传过来的上链key
	 */
	private String validKey;
	/**
	 * 交易类型
	 */
	private String type;

	
	public TblTransactionInfo() {
	}

	public TblTransactionInfo(String txid, String sender, String receiver,
			String senderAddr, String receiverAddr, String fee, String time,String validKey,String serialNo,String type) {
		super();
		this.id=UUID.randomUUID().toString();
		this.txid = txid;
		this.sender = sender;
		this.receiver = receiver;
		this.fee = fee;
		this.senderAddr = senderAddr;
		this.receiverAddr = receiverAddr;
		this.time = time;
		this.validKey=validKey;
		this.serialNo=serialNo;
		this.type=type;

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

	@Column(name = "TXID")
	@WhereSQL(sql = "TXID=:txid")
	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	@Column(name = "SENDER")
	@WhereSQL(sql = "SENDER=:sender")
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Column(name = "RECEIVER")
	@WhereSQL(sql = "RECEIVER=:receiver")
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Column(name = "FEE")
	@WhereSQL(sql = "FEE=:fee")
	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	@Column(name = "TIME")
	@WhereSQL(sql = "TIME=:time")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Column(name = "SENDER_ADDR")
	@WhereSQL(sql = "SENDER_ADDR=:senderAddr")
	public String getSenderAddr() {
		return senderAddr;
	}

	public void setSenderAddr(String senderAddr) {
		this.senderAddr = senderAddr;
	}

	@Column(name = "RECEIVER_ADDR")
	@WhereSQL(sql = "RECEIVER_ADDR=:receiverAddr")
	public String getReceiverAddr() {
		return receiverAddr;
	}

	public void setReceiverAddr(String receiverAddr) {
		this.receiverAddr = receiverAddr;
	}

	@Column(name = "VALID_KEY")
	@WhereSQL(sql = "VALID_KEY=:validKey")
	public String getValidKey() {
		return validKey;
	}

	public void setValidKey(String validKey) {
		this.validKey = validKey;
	}

	@Column(name = "SERIAL_NO")
	@WhereSQL(sql = "SERIAL_NO=:serialNo")
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@Column(name = "TYPE")
	@WhereSQL(sql = "TYPE=:type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TblTransactionInfo [id=" + id + ", serialNo=" + serialNo
				+ ", txid=" + txid + ", sender=" + sender + ", receiver="
				+ receiver + ", fee=" + fee + ", time=" + time
				+ ", senderAddr=" + senderAddr + ", receiverAddr="
				+ receiverAddr + ", validKey=" + validKey + ", type=" + type
				+ "]";
	}
	
}
