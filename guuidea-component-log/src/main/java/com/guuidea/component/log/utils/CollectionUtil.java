package com.guuidea.component.log.utils;

import java.util.Map;

/**
 * @author chenghao.zhou
 * @date 2020/5/21 16:42
 */
public class CollectionUtil {

    public static String changeMapToString(Map<String, String[]> map) {
        StringBuffer params = new StringBuffer();

        if (map != null && map.size() != 0) {
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                params.append(entry.getKey()).append(":").append(entry.getValue()[0]).append(",");
            }
            params.deleteCharAt(params.length()-1);
        }

        return params.toString();
    }

}
