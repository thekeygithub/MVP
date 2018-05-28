package com.xczg.blockchain.yibaodapp.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xczg.blockchain.common.annotation.WhereSQL;

@Entity
@Table(name = "TBL_TRANFER")
public class TblTranfer {

	/**
	 * 主键
	 */
	private String validKey;
	/**
	 * type_count
	 */
	private String typeCount;
	
	private String serialNo;
	
	private String state;

	public TblTranfer() {
	}

	public TblTranfer(String validKey, String typeCount,String state,String serialNo) {
		super();
		this.validKey = validKey;
		this.typeCount = typeCount;
		this.state=state;
		this.serialNo=serialNo;
	}

	@Id
	@Column(name = "VALID_KEY")
	@WhereSQL(sql = "VALID_KEY=:validKey")
	public String getValidKey() {
		return validKey;
	}

	public void setValidKey(String validKey) {
		this.validKey = validKey;
	}

	@Column(name = "TYPE_COUNT")
	@WhereSQL(sql = "TYPE_COUNT=:typeCount")
	public String getTypeCount() {
		return typeCount;
	}

	public void setTypeCount(String typeCount) {
		this.typeCount = typeCount;
	}

	@Column(name = "STATE")
	@WhereSQL(sql = "STATE=:state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "SERIAL_NO")
	@WhereSQL(sql = "SERIAL_NO=:serialNo")
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	

}
