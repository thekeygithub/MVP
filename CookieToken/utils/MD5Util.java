package com.ebao.hospitaldapp.utils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//对字符串进行MD5编码
public class MD5Util {

    public static String sign(Map<String, String> paramValues, String secret) throws Exception {
        StringBuilder sb = new StringBuilder();
        List<String> paramNames = new ArrayList<String>(paramValues.size());
        paramNames.addAll(paramValues.keySet());
        Collections.sort(paramNames);
        sb.append(secret);
        for (String paramName : paramNames) {
            sb.append(paramValues.get(paramName));
        }
        sb.append(secret);
        String signMd5 = getMD5Digest(sb.toString());
        return signMd5;
    }

    public static String getMD5Digest(String data) {
        try {
            byte[] bytes = null;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes("UTF-8"));
            bytes = md.digest();

            StringBuilder stringBuilder = new StringBuilder("");
            for (int i = 0; i < bytes.length; i++) {
                int v = bytes[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        } catch (GeneralSecurityException gse) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
