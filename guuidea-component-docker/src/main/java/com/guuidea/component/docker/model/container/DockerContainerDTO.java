package com.guuidea.component.docker.model.container;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Docker容器数据模型
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since
 */
@Data
@JsonIgnoreProperties
public class DockerContainerDTO {
    @JSONField(name = "Id")
    String id;

    @JSONField(name = "Names")
    List<String> names;

    @JSONField(name = "Image")
    String image;

    @JSONField(name = "ImageID")
    String imageID;

    @JSONField(name = "Command")
    String command;
    /**
     * 创建时间，单位：s
     */
    @JSONField(name = "Created")
    Long created;

    @JSONField(name = "Ports")
    List<DockerContainerPortDTO> ports;

    @JSONField(name = "State")
    String state;

    @JSONField(name = "Status")
    String status;

    @JSONField(name = "Mounts")
    List<DockerContainerMountDTO> mounts;
}
