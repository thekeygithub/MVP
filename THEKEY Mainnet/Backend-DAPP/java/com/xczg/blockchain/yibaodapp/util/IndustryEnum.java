package com.xczg.blockchain.yibaodapp.util;

/**
 * 行业枚举类
 * @author Administrator
 *
 */
public enum IndustryEnum {

	hospital("医院", "01"), hotel("酒店", "02"), customs("海关", "03"), insurance(
			"保险", "04"), school("学校", "05");

	// 成员变量
	private String name;
	private String code;

	// 构造方法
	private IndustryEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}

	// 普通方法
	public static String getName(String code) {
		for (IndustryEnum c : IndustryEnum.values()) {
			if (c.getCode().equals(code)) {
				return c.name;
			}
		}
		return null;
	}

	// get set 方法
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
