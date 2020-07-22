package com.guuidea.component.chrome.tool.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式化工具
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 11:13
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class TimeFormatterUtils {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

    /**
     * 格式化时间戳
     *
     * @param timestamp
     * @return
     */
    public static String formatYYYYMMddHHmmss(long timestamp) {
        Date date = new Date(timestamp);
        return formatter.format(date);
    }

}
