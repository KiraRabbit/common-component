
package com.guuidea.component.chrome.tool.libs.proxy.network.nio;

/**
 * Interface of socket handler
 */
public interface ISocketHandler {
    void send(ChangeRequest request, byte[] data);
    void send(ChangeRequest request);
}
