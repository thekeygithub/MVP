package com.ebao.hospitaldapp.utils;

import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.config.RestClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class HttpUtils {

    final static Logger LOGGER = LoggerFactory.getLogger(RestClientConfig.class);

    private static RestClientConfig restConf = new RestClientConfig();
    private static RestTemplate restTemplate = restConf.restTemplate();

    //POST
    //json参数
    public static String sendPOST(String url, JSONObject requset)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");//解决请求乱码问题
        headers.set("Accept", "application/json;charset=UTF-8");
        headers.set("User-Agent","hospital dapp");

        ResponseEntity<String> responseEntity = null;
        String body="";
        try {
            responseEntity = restTemplate.postForEntity(url,new HttpEntity<String>(requset.toString(), headers), String.class);
            LOGGER.debug("响应header：responseEntity0.getHeaders="+ responseEntity.getHeaders());
            //body=new String(responseEntity.getBody().getBytes("ISO-8859-1"), "utf-8");//解决返回报文的乱码问题
            body = responseEntity.getBody();
            LOGGER.debug("响应body：responseEntity0.getBody()="+ responseEntity.getBody());
        } catch (Exception e) {
            LOGGER.error("error happened in sendPost() : {}", e.getMessage());
            LOGGER.debug("error happened in sendPost(),", e);
        }
        return body;
    }

    public static String sendPOST(String url, String requset)
    {
        return sendPOST(url, JSONObject.parseObject(requset));
    }

    //POST
    //普通参数
    public static String sendPOST(String url,HashMap<String,Object> param)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/plain;charset=UTF-8");//解决请求乱码问题
        headers.set("Accept", "application/json;charset=UTF-8");
        headers.set("User-Agent","hospital dapp");

        ResponseEntity<String> responseEntity = null;
        String body="";
        try {
            LOGGER.debug("请求参数:{}", param);
            //restTemplate.postForObject(url,new HttpEntity<String>(null, headers), String.class, param);
            responseEntity = restTemplate.postForEntity(url,new HttpEntity<String>(null, headers), String.class, param);
            LOGGER.debug("响应header：responseEntity0.getHeaders={}", responseEntity.getHeaders());
            //body=new String(responseEntity.getBody().getBytes("ISO-8859-1"), "UTF-8");//解决返回报文的乱码问题
            body = responseEntity.getBody();
            LOGGER.debug("响应body：responseEntity0.getBody()={}", responseEntity.getBody());
        } catch (Exception e) {
            LOGGER.error(" errpr happened in sendPost(),", e);
        }
        return body;
    }


    //GET
    public static String sendGET(String url, String reqStr)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");//解决请求乱码问题
        headers.set("Accept", "application/json;charset=UTF-8");
        headers.set("User-Agent","hospital dapp");

        ResponseEntity<String> responseEntity = null;
        String body="";
        try {
            responseEntity = restTemplate.getForEntity(url, String.class);
            LOGGER.debug("响应header：responseEntity0.getHeaders="+ responseEntity.getHeaders());
            body=new String(responseEntity.getBody().getBytes("ISO-8859-1"), "UTF-8");//解决返回报文的乱码问题
            LOGGER.debug("响应body：responseEntity0.getBody()="+ responseEntity.getBody());
        } catch (Exception e) {
            LOGGER.error(" errpr happened in sendGET(),", e);
        }
        return body;
    }


}
