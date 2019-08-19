package com.springboot.whb.study.rpc.rpc_v2.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.springboot.whb.study.rpc.rpc_v2.io.protocol.MessageProtocol;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/8/19 9:57
 * @description: 服务端处理器
 */
@Slf4j
@Data
public class ServiceHandler {

    /**
     * 线程池
     */
    private ThreadPoolExecutor executor = null;

    /**
     * 服务接口
     */
    private RpcService rpcService;

    /**
     * 消息协议
     */
    private MessageProtocol messageProtocol;

    public ServiceHandler(RpcService rpcService) {
        this.rpcService = rpcService;
        //创建线程的线程工厂
        ThreadFactory commonThreadName = new ThreadFactoryBuilder()
                .setNameFormat("Parse-Task-%d")
                .build();
        //构造线程池
        this.executor = new ThreadPoolExecutor(
                10,
                10,
                2,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200),
                commonThreadName,
                (Runnable r, ThreadPoolExecutor executor) -> {
                    SocketTask socketTask = (SocketTask) r;
                    Socket socket = socketTask.getSocket();
                    if (socket != null) {
                        try {
                            //无法及时处理和响应就快速拒绝掉
                            socket.close();
                            log.info("reject socket:" + socketTask + ", and closed.");
                        } catch (IOException e) {
                            log.error("socket关闭失败，error:{}", e);
                        }
                    }
                }
        );
    }

    /**
     * 服务处理：接收到新的套接字，包装成为一个runnable提交给线程去执行
     *
     * @param socket
     */
    public void handler(Socket socket) {
        this.executor.execute(new SocketTask(socket));
    }

    class SocketTask implements Runnable {
        private Socket socket;

        public SocketTask(Socket socket) {
            this.socket = socket;
        }

        public Socket getSocket() {
            return socket;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                // 获取客户端请求数据，统一包装成RpcRequest
                RpcRequest request = messageProtocol.serviceToRequest(inputStream);
                RpcResponse response = rpcService.invoke(request);
                log.info("request:[" + request + "],response:[" + response + "]");
                // 反射调用，得到具体的返回值
                messageProtocol.serviceGetResponse(response, outputStream);
            } catch (Exception e) {
                log.error("服务端处理出现异常，error:{}", e);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        log.error("socket关闭失败，error:{}", e);
                    }
                }
            }
        }
    }
}
