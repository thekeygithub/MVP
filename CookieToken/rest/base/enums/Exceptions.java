package com.ebao.hospitaldapp.rest.base.enums;


/**
 * 异常枚举值
 */
public enum Exceptions implements INameEnum {

    // Common
    None(200, "无异常"),
    UnknownException(1, "未知异常"),
    NotSupport(2, "不支持"),
    Closed(3, "已关闭"),

    InvalidRequest(400, "参数格式错误"),
    Unauthorized(401, "鉴权失败"),
    Forbidden(403, "禁止访问"),
    NotFound(404, "未找到"),

    NotFinish(801, "未完成"),

    SystemMaintaining(999, "系统维护中"),


    // System
    ParamEmpty(1001, "参数不能为空"),
    ParamInvalid(1002, "参数无效"),
    ParamFormatInvalid(1003, "格式错误"),

    OperationFail(1101, "操作失败"),
    OperationForbidden(1102, "禁止操作"),
    OperationDuplicate(1103, "操作重复"),

    ObjectNotFound(1201, "未找到指定的内容"),
    ObjectInvalid(1202, "指定的内容无效"),
    ObjectDisabled(1203, "指定的内容不可用"),
    ObjectExists(1204, "指定的内容已存在"),

    DeleteDisallowed(1301, "不允许删除"),

    VCodeInvalid(1401, "验证码错误"),
    VCodeExpired(1402, "验证码已过期"),


    // Account
    AccountNotExists(10001, "账号不存在"),
    IdentityNotExists(10002, "账号标识不存在"),
    IdentityBounded(10003, "账号标识已绑定"),
    IdentityNotMatch(10004, "标识与账号不匹配"),
    GetTokenFailed(10005, "获取token失败"),
    SMSSendError(10006, "短信发送失败"),
    SMSNotMatch(10007, "短信验证码不正确"),
    InvalidNumber(10008, "非法号码"),
    SMSNotExist(10009, "请重新发送短信验证码"),
    NoTreatmentInfo(10009, "没有待缴费信息"),


    //verify
    VerifyFail(1001, "验证失败"),

    //payment
    TKYCalculateError(2001, "TKY计算错误"),
    PasswordError(2002, "支付密码不正确"),
    ;




    private int value;
    private String label;
    Exceptions(int value, String label){
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


    public Exceptions setLabel(String label) {
        this.label = label;
        return this;
    }

    public boolean isValid(){
        return this.value == Exceptions.None.getValue();
    }

    /**
     * 通过数值获取枚举对象
     * @param value
     * @return
     */
    public static Exceptions parse(int value){
        Exceptions type = Exceptions.UnknownException;
        for(Exceptions t:Exceptions.values()){
            if(value == t.getValue()){
                type = t;
                break;
            }
        }
        return type;
    }


    @Override
    public String toString() {
        return this.getLabel();
    }
}
