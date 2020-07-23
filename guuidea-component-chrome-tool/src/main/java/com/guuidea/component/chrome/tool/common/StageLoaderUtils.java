package com.guuidea.component.chrome.tool.common;

import static com.guuidea.component.chrome.tool.common.ResourceBundleUtile.DEFAULT_UI_RESOURCE;
import static com.guuidea.component.chrome.tool.common.ResourceBundleUtile.loadResource;

import java.io.IOException;


import com.guuidea.component.chrome.tool.GuuideaApp;
import com.guuidea.component.chrome.tool.ui.StageLifeCycle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 舞台加载器
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 08:02
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class StageLoaderUtils {

    /**
     * 加载舞台
     *
     * @param fxmlName
     * @param title
     * @return
     */
    public static Stage loadStage(String fxmlName, String title, Class loadClass) throws IOException {
        Stage stage = new Stage();
        FXMLLoader layoutLoader = new FXMLLoader(loadClass.getResource(fxmlName));
        layoutLoader.setResources(loadResource(DEFAULT_UI_RESOURCE));
        Pane pane = layoutLoader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(Boolean.FALSE);
        stage.getIcons().add(new Image(GuuideaApp.class.getResource("/resources/images/icon.png").toString()));
        return stage;
    }

    /**
     * 加载控制器
     *
     * @param fxmlName
     * @param title
     * @param loadClass
     * @param <T>
     * @return
     */
    public static <T> T loadController(String fxmlName, String title, Class loadClass) throws IOException {
        Stage stage = new Stage();
        FXMLLoader layoutLoader = new FXMLLoader(loadClass.getResource(fxmlName));
        layoutLoader.setResources(loadResource(DEFAULT_UI_RESOURCE));
        Pane pane = layoutLoader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(Boolean.FALSE);
        stage.getIcons().add(new Image(GuuideaApp.class.getResource("/resources/images/icon.png").toString()));
        T controller = layoutLoader.getController();
        if (controller instanceof StageLifeCycle) {
            ((StageLifeCycle)controller).init(stage);
        }
        return controller;
    }
}
