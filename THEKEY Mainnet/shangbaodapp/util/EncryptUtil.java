package com.xczg.blockchain.yibaodapp.util;

import java.security.MessageDigest;

public class EncryptUtil {
    public static String encode(String text, String algorithm) {
    	if ( text == null ) return "";
        byte[] unencodedText = text.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            return text;
        }
        md.reset();
        md.update(unencodedText);
        byte[] cryptograph = md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < cryptograph.length; i++) {
            if (((int) cryptograph[i] & 0xff) < 0x10) {
                buf.append('0');
            }
            buf.append(Long.toString((int) cryptograph[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     * Encode a string using Base64 encoding. Used when storing passwords
     * as cookies.
     * @param str
     * @return String
     */
    public static String encodeString(String str)  {
    	
    	org.apache.commons.codec.binary.Base64 encoder = new org.apache.commons.codec.binary.Base64();
        //sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encodeToString(str.getBytes());  //encodeBuffer(str.getBytes()).trim();
    }

    /**
     * Decode a string using Base64 encoding.
     * @param str
     * @return String
     */
    public static String decodeString(String str) {
        //sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
    	org.apache.commons.codec.binary.Base64 dec = new org.apache.commons.codec.binary.Base64();
        try {
            return new String(dec.decode(str)); //.decodeBuffer(str));
        } catch (Exception io) {
        	throw new RuntimeException(io.getMessage(), io.getCause());
        }
    }
    
    public static String md5(String str){
    	return encode(str,"MD5");
    }
    
    public static void main(String[] args){
    	System.out.println(encode("A123456zz","MD5"));
    }
}

