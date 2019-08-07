package com.springboot.whb.study.beanUtils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: whb
 * @date: 2019/8/7 14:35
 * @description: BeanUtils测试
 */
public class BeanUtilsTest {

    public static void main(String[] args) {
        //给对象A赋值
        EntityA entityA = new EntityA();
        List<UniteA> uniteAList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UniteA uniteA = new UniteA();
            uniteA.setAge(i);
            uniteA.setName("name" + i);
            uniteAList.add(uniteA);
        }
        entityA.setKey("entityA");
        entityA.setUniteList(uniteAList);

        //将对象A的值通过BeanUtils.copyProperties()方法复制给B
        EntityB entityB = new EntityB();
        //这么执行会报错（java.lang.ClassCastException:reflect.UniteA cannot be cast to reflect.UniteB）
        //EntityA中的UniteA中的字段与EntityB中UniteB中的字段类型不一样也能存储，原因是泛型仅仅适用于编译期。
        BeanUtils.copyProperties(entityA, entityB);
        System.out.println(entityB.getUniteList().get(0).getAge());
    }
}
