package com.ebao.hospitaldapp.ebao.entity;

import lombok.Getter;
import lombok.Setter;

public class PrescriptionDetail {

    @Getter @Setter private int id;
    @Getter @Setter private String mi_id;
    @Getter @Setter private String presgroupid;
    @Getter @Setter private String hosp_id;
    @Getter @Setter private String presgroupname;
    @Getter @Setter private String patientname;
    @Getter @Setter private String patientgend;
    @Getter @Setter private String age;
    @Getter @Setter private String treatmentdate;
    @Getter @Setter private String deptid;
    @Getter @Setter private String deptname;
    @Getter @Setter private String doctorid;
    @Getter @Setter private String doctorname;
    @Getter @Setter private String prediagicd;
    @Getter @Setter private String prediagname;
    @Getter @Setter private String treatmentcoststatus;//	支付状态
    @Getter @Setter private String applytime; //	申请时间
    @Getter @Setter private String exedeptid;
    @Getter @Setter private String exedeptname;
    @Getter @Setter private String operator;
    @Getter @Setter private String treatmentcosttype;
    @Getter @Setter private String itemname;
    @Getter @Setter private String itemtype;
    @Getter @Setter private String siitemtype;
    @Getter @Setter private String treatmentcostsubtype;
    @Getter @Setter private String hospitemid;
    @Getter @Setter private String siitemid;
    @Getter @Setter private String itemdate;
    @Getter @Setter private String spec;
    @Getter @Setter private String price;
    @Getter @Setter private String num;
    @Getter @Setter private String amount;
    @Getter @Setter private String dosageforms;
    @Getter @Setter private String dose;
    @Getter @Setter private String measunit;
    @Getter @Setter private String usage;
    @Getter @Setter private String usefreq;
    @Getter @Setter private String exedays;
    @Getter @Setter private String position;
    @Getter @Setter private String specimen;

}

