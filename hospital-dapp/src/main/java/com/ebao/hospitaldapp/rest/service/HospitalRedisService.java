package com.ebao.hospitaldapp.rest.service;

import com.ebao.hospitaldapp.config.ApplicationConfig;
import com.ebao.hospitaldapp.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalRedisService {

    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    ApplicationConfig applicationConfig;

    public void putHospitalToRedis(URL fileURL) throws Exception{
        String hospitalRedisKey = applicationConfig.getHospitalRedisKey();
        redis.boundListOps(hospitalRedisKey).trim(1, 0); //先清空redis里面的值，首比尾大即是清空
        File hospitalFile = ResourceUtils.getFile(fileURL);
        BufferedReader reader = new BufferedReader(new FileReader(hospitalFile));
        String txtline;
        while ((txtline = reader.readLine()) != null){
            redis.boundListOps(hospitalRedisKey).rightPush(txtline);
        }
    }

    public List<String> getHospitals(){
        String hospitalRedisKey = applicationConfig.getHospitalRedisKey();
        List<String> hospitalInfoList = redis.boundListOps(hospitalRedisKey).range(0, -1);
        return hospitalInfoList;
    }

    public List<String> getHospitalIds(){
        List<String> hospitalInfoList = getHospitals();
        if (CollectionUtils.isEmpty(hospitalInfoList)) return null;

        List<String> hospIds = new ArrayList<>();
        hospitalInfoList.forEach(hospital -> hospIds.add(getHospId(hospital)));

        return hospIds;
    }

    public String getHospId(String hospitalInfo){
        String[] hospitalInfoArray = hospitalInfo.split("\\|");
        if (hospitalInfoArray.length != 8) return null;
        else return hospitalInfoArray[0];
    }
}
