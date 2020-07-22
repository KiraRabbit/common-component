package com.guuidea.component.chrome.tool.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import com.guuidea.component.chrome.tool.common.StringUtils;
import com.guuidea.component.chrome.tool.libs.proxy.network.proxy.IProxy;
import com.guuidea.component.chrome.tool.model.GoogleAccount;
import com.guuidea.component.chrome.tool.model.ProxyAccount;


/**
 * 校验IP任务
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 18:02
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class CheckIPTask extends TaskLifeCycle {

    /**
     * IP查询地址
     */
    private static final String ipQueryUrl = "http://ip.cip.cc";

    private ProxyAccount proxyAccount;

    private GoogleAccount googleAccount;

    public CheckIPTask(ProxyAccount proxyAccount, GoogleAccount googleAccount) {
        this.proxyAccount = proxyAccount;
        this.googleAccount = googleAccount;
    }

    /**
     * 开始任务
     */
    @Override
    public void start() {
        //等待任务完成
        //TODO 优化状态检查方法
        TaskStatus preTaskStatus = pre.getStatus();
        if (preTaskStatus == TaskStatus.FAILED) {
            stopPre("启动代理监听任务失败");
            return;
        }
        String preIP = googleAccount.getPreIp();
        Boolean checkIP = googleAccount.getCheckIp();
        checkIP = checkIP == null ? Boolean.FALSE : checkIP;
        if (!checkIP) {
            startNext();
            return;
        }
        try {
            Thread.sleep(5000);
            URL url = new URL(ipQueryUrl);
            IProxy.TYPE proxyType = proxyAccount.getProxyType();
            InetSocketAddress inetAddress =
                    new InetSocketAddress(InetAddress.getByName("127.0.0.1"), proxyAccount.getLocalPort());
            Proxy proxy;
            if (proxyType == IProxy.TYPE.SOCKS5) {
                proxy = new Proxy(Proxy.Type.SOCKS, inetAddress);
            } else {
                proxy = new Proxy(Proxy.Type.HTTP, inetAddress);
            }
            URLConnection con = url.openConnection(proxy);
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            if (StringUtils.isNotBlank(preIP)) {
                if (StringUtils.equalsIgnoreCase(googleAccount.getPreIp(), sb.toString())) {
                    startNext();
                    return;
                }
                stopPre("IP不一致,先前IP:" + googleAccount.getPreIp() + ", 当前IP:" + sb.toString());
            }
            googleAccount.setPreIp(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            stopPre(errorMsg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁任务，回收资源
     */
    @Override
    public void destroy() {

    }
}
