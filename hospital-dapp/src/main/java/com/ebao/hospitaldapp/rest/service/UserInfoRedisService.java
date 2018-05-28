package com.ebao.hospitaldapp.rest.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.data.RedisClient;
import com.ebao.hospitaldapp.ebao.entity.UserInfoEntity;
import com.ebao.hospitaldapp.utils.JsonUtils;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoRedisService {

    @Autowired
    RedisKeyService redisKeyService;
    final static Logger LOGGER = LoggerFactory.getLogger(UserInfoRedisService.class);

    //以hospId+jzlsno为key，为每条用户数据添加k-v
    //以UserInfoEntityList为key，建立list的k-v。定时任务每2分钟刷新一次，更新这条list。
    public int putEntityListToRedis(String id, JSONArray jsArray)
    {
        int result = 0;
        try {

            String jsString = jsArray.toJSONString();
            String listKey = redisKeyService.getUserInfoListKey(id);
            RedisClient.set(listKey,jsString);

            for(int i=0; i<jsArray.size(); i++)
            {
                JSONObject obj = jsArray.getJSONObject(i);
                String hospId = obj.getString("hosp_id");
                String jzlsno = obj.getString("jzlsno");
                if (StringUtils.hasText(hospId) && StringUtils.hasText(jzlsno)){
                    String key = redisKeyService.getUserInfoKey(hospId, jzlsno);
                    //只有键值不存在的时候，才做set操作
                    RedisClient.set(key,obj.toJSONString(),"NX");
                }
            }
        }
        catch(Exception e)
        {
            LOGGER.error("UserInforRedisUtils:PutEntityListToRedis，未知失败！",e);
            result = 1;
        }
        return result;
    }

    public List<UserInfoEntity> getUserInfoEntityList(String hospId)
    {
        List<UserInfoEntity> entityList = new ArrayList<UserInfoEntity>();
        String listKey = redisKeyService.getUserInfoListKey(hospId);
        String jsString = RedisClient.get(listKey);
        if(jsString == null || "".equals(jsString))
        {
            LOGGER.error("UserInforRedisUtils:GetEntityList，未找到！");
            return null;
        }
        try {
            entityList = JsonUtils.fromJsonToList(jsString, UserInfoEntity.class);
        }
        catch(Exception e)
        {
            LOGGER.error("UserInforRedisUtils:GetEntityList，未知失败！",e);
            return null;
        }
        return entityList;
    }

    public  UserInfoEntity getUserInfoEntity(String hospId, String jzlsno)
    {
        UserInfoEntity entity;
        String key = redisKeyService.getUserInfoKey(hospId, jzlsno);
        return getUserInfoEntity(key);
    }

    public  UserInfoEntity getUserInfoEntity(String key)
    {
        UserInfoEntity entity;
        String jsString = getUserInfoString(key);
        try {
            entity = JsonUtils.fromJson(jsString, UserInfoEntity.class);
        }
        catch(Exception e)
        {
            LOGGER.error("UserInforRedisUtils:GetUserInfoEntity，解析失败！",e);
            return null;
        }
        return entity;
    }

    public String getUserInfoString(String key)
    {
        String jsString = RedisClient.get(key);

        if(jsString == null || "".equals(jsString))
        {
            LOGGER.error("UserInforRedisUtils:GetUserInfoEntity，未找到！");
            return null;
        }
        return jsString;
    }

    public String getUserInfoString(String hospId, String jzlsno)
    {
        String key = redisKeyService.getUserInfoKey(hospId, jzlsno);
        return getUserInfoString(key);
    }
}
