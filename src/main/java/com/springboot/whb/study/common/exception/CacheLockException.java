package com.springboot.whb.study.common.exception;

/**
 * @author: whb
 * @date: 2019/8/8 11:00
 * @description: 缓存锁异常处理类
 */
public class CacheLockException extends Throwable {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CacheLockException(String msg) {
        this.msg = msg;
    }

    public CacheLockException() {
    }

}
