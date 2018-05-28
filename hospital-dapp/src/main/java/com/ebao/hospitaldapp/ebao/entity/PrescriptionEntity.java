package com.ebao.hospitaldapp.ebao.entity;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionEntity {

    //医院+流水号，可以作为一个处方列表的key
    //@Getter @Setter private String jzlsno;
    //@Getter @Setter private String hospId;
    //@Getter @Setter private String treatmentDate;

    public List<PrescriptionGroup> mx = new ArrayList<PrescriptionGroup>();

    public void AddPresGroup(PrescriptionGroup group)
    {
        mx.add(group);
    }

    public PrescriptionGroup getPrescriptionGroup(String presId)
    {
        for(int i = 0; i< mx.size(); i++){
            if(mx.get(i).getPresgroupid().equals(presId)){
                return mx.get(i);
            }
        }
        return null;
    }


}
