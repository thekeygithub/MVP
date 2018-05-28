package com.ebao.hospitaldapp.ebao.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserInfoEntity {

    @Getter @Setter private String hosp_id;
    @Getter @Setter private String create_at;
    @Getter @Setter private int patient_id;
    @Getter @Setter private String name;                //姓名
    @Getter @Setter private String gender;              //性别
    @Getter @Setter private String nation;              //民族
    @Getter @Setter private String birthday;            //出生日期
    @Getter @Setter private String age;                 //年龄
    @Getter @Setter private String regist_nature;      //户口性质
    @Getter @Setter private String id_type;             //证件类型
    @Getter @Setter private String id_no;                //证件号码
    @Getter @Setter private String comm_addr;           //通讯地址
    @Getter @Setter private String zip_code;            //邮编
    @Getter @Setter private String registloca_addr;    //户口所在地地址
    @Getter @Setter private String jzlsno;              //就诊流水号
    @Getter @Setter private String jzks;                //就诊科室
    @Getter @Setter private String ksmc;                //科室名称

 //   @Getter @Setter private String certNo;

}
