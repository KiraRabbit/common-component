package com.guuidea.component.chrome.tool.listener;

/**
 * 任务结束监听.
 *
 * @author chendou
 * @date 2019/7/4
 * @since 1.0
 */
public interface TaskStopListener {

    /**
     * 成功结束
     */
    void success();

    /**
     * 异常错误
     *
     * @param msg
     */
    void error(String msg);

}
