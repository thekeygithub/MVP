package com.ebao.hospitaldapp.rest.entity.mapper;

import com.ebao.hospitaldapp.ebao.entity.UserInfoEntity;
import com.ebao.hospitaldapp.rest.entity.response.PatientModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2018-05-07T16:20:01+0800",
    comments = "version: 1.2.0.CR1, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class UserInfoMapperImpl implements UserInfoMapper {

    @Override
    public PatientModel map(UserInfoEntity userInfoEntity) {
        if ( userInfoEntity == null ) {
            return null;
        }

        PatientModel patientModel = new PatientModel();

        patientModel.setIdNumber( userInfoEntity.getId_no() );
        patientModel.setPatientId( userInfoEntity.getHosp_id() );
        patientModel.setTreatmentId( userInfoEntity.getJzlsno() );
        patientModel.setName( userInfoEntity.getName() );
        patientModel.setGender( userInfoEntity.getGender() );
        patientModel.setAge( userInfoEntity.getAge() );

        return patientModel;
    }

    @Override
    public List<PatientModel> map(List<UserInfoEntity> userInfoEntityList) {
        if ( userInfoEntityList == null ) {
            return null;
        }

        List<PatientModel> list = new ArrayList<PatientModel>( userInfoEntityList.size() );
        for ( UserInfoEntity userInfoEntity : userInfoEntityList ) {
            list.add( map( userInfoEntity ) );
        }

        return list;
    }
}
