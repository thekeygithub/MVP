package com.ebao.hospitaldapp.rest.service;

import com.ebao.hospitaldapp.utils.CollectionUtils;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class SerializedStringService {

    private static List<String> numberMeaning;
    private static Map<String, String> resultMap;

    private static String PREFIX;
    private static String SUCCESS;
    private static String FAIL;
    private static String UNKNOW_ERROR;
    private static int offset;

    public boolean isSuccess(String serializedString){
        if (!check(serializedString)) return false;
        String[] chars = serializedString.split("");
        String s = chars[0];
        return s.equals(SUCCESS) ? true : false;
    }

    public String getSingleResult(String serializedString){
        if (!check(serializedString)) return null;
        String[] chars = serializedString.split("");
        for (int i=0+offset; i<chars.length; i++){
            if (FAIL.equals(chars[i])){
                return PREFIX + numberMeaning.get(i) + resultMap.get(chars[i]);
            }
        }
        if(!isSuccess(serializedString)){
            return UNKNOW_ERROR;
        }
        return PREFIX + numberMeaning.get(offset - 1) + resultMap.get(SUCCESS);
    }

    public List<String> getTotalResultList(String serializedString){
        if (!check(serializedString)) return null;
        String[] chars = serializedString.split("");
        ArrayList resultList = new ArrayList();
        for (int i=0+offset; i<chars.length; i++){
            resultList.add(PREFIX + numberMeaning.get(i) + resultMap.get(chars[i]));
        }
        return resultList;
    }

    public List<String> getResultList(String serializedString){
        if (!check(serializedString)) return null;
        String[] chars = serializedString.split("");
        ArrayList resultList = new ArrayList();
        for (int i=0+offset; i<chars.length; i++){
            resultList.add(PREFIX + numberMeaning.get(i) + resultMap.get(chars[i]));
            if (!SUCCESS.equals(chars[i])){
                return resultList;
            }
        }
        return resultList;
    }

    private boolean check(String serializedString){
        if (CollectionUtils.isEmpty(numberMeaning) || !StringUtils.hasText(serializedString)){
            return false;
        }
        return numberMeaning.size() == serializedString.length() ? true : false;
    }

    static {
        offset = 1;
        SUCCESS = "0";
        FAIL = "1";
        PREFIX = "验证";
        UNKNOW_ERROR = "网络超时";

        resultMap = new HashMap<>();
        resultMap.put("0", "成功");
        resultMap.put("1", "未通过");
        resultMap.put("2", "未进行");
        resultMap.put("3", "错误");

        numberMeaning = new ArrayList<>();
        numberMeaning.add("");
        numberMeaning.add("社保卡基本信息");
        numberMeaning.add("社保卡头像信息");
        numberMeaning.add("公安头像信息");
        numberMeaning.add("人脸识别");
        numberMeaning.add("社保卡状态信息");
        numberMeaning.add("生物体征信息");
        numberMeaning.add("入刑判断");
        numberMeaning.add("追逃判断");
        numberMeaning.add("国家机密特殊诊断信息");
        numberMeaning.add("国家机密特殊人员信息");
        numberMeaning.add("位置信息");
        numberMeaning.add("CPA信息");
    }

    public static void main(String[] args) {
        String num = "3222222222222";
        SerializedStringService serializedStringService = new SerializedStringService();
        System.out.println(serializedStringService.getSingleResult(num));
    }
}
