package com.unicom.rsa;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class UseRSAUtil {

	 /** *//** 
     * RSA最大加密明文大小 
     */  
    private static final int MAX_ENCRYPT_BLOCK = 117;  
      
    /** *//** 
     * RSA最大解密密文大小 
     */  
    private static final int MAX_DECRYPT_BLOCK = 256; 

    public static final String KEY_ALGORITHM = "RSA";
    private static KeyFactory keyFactory = null;
    
    public static final String PrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCeWZmeGHpdjx4lBvWM1AG4J3r15rwPsk0Ylj9sYl6ln88a2e5JmhSwxh6uNHF1DUWZDs0pJQkW9gA7PSTbCoS+1PHG6Mdgh81Sk3D8X7w0QYahu527MXBFV3epH2fYFq3sxEKL/sQbqIn8+7NNAdW5lzegC3lLFoEHssu6ts+DcC1Wv7YYpMV7sQlhGMBlL787XXxWd+leACqc2OmxlIGbpe+gx/qsYPeMbpAAk721NsB+O9w5iGOG1x//IgnEVWryt0z99czxEoYXeYD2WfTgf4exc+uo+bm3trGFZAqJLlwbBugtQw85SMac8vlMnUbgrxbDE7iK+i8/QNriwk4JAgMBAAECggEAG9bg3RyslrpMsHHSxdmYG8pFIepY8eGFEGeJ6wI4ZaH9BVl/PU1ridCrIDF0KQIWVFx30V4DhRfm3oNXcXMd93b0suXF98CoZnYpjRU/v2gtxU1446pDdM116jPtS95g4Zl3oak+zSYKbIHL+iPycViWnPqnbn4cXUAf9UminhbX+O8kAgNyvZUfDKEzZOiI3zyieMFzVbkwt1myXZ5YfaB5CpIVH3AzDz9vggSvvFx9o6TRU5jIc0LphupVwO6XMZDuKXwzk9/NTC5WVNnqOUvvO88g2dADL83El9qeqJA32Xx2i3bQ4TgTGGhs5frS8R/AqDKuZ8P1Fvx1ZN/kAQKBgQD/DYsLO/WYrr8oc62rkJ5G7grcMJlk806u0qWfzHIl69t4sDNPm3leLeEZ+jUfn2QJ84hxZ4IljoOJjsks8UjasTE5MXygXF6ASQdJ0ff8AOPMje/H+UmgHwMMZ8GfFEBCOlD7WDaminG93ZZeWbUFJflLdikWZ2G8sbbNQY9LyQKBgQCe8CE6RzYRcvap65dAPhtoCxQcDRNC+tWZ0Srw2+uFUxMPM4WcaZ2Ocz/E0sY0evTAZ10OHb38paTYTER+5sV2QcmgkKdGnn46a35hMoITdtKv/aXlivbG0KBZwmxfkNx+thbAh8mGJMxcxEKrbQaKyNNZdGQcJwJSOfa8rkeQQQKBgCDdoOYwssQa+H0A8wchmpOvRuz6wjl09NWoRTljQe5LBzv1EJeYhDozlUcgq8nIYz65CAL9AJNQWvjV0mydJvMvVBV3oASWAoNrZaoIiXkKsFACljxhjjA47s6hbBzQojsmgprw0hIB0whRTngX/TCNpSMpNcAyUDDdgZ3PbeSpAoGAOrws21eToJEmnJbGl2QcRq/JZ7BhTOBbbXOB9e6fs+6GXZjBqJLTOeBXUYo+2wgRSMUp+1x8aFBigdh1e5wWnuQi+eZbGtrZdQdiddLvhPflVkpbM1/OYJyalXEnEY38Hgt2jwHfNGaG60VQCf/JaiJbEHTVxh2YWwC2sPWK1sECgYAGkQ9XU69SWEXFBn1XK59ShZ6rHsV5q3IrKolevrBWlKuGw2qPl9B/K9NYn+fOAE9wjZtpON92919VBWTTWZiqdsRipDW7b8iVEREow++Aa4lKAGk1nyaQeLO5fxDnTA6IIedL9+tIWFR/17X3SXmoCP+Pw3A9mV5XSUAZt0kGOw==";
	public static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnlmZnhh6XY8eJQb1jNQBuCd69ea8D7JNGJY/bGJepZ/PGtnuSZoUsMYerjRxdQ1FmQ7NKSUJFvYAOz0k2wqEvtTxxujHYIfNUpNw/F+8NEGGobuduzFwRVd3qR9n2Bat7MRCi/7EG6iJ/PuzTQHVuZc3oAt5SxaBB7LLurbPg3AtVr+2GKTFe7EJYRjAZS+/O118VnfpXgAqnNjpsZSBm6XvoMf6rGD3jG6QAJO9tTbAfjvcOYhjhtcf/yIJxFVq8rdM/fXM8RKGF3mA9ln04H+HsXPrqPm5t7axhWQKiS5cGwboLUMPOUjGnPL5TJ1G4K8WwxO4ivovP0Da4sJOCQIDAQAB";

    static {
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 解密方法
     * @param dataStr 要解密的数据
     * @return 解密后的原数据
     * @throws Exception
     */
    public static String decrypt(String dataStr) throws Exception{
        //要加密的数据
        System.out.println("要解密的数据:"+dataStr);
        //对私钥解密
        Key decodePrivateKey = getPrivateKeyFromBase64KeyEncodeStr(PrivateKey);
        //Log.i("机密",""+decodePrivateKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, decodePrivateKey);
        byte[] encodedData = Base64.decodeBase64(dataStr);
        byte[] decodedData = cipher.doFinal(encodedData);
        String decodedDataStr = new String(decodedData,"utf-8");
        System.out.println("私钥解密后的数据:"+decodedDataStr);
        return decodedDataStr;
    }

    public  static Key getPrivateKeyFromBase64KeyEncodeStr(String keyStr) {
        byte[] keyBytes = Base64.decodeBase64(keyStr);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        Key privateKey=null;
        try {
            privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 获取base64加密后的字符串的原始公钥
     * @param keyStr
     * @return
     */
    public static Key getPublicKeyFromBase64KeyEncodeStr(String keyStr) {
        byte[] keyBytes = Base64.decodeBase64(keyStr);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        Key publicKey = null;
        try {
            publicKey = keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 公钥加密方法
     * @param dataStr 要加密的数据
     * @param dataStr 公钥base64字符串
     * @return 加密后的base64字符串
     * @throws Exception
     */
    public static String encryptPublicKey(String dataStr) throws Exception{
        //要加密的数据
        System.out.println("要加密的数据:"+dataStr);
        byte[] data = dataStr.getBytes();
        // 对公钥解密
        Key decodePublicKey = getPublicKeyFromBase64KeyEncodeStr(publicKey);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, decodePublicKey);
        byte[] encodedData = cipher.doFinal(data);
        String encodedDataStr = new String(Base64.decodeBase64(encodedData));
        System.out.println("公钥加密后的数据:"+encodedDataStr);
        return encodedDataStr;
    }
    
    /**
     * 使用公钥进行分段加密
     * @param dataStr 要加密的数据
     * @return 公钥base64字符串
     * @throws Exception
     */
    public static String encryptByPublicKey(String dataStr)  
            throws Exception {  
        //要加密的数据
        System.out.println("要加密的数据:"+dataStr);
        byte[] data = dataStr.getBytes();
        // 对公钥解密
        Key decodePublicKey = getPublicKeyFromBase64KeyEncodeStr(publicKey);
         
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, decodePublicKey);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close(); 
        String encodedDataStr = new String(Base64.encodeBase64(encryptedData));
        System.out.println("公钥加密后的数据:"+encodedDataStr);
        return encodedDataStr;  
    } 

    /**
     * 使用私钥进行分段解密
     * @param dataStr 使用base64处理过的密文
     * @return 解密后的数据
     * @throws Exception
     */
    public static String decryptByPrivateKey(String dataStr)  
            throws Exception {  
        
        byte[] encryptedData = Base64.decodeBase64(dataStr);
        
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key decodePrivateKey = getPrivateKeyFromBase64KeyEncodeStr(PrivateKey);
        
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, decodePrivateKey);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段解密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        String decodedDataStr = new String(decryptedData,"utf-8");
        System.out.println("私钥解密后的数据:"+decodedDataStr);
        return decodedDataStr;  
    } 
    
}
