package com.guuidea.component.encrypt;


import com.guuidea.component.common.DataMaskingConstants;
import com.guuidea.component.utils.Base64Util;
import com.guuidea.component.utils.EncryptUtil;
import com.guuidea.component.utils.MD5Helper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
 * 摘要、文本加解密、流加解密
 * 摘要：md5的salt业务方直接保存
 * 加密：异或位加密向量可自定义 + base64码表可自定义
 * @author Gallen.Chu
 */
public class SecurityEncryption {

    /**
     * 异或位加密向量维护
     */
    private final static byte[] ENCRYPT_VAL = {
            25, 69, 54, 52, 65, 53, 69, 99,
            52, 56, 27, 33, 31, 56, 56, 69,
            38, 48, 70, 55, 70, 50, 49, 51,
            65, 52, 77, 69, 54, 65, 54, 26,
            35, 52, 88, 22, 54, 11, 56, 40
    };

    /**
     * 生成salt 默认16位长度
     *
     * @return salt
     */
    public static String generateSalt() {
        return generateSalt(16);
    }


    /**
     * 生成salt
     *
     * @param num 生成salt的位置
     * @return salt
     */
    public static String generateSalt(Integer num) {
        //num 位数
        return RandomStringUtils.randomAlphanumeric(num);
    }

    /**
     * 生成salt
     *
     * @param text 根据文本生成
     * @return salt
     */
    public static String generateSalt(String text) {
        return MD5Helper.encrypt16(text);
    }


    /**
     * 生成摘要
     *
     * @param text 文本
     * @param salt 盐
     * @return String 摘要
     */
    public static String generateDigest(String text, String salt) {

        // 生成最终的加密盐
        text = EncryptUtil.md5AndSha(text + salt);

        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = text.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = text.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }


    /**
     * 生成摘要
     *
     * @param text 文本
     * @return String 摘要
     */
    //todo  delete
    private static String generateDigest(String text) {
        String salt = generateSalt();
        text = EncryptUtil.md5AndSha(text + salt);

        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = text.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = text.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }


    /**
     * 生成摘要
     *
     * @param bytes 字节流
     * @return String 摘要
     */

    public static String generateDigestOfBytes(byte[] bytes) {
        String text = new String(bytes, Charset.forName("UTF-8"));
        String salt = generateSalt();
        return EncryptUtil.md5AndSha(text + salt);
    }

    /**
     * 生成摘要
     *
     * @param bytes 字节流
     * @param salt  盐
     * @return String 摘要
     */
    public static byte[] generateDigestOfBytes(byte[] bytes, String salt) {
        String text = new String(bytes, Charset.forName("UTF-8"));
        text = EncryptUtil.md5AndSha(text + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = text.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = text.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs).getBytes();
    }


    /**
     * 加密文本：bitEncode + base64
     *
     * @param bytes 明文（明文）
     * @return 密文（密文）
     */
    static byte[] encryptStream(byte[] bytes) {
        int len, i;
        byte[] wen;
        len = bytes.length;
        wen = new byte[len];
        for (i = 0; i < len; i++) {
            wen[i] = (byte) (bytes[i] ^ ENCRYPT_VAL[i % ENCRYPT_VAL.length]);
        }
        return wen;
    }

    /**
     * 加密文本：bitEncode + base64
     *
     * @param bytes 明文（密文）
     * @return 密文（明文）
     */
    static byte[] dencryptStream(byte[] bytes) {
        return encryptStream(bytes);
    }

