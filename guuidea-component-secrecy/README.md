## 公共服务-加解密组件

### 功能

组件主要供服务端使用以对请求进行解密和响应进行加密。

### 使用方法
首先导入依赖
```xml
<dependency>
  <groupId>com.guuidea.component</groupId>
  <artifactId>secrecy</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

组件以过滤器的形式在请求到达时进行解密再重新封装，对响应及进行加密并重新封装加密，故使用者只需对过滤器进行配置：

```java
@Configuration
public class FilterConfig {
     @Bean
     public FilterRegistrationBean filterRegistration() {
     FilterRegistrationBean registration = new FilterRegistrationBean(new SecurityFilter());
     registration.addUrlPatterns("/*"); 
     registration.setName("securityFilter");
     return registration;
     }
}
```

创建过滤器对象时不传参数则是默认对请求校验时，静态资源请求不做处理直接返回，请求异常时返回500。若使用者希望使用自定义策略进行请求校验或自定义异常时返回的信息，请传入相关参数，如下：

```java
@Configuration
public class FilterConfig {
     @Bean
     public FilterRegistrationBean filterRegistration() {
     //重写校验策略
     RequestVerification rv = new RequestVerification(){
         @Override
         public Boolean requestPassed(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
             if(httpRequest.getHeader("token") == null){
                 /**
                  * 信息处理并写入httpResponse
                  **/
                 //表示校验未通过，直接返回
                 return Boolean.FALSE;
             }
             //表示校验通过，可进入下一步
             return Boolean.TRUE;
         }
     };
     
     //自定义请求失败时返回的信息
     byte[] bytes = "请求失败，请稍后重试".getBytes(StandardCharsets.UTF_8);
         
     FilterRegistrationBean registration = new FilterRegistrationBean(new SecurityFilter(rv, bytes));
     registration.addUrlPatterns("/*"); 
     registration.setName("securityFilter");
     return registration;
     }
}
```
通过new SecurityFilter(RequestVerification rv, byte[] bytes)创建对象时可传入null，为null则按默认处理。

### 环境

需提供Servlet 3.0和JDK1.7等环境以及Sl4j、tomcat.embed和alibaba-fastjason等依赖。

### 打包上传
1. 设置运行环境为Java7，满足一些JDK1.7的项目使用
2. 使用maven命令`deploy -Dmaven.test.skip=true`编译打包

### 注意事项

因为协议是对请求体中的数据流进行解密，所以使用者应避免使用get等无法携带流的请求，建议统一使用post请求。

协议原理请前往[Confluence](http://confluence.guuidea.com:8090/pages/viewpage.action?pageId=2457662)查看



