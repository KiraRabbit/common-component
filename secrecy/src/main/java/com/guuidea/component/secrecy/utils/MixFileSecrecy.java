package com.guuidea.component.secrecy.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加解密工具类
 *
 * @author chenghao.zhou
 * @date 2020-4-9
 */
public class MixFileSecrecy {
    private static final Logger logger = LoggerFactory.getLogger(MixFileSecrecy.class);

    //密钥
    private static final int ENCRYPT_KEY = 159354208;

    /**
     * 对字节流加密并写入输出流中
     *
     * @param bytes
     * @return
     */
    public static byte[] getEncryptBytes(byte[] bytes) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        SecrecyOutputStream sout = null;
        try {
            sout = new SecrecyOutputStream(bout, ENCRYPT_KEY);
            sout.write(bytes);
        } catch (IOException e) {
            logger.error("Encrypt outputstream fails:", e);
            e.printStackTrace();
        } finally {
            try {
                sout.close();
            } catch (IOException e) {
                logger.error("Encrypt outputstream fails:", e);
            }
        }
        return bout.toByteArray();
    }

    /**
     * 对输入流解密并写入输出流
     *
     * @param in
     * @return
     */
    public static byte[] getDecryptBytes(InputStream in) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        SecrecyInputStream sin;
        try {
            sin = new SecrecyInputStream(in, ENCRYPT_KEY);
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = sin.read(b)) != -1) {
                bout.write(b, 0, len);
            }

        } catch (IOException e) {
            logger.error("Decrpt inputstream fails:", e);
        }

        return bout.toByteArray();
    }

    /**
     * 获取映射表map中的映射值
     *
     * @param map
     * @param value
     * @return
     */
    private static int mapping(Map<Integer, Integer> map, int value) {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            return value;
        }
    }

    /**
     * 根据随机数种子生成byte映射表
     *
     * @param value 随机数种子
     * @return
     */
    private static Map<Integer, Integer> getMapping(int value) {
        //byte映射表，用于对byte[]的值进行转换，实现加密
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        Random random = new Random(value);

        List<Integer> charList = new ArrayList<Integer>();

        //将byte的所有值添加到list中
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            if (i != -1) {
                charList.add((int) i);
            }
        }

        charList.add((int) Byte.MAX_VALUE);

        //将byte的值存入map，一对值互为key-value
        while (charList.size() > 1) {
            int index = random.nextInt(charList.size() - 1) + 1;
            map.put(charList.get(0), charList.get(index));
            map.put(charList.get(index), charList.get(0));
            charList.remove(index);
            charList.remove(0);
        }

        if (charList.size() != 0) {
            map.put(charList.get(0), charList.get(0));
        }

        return map;
    }


    /**
     * 解密后的输入流
     */
    public static class SecrecyInputStream extends InputStream {
        private InputStream stream;
        private Map<Integer, Integer> byteMap;

        //获取byte值映射map，即解密map
        public SecrecyInputStream(InputStream in, int value) throws IOException {
            stream = in;
            DataInputStream dataOut = new DataInputStream(in);
            int v = dataOut.readInt() ^ value;
            byteMap = getMapping(v);
        }

        /**
         * 根据解密map对byte[]的元素进行转换，完成解密
         *
         * @param b
         * @param off
         * @param len
         * @return
         * @throws IOException
         */
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int l = stream.read(b, off, len);

            for (int i = off; i < l; i++) {
                b[i] = (byte) mapping(byteMap, b[i]);
            }

            return l;
        }


        @Override
        public int read() throws IOException {
            return mapping(byteMap, stream.read());
        }
    }


    /**
     * 加密后的输出流
     */
    public static class SecrecyOutputStream extends OutputStream {
        private OutputStream stream;
        private Map<Integer, Integer> byteMap;

        public SecrecyOutputStream(OutputStream out, int value) throws IOException {
            stream = out;
            DataOutputStream dataOut = new DataOutputStream(out);
            int v = (int) System.nanoTime();
            byteMap = getMapping(v);
            v = value ^ v;
            dataOut.writeInt(v);
        }

        /**
         * 对byte[]加密并写入输出流
         *
         * @param b
         * @param off
         * @param len
         * @throws IOException
         */
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            for (int i = off; i < len; i++) {
                b[i] = (byte) mapping(byteMap, b[i]);
            }

            stream.write(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
            stream.write(mapping(byteMap, b));
        }


    }
}
