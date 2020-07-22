package com.guuidea.component.secrecy.common;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.secrecy.utils.MixFileSecrecy;

/**
 * 响应发送器
 *
 * @author chenghao.zhou
 * @date 2020-4-9
 */
public class ResponseSender {
    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);

    public static void sendEncryptResponse(HttpServletResponse response, byte[] bytes) {
        String ret = new String(bytes, StandardCharsets.UTF_8);
        logger.info("send response:{}", ret);
        sendUnEncryptResponse(response, MixFileSecrecy.getEncryptBytes(bytes));
    }

    public static void sendUnEncryptResponse(HttpServletResponse response, byte[] bytes) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
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
