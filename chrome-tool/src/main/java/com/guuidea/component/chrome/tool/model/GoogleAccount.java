package com.guuidea.component.chrome.tool.model;

/**
 * Google账号
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 16:00
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class GoogleAccount {

    /**
     * 谷歌账号id
     */
    private String id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码信息
     * 使用加密方式保存密码
     */
    private String password;

    /**
     * 登录地址
     */
    private String loginUrl;

    /**
     * 检查IP
     */
    private Boolean checkIp;

    /**
     * 先前IP地址信息
     */
    private String preIp;

    /**
     * 最后登录时间
     */
    private Long lastLoginAt;

    /**
     * 代理id信息
     */
    private String proxyId;

    /**
     * 创建时间
     */
    private Long createdAt;

    /**
     * 最后更新时间
     */
    private Long updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public Boolean getCheckIp() {
        return checkIp;
    }

    public void setCheckIp(Boolean checkIp) {
        this.checkIp = checkIp;
    }

    public String getPreIp() {
        return preIp;
    }

    public void setPreIp(String preIp) {
        this.preIp = preIp;
    }

    public Long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getProxyId() {
        return proxyId;
    }

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
