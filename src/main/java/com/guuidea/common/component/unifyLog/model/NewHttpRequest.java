package com.guuidea.common.component.unifyLog.model;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * HttpServletRequest装饰类，重新封装请求信息
 *
 * @author chenghao.zhou
 * @date 2020-5-21
 */
public class NewHttpRequest extends HttpServletRequestWrapper {
    private final byte[] body;
    private String urlPath;

    public NewHttpRequest(HttpServletRequest request, byte[] body) {
        super(request);
        this.body = body;
    }


    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bin = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read(){
                return bin.read();
            }
        };
    }

    @Override
    public BufferedReader getReader(){
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public String getRequestURI() {
        if (urlPath == null) {
            return super.getRequestURI();
        }
        return urlPath;
    }

    @Override
    public String getServletPath() {
        if (urlPath == null) {
            return super.getServletPath();
        }
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
}
