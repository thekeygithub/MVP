package com.yibao.mobileapp.entity;

import java.io.Serializable;

/**
 * Created by root on 2018/4/18.
 */

public class PrescriptionDetailEntity implements Serializable {
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
    private String tid;
    private String exedays;

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDosageforms() {
        return dosageforms;
    }

    public void setDosageforms(String dosageforms) {
        this.dosageforms = dosageforms;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getMeasunit() {
        return measunit;
    }

    public void setMeasunit(String measunit) {
        this.measunit = measunit;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getUsefreq() {
        return usefreq;
    }

    public void setUsefreq(String usefreq) {
        this.usefreq = usefreq;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getExedays() {
        return exedays;
    }

    public void setExedays(String exedays) {
        this.exedays = exedays;
    }
}
