package com.xczg.blockchain.common.model;

import java.util.Map;
/**
* 记录缓存数据库一个表对应的Entity的信息
*
 * @author zd
 * @version  2013-03-19 11:08:15
*/
public class EntityInfo {
	
	private String tableName=null;
	private String className=null;
	private String filedsName=null;
	private String tableExt="";
	private Map<String,String> sqlmap=null;

	private Class<?> pkReturnType;
	
	
	private String pkName=null;

	
	private boolean isGroup=false;
	/**
	 * 主键序列
	 */
	private String pksequence=null;
	/**
	 * 是否不记录日志,默认false 为记录
	 */
	private boolean notLog=false;
	
	/**
	 * 数据库的表名
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * 数据库表映射的实体类名
	 * @return
	 */
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * 获取表所有的字段名,用 , 隔开
	 * @return String
	 */
	public String getFiledsName() {
		return filedsName;
	}
	public void setFiledsName(String filedName) {
		this.filedsName = filedName;
	}
	
	/**
	 * 数据库分表的后缀名 例如 _history_2012
	 * @return
	 */
	public String getTableExt() {
		return tableExt;
	}
	public void setTableExt(String tableExt) {
		this.tableExt = tableExt;
	}
	/**
	 * 获取table主键对应Enitty属性名称
	 * @return String
	 */
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	public Map<String, String> getSqlmap() {
		return sqlmap;
	}
	public void setSqlmap(Map<String, String> sqlmap) {
		this.sqlmap = sqlmap;
	}
	public boolean isGroup() {
		return isGroup;
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	public String getPksequence() {
		return pksequence;
	}
	public void setPksequence(String pksequence) {
		this.pksequence = pksequence;
	}
	public boolean isNotLog() {
		return notLog;
	}
	public void setNotLog(boolean notLog) {
		this.notLog = notLog;
	}
	public Class getPkReturnType() {
		return pkReturnType;
	}
	public void setPkReturnType(Class pkReturnType) {
		this.pkReturnType = pkReturnType;
	}


}