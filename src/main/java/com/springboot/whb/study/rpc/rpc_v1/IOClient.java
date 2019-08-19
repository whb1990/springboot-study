package com.springboot.whb.study.rpc.rpc_v1;

import java.io.*;
import java.net.Socket;

/**
 * @author: whb
 * @date: 2019/8/15 14:46
 * @description: 客户端-网络处理
 * 代理对象被调用后生成一个MethodParameter对象，通过此IOClient把数据传输到服务端，并且返回对应的数据。
 */
public class IOClient {

    private String ip;
    private int port;

    public IOClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Object invoke(MethodParameter methodParameter) {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(outputStream);
            output.writeUTF(methodParameter.getClassName());
            output.writeUTF(methodParameter.getMethodName());
            output.writeObject(methodParameter.getParameterTypes());
            output.writeObject(methodParameter.getArguments());
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream input = new ObjectInputStream(inputStream);
            return input.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException("socket关闭失败");
                }
            }
        }
        return null;
    }
}
