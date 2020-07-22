package com.guuidea.component.secrecy.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义请求校验策略
 *
 * @author chenghao.zhou
 * @date 2020-4-10
 */
public interface RequestVerification {

    /**
     * 设置自定义策略对请求进行校验，通过则接收请求，否则直接返回
     *
     * @param httpRequest
     * @return
     */
    Boolean requestPassed(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}
