package com.guuidea.component.log.unifyLog;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.log.unifyLog.model.NewHttpRequest;
import com.guuidea.component.log.unifyLog.model.NewHttpResponse;
import com.guuidea.component.log.utils.CollectionUtil;
import com.guuidea.component.utils.StreamUtil;

/**
 * 请求日志过滤器
 *
 * @author chenghao.zhou
 * @date 2020-5-21
 */
public class LogFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    private String method_get = "GET";

    private String method_post = "POST";

    //是否需要打印返返回值
    private Boolean printResult;

    //返回结果的最大长度
    private Integer resultLength;

    public LogFilter(Boolean printResult, Integer resultLength) {
        this.printResult = printResult;
        this.resultLength = resultLength;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * 获取request中的参数，并解码
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            getLogOfRequest((HttpServletRequest) request, (HttpServletResponse) response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * 对request进行解密并对response进行加密
     *
     * @param httpRequest
     * @param httpResponse
     * @param chain
     */
    protected void getLogOfRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            FilterChain chain) {

        Map<String, String[]> map = httpRequest.getParameterMap();
        String uri = httpRequest.getRequestURI();
        Long startTime = System.currentTimeMillis();

        //响应装饰类
        NewHttpResponse response = new NewHttpResponse(httpResponse);
        String body = "";

        if (method_get.equals(httpRequest.getMethod())) {
            try {
                chain.doFilter(httpRequest, response);
            } catch (Exception e) {
                logger.error("请求失败,{}", e.getMessage());
            }
        } else if (method_post.equals(httpRequest.getMethod())) {
            try {
                InputStream inputStream = httpRequest.getInputStream();
                byte[] bytes = inputStream == null ? null : StreamUtil.getBytes(inputStream);
                if (bytes != null && bytes.length != 0) {
                    body = new String(bytes, StandardCharsets.UTF_8);
                    NewHttpRequest request = new NewHttpRequest(httpRequest, bytes);
                    chain.doFilter(request, response);
                } else {
                    chain.doFilter(httpRequest, response);
                }
            } catch (Exception e) {
                logger.error("请求失败,{}", e.getMessage());
            }
        }

        byte[] responseBytes = response.getContent();
        Long time = System.currentTimeMillis() - startTime;
        if (printResult) {
            String ret = new String(responseBytes, StandardCharsets.UTF_8);

            String[] logInfo = new String[5];
            logInfo[0] = "{" + uri + "}";
            logInfo[1] = "{" + CollectionUtil.changeMapToString(map) + "}";
            logInfo[2] = "{" + body + "}";
            logInfo[3] = time.toString() + "ms";
            logInfo[4] = ret.substring(0, ret.length() > resultLength ? resultLength : ret.length());

            logger.info("uri:{}, params:{}, requestBody:{}, time:{}, result:{}\n", logInfo);
        } else {
            String[] logInfo = new String[4];
            logInfo[0] = "{" + uri + "}";
            logInfo[1] = "{" + CollectionUtil.changeMapToString(map) + "}";
            logInfo[2] = "{" + body + "}";
            logInfo[3] = time.toString() + "ms";

            logger.info("uri:{}, params:{}, requestBody:{}, time:{}\n", logInfo);
        }
        sendResponse(httpResponse, responseBytes);
    }

    public static void sendResponse(HttpServletResponse response, byte[] bytes) {

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentLength(-1);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                logger.error("close OutputStream failed,{}", e.getMessage());
            }
        }
    }
}
