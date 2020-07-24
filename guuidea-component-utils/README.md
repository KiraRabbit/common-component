## 工具类组件
### 功能
提供一些常用的工具类，为开发人员节省时间

### 环境配置
`JDK1.7`及以上和`commons-lang3`、`commons-codec`依赖

### 打包上传
1. 设置运行环境为Java7，满足一些JDK1.7的项目使用
2. 使用maven命令`deploy -Dmaven.test.skip=true`编译打包

### 现有工具类
- AESUtils 

  AES加解密工具类，提供对byte数组类型数据的加密和解密。
- IOUtils
  
  IO工具类，提供BASE64编码的`InputStream->String`和BASE64解码的`String->InputStream`。
  
- RegxUtils

  正则表达式工具类，判断字符串是否以英文或数字开头并且只包含英文、数字或`_·-`三种符号。
  
- StreamUtil
 
  流工具类，提供将InputStream转为byte数组的方法。
  
- TimeFormatterUtils

  时间格式化工具类，将Long类型的时间数转为`yyyy-MMM-dd HH:mm:ss`格式的字符串。
  
- UUIDUtils

  UUID工具类，生成UUID并去除其中的`-`。 
  
