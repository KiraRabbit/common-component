package com.guuidea.component.docker.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * p12格式证书工具类.
 *
 * @author hzchendou
 * @date 2020/2/24
 * @since 1.0
 */
public class P12FileUtils {

    public static String encodeP12Files(String filePath) {
        try {
            InputStream inputStream = new FileInputStream(filePath);
            return IOUtils.base64File(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
