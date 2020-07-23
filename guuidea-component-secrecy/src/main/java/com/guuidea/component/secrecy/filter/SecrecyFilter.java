package com.guuidea.component.secrecy.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.guuidea.component.secrecy.common.RequestVerification;
import com.guuidea.component.secrecy.common.RequestVerificationImpl;
import com.guuidea.component.secrecy.common.ResponseSender;
import com.guuidea.component.secrecy.utils.MixFileSecrecy;


/**
 * 加密通信过滤器
 *
 * @author chenghao.zhou
 * @date 2020-4-9
 */
public class SecrecyFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SecrecyFilter.class);

    private RequestVerification requestVerification;

    private byte[] bytes;

    private static final Map<String, Object> map = new HashMap<>();

    static {
        map.put("code", 500);
        map.put("uid", null);
        map.put("result", null);
    }

    /**
     * 默认
     */
    public SecrecyFilter() {
        this.requestVerification = new RequestVerificationImpl();
        this.bytes = JSON.toJSONBytes(map);
    }

    /**
     * 采用自定义策略和异常返回信息
     *
     * @param requestVerification
     * @param bytes
     */
    public SecrecyFilter(RequestVerification requestVerification, byte[] bytes) {
        this.requestVerification = requestVerification == null ? new RequestVerificationImpl() : requestVerification;
        this.bytes = bytes == null ? JSON.toJSONBytes(map) : bytes;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * 获取request中的参数，并解码
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            encryptRequest((HttpServletRequest) request, (HttpServletResponse) response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 对request进行解密并对response进行加密
     *
     * @param httpRequest
     * @param httpResponse
     * @param chain
     */
    protected void encryptRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain) {
        //响应装饰类
        DecryptHttpResponseWrapper responseWrapper = new DecryptHttpResponseWrapper(httpResponse);
        //请求装饰类
        DecryptHttpRequestWrapper requestWrapper;
        try {
            if (!requestVerification.requestPassed(httpRequest, httpResponse)) {
                return;
            }

            //获取解密后的数据流
            byte[] decryptBytes = MixFileSecrecy.getDecryptBytes(httpRequest.getInputStream());
            if (decryptBytes != null && decryptBytes.length != 0) {
                //包装成新的请求
                requestWrapper = new DecryptHttpRequestWrapper(httpRequest, decryptBytes);
                chain.doFilter(requestWrapper, responseWrapper);
            } else {//若流为空则直接进行处理
                chain.doFilter(httpRequest, responseWrapper);
            }
            //加密响应并发送
            ResponseSender.sendEncryptResponse(httpResponse, responseWrapper.getContent());
            return;
        } catch (Exception e) {
            logger.error("接口请求失败:", e);
        }
        ResponseSender.sendEncryptResponse(httpResponse, bytes);
    }

    @Override
    public void destroy() {
    }
}
