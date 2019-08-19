package com.springboot.whb.study.rpc.rpc_v2.core;

import com.google.common.base.Joiner;
import com.springboot.whb.study.rpc.rpc_v2.config.ServiceConfig;
import com.springboot.whb.study.rpc.rpc_v2.io.protocol.DefaultMessageProtocol;
import com.springboot.whb.study.rpc.rpc_v2.io.protocol.MessageProtocol;
import com.springboot.whb.study.rpc.rpc_v2.register.ServiceRegister;
import com.springboot.whb.study.rpc.rpc_v2.register.ZkServiceRegister;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/8/19 9:57
 * @description: RPC服务
 */
@Slf4j
@Data
public class RpcService {

    /**
     * k 是接口全名称
     * v 是对应的对象包含的详细信息
     */
    private Map<String, ServiceConfig> serviceConfigMap = new HashMap<>();

    /**
     * 端口号
     */
    private int port;

    /**
     * 服务注册
     */
    private ServiceRegister serviceRegister;

    /**
     * 连接器还未抽象处理，使用的还是BIO模型
     */
    private ServiceConnection serviceConnection;

    /**
     * 服务处理器
     */
    private ServiceHandler serviceHandler;

    /**
     * 线程池
     */
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
            new BasicThreadFactory.Builder().namingPattern(Joiner.on("-").join("service-thread-pool-", "%s")).build());

    public RpcService(int port) {
        this.port = port;
        this.serviceHandler = new ServiceHandler(this);
        this.serviceHandler.setMessageProtocol(new DefaultMessageProtocol());
        this.serviceRegister = new ZkServiceRegister();
    }

    /**
     * 设置消息协议
     *
     * @param messageProtocol
     */
    public void setMessageProtocol(MessageProtocol messageProtocol) {
        if (this.serviceHandler == null) {
            throw new RuntimeException("套接字处理器无效");
        }
        this.serviceHandler.setMessageProtocol(messageProtocol);
    }

    /**
     * 添加服务接口
     *
     * @param interfaceClass
     * @param ref
     * @param <T>
     */
    public <T> void addService(Class<T> interfaceClass, T ref) {
        String interfaceName = interfaceClass.getName();
        ServiceConfig<T> serviceConfig = ServiceConfig.convert(interfaceName, interfaceClass, ref, this);
        serviceConfigMap.put(interfaceName, serviceConfig);
    }

    /**
     * 注册服务
     */
    private void register() {
        //服务注册，在网络监听启动之前就需要完成
        serviceConfigMap.values().forEach(serviceRegister::register);
    }

    /**
     * 服务启动
     */
    public void start() {
        this.register();
        log.info("服务注册完成");

        this.serviceConnection = new ServiceConnection();
        this.serviceConnection.init(port, serviceHandler);
        threadPoolExecutor.execute(serviceConnection);

        //优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            RpcService.this.destroy();
        }));
    }

    /**
     * 通过反射执行，执行结果封装RpcResponse
     *
     * @param request
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> RpcResponse invoke(RpcRequest request) {
        if (request == null) {
            RpcResponse<V> response = new RpcResponse<>();
            response.setResult(null);
            response.setIsError(true);
            response.setErrorMsg("未知异常");
            return response;
        }
        String className = request.getClassName();
        //暂时不考虑没有对应的serviceConfig的情况
        ServiceConfig<K> serviceConfig = serviceConfigMap.get(className);
        K ref = serviceConfig.getRef();
        try {
            Method method = ref.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
            V result = (V) method.invoke(ref, request.getArguments());
            RpcResponse<V> response = new RpcResponse<>();
            response.setResult(result);
            response.setIsError(false);
            response.setErrorMsg("");
            return response;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 关闭服务
     */
    public void destroy() {
        this.serviceConnection.destory();
        log.info("服务端关闭了");
    }
}
