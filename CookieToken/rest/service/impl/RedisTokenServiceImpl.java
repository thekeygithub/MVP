package com.ebao.hospitaldapp.rest.service.impl;

import com.ebao.hospitaldapp.rest.entity.TokenEntity;
import com.ebao.hospitaldapp.rest.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 * @see com.ebao.hospitaldapp.rest.service.TokenService
 */
@Component
public class RedisTokenServiceImpl implements TokenService {

    @Autowired
    private RedisTemplate<String, String> redis;
    private static long TOKEN_EXPIRES = 365;
    private static TimeUnit timeUnit = TimeUnit.DAYS;

    final Logger logger = LoggerFactory.getLogger(RedisTokenServiceImpl.class);

    public TokenEntity createToken(String deviceId) {
        //使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenEntity model = new TokenEntity(deviceId, token);
        //存储到redis并设置过期时间
        redis.boundValueOps(deviceId).set(token, TOKEN_EXPIRES, timeUnit);
        return model;
    }

    public TokenEntity getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            logger.info("[auth check fail][token empty]");
            return null;
        }
        String[] param = authentication.split("-");
        if (param.length != 2) {
            logger.info("[auth check fail][token format fail]");
            return null;
        }
        //使用deviceId和源token简单拼接成的token，可以增加加密措施
        String deviceId = param[0];
        String token = param[1];
        return new TokenEntity(deviceId, token);
    }

    public boolean checkToken(TokenEntity model) {
        if (model == null) {
            return false;
        }
        String token = redis.boundValueOps(model.getDeviceId()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        return true;
    }

    public void deleteToken(String deviceId) {
        redis.delete(deviceId);
    }
}
