package com.guuidea.component.chrome.tool;

import static com.guuidea.component.chrome.tool.common.ResourceBundleUtile.DEFAULT_UI_RESOURCE;
import static com.guuidea.component.chrome.tool.common.ResourceBundleUtile.loadResource;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.chrome.tool.common.StringUtils;
import com.guuidea.component.chrome.tool.model.GoogleAccount;
import com.guuidea.component.chrome.tool.model.ProxyAccount;
import com.guuidea.component.chrome.tool.task.TaskChain;
import com.guuidea.component.chrome.tool.ui.MainLayoutController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 古点应用程序
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 13:55
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class GuuideaApp extends Application {

    private static Logger logger = LoggerFactory.getLogger(GuuideaApp.class);
    /**
     * 桌面托盘信息
     */
    public TrayIcon trayIcon;
    /**
     * 主舞台
     */
    private Stage primaryStage;
    /**
     * 主场景
     */
    private Scene rootScene;
    /**
     * 主控制器
     */
    private MainLayoutController controller;

    /**
     * 停止按钮
     */
    private Menu stopMenu = new Menu("停止运行");

    /**
     * 存储谷歌账号信息
     */
    private java.util.List<GoogleAccount> googleAccountList = new ArrayList<>();

    /**
     * 程序开始执行
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Chrome 账号助手@技术预研组");
        try {
            //加载主场景界面
            FXMLLoader mainLayoutLoader = new FXMLLoader(GuuideaApp.class.getResource("/resources/ui/main.fxml"));
            mainLayoutLoader.setResources(loadResource(DEFAULT_UI_RESOURCE));
            Pane mainLayoutPane = mainLayoutLoader.load();

            rootScene = new Scene(mainLayoutPane);
            primaryStage.setScene(rootScene);
            primaryStage.setResizable(false);

            controller = mainLayoutLoader.getController();
            controller.setMainGui(this);
            controller.init(primaryStage);
            //添加桌面托盘信息
            addToTray();
            primaryStage.getIcons()
                    .add(new Image(GuuideaApp.class.getResource("/resources/images/icon.png").toString()));
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 添加桌面托盘菜单
     */
    private void addToTray() {
        // ensure awt is initialized
        Toolkit.getDefaultToolkit();

        // make sure system tray is supported
        if (!SystemTray.isSupported()) {
            logger.warn("当前系统不支持设置桌面托盘菜单");
        }

        final SystemTray tray = SystemTray.getSystemTray();
        try {

            java.awt.Image image = ImageIO.read(GuuideaApp.class.getResource("/resources/images/icon.png"));
            trayIcon = new TrayIcon(image);
            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            primaryStage.show();
                        }
                    });
                }
            });

            MenuItem openItem = new MenuItem("显示主程序");
            openItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            show();
                        }
                    });
                }
            });


            MenuItem exitItem = new MenuItem("退出");
            exitItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.destroy();
                    Platform.exit();
                    tray.remove(trayIcon);
                }
            });
            PopupMenu popup = new PopupMenu();
            popup.add(openItem);
            popup.add(stopMenu);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);
            trayIcon.setToolTip("当前未登录账号");
            tray.add(trayIcon);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示主场景
     */
    public void show() {
        primaryStage.show();
    }

    /**
     * 停止菜单
     *
     * @param proxyAccount
     * @param googleAccount
     * @param taskChain
     */
    public boolean addStopMenuItem(ProxyAccount proxyAccount, GoogleAccount googleAccount, TaskChain taskChain) {
        MenuItem menuItem = new MenuItem(googleAccount.getUserName());

        menuItem.addActionListener(new ActionListener() {
            //点击执行销毁操作
            @Override
            public void actionPerformed(ActionEvent e) {
                //停止任务链
                taskChain.stop();
            }
        });
        stopMenu.add(menuItem);
        googleAccountList.add(googleAccount);
        return Boolean.TRUE;
    }

    /**
     * 释放菜单
     *
     * @param googleAccount
     */
    public void removeMenuItem(GoogleAccount googleAccount) {
        for (int i = 0; i < googleAccountList.size(); i++) {
            GoogleAccount account = googleAccountList.get(i);
            //匹配账号
            if (StringUtils.equalsIgnoreCase(account.getId(), googleAccount.getId())) {
                //删除菜单
                stopMenu.remove(i);
                googleAccountList.remove(i);
                return;
            }
        }
    }
}
