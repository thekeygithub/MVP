package com.ebao.hospitaldapp.ipfs.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

//@Data
public class E01_INP_LIST02 {
    private String itemname;                //诊疗名称  itemname

    @JSONField(name = "itemname")
    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    private String E01_INP_LIST02_NO01;    //诊疗MTS返回编码  siitemid

    @JSONField(name = "E01_INP_LIST02_NO01")
    public String getE01_INP_LIST02_NO01() {
        return E01_INP_LIST02_NO01;
    }

    public void setE01_INP_LIST02_NO01(String e01_INP_LIST02_NO01) {
        E01_INP_LIST02_NO01 = e01_INP_LIST02_NO01;
    }

    @JSONField(name = "E01_INP_LIST02_NO02")
    public String getE01_INP_LIST02_NO02() {
        return E01_INP_LIST02_NO02;
    }

    public void setE01_INP_LIST02_NO02(String e01_INP_LIST02_NO02) {
        E01_INP_LIST02_NO02 = e01_INP_LIST02_NO02;
    }

    private String E01_INP_LIST02_NO02;        //诊疗频次  num
}
