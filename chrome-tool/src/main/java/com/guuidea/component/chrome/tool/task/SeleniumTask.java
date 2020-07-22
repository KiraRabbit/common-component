package com.guuidea.component.chrome.tool.task;

import java.util.Collections;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.guuidea.component.chrome.tool.libs.proxy.network.proxy.IProxy;
import com.guuidea.component.chrome.tool.model.GoogleAccount;
import com.guuidea.component.chrome.tool.model.ProxyAccount;


/**
 * 启动浏览器，并且实现自动登录
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 18:01
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class SeleniumTask extends TaskLifeCycle {

    private GoogleAccount googleAccount;

    private ProxyAccount proxyAccount;

    private ChromeDriver driver;

    public SeleniumTask(GoogleAccount googleAccount, ProxyAccount proxyAccount) {
        this.googleAccount = googleAccount;
        this.proxyAccount = proxyAccount;
    }

    /**
     * 启动浏览器
     */
    @Override
    public void start() {
        try {
            String rootPath = System.getProperty("user.dir");
            String chromeDriver = System.getProperty("os.name").indexOf("Window") > -1 ? "chromedriver_77.exe" : "chromedriver";
            System.setProperty("webdriver.chrome.driver", rootPath + "/bin/" + chromeDriver);
            ChromeOptions options = new ChromeOptions();
//            设置开发者模式启动，该模式下webdriver属性为正常值
            options.setExperimentalOption("excludeSwitches", Collections.singleton("enable-automation"));
            options.addArguments("user-data-dir=" + rootPath + "/tmp/" + googleAccount.getId());
            IProxy.TYPE proxyType = proxyAccount.getProxyType();
            options.addArguments("proxy-server=" + proxyType.name() + "://127.0.0.1:" + proxyAccount.getLocalPort());
            driver = new ChromeDriver(options);

            driver.manage().window().maximize();
            driver.get(googleAccount.getLoginUrl());

            //TODO 使用selenium执行模型登录会被检测, 暂时不执行模拟登陆
            // 开始执行账号登录
//            boolean status = AliyunAccountLoginService.startLogin(driver, googleAccount);
//            if (status) {
//                logger.info("登录成功: {}", googleAccount.getUserName());
//            } else {
//                logger.info("登录失败: {}", googleAccount.getUserName());
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String errorMsg = ex.getMessage();
            stopPre(errorMsg);
        }
    }

    /**
     * 销毁浏览器
     */
    @Override
    public void destroy() {
        try {
            if (driver != null) {
                driver.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
