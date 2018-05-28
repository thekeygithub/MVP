package com.ebao.hospitaldapp.ipfs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.config.ApplicationConfig;
import com.ebao.hospitaldapp.data.RedisClient;
import com.ebao.hospitaldapp.ebao.entity.PrescriptionDetail;
import com.ebao.hospitaldapp.ebao.entity.PrescriptionEntity;
import com.ebao.hospitaldapp.ebao.entity.PrescriptionGroup;
import com.ebao.hospitaldapp.ebao.entity.UserInfoEntity;
import com.ebao.hospitaldapp.ipfs.entity.DmiResultEntity;
import com.ebao.hospitaldapp.ipfs.entity.E01_INP_LIST01;
import com.ebao.hospitaldapp.ipfs.entity.E01_INP_LIST02;
import com.ebao.hospitaldapp.ipfs.entity.PayInfo;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountRedisService;
import com.ebao.hospitaldapp.rest.service.PrescriptionsRedisService;
import com.ebao.hospitaldapp.rest.service.RedisKeyService;
import com.ebao.hospitaldapp.rest.service.UserInfoRedisService;
import com.ebao.hospitaldapp.utils.HttpUtils;
import com.ebao.hospitaldapp.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class IPFSoperations {

    final static Logger LOGGER = LoggerFactory.getLogger(IPFSoperations.class);
    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    private RedisKeyService redisKeyService;
    @Autowired
    private UserInfoRedisService userInfoRedisService;
    @Autowired
    private PrescriptionsRedisService prescriptionsRedisService;
    @Autowired
    private HospitalAccountRedisService hospitalAccountRedisService;


    //1. 存入个人信息，处方信息，照片
    //2. 取验证结果和预结算分账单信息
    //3. 存入结算账单信息
//    private String putValidInfoURL ="http://54.255.243.229:8090/IPFS/api/putUserInformation"; //TODO: 接口名改成putValidInfo
//    private String getResultInfoURL = "http://54.255.243.229:8090/IPFS/api/getUserInformation";
//    private String putPayInfoURL = "http://54.255.243.229:8090/IPFS/api/putPayInformaton";

    private String putValidInfoURL = "api/putUserInformation"; //TODO: 接口名改成putValidInfo
    private String getResultInfoURL = "api/getUserInformation";
    private String putPayInfoURL = "api/putPayInformaton";

    private String INFO_TYPE_VALIDINFO = "0";
    private String INFO_TYPE_USERINFO = "1";
    private String INFO_TYPE_PRESCRIPTION_INFO = "2";
    private String INFO_TYPE_IMAGE = "3";
    private String INFO_TYPE_PAYINFO = "4";
    private String INFO_TYPE_RESULTINFO = "5";

    //存入用户待验证信息，包括个人信息，处方信息，照片信息
    //返回哈希值，然后加密上链
    public String addValidationInfo(String userInfo, String presInfo, String image)
    {
        JSONObject validInfoObj = new JSONObject();
        JSONObject userObj = JSON.parseObject(userInfo);
        JSONArray presObj = JSON.parseArray(presInfo);
        validInfoObj.put("userInfo",userObj);
        validInfoObj.put("groupList",presObj);
        validInfoObj.put("image",image);
        JSONObject inObj = new JSONObject();
        inObj.put("validInfo",validInfoObj);

        //String in = inObj.toJSONString();
        LOGGER.debug("Add validation info into IPFS:{}",validInfoObj.toString());

        JSONObject jsObj;
        try {
            String response = HttpUtils.sendPOST(applicationConfig.getIpfsUrl()+putValidInfoURL, inObj);
            //解析response
            jsObj = JSONObject.parseObject(response);
        }
        catch(Exception e)
        {
            LOGGER.error("addValidationInfor: IPFS服务异常！",e);
            return "";
        }

        if(jsObj == null)
        {
            LOGGER.error("addValidationInfor: IPFS无响应！");
            return "";
        }
        if(!jsObj.containsKey("rsCode") || !jsObj.getString("rsCode").equals("0") || !jsObj.containsKey("newHash"))
        {
            LOGGER.error("addValidationInfor: IPFS响应错误.");
            return "";
        }
        //取出新创建文件的hash
        String hashId = jsObj.getString("newHash");

        return hashId;
    }

    //存入用户支付详情信息
    public String addPayInfo(String hospId, String jzlsno, String transactionNO, List<String>prescriptions)
    {
        String userInfoKey = redisKeyService.getUserInfoKey(hospId,jzlsno);
        String presInfoKey = redisKeyService.getPrescriptionKey(hospId,jzlsno);
        try {
            UserInfoEntity userInfoEntity = userInfoRedisService.getUserInfoEntity(userInfoKey);
            PrescriptionEntity presEntity = prescriptionsRedisService.getPresEntity(presInfoKey);
            HospitalAccountEntity hospEntity = hospitalAccountRedisService.getHospitalAccount(hospId);

            PrescriptionGroup presGroup0 = new PrescriptionGroup();
            PrescriptionDetail presDetail0 = new PrescriptionDetail();
            if(prescriptions.size()>0)
                presGroup0 = presEntity.getPrescriptionGroup(prescriptions.get(0));
            if(presGroup0!=null && presGroup0.getMx().size()>0)
                presDetail0 = presGroup0.getPrescriptionDetail(0);

            if(presGroup0 == null || presDetail0 == null){
                LOGGER.error("addPayInfo: 找不到处方信息！");
                return null;
            }
            //temporary
            Map<String, String> medTypeMap = new HashMap<>();
            medTypeMap.put("11","普通门诊");
            medTypeMap.put("14","药店购药");
            medTypeMap.put("16","门诊规定病种(慢性病)");
            medTypeMap.put("45","计划生育手术(门诊)");

            PayInfo payInfo = new PayInfo();
            payInfo.setApiType("Ebao01");
            payInfo.setE01_INP_NO01(transactionNO);
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            payInfo.setE01_INP_NO02(today);//结算日期
            payInfo.setE01_INP_NO03(userInfoEntity.getName());  //患者姓名
            payInfo.setE01_INP_NO04(userInfoEntity.getId_no());  //患者身份证号
            payInfo.setMi_id(presDetail0.getMi_id());             //患者社保卡号
            payInfo.setDeptname(presDetail0.getDeptname());  //就诊科室
            String med = presGroup0.getMed_type();
            if(medTypeMap.containsKey(med))
                payInfo.setMed_type(medTypeMap.get(med));  //就诊类型 字典 11代表普通门诊--名称
            payInfo.setPrediagname(presDetail0.getPrediagname());   //诊断
            payInfo.setTreatmentdate(presDetail0.getTreatmentdate());   //就诊日期
            payInfo.setPatientgend(presDetail0.getPatientgend());   //性别
            payInfo.setE01_INP_NO05(userInfoEntity.getGender());  //患者性别，字典表
            payInfo.setE01_INP_NO06(userInfoEntity.getBirthday());  //出生日期
            payInfo.setE01_INP_NO07(hospEntity.getHospName());  //由hospId找到医院名
            payInfo.setE01_INP_NO08(hospEntity.getHospClass());  //医疗机构级别,字典表，3代表三级
            payInfo.setE01_INP_NO09(hospEntity.getHospGrade());      //医疗机构等级,字典表，3代表甲等
            payInfo.setE01_INP_NO10(hospEntity.getHospType());  //医疗机构类型,字典表，A100代表综合医院

            for(int i=0;i<prescriptions.size();i++){
                String presGroupId = prescriptions.get(i);
                PrescriptionGroup group = presEntity.getPrescriptionGroup(presGroupId);
                for(int j = 0; j<group.getMx().size(); j++){
                    PrescriptionDetail detail = group.getPrescriptionDetail(j);
                    String siitemType = detail.getSiitemtype();
                    int siType = Integer.parseInt(siitemType);
                    if(siType == 1) {    //药品
                        E01_INP_LIST01 drugItem = new E01_INP_LIST01();
                        drugItem.setItemname(detail.getItemname());
                        drugItem.setE01_INP_LIST01_NO01(detail.getSiitemid());
                        drugItem.setE01_INP_LIST01_NO02(detail.getNum());
                        payInfo.getE01_INP_LIST01().add(drugItem);
                    }else if(siType == 2){   //诊疗
                        E01_INP_LIST02 treatItem = new E01_INP_LIST02();
                        treatItem.setItemname(detail.getItemname());
                        treatItem.setE01_INP_LIST02_NO01(detail.getSiitemid());
                        treatItem.setE01_INP_LIST02_NO02(detail.getNum());
                        payInfo.getE01_INP_LIST02().add(treatItem);
                    }
                }
            }

            String ipfsKey = redisKeyService.getUserIPFSfileKey(hospId,jzlsno);
            String ipfsHash = RedisClient.get(ipfsKey);
            String newHash = addPayInfo(ipfsHash,payInfo);
            return newHash;
        }catch(Exception e){
            return null;
        }
    }

    private String addPayInfo(String hash,PayInfo payInfo)
    {
        try {
            String payStr = JSONObject.toJSONString(payInfo);
            JSONObject payObj = JSON.parseObject(payStr);
            JSONObject obj = new JSONObject();
            obj.put("payInfo",payObj);
            return addPayInfo(hash,obj);
        }catch(Exception e){
            LOGGER.error("addPayInfo error");
        }

        return null;
    }

    //存入结算账单信息
    //传入json串，和文件hash
    //返回文件的新hash
    //private String addPayInfo(String hash,String payInfo)
    private String addPayInfo(String hash,JSONObject payInfo)
    {
        JSONObject payInfoObj = new JSONObject();
        payInfoObj.put("hashId",hash);
        payInfoObj.put("payInfo",payInfo);
        String in = payInfoObj.toJSONString();
        LOGGER.debug("Add PAY info into IPFS:{}",in);

        JSONObject jsObj;
        try {
            String response = HttpUtils.sendPOST(applicationConfig.getIpfsUrl()+putPayInfoURL, payInfoObj);
            //解析response
            jsObj = JSONObject.parseObject(response);
        }
        catch(Exception e)
        {
            LOGGER.error("addPayInfo: IPFS服务异常！",e);
            return "";
        }

        if (jsObj == null) {
             LOGGER.error("addPayInfo: IPFS无响应！");
             return "";
        }
        if (!jsObj.containsKey("rsCode") || !jsObj.getString("rsCode").equals("0") || !jsObj.containsKey("newHash")) {
            LOGGER.error("addPayInfo: IPFS响应错误.");
            return "";
        }
        //取出新创建文件的hash
        String hashId = jsObj.getString("newHash");
        return hashId;
    }

    //获取DMI验证结果
    public DmiResultEntity getDmiResultInfo(String hashId)
    {
        String reStr = getUserInformation(hashId, INFO_TYPE_RESULTINFO);
        if(reStr.equals("")) return null;
        LOGGER.debug("从IPFS取回的验证结果:{}", reStr);
        DmiResultEntity dmiEntity;
        try {
            dmiEntity = JsonUtils.fromJson(reStr, DmiResultEntity.class);
        }
        catch(Exception e)
        {
            LOGGER.error("解析多维验证结果错误！");
            return null;
        }
        return dmiEntity;
    }

    //需要再取一下userInfo
    public UserInfoEntity getUserInfo(String hashId)
    {
        String reStr = getUserInformation(hashId, INFO_TYPE_USERINFO);
        if(reStr.equals("")) return null;

        UserInfoEntity userEntity;
        try {
            JSONObject allObj = JSON.parseObject(reStr);
            String userStr = allObj.getString("userInfo");
            userEntity = JsonUtils.fromJson(userStr, UserInfoEntity.class);
        }
        catch(Exception e)
        {
            LOGGER.error("解析用户信息错误！");
            return null;
        }
        return userEntity;
    }

    //获取用户认证信息
    private String getUserInformation(String hashId, String infoType)
    {
        LOGGER.debug("getDmiResultInfo from IPFS.");

        JSONObject reqObj = new JSONObject();
        reqObj.put("hashId",hashId);
        reqObj.put("infoType",infoType);

        JSONObject jsObj;
        try {
            String response = HttpUtils.sendPOST(applicationConfig.getIpfsUrl()+getResultInfoURL, reqObj);
            //解析response
            jsObj = JSONObject.parseObject(response);
        }
        catch(Exception e)
        {
            LOGGER.error("getUserInformation: IPFS服务异常！",e);
            return "";
        }

        if(jsObj == null)
        {
            LOGGER.error("getUserInformation: IPFS无响应！");
            return "";
        }
        if(!jsObj.containsKey("rsCode") || !jsObj.getString("rsCode").equals("0") || !jsObj.containsKey("rsJsonString"))
        {
            LOGGER.error("getUserInformation: IPFS响应错误.");
            return "";
        }

        return jsObj.getString("rsJsonString");
    }

}
