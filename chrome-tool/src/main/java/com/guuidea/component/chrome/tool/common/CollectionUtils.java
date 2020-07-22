package com.guuidea.component.chrome.tool.common;

import java.util.Collection;

/**
 * 数组集合工具
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 18:58
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection colls) {
        return colls == null || colls.size() < 1;
    }


    public static boolean isNotEmpty(Collection colls) {
        return !isEmpty(colls);
    }
}
