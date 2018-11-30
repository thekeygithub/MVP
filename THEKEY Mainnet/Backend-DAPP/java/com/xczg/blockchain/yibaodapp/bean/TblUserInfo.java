package com.xczg.blockchain.yibaodapp.bean;

import com.xczg.blockchain.yibaodapp.util.StringUtil;

import java.util.UUID;

public class TblUserInfo {
    private String verified_id ;
    private String name   ;
    private String sex ;
    private String id_card     ;
    private String social_id ;
    private int is_deleted =0 ;
    private String phone;
    private int is_certification=0;
    public int getIs_certification() {
		return is_certification;
	}
	public void setIs_certification(int is_certification) {
		this.is_certification = is_certification;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public TblUserInfo( String name, String sex, String id_card, String social_id) {
        this.verified_id = UUID.randomUUID().toString();
        this.name = name;
        this.sex = sex;
        this.id_card = id_card;
        this.social_id = social_id;
        this.is_deleted =is_deleted;
    }

    public TblUserInfo() {
        super();
        this.verified_id = UUID.randomUUID().toString();
    }

    public String getVerified_id() {
        return verified_id;
    }

    public void setVerified_id() {
        this.verified_id = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }
    public String toJson(){
        StringBuffer str=new StringBuffer();
        str.append("{");
        str.append("\"verified_id\":\"").append(StringUtil.render(this.verified_id, false, "")).append("\"");
        str.append(",\"name\":\"").append(StringUtil.render(this.name, false, "")).append("\"");
        str.append(",\"sex\":\"").append(StringUtil.render(this.sex, false, "")).append("\"");
        str.append(",\"id_card\":\"").append(StringUtil.render(this.id_card, false, "")).append("\"");
        str.append(",\"social_id\":").append(this.social_id);
        str.append(",\"phone\":").append(this.phone);
        str.append(",\"is_certification\":").append(this.is_certification);
        str.append("}");
        return str.toString();
    }
}
