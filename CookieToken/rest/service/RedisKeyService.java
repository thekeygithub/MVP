package com.ebao.hospitaldapp.rest.service;

import com.ebao.hospitaldapp.ebao.entity.PrescriptionEntity;
import com.ebao.hospitaldapp.ebao.entity.UserInfoEntity;
import com.ebao.hospitaldapp.ipfs.IPFSoperations;
import com.ebao.hospitaldapp.ipfs.entity.DmiResultEntity;
import com.ebao.hospitaldapp.utils.CommonUtils;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountEntity;
import com.ebao.hospitaldapp.rest.entity.request.VerifyInfo;
import org.springframework.stereotype.Service;

@Service
public class RedisKeyService {

    private String userKeySuffix = UserInfoEntity.class.getSimpleName();
    private String prescriptionKeySuffix = PrescriptionEntity.class.getSimpleName();
    private String dmiResultKeySuffix = DmiResultEntity.class.getSimpleName();
    private String userAccountKeySuffix = UserAccountEntity.class.getSimpleName();
    private String hospAccountKeySuffix = HospitalAccountEntity.class.getSimpleName();
    private String userIPFSfileKeySuffix = IPFSoperations.class.getSimpleName();
    private String serialNoKeySuffix = "SerialNo";
    private String loginVcodeSuffix = "LOGIN";

    private String delimiter = "-";
    private String keyPattern = "*";

    public String getUserInfoKey(String hospId, String treatmentId){
        return CommonUtils.KeyGenerator(delimiter, hospId, treatmentId, userKeySuffix);
    }

    public String getUserInfoListKey(String hospId){
        return CommonUtils.KeyGenerator(delimiter, hospId, userKeySuffix);
    }

    public String getSerialNoKey(String hospId, String treatmentId){
        return CommonUtils.KeyGenerator(delimiter, hospId, treatmentId, serialNoKeySuffix);
    }

    public String getPrescriptionKey(String hospId, String treatmentId){
        return  CommonUtils.KeyGenerator(delimiter, hospId, treatmentId, prescriptionKeySuffix);
    }

    public String getDMIResultKey(String hospId, String treatmentId){
        return  CommonUtils.KeyGenerator(delimiter, hospId, treatmentId, dmiResultKeySuffix);
    }

    public String getUserInfoKey(VerifyInfo verifyInfo){
        return getUserInfoKey(verifyInfo.getHospId(), verifyInfo.getTreatmentId());
    }

    public String getPrescriptionKey(VerifyInfo verifyInfo){
        return getPrescriptionKey(verifyInfo.getHospId(), verifyInfo.getTreatmentId());
    }

    public String getDMIResultKey(VerifyInfo verifyInfo){
        return  getDMIResultKey(verifyInfo.getHospId(), verifyInfo.getTreatmentId());
    }

    public String getUserAccountKeyPattern(){
        return  keyPattern + userAccountKeySuffix;
    }

    public String getUserAccountKeyPattern(String ID){
        return  CommonUtils.KeyGenerator(delimiter, ID, userAccountKeySuffix);
    }

    public String getHospAccountKey(String hospId){
        return  CommonUtils.KeyGenerator(delimiter, hospId, hospAccountKeySuffix);
    }

    public String getUserImageKey(String hospId,String IdNumber){
        return CommonUtils.KeyGenerator(delimiter, hospId,IdNumber, "image");
    }

    public String getUserIPFSfileKey(String hospId,String jzlsno){
        return CommonUtils.KeyGenerator(delimiter, hospId,jzlsno,userIPFSfileKeySuffix);
    }

    public String getLoginVcodeKey(String number){
        return CommonUtils.KeyGenerator(delimiter, number,loginVcodeSuffix);
    }
}
