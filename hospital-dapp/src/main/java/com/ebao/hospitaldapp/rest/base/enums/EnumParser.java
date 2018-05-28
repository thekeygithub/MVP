package com.ebao.hospitaldapp.rest.base.enums;

import com.ebao.hospitaldapp.utils.CollectionUtils;
import com.ebao.hospitaldapp.utils.StringUtils;

public class EnumParser {

    public static <T extends INameEnum> T parse(Class<T> clazz, int value){

        T[] values = clazz.getEnumConstants();
        for (T v : values){
            if (v.getValue() == value)
                return v;
        }

        if (CollectionUtils.isEmpty(values))
            return null;

        return values[0];
    }

    public static <T extends INameEnum> T parseIfFound(Class<T> clazz, int value){

        T[] values = clazz.getEnumConstants();
        for (T v : values){
            if (v.getValue() == value)
                return v;
        }

        if (CollectionUtils.isEmpty(values))
            return null;

        return null;
    }

    public static <T extends INameEnum> T parse(Class<T> clazz, String code){

        T[] values = clazz.getEnumConstants();
        for (T v : values){
            if (StringUtils.equalsIgnoreCase(v.getCode(), code))
                return v;
        }

        if (CollectionUtils.isEmpty(values))
            return null;

        return values[0];
    }
}
