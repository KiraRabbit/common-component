package com.guuidea.component.chrome.tool.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 任务生命周期接口
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 17:16
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public abstract class TaskLifeCycle {

    //任务状态，开始中，启动成功，启动失败
    public enum TaskStatus {STARTING, OK, FAILED}

    protected TaskStatus status = TaskStatus.OK;

    protected static final Logger logger = LoggerFactory.getLogger(TaskLifeCycle.class);

    /**
     * 下一个启动任务
     */
    protected TaskLifeCycle next;
    /**
     * 先前任务
     */
    protected TaskLifeCycle pre;


    /**
     * 开始
     */
    public abstract void start();


    /**
     * 销毁
     */
    public abstract void destroy();

    /**
     * 执行下一个任务
     */
    public void startNext() {
        if (next != null) {
            logger.info("开始启动任务 : {}" , getNextName());
            next.start();
        }
    }

    /**
     * 首先停止下一个任务
     */
    public void stopNext() {
        if (next != null) {
            next.stopNext();
            logger.info("任务停止完成: {}" , getNextName());
        }
        destroy();
    }

    /**
     * 停止先前任务
     *
     * @param msg
     */
    public void stopPre(String msg) {
        destroy();
        if (pre != null) {
            pre.stopPre(msg);
        }
    }

    /**
     * 设置下一个任务
     *
     * @param next
     * @return
     */
    public TaskLifeCycle setNext(TaskLifeCycle next) {
        this.next = next;
        this.next.setPre(this);
        return next;
    }

    /**
     * 获取任务状态
     *
     * @return
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * 设置上一个任务
     *
     * @param pre
     */
    public void setPre(TaskLifeCycle pre) {
        this.pre = pre;
    }

    protected String getNextName() {
        return next.getName();
    }

    protected String getName() {
        return this.getClass().getSimpleName();
    }
}
