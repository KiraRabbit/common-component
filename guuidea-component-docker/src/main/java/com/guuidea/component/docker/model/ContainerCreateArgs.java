package com.guuidea.component.docker.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 容器创建参数.
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since 1.0
 */
@Data
public class ContainerCreateArgs {

    private String name;

    private boolean restart;

    /**
     * 环境变量
     * FOO=bar
     */
    private List<String> envs;
    /**
     * 镜像信息
     */
    private String image;

    /**
     * 文件挂载配置
     */
    private Map<String, String> hostConfBinds;

    /**
     * 端口映射
     */
    private Map<String, String> hostConfPortBinds;
    /**
     * 命令参数
     */
    private List<String> cmd;

}
