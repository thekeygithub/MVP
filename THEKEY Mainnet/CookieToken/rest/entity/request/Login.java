package com.ebao.hospitaldapp.rest.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Login {

    @NotBlank(message = "请输入手机号")
    private String mobile;
    @NotBlank(message = "请输入短信验证码")
    private String code;

}
