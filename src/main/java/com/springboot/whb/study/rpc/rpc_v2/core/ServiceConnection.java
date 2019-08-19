package com.springboot.whb.study.rpc.rpc_v2.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: whb
 * @date: 2019/8/19 9:57
 * @description: 服务连接
 */
@Slf4j
@Data
public class ServiceConnection implements Runnable {

    /**
     * 端口号
     */
    private int port;

    /**
     * 服务关闭标记位
     */
    private volatile boolean flag = true;

    /**
     * 服务端套接字
     */
    private ServerSocket serverSocket;

    /**
     * 服务处理器
     */
    private ServiceHandler serviceHandler;

    /**
     * 初始化
     *
     * @param port
     * @param serviceHandler
     */
    public void init(int port, ServiceHandler serviceHandler) {
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("启动失败：" + e.getMessage());
        }
        this.serviceHandler = serviceHandler;
        log.info("服务启动了...");
    }

    @Override
    public void run() {
        while (flag) {
            try {
                Socket socket = serverSocket.accept();
                serviceHandler.handler(socket);
            } catch (IOException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    log.error("服务处理异常，error:{}", e);
                }
            }
        }
    }

    /**
     * 关闭连接
     */
    public void destory() {
        log.info("服务端套接字关闭...");
        this.flag = false;
    }
}
