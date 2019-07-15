package com.springboot.whb.study.web;

import com.springboot.whb.study.common.Result;
import com.springboot.whb.study.distributedlock.curator.CuratorDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: whb
 * @date: 2019/7/15 15:33
 * @description: 分布式锁控制器层
 */
@Slf4j
@RestController
@RequestMapping("/distributedLock")
public class DistributedLockController {
    @Autowired
    private CuratorDistributedLock curatorDistributedLock;

    private static final String PATH = "test";

    @GetMapping("/curator/lock1")
    public Result getLock1() {
        Boolean flag;
        curatorDistributedLock.acquireDistributedLock(PATH);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            log.error("Curator分布式锁getLock1失败:{}", e);
            flag = curatorDistributedLock.releaseDistributedLock(PATH);
        }
        flag = curatorDistributedLock.releaseDistributedLock(PATH);
        return Result.ok(flag);
    }

    @GetMapping("/curator/lock2")
    public Result getLock2() {
        Boolean flag;
        curatorDistributedLock.acquireDistributedLock(PATH);
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            log.error("Curator分布式锁getLock2失败:{}", e);
            flag = curatorDistributedLock.releaseDistributedLock(PATH);
        }
        flag = curatorDistributedLock.releaseDistributedLock(PATH);
        return Result.ok(flag);
    }
}
