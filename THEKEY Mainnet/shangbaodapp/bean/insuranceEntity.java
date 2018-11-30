package com.xczg.blockchain.shangbaodapp.bean;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Value;

@Entity
public class insuranceEntity {
	
	@Value("平安人寿")
	private String insuName; 
	@Value("医疗保险")
	private String type;
	@Value("0.8")
	private int scale;//报销比例
}
