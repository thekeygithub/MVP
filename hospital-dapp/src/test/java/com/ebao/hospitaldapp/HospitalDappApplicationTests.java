package com.ebao.hospitaldapp;

import com.ebao.hospitaldapp.rest.service.SerializedStringService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HospitalDappApplicationTests {

	@Autowired
	SerializedStringService serializedNumberService;

	@Test
	public void contextLoads() {
		System.out.println("123");
	}

}
