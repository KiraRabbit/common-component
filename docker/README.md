## Docker API 项目
提供 Java 版本 Docker SDK，通过该SDK可以远程操作 Docker 完成：

- [x] Docker 服务状态检测
- [x] 镜像创建
- [x] 镜像推送
- [x] 容器信息查询
- [x] 容器创建
- [x] 容器运行
- [x] 容器停止
- [x] 容器重启
- [x] 容器清除

## 环境依赖
- jdk8
- retrofit2
- okhttp3

## 相关使用说明

创建DockerClient 客户端实例(不安全连接):

```
DockerClient dockerClient = new DockerClient("http://190.1.1.51:9527");
# 检查Docker 状态
String ping = dockerClient.ping();
```

### 创建DockerClient 客户端实例（安全连接）
需要创建Ca 根证书、服务端证书、客户端证书
运行 scripts文件夹下的 `ca.sh` 脚本，创建根证书并且颁发下属证书。
- ca-key.pem (私钥信息)
- ca.pem （根证书）
- ca.srl 
- cert.pem (客户端证书)
- key.pem （客户端私钥）
- server-cert.pem （服务端证书）
- server-key.pem （服务端私钥）

在Centos 7 环境下设置Docker API TLS 配置：
- 执行 `vi /usr/lib/systemd/system/docker.service`, 添加证书信息：

```shell script
ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:9527
--tlsverify（启用TLS）
--tlscacert=${文件路径}/ca.pem (根证书)
--tlscert=${文件路径}/server-cert.pem（服务端证书）
--tlskey=${文件路径}/server-key.pem（服务端私钥）
-H fd:// --containerd=/run/containerd/containerd.sock
```

配置完成后重新启动 Docker 服务，执行以下两个命令：
- systemctl daemon-reload
- service docker restart

测试是否启用，执行以下命令：
```
curl -k https://docker所在机器ip:9527/info --cert ./cert.pem --key ./key.pem
```

至此完成TLS配置，在Java 的KeyStore（存储证书密钥信息对象）支持以下格式
- BKS(需要额外包支持)
- PKCS12 （确认使用）
- JKS
- UBER
- JCEKS

需要将上述PEM格式的证书转化为p12格式证书, 执行scripts中的pem2p12.sh 将PEM 转化为p12格式，为了方便存储以及使用，在工具类IOUtils提供了
InputStream转化为String(base64编码)，同时提供了反向转换。

```
DockerClient dockerClient = new DockerClient("http://190.1.1.51:9527", "p12文件base64编码内容", "p12证书密码");
# 检查Docker 状态
String ping = dockerClient.ping();
```







## 构建项目并上传到私服仓库

```shell script
mvn deploy -f pom.xml -Dmaven.test.skip=true
```

> 必须在maven setting.xml中设置nexus-snapshot、以及 nexus-release 服务账号密码, 账号必须要有上传权限，类似配置如下:
```shell script
<server>
    <id>nexus-snapshot</id>
    <username>your username</username>
    <password>your password</password>
</server>
<server>
    <id>nexus-release</id>
    <username>your username</username>
    <password>your password</password>
</server>
```

## 项目中使用

在maven项目中直接配置依赖

```shell script
<dependency>
  <groupId>com.guuidea.api</groupId>
  <artifactId>docker</artifactId>
  <version>${version}</version>
</dependency>
```

可以参考单元测试样例进行书写，需要注意的是Docker 镜像构建过程，这里需要特殊处理,Demo如下：

```
String t = "190.1.1.51:9090/jdk:20200217";
String remote = "https://demotest0901.oss-cn-hangzhou.aliyuncs.com/demo/jdk.tar";
boolean status = dockerClient.buildImage(t, remote, null, null);
Assert.assertTrue("构建失败", status);
```

这里的remote表示的是文件访问地址，需要注意这里的文件是一个tar归档文件，可以是tar格式也可以是gzip压缩算法。，对于tar 归档文件加压后的形式有
要求，

例如 `jdk.tar` 文档，解压后为：

- Dockerfile
- txt

解压后Dockerfile文件直接在当前目录下，不能出现解压后Dockerfile存在一个文件目录下。

t 就是镜像名称信息

## 后续优化内容
- [x] 提供 tls 安全策略

## 版本发布
- 1.0 提供Docker Http 请求 SDK
- 1.1 提供Docker TLS APi请求功能
