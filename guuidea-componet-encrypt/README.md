
## 公共服务-可逆加密，不可逆加密

### 功能

组件主要提供各种加解密的方法供开发人员使用

### 使用方法
首先导入依赖
```xml
<dependency>
  <groupId>com.guuidea.component</groupId>
  <artifactId>encrypt</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```


### 环境

JDK1.7，JDK1.8均可使用。


| 限定符和类型              | 方法和说明                                                   |
| :------------------------ | :----------------------------------------------------------- |
| `static java.lang.Object` | `dataMask(java.lang.String data)`脱敏,data长度小于等于3位,将返回400,脱敏异常 data长度大于3位,默认会从下标3开始脱敏 eg:dsadsadsadsada 脱敏后:dsa**********a 返回:{code=200, dataMasking=dsa**********a, encrypt=fTZXUDJUIRBVXGhAe1k=} |
| `static java.lang.Object` | `dataMask(java.lang.String data, java.lang.Integer begin, java.lang.Integer end)`指定位置脱敏,脱敏将字符串开始位置到结束位置之间的字符用指定字符替换 |
| `static java.lang.Object` | `dataMask(java.lang.String data, java.lang.String dataType)`根据类型脱敏. |
| `static byte[]`           | `generateBytes()`随机生成 byte[] 40位长度                    |
| `static java.lang.String` | `generateDigest(java.lang.String text, java.lang.String salt)`生成摘要 |
| `static java.lang.String` | `generateDigestOfBytes(byte[] bytes)`生成摘要                |
| `static byte[]`           | `generateDigestOfBytes(byte[] bytes, java.lang.String salt)`生成摘要 |
| `static java.lang.String` | `generateSalt()`生成salt 默认16位长度                        |
| `static java.lang.String` | `generateSalt(java.lang.Integer num)`生成salt                |
| `static java.lang.String` | `generateSalt(java.lang.String text)`生成salt                |
| `static byte[]`           | `getEncryptVal()`                                            |
| `static void`             | `main(java.lang.String[] args)`                              |
| `static void`             | `setEncryptVal(byte[] encryptVal)`                           |
| `static java.lang.Object` | `undoDataMask(java.lang.String data)`反脱敏,根据传入密文进行反脱敏 |





  ### 方法详细资料

  

  - #### getEncryptVal

    ```
    public static byte[] getEncryptVal()
    ```

  

  - #### setEncryptVal

    ```
    public static void setEncryptVal(byte[] encryptVal)
    ```

  

  - #### generateBytes

    ```
    public static byte[] generateBytes()
    ```

    随机生成 byte[] 40位长度

    - **返回:**

      byte[]

  

  - #### generateSalt

    ```
    public static java.lang.String generateSalt()
    ```

    生成salt 默认16位长度

    - **返回:**

      salt

  

  - #### generateSalt

    ```
    public static java.lang.String generateSalt(java.lang.Integer num)
    ```

    生成salt

    - **参数:**

      `num` - 生成salt的位置

    - **返回:**

      salt

  

  - #### generateSalt

    ```
    public static java.lang.String generateSalt(java.lang.String text)
    ```

    生成salt

    - **参数:**

      `text` - 根据文本生成

    - **返回:**

      salt

  

  - #### generateDigest

    ```
    public static java.lang.String generateDigest(java.lang.String text,
                                                  java.lang.String salt)
    ```

    生成摘要

    - **参数:**

      `text` - 文本

      `salt` - 盐

    - **返回:**

      String 摘要

  

  - #### generateDigestOfBytes

    ```
    public static java.lang.String generateDigestOfBytes(byte[] bytes)
    ```

    生成摘要

    - **参数:**

      `bytes` - 字节流

    - **返回:**

      String 摘要

  

  - #### generateDigestOfBytes

    ```
    public static byte[] generateDigestOfBytes(byte[] bytes,
                                               java.lang.String salt)
    ```

    生成摘要

    - **参数:**

      `bytes` - 字节流

      `salt` - 盐

    - **返回:**

      String 摘要

  

  - #### dataMask

    ```
    public static java.lang.Object dataMask(java.lang.String data,
                                            java.lang.String dataType)
    ```

    根据类型脱敏. dataType目前支持下几类: 身份证: "id_card_number"; 15位或18位 手机号: "phone_number"; 11位 银行卡卡号 "bank_card_number"; 鉴于各个银行的卡号长度可能存在不一样,目前校验长度10位,直接脱敏data 邮箱 "email"; 邮箱检验,保留邮箱第1,2个及@前 最后一个字母

    - **参数:**

      `data` - 明文

      `dataType` - 脱敏类型

    - **返回:**

      Object code:200(成功) code:400(失败) encrypt:密文 dataMasking:脱敏数据

  

  - #### dataMask

    ```
    public static java.lang.Object dataMask(java.lang.String data,
                                            java.lang.Integer begin,
                                            java.lang.Integer end)
    ```

    指定位置脱敏,脱敏将字符串开始位置到结束位置之间的字符用指定字符替换

    - **参数:**

      `data` - 待处理字符串

      `begin` - 开始位置

      `end` - 结束位置

    - **返回:**

      Object

  

  - #### dataMask

    ```
    public static java.lang.Object dataMask(java.lang.String data)
    ```

    脱敏,data长度小于等于3位,将返回400,脱敏异常 data长度大于3位,默认会从下标3开始脱敏 eg:dsadsadsadsada 脱敏后:dsa**********a 返回:{code=200, dataMasking=dsa**********a, encrypt=fTZXUDJUIRBVXGhAe1k=}

    - **参数:**

      `data` - 明文

    - **返回:**

      Object 成功:code=200 失败:code=400 dataMasking 脱敏数据 encrypt 密文

  

  - #### undoDataMask

    ```
    public static java.lang.Object undoDataMask(java.lang.String data)
    ```

    反脱敏,根据传入密文进行反脱敏

    - **参数:**

      `data` - 密文

    - **返回:**

      Object 成功:code=200 失败:code=400 decrypt 反脱敏明文

  

  - #### main (用于测试)

    ```
    public static void main(java.lang.String[] args)
                     throws java.io.IOException
    ```

    - **抛出:**

      `java.io.IOException`