package com.springboot.whb.study.rpc.rpc_v2.config;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: whb
 * @date: 2019/8/19 9:55
 * @description: 方法配置
 */
@Data
public class MethodConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private List<ArgumentConfig> argumentConfigs;

    /**
     * 是否需要返回
     */
    private Boolean isReturn;

    /**
     * 返回值类型
     */
    private Class<?> returnType;

    /**
     * 方法数组转方法配置集合
     *
     * @param methods
     * @return
     */
    public static List<MethodConfig> convert(Method[] methods) {
        List<MethodConfig> methodConfigList = new ArrayList<>(methods.length);
        MethodConfig methodConfig = null;
        for (Method method : methods) {
            methodConfig = new MethodConfig();
            methodConfig.setMethodName(method.getName());

            Class<?> returnType = method.getReturnType();
            String returnName = returnType.getName();
            if ("void".equals(returnName)) {
                methodConfig.setIsReturn(false);
            } else {
                methodConfig.setIsReturn(true);
            }
            methodConfig.setReturnType(returnType);
            methodConfig.setArgumentConfigs(convert(method.getParameters()));

            methodConfigList.add(methodConfig);
        }
        return methodConfigList;
    }

    /**
     * 参数数组转参数配置集合
     *
     * @param parameters
     * @return
     */
    private static List<ArgumentConfig> convert(Parameter[] parameters) {
        List<ArgumentConfig> argumentConfigs = new ArrayList<>(parameters.length);
        int start = 0;
        ArgumentConfig argumentConfig = null;
        for (Parameter parameter : parameters) {
            argumentConfig = new ArgumentConfig();
            argumentConfig.setIndex(start);
            argumentConfig.setType(parameter.getType().getName());
            argumentConfigs.add(argumentConfig);
            start += 1;
        }
        return argumentConfigs;
    }
}
