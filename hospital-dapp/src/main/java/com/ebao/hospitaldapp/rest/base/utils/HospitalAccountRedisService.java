package com.ebao.hospitaldapp.rest.base.utils;

import com.ebao.hospitaldapp.data.RedisClient;
import com.ebao.hospitaldapp.rest.service.RedisKeyService;
import com.ebao.hospitaldapp.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalAccountRedisService {

    @Autowired
    private RedisKeyService redisKeyService;

    final Logger LOGGER = LoggerFactory.getLogger(HospitalAccountRedisService.class);

    //将redis中存的医院信息json串，读入HospitalAccountEntity
    public HospitalAccountEntity getHospitalAccount(String hospId)
    {
        String key = redisKeyService.getHospAccountKey(hospId);
        String accountJs = RedisClient.get(key);
        HospitalAccountEntity userEntiry;
        try {
            userEntiry = JsonUtils.fromJson(accountJs, HospitalAccountEntity.class);
        }
        catch(Exception e)
        {
            LOGGER.error("解析账户数据错误！");
            return null;
        }
        return userEntiry;
    }

    public void putHospitalAccount(HospitalAccountEntity entity)
    {
        String hospId = entity.getHospId();
        String key = redisKeyService.getHospAccountKey(hospId);
        try {
            RedisClient.set(key, JsonUtils.toJson(entity));
        }
        catch(Exception e){
            LOGGER.error("putHospitalAccount:解析账户数据错误！");
        }
    }

}
