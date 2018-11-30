package com.ebao.hospitaldapp.rest.base.enums;

import java.io.Serializable;

/**
 * 名称枚举接口
 */
public interface INameEnum extends Serializable {
    /**
     * 获取枚举变量对应的数值
     * @return  数值
     */
    int getValue();

    /**
     * 获取枚举变量对应的提示信息
     * @return  提示信息
     */
    String getLabel();

    /**
     * 获取枚举变量对应的编码
     * @return  编码
     */
    String getCode();
    
//    <T extends INameEnum> T parse(int value);
//    <T extends INameEnum> T parse(String code);
}


