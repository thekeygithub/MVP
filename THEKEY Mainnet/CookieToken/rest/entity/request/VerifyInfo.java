package com.ebao.hospitaldapp.rest.entity.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ToString(callSuper=true,exclude="image")
public class VerifyInfo {
    @NotBlank(message = "请输入设备ID")
    private String deviceId;
    @NotBlank(message = "请输入医院ID")
    private String hospId;
    @NotBlank(message = "请输入日期")
    private String dateTime;

    @NotBlank(message = "请输入18位身份证号码")
    @Size(min = 18, max = 18, message = "请输入18位身份证号码")
    private String idNumber;
    @NotBlank(message = "请上传照片")
    private String image;
    @NotBlank(message = "请输入就诊流水号")
    private String treatmentId;
}
