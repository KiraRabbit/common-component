package com.guuidea.component.chrome.tool.service;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.chrome.tool.common.AesCipher;
import com.guuidea.component.chrome.tool.model.GoogleAccount;

/**
 * 谷歌账号登录服务
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 18:04
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class GoogleAccountLoginService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAccountLoginService.class);


    public static boolean startLogin(ChromeDriver driver, GoogleAccount googleAccount) throws InterruptedException {
        //查看是否有登录按钮
        Boolean loginStatus = operateWebElement(driver, By.linkText("登录"), GoogleAccountLoginService::clickWebElement);
        //如果没有登录按钮, 查看是否有输入邮箱按钮
        if (!loginStatus) {
            List<WebElement> webElements = driver.findElements(By.id("identifierId"));
            if (webElements.size() > 0) {
                loginStatus = Boolean.TRUE;
            }
        }
        //如果有登陆按钮，并且点击了登陆按钮，需要进行登陆操作
        if (loginStatus) {
            Boolean stepStatus;
            Thread.sleep(1000);
            //输入邮箱
            stepStatus = operateWebElement(driver, By.id("identifierId"), element -> setWebElementValue(element, googleAccount.getUserName()));
            if (!stepStatus) {
                logger.warn("未能找到邮箱输入框: {}", googleAccount.getUserName());
                return false;
            }
            Thread.sleep(1000);
            //点击下一步
            stepStatus = operateWebElement(driver, By.id("identifierNext"), GoogleAccountLoginService::clickWebElement);
            if (!stepStatus) {
                logger.warn("未能找到邮箱下一步按钮: {}", googleAccount.getUserName());
                return false;
            }
            Thread.sleep(2000);
            //输入密码
            //解密密码
            String password = AesCipher.cipher.decodeToString(googleAccount.getPassword());
            stepStatus = operateWebElement(driver, By.cssSelector("input[type=password]"), element -> setWebElementValue(element, password));
            if (!stepStatus) {
                logger.warn("未能找到输入密码框信息: {}", googleAccount.getUserName());
                return false;
            }
            Thread.sleep(1000);
            stepStatus = operateWebElement(driver, By.id("passwordNext"), GoogleAccountLoginService::clickWebElement);
            if (!stepStatus) {
                logger.warn("未能找到密码下一步按钮: {}", googleAccount.getUserName());
                return false;
            }
            Thread.sleep(1000);
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.startsWith("https://myaccount.google.com")) {
                logger.warn("登陆成功: {}", googleAccount.getUserName());
            } else {
                logger.warn("登陆失败: {}", googleAccount.getUserName());
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 操作网页元素信息
     *
     * @param driver
     * @param by
     * @return
     */
    private static Boolean operateWebElement(ChromeDriver driver, By by, CallBackFnBoolean callBackFn) throws InterruptedException {
        List<WebElement> webElements = driver.findElements(by);
        if (webElements.size() < 1) {
            return Boolean.FALSE;
        }
        for (int i = 0; i < 5; i++) {
            try {
                return callBackFn.callBack(webElements.get(0));
            } catch (ElementNotInteractableException ex) {
                logger.error("执行浏览器元素操作异常, 不可交互, 元素：: {},", by.toString());
                Thread.sleep(1000);
            } catch (Exception ex) {
                logger.error("执行浏览器元素操作异常, 元素：: {}", by.toString(), ex);
                Thread.sleep(1000);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * 点击操作元素
     *
     * @param element
     * @return
     */
    private static boolean clickWebElement(WebElement element) {
        element.click();
        return Boolean.TRUE;
    }

    /**
     * 设置元素值信息
     *
     * @param element
     * @return
     */
    private static boolean setWebElementValue(WebElement element, String value) {
        element.sendKeys(value);
        return Boolean.TRUE;
    }

    /**
     * 函数回调接口
     */
    @FunctionalInterface
    interface CallBackFnBoolean {
        Boolean callBack(WebElement element) throws InterruptedException;
    }

}
