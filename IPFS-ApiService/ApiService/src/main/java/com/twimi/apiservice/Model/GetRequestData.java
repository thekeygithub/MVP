package com.twimi.apiservice.Model;

public class GetRequestData {
    private String hashId;
    private String infoType;

    public String getHashId() {
        return hashId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }
}
