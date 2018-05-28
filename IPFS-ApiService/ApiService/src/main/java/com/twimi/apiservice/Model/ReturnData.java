package com.twimi.apiservice.Model;

//Used to return all
public class ReturnData {

   // private UploadData data;

    private String rsJsonString="";

    private String rsCode;
/*
    public void setData(UploadData data) {
        this.data = data;
    }

    public UploadData getData() {
        return data;
    }
*/
    public String getRsCode() {
        return rsCode;
    }

    public void setRsCode(String rsCode) {
        this.rsCode = rsCode;
    }

    public String getRsJsonString() {
        return rsJsonString;
    }

    public void setRsJsonString(String rsJsonString) {
        this.rsJsonString = rsJsonString;
    }
}
