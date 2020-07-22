package com.guuidea.component.chrome.tool.service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.guuidea.component.chrome.tool.model.GoogleAccount;


/**
 * 谷歌账户存储服务
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 11:46
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class GoogleStoreDB {

    private static Lock lock = new ReentrantLock();

    /**
     * 加载所有Google账户信息
     *
     * @return
     */
    public static List<GoogleAccount> loadAllGoogleAccount() {
        return StoreDB.loadAllGoogleAccount();
    }

    /**
     * 新增账号信息
     *
     * @param account
     */
    public static void addGoogleAccount(GoogleAccount account) {
        lockGoogleAccount(() -> StoreDB.addGoogleAccount(account));
    }

    /**
     * 更新Google账号
     *
     * @param account
     */
    public static boolean upadteGoogleAccount(GoogleAccount account) {
        return lockGoogleAccount(() -> StoreDB.updateGoogleAccount(account));
    }

    /**
     * 删除Google账号
     *
     * @param account
     * @return
     */
    public static boolean delGoogleAccount(GoogleAccount account) {
        return lockGoogleAccount(() -> StoreDB.delGoogleAccount(account));
    }


    /**
     * 对代理账户增加和删除操作加锁
     *
     * @param supplier
     * @param <T>
     * @return
     */
    private static <T> T lockGoogleAccount(Supplier<T> supplier) {
        lock.lock();
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }
}
