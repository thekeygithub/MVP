package com.ebao.hospitaldapp.ebao;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.utils.HttpUtils;
import com.ebao.hospitaldapp.utils.MD5Util;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


public class GetPictureCheckResult {

    final static Logger LOGGER = LoggerFactory.getLogger(GetPictureCheckResult.class);
//    private String url = "http://auth.nationauth.cn/face-service/v1.0/service/face-compare?" +
//            "app_key={app_key}&photo1={photo1}&photo2={photo2}&client_time={client_time}" +
//            "&transaction_no={transaction_no}&sign={sign}";
    private String url = "http://auth.nationauth.cn/face-service/v1.0/service/face-compare";



    @Getter @Setter private String photo1 = "";
    @Getter @Setter private String photo2 = "";
    @Getter @Setter private String app_secret = "2RLYsN1SK03OsQD4EPWWrDOW";
    @Getter @Setter private String app_key = "1208661ca8c342d4a1b02ca34baf6640";
    @Getter @Setter private String client_time = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
    @Getter @Setter private String transaction_no = UUID.randomUUID().toString().replaceAll("-", "");



    public String retrieveFromPicAPI(String photo1, String photo2){
        LOGGER.debug("Picture checking from EbaoAPI...");

        if (photo1.isEmpty() && photo2.isEmpty()) {
            LOGGER.debug("请保证输入两张图片！");
            return null;
        }
        HashMap<String, String> picRequest = new HashMap<String, String>();
        HashMap<String, String> signParameters = new HashMap<String, String>();
        picRequest.put("app_key", app_key);
        picRequest.put("photo1", photo1);
        picRequest.put("photo2", photo2);
        picRequest.put("client_time", client_time);
        picRequest.put("transaction_no", transaction_no);

        signParameters.put("app_key", app_key);
        signParameters.put("client_time", client_time);
        signParameters.put("transaction_no", transaction_no);

        String sign = "";
        try{
            sign = MD5Util.sign(signParameters, app_secret);
        }catch (Exception e){
            sign = "";
            LOGGER.error("人脸识别接口MD5加密 error " + e.getMessage(), e);
            return null;
        }
        picRequest.put("sign", sign);

        try{
            String response = HttpUtils.sendPOST(url, JSONObject.parseObject(JSON.toJSONString(picRequest)));
            //解析response
            JSONObject jsObj = JSONObject.parseObject(response);
            LOGGER.info("人脸比对结果：{}", response);
            Integer msg = jsObj.get("score") == null ? 0 : Integer.valueOf(jsObj.get("score").toString());
            //System.out.println("jieguo: " + msg);
            LOGGER.info("人脸比对分数：{}", msg);
            return msg >= 60 ? response : null;
        } catch (Exception e){
            LOGGER.error("addPayInfo: 图片检测 API服务异常！",e);
            return null;
        }
    }




}
