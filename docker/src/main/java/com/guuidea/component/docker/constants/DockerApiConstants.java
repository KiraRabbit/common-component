package com.guuidea.component.docker.constants;

/**
 * DockerAPi常量信息.
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since 1.0
 */
public interface DockerApiConstants {

    String PING_API_PATH = "/_ping";

    String CONTAINERS_JSON_PATH = "/containers/json";


    String CONTAINERS_CREATE_PATH = "/containers/create";

    String CONTAINERS_START_PATH = "/containers/{id}/start";

    String CONTAINERS_STOP_PATH = "/containers/{id}/stop";

    String CONTAINERS_RESTART_PATH = "/containers/{id}/restart";

    String CONTAINERS_KILL_PATH = "/containers/{id}/kill";

    String CONTAINERS_DELETE_PATH = "/containers/{id}";

    String IMAGES_JSON_PATH = "/images/json";

    String IMAGES_PUSH_PATH = "/images/{name}/push";

    /**
     * 构建镜像
     */
    String IMAGES_BUILD_PATH = "/build";

    /**
     * 拉取镜像
     */
    String IMAGES_CREATE_PATH = "/images/create";

}