    /**
     * 加密文本：bitEncode + base64
     *
     * @param str 明文（明文）
     * @return 密文（密文）
     */
    static String encryptText(String str) {
        byte[] bytes = str.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ ENCRYPT_VAL[i % ENCRYPT_VAL.length]);
        }
        return Base64Util.encodeBytes(bytes);
    }

    /**
     * 解密文本：bitEncode + base64
     *
     * @param str 明文（密文）
     * @return 密文（明文）
     */
    static String decryptText(String str) {
        byte[] bytes = new byte[0];
        try {
            bytes = Base64Util.decodeString(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ ENCRYPT_VAL[i % ENCRYPT_VAL.length]);
        }
        return new String(bytes);
    }


    /**
     * 验证加盐后是否和原密码一致
     *
     * @param text   明文
     * @param md5str 密文
     * @return boolean true表示一致   false表示不一致
     */
    private static boolean verifyDigest(String text, String md5str) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5str.charAt(i);
            cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
            cs2[i / 3] = md5str.charAt(i + 1);
        }
        String salt = new String(cs2);
        String encryptText = EncryptUtil.md5AndSha(text + salt);

        encryptText = encryptText.substring(0, encryptText.length() - 8);

        return encryptText.equals(String.valueOf(cs1));
    }

    /**
     * 脱敏
     *
     * @param data     明文
     * @param dataType 脱敏类型
     * @return Object code:200(成功) code:400(失败)   encrypt:密文 dataMasking:脱敏数据
     */
    public static Object dataMask(String data, String dataType) {
        String encryptText = "";
        Map<String, Object> result = new HashMap<>();

        try {
            switch (dataType) {
                case DataMaskingConstants.ID_CARD_NUMBER:
                    if (data.length() == 15) {
                        encryptText = encryptText(data);
                        data = data.replaceAll("(\\w{6})\\w*(\\w{3})", "$1******$2");
                    }
                    if (data.length() == 18) {
                        encryptText = encryptText(data);
                        data = data.replaceAll("(\\w{6})\\w*(\\w{3})", "$1*********$2");
                    }
                    break;
                case DataMaskingConstants.PHONE_NUMBER:
                    if (data.length() == 11) {
                        encryptText = encryptText(data);
                        data = data.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    }
                    break;
                case DataMaskingConstants.BANK_CARD_NUMBER:
                    encryptText = encryptText(data);

                    if (data == null) {
                        return "";
                    }
                    data = replaceBetween(data, 6, data.length() - 4, null);
                    break;
                case DataMaskingConstants.EMAIL:
                    encryptText = encryptText(data);
                    data = data.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2");
                    break;
                default:
                    if (data.length() > 3) {
                        encryptText = encryptText(data);
                        data = replaceBetween(data, 3, data.length() - 1, null);
                    } else {
                        result.put("code", 400);
                        result.put("encrypt", "error");
                    }
                    break;

            }
            result.put("code", 200);
            result.put("encrypt", encryptText);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("encrypt", "error");
        }

        result.put("dataMasking", data);
        return result;

    }


    /**
     * 将字符串开始位置到结束位置之间的字符用指定字符替换
     *
     * @param data  待处理字符串
     * @param begin 开始位置
     * @param end   结束位置
     * @return Object
     */
    //todo 3.自定义脱敏 4. 自定义码表
    public static Object dataMask(String data, Integer begin, Integer end) {
        Map<String, Object> result = new HashMap<>();

        if (begin <= end && data.length() > end) {
            String dataMasking = replaceBetween(data, begin, end, "*");
            String encrypt = encryptText(data);
            result.put("code", 200);
            result.put("dataMasking", dataMasking);
            result.put("encrypt", encrypt);
            return result;
        }
        result.put("code", 400);
        result.put("msg", "脱敏异常");
        return result;
    }

    /**
     * 脱敏
     *
     * @param data 明文
     * @return Object 成功:code=200 失败:code=400 dataMasking 脱敏数据 encrypt 密文
     */
    public static Object dataMask(String data) {
        Map<String, Object> result = new HashMap<>();
        if (data.length()>3){
            String encryptText = encryptText(data);
            data = replaceBetween(data, 3, data.length() - 1, null);
            result.put("code", 200);
            result.put("dataMasking", data);
            result.put("encrypt", encryptText);
            return result;
        }

        result.put("code", 400);
        result.put("msg", "脱敏异常");
        result.put("dataMasking", data);
        return result;
    }

    /**
     * 反脱敏
     *
     * @param data 密文
     * @return Object 成功:code=200 失败:code=400 decrypt 反脱敏明文
     */
    public static Object undoDataMask(String data) {
        Map<String, Object> result = new HashMap<>();
        if (data == null) {
            result.put("code", 400);
            return result;
        }
        String decryptData = decryptText(data);
        result.put("code", 200);
        result.put("decrypt", decryptData);
        return result;

    }


    /**
     * 将字符串开始位置到结束位置之间的字符用指定字符替换
     *
     * @param sourceStr   待处理字符串
     * @param begin       开始位置
     * @param end         结束位置
     * @param replacement 替换字符
     * @return
     */
    private static String replaceBetween(String sourceStr, Integer begin, Integer end, String replacement) {
        if (sourceStr == null) {
            return "";
        }
        if (replacement == null) {
            replacement = "*";
        }
        int replaceLength = end - begin;
        if (StringUtils.isNotBlank(sourceStr) && replaceLength > 0) {
            StringBuilder sb = new StringBuilder(sourceStr);
            sb.replace(begin, end, StringUtils.repeat(replacement, replaceLength));
            return sb.toString();
        } else {
            return sourceStr;
        }
    }


    private static void test() {
        Map<String, String> id_card_number = (Map<String, String>) dataMask("340111119606060026", "id_card_number");
        System.out.println("身份证脱敏 : "+id_card_number);
        System.out.println("身份证解密 : " + decryptText(id_card_number.get("encrypt")));


        Map<String, String> phone_number = (Map<String, String>) dataMask("17621205270", "phone_number");
        System.out.println("手机号脱敏 : " +dataMask("17621205270", "phone_number"));
        System.out.println("手机号解密 : " + decryptText(phone_number.get("encrypt")));


        Map<String, String> bank_card_number = (Map<String, String>) dataMask("6223123456781230", "bank_card_number");
        System.out.println("银行卡脱敏 : " +bank_card_number);
        System.out.println("银行卡解密 : " + decryptText(bank_card_number.get("encrypt")));


        Map<String, String> email = (Map<String, String>) dataMask("2231876567@qq.com", "email");
        System.out.println("邮箱脱敏 : "+email);
        System.out.println("邮箱解密 : " + decryptText(email.get("encrypt")));
    }

    private static void test2() throws IOException {
        // 明文
        String text = "123456";
        // 获取加盐后的MD5值
        String ciphertext = generateDigest(text);
        System.out.println("加盐后MD5：" + ciphertext);
        System.out.println("是否是同一字符串:" + verifyDigest(text, ciphertext));


        System.out.println("文件流加密");
        File file = new File("/Users/guilong/Desktop/IntelliJIDEAKEYMAP.pdf");
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(generateDigestOfBytes(data));

        System.out.println("-------------------------------");
        String str, miwen, jiemi;
        int i;
        char n;
        String go;
        System.out.println("位加密解密算法演示！");
        Scanner input = new Scanner(System.in);
        do {
            System.out.print("请输入明文：");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            str = br.readLine();
            System.out.print("明文为：");
            System.out.print(str);
            System.out.println();

            long start1 = System.currentTimeMillis();

            System.out.println(start1);
            miwen = encryptText(str);
            System.out.println("耗时：" + (System.currentTimeMillis() - start1));

            System.out.print("密文为：");
            System.out.print(miwen);
            System.out.println();


            jiemi = decryptText(miwen);
            System.out.print("解密为：");
            System.out.print(jiemi);
            System.out.println();

            System.out.print("是否继续(y/n):");
            go = input.next();
        } while (go.equalsIgnoreCase("y"));
        System.out.println("退出程序！");
    }


    public static void main(String[] args) throws IOException {

        System.out.println(dataMask("1222"));
        Map<String, Object> map = (Map<String, Object>) dataMask("1222");
        System.out.println(undoDataMask((String) map.get("encrypt")));
        System.out.println(dataMask("dsadsadsadsada", 1, 4));
        test();
    }

}