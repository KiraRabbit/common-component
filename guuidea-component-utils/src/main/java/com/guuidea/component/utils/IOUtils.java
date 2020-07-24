package com.guuidea.component.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Encoder;

/**
 * IO工具类.
 *
 * @author hzchendou
 * @date 2020/2/24
 * @since 1.0
 */
public class IOUtils {

    /**
     * 将二进制流转换为String
     *
     * @param inputStream
     * @return
     */
    public static String base64File(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            return Base64.encodeBase64String(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 将文本内容转化为流
     *
     * @param text
     * @return
     */
    public static InputStream toStream(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        byte[] bytes = Base64.decodeBase64(text);
        InputStream stream = new ByteArrayInputStream(bytes);
        return stream;
    }
}
