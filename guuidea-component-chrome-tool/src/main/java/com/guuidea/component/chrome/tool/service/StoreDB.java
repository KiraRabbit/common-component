package com.guuidea.component.chrome.tool.service;

import static com.guuidea.component.chrome.tool.common.CollectionUtils.isNotEmpty;
import static com.guuidea.component.chrome.tool.model.SystemConf.googleAccountCacheFile;
import static com.guuidea.component.chrome.tool.model.SystemConf.proxyAccountCacheFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.guuidea.component.chrome.tool.common.StoreUtils;
import com.guuidea.component.chrome.tool.common.StringUtils;
import com.guuidea.component.chrome.tool.model.GoogleAccount;
import com.guuidea.component.chrome.tool.model.ProxyAccount;

/**
 * 数据存储仓库信息
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 16:39
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class StoreDB {

    /**
     * 谷歌账号信息
     */
    private static List<GoogleAccount> accounts = new ArrayList<>();

    /**
     * 代理信息
     */
    private static Map<String, ProxyAccount> proxyAccountMap = new ConcurrentHashMap<>();

    static {
        //取出存储数据
        init();
    }

    /**
     * 更新或者是添加代理账号信息
     *
     * @param account
     */
    public static ProxyAccount updateOrAddProxyAccount(ProxyAccount account) {
        return proxyAccountMap.put(account.getId(), account);
    }

    /**
     * 删除代理信息
     *
     * @param id
     * @return
     */
    public static ProxyAccount delProxyAccount(String id) {
        return proxyAccountMap.remove(id);
    }

    /**
     * 依据代理id查询代理信息
     *
     * @param id
     * @return
     */
    public static ProxyAccount queryById(String id) {
        return proxyAccountMap.get(id);
    }

    /**
     * 加载所有代理账户信息
     *
     * @return
     */
    public static List<ProxyAccount> loadAllProxyAccount() {
        return Lists.newArrayList(proxyAccountMap.values());
    }

    /**
     * 加载所有google账户信息
     *
     * @return
     */
    public static List<GoogleAccount> loadAllGoogleAccount() {
        return Lists.newArrayList(accounts);
    }

    /**
     * 添加google账号
     *
     * @param account
     */
    public static boolean addGoogleAccount(GoogleAccount account) {
        return accounts.add(account);
    }

    /**
     * 更新google账号信息
     *
     * @param account
     * @return
     */
    public static boolean updateGoogleAccount(GoogleAccount account) {
        GoogleAccount cacheAccount = accounts.stream().filter(account1 -> StringUtils
                .equalsIgnoreCase(account1.getId(), account.getId())).findFirst().get();
        if (cacheAccount != null) {
            cacheAccount.setUserName(account.getUserName());
            cacheAccount.setProxyId(account.getProxyId());
            cacheAccount.setCheckIp(account.getCheckIp());
            cacheAccount.setLoginUrl(account.getLoginUrl());
            cacheAccount.setPassword(cacheAccount.getPassword());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 删除google账号
     *
     * @param account
     * @return
     */
    public static boolean delGoogleAccount(GoogleAccount account) {
        return accounts.removeIf(a -> StringUtils.equalsIgnoreCase(account.getId(), a.getId()));
    }


    /**
     * 初始化数据，取出历史数据
     */
    private static void init() {
        String googleFile = googleAccountCacheFile();
        List<GoogleAccount> accountsDB = StoreUtils.parseArray(googleFile, GoogleAccount.class);
        if (isNotEmpty(accountsDB)) {
            accounts.addAll(accountsDB);
        }
        String proxyFile = proxyAccountCacheFile();
        List<ProxyAccount> proxyAccounts = StoreUtils.parseArray(proxyFile, ProxyAccount.class);
        if (isNotEmpty(proxyAccounts)) {
            proxyAccounts.forEach(proxyAccount -> {
                proxyAccountMap.put(proxyAccount.getId(), proxyAccount);
            });
        }
    }

    /**
     * 存储数据
     */
    public static void store() {
        String googleFile = googleAccountCacheFile();
        StoreUtils.saveFile(googleFile, JSON.toJSONString(accounts));
        String proxyFile = proxyAccountCacheFile();
        StoreUtils.saveFile(proxyFile, JSON.toJSONString(proxyAccountMap.values()));
    }

}
