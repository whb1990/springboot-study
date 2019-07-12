package com.springboot.whb.study.common.runtime;

/**
 * @author: whb
 * @description:对外接口（调用Java的Runction.exec()）
 * @Date 9:46 2018/5/8
 */
public interface LocalCommandExecutor {

    /**
     * 调用Java的Runtime.exec执行外部命令的接口
     *
     * @param command--命令
     * @param timeout--超时时长
     * @return 执行结果
     */
    ExecuteResult executeCommand(String command, long timeout);
}
