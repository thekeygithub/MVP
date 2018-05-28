package com.ebao.hospitaldapp.ebao;

import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.config.ApplicationConfig;
import com.ebao.hospitaldapp.rest.service.PrescriptionsRedisService;
import com.ebao.hospitaldapp.utils.HttpUtils;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GetPrescriptionInfo {

    final static Logger LOGGER = LoggerFactory.getLogger(GetUnpaidUserList.class);

    @Autowired
    PrescriptionsRedisService prescriptionsRedisService;
    @Autowired
    ApplicationConfig applicationConfig;

    public void retrieveFromEbaoAPI(String hospId, String jzlsno) {
        String url = applicationConfig.getGetPrescriptionUrl();

        if (!StringUtils.hasText(hospId) || !StringUtils.hasText(jzlsno)) {
            LOGGER.error("请输入正确的hospId和jzlsno！");
            return;
        }
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("hospId", hospId);
        param.put("jzlsno", jzlsno);
        JSONObject jsObj;

        try {
            String response = HttpUtils.sendPOST(url, param);
            //解析response
            jsObj = JSONObject.parseObject(response);
        }
        catch(Exception e)
        {
            LOGGER.error("GetPrescriptionInfo: ebao API服务异常！",e);
            return;
        }

        if(jsObj == null)
        {
            LOGGER.error("GetPrescriptionInfo: 无响应！");
            return;
        }
        if(!jsObj.containsKey("code") || !jsObj.getString("code").equals("0"))
        {
            LOGGER.error("GetPrescriptionInfo return: code="+jsObj.get("code")+", message="+jsObj.get("message"));
            return;
        }

        try {
            if(jsObj.containsKey("groupList"))
            {
                if(prescriptionsRedisService.putEntityListToRedis(jsObj.getJSONArray("groupList")) == 0)
                    LOGGER.debug("GetPrescriptionInfo，已放入缓存！");
                else
                    LOGGER.error("GetPrescriptionInfo，缓存失败！");
            }
        }
        catch(Exception e)
        {
            LOGGER.error("GetPrescriptionInfo，读取响应失败！",e);
        }


    }
}
