package com.xczg.blockchain.shangbaodapp.bean;

import com.xczg.blockchain.yibaodapp.util.StringUtil;

public class ProductApply {
    private String apply_user_name="";
    private String apply_phone="";
    private String apply_id_card="";
    private String check_user_code="";
    private String check_desc="";
    private String product_name="";
    private String product_insured_age="";
    private String product_money="";
    private String product_insurance_period="";
    private String product_payment_time="";
    private String product_payment_method="";
    private String product_hesitation="";
    private String product_waiting_period="";
    private String product_insurance_area="";
    private String check_user_name="";
    private String status="";
    private String organize_id="";
    private String apply_user_id="";
    private long id=0;
    private long apply_time=0;
    private long check_time=0;
    private String chain_key="";
    private String amount;
    private String audit_results;
    private String payment_status;
	private String authCode="";//个人档案授权码
	private double pay_amount;
	private String contractNo;
	private String insured_name;

	public double getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(double pay_amount) {
		this.pay_amount = pay_amount;
	}

	public ProductApply() {
    	super();
    }
    
    public ProductApply(  String apply_user_name,
    	     String apply_phone,
    	     String apply_id_card,
    	     String check_user_code,
    	     String check_desc,
    	     String product_name,
    	     String product_insured_age,
    	     String product_money,
    	     String product_insurance_period,
    	     String product_payment_time,
    	     String product_payment_method,
    	     String product_hesitation,
    	     String product_waiting_period,
    	     String product_insurance_area,
    	     String check_user_name,
    	     String status,
    	     String organize_id,
    	     String apply_user_id,
    	     long id,
    	     long apply_time,
    	     long check_time,
			 double pay_amount) {
    	            super();
    	             this.apply_user_name=apply_user_name;
    			     this.apply_phone=apply_phone;
    			     this.apply_id_card=apply_id_card;
    			     this.check_user_code=check_user_code;
    			     this.check_desc=check_desc;
    			     this.product_name=product_name;
    			     this.product_insured_age=product_insured_age;
    			     this.product_money=product_money;
    			     this.product_insurance_period=product_insurance_period;
    			     this.product_payment_time=product_payment_time;
    			     this.product_payment_method=product_payment_method;
    			     this.product_hesitation=product_hesitation;
    			     this.product_waiting_period=product_waiting_period;
    			     this.product_insurance_area=product_insurance_area;
    			     this.check_user_name=check_user_name;
    			     this.status=status;
    			     this.organize_id=organize_id;
    			     this.apply_user_id=apply_user_id;
    			     this.id=id;
    			     this.apply_time=apply_time;
    			     this.check_time=check_time;
    			     this.pay_amount=pay_amount;
    }

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String toJson(){
        StringBuffer str=new StringBuffer();
        str.append("{");
        str.append("\"apply_user_name\":\"").append(StringUtil.render(this.apply_user_name, false, "")).append("\"");
        str.append(",\"apply_phone\":\"").append(StringUtil.render(this.apply_phone, false, "")).append("\"");
        str.append(",\"apply_id_card\":\"").append(StringUtil.render(this.apply_id_card, false, "")).append("\"");
        str.append(",\"check_user_code\":\"").append(StringUtil.render(this.check_user_code, false, "")).append("\"");
        str.append(",\"check_desc\":\"").append(StringUtil.render(this.check_desc, false, "")).append("\"");
        str.append(",\"product_name\":\"").append(StringUtil.render(this.product_name, false, "")).append("\"");
        str.append(",\"product_insured_age\":\"").append(StringUtil.render(this.product_insured_age, false, "")).append("\"");
        str.append(",\"product_money\":\"").append(StringUtil.render(this.product_money, false, "")).append("\"");
        str.append(",\"product_insurance_period\":\"").append(StringUtil.render(this.product_insurance_period, false, "")).append("\"");
        str.append(",\"product_payment_time\":\"").append(StringUtil.render(this.product_payment_time, false, "")).append("\"");
        str.append(",\"contractNo\":\"").append(StringUtil.render(this.contractNo, false, "")).append("\"");
        str.append(",\"product_payment_method\":\"").append(StringUtil.render(this.product_payment_method, false, "")).append("\"");
        str.append(",\"product_hesitation\":\"").append(StringUtil.render(this.product_hesitation, false, "")).append("\"");
        str.append(",\"product_waiting_period\":\"").append(StringUtil.render(this.product_waiting_period, false, "")).append("\"");
        str.append(",\"product_insurance_area\":\"").append(StringUtil.render(this.product_insurance_area, false, "")).append("\"");
        str.append(",\"check_user_name\":\"").append(StringUtil.render(this.check_user_name, false, "")).append("\"");
        str.append(",\"status\":\"").append(StringUtil.render(this.status, false, "")).append("\"");
        str.append(",\"organize_id\":\"").append(StringUtil.render(this.organize_id, false, "")).append("\"");
        str.append(",\"apply_user_id\":\"").append(StringUtil.render(this.apply_user_id, false, "")).append("\"");
        str.append(",\"authCode\":\"").append(StringUtil.render(this.authCode, false, "")).append("\"");
        str.append(",\"insured_name\":\"").append(StringUtil.render(this.insured_name, false, "")).append("\"");
        str.append(",\"id\":").append(this.id);
        str.append(",\"amount\":\"").append(StringUtil.render(this.amount, false, "")).append("\"");
        str.append(",\"audit_results\":\"").append(StringUtil.render(this.audit_results, false, "")).append("\"");
        str.append(",\"payment_status\":\"").append(StringUtil.render(this.payment_status, false, "")).append("\"");
        str.append(",\"pay_amount\":\"").append(this.pay_amount).append("\"");
        str.append(",\"apply_time\":").append(this.apply_time);
        str.append(",\"check_time\":").append(this.check_time);
        str.append("}");
        return str.toString();
   }

