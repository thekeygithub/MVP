package com.xczg.blockchain.common.model;

import java.util.Locale;

public class TreeModel 
{
	private String id;
	
	private String pId;
	
	private String rootid;
	
	private boolean isParent ;
	
	private String name;
	
	private String fullName;
	
	private boolean open;
	
	private String nocheck;
	
	private String letter;
	
	private String publishStat;//发布状态：草稿/发布
	
	private String startTime;//开始时间
	
	private String endTime;//结束时间
	
	private String engname;
	
	private String engfullName;
	
	private String businessType;//业务类型
	
	private String remark;
	
	private boolean isSms;//是否配置短信模版
	
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	
	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	
	public String getPublishStat()
	{
		return publishStat;
	}

	public void setPublishStat(String publishStat)
	{
		this.publishStat = publishStat;
	}
	
	public String getRootid()
	{
		return rootid;
	}

	public void setRootid(String rootid)
	{
		this.rootid = rootid;
	}

	public String getLetter()
	{
		return letter;
	}

	public void setLetter(String letter)
	{
		this.letter = letter;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEngfullName() {
		return engfullName;
	}

	public void setEngfullName(String engfullName) {
		this.engfullName = engfullName;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEngname() {
		return engname;
	}

	public void setEngname(String engname) {
		this.engname = engname;
	}
	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getNocheck() {
		return nocheck;
	}

	public void setNocheck(String nocheck) {
		this.nocheck = nocheck;
	}
	
	public boolean isSms() {
		return isSms;
	}

	public void setSms(boolean isSms) {
		this.isSms = isSms;
	}

	public TreeModel()
	{
		
	}
	
	public TreeModel(String letter,String fullName)
	{
		this.letter = letter;
		this.fullName = fullName;
	}
	
	public TreeModel(String letter,String fullName,Locale locale)
	{
		if(locale.equals(java.util.Locale.US)){
			this.letter = letter;
			this.engfullName = fullName;
		}else{
			this.letter = letter;
			this.fullName = fullName;
		}
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof TreeModel))
			return false;
		if (obj == this)
			return true;
		return this.letter.toUpperCase().indexOf(
				((TreeModel) obj).letter.toUpperCase()) != -1
				|| this.fullName.indexOf(((TreeModel) obj).fullName) != -1;
	}
    
	public boolean engequals(Object obj)
	{
		if (!(obj instanceof TreeModel))
			return false;
		if (obj == this)
			return true;
		return this.letter.toUpperCase().indexOf(
				((TreeModel) obj).letter.toUpperCase()) != -1
				|| this.engfullName.toUpperCase().indexOf(((TreeModel) obj).engfullName) != -1;
	}
	
    public int hashCode(){  
        return id.length();
    } 
}
