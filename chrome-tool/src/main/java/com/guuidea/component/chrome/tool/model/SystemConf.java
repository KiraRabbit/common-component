package com.guuidea.component.chrome.tool.model;

import java.io.File;

import com.alibaba.fastjson.JSONObject;
import com.guuidea.component.chrome.tool.common.AesCipher;
import com.guuidea.component.chrome.tool.common.StoreUtils;

/**
 * 系统配置信息
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 16:40
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class SystemConf {

    /**
     * 默认路径,对路径进行转义处理，防止出现解析异常
     */
    public static final String DEFAULT_PATH = System.getProperty("user.dir").replaceAll("\\\\","/");

    /**
     * 默认系统配置文件名称
     */
    public static final String DEFAULT_SYS_CONF_NAME = "config.json";

    /**
     * google账号
     */
    public static final String DEFAULT_GOOGLE_ACCOUNT_NAME = "google.json";

    /**
     * 代理数据
     */
    public static final String DEFAULT_PROXY_ACCOUNT_NAME = "proxy.json";

    /**
     * 存储路径前缀
     */
    public static String storePrefixPath = DEFAULT_PATH;

    /**
     * aes算法密钥信息
     */
    public static String aesSlat;

    static {
        //初始化数据，加载内容
        JSONObject confJson = StoreUtils.parseFileContent(DEFAULT_PATH + File.separator + DEFAULT_SYS_CONF_NAME);
        if (confJson == null) {
            //第一次初始化
            String[] keys = AesCipher.genAesKey();
            aesSlat = keys[0] + keys[1];
        } else {
            aesSlat = confJson.getString("aesSlat");
        }
        //初始化加密工具
        AesCipher.init(aesSlat.substring(0, 24), aesSlat.substring(24));
    }


    /**
     * 转换为JSON格式
     *
     * @return
     */
    public static String toJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("{")
                .append("\"storePrefixPath\":\"").append(storePrefixPath).append("\",")
                .append("\"aesSlat\":\"").append(aesSlat).append("\"")
                .append("}");
        return builder.toString();
    }


    /**
     * Google账号缓存文件地址
     *
     * @return
     */
    public static String googleAccountCacheFile() {
        return SystemConf.storePrefixPath + File.separator + SystemConf.DEFAULT_GOOGLE_ACCOUNT_NAME;
    }


    /**
     * 代理数据缓存文件地址
     *
     * @return
     */
    public static String proxyAccountCacheFile() {
        return SystemConf.storePrefixPath + File.separator + SystemConf.DEFAULT_PROXY_ACCOUNT_NAME;
    }
}
