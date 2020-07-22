package com.guuidea.component.chrome.tool.service;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.chrome.tool.common.AesCipher;
import com.guuidea.component.chrome.tool.model.GoogleAccount;


/**
 * 阿里云账号登陆
 *
 * @Author: hzchendou
 * @Date: 2019/10/21 11:49
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class AliyunAccountLoginService {

    private static final Logger logger = LoggerFactory.getLogger(AliyunAccountLoginService.class);

    public static boolean startLogin(ChromeDriver driver, GoogleAccount googleAccount) throws InterruptedException {
        //查看是否存在 阿里云 login iframe alibaba-login-box
        final WebDriver alibabaDriver;
        try {
            alibabaDriver = driver.switchTo().frame("alibaba-login-box");
        } catch (Exception ex) {
            //TODO 两个版本跳转目录， 国际版
            return Boolean.TRUE;
        }

        //查看当前是否在账号输入栏, 如果不是那么切换到账号密码登陆栏
        operateWebElement(alibabaDriver, By.className("icon-password"), AliyunAccountLoginService::clickWebElement);
        //输入账号
        //输入密码
        //如果有登陆按钮，并且点击了登陆按钮，需要进行登陆操作
        Boolean stepStatus = false;
        Thread.sleep(1000);
        //输入邮箱
        stepStatus = operateWebElement(alibabaDriver, By.id("fm-login-id"), element -> {
            //鼠标移动到元素
            Actions actions = new Actions(alibabaDriver);
            Thread.sleep(200);
            actions.moveToElement(element).perform();
            Thread.sleep(200);
            actions.click(element).perform();
            String userName = googleAccount.getUserName();
//            TODO 模拟输入
            for (int i = 0; i < userName.length(); i++) {
                setWebElementValue(element, userName.substring(i, i + 1));
                Thread.sleep(100);
            }
            return Boolean.TRUE;
        });
        if (!stepStatus) {
            logger.warn("未能找到账号输入框: {}", googleAccount.getUserName());
            return false;
        }
        Thread.sleep(2000);
        //输入密码
        //解密密码
        String password = AesCipher.cipher.decodeToString(googleAccount.getPassword());
        stepStatus = operateWebElement(alibabaDriver, By.id("fm-login-password"), element -> setWebElementValue(element, password));
        if (!stepStatus) {
            logger.warn("未能找到输入密码框元素: {}", googleAccount.getUserName());
            return false;
        }
        //TODO 暂不点击登陆
        Thread.sleep(1000);
        stepStatus = operateWebElement(alibabaDriver, By.cssSelector("button[type=submit]"), AliyunAccountLoginService::clickWebElement);
        if (!stepStatus) {
            logger.warn("未能找到登陆按钮: {}", googleAccount.getUserName());
            return false;
        }
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.startsWith("https://homenew.console.aliyun.com")) {
            logger.warn("登陆成功: {}", googleAccount.getUserName());
        } else {
            logger.warn("登陆失败: {}", googleAccount.getUserName());
            return false;
        }
        return true;
    }

    /**
     * 操作网页元素信息
     *
     * @param driver
     * @param by
     * @return
     */
    private static Boolean operateWebElement(WebDriver driver, By by, CallBackFnBoolean callBackFn) throws InterruptedException {
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
