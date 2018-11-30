package com.ebao.hospitaldapp.utils;

import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class CommonUtils {

    public static Map<String,Object> makeFailMap(String status, String reason){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status", status);
        map.put("reason", reason);
        return map;
    }

    public static Map<String,Object> makeListMap(String status, String listName, String reason,List<?> objList){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status", status);
        map.put("reason", reason);
        map.put(listName, objList);
        return map;
    }

    public static String KeyGenerator(String delimiter, String ...params)
    {
        String key = "";
        for(int i=0; i<params.length; i++)
        {
            key = key.concat(params[i]);
            if(i != params.length-1)
                key = key.concat(delimiter);
        }
        return key;
    }

    public static boolean isFloatNumber(String number){
        Pattern pattern = Pattern.compile("[0-9]*(\\.)[0-9]*");     //浮点型一定有 "."
        return pattern.matcher(number).matches();
    }

    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null) return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
        return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取随机数字
     * @return 随机数字
     */
    public static String getRandomNumberString(int length) {
        String random = String.valueOf(new Random().nextDouble()).substring(2);
        return StringUtils.lengthenByPreRandom(random, length, StringUtils.CharRandomType.Number);
    }
}
