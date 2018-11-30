package com.ebao.hospitaldapp.rest.service;

import com.ebao.hospitaldapp.rest.entity.TokenEntity;

public interface TokenService {

    /**
     * 创建一个token关联上指定用户
     * @param deviceId 指定用户的id
     * @return 生成的token
     */
    public TokenEntity createToken(String deviceId);

    /**
     * 检查token是否有效
     * @param model token
     * @return 是否有效
     */
    public boolean checkToken(TokenEntity model);

    /**
     * 从字符串中解析token
     * @param authentication 加密后的字符串
     * @return
     */
    public TokenEntity getToken(String authentication);

    /**
     * 清除token
     * @param deviceId 登录用户的id
     */
    public void deleteToken(String deviceId);
}
