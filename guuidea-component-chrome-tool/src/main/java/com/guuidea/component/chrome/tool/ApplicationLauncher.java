package com.guuidea.component.chrome.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 应用程序加载器
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 13:55
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class ApplicationLauncher {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationLauncher.class);

    public static void main(String[] args) {
        logger.info("启动应用...");
        GuuideaApp.launch(GuuideaApp.class);
    }
}
