package com.springboot.whb.study.rpc.rpc_v2.serialize;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author: whb
 * @date: 2019/8/19 10:46
 * @description: Java序列化
 */
@Slf4j
public class JavaInnerSerialize implements SerializeProtocol {

    /**
     * 序列化
     *
     * @param clazz
     * @param t
     * @param <T>
     * @return
     */
    @Override
    public <T> byte[] serialize(Class<T> clazz, T t) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(t);
            objectOutputStream.flush();
            byte[] bytes = outputStream.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error("Java 序列化失败，error:{}", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("Java 序列化关闭二进制输出流失败,error:{}", e);
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    log.error("Java 序列化关闭对象流失败，error:{}", e);
                }
            }
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            T obj = (T) objectInputStream.readObject();
            return obj;
        } catch (Exception e) {
            log.error("Java 反序列化失败，error:{}", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Java 反序列化关闭二进制输入流失败，error:{}", e);
                }
            }
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    log.error("Java 反序列化关闭对象输入流失败，error:{}", e);
                }
            }
        }
        return null;
    }
}
