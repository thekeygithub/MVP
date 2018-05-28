package com.ebao.hospitaldapp.rest.entity.response;

import lombok.Data;

@Data
public class PrescriptionDetailModel {

    private String treatmentType;
    private String itemName;
    private String specification;
    private String price;
    private String num;
    private String amount;
    private String dosageforms;
    private String dose;
    private String measunit;
    private String usage;
    private String usefreq;
    private String exedays;
}
