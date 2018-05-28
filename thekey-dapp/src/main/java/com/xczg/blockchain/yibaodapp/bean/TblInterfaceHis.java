package com.xczg.blockchain.yibaodapp.bean;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xczg.blockchain.common.annotation.WhereSQL;

/**
 * 接口调用明细信息
 * 
 * @author guoliang
 *
 */
@Entity
@Table(name = "TBL_INTERFACE_HIS")
public class TblInterfaceHis {
	/**
	 * 主键
	 */
	private String hisId;
	/**
	 * 行业编码
	 */
	private String type;
	/**
	 * 调用的接口编码
	 */
	private String interfaceName;
	/**
	 * 接口返回的原始数据
	 */
	private String interfaceRestlt;

	/**
	 * 接口调用时间
	 */
	private String exeTime;
	/**
	 * 身份证ID
	 */
	private String idNo;
	/**
	 * 医院发起通知传过来的上链key
	 */
	private String validKey;

	public TblInterfaceHis() {
	}

	public TblInterfaceHis(String type, String idNo, String interfaceName,
			String interfaceRestlt, String exeTime, String validKey) {
		super();
		this.hisId = UUID.randomUUID().toString();
		this.type = type;
		this.interfaceName = interfaceName;
		this.interfaceRestlt = interfaceRestlt;
		this.exeTime = exeTime;
		this.idNo = idNo;
		this.validKey = validKey;
	}

	@Id
	@Column(name = "HIS_ID")
	@WhereSQL(sql = "HIS_ID=:hisId")
	public String getHisId() {
		return hisId;
	}

	public void setHisId(String hisId) {
		this.hisId = hisId;
	}

	@Column(name = "TYPE")
	@WhereSQL(sql = "TYPE=:type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "INTERFACE_NAME")
	@WhereSQL(sql = "INTERFACE_NAME=:interfaceName")
	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	@Column(name = "INTERFACE_RESTLT")
	@WhereSQL(sql = "INTERFACE_RESTLT=:interfaceRestlt")
	public String getInterfaceRestlt() {
		return interfaceRestlt;
	}

	public void setInterfaceRestlt(String interfaceRestlt) {
		this.interfaceRestlt = interfaceRestlt;
	}

	@Column(name = "EXE_TIME")
	@WhereSQL(sql = "EXE_TIME=:exeTime")
	public String getExeTime() {
		return exeTime;
	}

	public void setExeTime(String exeTime) {
		this.exeTime = exeTime;
	}

	@Column(name = "ID_NO")
	@WhereSQL(sql = "ID_NO=:idNo")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	@Column(name = "VALID_KEY")
	@WhereSQL(sql = "VALID_KEY=:validKey")
	public String getValidKey() {
		return validKey;
	}

	public void setValidKey(String validKey) {
		this.validKey = validKey;
	}

}
