package com.ebao.hospitaldapp.rest.entity.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class Terminal {
    @NotBlank(message = "请输入设备ID")
    private String deviceId;
    @NotBlank(message = "请输入医院ID")
    private String hospId;
    @NotBlank(message = "请输入日期")
    private String dateTime;

}
