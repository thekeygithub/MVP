package com.ebao.hospitaldapp.rest.entity.mapper;

import com.ebao.hospitaldapp.ebao.entity.PrescriptionDetail;
import com.ebao.hospitaldapp.ebao.entity.PrescriptionEntity;
import com.ebao.hospitaldapp.ipfs.entity.DmiResultEntity;
import com.ebao.hospitaldapp.rest.entity.response.PrescriptionDetailModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface PrescriptionDetailMapper {
    @Mappings({
            @Mapping(source = "treatmentcosttype.",target = "treatmentType"),
            @Mapping(source = "spec",target = "specification"),
            @Mapping(source = "itemname",target = "itemName")
    })
    public PrescriptionDetailModel map(PrescriptionDetail prescriptionDetail);

    public List<PrescriptionDetailModel> map(List<PrescriptionDetail> prescriptionDetailList);
}
