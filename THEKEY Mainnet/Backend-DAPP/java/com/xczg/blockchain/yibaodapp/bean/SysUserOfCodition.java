package com.xczg.blockchain.yibaodapp.bean;

public class SysUserOfCodition {
    private  String sEcho ="";
    private  int iDisplayStart ;
    private  int iDisplayLength ;
    private  String OrderAz="";
    private  String OrderName="";
    private  String qusername ="";
    private  String qsex ="";
    private  String qemail ="";
    private  String qmobile ="";
    private  String qjobtitle ="";
    private  String qofficephone ="";
    private  String qjoblever ="";
    private  String qstatus ="";

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
        return OrderAz;
    }

    public void setOrderAz(String orderAz) {
        this.OrderAz = orderAz;
    }

    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }

    public String getQusername() {
        return qusername;
    }

    public void setQusername(String qusername) {
        this.qusername = qusername;
    }

    public String getQsex() {
        return qsex;
    }

    public void setQsex(String qsex) {
        this.qsex = qsex;
    }

    public String getQemail() {
        return qemail;
    }

    public void setQemail(String qemail) {
        this.qemail = qemail;
    }

    public String getQmobile() {
        return qmobile;
    }

    public void setQmobile(String qmobile) {
        this.qmobile = qmobile;
    }

    public String getQjobtitle() {
        return qjobtitle;
    }

    public void setQjobtitle(String qjobtitle) {
        this.qjobtitle = qjobtitle;
    }

    public String getQofficephone() {
        return qofficephone;
    }

    public void setQofficephone(String qofficephone) {
        this.qofficephone = qofficephone;
    }

    public String getQjoblever() {
        return qjoblever;
    }

    public void setQjoblever(String qjoblever) {
        this.qjoblever = qjoblever;
    }

    public String getQstatus() {
        return qstatus;
    }

    public void setQstatus(String qstatus) {
        this.qstatus = qstatus;
    }

    public SysUserOfCodition() {
    }

    public SysUserOfCodition(String sEcho, int iDisplayStart, int iDisplayLength, String orderAz, String orderName, String qusername, String qsex, String qemail, String qmobile, String qjobtitle, String qofficephone, String qjoblever, String qstatus) {
        this.sEcho = sEcho;
        this.iDisplayStart = iDisplayStart;
        this.iDisplayLength = iDisplayLength;
        this.OrderAz = orderAz;
        this.OrderName = orderName;
        this.qusername = qusername;
        this.qsex = qsex;
        this.qemail = qemail;
        this.qmobile = qmobile;
        this.qjobtitle = qjobtitle;
        this.qofficephone = qofficephone;
        this.qjoblever = qjoblever;
        this.qstatus = qstatus;
    }
}
