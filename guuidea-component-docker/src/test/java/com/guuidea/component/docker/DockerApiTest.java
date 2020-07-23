package com.guuidea.component.docker;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.guuidea.component.docker.model.ContainerCreateArgs;
import com.guuidea.component.docker.model.RegistryAccountModel;
import com.guuidea.component.docker.model.container.DockerContainerDTO;
import com.guuidea.component.docker.model.images.DockerImageDTO;
import com.guuidea.component.docker.utils.P12FileUtils;

/**
 * docker api 测试.
 *
 * @author chendou
 * @date 2020/2/17
 * @since 1.0
 */
@RunWith(JUnit4.class)
public class DockerApiTest {

    private DockerClient dockerClient;

    @Before
    public void beforeTest() {
        String filePath = System.getProperty("user.dir") + File.separator +
                "certs" + File.separator + "cert.p12";
        String p12 = P12FileUtils.encodeP12Files(filePath);
        dockerClient = new DockerClient("http://190.1.1.51:9527", p12, "guuidea");
    }

    /**
     * 测试Docker 状态
     */
    @Test
    public void testPingApi() {
        String ping = dockerClient.ping();
        Assert.assertTrue("Docker 状态异常", StringUtils.equalsAnyIgnoreCase("OK", ping));
    }

    @Test
    public void testContainerJson() {
        List<String> names = Lists.newArrayList("mongo");
        List<DockerContainerDTO> containerJson = dockerClient.containerJson(names, null);
        System.out.println(containerJson);
    }

    @Test
    public void testCreateContainer() {
        ContainerCreateArgs args = new ContainerCreateArgs();
        args.setName("mysql-51");
        args.setImage("mysql:5.7");
        args.setEnvs(Lists.newArrayList("MYSQL_ROOT_PASSWORD=952711", "TZ=Asia/Shanghai"));
        Map<String, String> binds = new HashMap<>();
        binds.put("/data/docker/data/mysql190/data", "/var/lib/mysql");
        args.setHostConfBinds(binds);
        Map<String, String> ports = new HashMap<>();
        ports.put("3307", "3306");
        args.setHostConfPortBinds(ports);
        String result = dockerClient.createContainer(args);
        Assert.assertTrue("创建容器失败", result != null);
        System.out.println("id: " + result);
    }

    @Test
    public void startContainer() {
//        String id = "0da60c44155e806159d10d732971c3ed6fef230c8f42c023da6e24fd86854907";
        String id = "mysql-512";
        boolean result = dockerClient.startContainer(id);
        Assert.assertTrue("启动容器失败", result);
    }

    @Test
    public void stopContainer() {
        String id = "mysql-51";
        boolean result = dockerClient.stopContainer(id);
        Assert.assertTrue("停止容器失败", result);
    }

    @Test
    public void restartContainer() {
        String id = "mysql-51";
        boolean result = dockerClient.restartContainer(id);
        Assert.assertTrue("重启容器失败", result);
    }

    @Test
    public void killContainer() {
        String id = "mysql-51";
        boolean result = dockerClient.killContainer(id);
        Assert.assertTrue("终止容器失败", result);
    }

    @Test
    public void deleteContainer() {
        String id = "mysql-51";
        boolean result = dockerClient.deleteContainer(id);
        Assert.assertTrue("终止容器失败", result);
    }



    @Test
    public void testImagesJson() {
        List<DockerImageDTO> imageJson = dockerClient.imageJson(null);
        System.out.println(imageJson);
    }

    @Test
    public void testBuildApi() {
        String t = "190.1.1.51:9090/jdk:20200217";
        String remote = "https://demotest0901.oss-cn-hangzhou.aliyuncs.com/demo/jdk.tar";
        boolean status = dockerClient.buildImage(t, remote, null, null);
        Assert.assertTrue("构建失败", status);
    }

    /**
     * 推送进行概念股
     */
    @Test
    public void testPushImage() {
        String name = "190.1.1.51:5000/demo/jdk:20200227";
        RegistryAccountModel accountModel = new RegistryAccountModel();
        accountModel.setEmail("admin@example.com");
        accountModel.setServeraddress("190.1.1.51:5000");
        accountModel.setUsername("admin");
        accountModel.setPassword("Harbor12345");
        boolean status = dockerClient.pushImage(name, accountModel);
        Assert.assertTrue("构建失败", status);
    }

    @Test
    public void testCreateImage(){
        String fromImage = "centos:8";
        RegistryAccountModel accountModel = new RegistryAccountModel();
        accountModel.setEmail("admin@example.com");
        accountModel.setServeraddress("190.1.1.51:5000");
        accountModel.setUsername("admin");
        accountModel.setPassword("Harbor12345");
        boolean result = dockerClient.createImage(fromImage,accountModel);
        System.out.println(result);
        Assert.assertTrue("镜像拉取失败",result);
    }

}
