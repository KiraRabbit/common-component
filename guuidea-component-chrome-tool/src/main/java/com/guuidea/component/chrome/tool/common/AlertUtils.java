package com.guuidea.component.chrome.tool.common;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * 警告工具类
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 09:44
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class AlertUtils {

    /**
     * 显示警告信息
     *
     * @param title
     * @param message
     * @param type
     */
    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(type.name());
        a.setResizable(false);
        a.setContentText(message);
        a.showAndWait();
    }

    /**
     * 显示警告信息
     *
     * @param message
     */
    public static void showWarn(String message) {
        showAlert(Constant.APP_NAME, message, Alert.AlertType.WARNING);
    }


    /**
     * 显示确认按钮
     *
     * @param message
     * @return
     */
    public static boolean showConfirmAlert(String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, message, new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("确定", ButtonBar.ButtonData.YES));
        a.setTitle(Constant.APP_NAME);
        a.setHeaderText(Alert.AlertType.CONFIRMATION.name());
        a.setResizable(false);
        a.setContentText(message);
        Optional<ButtonType> _buttonType = a.showAndWait();
//        根据点击结果返回
        if (_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示错误消息
     *
     * @param msg
     */
    public static void showError(String msg) {
        showAlert(Constant.APP_NAME, msg, Alert.AlertType.ERROR);
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    public static void showMsg(String msg) {
        showAlert(Constant.APP_NAME, msg, Alert.AlertType.INFORMATION);
    }
}
