package com.yibao.hospitalapp.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 2018/4/17.
 */

public class UserInfoListEntity implements Serializable {
    private String code;
    private String message;
    private ArrayList<UserInfoEntity> patientList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<UserInfoEntity> getPatientList() {
        return patientList;
    }

    public void setPatientList(ArrayList<UserInfoEntity> patientList) {
        this.patientList = patientList;
    }
}
