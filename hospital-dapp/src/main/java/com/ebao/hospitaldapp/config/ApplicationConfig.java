package com.ebao.hospitaldapp.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;


@Data
@ConfigurationProperties(prefix = "dapp")
@Configuration
public class ApplicationConfig {
    private String ipfsUrl;
    private Map<String, String> fakePassword;
    private String hospitalConfigFile;
    private String hospitalRedisKey;
    private String ebaoPublicKey;
    private String userAccountConfigFile;
    private String hospitalAccountConfigFile;
    private String tkyExchangeRate;
    private String neoCliUrl;
    private String nep5AssetId;
    private String verifyScriptHash;
    private String transferContractAddr;
    private String saveToContractUrl;
    private String verificationFee;
    private String getUnpaidUserUrl;
    private String getPrescriptionUrl;
    private String notifyEbaoVerifyUrl;
    private String notifyEbaoVerifyUrl2;
    private String notifyEbaoPayUrl;
    private String notifyEbaoSaveTxUrl;
    private boolean IPFSMode;
    private boolean faceMode;
    private Map<String, String> superMobileNumbers;
}
