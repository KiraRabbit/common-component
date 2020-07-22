
package com.guuidea.component.chrome.tool.libs.proxy.network.proxy;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.chrome.tool.libs.proxy.utils.Reflection;
import com.guuidea.component.chrome.tool.libs.proxy.utils.Util;


/**
 * Proxy factory
 */
public class ProxyFactory {
    public static final Map<IProxy.TYPE, String> proxies = new HashMap<IProxy.TYPE, String>() {{
        put(IProxy.TYPE.HTTP, HttpProxy.class.getName());
        put(IProxy.TYPE.SOCKS5, Socks5Proxy.class.getName());
    }};
    private static Logger logger = LoggerFactory.getLogger(ProxyFactory.class);

    public static boolean isProxyTypeExisted(String name) {
        IProxy.TYPE type = IProxy.TYPE.valueOf(name);
        return (proxies.get(type) != null);
    }

    public static IProxy get(IProxy.TYPE type) {
        try {
            Object obj = Reflection.get(proxies.get(type));
            return (IProxy)obj;

        } catch (Exception e) {
            logger.info(Util.getErrorMessage(e));
        }

        return null;
    }

    public static List<IProxy.TYPE> getSupportedProxyTypes() {
        List sortedKeys = new ArrayList<>(proxies.keySet());
        Collections.sort(sortedKeys);
        return sortedKeys;
    }
}
