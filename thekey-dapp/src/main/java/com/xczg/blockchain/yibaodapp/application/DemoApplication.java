package com.xczg.blockchain.yibaodapp.application;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.xczg.blockchain"})
public class DemoApplication extends SpringBootServletInitializer{
	 
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(DemoApplication.class);
	}
	
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
   
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        //设置默认区域,
        slr.setDefaultLocale(Locale.CHINA);
        return slr;
    }
}
