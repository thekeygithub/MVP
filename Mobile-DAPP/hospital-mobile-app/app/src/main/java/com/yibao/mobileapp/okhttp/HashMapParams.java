package com.yibao.mobileapp.okhttp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * 公共参数
 * 
 * @author WY01
 * 
 */
public class HashMapParams extends HashMap<String, String> implements Serializable {

	public HashMapParams() {
		SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=formatTime.format(System.currentTimeMillis());

		put("dateTime",time);

	}
}
