package com.ebao.hospitaldapp.rest.base.enums;

/**
 * 性别
 */
public enum Genders implements INameEnum {

    UNKNOWN(9, "未知", "unknown"),

    MALE(1, "男", "male"),
    FEMALE(2, "女", "female"),

    SECRECY(99, "保密", "secrecy"),

    ;

    private int value;
    private String label;
    private String code;
    Genders(int value, String label, String code){
        this.value = value;
        this.label = label;
        this.code = code;
    }
    /**
     * 获取枚举变量对应的数值
     * @return
     */
    public int getValue() {
        return value;
    }
    /**
     * 获取枚举变量对应的提示信息
     * @return
     */
    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }


}
