
package com.guuidea.component.chrome.tool.libs.proxy.network.proxy;

import java.util.List;

public interface IProxy {
    enum TYPE {SOCKS5, HTTP}

    boolean isReady();
    TYPE getType();
    byte[] getResponse(byte[] data);
    List<byte[]> getRemoteResponse(byte[] data);
    boolean isMine(byte[] data);
}
