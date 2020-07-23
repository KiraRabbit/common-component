package com.guuidea.component.chrome.tool.service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.guuidea.component.chrome.tool.model.ProxyAccount;


/**
 * 代理存储服务
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 09:54
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class ProxyStoreDB {

    private static Lock lock = new ReentrantLock();

    /**
     * 添加代理信息
     *
     * @param account
     */
    public static void addProxy(final ProxyAccount account) {
        lockProxyAccount(() -> StoreDB.updateOrAddProxyAccount(account));
    }

    /**
     * 删除代理信息
     *
     * @param account
     * @return
     */
    public static void delProxy(final ProxyAccount account) {
        lockProxyAccount(() -> StoreDB.delProxyAccount(account.getId()));
    }

    /**
     * 更新代理信息
     *
     * @param account
     */
    public static void updateProxy(final ProxyAccount account) {
        lockProxyAccount(() -> StoreDB.updateOrAddProxyAccount(account));
    }

    /**
     * 查询代理信息
     *
     * @param id
     * @return
     */
    public static ProxyAccount queryById(final String id) {
        return lockProxyAccount(() -> StoreDB.queryById(id));
    }

    /**
     * 加载所有代理数据
     *
     * @return
     */
    public static List<ProxyAccount> loadAllProxyAccount() {
        return StoreDB.loadAllProxyAccount();
    }

    /**
     * 对代理账户增加和删除操作加锁
     *
     * @param supplier
     * @param <T>
     * @return
     */
    private static <T> T lockProxyAccount(Supplier<T> supplier) {
        lock.lock();
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }
}
