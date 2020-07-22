package com.guuidea.component.chrome.tool.task;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.chrome.tool.listener.TaskStopListener;


/**
 * 任务链
 *
 * @Author: hzchendou
 * @Date: 2019-07-04 17:19
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class TaskChain {

    public static final Logger logger = LoggerFactory.getLogger(TaskChain.class);

    //执行状态，启动中，运行中，停止中，已停止
    enum TaskChainStatus {STARTING, STARTED, STOPING, STOPED}

    ;

    /**
     * 任务链运行状态
     */
    private AtomicReference<TaskChainStatus> taskChainStatus = new AtomicReference(TaskChainStatus.STOPED);

    /**
     * 开始任务
     */
    private TaskLifeCycle bootTask = new TaskLifeCycle() {

        /**
         * 开始任务
         */
        @Override
        public void start() {
            if (taskChainStatus.compareAndSet(TaskChainStatus.STOPED, TaskChainStatus.STARTING)) {
                logger.info("开始执行任务....");
                startNext();
            }
        }

        /**
         * 停止先前任务
         * @param msg
         */
        @Override
        public void stopPre(String msg) {
            if (listener != null) {
                listener.error(msg);
            }
            taskChainStatus.set(TaskChainStatus.STOPED);
        }

        /**
         * 结束任务
         */
        @Override
        public void destroy() {
            taskChainStatus.set(TaskChainStatus.STOPED);
            if (listener != null) {
                listener.success();
            }
            logger.info("任务执行结束！！！");
        }
    };

    /**
     * 最后任务
     */
    private TaskLifeCycle lastTask = bootTask;

    private TaskStopListener listener;

    public TaskChain(TaskStopListener listener) {
        this.listener = listener;
    }

    /**
     * 新建任务链
     *
     * @return
     */
    public static TaskChain chain(TaskStopListener listener) {
        return new TaskChain(listener);
    }

    /**
     * 服务开始
     */
    public void start() {
        bootTask.start();
    }

    /**
     * 服务结束
     */
    public void stop() {
        bootTask.stopNext();
    }


    /**
     * 添加最后任务
     *
     * @return
     */
    public void end() {
        setNext(new TaskLifeCycle() {
            @Override
            public void start() {
                if (taskChainStatus.compareAndSet(TaskChainStatus.STARTING, TaskChainStatus.STARTED)) {
                    logger.info("任务执行成功");
                }
            }

            @Override
            public void destroy() {
                if (taskChainStatus.get() == TaskChainStatus.STARTING || taskChainStatus.get() == TaskChainStatus.STARTED) {
                    taskChainStatus.set(TaskChainStatus.STOPING);
                    logger.info( "开始销毁任务");
                }
            }
        });

    }

    public TaskChain setNext(TaskLifeCycle bootTask) {
        this.lastTask = lastTask.setNext(bootTask);
        return this;
    }

    public TaskStopListener getListener() {
        return listener;
    }

    public TaskChain setListener(TaskStopListener listener) {
        this.listener = listener;
        return this;
    }
}
