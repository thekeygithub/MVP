package com.ebao.hospitaldapp.rest.base.annotations;


import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping
@Inherited
public @interface AuthCheck {
    String desc() default "token验证开始...";
}
