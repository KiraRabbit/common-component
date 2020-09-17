package com.guuidea.component.encrypt;



import com.guuidea.component.utils.EncryptUtil;
import com.guuidea.component.utils.MD5Helper;
import com.guuidea.component.utils.Base64Util;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.nio.charset.Charset;
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
    public static String generateDigest(String text,String salt) {

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
    public static String generateDigest(String text) {
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
     * @param salt 盐
     * @return String 摘要
     */
    public static byte[] generateDigestOfBytes(byte[] bytes,String salt) {
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
     * @param bytes 明文（明文）
     * @return 密文（密文）
     */
    static byte[] encryptStream(byte[] bytes){
        int len,i;
        byte[] wen;
        len=bytes.length;
        wen = new byte[len];
        for(i=0; i<len; i++){
            wen[i] = (byte) (bytes[i]^ENCRYPT_VAL[i % ENCRYPT_VAL.length]);
        }
        return wen;
    }

    /**
     * 加密文本：bitEncode + base64
     * @param bytes 明文（密文）
     * @return 密文（明文）
     */
    static byte[] dencryptStream(byte[] bytes){
        return encryptStream(bytes);
    }

    /**
     * 加密文本：bitEncode + base64
     * @param str 明文（明文）
     * @return 密文（密文）
     */
    static String encryptText(String str){
        byte[] bytes = str.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ ENCRYPT_VAL[i % ENCRYPT_VAL.length]);
        }
        return Base64Util.encodeBytes(bytes);
    }

    /**
     * 解密文本：bitEncode + base64
     * @param str 明文（密文）
     * @return 密文（明文）
     */
    static String decryptText(String str){
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
    public static boolean isVerify(String text, String md5str) {
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



    public static void main(String[] args) throws IOException {


        // 明文
        String text = "123456";
        // 获取加盐后的MD5值
        String ciphertext = generateDigest(text);
        System.out.println("加盐后MD5：" + ciphertext);
        System.out.println("是否是同一字符串:" + isVerify(text, ciphertext));


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
        String str,miwen,jiemi;
        int i;
        char n;
        String go;
        System.out.println("位加密解密算法演示！");
        Scanner input = new Scanner(System.in);
        do{
            System.out.print("请输入明文：");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            str=br.readLine();
            System.out.print("明文为：");
            System.out.print(str);
            System.out.println();

            long start1 = System.currentTimeMillis();

            System.out.println(start1);
            miwen=encryptText(str);
            System.out.println("耗时：" + (System.currentTimeMillis() - start1));

            System.out.print("密文为：");
            System.out.print(miwen);
            System.out.println();


            jiemi= decryptText(miwen);
            System.out.print("解密为：");
            System.out.print(jiemi);
            System.out.println();

            System.out.print("是否继续(y/n):");
            go = input.next();
        }while(go.equalsIgnoreCase("y"));
        System.out.println("退出程序！");

    }

}