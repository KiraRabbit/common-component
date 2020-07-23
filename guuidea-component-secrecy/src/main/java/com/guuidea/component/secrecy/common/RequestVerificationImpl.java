package com.guuidea.component.secrecy.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求校验，不通过直接返回
 *
 * @author chenghao.zhou
 * @date 2020-4-10
 */
public class RequestVerificationImpl implements RequestVerification {

    private static final Logger logger = LoggerFactory.getLogger(RequestVerificationImpl.class);

    private static final Set<String> EXCLUDE_PATH = new HashSet<>();
    private static final byte[] bytes = "good!!".getBytes();

    static {
        EXCLUDE_PATH.add("txt");
        EXCLUDE_PATH.add("gif");
        EXCLUDE_PATH.add("jpg");
        EXCLUDE_PATH.add("png");
        EXCLUDE_PATH.add("gif");
        EXCLUDE_PATH.add("css");
        EXCLUDE_PATH.add("ico");
        EXCLUDE_PATH.add("html");
        EXCLUDE_PATH.add("htm");
    }

    /**
     * 默认拒绝静态资源请求，自定义策略需重写方法
     *
     * @param httpRequest
     * @return
     */
    @Override
    public Boolean requestPassed(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String[] strings = httpRequest.getServletPath().split("\\.");
        //判断是否为静态资源请求
        if (strings != null && strings.length != 0 && EXCLUDE_PATH.contains(strings[strings.length - 1])) {
            try {
                httpResponse.getOutputStream().write(bytes);
            } catch (IOException e) {
                logger.error("write OutputStream to httpResponse failed,{}", e.getMessage());
            }

            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
}
