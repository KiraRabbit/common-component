package com.guuidea.component.chrome.tool.common;

import static com.guuidea.component.chrome.tool.common.Constant.EMPTY_STR;

import java.util.UUID;

/**
 * uuid工具类
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 09:48
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class UUIDUtils {


    /**
     * 生成UUID
     *
     * @return
     */
    public static String genUUID() {
        return UUID.randomUUID().toString().replace("-", EMPTY_STR);
    }
}