	public String getInsured_name() {
		return insured_name;
	}

	public void setInsured_name(String insured_name) {
		this.insured_name = insured_name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAudit_results() {
		return audit_results;
	}

	public void setAudit_results(String audit_results) {
		this.audit_results = audit_results;
	}

	public String getPayment_status() {
		return payment_status;
	}

	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}
    
    public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getChain_key() {
		return chain_key;
	}

	public void setChain_key(String chain_key) {
		this.chain_key = chain_key;
	}
    
	public String getApply_user_name() {
		return apply_user_name;
	}
	public void setApply_user_name(String apply_user_name) {
		this.apply_user_name = apply_user_name;
	}
	public String getApply_phone() {
		return apply_phone;
	}
	public void setApply_phone(String apply_phone) {
		this.apply_phone = apply_phone;
	}
	public String getApply_id_card() {
		return apply_id_card;
	}
	public void setApply_id_card(String apply_id_card) {
		this.apply_id_card = apply_id_card;
	}
	public String getCheck_user_code() {
		return check_user_code;
	}
	public void setCheck_user_code(String check_user_code) {
		this.check_user_code = check_user_code;
	}
	public String getCheck_desc() {
		return check_desc;
	}
	public void setCheck_desc(String check_desc) {
		this.check_desc = check_desc;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_insured_age() {
		return product_insured_age;
	}
	public void setProduct_insured_age(String product_insured_age) {
		this.product_insured_age = product_insured_age;
	}
	public String getProduct_money() {
		return product_money;
	}
	public void setProduct_money(String product_money) {
		this.product_money = product_money;
	}
	public String getProduct_insurance_period() {
		return product_insurance_period;
	}
	public void setProduct_insurance_period(String product_insurance_period) {
		this.product_insurance_period = product_insurance_period;
	}
	public String getProduct_payment_time() {
		return product_payment_time;
	}
	public void setProduct_payment_time(String product_payment_time) {
		this.product_payment_time = product_payment_time;
	}
	public String getProduct_payment_method() {
		return product_payment_method;
	}
	public void setProduct_payment_method(String product_payment_method) {
		this.product_payment_method = product_payment_method;
	}
	public String getProduct_hesitation() {
		return product_hesitation;
	}
	public void setProduct_hesitation(String product_hesitation) {
		this.product_hesitation = product_hesitation;
	}
	public String getProduct_waiting_period() {
		return product_waiting_period;
	}
	public void setProduct_waiting_period(String product_waiting_period) {
		this.product_waiting_period = product_waiting_period;
	}
	public String getProduct_insurance_area() {
		return product_insurance_area;
	}
	public void setProduct_insurance_area(String product_insurance_area) {
		this.product_insurance_area = product_insurance_area;
	}
	public String getCheck_user_name() {
		return check_user_name;
	}
	public void setCheck_user_name(String check_user_name) {
		this.check_user_name = check_user_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrganize_id() {
		return organize_id;
	}
	public void setOrganize_id(String organize_id) {
		this.organize_id = organize_id;
	}
	public String getApply_user_id() {
		return apply_user_id;
	}
	public void setApply_user_id(String apply_user_id) {
		this.apply_user_id = apply_user_id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getApply_time() {
		return apply_time;
	}
	public void setApply_time(long apply_time) {
		this.apply_time = apply_time;
	}
	public long getCheck_time() {
		return check_time;
	}
	public void setCheck_time(long check_time) {
		this.check_time = check_time;
	}
    
    

}
