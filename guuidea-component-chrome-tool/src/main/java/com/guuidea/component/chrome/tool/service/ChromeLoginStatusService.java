package com.guuidea.component.chrome.tool.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.guuidea.component.chrome.tool.model.GoogleAccount;
import com.guuidea.component.chrome.tool.model.ProxyAccount;


/**
 * 登录状态服务类
 *
 * @Author: hzchendou
 * @Date: 2019-07-05 18:49
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class ChromeLoginStatusService {

    /**
     * 锁定
     */
    private static Map<String, LockModel> lockGoogleAccountMap = new ConcurrentHashMap<>();

    /**
     * 锁定
     */
    private static Map<String, LockModel> lockProxyAccountMap = new ConcurrentHashMap<>();

    /**
     * 锁状态(这里不用lock锁而使用状态的原因在于，操作事件都是由一个主线程执行，lock锁无法使用)
     */
    private static AtomicBoolean lockStatus = new AtomicBoolean();

    /**
     * 锁住账号
     *
     * @param proxyAccount
     * @param googleAccount
     * @return
     */
    public static boolean lockAccount(ProxyAccount proxyAccount, GoogleAccount googleAccount) {
        return lockAndOperation(() -> {
            if (compare(proxyAccount, googleAccount)) {
                return false;
            }
            LockModel lockModel = new LockModel(googleAccount, proxyAccount);
            lockGoogleAccountMap.put(googleAccount.getId(), lockModel);
            lockProxyAccountMap.put(proxyAccount.getId(), lockModel);
            return Boolean.TRUE;
        });
    }

    /**
     * 释放锁
     *
     * @param proxyAccount
     * @param googleAccount
     * @return
     */
    public static boolean release(ProxyAccount proxyAccount, GoogleAccount googleAccount) {
        return lockAndOperation(() -> {
            lockGoogleAccountMap.remove(googleAccount.getId());
            lockProxyAccountMap.remove(proxyAccount.getId());
            return Boolean.TRUE;
        });
    }

    /**
     * 释放所有数据
     *
     * @return
     */
    public static boolean releasAll() {
        lockGoogleAccountMap = null;
        lockProxyAccountMap = null;
        return Boolean.TRUE;
    }

    /**
     * 比较是否含有数据
     *
     * @param proxyAccount
     * @param googleAccount
     * @return
     */
    public static boolean compare(ProxyAccount proxyAccount, GoogleAccount googleAccount) {
        if (lockGoogleAccountMap.containsKey(googleAccount.getId())) {
            return Boolean.TRUE;
        }
        if (lockProxyAccountMap.containsKey(proxyAccount.getId())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 锁操作
     *
     * @param supplier
     * @return
     */
    public static boolean lockAndOperation(Supplier<Boolean> supplier) {
        if (lockStatus.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            try {
                return supplier.get();
            } finally {
                lockStatus.set(Boolean.FALSE);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * 锁模型
     */
    static class LockModel {
        /**
         * google账号
         */
        private GoogleAccount googleAccount;

        /**
         * 代理账号
         */
        private ProxyAccount proxyAccount;

        public LockModel(GoogleAccount googleAccount, ProxyAccount proxyAccount) {
            this.googleAccount = googleAccount;
            this.proxyAccount = proxyAccount;
        }

        public GoogleAccount getGoogleAccount() {
            return googleAccount;
        }

        public void setGoogleAccount(GoogleAccount googleAccount) {
            this.googleAccount = googleAccount;
        }

        public ProxyAccount getProxyAccount() {
            return proxyAccount;
        }

        public void setProxyAccount(ProxyAccount proxyAccount) {
            this.proxyAccount = proxyAccount;
        }
    }

}
