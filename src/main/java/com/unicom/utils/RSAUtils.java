package com.unicom.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.aspectj.weaver.ast.Var;

import sun.misc.BASE64Decoder;

public class RSAUtils {
	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCL0sokx0woT9ErZdPx3e8x1Gm+"
			+ "Cz6cGifK4iPpRBu5IzKa8nNore57ik3QQTVEms/hrxnzTosxpUKj/sPmdQ/cF5SE"
			+ "TKJEHHYJZN4LuOY2xN4UXuW69SldNlqof79pP6Qo4gVCqzmBqCifgdSQh64RnZT/" + "dFBxTV4DkSlpEkOuIwIDAQAB";
	public static final String PRIVATE_KEY = "MIICWwIBAAKBgQCL0sokx0woT9ErZdPx3e8x1Gm+Cz6cGifK4iPpRBu5IzKa8nNo"
			+ "re57ik3QQTVEms/hrxnzTosxpUKj/sPmdQ/cF5SETKJEHHYJZN4LuOY2xN4UXuW6"
			+ "9SldNlqof79pP6Qo4gVCqzmBqCifgdSQh64RnZT/dFBxTV4DkSlpEkOuIwIDAQAB"
			+ "AoGAIgT4pyzRBsMvbT5VQZ/HNs8rpdAukjnQPLimFsyPbOLMyTj8LhfuJ6Nlff9K"
			+ "P+rlcU+dIhRAzE9ygLGCZcFNni8F6T82uHFNAIxOJOHUCgooqD7MmoemUjc26B12"
			+ "YDoQ3KeOVxAlukvGaSi+f9+IVHSFml5MU5/E7rxcF/ahjmECQQDDrWo6aiiVxnCM"
			+ "jxasqo1udPVYrQ35fvtDoAlKalmGplCq3Fdt00nwec+AwZIal2GuyOvK5ISuENS5"
			+ "qhcNaHi3AkEAtu10icvWz52xZgpPGvInXW50ozPsbYNOqF3FeigqlKW2mcC2hMF6"
			+ "Hh91tDAIU3u0XyXqiqN8be3iSvuO8NwR9QJAN0HibKqL2XfXUqoZohrro5SdsDP2"
			+ "bU1hkUBBP2V28KJIx2c/vUDccW9EnLyNELtNF3mftIUBvtH78I5KwjuNUwJADFwG"
			+ "37iSDv0Mm2Re6+combt4zf4YH14b1mxfh11nYxu/Nqw6qMWZxqdBgKcuKNXW3gbl"
			+ "zfA13AeSnpZYlhWIcQJAIdSHbCPHRUPTtD9CTzyDsg/cLfZZ4oIMY1773Zioq5Os"
			+ "3KExauc5Hc5Ksbs3y9ndgejntIiiVAxwGFOjTQP8Gw==";

	public static PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = (new BASE64Decoder()).decodeBuffer(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = (new BASE64Decoder()).decodeBuffer(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	private static KeyPair initKey() {
		try {

			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			SecureRandom random = new SecureRandom();
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");

			generator.initialize(1024, random);
			return generator.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密
	 * 
	 * @param string
	 * @return
	 */
	public static String encryptBase64(String string) {
		return new String(Base64.encodeBase64(encrypt(string.getBytes())));
		//return new String(encrypt(Base64.decodeBase64(string)));
	}

	private static byte[] encrypt(byte[] string) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			RSAPublicKey pbk = (RSAPublicKey) getPublicKey(PUBLIC_KEY);

			cipher.init(Cipher.ENCRYPT_MODE, pbk);
			byte[] plainText = cipher.doFinal(string);
			return plainText;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密
	 * 
	 * @param string
	 * @return
	 */
	public static String decryptBase64(String string) {
		return new String(decrypt(Base64.decodeBase64(string)));
	}

	private static byte[] decrypt(byte[] string) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			RSAPrivateKey pbk = (RSAPrivateKey) getPrivateKey(PRIVATE_KEY);

			cipher.init(Cipher.DECRYPT_MODE, pbk);
			byte[] plainText = cipher.doFinal(string);
			return plainText;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
			
		}
	}

	public static void main(String[] args) {
		// 生成public key

		//加密
//		String string = "郝成阳";
//		String encStr=encryptBase64(string);
//		System.out.println(encStr);
		// 解密
		String dd=decryptBase64(
				"TFcWUnFwSmSnmH6vXu10BNDbQaEiZdQ2ca+v8P0EUcfK/4yec1z4LMfW4U0X0FTymYcpD/W/DFJcfrHvIaj9H/wqoZuPLc71s6NBDKxlyjiEH3MlfTnnKbfj4Bkd0ZzP92g51f0hlxYpHycblTs8q7m2pgk7+vkbgmEeWuPJLy4=");
		
		System.out.println(dd);
	}

}
