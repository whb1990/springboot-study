package com.springboot.whb.study.beanUtils;

import lombok.Data;

import java.util.List;

/**
 * @author: whb
 * @date: 2019/8/7 14:31
 * @description: 测试BeanUtils.copyProperties方法的对象A
 */
@Data
public class EntityA {

    private String key;

    private List<UniteA> uniteList;
}
