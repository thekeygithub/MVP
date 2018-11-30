package com.xczg.blockchain.yibaodapp.bean;

public class TblSysUser {
	private String	account="";
	private String	username="";
	private String	password="";
	private String	status="";
	private String	lastlogin="";
	private String	email="";
	private String	mobile="";
	private String	officephone="";
	private String	entrydate="";
	private String	jobtitle="";
	private String	sex="";
	private String	joblevel="";
	private String	memo="";
	private String	birthday="";
	private String 	avatarpath="";
	private int  	is_deleted=0;
	private int     sys_type=0;
	public int getSys_type() {
		return sys_type;
	}

	public void setSys_type(int sys_type) {
		this.sys_type = sys_type;
	}

	public TblSysUser() {
		super();
	}
	
	public int getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}

	public TblSysUser(String account, String username, String password, String status, String lastlogin, String email,
			String mobile, String officephone, String entrydate, String jobtitle, String sex, String joblevel,
			String memo, String birthday, String avatarpath) {
		super();
		this.account = account;
		this.username = username;
		this.password = password;
		this.status = status;
		this.lastlogin = lastlogin;
		this.email = email;
		this.mobile = mobile;
		this.officephone = officephone;
		this.entrydate = entrydate;
		this.jobtitle = jobtitle;
		this.sex = sex;
		this.joblevel = joblevel;
		this.memo = memo;
		this.birthday = birthday;
		this.avatarpath = avatarpath;
	}

	public TblSysUser(String account, String username, String status, String lastlogin, String email, String mobile,
			String officephone, String entrydate, String jobtitle, String sex, String joblevel, String memo,
			String birthday, String avatarpath) {
		super();
		this.account = account;
		this.username = username;
		this.status = status;
		this.lastlogin = lastlogin;
		this.email = email;
		this.mobile = mobile;
		this.officephone = officephone;
		this.entrydate = entrydate;
		this.jobtitle = jobtitle;
		this.sex = sex;
		this.joblevel = joblevel;
		this.memo = memo;
		this.birthday = birthday;
		this.avatarpath = avatarpath;
	}

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOfficephone() {
		return officephone;
	}
	public void setOfficephone(String officephone) {
		this.officephone = officephone;
	}
	public String getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(String entrydate) {
		this.entrydate = entrydate;
	}
	public String getJobtitle() {
		return jobtitle;
	}
	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getJoblevel() {
		return joblevel;
	}
	public void setJoblevel(String joblevel) {
		this.joblevel = joblevel;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAvatarpath() {
		return avatarpath;
	}
	public void setAvatarpath(String avatarpath) {
		this.avatarpath = avatarpath;
	}
	public String toJson(){
		StringBuffer cuStr=new StringBuffer();
		cuStr.append("{\"account\":").append("\""+this.account+"\"");
		cuStr.append(",\"username\":").append("\"").append(this.username).append("\"");
//		cuStr.append(",\"password\":").append("\"").append(this.password).append("\"");
		cuStr.append(",\"status\":").append("\"").append(this.status).append("\"");
		cuStr.append(",\"lastlogin\":").append("\"").append(this.lastlogin).append("\"");
		cuStr.append(",\"email\":").append("\"").append(this.email).append("\"");
		cuStr.append(",\"mobile\":").append("\"").append(this.mobile).append("\"");
		cuStr.append(",\"officephone\":").append("\"").append(this.officephone).append("\"");
		cuStr.append(",\"entrydate\":").append("\"").append(this.entrydate).append("\"");
		cuStr.append(",\"jobtitle\":").append("\"").append(this.jobtitle).append("\"");
		cuStr.append(",\"sex\":").append("\"").append(this.sex).append("\"");
		cuStr.append(",\"joblever\":").append("\"").append(this.joblevel).append("\"");
		cuStr.append(",\"memo\":").append("\"").append(this.memo).append("\"");
		cuStr.append(",\"birthday\":").append("\"").append(this.birthday).append("\"");
		cuStr.append(",\"avatarpath\":").append("\"").append(this.avatarpath).append("\"");
		cuStr.append("}");
		return cuStr.toString();
	} 
	
}
