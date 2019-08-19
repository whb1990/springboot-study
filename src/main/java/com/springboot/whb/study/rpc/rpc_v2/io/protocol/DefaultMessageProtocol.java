package com.springboot.whb.study.rpc.rpc_v2.io.protocol;

import com.springboot.whb.study.rpc.rpc_v2.core.RpcRequest;
import com.springboot.whb.study.rpc.rpc_v2.core.RpcResponse;
import com.springboot.whb.study.rpc.rpc_v2.serialize.HessianSerialize;
import com.springboot.whb.study.rpc.rpc_v2.serialize.SerializeProtocol;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author: whb
 * @date: 2019/8/19 9:58
 * @description: 套接字的io流和服务端、客户端的数据传输
 */
@Slf4j
public class DefaultMessageProtocol implements MessageProtocol {

    /**
     * 序列化协议
     */
    private SerializeProtocol serializeProtocol;

    public DefaultMessageProtocol() {
        this.serializeProtocol = new HessianSerialize();
        //this.serializeProtocol = new JavaInnerSerialize();
    }

    public void setSerializeProtocol(SerializeProtocol serializeProtocol) {
        // 可替换序列化协议
        this.serializeProtocol = serializeProtocol;
    }

    /**
     * 服务端解析从网络传输的数据，转变成request对象
     *
     * @param inputStream
     * @return
     */
    @Override
    public RpcRequest serviceToRequest(InputStream inputStream) {
        try {
            // 2、bytes -> request 反序列化
            byte[] bytes = readBytes(inputStream);
            System.out.println("[2]服务端反序列化出obj:[" + new String(bytes, "utf-8") + "], length:" + bytes.length);
            //System.out.println("[2]服务端反序列化出obj length:" + bytes.length);
            RpcRequest request = serializeProtocol.deserialize(RpcRequest.class, bytes);
            return request;
        } catch (Exception e) {
            log.error("[2]服务端反序列化从网络传输的数据转变成request对象失败，error:{}", e);
        }
        return null;
    }

    /**
     * 服务端把计算的结果包装好，通过输出流返回给客户端
     *
     * @param response
     * @param outputStream
     * @param <T>
     */
    @Override
    public <T> void serviceGetResponse(RpcResponse<T> response, OutputStream outputStream) {
        try {
            // 3、把response 序列化成bytes 传给客户端
            byte[] bytes = serializeProtocol.serialize(RpcResponse.class, response);
            System.out.println("[3]服务端序列化出bytes:[" + new String(bytes, "utf-8") + "], length:" + bytes.length);
            //System.out.println("[3]服务端序列化出bytes length:" + bytes.length);
            outputStream.write(bytes);
        } catch (Exception e) {
            log.error("[3]服务端序列化计算的结果出输给客户端失败，error:{}", e);
        }
    }

    /**
     * 客户端把请求拼接好，通过输出流发送到服务端
     *
     * @param request
     * @param outputStream
     */
    @Override
    public void clientToRequest(RpcRequest request, OutputStream outputStream) {
        try {
            // 1、先把这个request -> bytes 序列化掉
            byte[] bytes = serializeProtocol.serialize(RpcRequest.class, request);
            System.out.println("[1]客户端序列化出bytes:[" + new String(bytes, "utf-8") + "], length:" + bytes.length);
            //System.out.println("[1]客户端序列化出bytes length:" + bytes.length);
            outputStream.write(bytes);
        } catch (IOException e) {
            log.error("[1]客户端序列化请求参数失败，error:{}", e);
        }
    }

    /**
     * 客户端接收到服务端响应的结果，转变成response对象
     *
     * @param inputStream
     */
    @Override
    public <T> RpcResponse<T> clientGetResponse(InputStream inputStream) {
        try {
            // 4、bytes 反序列化成response
            byte[] bytes = readBytes(inputStream);
            System.out.println("[4]客户端反序列化出bytes:[" + new String(bytes, "utf-8") + "], length:" + bytes.length);
            //System.out.println("[4]客户端反序列化出bytes length:" + bytes.length);
            RpcResponse response = serializeProtocol.deserialize(RpcResponse.class, bytes);

            return response;
        } catch (Exception e) {
            log.error("[4]客户端反序列化计算结果失败，error:{}", e);
        }
        return null;
    }

    /**
     * 流转二进制数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] readBytes(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new RuntimeException("输入流为空");
        }
        return inputStreamToByteArr2(inputStream);
    }

    /**
     * 流转二进制数组方法1
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] inputStreamToByteArr1(InputStream inputStream) throws IOException {
        // 有个前提是数据最大是1024，并没有迭代读取数据
        byte[] bytes = new byte[1024];
        int count = inputStream.read(bytes, 0, 1024);
        return Arrays.copyOf(bytes, count);
    }

    /**
     * 流转二进制数组方法2
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] inputStreamToByteArr2(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufesize = 1024;
        while (true) {
            byte[] data = new byte[bufesize];
            int count = inputStream.read(data, 0, bufesize);
            byteArrayOutputStream.write(data, 0, count);
            if (count < bufesize) {
                break;
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 流转二进制数组方法3，调用该方法之后会阻塞在read，可通过jstack查看相关信息
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] inputStreamToByteArr3(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufesize = 1024;

        byte[] buff = new byte[bufesize];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, bufesize)) > 0) {
            byteArrayOutputStream.write(buff, 0, rc);
            buff = new byte[bufesize];
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

}
