package com.ebao.hospitaldapp.rest.entity.mapper;

import com.ebao.hospitaldapp.ebao.entity.PrescriptionDetail;
import com.ebao.hospitaldapp.rest.entity.response.PrescriptionDetailModel;
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
public class PrescriptionDetailMapperImpl implements PrescriptionDetailMapper {

    @Override
    public PrescriptionDetailModel map(PrescriptionDetail prescriptionDetail) {
        if ( prescriptionDetail == null ) {
            return null;
        }

        PrescriptionDetailModel prescriptionDetailModel = new PrescriptionDetailModel();

        prescriptionDetailModel.setSpecification( prescriptionDetail.getSpec() );
        prescriptionDetailModel.setTreatmentType( prescriptionDetail.getTreatmentcosttype() );
        prescriptionDetailModel.setItemName( prescriptionDetail.getItemname() );
        prescriptionDetailModel.setPrice( prescriptionDetail.getPrice() );
        prescriptionDetailModel.setNum( prescriptionDetail.getNum() );
        prescriptionDetailModel.setAmount( prescriptionDetail.getAmount() );
        prescriptionDetailModel.setDosageforms( prescriptionDetail.getDosageforms() );
        prescriptionDetailModel.setDose( prescriptionDetail.getDose() );
        prescriptionDetailModel.setMeasunit( prescriptionDetail.getMeasunit() );
        prescriptionDetailModel.setUsage( prescriptionDetail.getUsage() );
        prescriptionDetailModel.setUsefreq( prescriptionDetail.getUsefreq() );
        prescriptionDetailModel.setExedays( prescriptionDetail.getExedays() );

        return prescriptionDetailModel;
    }

    @Override
    public List<PrescriptionDetailModel> map(List<PrescriptionDetail> prescriptionDetailList) {
        if ( prescriptionDetailList == null ) {
            return null;
        }

        List<PrescriptionDetailModel> list = new ArrayList<PrescriptionDetailModel>( prescriptionDetailList.size() );
        for ( PrescriptionDetail prescriptionDetail : prescriptionDetailList ) {
            list.add( map( prescriptionDetail ) );
        }

        return list;
    }
}
