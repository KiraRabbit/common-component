package com.guuidea.component.docker.model.container;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Docker container port 配置.
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since 1.0
 */
@Data
public class DockerContainerPortDTO {
    @JSONField(name = "IP")
    String ip;

    @JSONField(name = "PrivatePort")
    Integer privatePort;

    @JSONField(name = "PublicPort")
    Integer publicPort;

    @JSONField(name = "Type")
    String type;
}
