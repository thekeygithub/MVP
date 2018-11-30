package com.xczg.blockchain.yibaodapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

	private static final String dateFormatStr = "yyyy-MM-dd";
	
	/**
	 * 纯数字、带毫秒的日期格式串
	 */
	private static final String numMillFormatStr = "yyyyMMddHHmmssSSS";
	/**
	 * yyyy-MM-dd的日期格式化对象
	 */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
	
	/**
	 * yyyyMMddHHmmssSSS格式的日期格式化对象
	 */
	public static final SimpleDateFormat numMillFormat = new SimpleDateFormat(numMillFormatStr);

	/**
	 * 将日期字符串转换为时间戳
	 * @param dateStr
	 * @return
	 */
	public static long stringToTimestamp(String dateStr){
		if ( dateStr == null ) return 0;
		if ( "".equals(dateStr) ) return 0;
		SimpleDateFormat sdf=new SimpleDateFormat(dateFormatStr);
		try {
			Date d=sdf.parse(dateStr);
			return d.getTime();
		} catch (Exception e) {
			System.out.println("DateUtil error:"+ e.getMessage()+" "+dateStr);
			return 0;
		}
	}
	
	/**
	 * 获得当前时间，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return  
	 */
	public synchronized static String getNow() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	/**
	 * 将字符串日期(yyyy-mm-dd)转换为时间戳
	 * @param str
	 * @param isStart true表示返回一天的开始，false 表示返回一天的结束
	 * @return
	 */
	public static long toTimestamp(String str,boolean isStart){
		if ( !(str == null || "".equals(str)) ) {
	  	    try{
	  	    	Date date = DateUtil.getDate(Integer.parseInt(str.substring(0,4)), Integer.parseInt(str.substring(5,7)), 
		    			Integer.parseInt(str.substring(8,10)), isStart?0:23, isStart?0:59, isStart?0:59);
	  			return date.getTime();
	  	    }catch(Exception e){
	  	    }
	    }
		return -1;
	}
	
	/**
	 * 将字符串日期(yyyy-mm-dd hh:mm:ss)转换为时间戳
	 * @param str
	 * @return
	 */
	public static long toTimestamp(String str){
		if ( !(str == null || "".equals(str)) ) {
	  	    try{
	  	    	if ( str.length() == 10 ) str=str+" 00:00:00";
	  	    	Date date = DateUtil.getDate(Integer.parseInt(str.substring(0,4)), Integer.parseInt(str.substring(5,7)), Integer.parseInt(str.substring(8,10)), 
		    			Integer.parseInt(str.substring(11,13)), Integer.parseInt(str.substring(14,16)), Integer.parseInt(str.substring(17,19)));
	  			return date.getTime();
	  	    }catch(Exception e){
	  	    }
	    }
		return 0;
	}
	
	/**
	 * 北京时
	 * @return
	 */
	public static Calendar getCurrentBeijingCalendar(){
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone( TimeZone.getTimeZone("GMT+08:00") );
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	/**
	 * 返回日期对象
	 * @param d
	 * @param months
	 * @return
	 */
	public static Date getDate(int year, int month, int day){
		Calendar cal=getCurrentBeijingCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 返回日期对象
	 * @param d
	 * @param months
	 * @return
	 */
	public static Date getDate(int year, int month, int day, int hour){
		Calendar cal=getCurrentBeijingCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}
	/**
	 * 返回日期对象
	 * @param d
	 * @param months
	 * @return
	 */
	public static Date getDate(int year, int month, int day, int hour, int min, int sec){
		Calendar cal=getCurrentBeijingCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
  
	/**
	 * 获得当前时间，格式为yyyy-MM-dd
	 * 
	 * @return
	 */
	public synchronized static String getDay() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	public synchronized static String getDay2() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	public static Date addDay(Date d, int days) {
		Calendar cal = Calendar.getInstance(new Locale("zh-CN"));
		cal.setTime(d);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	public static String getBeforeDay() {
		Calendar cal = Calendar.getInstance(new Locale("zh-CN"));
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	/**
	 * 获得精确到毫秒的当前时间字符串。格式为：yyyyMMddHHmmssSSS
	 * @return
	 */
	public static String currentNumMill() {
		return numMillFormat.format(new Date());
	}

}
