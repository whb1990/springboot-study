package com.springboot.whb.study.common.util;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: whb
 * @date: 2019/7/12 9:33
 * @description: Http工具类
 */
public class HttpContextUtils {

    /**
     * 获取HttpServletRequest对象
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) RequestContextHolder.getRequestAttributes();
    }

    public static SimpleClientHttpRequestFactory getRequestTimeOut() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //设置连接超时时间
        requestFactory.setConnectTimeout(3000);
        //设置读取超时时间
        requestFactory.setReadTimeout(3000);
        return requestFactory;
    }

    public static String getDomain() {
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    public static String getOrigin() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader("Origin");
    }
}
