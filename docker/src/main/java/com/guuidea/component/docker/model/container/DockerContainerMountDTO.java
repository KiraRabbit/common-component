package com.guuidea.component.docker.model.container;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * Docker容器挂载模型
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since
 */
@Data
public class DockerContainerMountDTO {
    @JSONField(name = "Type")
    String type;
    @JSONField(name = "Name")
    String name;
    @JSONField(name = "Source")
    String source;
    @JSONField(name = "Destination")
    String destination;
    @JSONField(name = "Driver")
    String driver;
    @JSONField(name = "Mode")
    String mode;
    @JSONField(name = "RW")
    String rw;
    @JSONField(name = "Propagation")
    String propagation;
}
