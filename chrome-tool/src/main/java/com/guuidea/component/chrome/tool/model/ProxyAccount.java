package com.guuidea.component.chrome.tool.model;

import java.util.List;

import com.guuidea.component.chrome.tool.libs.proxy.network.proxy.IProxy;


/**
 * 代理账号信息
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 17:32
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class ProxyAccount {

    /**
     * 代理ID，用于外部引用
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 本地监听端口
     */
    private Integer localPort;

    /**
     * 加密类型
     */
    private String method;

    /**
     * 密码信息
     */
    private String password;

    /**
     * 日志记录级别
     */
    private String logLevel;

    /**
     * 远程代理端口
     */
    private Integer remotePort;

    /**
     * 代理方式
     */
    private IProxy.TYPE proxyType;

    /**
     * 本地IP地址
     */
    private String localIpAddress;

    /**
     * 远程IP地址
     */
    private String remoteIpAddress;

    /**
     * 历史IP信息
     */
    private List<String> historyIps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public IProxy.TYPE getProxyType() {
        return proxyType;
    }

    public void setProxyType(IProxy.TYPE proxyType) {
        this.proxyType = proxyType;
    }

    public String getLocalIpAddress() {
        return localIpAddress;
    }

    public void setLocalIpAddress(String localIpAddress) {
        this.localIpAddress = localIpAddress;
    }

    public String getRemoteIpAddress() {
        return remoteIpAddress;
    }

    public void setRemoteIpAddress(String remoteIpAddress) {
        this.remoteIpAddress = remoteIpAddress;
    }

    public List<String> getHistoryIps() {
        return historyIps;
    }

    public void setHistoryIps(List<String> historyIps) {
        this.historyIps = historyIps;
    }
}
