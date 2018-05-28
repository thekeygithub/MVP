package com.ebao.hospitaldapp.rest.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class EbaoDmiResultEntity {

    private String blockKey;
    private String hospitalId;
    private String jzlsno;
    private String newHash;
    private String serialNo;

}