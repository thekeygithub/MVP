package com.ebao.hospitaldapp.rest.aspect;

import com.ebao.hospitaldapp.rest.base.annotations.AuthCheck;
import com.ebao.hospitaldapp.rest.base.enums.Exceptions;
import com.ebao.hospitaldapp.rest.base.result.JsonRESTResult;
import com.ebao.hospitaldapp.rest.entity.TokenEntity;
import com.ebao.hospitaldapp.rest.service.TokenService;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthCheckAspect {
    final Logger logger = LoggerFactory.getLogger(AuthCheckAspect.class);

    @Autowired
    private TokenService tokenService;
    @Autowired
    private HttpServletRequest request;

    @Around("(within(@org.springframework.web.bind.annotation.RequestMapping *)) && @annotation(is)")
    public Object doAround(ProceedingJoinPoint point, AuthCheck is) throws Throwable {

        String tokens = request.getHeader("TOKEN");
        TokenEntity tokenEntity = tokenService.getToken(tokens);
        if (tokenEntity == null){
            return new JsonRESTResult(Exceptions.Unauthorized, "请登录").encode();
        }

        if (!tokenService.checkToken(tokenEntity)){
            logger.info("[auth check fail][token invalid]");
            return new JsonRESTResult(Exceptions.Unauthorized, "请登录").encode();
        };

        return point.proceed();
    }
}
