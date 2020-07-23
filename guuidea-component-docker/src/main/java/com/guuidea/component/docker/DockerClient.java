package com.guuidea.component.docker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.guuidea.component.docker.model.ContainerCreateArgs;
import com.guuidea.component.docker.model.RegistryAccountModel;
import com.guuidea.component.docker.model.container.DockerContainerDTO;
import com.guuidea.component.docker.model.images.DockerImageDTO;

/**
 * Docker 客户端.
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since 1.0
 */
public class DockerClient {

    /**
     * 远程Docker 服务API
     */
    private DockerRemoteApi dockerRemoteApi;

    /**
     * docker host信息构建Docker 客户端
     * 采用TLS 安全校验方案（目前只对客户端内容进行校验，不校验服务端证书）
     *
     * @param dockerHost 使用
     * @param p12        p12 证书信息
     * @param password   证书密钥信息
     */
    public DockerClient(String dockerHost, String p12, String password) {
        dockerRemoteApi = ApiClientFactory.createService(DockerRemoteApi.class, dockerHost, p12, password);
    }

    /**
     * docker host信息构建Docker 客户端(不校验客户端请求)
     *
     * @param dockerHost
     */
    public DockerClient(String dockerHost) {
        dockerRemoteApi = ApiClientFactory.createService(DockerRemoteApi.class, dockerHost, null, null);
    }

    /**
     * 获取服务器时间
     *
     * @return
     */
    public String ping() {
        return ApiClientFactory.executeStringSync(dockerRemoteApi.ping());
    }

    /**
     * 获取容器信息
     *
     * @param names, 容器名称列表
     * @param status 容器状态：created|restarting|running|removing|paused|exited|dead
     * @return
     */
    public List<DockerContainerDTO> containerJson(@Nullable List<String> names, @Nullable List<String> status) {
        Map<String, List<String>> params = new HashMap<>();
        if (names != null && names.size() > 0) {
            params.put("name", names);
        }
        if (status != null && status.size() > 0) {
            params.put("status", status);
        }
        String filters = JSONObject.toJSONString(params);
        String result = ApiClientFactory.executeStringSync(dockerRemoteApi.containers(filters));
        return JSONObject.parseArray(result, DockerContainerDTO.class);
    }

    /**
     * 创建容器
     *
     * @param args
     * @return
     */
    public String createContainer(ContainerCreateArgs args) {
        JSONObject argJson = new JSONObject();
        String name = args.getName();
        if (args.getEnvs() != null) {
            argJson.put("Env", args.getEnvs());
        }
        if (args.getCmd() != null) {
            argJson.put("Cmd", args.getCmd());
        }
        argJson.put("Image", args.getImage());
        JSONObject hostConfig = new JSONObject();
        if (args.getHostConfBinds() != null) {
            Map<String, String> bindMap = args.getHostConfBinds();
            List<String> binds = new ArrayList<>();
            bindMap.forEach((key, value) -> {
                binds.add(key + ":" + value);
            });
            hostConfig.put("Binds", binds);
        }
        if (args.getHostConfPortBinds() != null) {
            Map<String, List<Map<String, String>>> ports = new HashMap<>();
            Map<String, String> portMap = args.getHostConfPortBinds();
            portMap.forEach((key, value) -> {
                Map<String, String> portConf = new HashMap<>(1);
                portConf.put("HostPort", key);
                ports.put((value + "/tcp"), Lists.newArrayList(portConf));
            });
            hostConfig.put("PortBindings", ports);
        }
        if (args.isRestart()) {
            Map<String, Object> policy = new HashMap<>(1);
            policy.put("Name", "always");
            policy.put("MaximumRetryCount", 100);
            hostConfig.put("RestartPolicy", policy);
        }
        argJson.put("HostConfig", hostConfig);
        //创建容器
        try {
            //{"message":"Conflict. The container name \"/mysql-190\" is already in use by container \"04142d7dcdc3fef0c7b63e80c0948de759917eaf930fc88975ff00a2d9e0e228\". You have to remove (or rename) that container to be able to reuse that name."}
            String result = ApiClientFactory.executeStringSync(dockerRemoteApi.createContainer(name, argJson));
            return JSONObject.parseObject(result).getString("Id");
        } catch (Exception ex) {
            System.out.println("createContainer error :" + ex.getMessage());
        }
        return null;
    }

