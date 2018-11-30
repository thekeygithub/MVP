package com.xczg.blockchain.yibaodapp.bean;
import java.util.UUID;

public class EnterpriseInfo {
    private String id="";
    private String enterprisename="";
    private String licenseno="";
    private String operatorname="";
    private String operatoridcard="";
    private String operatormobile="";
    private String bankaccount="";
    private String idcardpositive="";
    private String idcardnegative="";
    private String licenseimg="";
    private String enterprisetype="";// 登录身份
    private String addr="";
    private String memo="";
    private String privince="";
    private String city="";
    private String status="";
    private int is_deleted=0;
    private String password="";//登录密码
    private String logincode="";//登录账号
    
    public EnterpriseInfo() {
    	super();
    	this.id = UUID.randomUUID().toString();
    }

    
    public int getIs_deleted() {
		return is_deleted;
	}


	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}

	public EnterpriseInfo(String id, String enterprisename, String licenseno, String operatorname,
			String operatoridcard, String operatormobile, String bankaccount, String idcardpositive,
			String idcardnegative, String licenseimg, String enterprisetype, String addr, String memo, String privince,
			String city, String status) {
		super();
		this.id = id;
		this.enterprisename = enterprisename;
		this.licenseno = licenseno;
		this.operatorname = operatorname;
		this.operatoridcard = operatoridcard;
		this.operatormobile = operatormobile;
		this.bankaccount = bankaccount;
		this.idcardpositive = idcardpositive;
		this.idcardnegative = idcardnegative;
		this.licenseimg = licenseimg;
		this.enterprisetype = enterprisetype;
		this.addr = addr;
		this.memo = memo;
		this.privince = privince;
		this.city = city;
		this.status = status;
	}
 
	public String getEnterprisename() {
		return enterprisename;
	}


	public void setEnterprisename(String enterprisename) {
		this.enterprisename = enterprisename;
	}


	public String getLicenseno() {
		return licenseno;
	}


	public void setLicenseno(String licenseno) {
		this.licenseno = licenseno;
	}


	public String getOperatorname() {
		return operatorname;
	}


	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}


	public String getOperatoridcard() {
		return operatoridcard;
	}


	public void setOperatoridcard(String operatoridcard) {
		this.operatoridcard = operatoridcard;
	}


	public String getOperatormobile() {
		return operatormobile;
	}


	public void setOperatormobile(String operatormobile) {
		this.operatormobile = operatormobile;
	}


	public String getBankaccount() {
		return bankaccount;
	}


	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
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


	public String getLicenseimg() {
		return licenseimg;
	}


	public void setLicenseimg(String licenseimg) {
		this.licenseimg = licenseimg;
	}


	public String getEnterprisetype() {
		return enterprisetype;
	}


	public void setEnterprisetype(String enterprisetype) {
		this.enterprisetype = enterprisetype;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getLogincode() {
		return logincode;
	}


	public void setLogincode(String logincode) {
		this.logincode = logincode;
	}


	public String getId() {
        return id;
    }

    public void setId(String id) {
    	this.id = id;
    }

    public String getEnterprise_name() {
        return enterprisename;
    }

    public void setEnterprise_name(String enterprise_name) {
        this.enterprisename = enterprise_name;
    }

    public String getLicense_no() {
        return licenseno;
    }

    public void setLicense_no(String license_no) {
        this.licenseno = license_no;
    }

    public String getOperator_name() {
        return operatorname;
    }

    public void setOperator_name(String operator_name) {
        this.operatorname = operator_name;
    }

    public String getOperator_id_card() {
        return operatoridcard;
    }

    public void setOperator_id_card(String operator_id_card) {
        this.operatoridcard = operator_id_card;
    }

    public String getOperator_mobile() {
        return operatormobile;
    }

    public void setOperator_mobile(String operator_mobile) {
        this.operatormobile = operator_mobile;
    }

    public String getBank_account() {
        return bankaccount;
    }

    public void setBank_account(String bank_account) {
        this.bankaccount = bank_account;
    }

    public String getId_card_positive() {
        return idcardpositive;
    }

    public void setId_card_positive(String id_card_positive) {
        this.idcardpositive = id_card_positive;
    }

    public String getId_card_negative() {
        return idcardnegative;
    }

    public void setId_card_negative(String id_card_negative) {
        this.idcardnegative = id_card_negative;
    }

    public String getLicense_img() {
        return licenseimg;
    }

    public void setLicense_img(String license_img) {
        this.licenseimg = license_img;
    }

    public String getEnterprise_type() {
        return enterprisetype;
    }

    public void setEnterprise_type(String enterprise_type) {
        this.enterprisetype = enterprise_type;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPrivince() {
        return privince;
    }

    public void setPrivince(String privince) {
        this.privince = privince;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String toJson(){
    	StringBuffer enStr=new StringBuffer();
    	enStr.append("{\"id\":").append("\""+this.id+"\"");
    	enStr.append(",\"enterprisename\":").append("\""+this.enterprisename+"\"");
    	enStr.append(",\"licenseno\":").append("\""+this.licenseno+"\"");
    	enStr.append(",\"operatorname\":").append("\""+this.operatorname+"\"");
    	enStr.append(",\"operatoridcard\":").append("\""+this.operatoridcard+"\"");
    	enStr.append(",\"operatormobile\":").append("\""+this.operatormobile+"\"");
    	enStr.append(",\"bankaccount\":").append("\""+this.bankaccount+"\"");
    	enStr.append(",\"idcardpositive\":").append("\""+this.idcardpositive+"\"");
    	enStr.append(",\"idcardnegative\":").append("\""+this.idcardnegative+"\"");
    	enStr.append(",\"licenseimg\":").append("\""+this.licenseimg+"\"");
    	enStr.append(",\"enterprisetype\":").append("\""+this.enterprisetype+"\"");
    	enStr.append(",\"addr\":").append("\""+this.addr+"\"");
    	enStr.append(",\"memo\":").append("\""+this.memo+"\"");
    	enStr.append(",\"privince\":").append("\""+this.privince+"\"");
    	enStr.append(",\"city\":").append("\""+this.city+"\"");
    	enStr.append(",\"status\":").append("\""+this.status+"\"");
    	enStr.append(",\"logincode\":").append("\""+this.logincode+"\"");
    	enStr.append(",\"password\":").append("\""+this.password+"\"");
    	enStr.append("}");
    	return enStr.toString();
    }
}
