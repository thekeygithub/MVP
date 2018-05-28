package com.ebao.hospitaldapp.ebao.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrescriptionGroup {

    private int id;
    private String jzlsno;
    private String presgroupid;
    private String hosp_id;
    private String treatment_date;
    private int patient_id;
    private String create_at;
    private String med_type;
    private String dis_code;
    private String op_type;
    private List<PrescriptionDetail> mx = new ArrayList<PrescriptionDetail>();

    public  List<PrescriptionDetail> getMx()
    {
        return mx;
    }

    public void AddPresDetail(PrescriptionDetail detail)
    {
        mx.add(detail);
    }

    public PrescriptionDetail getPrescriptionDetail(int idx)
    {
        if(mx.size()>0)
            return mx.get(idx);
        else
            return null;
    }

}
