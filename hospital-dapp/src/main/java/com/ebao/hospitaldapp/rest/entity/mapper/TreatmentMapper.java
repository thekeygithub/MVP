package com.ebao.hospitaldapp.rest.entity.mapper;

import com.ebao.hospitaldapp.ebao.entity.PrescriptionEntity;
import com.ebao.hospitaldapp.ipfs.entity.DmiResultEntity;
import com.ebao.hospitaldapp.ipfs.entity.PointsBills;
import com.ebao.hospitaldapp.rest.entity.response.PrescriptionModel;
import com.ebao.hospitaldapp.rest.entity.response.TreatmentModel;
import com.ebao.hospitaldapp.utils.CollectionUtils;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper(componentModel = "spring", uses = PrescriptionMapper.class)
@Component
public abstract class TreatmentMapper {

    @AfterMapping
    protected TreatmentModel set(@MappingTarget TreatmentModel treatmentModel, DmiResultEntity dmiResultEntity){
        List<PrescriptionModel> prescriptionModels = treatmentModel.getPrescriptions();
        List<PointsBills> pointsBills = dmiResultEntity.getPointsBills();
        if (!CollectionUtils.isEmpty(prescriptionModels) && !CollectionUtils.isEmpty(pointsBills)){
//            pointsBills.stream()
//                    .filter(pointsBills1 -> CommonUtils.isFloatNumber(pointsBills1.getCashpay()))
//                    .collect(Collectors.groupingBy(PointsBills::getPresgroupid, Collectors.summarizingDouble(p -> new BigDecimal(PointsBills::getCashpay))));
            HashMap<String, BigDecimal> map1 = new HashMap();
            HashMap<String, BigDecimal> map2 = new HashMap();
            for (PointsBills pointsBill : pointsBills){
                String prescriptionId = pointsBill.getPresgroupid();
                BigDecimal bigDecimal1;
                BigDecimal bigDecimal2;
                try {
                    bigDecimal1 = BigDecimal.valueOf(Double.valueOf(pointsBill.getCashpay()));
                }catch (Exception e){
                    bigDecimal1 = new BigDecimal(0);
                }
                try {
                    bigDecimal2 = BigDecimal.valueOf(Double.valueOf(pointsBill.getYlsum()));
                }catch (Exception e){
                    bigDecimal2 = new BigDecimal(0);
                }
                BigDecimal result1 = map1.get(prescriptionId);
                map1.put(prescriptionId, result1 == null ? bigDecimal1 : bigDecimal1.add(result1));
                BigDecimal result2 = map2.get(prescriptionId);
                map2.put(prescriptionId, result2 == null ? bigDecimal2 : bigDecimal2.add(result2));
            }

            prescriptionModels.forEach(prescriptionModel -> {
                prescriptionModel.setReimbursement(map1.get(prescriptionModel.getPrescriptionId()));
                prescriptionModel.setPayActully(map2.get(prescriptionModel.getPrescriptionId()));
            });
        }
        return treatmentModel;
    }

    @Mappings({
            @Mapping(source = "prescriptionEntity.mx",target = "prescriptions")
    })
    public abstract TreatmentModel map(PrescriptionEntity prescriptionEntity, DmiResultEntity dmiResultEntity);

    public abstract ArrayList<TreatmentModel> map(List<PrescriptionEntity> prescriptionEntityList, List<DmiResultEntity> dmiResultEntityList);
}
