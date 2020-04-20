package com.zhuchao.android.tianpu.utils;

/**
 * Created by Oracle on 2017/12/13.
 */

public class Hex {

    public static String encode(byte[] b, int len) {
        StringBuilder sb = new StringBuilder();
        String stmp = null;
        for (int i = 0; i < len; i++) {
            stmp = Integer.toHexString(b[i] & 0xFF);
            if (stmp.length() == 1) {
                sb.append("0").append(stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString().toUpperCase();
    }
}
