package com.xczg.blockchain.yibaodapp.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSAUtil {
	
	private static Logger log = LoggerFactory.getLogger(RSAUtil.class);

	// 非对称加密密钥算法
	public static final String KEY_ALGORITHM = "RSA";
	// 数字签名 签名/验证算法
	public static final String SIGNATURE_ALGORRITHM = "SHA1withRSA";
	// 公钥
	private static final String PUBLIC_KEY = "RSAPublicKey";
	// 私钥
	private static final String PRIVATE_KEY = "RSAPrivateKey";
	// RSA密钥长度,默认为1024,密钥长度必须是64的倍数,范围在521~65526位之间
	private static final int KEY_SIZE = 1024;

	/**
	 * 私钥解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            私钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, byte[] key)
			throws Exception {
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成私钥
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私钥解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            私钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String privateKey)
			throws Exception {
		return decryptByPrivateKey(data, getKey(privateKey));
	}

	/**
	 * 公钥解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            公钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, byte[] key)
			throws Exception {
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成公钥
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            公钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String publicKey)
			throws Exception {
		return decryptByPublicKey(data, getKey(publicKey));
	}

	

	/**
	 * 公钥加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            公钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey)
			throws Exception {
		return encryptByPublicKey(data, getKey(publicKey));
	}

	/**
	 * 私钥加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            私钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, byte[] key)
			throws Exception {
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成私钥
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私钥加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            私钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key)
			throws Exception {
		return encryptByPrivateKey(data, getKey(key));
	}

	/**
	 * 取得私钥
	 *
	 * @param keyMap
	 *            密钥Map
	 * @return byte[] 私钥
	 * @throws Exception
	 */
	public static byte[] getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	/**
	 * 取得公钥
	 *
	 * @param keyMap
	 *            密钥Map
	 * @return byte[] 公钥
	 * @throws Exception
	 */
	public static byte[] getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}

	/**
	 * 初始化密钥
	 *
	 * @return 密钥Map
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
		// 实例化实钥对生成器
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		// 初始化密钥对生成器
		keyPairGen.initialize(KEY_SIZE);
		// 生成密钥对
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 封装密钥
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * 签名
	 *
	 * @param data
	 *            待签名数据
	 * @param privateKey
	 *            私钥
	 * @return byte[] 数字签名
	 * @throws Exception
	 */
	public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
		// 转接私钥材料
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取私钥对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 实例化Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORRITHM);
		// 初始化Signature
		signature.initSign(priKey);
		// 更新
		signature.update(data);
		// 签名
		return signature.sign();
	}

	/**
	 * 公钥校验
	 *
	 * @param data
	 *            待校验数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign)
			throws Exception {
		// 转接公钥材料
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成公钥
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// 实例化Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORRITHM);
		// 初始化Signature
		signature.initVerify(pubKey);
		// 更新
		signature.update(data);
		// 验证
		return signature.verify(sign);
	}

	/**
	 * 私钥签名
	 *
	 * @param data
	 *            待签名数据
	 * @param privateKey
	 *            私钥
	 * @return String 十六进制签名字符串
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] sign = sign(data, getKey(privateKey));
		return Hex.encodeHexString(sign);
	}

	/**
	 * 公钥校验
	 *
	 * @param data
	 *            待验证数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            签名
	 * @return boolean 成功返回true,失败返回false
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)
			throws Exception {
		return verify(data, getKey(publicKey),
				Hex.decodeHex(sign.toCharArray()));
	}

	/**
	 * 取得私钥十六进制表示形式
	 *
	 * @param keyMap
	 *            密钥Map
	 * @return String 私钥十六进制字符串
	 * @throws Exception
	 */
	public static String getPrivateKeyString(Map<String, Object> keyMap)
			throws Exception {
		return Hex.encodeHexString(getPrivateKey(keyMap));
	}

	/**
	 * 取得公钥十六进制表示形式
	 *
	 * @param keyMap
	 *            密钥Map
	 * @return String 公钥十六进制字符串
	 * @throws Exception
	 */
	public static String getPublicKeyString(Map<String, Object> keyMap)
			throws Exception {
		return Hex.encodeHexString(getPublicKey(keyMap));
	}

	/**
	 * 获取密钥
	 *
	 * @param key
	 *            密钥
	 * @return byte[] 密钥
	 * @throws Exception
	 */
	public static byte[] getKey(String key) throws Exception {
		return Hex.decodeHex(key.toCharArray());
	}

	/**
	 * 公钥加密
	 * 
	 * @param publicKey
	 * @param data
	 * @return
	 */
	public static String encryption(String publicKey, String inputStr) {
		 byte[] data = inputStr.getBytes();
		 byte[] enCodeData;
		try {
			enCodeData = encryptByPublicKey(data, publicKey);
		} catch (Exception e) {
			log.error("公钥加密 error "+e.getMessage(),e);
			return null;
		}
		 return Base64.encodeBase64String(enCodeData);
	}
	
	 /**
     * 公钥加密
     *
     * @param data
     *            待加密数据
     * @param key
     *            公钥
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

	/**
	 * 私钥解密
	 * 
	 * @param publicKey
	 * @param data
	 * @return
	 */
	public static String decryption(String privateKey, String data) {
		byte[] deCodeData;
		try {
			deCodeData = decryptByPrivateKey(Base64.decodeBase64(data), privateKey);
		} catch (Exception e) {
			log.error("私钥解密 error "+e.getMessage(),e);
			return null;
		}
		return new String(deCodeData);
	}
	
	public static void main(String[] args) {
		try {
			/*String a = RSAUtil
					.encryption(
							"30819f300d06092a864886f70d010101050003818d003081890281810088e8dd01e3a30fe51deeb867fc1e921de7e38955cf85351be6c35ebd92ec87e78d504032d6204aed60a7de799dba54657c8ce5385901a3ec6de8532b49450feb42a509a04869f2bb51542cfa6f42d7abf36151e5e60101e83d5e9b7d3fd71da4cd78a0de11442b3f1b68c49b488fc93f805dcd6ed1400369b50066dbbc057b830203010001", "{\"newHash\":\"QmRbP5h3AQS34P3xNmP5tG8KHd7JpBpSvBrUAZH14wuc2k\",\"resTotal\":\"0002000000200\"}");
		System.out.println(a);*/
			
//			String publicKey="30819f300d06092a864886f70d010101050003818d00308189028181008cfbca2b2089c8ceb76d61c8b34c58d98e8b2a13f94c2fb41e6fdfca546a96f69937c055ee56bbf179147456b08d0db01cce9bd7aa106d230478c3625b4173f3fedbf1fa8f74a582cb9a5a5a534915eb464bd29914fdb25125cf0e6645c084db00b7856205bff2688bc0e61173dcd4b2c07ad898f39bc0d6f24df09b33645eb70203010001";
	String privateKey="30820276020100300d06092a864886f70d0101010500048202603082025c020100028181008cfbca2b2089c8ceb76d61c8b34c58d98e8b2a13f94c2fb41e6fdfca546a96f69937c055ee56bbf179147456b08d0db01cce9bd7aa106d230478c3625b4173f3fedbf1fa8f74a582cb9a5a5a534915eb464bd29914fdb25125cf0e6645c084db00b7856205bff2688bc0e61173dcd4b2c07ad898f39bc0d6f24df09b33645eb7020301000102818005c5001f07b3dbcde9dfc612aa93c34713f849fe8b433ce8881e57f035c82ee96497cd7d36c5c1e73de7426391d213ef531e796e37fd7819e8c2f06c040407b4b27920006f831f493f2e280bacd6762751bdbf9c95920a3818aba3b5b7f7fd665f2ba14a6290c325f62c1884fcff155ad445b83557dd5f04c53504666d070911024100f507f134ebf2302c0488609b72858b410df00eb61083afbfbd1622547cb7ee7b170f0066d51fbf572aba336fe1f63d3fc06f954f6a2efbff040eb07ab7fc3d85024100934b7668e815f31bbe895d07e8c4c889c6dc4de09d238d266b3ab30d0b1d12ffd59d0574ffd60fae8e74976b665e40fa938f216566cda9b8cac27018b5aef20b024100b37e45eb868bbb4e036403a0db9b880ef7aeca980430f32579d624ffcefccdedcd4e67f4ec5a7d47d53ce1412c3dbd505e1f5f38f587c58161ef80c7e4cf1a2102404bba3a708911efe14dbe95a3e15a7ab7cb13acbe544bd8dfedb54a24e497d812cc226491d88e9795690c2c653d462c262803286a57c65901f6ccac9127a7802d024022a484b903061c3d8189f33f590997686c755ab83c3e134ab808a98f66911438069b9a4961164b76ef04dbc5daa8eb60025aa4e59ffbf8e7de723df2cf2da3c2";
//			String inputStr="QmUVVNoqXPbbCFuBqnVfKWFeB9RzPJEJXNgZLKR9auMh6H";
//			String a=RSAUtil.encryption(publicKey, inputStr);
//			System.out.println(a);
			String b=RSAUtil.decryption(privateKey,"dTNCYFJ+LciMz6fvLftpMdUCCu+ac+sqJKsnZ1H6UAKVLGbcj9wPslDkC5G8qMKZeZ3Mvo4tg2NjRCIyXjKXluhw250zO74eaUKisAEU9YhY9Y7ONdriBY0jUDckJGofM13dv2YnAVm+cgK609b++YmbSWz6hdudH7KNRQLukEs=");
			System.out.println(b);
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
