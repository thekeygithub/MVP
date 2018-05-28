package com.ebao.hospitaldapp.rest.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SMS {
    @NotBlank(message = "请输入手机号")
    String mobile;
    @NotBlank(message = "请输入短信验证类型")
    String type;
}
