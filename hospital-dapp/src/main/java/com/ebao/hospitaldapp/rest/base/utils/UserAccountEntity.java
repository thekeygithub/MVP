package com.ebao.hospitaldapp.rest.base.utils;

import lombok.Data;

@Data
public class UserAccountEntity {

    private String ID;
    private String payPwd;
    private String wallet;
    private String walletAddr;
    private String walletPwd;
    private String name;
    private String mobile;
}
