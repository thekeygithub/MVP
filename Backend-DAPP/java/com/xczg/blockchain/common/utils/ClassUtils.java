package com.xczg.blockchain.common.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import com.xczg.blockchain.common.annotation.WhereSQL;
import com.xczg.blockchain.common.model.EntityInfo;


/**
* 处理类的工具类. 例如反射
*
 * @copyright {@link 9iu.org}
 * @author springrain<Auto generate>
 * @version  2013-03-19 11:08:15
 * @see org.springrain.frame.util.ClassUtils
*/

public class ClassUtils {
	
	
	//缓存 entity的字段信息
	public static Map<String,EntityInfo> staticEntitymap=new  ConcurrentHashMap<String,EntityInfo>();
	//缓存 所有的WhereSql注解
	public static Map<String, List<WhereSQLInfo>> staticWhereSQLmap=new  ConcurrentHashMap<String, List<WhereSQLInfo>>();
	//缓存 所有的字段
	public static Map<String, Set<String>> allFieldmap=new  ConcurrentHashMap<String, Set<String>>();
	//缓存 所有的数据库字段
	public static Map<String, List<String>> allDBFieldmap=new  ConcurrentHashMap<String, List<String>>();
	
	//缓存 所有的参与Lucene的字段
	public static Map<String,List<String>> allLucenemap=new  ConcurrentHashMap<String, List<String>>();
	

	
	/**
	 * 添加一个EntityInfo 信息,用于缓存.
	 * @param info
	 * @return
	 */
	public static  Map<String,EntityInfo> addEntityInfo(EntityInfo info){
		if(info==null||info.getClassName()==null){
			 return null;
		}
		staticEntitymap.put(info.getClassName(), info);
		return staticEntitymap;
	}
/**
 * 根据ClassName获取 EntityInfo
 * @param className
 * @return
 * @throws Exception
 */
	public static EntityInfo getEntityInfoByClass(Class clazz){
		if(clazz==null)
			return null;
		String className=clazz.getName();
		if(className==null)
			return null;
		boolean iskey=staticEntitymap.containsKey(className);
		if(iskey){
			return staticEntitymap.get(className);
		}

		
		 if((clazz.isAnnotationPresent(Table.class)==false)){
			 return null;
		 }
		
		String tableName = ClassUtils.getTableNameByClass(clazz);
		if(tableName==null)
			return null;
		EntityInfo info=new EntityInfo();
		info.setTableName(tableName);
		info.setClassName(clazz.getName());
		List<String> fields = ClassUtils.getAllDBFields(clazz);
    	if(fields==null)
		return null;
    	 for(String fdName:fields){
 			boolean ispk= isAnnotation(clazz,fdName,Id.class);
 			if(ispk==true){
 				info.setPkName(fdName);
 				Class returnType = getReturnType(fdName, clazz);
 	 			info.setPkReturnType(returnType);
 				break;
 			}
 		 }
   	     	staticEntitymap.put(className,info);
		return staticEntitymap.get(className);
	}

	
	
