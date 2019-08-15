package com.springboot.whb.study.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: whb
 * @date: 2019/8/15 14:46
 * @description: 客户端-服务订阅
 * 服务使用方需要使用register进行服务的注册，会生成对应的本地代理对象，后续只需要通过本地代理对象。
 */
public class RpcUsedService {

    private Map<String, Object> proxyObejctMap = new HashMap<>();
    private Map<String, Class> classMap = new HashMap<>();
    private IOClient ioClient;

    public void setIoClient(IOClient ioClient) {
        this.ioClient = ioClient;
    }

    /**
     * 服务注册
     *
     * @param clazz
     */
    public void register(Class clazz) {
        String className = clazz.getName();
        classMap.put(className, clazz);
        if (!clazz.isInterface()) {
            throw new RuntimeException("暂时只支持接口类型的");
        }
        try {
            RpcInvocationHandler handler = new RpcInvocationHandler();
            handler.setClazz(clazz);
            Object proxyInstance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, handler);
            proxyObejctMap.put(className, proxyInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T get(Class<T> clazz) {
        String className = clazz.getName();
        return (T) proxyObejctMap.get(className);
    }

    class RpcInvocationHandler implements InvocationHandler {
        private Class clazz;

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //实际上proxy没啥用处，不需要真正的反射
            MethodParameter methodParameter = new MethodParameter();
            methodParameter.setClassName(clazz.getName());
            methodParameter.setMethodName(method.getName());
            methodParameter.setArguments(args);
            methodParameter.setParameterTypes(method.getParameterTypes());
            return ioClient.invoke(methodParameter);
        }
    }
}
