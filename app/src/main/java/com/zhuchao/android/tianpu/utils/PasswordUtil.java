package com.zhuchao.android.tianpu.utils;

import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by Oracle on 2017/12/13.
 */

public class PasswordUtil {

    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            cs[i + 1] = salt.charAt(i / 3);
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes("UTF-8"));
            return new String(Hex.encode(bs, bs.length));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs2[i / 3] = md5.charAt(i + 1);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }
}
