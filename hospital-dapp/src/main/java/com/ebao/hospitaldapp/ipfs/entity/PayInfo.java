package com.ebao.hospitaldapp.ipfs.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//@Data
public class PayInfo {

    private String apiType;         //业务接口编号    "Ebao01"

    @JSONField(name = "apiType")
    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    private String E01_INP_NO01;    //结算单单号,date-hospId-jzlsno

    @JSONField(name = "E01_INP_NO01")
    public String getE01_INP_NO01() {
        return E01_INP_NO01;
    }

    public void setE01_INP_NO01(String e01_INP_NO01) {
        E01_INP_NO01 = e01_INP_NO01;
    }

    private String E01_INP_NO02;    //结算单日期,yyyy-MM-dd
    @JSONField(name = "E01_INP_NO02")
    public String getE01_INP_NO02() {
        return E01_INP_NO02;
    }

    public void setE01_INP_NO02(String e01_INP_NO02) {
        E01_INP_NO02 = e01_INP_NO02;
    }

    private String E01_INP_NO03;    //患者姓名  patientname
    @JSONField(name = "E01_INP_NO03")
    public String getE01_INP_NO03() {
        return E01_INP_NO03;
    }

    public void setE01_INP_NO03(String e01_INP_NO03) {
        E01_INP_NO03 = e01_INP_NO03;
    }

    private String E01_INP_NO04;    //患者身份证号    userInfo:id_no
    @JSONField(name = "E01_INP_NO04")
    public String getE01_INP_NO04() {
        return E01_INP_NO04;
    }

    public void setE01_INP_NO04(String e01_INP_NO04) {
        E01_INP_NO04 = e01_INP_NO04;
    }

    private String mi_id;            //患者社保卡号   mi_id
    @JSONField(name = "mi_id")
    public String getMi_id() {
        return mi_id;
    }

    public void setMi_id(String mi_id) {
        this.mi_id = mi_id;
    }

    private String deptname;        //就诊科室      deptname
    @JSONField(name = "deptname")
    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    private String med_type;        //就诊类型 字典 11代表普通门诊--名称  med_type
    @JSONField(name = "med_type")
    public String getMed_type() {
        return med_type;
    }

    public void setMed_type(String med_type) {
        this.med_type = med_type;
    }

    private String prediagname;     //诊断    prediagname

    @JSONField(name = "prediagname")
    public String getPrediagname() {
        return prediagname;
    }

    public void setPrediagname(String prediagname) {
        this.prediagname = prediagname;
    }

    private String treatmentdate;   //就诊日期  treatmentdate
    @JSONField(name = "treatmentdate")
    public String getTreatmentdate() {
        return treatmentdate;
    }

    public void setTreatmentdate(String treatmentdate) {
        this.treatmentdate = treatmentdate;
    }

    private String patientgend;     //性别名称  patientgend
    @JSONField(name = "patientgend")
    public String getPatientgend() {
        return patientgend;
    }

    public void setPatientgend(String patientgend) {
        this.patientgend = patientgend;
    }

    private String E01_INP_NO05;       //患者性别，字典表，男1女2
    @JSONField(name = "E01_INP_NO05")
    public String getE01_INP_NO05() {
        return E01_INP_NO05;
    }

    public void setE01_INP_NO05(String e01_INP_NO05) {
        E01_INP_NO05 = e01_INP_NO05;
    }

    private String E01_INP_NO06;    //出生日期  birthday
    @JSONField(name = "E01_INP_NO06")
    public String getE01_INP_NO06() {
        return E01_INP_NO06;
    }

    public void setE01_INP_NO06(String e01_INP_NO06) {
        E01_INP_NO06 = e01_INP_NO06;
    }

    private String E01_INP_NO07;    //由hospId找到医院名
    @JSONField(name = "E01_INP_NO07")
    public String getE01_INP_NO07() {
        return E01_INP_NO07;
    }

    public void setE01_INP_NO07(String e01_INP_NO07) {
        E01_INP_NO07 = e01_INP_NO07;
    }

    private String E01_INP_NO08;       //医疗机构级别class,字典表，3代表三级
    @JSONField(name = "E01_INP_NO08")
    public String getE01_INP_NO08() {
        return E01_INP_NO08;
    }

    public void setE01_INP_NO08(String e01_INP_NO08) {
        E01_INP_NO08 = e01_INP_NO08;
    }

    private String E01_INP_NO09;       //医疗机构等级grade,字典表，3代表甲等
    @JSONField(name = "E01_INP_NO09")
    public String getE01_INP_NO09() {
        return E01_INP_NO09;
    }

    public void setE01_INP_NO09(String e01_INP_NO09) {
        E01_INP_NO09 = e01_INP_NO09;
    }

    private String E01_INP_NO10;    //医疗机构类型,字典表，A100代表综合医院
    @JSONField(name = "E01_INP_NO10")
    public String getE01_INP_NO10() {
        return E01_INP_NO10;
    }

    public void setE01_INP_NO10(String e01_INP_NO10) {
        E01_INP_NO10 = e01_INP_NO10;
    }

    //siitemtype用来判断是药品(1)还是诊疗(2)
    private List<E01_INP_LIST01> E01_INP_LIST01 = new ArrayList<>();   //结算单药品集合

    @JSONField(name = "E01_INP_LIST01")
    public List<com.ebao.hospitaldapp.ipfs.entity.E01_INP_LIST01> getE01_INP_LIST01() {
        return E01_INP_LIST01;
    }

    private List<E01_INP_LIST02> E01_INP_LIST02 = new ArrayList<>() ;   //结算单诊疗集合
    @JSONField(name = "E01_INP_LIST02")
    public List<com.ebao.hospitaldapp.ipfs.entity.E01_INP_LIST02> getE01_INP_LIST02() {
        return E01_INP_LIST02;
    }
}
