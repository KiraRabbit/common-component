package com.guuidea.component.chrome.tool.common;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源绑定工具
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 07:52
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class ResourceBundleUtile {

    public static final String DEFAULT_UI_RESOURCE = "resources.bundle.ui";

    /**
     * 资源缓存
     */
    private static Map<String, ResourceBundle> resourceBundleCache = new ConcurrentHashMap<>();


    /**
     * 加载资源信息
     *
     * @param baseName
     * @return
     */
    public static ResourceBundle loadResource(String baseName) {
        if (resourceBundleCache.containsKey(baseName)) {
            return resourceBundleCache.get(baseName);
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, Constant.LOCALE, new UTF8Control());
        resourceBundleCache.put(baseName, resourceBundle);
        return resourceBundle;
    }
}
