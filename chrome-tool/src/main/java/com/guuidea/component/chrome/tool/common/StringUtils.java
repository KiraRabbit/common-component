package com.guuidea.component.chrome.tool.common;

/**
 * 字符串工具
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 16:32
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class StringUtils {

    public static final String EMPTY = "";


    /**
     * 使用默认值
     *
     * @param str
     * @param defaultVal
     * @return
     */
    public static String defaultIfBlank(String str, String defaultVal) {
        return isBlank(str) ? defaultVal : str;
    }

    /**
     * 是否为空字符串
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() < 1;
    }

    /**
     * 不为空字符串
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 字符串是否相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return Boolean.TRUE;
        }
        if (str1 != null) {
            return str1.equalsIgnoreCase(str2);
        }
        return Boolean.FALSE;
    }
}
