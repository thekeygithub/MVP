package com.xczg.blockchain.yibaodapp.util;

/**
 * 接收方枚举类
 * @author Administrator
 *
 */
public enum ReceiverEnum {

	YB("THEKEY", "yb"),RS("人社", "rs"), GA("公安", "ga"), YYS("运营商", "yys"),YY("医院","yy");

	// 成员变量
	String name;
	private String code;

	// 构造方法
	private ReceiverEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}

	// 普通方法
	public static String getName(String code) {
		for (ReceiverEnum c : ReceiverEnum.values()) {
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
