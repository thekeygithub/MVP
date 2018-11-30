package com.xczg.blockchain.yibaodapp.bean;

public class CustInfo {
	private String id;
	private String custname="";
	private String idcard;
	private String socialcardid;
	private String mobile;
	private String sex;
	private String password;
	private String avatarimg;
	private String idcardpositive;
	private String idcardnegative;
	private String status;
	private String memo;
	private long birthdate=0;
	private String addr;
	private int is_deleted=0;
	private String chain_key="";
	private double tky_balance;
	private String login_token="";//登录令牌
	
	public CustInfo() {
		super();
	}
	
	public CustInfo(String id, String custname, String idcard, String socialcardid, String mobile, String sex,
			String password, String avatarimg, String idcardpositive, String idcardnegative, String status, String memo,
			long birthdate, String addr,double tky_balance,String login_token) {
		super();
		this.id = id;
		this.custname = custname;
		this.idcard = idcard;
		this.socialcardid = socialcardid;
		this.mobile = mobile;
		this.sex = sex;
		this.password = password;
		this.avatarimg = avatarimg;
		this.idcardpositive = idcardpositive;
		this.idcardnegative = idcardnegative;
		this.status = status;
		this.memo = memo;
		this.birthdate = birthdate;
		this.addr = addr;
		this.tky_balance = tky_balance;
		this.login_token=login_token;
	}


	public String getLogin_token() {
		return login_token;
	}

	public void setLogin_token(String login_token) {
		this.login_token = login_token;
	}

	public double getTky_balance() {
		return tky_balance;
	}

	public void setTky_balance(double tky_balance) {
		this.tky_balance = tky_balance;
	}
    public long getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(long birthdate) {
		this.birthdate = birthdate;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getSocialcardid() {
		return socialcardid;
	}

	public void setSocialcardid(String socialcardid) {
		this.socialcardid = socialcardid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatarimg() {
		return avatarimg;
	}

	public void setAvatarimg(String avatarimg) {
		this.avatarimg = avatarimg;
	}

	public String getIdcardpositive() {
		return idcardpositive;
	}

	public void setIdcardpositive(String idcardpositive) {
		this.idcardpositive = idcardpositive;
	}

	public String getIdcardnegative() {
		return idcardnegative;
	}

	public void setIdcardnegative(String idcardnegative) {
		this.idcardnegative = idcardnegative;
	}

	public String getChain_key() {
		return chain_key;
	}

	public void setChain_key(String chain_key) {
		this.chain_key = chain_key;
	}

	public int getIs_deleted() {
			return is_deleted;
	}


	public void setIs_deleted(int is_deleted) {
			this.is_deleted = is_deleted;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCust_name() {
		return custname;
	}
	public void setCust_name(String custname) {
		this.custname = custname;
	}
	public String getId_card() {
		return idcard;
	}
	public void setId_card(String idcard) {
		this.idcard = idcard;
	}
	public String getSocial_card_id() {
		return socialcardid;
	}
	public void setSocial_card_id(String socialcardid) {
		this.socialcardid = socialcardid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPass_word() {
		return password;
	}
	public void setPass_word(String password) {
		this.password = password;
	}
	public String getAvatar_img() {
		return avatarimg;
	}
	public void setAvatar_img(String avatarimg) {
		this.avatarimg = avatarimg;
	}
	public String getId_card_positive() {
		return idcardpositive;
	}
	public void setId_card_positive(String idcardpositive) {
		this.idcardpositive = idcardpositive;
	}
	public String getId_card_negative() {
		return idcardnegative;
	}
	public void setId_card_negative(String idcardnegative) {
		this.idcardnegative = idcardnegative;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public long getBirth_date() {
		return birthdate;
	}
	public void setBirth_date(long birthdate) {
		this.birthdate = birthdate;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String toJson(){
		StringBuffer cuStr=new StringBuffer();
		cuStr.append("{\"id\":").append("\""+this.id+"\"");
		cuStr.append(",\"custname\":").append("\"").append(this.custname).append("\"");
		cuStr.append(",\"idcard\":").append("\"").append(this.idcard).append("\"");
		cuStr.append(",\"socialcardid\":").append("\"").append(this.socialcardid).append("\"");
		cuStr.append(",\"mobile\":").append("\"").append(this.mobile).append("\"");
		cuStr.append(",\"sex\":").append("\"").append(this.sex).append("\"");
//		cuStr.append(",\"pass_word\":").append("\"").append(this.password).append("\"");
		cuStr.append(",\"avatar_img\":").append("\"").append(this.avatarimg).append("\"");
		cuStr.append(",\"id_card_positive\":").append("\"").append(this.idcardpositive).append("\"");
		cuStr.append(",\"id_card_negative\":").append("\"").append(this.idcardnegative).append("\"");
		cuStr.append(",\"status\":").append("\"").append(this.status).append("\"");
		cuStr.append(",\"addr\":").append("\"").append(this.addr).append("\"");
		cuStr.append(",\"birth_date\":").append("\"").append(this.birthdate).append("\"");
		cuStr.append(",\"memo\":").append("\"").append(this.memo).append("\"");
		cuStr.append(",\"tky_balance\":").append("\"").append(this.tky_balance).append("\"");
//		cuStr.append(",\"addr\":").append("\"").append(this.addr).append("\"");
		cuStr.append("}");
		return cuStr.toString();
	}
}
