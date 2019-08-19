package com.springboot.whb.study.rpc.rpc_v2.serialize;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author: whb
 * @date: 2019/8/19 10:46
 * @description: Hessian二进制序列化
 */
@Slf4j
public class HessianSerialize implements SerializeProtocol {

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
        Hessian2Output hessian2Output = new Hessian2Output(outputStream);
        try {
            //验证过，一定需要在flush之前关闭掉hessian2Output，否则获取的bytes字段信息为空
            hessian2Output.writeObject(t);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                hessian2Output.close();
            } catch (IOException e) {
                log.error("Hessian 二进制序列化，关闭流失败，error:{}", e);
            }
        }
        try {
            outputStream.flush();
            byte[] bytes = outputStream.toByteArray();
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("Hessian 二进制序列化，关闭输出流失败，error:{}", e);
            }
        }
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
        Hessian2Input hessian2Input = new Hessian2Input(inputStream);
        try {
            T t = (T) hessian2Input.readObject();
            return t;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                hessian2Input.close();
            } catch (IOException e) {
                log.error("Hessian 反序列化，流关闭失败，error:{}", e);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("Hessian 反序列化，输入流关闭失败，error:{}", e);
            }
        }
    }
}
