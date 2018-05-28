package com.ebao.hospitaldapp.ebao.entity;

import lombok.Getter;
import lombok.Setter;

public class EbaoDappEntity {

    @Getter @Setter private String ID;
    @Getter @Setter private String hospitalID;
    @Getter @Setter private String validKey;
    @Getter @Setter private String transferSerial;
    @Getter @Setter private String publicKey;
    @Getter @Setter private String senderName;
    @Getter @Setter private String senderAddr;
    @Getter @Setter private String type;
    @Getter @Setter private String Fee;
}
