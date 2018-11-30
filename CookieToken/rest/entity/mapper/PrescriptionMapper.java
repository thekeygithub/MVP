package com.ebao.hospitaldapp.rest.entity.mapper;

import com.ebao.hospitaldapp.ebao.entity.PrescriptionDetail;
import com.ebao.hospitaldapp.ebao.entity.PrescriptionGroup;
import com.ebao.hospitaldapp.rest.entity.response.PrescriptionModel;
import com.ebao.hospitaldapp.utils.CollectionUtils;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", uses = PrescriptionDetailMapper.class)
@Component
public abstract class PrescriptionMapper {

    @AfterMapping
    protected PrescriptionModel set(@MappingTarget PrescriptionModel prescriptionModel, PrescriptionGroup prescriptionGroup){
        List<PrescriptionDetail> prescriptionDetails = prescriptionGroup.getMx();
        if (!CollectionUtils.isEmpty(prescriptionDetails)){
            PrescriptionDetail detailSample = prescriptionDetails.get(0);
            prescriptionModel.setPrescriptionName(detailSample.getPresgroupname());
            prescriptionModel.setDoctorName(detailSample.getDoctorname());
            prescriptionModel.setDeptName(detailSample.getDeptname());
            prescriptionModel.setDiagnosis(detailSample.getPrediagname());
        }
        return prescriptionModel;
    }

    @Mappings({
            @Mapping(source = "presgroupid.",target = "prescriptionId"),
            @Mapping(source = "treatment_date",target = "treatmentDate"),
            @Mapping(source = "mx",target = "prescriptionDetails")
    })
    public abstract PrescriptionModel map(PrescriptionGroup prescriptionGroup);

    public abstract List<PrescriptionModel> map(List<PrescriptionGroup> prescriptionGroupList);
}
