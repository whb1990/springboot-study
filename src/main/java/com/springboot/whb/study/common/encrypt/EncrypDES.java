package com.springboot.whb.study.common.encrypt;

import com.springboot.whb.study.common.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author: whb
 * @date: 2018/7/12 11:33
 * @description: DESC加密/解密
 */
public class EncrypDES {
    /**
     * 使用KeyGenerator生成key
     *
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Key newKeyByKeyGenerator(String algorithm) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        Key key = kg.generateKey();
        return key;
    }

    /**
     * 使用SecretKeySpec生成key
     * 一般是从一个文件中读取出key的byte数组，然后根据文件key的算法，构建出key对象
     */
    public static Key newKeyBySecretKeySpec(byte[] key, String algorithm) throws NoSuchAlgorithmException {
        return new SecretKeySpec(key, algorithm);
    }

    /**
     * 加密，对字符串进行加密，返回结果为byte数组
     * 保存的时候，可以把byte数组进行base64编码成字符串，或者把byte数组转换成16进制的字符串
     * <p>
     * 其中，transformation支持的全部算法见官方文档：
     * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
     */
    public static byte[] encrypt(String transformation, Key key, String password) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        //加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(password.getBytes());
    }

    /**
     * 解密，返回结果为原始字符串
     * <p>
     * 其中，transformation支持的全部算法见官方文档：
     * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
     */
    public static String decrypt(String transformation, Key key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        //解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(data);
        String password = new String(result);
        return password;
    }

    public static void main(String[] args) throws Exception {
        String password = "SpringBoot学习";

        String algorithm = "DES";
        String transformation = algorithm;

        //加密解密使用的都是同一个秘钥key
        Key key = newKeyByKeyGenerator(algorithm);
        System.out.println(" 秘钥: " + key);
        //加密
        byte[] passData = encrypt(transformation, key, password);
        System.out.println("加密后的秘钥 ：" + StringUtils.toHexString(passData));
        //解密
        String pass = decrypt(transformation, key, passData);

        System.out.println("解密后的密码 : " + pass);
    }
}
