package com.xczg.blockchain.yibaodapp.util;

public class StringUtil {
	
	
	public static String render(String value, boolean pretty, String indent){
		if ( value == null ) return "";
		final StringBuilder lBuf = new StringBuilder();
		if(pretty) lBuf.append(indent);
		//value = value.replaceAll("	", "");
		for(int i = 0; i < value.length(); i++)
		{
			final char lChar = value.charAt(i);
			if(lChar == '\n') lBuf.append("\\n");
			else if(lChar == '\r') lBuf.append("\\r");
			else if(lChar == '\t') lBuf.append("\\t");
			else if(lChar == '\b') lBuf.append("\\b");
			else if(lChar == '\f') lBuf.append("\\f");
			//            else if(lChar == '/') lBuf.append("\\/");
			else if(lChar == '\"') lBuf.append("\\\"");
			else if(lChar == '\\') lBuf.append("\\\\");
			else lBuf.append(lChar);
		}
		return lBuf.toString();
	}

	/** 
	 * 字符串转化成为16进制字符串
	 * 
	 * @param s  
	 * @return
	 */
	public static String strTo16(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}
	
	/**
	 * check string isEmpty
	 * @param str
	 * @return
	 */
	public static final boolean isEmpty(String str){
		if ( str == null ) return true;
		return ( "".equals( str.trim() ) );
	}

	/**
	 * 16进制转换成为string类型字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "UTF-8");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
}
