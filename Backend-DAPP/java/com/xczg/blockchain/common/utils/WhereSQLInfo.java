package com.xczg.blockchain.common.utils;

/**
* Where 条件的信息,用于缓存信息
 * @author zd
 * @version  2013-03-19 11:08:15
*/
public class WhereSQLInfo {

	private String name;
	private String wheresql;
	/**
	 * 获取字段的属性名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取对应字段的 where sql 语句
	 * @return
	 */
	public String getWheresql() {
		return wheresql;
	}
	public void setWheresql(String wheresql) {
		this.wheresql = wheresql;
	}
}
