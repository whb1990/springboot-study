package com.springboot.whb.study.rpc.rpc_v1;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/8/15 14:46
 * @description: 服务端-网络数据处理
 * 简单的BIO模型，开启了一个ServerSocket后，接收到数据后就将套接字丢给一个新的线程处理，ServerSocketRunnable接收一个socket之后，
 * 解析出MethodParameter请求对象，然后调用服务暴露的Invoke方法，再写回socket传输给客户端。
 */
public class IOService implements Runnable {

    private int port;

    private ServerSocket serverSocket;

    private RpcExploreService rpcExploreService;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
            new BasicThreadFactory.Builder().namingPattern(Joiner.on("-").join("thread-pool-", "%s")).build());

    private volatile boolean flag;

    public IOService(RpcExploreService rpcExploreService, int port) throws IOException {
        this.rpcExploreService = rpcExploreService;
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.flag = true;
        System.out.println("******服务端启动了********");

        //优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            flag = false;
            System.out.println("+++++服务端关闭了+++++");
        }));
    }

    @Override
    public void run() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {

            }
            if (socket == null) {
                continue;
            }
            threadPoolExecutor.execute(new ServerSocketRunnable(socket));
        }
    }

    class ServerSocketRunnable implements Runnable {
        private Socket socket;

        public ServerSocketRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                MethodParameter methodParameter = MethodParameter.convert(inputStream);
                Object result = rpcExploreService.invoke(methodParameter);
                ObjectOutputStream output = new ObjectOutputStream(outputStream);
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
