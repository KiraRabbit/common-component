
package com.guuidea.component.chrome.tool.common;



import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ASE加密工具类
 *
 * @Author: hzchendou
 * @Date: 2019-06-24 10:41
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public final class AESUtils {
    private static final Logger LOGGER = Logger.getLogger(AESUtils.class.getName());
    public static final String KEY_ALGORITHM = "AES";
    public static final String KEY_ALGORITHM_PADDING = "AES/CBC/PKCS5Padding";


    public static SecretKey getSecretKey(byte[] seed) throws Exception {
        SecureRandom secureRandom = new SecureRandom(seed);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(secureRandom);
        return keyGenerator.generateKey();
    }

    public static byte[] encrypt(byte[] data, byte[] encryptKey, byte[] iv) {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey, KEY_ALGORITHM);
        return encrypt(data, zeroIv, key);
    }

    public static byte[] decrypt(byte[] data, byte[] decryptKey, byte[] iv) {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey, KEY_ALGORITHM);
        return decrypt(data, zeroIv, key);
    }

    public static byte[] encrypt(byte[] data, IvParameterSpec zeroIv, SecretKeySpec keySpec) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, zeroIv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "AES encrypt ex, iv=%s, key=%s",
                    new String[] {Arrays.toString(zeroIv.getIV()),
                            Arrays.toString(keySpec.getEncoded())});
            throw new RuntimeException("AES encrypt ex", e);
        }
    }

    public static byte[] decrypt(byte[] data, IvParameterSpec zeroIv, SecretKeySpec keySpec) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, zeroIv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "AES decrypt ex, iv=%s, key=%s, data=%s",
                    new String[] {Arrays.toString(zeroIv.getIV()),
                            Arrays.toString(keySpec.getEncoded()),
                            Arrays.toString(data)});
            throw new RuntimeException("AES decrypt ex", e);
        }
    }
}
