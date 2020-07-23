package com.guuidea.component.chrome.tool.ui;

import javafx.stage.Stage;

/**
 * 组件舞台生命周期接口
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 08:13
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public interface StageLifeCycle {

    /**
     * 初始化
     */
    default void init(Stage stage) {

    }

    /**
     * 显示
     */
    void show();

    /**
     * 隐藏
     */
    void hide();

    /**
     * 销毁
     */
    void destroy();
}
