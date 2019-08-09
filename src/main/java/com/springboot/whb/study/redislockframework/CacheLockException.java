package com.springboot.whb.study.redislockframework;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存异常处理类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheLockException extends Throwable {

    private String msg;
}
