
package com.guuidea.component.chrome.tool.libs.proxy.network.nio;

/**
 * pipe event for pipe worker
 */
public class PipeEvent {
    public byte[] data;
    public boolean isEncrypted;

    public PipeEvent() {}

    public PipeEvent(byte[] data, boolean isEncrypted) {
        this.data = data;
        this.isEncrypted = isEncrypted;
    }
}
