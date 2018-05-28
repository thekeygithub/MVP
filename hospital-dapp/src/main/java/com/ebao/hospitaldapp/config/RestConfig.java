package com.ebao.hospitaldapp.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "rest")
@Data
public class RestConfig {

    final static Logger LOGGER = LoggerFactory.getLogger(RestConfig.class);

    private int readTimeout;
    private int connectTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
    {
        LOGGER.info("rest超时时间，读超时={}毫秒，连接超时={}毫秒", readTimeout, connectTimeout);
        return restTemplateBuilder
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .build();
    }
}
