package com.xczg.blockchain.yibaodapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	/**
	 * 获得当前时间，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public synchronized static String getNow() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * 获得当前时间，格式为yyyy-MM-dd HH:mm:ss
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

}
