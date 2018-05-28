package com.ebao.hospitaldapp.rest.entity.response;

import lombok.Data;

@Data
public class PatientModel {

    private String patientId;
    private String name;
    private String gender;
    private String age;
    private String idNumber;
    private String treatmentId;

}
