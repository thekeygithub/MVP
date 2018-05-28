package com.ebao.hospitaldapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.config.ApplicationConfig;
import com.ebao.hospitaldapp.neo.NEOoperations;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountRedisService;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountRedisService;
import com.ebao.hospitaldapp.rest.service.HospitalRedisService;
import com.ebao.hospitaldapp.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

@Component
public class InitializationRunner implements ApplicationRunner{

    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired
    private UserAccountRedisService userAccountRedisService;
    @Autowired
    private HospitalAccountRedisService hospAccountRedisService;
    @Autowired
    private HospitalRedisService hospitalRedisService;

    final Logger logger = LoggerFactory.getLogger(InitializationRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String filepath = applicationConfig.getHospitalConfigFile();
        URL fileURL = ResourceUtils.getURL(filepath);
        if (ResourceUtils.isFileURL(fileURL)) {
            hospitalRedisService.putHospitalToRedis(fileURL);
        }else {
            logger.error("初始化医院信息失败，配置文件路径:" + fileURL);
        }

        //读取预设的用户账号文件
        String userAccountFilePath = applicationConfig.getUserAccountConfigFile();
        try{
            String jsStr = readStrFromFile(userAccountFilePath);
            if(!jsStr.equals("")){
                JSONObject jsObj = JSONObject.parseObject(jsStr);
                JSONArray accList = jsObj.getJSONArray("UserAccountList");
                List<UserAccountEntity> accountList = JsonUtils.fromJsonToList(accList.toJSONString(), UserAccountEntity.class);
                for (UserAccountEntity entity:accountList ) {
                    userAccountRedisService.putUserAccount(entity);
                }
            }
        }catch(Exception e){
            logger.error("初始化用户账号信息失败!",e);
        }

        //读取预设的医院账号文件
        String hospAccountFilePath = applicationConfig.getHospitalAccountConfigFile();
        try{
            String jsStr = readStrFromFile(hospAccountFilePath);
            if(!jsStr.equals("")){
                JSONObject jsObj = JSONObject.parseObject(jsStr);
                JSONArray accList = jsObj.getJSONArray("HospitalAccountList");
                List<HospitalAccountEntity> accountList = JsonUtils.fromJsonToList(accList.toJSONString(), HospitalAccountEntity.class);
                for (HospitalAccountEntity entity:accountList ) {
                    hospAccountRedisService.putHospitalAccount(entity);
                }
            }
        }catch(Exception e){
                logger.error("初始化用户账号信息失败!",e);
        }


    }


    private String readStrFromFile(String path) {
        try {
            URL url = ResourceUtils.getURL(path);
            if (ResourceUtils.isFileURL(url)) {
                File file = ResourceUtils.getFile(url);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String jsStr = "";
                String lineStr = null;
                while ((lineStr = reader.readLine()) != null) {
                    jsStr += lineStr;
                }
                reader.close();
                return jsStr;
            }
        }catch(Exception e){
            logger.error("读取配置文件异常,路径:"+path,e);
        }
        return "";
    }

}
