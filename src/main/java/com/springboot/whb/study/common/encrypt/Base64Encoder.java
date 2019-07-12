package com.springboot.whb.study.common.encrypt;

import org.apache.commons.codec.binary.Base64;

/**
 * @author: whb
 * @date: 2018/7/12 10:56
 * @description: 基于Base64加密/解密
 */
public class Base64Encoder {
    /**
     * 字符编码
     */
    public final static String ENCODING = "UTF-8";

    /**
     * Base64编码
     *
     * @param data 待编码数据
     * @return String 编码数据
     * @throws Exception
     */
    public static String encode(String data) throws Exception {

        // 执行编码
        byte[] b = Base64.encodeBase64(data.getBytes(ENCODING));

        return new String(b, ENCODING);
    }

    /**
     * Base64安全编码<br>
     * 遵循RFC 2045实现
     *
     * @param data 待编码数据
     * @return String 编码数据
     * @throws Exception
     */
    public static String encodeSafe(String data) throws Exception {

        // 执行编码
        byte[] b = Base64.encodeBase64(data.getBytes(ENCODING), true);

        return new String(b, ENCODING);
    }

    /**
     * Base64解码
     *
     * @param data 待解码数据
     * @return String 解码数据
     * @throws Exception
     */
    public static String decode(String data) throws Exception {

        // 执行解码
        byte[] b = Base64.decodeBase64(data.getBytes(ENCODING));

        return new String(b, ENCODING);
    }

    public static void main(String[] args) throws Exception {
        String str = "SpringBoot学习";
        String encrypStr = encode(str);
        System.out.println("加密后的字符串：" + encrypStr);
        System.out.println("解密后的字符串：" + decode(encrypStr));
    }
}
