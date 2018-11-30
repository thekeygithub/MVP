package com.xczg.blockchain.yibaodapp.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

public class KeyGenerater {
	private byte[] priKey;
	private byte[] pubKey;

	public void generater() {
		try {
			java.security.KeyPairGenerator keygen = java.security.KeyPairGenerator.getInstance("RSA");
			SecureRandom secrand = new SecureRandom();
			secrand.setSeed("today".getBytes()); // 初始化随机产生器
			keygen.initialize(1024, secrand);
			KeyPair keys = keygen.genKeyPair();

			PublicKey pubkey = keys.getPublic();
			PrivateKey prikey = keys.getPrivate();

			pubKey = Base64.encodeBase64(pubkey.getEncoded());
			priKey = Base64.encodeBase64(prikey.getEncoded());

			System.out.println("pubKey = " + new String(pubKey));
			System.out.println("priKey = " + new String(priKey));
		} catch (java.lang.Exception e) {
			System.out.println("生成密钥对失败");
			e.printStackTrace();
		}
	}

	public byte[] getPriKey() {
		return priKey;
	}

	public byte[] getPubKey() {
		return pubKey;
	}

	/**
	 * 
	 * Description:数字签名
	 * 
	 * @param priKeyText
	 * @param plainText
	 * @return
	 */
	public static byte[] sign(byte[] priKeyText, String plainText) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(priKeyText);
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey prikey = keyf.generatePrivate(priPKCS8);

			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");
			signet.initSign(prikey);
			signet.update(plainText.getBytes());
			byte[] signed = Base64.encodeBase64(signet.sign());
			return signed;
		} catch (java.lang.Exception e) {
			System.out.println("签名失败");
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 
	 * Description:校验数字签名,此方法不会抛出任务异常,成功返回true,失败返回false,要求全部参数不能为空
	 * 
	 * @param pubKeyText
	 *            公钥,base64编码
	 * @param plainText
	 *            明文
	 * @param signTest
	 *            数字签名的密文,base64编码
	 * @return 校验成功返回true 失败返回false
	 */
	public static boolean verify(byte[] pubKeyText, String plainText,byte[] signText) {
		try {
			// 解密由base64编码的公钥,并构造X509EncodedKeySpec对象
			java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(Base64.encodeBase64(pubKeyText));
			// RSA对称加密算法
			java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
			// 取公钥匙对象
			java.security.PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
			// 解密由base64编码的数字签名
			byte[] signed = Base64.encodeBase64(signText);
			java.security.Signature signatureChecker = java.security.Signature.getInstance("MD5withRSA");
			signatureChecker.initVerify(pubKey);
			signatureChecker.update(plainText.getBytes());
			// 验证签名是否正常
			if (signatureChecker.verify(signed))
				return true;
			else
				return false;
		} catch (Throwable e) {
			System.out.println("校验签名失败");
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		KeyGenerater kg=new KeyGenerater();
		kg.generater();
		String plainText="hi,Damon!";
		byte[] signByte=sign(kg.getPriKey(), plainText);
		
		if ( signByte != null ) {
			System.out.println(new String(signByte));
		} else {
			System.out.println("signByte is null");
		}
		//System.out.println("verify="+verify(kg.getPubKey(),plainText,signByte));
		
	}
	
}
