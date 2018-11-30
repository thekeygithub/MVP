package com.xczg.blockchain.yibaodapp.util;

public enum InterfaceEnum {
	SBKJBXX("社保卡基本信息接口", "SBKJBXX"), SBKTXXX("社保卡头像信息接口", "SBKTXXX"), GAJBXX(
			"公安基本信息接口", "GAJBXX"), GATXXX("公安头像信息接口", "GATXXX"), HGJBXX(
			"海关基本信息接口", "HGJBXX"), HGTXXX("海关头像信息接口", "HGTXXX"), RLSB("人脸识别接口",
			"RLSB"), SBKZTXX("社保卡状态信息接口", "SBKZTXX"), SWPD("死亡判断接口", "SWPD"), RXPD(
			"入刑判断接口", "RXPD"), ZTPW("追逃判断接口", "ZTPW"), TSZD("国家机密信息-特殊诊断接口",
			"TSZD"), TSRY("国家机密信息-特殊人员接口", "TSRY"), GPS("位置信息接口", "GPS"), CPAB(
			"CPA事前审核接口", "CPAB"), CPAA("CPA事后审核接口", "CPAA"), YBYJS("医保预结算接口",
			"YBYJS");
  
	// 成员变量
	private String name;
	private String code;

	// 构造方法
	private InterfaceEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}

	// 普通方法
	public static String getName(String code) {
		for (InterfaceEnum c : InterfaceEnum.values()) {
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
