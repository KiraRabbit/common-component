package com.guuidea.component.chrome.tool.common;

import static com.guuidea.component.chrome.tool.common.StringUtils.isNotBlank;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 存储工具
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 16:07
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class StoreUtils {

    private static final Logger logger = LoggerFactory.getLogger(StoreUtils.class);


    /**
     * 保存文件信息
     *
     * @param fn      文件名称
     * @param content 文件内容
     * @return
     */
    public static boolean saveFile(String fn, String content) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(fn, "utf-8");
            writer.print(content);
            writer.close();
        } catch (FileNotFoundException e) {
            logger.error("未找到指定文件, {}, 文件内容：{}", fn, content);
            return false;
        } catch (UnsupportedEncodingException e) {
            logger.error("当前编码不支持, {}, 文件内容：{}", fn, content);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取文件内容，并转换为对象
     *
     * @param fn
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String fn, Class<T> cls) {
        try {
            String content = readFileContent(fn);
            if (isNotBlank(content)) {
                return JSONObject.parseObject(content, cls);
            }
        } catch (Exception e) {
            // TODO 收集错误信息 do nothing
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析数据信息
     *
     * @param fn
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String fn, Class<T> cls) {
        try {
            String content = readFileContent(fn);
            if (isNotBlank(content)) {
                return JSONObject.parseArray(content, cls);
            }
        } catch (Exception e) {
            // TODO 收集错误信息 do nothing
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将文件内容转换为JSON格式数据
     *
     * @param fn
     * @return
     */
    public static JSONObject parseFileContent(String fn) {
        String content;
        try {
            content = readFileContent(fn);
            if (isNotBlank(content)) {
                return JSONObject.parseObject(content);
            }
        } catch (Exception e) {
            // TODO 收集错误信息 do nothing
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取文件数据
     *
     * @param fn
     * @return
     */
    public static String readFileContent(String fn) {
        Path path = Paths.get(fn);
        if (Files.exists(path)) {
            try {
                return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            } catch (Exception e) {
                // TODO 收集错误信息 do nothing
                e.printStackTrace();
            }
        }
        return null;
    }
}
