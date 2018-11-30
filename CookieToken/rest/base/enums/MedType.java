package com.ebao.hospitaldapp.rest.base.enums;

public enum MedType implements INameEnum {


    UNKNOWN(0, "未知", "unknown"),

    OUTPATIENT(11, "普通门诊", "outpatient"),
    PHARMACY(14, "药店购药", "pharmacy"),
    CHRONIC(16, "门诊规定病种(慢性病)", "chronic"),
    PLANNEDBIRTH(45, "计划生育手术(门诊)", "plannedbirth"),
    ;

    private int value;
    private String label;
    private String code;
    MedType(int value, String label, String code){
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
