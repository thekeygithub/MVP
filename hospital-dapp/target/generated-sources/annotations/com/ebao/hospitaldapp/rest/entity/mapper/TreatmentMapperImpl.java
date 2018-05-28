package com.ebao.hospitaldapp.rest.entity.mapper;

import com.ebao.hospitaldapp.ebao.entity.PrescriptionEntity;
import com.ebao.hospitaldapp.ipfs.entity.DmiResultEntity;
import com.ebao.hospitaldapp.rest.entity.response.TreatmentModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2018-05-07T16:20:01+0800",
    comments = "version: 1.2.0.CR1, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class TreatmentMapperImpl extends TreatmentMapper {

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Override
    public TreatmentModel map(PrescriptionEntity prescriptionEntity, DmiResultEntity dmiResultEntity) {
        if ( prescriptionEntity == null && dmiResultEntity == null ) {
            return null;
        }

        TreatmentModel treatmentModel = new TreatmentModel();

        if ( prescriptionEntity != null ) {
            treatmentModel.setPrescriptions( prescriptionMapper.map( prescriptionEntity.mx ) );
        }

        TreatmentModel target = set( treatmentModel, dmiResultEntity );
        if ( target != null ) {
            return target;
        }

        return treatmentModel;
    }

    @Override
    public ArrayList<TreatmentModel> map(List<PrescriptionEntity> prescriptionEntityList, List<DmiResultEntity> dmiResultEntityList) {
        if ( prescriptionEntityList == null && dmiResultEntityList == null ) {
            return null;
        }

        ArrayList<TreatmentModel> arrayList = new ArrayList<TreatmentModel>();

        return arrayList;
    }
}
