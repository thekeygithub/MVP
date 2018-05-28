package com.ebao.hospitaldapp.rest.entity.request;

import com.ebao.hospitaldapp.rest.base.result.JsonRESTResult;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountEntity;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PayByPasswordInfo extends PaymentBase{

    @NotBlank(message = "请输入密码")
    private String password;

}
