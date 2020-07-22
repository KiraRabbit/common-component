# IP解析项目说明文档

## 介绍

一个IP解析工具包，具体作用就是将数据库中的IP数据查询出来并保存，然后将传入的IP进行地址解析。

## 项目环境依赖
### Windows下环境

[![java](https://img.shields.io/badge/java-1.7-color.svg?style=flat-square)](https://www.oracle.com/technetwork/java/javase/downloads/index.html)

### Centos下环境

[![java](https://img.shields.io/badge/java-1.7-color.svg?style=flat-square)](https://www.oracle.com/technetwork/java/javase/downloads/index.html)


## 编译说明

1. 使用maven命令编译打包
2. 打包后在/target文件夹下生成同项目名带版本号的jar包

## 使用说明

1.通过maven导入项目中
2.在项目启动时需要调用IpUtil.init()方法进行初始化，方法的最后一个参数用来判断初始化国内IP库还是海外IP库，国内为参数为true，海外为false。
3.使用时通过调用ip2Long()方法计算出IP的IP数值，然后调用getUserLocationByIp()方法获取该IP数值的地区对象


