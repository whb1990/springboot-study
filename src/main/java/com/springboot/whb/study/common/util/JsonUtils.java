package com.springboot.whb.study.common.util;

import com.alibaba.fastjson.JSON;

/**
 * @author: whb
 * @date: 2019/8/8 11:18
 * @description: Json工具类
 */
public class JsonUtils {

    /**
     * 对象转json字符串
     *
     * @param o
     * @return
     */
    public static String beanToJson(Object o) {
        return JSON.toJSONString(o);
    }

    /**
     * 字符串转对象
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }
}
