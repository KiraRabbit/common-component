
package com.guuidea.component.chrome.tool.common;

import static com.guuidea.component.chrome.tool.common.AESUtils.KEY_ALGORITHM;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * AES加密套件
 *
 * @Author: hzchendou
 * @Date: 2019-06-24 09:29
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public final class AesCipher {

    public static AesCipher cipher;
    public final byte[] key;
    public final byte[] iv;
    private final IvParameterSpec zeroIv;
    private final SecretKeySpec keySpec;


    /**
     * 初始化
     *
     * @param k1
     * @param k2
     */
    public static void init(String k1, String k2) {
        Base64.Decoder decoder = Base64.getDecoder();
        if (cipher == null) {
            cipher = new AesCipher(decoder.decode(k1), decoder.decode(k2));
        }
    }

    public AesCipher(byte[] key, byte[] iv) {
        this.key = key;
        this.iv = iv;
        this.zeroIv = new IvParameterSpec(iv);
        this.keySpec = new SecretKeySpec(key, KEY_ALGORITHM);
    }


    public byte[] encrypt(byte[] data) {
        return AESUtils.encrypt(data, zeroIv, keySpec);
    }

    /**
     * 数据加密
     *
     * @param data
     * @return
     */
    public String encryptToString(String data) {
        byte[] datas = data.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(encrypt(datas));
    }

    /**
     * 解密数据
     *
     * @param data
     * @return
     */
    public String decodeToString(String data) {
        byte[] datas =Base64.getDecoder().decode(data);
        return new String(decrypt(datas), StandardCharsets.UTF_8);
    }

    public byte[] decrypt(byte[] data) {
        return AESUtils.decrypt(data, zeroIv, keySpec);
    }

    @Override
    public String toString() {
        return toString(key) + ',' + toString(iv);
    }

    public String toString(byte[] a) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            if (i != 0) b.append('|');
            b.append(a[i]);
        }
        return b.toString();
    }

    public static byte[] toArray(String str) {
        String[] a = str.split("\\|");
        byte[] bytes = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            bytes[i] = Byte.parseByte(a[i]);
        }
        return bytes;
    }

    /**
     * 获取密钥值
     *
     * @return
     */
    public static String[] genAesKey() {
        SecureRandom random = new SecureRandom();
        byte[] k1 = random.generateSeed(16);
        byte[] k2 = random.generateSeed(16);
        Base64.Encoder encoder = Base64.getEncoder();
        return new String[]{encoder.encodeToString(k1), encoder.encodeToString(k2)};
    }
}