	/**
	 * 根据ClassName获取wheresql 注解的类信息
	 * @param className
	 * @return
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public static List<WhereSQLInfo> getWhereSQLInfo(Class clazz){
		if(clazz==null)
			return null;
		String className=clazz.getName();
		if(StringUtils.isBlank(className)){
			return null;
		}
		
		boolean iskey=staticWhereSQLmap.containsKey(className);
		if(iskey){
			return staticWhereSQLmap.get(className);
		}
		Set<String> names = getAllFieldNames(clazz);
		if(CollectionUtils.isEmpty(names))
			return null;
		
		 List<WhereSQLInfo>  wheresql=new ArrayList<WhereSQLInfo> ();
		for(String name:names){
			boolean isWhereSQL= isAnnotation(clazz,name,WhereSQL.class);
			if(isWhereSQL==false){
				continue;
			}
			
			PropertyDescriptor pd;
			try {
				pd = new PropertyDescriptor(name, clazz);
			} catch (IntrospectionException e) {
				break;
			}
			Method getMethod = pd.getReadMethod();// 获得get方法
			
			WhereSQL ws= (WhereSQL) getMethod.getAnnotation(WhereSQL.class);
			WhereSQLInfo info=new WhereSQLInfo();
			info.setName(name);
			info.setWheresql(ws.sql());
			wheresql.add(info);
		}
		
		return wheresql;
		
	}
	
	
	
	

	/**
	 * 获取一个类的所有属性名称,包括继承的父类
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getAllFieldNames(Class clazz){
		if(clazz==null){
			return null;
		}
		String className=clazz.getName();
		boolean iskey=allFieldmap.containsKey(className);
		if(iskey){
		 return  allFieldmap.get(className);
		}
		Set<String>	allSet=new HashSet<String>();
		allSet=	recursionFiled(clazz,allSet);
		allFieldmap.put(className, allSet);
		return allSet;
	}
	
	
	
	
	/**
	 * 获取所有数据库的类字段对应的属性
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static List<String> getAllDBFields(Class clazz){
		
		if(clazz==null){
			return null;
		}
		String className=clazz.getName();
		boolean iskey=allDBFieldmap.containsKey(className);
		if(iskey){
			return allDBFieldmap.get(className);
		}
		
		Set<String> allNames = getAllFieldNames(clazz);
     if(CollectionUtils.isEmpty(allNames))
    	 return null;
    
     List<String>   dbList=new ArrayList<String>();
	 for(String fdName:allNames){
		boolean isDB= isAnnotation(clazz,fdName,Transient.class);
		if(isDB==false){
			dbList.add(fdName);
		}
	 }
	 allDBFieldmap.put(className, dbList);
		return dbList;
	}

	/**
	 * clazz 属性 fd 的 getReadMethod() 是否包含 注解 annotationName
	 * @param clazz
	 * @param fdName
	 * @param annotationClass
	 * @return
	 * @throws Exception
	 */
	public static boolean isAnnotation(Class clazz,String fdName,Class annotationClass){
		
		if(clazz==null||fdName==null||annotationClass==null)
			return false;
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(fdName, clazz);
		} catch (IntrospectionException e) {
			return false;
		}
		Method getMethod = pd.getReadMethod();// 获得get方法
		return getMethod.isAnnotationPresent(annotationClass);
		
	}
	
	/**
	 * 获取 Class 的@Table注解 name 属性,没有属性则返回 类名
	 * @param clazz
	 * @return
	 * @throws Exception 
	 */
	public static String  getTableName(Object object){
		
		if(object==null)
			return null;
	   String tableName=null;
	   
		if(object instanceof Class){
			EntityInfo entityInfo = getEntityInfoByClass((Class)object);
			tableName=entityInfo.getTableName();
		}else{
			EntityInfo entityInfoByEntity = ClassUtils
					.getEntityInfoByEntity(object);
			 tableName = entityInfoByEntity.getTableName();
		}

		if(tableName==null){
			return object.getClass().getSimpleName();
		}
		
			return tableName;
		
	}
	
	/**
	 * 根据对象获取Entity信息,主要是为了获取分表的信息
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static EntityInfo getEntityInfoByEntity(Object o){
		if(o==null)
			return null;
		Class clazz=o.getClass();
		EntityInfo info=getEntityInfoByClass(clazz);
		if(info==null){
			return null;
		}
		return info;
	}
	
	
	/**
	 * 获取 Class 的@Table注解 name 属性,没有属性则返回 类名
	 * @param clazz
	 * @return
	 */
	public static String  getTableNameByClass(Class clazz){
		
		if(clazz==null)
			return null;
		
		 if((clazz.isAnnotationPresent(Table.class)==false))
			 return clazz.getSimpleName();
		 
		Table table= (Table) clazz.getAnnotation(Table.class);
		
		String tableName=table.name();
		if(tableName==null)
			return clazz.getSimpleName();
			return tableName;
		
	}
	
	/**
	 * 递归查询父类的所有属性,set 去掉重复的属性
	 * @param clazz
	 * @param fdNameSet
	 * @return
	 * @throws Exception
	 */
	private static  Set<String> recursionFiled(Class clazz,Set<String> fdNameSet){
		Field[] fds = clazz.getDeclaredFields();
		for (int i = 0; i < fds.length; i++) {
			Field fd = fds[i];
			fdNameSet.add(fd.getName());
		}
		Class superClass = clazz.getSuperclass();
		if (superClass != Object.class) {
			recursionFiled(superClass,fdNameSet);
		}
		return fdNameSet;
	}
	
	
	/**
	 * 获得主键的值
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static Object getPKValue(Object o) throws Exception{
		Class clazz=o.getClass();
	     String id=getEntityInfoByClass(clazz).getPkName();
		return getPropertieValue(id,o) ;
			
	}
	
	/**
	 * 获取一个实体类的属性值
	 * @param p
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static Object getPropertieValue(String p,Object o) throws Exception{
		Object _obj=null;
		for(Class<?> clazz = o.getClass(); clazz != Object.class;  clazz = clazz.getSuperclass()) {
			try{
			 PropertyDescriptor pd = new PropertyDescriptor(p, clazz);
				Method getMethod = pd.getReadMethod();// 获得get方法
				if(getMethod!=null){
					_obj= getMethod.invoke(o);
					break;
				}
			}catch(Exception e){
				return null;
			}
			
		}
		
		return _obj;
		
	}
	/**
	 * 设置实体类的属性值
	 * @param p
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static Object setPropertieValue(String p,Object o,Object value) throws Exception{
		Object _obj=null;
		for(Class<?> clazz = o.getClass(); clazz != Object.class;  clazz = clazz.getSuperclass()) {
			 PropertyDescriptor pd = new PropertyDescriptor(p, clazz);
				Method setMethod = pd.getWriteMethod();// 获得set方法
				if(setMethod!=null){
					setMethod.invoke(o, value);  
					break;
				}
			
		}
		
		return _obj;
		
	}
	
	/**
	 * 获取字段的返回类型
	 * @param p
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static Class getReturnType(String p,Class _clazz){
		
		Class  returnType=null;
		for(Class<?> clazz = _clazz; clazz != Object.class;  clazz = clazz.getSuperclass()) {
			 PropertyDescriptor pd;
			try {
				pd = new PropertyDescriptor(p, clazz);
			} catch (IntrospectionException e) {
				return null;
			}
				Method getMethod = pd.getReadMethod();// 获得get方法
				if(getMethod!=null){
					returnType= getMethod.getReturnType();
					break;
				}
			
		}
		
		return returnType;
	}
	
	/**
	 * 是否是java的基本类型
	 * @param clazz
	 * @return
	 */
	public static  boolean isBaseType(Class clazz){
		if(clazz==null){
			return false;
		}
		String className=clazz.getName().toLowerCase();
		if(className.startsWith("java.")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 对象属性转换为字段  例如：userName to user_name
	 * @param property 字段名
	 * @return
	 */
	public static String propertyToField(String property) {
		if (null == property) {
			return "";
		}
		char[] chars = property.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char c : chars) {
			if (CharUtils.isAsciiAlphaUpper(c)) {
				sb.append("_" + StringUtils.lowerCase(CharUtils.toString(c)));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 字段转换成对象属性 例如：user_name to userName
	 * @param field
	 * @return
	 */
	public static String fieldToProperty(String field) {
		if (null == field) {
			return "";
		}
		char[] chars = field.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					sb.append(StringUtils.upperCase(CharUtils.toString(chars[j])));
					i++;
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
