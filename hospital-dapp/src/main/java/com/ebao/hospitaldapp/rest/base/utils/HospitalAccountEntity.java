package com.ebao.hospitaldapp.rest.base.utils;

import lombok.Data;

@Data
public class HospitalAccountEntity {
    private String hospId;
    private String hospName;
    private String hospAddr;
    private String hospGrade;//等-甲等乙等
    private String hospClass;//级01,02,03
    private String hospType;
    private String publicKey;
    private String privateKey;
    private String walletAddr;
    private String wallet;
    private String walletPwd;
}
