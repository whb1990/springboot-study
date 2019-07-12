package com.springboot.whb.study.common.aspect;

import com.springboot.whb.study.common.annotation.FrequencyLimit;
import com.springboot.whb.study.common.constant.Constant;
import com.springboot.whb.study.common.exception.RRException;
import com.springboot.whb.study.common.util.HttpContextUtils;
import com.springboot.whb.study.common.util.IPUtils;
import com.springboot.whb.study.common.util.RedisUtils;
import com.springboot.whb.study.common.util.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author: whb
 * @date: 2019/7/11 14:59
 * @description: 频率限制，不保证多线程精确性，但可以保证整体有效
 */
@Aspect
@Component
public class FrequencyLimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(FrequencyLimitAspect.class);

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RedisTemplate redisTemplate;

    @Before("@annotation(frequencyLimit)")
    public void limit(FrequencyLimit frequencyLimit) throws Throwable {
        String countStr = frequencyLimit.count();
        Integer count = Integer.valueOf(countStr);
        String secondStr = frequencyLimit.second();
        Long second = Long.valueOf(secondStr);
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String ip = IPUtils.getIPAddr(request);
        if (StringUtils.isNotBlank(ip)) {
            Object blackObject = redisUtils.get(Constant.FREQUENCYLIMITBLACKKEY + ip);
            if (blackObject != null) {
                throw new RRException("该ip【" + ip + "】已经被拉黑");
            }
            String ipKey = Constant.FREQUENCYLIMITKEY + ip;
            Object object = redisUtils.get(ipKey);
            if (object == null) {
                redisUtils.set(ipKey, "1", second);
            } else {
                Long expire = 10L;
                if (expire > second || expire == -1) {
                    logger.error("过期时间出错ipKey:{},expire:{}", ipKey, expire);
                    redisTemplate.expire(ipKey, second, TimeUnit.SECONDS);
                }
                redisTemplate.opsForValue().increment(ipKey, 1L);
                Integer ipCount = Integer.valueOf(object.toString());
                if (ipCount >= count) {
                    if (ipCount > 50) {
                        redisTemplate.opsForValue().set(Constant.FREQUENCYLIMITBLACKKEY + ip, "1", 1, TimeUnit.DAYS);
                        logger.error("访问频繁ip:{},count:{},已拉黑", ip, count);
                    }
                    logger.info("该ip已超过访问限制：{}", ip);
                    throw new RRException("访问频繁，请稍后重试");
                }
            }
        }
    }
}
