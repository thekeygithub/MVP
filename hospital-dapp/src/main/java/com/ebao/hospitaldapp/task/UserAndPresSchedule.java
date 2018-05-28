package com.ebao.hospitaldapp.task;

import com.ebao.hospitaldapp.config.ApplicationConfig;
import com.ebao.hospitaldapp.ebao.GetPrescriptionInfo;
import com.ebao.hospitaldapp.ebao.GetUnpaidUserList;
import com.ebao.hospitaldapp.ebao.entity.UserInfoEntity;
import com.ebao.hospitaldapp.rest.service.HospitalRedisService;
import com.ebao.hospitaldapp.rest.service.UserInfoRedisService;
import com.ebao.hospitaldapp.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class UserAndPresSchedule {

    @Autowired
    StringRedisTemplate redis;
    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired
    UserInfoRedisService userInfoRedisService;
    @Autowired
    GetUnpaidUserList getUnpaidUserList;
    @Autowired
    GetPrescriptionInfo getPrescriptionInfo;
    @Autowired
    HospitalRedisService hospitalRedisService;

    final static Logger LOGGER = LoggerFactory.getLogger(UserAndPresSchedule.class);

    @Scheduled(cron = "*/120 * * * * ?")
    public void retriveUserInfo(){
        List<String> hospitalIds = hospitalRedisService.getHospitalIds();
        if (!CollectionUtils.isEmpty(hospitalIds)){
            for (String hospId : hospitalIds){
                getUnpaidUserList.retrieveFromEbaoAPI(hospId, "2018-04-03");
            }
        }
    }

    @Scheduled(cron = "*/120 * * * * ?")
    public void retriveDescriptionInfo(){
        List<String> hospitalIds = hospitalRedisService.getHospitalIds();
        if (!CollectionUtils.isEmpty(hospitalIds)) {
            for (String hospId : hospitalIds) {
                List<UserInfoEntity> entityList = userInfoRedisService.getUserInfoEntityList(hospId);
                if (!CollectionUtils.isEmpty(entityList)){
                    for (UserInfoEntity userInfoEntity : entityList){
                        getPrescriptionInfo.retrieveFromEbaoAPI(userInfoEntity.getHosp_id(), userInfoEntity.getJzlsno());
                    }
                }
            }
        }
    }

}
