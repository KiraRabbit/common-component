## 统一入口日志打印组件

### 功能
为了方便日志的记录，提供过滤器对请求信息和响应信息进行统一的日志打印，包括uri、请求参数、请求体、耗时以及响应结果，
其中响应结果可选择不打印或只打印一定长度。

### 环境配置
`JDK1.7`及以上和slf4j相关依赖

## 打包上传
1. 设置运行环境为Java7，满足一些JDK1.7的项目使用
2. 使用maven命令`deploy -Dmaven.test.skip=true`编译打包

### 使用说明
导入依赖
```xml
<dependency>
  <groupId>com.guuidea.component</groupId>
  <artifactId>log</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

只需要在项目中配置过滤器即可，这里以spring boot项目为例：
```java
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new LogFilter(Boolean.TRUE, 1000));
        registration.addUrlPatterns("/api/*");
        registration.setName("logFilter");
        return registration;
    }
}
```
`new LogFilter(Boolean.TRUE, 1000)`中第一个参数表示是否打印结果，若填true则打印，并且第二个参数限制打印的字符数。
打印的结果示例如下：
```text
uri:{/api/v1/notice/app/template/query}, params:{appId:,offset:0,pageSize:10}, requestBody:{}, time:233ms
```


