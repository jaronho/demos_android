package com.liyuu.strategy.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2017-07-06.
 */

public class AES {
    private final static String IVKEY = "bbc077ccff5c1ab8";

    public static String encrypt(String data, String key) {
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            SecretKeySpec keyspec = new SecretKeySpec(fullZore(key,blockSize), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(fullZore(IVKEY,blockSize));
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(fullZore(data,blockSize));
            return new String(Base64.encode(encrypted, Base64.DEFAULT)).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decrypt(String data, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            SecretKeySpec keyspec = new SecretKeySpec(fullZore(key,blockSize), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(fullZore(IVKEY,blockSize));
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] decrypted = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
            return new String(decrypted).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] fullZore(String data, int blockSize){
        byte[] dataBytes = data.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        return plaintext;
    }
}
