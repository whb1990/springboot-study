package com.springboot.whb.study.rpc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: whb
 * @date: 2019/8/15 14:45
 * @description: 服务端暴露
 * 服务暴露存储在objectMap对象中，所有可对提供的服务都必须添加到该容器中，以便于收到网络数据后能找到对应的服务，然后采用反射invoke调用，返回得到的结果。
 */
public class RpcExploreService {

    private Map<String, Object> objectMap = new HashMap<>();

    /**
     * 可对外提供的服务
     *
     * @param className
     * @param object
     */
    public void explore(String className, Object object) {
        objectMap.put(className, object);
    }

    /**
     * 采用反射进行调用
     *
     * @param methodParameter
     * @return
     */
    public Object invoke(MethodParameter methodParameter) {
        Object object = objectMap.get(methodParameter.getClassName());
        if (object == null) {
            throw new RuntimeException("无对应的执行类：【" + methodParameter.getClassName() + "】");
        }
        Method method = null;
        try {
            method = object.getClass().getMethod(methodParameter.getMethodName(), methodParameter.getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("执行类：【" + methodParameter.getClassName() + "】无对应的执行方法：【" + methodParameter.getMethodName() + "】");
        }
        try {
            Object result = method.invoke(object, methodParameter.getArguments());
            System.out.println(methodParameter);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("invoke方法执行失败：" + e.getMessage());
        }
    }
}
