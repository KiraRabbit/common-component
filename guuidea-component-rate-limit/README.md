# 限流组件

## 功能
提供java版本的jar，通过引入架包，自定义注解实现限流操作。(针对QPS限制)

## 环境依赖

jdk1.7及以上

## 相关说明
1.引入jar依赖
```xml
  <dependency>
    <groupId>com.guuidea.component</groupId>
    <artifactId>rate-limite</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
```
2.增加外部包扫描
```html
springBoot项目，在主启动类上增加@ComponentScan({"xxx.xxx","com.guuidea.component"})
注意：xxx.xxx为主启动类的地址，扫描外部包同时，需要将默认扫描包一同写上，不然项目中的bean不会加载到spring中。
```
```html
spring项目，在配置文件applicationContext.xml中增加
<context:component-scan base-package="x.y.z,com.guuidea.component" />
```
3.在需要接口上增加限流注解
```
@GdRateLimiter(number=100.0)
```
number为每秒创建的令牌数（QPS）如上100，即该接口1秒时间内最多100个并发请求。**如果不写number,默认QPS为1000**.

4.调整服务接口QPS限制（**选做**）
针对那种不想改代码修改QPS容量时，可以在配置文件中增加该接口限制
以下配置以springBoot为例
```yaml
rate:
  limit:
    path: "{ 'com.zsy.ratelimit.controller.RateLimitController.annotationLimit': '4.0'}"
```
字符串中冒号前为方法路径，冒号后为令牌数。
## 版本发布
- 1.0.0 提供限流功能

## 打包上传
1. 设置运行环境为Java7，满足一些JDK1.7的项目使用
2. 使用maven命令`deploy -Dmaven.test.skip=true`编译打包
