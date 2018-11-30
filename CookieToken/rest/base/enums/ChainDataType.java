package com.ebao.hospitaldapp.rest.base.enums;

public enum ChainDataType  implements INameEnum {
    // error
    None(0, "未知类型"),
    //validInfo
    VALIDINFO(1, "认证信息"),
    PAYINFO(2, "支付信息"),
    ;

    private int value;
    private String label;
    ChainDataType(int value, String label){
        this.value = value;
        this.label = label;
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
        return String.valueOf(value);
    }

    public ChainDataType setLabel(String label) {
        this.label = label;
        return this;
    }

    public boolean isValid(){
        return this.value != ChainDataType.None.getValue();
    }

    /**
     * 通过数值获取枚举对象
     * @param value
     * @return
     */
    public static ChainDataType parse(int value){
        ChainDataType type = ChainDataType.None;
        for(ChainDataType t:ChainDataType.values()){
            if(value == t.getValue()){
                type = t;
                break;
            }
        }
        return type;
    }


    public boolean equals(ChainDataType type){
        return (this.value == type.getValue()) && (this.label == type.getLabel());
    }

    @Override
    public String toString() {
        return this.getLabel();
    }
}
