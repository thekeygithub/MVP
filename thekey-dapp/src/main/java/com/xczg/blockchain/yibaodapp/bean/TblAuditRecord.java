package com.xczg.blockchain.yibaodapp.bean;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xczg.blockchain.common.annotation.WhereSQL;

@Entity
@Table(name = "TBL_AUDIT_RECORD")
public class TblAuditRecord {

	/**
	 * 主键 审核编号
	 */
	private String auditId;

	/**
	 * 医疗机构
	 */
	private String medicalInstitution;

	/**
	 * 就诊类型
	 */
	private String treatType;

	/**
	 * 违规药品/项目
	 */
	private String illegalDrugs;

	/**
	 * 违规类型
	 */
	private String illegalType;

	/**
	 * 身份证号
	 */
	private String sfzh;

	/**
	 * 诊断/治疗
	 */
	private String diagnosis;

	/**
	 * 就诊日期
	 */
	private String treatmentDate;

	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 查询时时间查询区间的开始时间
	 */
	private String startTime;

	/**
	 * 查询时时间查询区间的结束时间
	 */
	private String endTime;

	private String ipfsHash;

	public TblAuditRecord() {
	}

	public TblAuditRecord(String ipfsHash, String medicalInstitution,
			String treatType, String illegalDrugs, String illegalType,
			String sfzh, String diagnosis, String treatmentDate, String sex,
			String name) {
		super();
		this.auditId = UUID.randomUUID().toString();
		this.ipfsHash = ipfsHash;
		this.medicalInstitution = medicalInstitution;
		this.treatType = treatType;
		this.illegalDrugs = illegalDrugs;
		this.illegalType = illegalType;
		this.sfzh = sfzh;
		this.diagnosis = diagnosis;
		this.treatmentDate = treatmentDate;
		this.sex = sex;
		this.name = name;
	}

	@Id
	@Column(name = "AUDIT_ID")
	@WhereSQL(sql = "AUDIT_ID=:auditId")
	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	@Column(name = "MEDICAL_INSTITUTION")
	@WhereSQL(sql = "MEDICAL_INSTITUTION=:medicalInstitution")
	public String getMedicalInstitution() {
		return medicalInstitution;
	}

	public void setMedicalInstitution(String medicalInstitution) {
		this.medicalInstitution = medicalInstitution;
	}

	@Column(name = "TREAT_TYPE")
	@WhereSQL(sql = "TREA_TYPE=:treatType")
	public String getTreatType() {
		return treatType;
	}

	public void setTreatType(String treatType) {
		this.treatType = treatType;
	}

	@Column(name = "ILLEGAL_DRUGS")
	@WhereSQL(sql = "ILLEGAL_DRUGS=:illegalDrugs")
	public String getIllegalDrugs() {
		return illegalDrugs;
	}

	public void setIllegalDrugs(String illegalDrugs) {
		this.illegalDrugs = illegalDrugs;
	}

	@Column(name = "ILLEGAL_TYPE")
	@WhereSQL(sql = "ILLEGAL_TYPE=:illegalType")
	public String getIllegalType() {
		return illegalType;
	}

	public void setIllegalType(String illegalType) {
		this.illegalType = illegalType;
	}

	@Column(name = "SFZH")
	@WhereSQL(sql = "SFZH=:sfzh")
	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	@Column(name = "DIAGNOSIS")
	@WhereSQL(sql = "DIAGNOSIS=:diagnosis")
	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	@Column(name = "TREATMENT_DATE")
	@WhereSQL(sql = "TREATMENT_DATE=:treatmentDate")
	public String getTreatmentDate() {
		return treatmentDate;
	}

	public void setTreatmentDate(String treatmentDate) {
		this.treatmentDate = treatmentDate;
	}

	@Column(name = "SEX")
	@WhereSQL(sql = "SEX=:sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "NAME")
	@WhereSQL(sql = "NAME=:name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@WhereSQL(sql = "TREATMENT_DATE>:startTime")
	@Transient
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@WhereSQL(sql = "TREATMENT_DATE<:endTime")
	@Transient
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "IPFS_HASH")
	@WhereSQL(sql = "IPFS_HASH=:ipfsHash")
	public String getIpfsHash() {
		return ipfsHash;
	}

	public void setIpfsHash(String ipfsHash) {
		this.ipfsHash = ipfsHash;
	}

	@Override
	public String toString() {
		return "TblAuditRecord [auditId=" + auditId + ", medicalInstitution="
				+ medicalInstitution + ", treatType=" + treatType
				+ ", illegalDrugs=" + illegalDrugs + ", illegalType="
				+ illegalType + ", sfzh=" + sfzh + ", diagnosis=" + diagnosis
				+ ", treatmentDate=" + treatmentDate + ", sex=" + sex
				+ ", name=" + name + ", startTime=" + startTime + ", endTime="
				+ endTime + ", ipfsHash=" + ipfsHash + "]";
	}
}
