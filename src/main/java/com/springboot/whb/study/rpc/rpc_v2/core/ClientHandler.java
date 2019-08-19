package com.springboot.whb.study.rpc.rpc_v2.core;

import com.springboot.whb.study.rpc.rpc_v2.io.protocol.MessageProtocol;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author: whb
 * @date: 2019/8/19 9:56
 * @description: 客户端处理器
 */
@Slf4j
public class ClientHandler {

    private RpcClient rpcClient;

    private MessageProtocol messageProtocol;

    public ClientHandler(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public void setMessageProtocol(MessageProtocol messageProtocol) {
        this.messageProtocol = messageProtocol;
    }

    public <T> RpcResponse<T> invoke(RpcRequest request, InetSocketAddress address) {
        RpcResponse<T> response = new RpcResponse<>();

        Socket socket = getSocketInstance(address);
        if (socket == null) {
            // 套接字链接失败
            response.setIsError(true);
            response.setErrorMsg("套接字链接失败");
            return response;
        }

        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            messageProtocol.clientToRequest(request, outputStream);

            response = messageProtocol.clientGetResponse(inputStream);
        } catch (IOException e) {
            log.error("客户端处理异常，error:{}", e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("客户端关闭套接字失败，error:{}", e);
                }
            }
        }
        return response;
    }

    /**
     * 获取对象实例
     *
     * @param address
     * @return
     */
    private Socket getSocketInstance(InetSocketAddress address) {
        try {
            return new Socket(address.getHostString(), address.getPort());
        } catch (IOException e) {
            log.error("客户端获取套接字失败，error:{}", e);
        }
        return null;
    }
}
