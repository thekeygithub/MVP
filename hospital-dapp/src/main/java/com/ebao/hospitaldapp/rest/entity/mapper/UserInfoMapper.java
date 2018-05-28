package com.ebao.hospitaldapp.rest.entity.mapper;

import com.ebao.hospitaldapp.ebao.entity.UserInfoEntity;
import com.ebao.hospitaldapp.rest.entity.response.PatientModel;
import io.lettuce.core.protocol.CommandHandler;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserInfoMapper {

    @Mappings({
            @Mapping(source = "hosp_id",target = "patientId"),
            @Mapping(source = "id_no",target = "idNumber"),
            @Mapping(source = "jzlsno",target = "treatmentId"),
    })
    public PatientModel map(UserInfoEntity userInfoEntity);

    public List<PatientModel> map(List<UserInfoEntity> userInfoEntityList);
}
