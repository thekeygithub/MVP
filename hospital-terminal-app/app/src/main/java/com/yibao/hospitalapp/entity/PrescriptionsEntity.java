package com.yibao.hospitalapp.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 2018/4/18.
 */

public class PrescriptionsEntity implements Serializable{
    private String prescriptionId="";
    private String prescriptionName="";
    private String doctorName="";
    private String deptName="";
    private String diagnosis="";
    private String treatmentDate="";
    private double payActully;
    private double reimbursement;
    private boolean isSelect;
    private ArrayList<PrescriptionDetailEntity> prescriptionDetails;


    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPrescriptionName() {
        return prescriptionName;
    }

    public void setPrescriptionName(String prescriptionName) {
        this.prescriptionName = prescriptionName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(String treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public ArrayList<PrescriptionDetailEntity> getPrescriptionDetails() {
        return prescriptionDetails;
    }

    public void setPrescriptionDetails(ArrayList<PrescriptionDetailEntity> prescriptionDetails) {
        this.prescriptionDetails = prescriptionDetails;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public double getPayActully() {
        return payActully;
    }

    public void setPayActully(double payActully) {
        this.payActully = payActully;
    }

    public double getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(double reimbursement) {
        this.reimbursement = reimbursement;
    }
}
