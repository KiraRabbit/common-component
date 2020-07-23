package com.guuidea.component.secrecy.filter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HttpServletRequest装饰类，作为解密后的请求
 *
 * @author chenghao.zhou
 * @date 2020-4-9
 */
public class DecryptHttpRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;
    private String urlPath;

    public DecryptHttpRequestWrapper(HttpServletRequest request, byte[] body) {
        super(request);
        this.body = body;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bin = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bin.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
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
