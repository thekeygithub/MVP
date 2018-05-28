package com.ebao.hospitaldapp.ipfs.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

//@Data
public class E01_INP_LIST01 {
    private String itemname;                  //药品名称        itemname
    @JSONField(name = "itemname")
    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    private String E01_INP_LIST01_NO01;     //药品MTS返回编码 siitemid

    @JSONField(name = "E01_INP_LIST01_NO01")
    public String getE01_INP_LIST01_NO01() {
        return E01_INP_LIST01_NO01;
    }

    public void setE01_INP_LIST01_NO01(String e01_INP_LIST01_NO01) {
        E01_INP_LIST01_NO01 = e01_INP_LIST01_NO01;
    }

    private String E01_INP_LIST01_NO02;        //药品数量          num

    @JSONField(name = "E01_INP_LIST01_NO02")
    public String getE01_INP_LIST01_NO02() {
        return E01_INP_LIST01_NO02;
    }

    public void setE01_INP_LIST01_NO02(String e01_INP_LIST01_NO02) {
        E01_INP_LIST01_NO02 = e01_INP_LIST01_NO02;
    }
}
