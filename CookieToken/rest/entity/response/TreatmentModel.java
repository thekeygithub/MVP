package com.ebao.hospitaldapp.rest.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class TreatmentModel {

    private String patientId;
    private String treatmentId;
    private List<PrescriptionModel> prescriptions;

}
