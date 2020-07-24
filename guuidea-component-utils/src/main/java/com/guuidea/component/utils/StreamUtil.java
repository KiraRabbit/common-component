package com.guuidea.component.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 流工具类
 *
 * @author chenghao.zhou
 * @date 2020/5/21 16:17
 */
public class StreamUtil {

    public static byte[] getBytes(InputStream in) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int len = 2048;
        byte[] b = new byte[len];
        while ((len = in.read(b)) != -1) {
            bout.write(b, 0, len);
        }

        byte[] bytes = bout.toByteArray();
        bout.close();
        in.close();

        return bytes;
    }
}
