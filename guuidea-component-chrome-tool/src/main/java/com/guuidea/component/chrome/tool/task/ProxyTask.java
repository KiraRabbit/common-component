package com.guuidea.component.chrome.tool.task;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.guuidea.component.chrome.tool.libs.proxy.network.NioLocalServer;
import com.guuidea.component.chrome.tool.model.ProxyAccount;


/**
 * 代理任务
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 17:51
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class ProxyTask extends TaskLifeCycle {

    /**
     * 代理账号信息
     */
    private ProxyAccount account;

    private ExecutorService service = Executors.newSingleThreadExecutor();

    private NioLocalServer localServer;

    public ProxyTask(ProxyAccount account) {
        this.account = account;
    }

    /**
     * 开始执行任务
     */
    @Override
    public void start() {
        try {
            //首先启动本地监听
            localServer = new NioLocalServer(account);
            service.execute(localServer);
            startNext();
        } catch (IOException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            stopPre(errorMsg);
        }
    }

    /**
     * 销毁任务
     */
    @Override
    public void destroy() {
        if (service != null) {
            service.shutdown();
        }
        if (localServer != null) {
            localServer.close();
        }
    }
}
