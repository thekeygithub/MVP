package com.ebao.hospitaldapp;

import com.ebao.hospitaldapp.config.RedisConfig;
import com.ebao.hospitaldapp.data.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


//@SpringBootApplication
//@EnableCaching
//@EnableScheduling
//@EnableAsync
public class HospitalDappApplication {

	public static void main(String[] args) {
		final  Logger logger = LoggerFactory.getLogger(HospitalDappApplication.class);
		logger.info("Start hospital dapp");

		//读取redis配置
		RedisConfig rConfig = new RedisConfig();
		rConfig.init();

		SpringApplication app = new SpringApplication(HospitalDappApplication.class);
		app.run(args);
		logger.info("Start hospitial end");
		System.out.println("Ebao hospital is running...");

		System.out.println(RedisClient.ping());
	}
}
