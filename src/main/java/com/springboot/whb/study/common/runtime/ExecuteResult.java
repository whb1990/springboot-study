package com.springboot.whb.study.common.runtime;

/**
 * @author: whb
 * @description:封装返回结果（调用Java的Runction.exec()）
 * @Date 9:46 2018/5/8
 */
public class ExecuteResult {
    /**
     * 退出码
     */
    private int exitCode;
    /**
     * 输出内容
     */
    private String executeOut;

    public ExecuteResult(int exitCode, String executeOut) {
        this.exitCode = exitCode;
        this.executeOut = executeOut;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public String getExecuteOut() {
        return executeOut;
    }

    public void setExecuteOut(String executeOut) {
        this.executeOut = executeOut;
    }
}
