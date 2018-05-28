package com.ebao.hospitaldapp.rest.entity.mapper;

import com.ebao.hospitaldapp.ebao.entity.PrescriptionEntity;
import com.ebao.hospitaldapp.ebao.entity.UserInfoEntity;
import com.ebao.hospitaldapp.ipfs.entity.DmiResultEntity;
import com.ebao.hospitaldapp.ipfs.entity.PointsBills;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountRedisService;
import com.ebao.hospitaldapp.rest.entity.response.PrescriptionModel;
import com.ebao.hospitaldapp.rest.entity.response.TreatmentInfoModel;
import com.ebao.hospitaldapp.rest.entity.response.TreatmentModel;
import com.ebao.hospitaldapp.utils.CollectionUtils;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class TreatmentInfoMapper {

    @Autowired
    private HospitalAccountRedisService hospAccountRedisService;

    @AfterMapping
    protected TreatmentInfoModel set(@MappingTarget TreatmentInfoModel treatmentInfoModel){
        String hospId = treatmentInfoModel.getHospId();
        if (StringUtils.hasText(hospId)){
            HospitalAccountEntity hospitalAccountEntity = hospAccountRedisService.getHospitalAccount(hospId);
            if (hospitalAccountEntity != null){
                treatmentInfoModel.setHospName(hospitalAccountEntity.getHospName());
            }
        }

        return treatmentInfoModel;
    }

    @Mappings({
            @Mapping(source = "hosp_id",target = "hospId"),
            @Mapping(source = "id_no",target = "idNumber"),
            @Mapping(source = "jzlsno",target = "treatmentId")
    })
    public abstract TreatmentInfoModel map(UserInfoEntity userInfoEntity);

    public abstract ArrayList<TreatmentInfoModel> map(List<UserInfoEntity> userInfoEntityList);
}
