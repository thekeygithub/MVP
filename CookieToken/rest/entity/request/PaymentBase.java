package com.ebao.hospitaldapp.rest.entity.request;

import com.ebao.hospitaldapp.rest.base.result.JsonRESTResult;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public abstract class PaymentBase {
    @NotBlank(message = "请输入TKY")
    private String TKY;
    @NotBlank(message = "请输入设备ID")
    private String deviceId;
    @NotBlank(message = "请输入医院ID")
    private String hospId;
    @NotBlank(message = "请输入日期")
    private String dateTime;
    @NotBlank(message = "请输入18位身份证号码")
    @Size(min = 18, max = 18, message = "请输入18位身份证号码")
    private String idNumber;
    @NotBlank(message = "请输入就诊流水号")
    private String treatmentId;
    @NotBlank(message = "请输入结算单号")
    private String transactionNO;
    @NotEmpty(message = "请输入处方列表")
    private List<String> prescriptionList;

}
