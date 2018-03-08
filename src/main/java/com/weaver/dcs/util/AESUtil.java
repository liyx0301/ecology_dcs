package com.weaver.dcs.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {

    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 初始化密钥
     */
    public static byte[] initSecretKey(String code) {
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(code.getBytes());
            kg.init(128, secureRandom);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 转换密钥
     */
    private static Key toKey(byte[] key) {
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    /**
     * 加密&解密
     */
    public static void encryptAndDecrypt(String sourcePath, String targetPath, String code, String type)
            throws Exception {
        InputStream in = null;
        CipherInputStream cis = null;
        OutputStream out = null;
        try
        {
            in = new FileInputStream(new File(sourcePath));
            byte[] key = initSecretKey(code);
            Key k = toKey(key);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            if(type.equals("1"))
            {
                cipher.init(Cipher.ENCRYPT_MODE, k);// 初始化
            }
            else
            {
                cipher.init(Cipher.DECRYPT_MODE, k);// 初始化
            }
            cis = new CipherInputStream(in, cipher);
            out = new FileOutputStream(targetPath);

            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }
        finally {
            if(in != null)
            {
                in.close();
            }
            if(cis != null)
            {
                cis.close();
            }
            if(out != null)
            {
                out.close();
            }
            File file = new File(sourcePath);
            file.delete();
        }
    }
}