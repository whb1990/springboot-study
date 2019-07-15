package com.springboot.whb.study.common.enums;

/**
 * @author: whb
 * @date: 2019/7/12 21:00
 * @description: 秒杀常量
 */
public enum SeckillStateEnum {
    END(0, "秒杀结束"),
    SUCCESS(1, "秒杀成功"),
    MANY(2, "当前参与活动人数太多，请稍后重试"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATE_REWRITE(-3, "数据篡改");

    private int state;
    private String info;

    SeckillStateEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }


    public String getInfo() {
        return info;
    }


    public static SeckillStateEnum stateOf(int index) {
        for (SeckillStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
