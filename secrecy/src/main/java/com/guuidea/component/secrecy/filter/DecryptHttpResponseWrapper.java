package com.guuidea.component.secrecy.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * HttpServletResponse装饰类，作为未加密的响应
 *
 * @author chenghao.zhou
 * @date 2020-4-9
 */
public class DecryptHttpResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream bout;

    public DecryptHttpResponseWrapper(HttpServletResponse response) {
        super(response);
        bout = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                bout.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(bout);
    }

    public byte[] getContent() {
        return bout.toByteArray();
    }
}
