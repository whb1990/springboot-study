package com.springboot.whb.study.beanUtils;

import lombok.Data;

import java.util.List;

/**
 * @author: whb
 * @date: 2019/8/7 14:34
 * @description: 测试BeanUtils.copyProperties方法的对象B
 */
@Data
public class EntityB {

    private String key;

    private List<UniteB> uniteList;
}
