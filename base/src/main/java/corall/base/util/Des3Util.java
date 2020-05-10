package corall.base.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by xxj on 2018/7/4.
 */

public class Des3Util {

    /**
     * 定义加密算法，有DES、DESede(即3DES)、Blowfish
     */
    private static final String Algorithm = "DESede";

    public static String encrypt(String src, String key) {
        //加密
        byte[] secretArr = encryptMode(src.getBytes(), key);
        return new String(Base64.encode(secretArr));
    }

    public static String decrypt(String src, String key) {
        byte[] myMsgArr = decryptMode(Base64.decode(src), key);
        return new String(myMsgArr);
    }

    /**
     * 加密方法
     *
     * @param src 源数据的字节数组
     * @return
     */
    public static byte[] encryptMode(byte[] src, String key) {
        try {
            // 生成密钥
            SecretKey desKey = new SecretKeySpec(build3DesKey(key), Algorithm);
            // 实例化负责加密/解密的Cipher工具类
            Cipher c1 = Cipher.getInstance(Algorithm);
            // 初始化为加密模式
            c1.init(Cipher.ENCRYPT_MODE, desKey);
            return c1.doFinal(src);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }


    /**
     * 解密函数
     *
     * @param src 密文的字节数组
     * @return
     */
    public static byte[] decryptMode(byte[] src, String key) {
        try {
            SecretKey desKey = new SecretKeySpec(build3DesKey(key), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            // 初始化为解密模式
            c1.init(Cipher.DECRYPT_MODE, desKey);
            return c1.doFinal(src);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }


    /**
     * 根据字符串生成密钥字节数组
     *
     * @param keyStr 密钥字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        //声明一个24位的字节数组，默认里面都是0
        byte[] key = new byte[24];
        //将字符串转成字节数组
        byte[] temp = keyStr.getBytes(StandardCharsets.UTF_8);

        /*
         * 执行数组拷贝
         * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
         */
        // 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
        // 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
        System.arraycopy(temp, 0, key, 0, Math.min(key.length, temp.length));
        return key;
    }

}
