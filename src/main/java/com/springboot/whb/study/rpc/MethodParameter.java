package com.springboot.whb.study.rpc;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @author: whb
 * @date: 2019/8/15 14:39
 * @description: 请求对象
 */
@Data
public class MethodParameter {

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private Object[] arguments;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 从输入流中读取出类名、方法名、参数等数据组装成一个MethodParameter
     *
     * @param inputStream
     * @return
     */
    public static MethodParameter convert(InputStream inputStream) {
        try {
            ObjectInputStream input = new ObjectInputStream(inputStream);
            String className = input.readUTF();
            String methodName = input.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
            Object[] arguments = (Object[]) input.readObject();
            MethodParameter methodParameter = new MethodParameter();
            methodParameter.setClassName(className);
            methodParameter.setMethodName(methodName);
            methodParameter.setArguments(arguments);
            methodParameter.setParameterTypes(parameterTypes);
            return methodParameter;
        } catch (Exception e) {
            throw new RuntimeException("解析请求错误：{}", e);
        }
    }
}
