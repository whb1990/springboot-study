package com.springboot.whb.study.beanUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: whb
 * @date: 2019/8/7 14:42
 * @description: Java反射测试类
 */
public class ReflectTest {

    public static void main(String[] args) {
        //通过反射将Integer值添加到String类型的集合中
        List<String> strList = new ArrayList<>();
        strList.add("1234");
        try {
            Method method = strList.getClass().getDeclaredMethod("add", Object.class);
            method.invoke(strList, 5678);
            System.out.println(strList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
