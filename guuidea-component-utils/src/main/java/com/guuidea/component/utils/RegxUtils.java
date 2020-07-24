package com.guuidea.component.utils;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类.
 *
 * @author hzchendou
 * @date 2020/2/18
 * @since 1.0
 */
public class RegxUtils {

    private static final Pattern NAME_PATTERN = Pattern.compile("^/?[a-zA-Z0-9][a-zA-Z0-9_.-]+$");

    /**
     * 名称校验
     *
     * @param name
     * @return
     */
    public static boolean nameMatch(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }
}
