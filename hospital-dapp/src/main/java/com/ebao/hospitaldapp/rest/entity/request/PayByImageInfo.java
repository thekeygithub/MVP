package com.ebao.hospitaldapp.rest.entity.request;

import com.ebao.hospitaldapp.ebao.GetPictureCheckResult;
import com.ebao.hospitaldapp.rest.base.result.JsonRESTResult;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountEntity;
import com.ebao.hospitaldapp.utils.StringUtils;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ToString(callSuper=true,exclude="image")
public class PayByImageInfo extends PaymentBase{

    @NotBlank(message = "请输入图片")
    private String image;

}
