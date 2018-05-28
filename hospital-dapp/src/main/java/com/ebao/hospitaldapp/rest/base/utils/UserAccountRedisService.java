package com.ebao.hospitaldapp.rest.base.utils;

import com.alibaba.fastjson.JSON;
import com.ebao.hospitaldapp.data.RedisClient;
import com.ebao.hospitaldapp.rest.service.RedisKeyService;
import com.ebao.hospitaldapp.utils.JsonUtils;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserAccountRedisService {

    @Autowired
    private RedisKeyService redisKeyService;

    final Logger LOGGER = LoggerFactory.getLogger(UserAccountRedisService.class);

    //将redis中存的用户账号json串读入UserAccountEntity
    public UserAccountEntity getUserAccount(String ID)
    {
        String key = redisKeyService.getUserAccountKeyPattern(ID);
        String accountJs = RedisClient.get(key);
        UserAccountEntity userEntiry;
        try {
            userEntiry = JsonUtils.fromJson(accountJs, UserAccountEntity.class);
        }
        catch(Exception e)
        {
            LOGGER.error("getUserAccount:解析账户数据错误！");
            return null;
        }
        return userEntiry;
    }

    public UserAccountEntity getUserAccountByMobile(String mobile)
    {
        if (!StringUtils.hasText(mobile)) return null;

        String keyPattern = redisKeyService.getUserAccountKeyPattern();
        Set<String> keys = RedisClient.keys(keyPattern);
        for (String key: keys){
            String accountJs = RedisClient.get(key);
            UserAccountEntity userAccountEntity = JSON.parseObject(accountJs, UserAccountEntity.class);
            if (userAccountEntity != null && mobile.equals(userAccountEntity.getMobile())){
                return userAccountEntity;
            }
        }

        return null;
    }

    public void putUserAccount(UserAccountEntity userAccountEntity)
    {
        String ID = userAccountEntity.getID();
        String key = redisKeyService.getUserAccountKeyPattern(ID);
        try {
            RedisClient.set(key, JsonUtils.toJson(userAccountEntity));
        }
        catch(Exception e){
            LOGGER.error("putUserAccount:解析账户数据错误！");
        }
    }


}
