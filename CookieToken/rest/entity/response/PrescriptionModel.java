package com.ebao.hospitaldapp.rest.entity.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PrescriptionModel {

    private String prescriptionId;
    private String prescriptionName;
    private String doctorName;
    private String deptName;
    private String diagnosis;
    private String treatmentDate;
    private BigDecimal payActully;
    private BigDecimal reimbursement;
    private List<PrescriptionDetailModel> prescriptionDetails;

}
