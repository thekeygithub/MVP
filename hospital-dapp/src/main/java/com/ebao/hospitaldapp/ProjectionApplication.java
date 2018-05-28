package com.ebao.hospitaldapp;

import com.ebao.hospitaldapp.config.RedisConfig;
import com.ebao.hospitaldapp.data.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
public class ProjectionApplication extends SpringBootServletInitializer implements WebApplicationInitializer{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        RedisConfig rConfig = new RedisConfig();
        rConfig.init();
        return builder.sources(ProjectionApplication.class);
    }

    public static void main(String[] args){
        final Logger logger = LoggerFactory.getLogger(ProjectionApplication.class);
        logger.info("Start hospital dapp");
        SpringApplication.run(ProjectionApplication.class, args);
        logger.info("Start hospitial end");
        System.out.println(RedisClient.ping());
    }
}
