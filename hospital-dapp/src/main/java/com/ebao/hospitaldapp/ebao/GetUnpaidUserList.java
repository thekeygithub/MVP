package com.ebao.hospitaldapp.ebao;

import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.config.ApplicationConfig;
import com.ebao.hospitaldapp.rest.service.UserInfoRedisService;
import com.ebao.hospitaldapp.utils.HttpUtils;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GetUnpaidUserList {

    final static Logger LOGGER = LoggerFactory.getLogger(GetUnpaidUserList.class);

    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired
    UserInfoRedisService userInfoRedisService;

    public int retrieveFromEbaoAPI(String hospId, String dateTime) {
        LOGGER.debug("retrieve unpaid user list from EbaoAPI...");

        String url = applicationConfig.getGetUnpaidUserUrl();

        if (!StringUtils.hasText(hospId) || !StringUtils.hasText(dateTime) ) {
            LOGGER.error("请输入正确的hospId和dateTime！");
            return -1;
        }
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("hospId", hospId);
        param.put("dateTime", dateTime);
        JSONObject jsObj;

        try {
            String response = HttpUtils.sendPOST(url, param);
            //解析response
            jsObj = JSONObject.parseObject(response);
        }
        catch(Exception e)
        {
            LOGGER.error("addPayInfo: ebao API服务异常！",e);
            return -1;
        }

        if(jsObj == null)
        {
            LOGGER.error("GetUnpaidUserList: 无响应！");
            return -1;
        }
        if(!jsObj.containsKey("code") || !jsObj.getString("code").equals("0"))
        {
            LOGGER.error("GetUnpaidUserList return: code="+jsObj.get("code")+", message="+jsObj.get("message"));
            return -1;
        }
        try {
            if(jsObj.containsKey("list"))
            {
                //JSONArray userlist = jsObj.getJSONArray("list");
                if(userInfoRedisService.putEntityListToRedis(hospId, jsObj.getJSONArray("list")) == 0)
                    LOGGER.debug("GetUnpaidUserList，已放入缓存！");
                else
                    LOGGER.error("GetUnpaidUserList，缓存失败！");
            }
        }
        catch(Exception e)
        {
            LOGGER.error("GetUnpaidUserList，读取响应失败！",e);
            return -1;
        }

        return 0;
    }
}
