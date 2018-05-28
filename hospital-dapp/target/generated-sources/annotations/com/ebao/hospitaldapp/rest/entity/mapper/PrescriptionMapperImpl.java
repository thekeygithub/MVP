package com.ebao.hospitaldapp.rest.entity.mapper;

import com.ebao.hospitaldapp.ebao.entity.PrescriptionGroup;
import com.ebao.hospitaldapp.rest.entity.response.PrescriptionModel;
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
public class PrescriptionMapperImpl extends PrescriptionMapper {

    @Autowired
    private PrescriptionDetailMapper prescriptionDetailMapper;

    @Override
    public PrescriptionModel map(PrescriptionGroup prescriptionGroup) {
        if ( prescriptionGroup == null ) {
            return null;
        }

        PrescriptionModel prescriptionModel = new PrescriptionModel();

        prescriptionModel.setPrescriptionId( prescriptionGroup.getPresgroupid() );
        prescriptionModel.setTreatmentDate( prescriptionGroup.getTreatment_date() );
        prescriptionModel.setPrescriptionDetails( prescriptionDetailMapper.map( prescriptionGroup.getMx() ) );

        PrescriptionModel target = set( prescriptionModel, prescriptionGroup );
        if ( target != null ) {
            return target;
        }

        return prescriptionModel;
    }

    @Override
    public List<PrescriptionModel> map(List<PrescriptionGroup> prescriptionGroupList) {
        if ( prescriptionGroupList == null ) {
            return null;
        }

        List<PrescriptionModel> list = new ArrayList<PrescriptionModel>( prescriptionGroupList.size() );
        for ( PrescriptionGroup prescriptionGroup : prescriptionGroupList ) {
            list.add( map( prescriptionGroup ) );
        }

        return list;
    }
}
