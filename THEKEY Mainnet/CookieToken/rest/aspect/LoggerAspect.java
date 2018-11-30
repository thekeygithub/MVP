package com.ebao.hospitaldapp.rest.aspect;


import com.ebao.hospitaldapp.utils.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Aspect
@Component
public class LoggerAspect {

    final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    // 定义切点Pointcut
    @Pointcut("execution(* com.ebao.hospitaldapp.rest.controller..*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        StringBuilder param = new StringBuilder();
        if (!CollectionUtils.isEmpty(pjp.getArgs())) {
            for (Object obj : pjp.getArgs()) {
                if (obj == null)
                    continue;
                param.append(obj.getClass().getSimpleName());
                if (obj instanceof MultipartFile[]) {
                    for (MultipartFile file : (MultipartFile[]) obj) {
                        param.append(file.getSize()).append("|");
                    }
                } else if (obj instanceof MultipartFile) {
                    param.append(((MultipartFile) obj).getSize());
                } else if (obj instanceof InputStream) {
                } else
                    param.append(":").append(obj).append(",");
            }
        }
        logger.info(pjp.toShortString() + "[" + param + "]");

        Object object = pjp.proceed();
        logger.info(pjp.toShortString() + "[RETURN:" + object + "]");

        return object;
    }
}
