package com.xczg.blockchain.yibaodapp.bean;

public class EnterPriseOfQueryCodition {
    private String sEcho = "";
    private  int iDisplayStart ;
    private  int iDisplayLength ;
    private  String orderAz= "";
    private   String OrderName= "";
    private   String qenterprisename = "";
    private   String qoperatorname =  "";
    private  String qoperatoridcard = "";
    private   String qoperatormobile = "";
    private   String qlicenseno = "";
    private   String qstatus = "";

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public String getOrderAz() {
        return orderAz;
    }

    public void setOrderAz(String orderAz) {
        this.orderAz = orderAz;
    }

    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }

    public String getQenterprisename() {
        return qenterprisename;
    }

    public void setQenterprisename(String qenterprisename) {
        this.qenterprisename = qenterprisename;
    }

    public String getQoperatorname() {
        return qoperatorname;
    }

    public void setQoperatorname(String qoperatorname) {
        this.qoperatorname = qoperatorname;
    }

    public String getQoperatoridcard() {
        return qoperatoridcard;
    }

    public void setQoperatoridcard(String qoperatoridcard) {
        this.qoperatoridcard = qoperatoridcard;
    }

    public String getQoperatormobile() {
        return qoperatormobile;
    }

    public void setQoperatormobile(String qoperatormobile) {
        this.qoperatormobile = qoperatormobile;
    }

    public String getQlicenseno() {
        return qlicenseno;
    }

    public void setQlicenseno(String qlicenseno) {
        this.qlicenseno = qlicenseno;
    }

    public String getQstatus() {
        return qstatus;
    }

    public void setQstatus(String qstatus) {
        this.qstatus = qstatus;
    }

    public EnterPriseOfQueryCodition(String sEcho, int iDisplayStart, int iDisplayLength, String orderAz, String orderName, String qenterprisename, String qoperatorname, String qoperatoridcard, String qoperatormobile, String qlicenseno, String qstatus) {
        this.sEcho = sEcho;
        this.iDisplayStart = iDisplayStart;
        this.iDisplayLength = iDisplayLength;
        this.orderAz = orderAz;
        OrderName = orderName;
        this.qenterprisename = qenterprisename;
        this.qoperatorname = qoperatorname;
        this.qoperatoridcard = qoperatoridcard;
        this.qoperatormobile = qoperatormobile;
        this.qlicenseno = qlicenseno;
        this.qstatus = qstatus;
    }

    public EnterPriseOfQueryCodition() {

    }
}
