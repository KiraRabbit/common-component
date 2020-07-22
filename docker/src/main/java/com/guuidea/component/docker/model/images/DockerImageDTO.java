package com.guuidea.component.docker.model.images;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * Docker 镜像数据模型.
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since 1.0
 */
@Data
public class DockerImageDTO {
    @JSONField(name = "Containers")
    String containers;
    @JSONField(name = "Created")
    Long created;
    @JSONField(name = "Id")
    String id;
    @JSONField(name = "Labels")
    List<String> labels;
    @JSONField(name = "ParentId")
    String parentId;
    @JSONField(name = "RepoDigests")
    String repoDigests;
    @JSONField(name = "RepoTags")
    List<String> repoTags;
    @JSONField(name = "SharedSize")
    Long sharedSize;
    @JSONField(name = "Size")
    Long size;
    @JSONField(name = "VirtualSize")
    Long virtualSize;
}
