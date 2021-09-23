package com.zhoufu.ssoauthserver.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Author: zhoufu
 * @Date: 2021/2/23 14:32
 * @description:
 */
public class EncryptUtil {
    public static final String SALT = "1io10fdgadfjvower389fhday29834aguourfwpg0w82dllfkfadf";

    public static final String AES_SALT = "g^&*g%^F766R&PIpGY&%yg%yt$^RyfU&UT*ugyTR^R^uf&&";

    /**
     * AES 加密
     * @param content 加密明文
     * @return 密文
     */
    public static String AESEncode(String content){
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128, new SecureRandom(AES_SALT.getBytes()));
            SecretKey secretKey = keygen.generateKey();
            byte[] encoded = secretKey.getEncoded();
            SecretKey aeskey = new SecretKeySpec(encoded, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aeskey);

            byte[] byte_encode = content.getBytes("UTF-8");
            byte[] byte_AES = cipher.doFinal(byte_encode);
            return new BASE64Encoder().encode(byte_AES);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  AES 解密
     * @param content  密文
     * @return  明文
     */
    public static String AESDecode(String content){
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128, new SecureRandom(AES_SALT.getBytes()));
            SecretKey secretkey = keygen.generateKey();
            byte[] encoded = secretkey.getEncoded();

            SecretKey aeskey = new SecretKeySpec(encoded, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aeskey);

            byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
            byte[] byte_decode = cipher.doFinal(byte_content);

            return new String(byte_decode, "utf-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