    /**
     * 启动容器
     *
     * @param id
     * @return
     */
    public boolean startContainer(String id) {
        try {
            ApiClientFactory.executeStringSync(dockerRemoteApi.startContainer(id));
            return Boolean.TRUE;
        } catch (Exception ex) {
            System.out.println("startContainer error: " + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * 停止容器
     *
     * @param id
     * @return
     */
    public boolean stopContainer(String id) {
        try {
            ApiClientFactory.executeStringSync(dockerRemoteApi.stopContainer(id));
            return Boolean.TRUE;
        } catch (Exception ex) {
            System.out.println("stopContainer error: " + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * 重启容器
     *
     * @param id
     * @return
     */
    public boolean restartContainer(String id) {
        try {
            ApiClientFactory.executeStringSync(dockerRemoteApi.restartContainer(id));
            return Boolean.TRUE;
        } catch (Exception ex) {
            System.out.println("restartContainer error: " + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * 终止容器
     *
     * @param id
     * @return
     */
    public boolean killContainer(String id) {
        try {
            ApiClientFactory.executeStringSync(dockerRemoteApi.killContainer(id));
            return Boolean.TRUE;
        } catch (Exception ex) {
            System.out.println("killContainer error: " + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * 删除容器
     *
     * @param id
     * @return
     */
    public boolean deleteContainer(String id) {
        try {
            ApiClientFactory.executeStringSync(dockerRemoteApi.deleteContainer(id));
            return Boolean.TRUE;
        } catch (Exception ex) {
            System.out.println("deleteContainer error: " + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * 查询镜像信息
     *
     * @param since
     * @return
     */
    public List<DockerImageDTO> imageJson(@Nullable String since) {
        Map<String, String> params = new HashMap<>();
        if (since != null && since.length() > 0) {
            params.put("since", since);
        }
        String filters = JSONObject.toJSONString(params);
        String result = ApiClientFactory.executeStringSync(dockerRemoteApi.images(filters));
        return JSONObject.parseArray(result, DockerImageDTO.class);
    }

    /**
     * 构建镜像信息
     *
     * @param t
     * @param remote
     * @param dockerfile
     * @param argsJson
     * @return
     */
    public boolean buildImage(@Nonnull String t, @Nonnull String remote, @Nullable String dockerfile,
            @Nullable Map<String, String> argsJson) {
        String args = (argsJson == null || argsJson.size() < 1) ? null : JSONObject.toJSONString(argsJson);
        try {
            ApiClientFactory.executeStringSync(dockerRemoteApi.buildImage(t, remote, dockerfile, args));
            return Boolean.TRUE;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 推送镜像信息
     *
     * @param name
     * @param accountModel
     * @return
     */
    public boolean pushImage(String name, RegistryAccountModel accountModel) {
        String accountJson = JSONObject.toJSONString(accountModel);
        String auth = Base64.getEncoder().encodeToString(accountJson.getBytes(StandardCharsets.UTF_8));
        try {
            String result = ApiClientFactory.executeStringSync(dockerRemoteApi.pushImage(name, auth));
            if (result.contains("errorDetail")) {
                System.out.println("推送失败," + result);
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Boolean.FALSE;
    }


    /**
     * 拉取镜像
     * @param fromImage
     * @param accountModel
     * @return
     */
    public boolean createImage(String fromImage, RegistryAccountModel accountModel){
        String auth = null;
        if(accountModel!=null){
            String accountJson = JSONObject.toJSONString(accountModel);
            auth = Base64.getEncoder().encodeToString(accountJson.getBytes(StandardCharsets.UTF_8));
        }

        try {
            String result = ApiClientFactory.executeStringSync(dockerRemoteApi.createImage(fromImage,auth));
            if(result!=null && result.contains("error")){
                return false;
            }
            return true;
        } catch (Exception ex) {
            System.out.println("createImage error: " + ex.getMessage());
        }
        return false;
    }

}
