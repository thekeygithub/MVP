package com.ebao.hospitaldapp.rest.entity.request;

import com.ebao.hospitaldapp.rest.base.enums.Genders;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class User {
    @NotBlank(message = "请输入设备ID")
    private String deviceId;
    @NotBlank(message = "请输入医院ID")
    private String hospId;
    @NotBlank(message = "请输入日期")
    private String dateTime;
    private String patientId;
    @NotBlank(message = "请输入18位身份证号码")
    @Size(min = 18, max = 18, message = "请输入18位身份证号码")
    private String idNumber;
    private String image;
    private Genders gender;

}
