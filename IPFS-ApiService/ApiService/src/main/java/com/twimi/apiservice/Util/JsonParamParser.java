package com.twimi.apiservice.Util;

import java.util.HashMap;
import java.util.Map;

public class JsonParamParser {
    public static Map<String, String> getParam(String strUrlParam) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            if (arrSplitEqual.length > 1) {
                mapRequest.put(arrSplitEqual[0].toLowerCase(), arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    mapRequest.put(arrSplitEqual[0].toLowerCase(), "");
                }
            }
        }
        return mapRequest;
    }

}
